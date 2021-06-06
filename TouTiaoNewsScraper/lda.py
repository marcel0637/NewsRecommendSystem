# coding=UTF-8
import jieba, os, re
import warnings

warnings.filterwarnings('ignore')
from gensim import corpora, models, similarities
from redis_utils import save_news_topic, save_topic_news, save_cat_news, save_all_news, save_topic_word
from mysql_utils import save_news_topic_mysql
from common import getConfigValue, logPrint

topic_limits = int(getConfigValue('topicLimits'))
topic_start = int(getConfigValue('topicStart'))
topic_step = int(getConfigValue('topicStep'))
topic_top_count = int(getConfigValue('topicTopCount'))
mallet_path = 'mallet-2.0.8/bin/mallet'  # update this path

"""创建停用词列表"""


def stopwordslist():
    stopwords = [line.strip() for line in open('./stopwords.txt', encoding='UTF-8').readlines()]
    return stopwords


"""对句子进行中文分词"""


def seg_depart(sentence):
    sentence_depart = jieba.cut(sentence.strip())
    stopwords = stopwordslist()
    outstr = ''
    for word in sentence_depart:
        if word not in stopwords:
            outstr += word
            outstr += " "
    # outstr：'黄蜂 湖人 首发 科比 带伤 战 保罗 加索尔 ...'
    return outstr


"""去除非汉字字符"""


def handle_sentence(sentence):
    sentence = re.sub(r'[^\u4e00-\u9fa5]+', '', sentence)
    sentence_seg = seg_depart(sentence.strip())
    return sentence_seg


"""
    Compute c_v coherence for various number of topics

    Parameters:
    ----------
    dictionary : Gensim dictionary
    corpus : Gensim corpus
    texts : List of input texts
    limit : Max num of topics

    Returns:
    -------
    model_list : List of LDA topic models
    coherence_values : Coherence values corresponding to the LDA model with respective number of topics
"""


def compute_coherence_values(dictionary, corpus, texts, limit=topic_limits + 1, start=topic_start, step=topic_step):
    # logPrint("dictionary: " + str(dictionary) + "\n")
    # logPrint("corpus: " + str(corpus) + "\n")
    # logPrint("texts: " + str(texts) + "\n")

    coherence_values = []
    model_list = []
    for num_topics in range(start, limit, step):  # 枚举topic数目
        model = models.wrappers.LdaMallet(mallet_path, corpus=corpus, num_topics=num_topics, id2word=dictionary)
        model_list.append(model)
        coherencemodel = models.CoherenceModel(model=model, texts=texts, dictionary=dictionary, coherence='c_v')
        coherence_values.append(coherencemodel.get_coherence())
    best_index = 0
    for index in range(len(coherence_values)):  # 遍历topic数目对应的分数，返回最好的模型以及对应的topic数目
        logPrint("num_topics: " + str(step * index + start) + "Coherence Score: " + str(coherence_values[index]))
        if coherence_values[best_index] < coherence_values[index]:
            best_index = index
    logPrint("best num_topics: " + str(step * best_index + start) + "best Coherence Score: " + str(
        coherence_values[best_index]))
    return model_list[best_index], step * best_index + start


"""核心处理流程"""


def start_lda(data):
    # 进行数据的预处理
    train = []
    for value in data:
        line = handle_sentence(value["artibody"])
        line = [word.strip() for word in line.split(' ')]
        value["splitbody"] = line  # 记录处理后的数据，方便等会直接进行评分
        train.append(line)

    # 构建模型
    dictionary = corpora.Dictionary(train)  # 构建词频矩阵
    corpus = [dictionary.doc2bow(text) for text in train]
    ldamallet, topic_nums = compute_coherence_values(dictionary, corpus, train)

    # 输出主题
    topic_list = ldamallet.print_topics(topic_nums)
    logPrint("" + str(topic_nums) + "个主题的单词分布为：\n")
    for topic in topic_list:
        logPrint(topic)

    save_topic_word(topic_list)
    logPrint("存储 主题-单词 到redis成功...")

    docid_list = []
    topic_num_list = []
    prop_topic_list = []

    # 构建好模型之后，需要进行每个新闻的评分/主题处理
    for i, row in enumerate(ldamallet[corpus]):
        row = sorted(row, key=lambda x: (x[1]), reverse=True)  # 按评分降序
        logPrint('这条' + data[i]["tag"] + '新闻(' + data[i]["url"] + ')的主题分布为：\n')
        logPrint(str(row) + '\n')
        for j, (topic_num, prop_topic) in enumerate(row):

            save_news_topic(data[i]["docid"], topic_num, prop_topic)

            docid_list.append(data[i]["docid"])
            topic_num_list.append(topic_num)
            prop_topic_list.append(prop_topic)

            if j < topic_top_count:  # 保存每个新闻的 top K 主题，标记为该新闻与这些主题有关
                save_topic_news(topic_num, data[i]["docid"], prop_topic)

    logPrint('主题-新闻 数据存储到redis成功...')
    logPrint('新闻-主题 数据存储到redis成功...')
    save_cat_news(data)
    logPrint('分类-新闻 数据存储到redis成功...')
    save_all_news(data)
    logPrint('所有-新闻 数据存储到redis成功...')
    save_news_topic_mysql(docid_list, topic_num_list, prop_topic_list)
    logPrint('LDA 处理完毕...')
