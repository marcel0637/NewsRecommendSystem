import redis
import re
from common import getConfigValue, logPrint

news_topic_prefix = getConfigValue("news_topic_prefix")
topic_news_prefix = getConfigValue("topic_news_prefix")
topic_word_prefix = getConfigValue("topic_word_prefix")
cat_news_prefix = getConfigValue("cat_news_prefix")
user_view_news_prefix = getConfigValue("user_view_news_prefix")
user_scan_news_prefix = getConfigValue("user_scan_news_prefix")
user_topic_prefix = getConfigValue("user_topic_prefix")
tmp_prefix = getConfigValue("tmp_prefix")

def get_connection():
    r = redis.Redis(host="xxx", port=6379, password="xxx", decode_responses=True, socket_connect_timeout=864000)
    return r

flagA = False
flagB = False
rA = get_connection() # 用于缓存
rB = get_connection() # 用于缓存

# 清空之前的数据
def clearRedisCache():
    r = get_connection()
    if r.keys(news_topic_prefix + "*"):
        r.delete(*r.keys(news_topic_prefix + "*"))
    if r.keys(topic_news_prefix + "*"):
        r.delete(*r.keys(topic_news_prefix + "*"))
    if r.keys(cat_news_prefix + "*"):
        r.delete(*r.keys(cat_news_prefix + "*"))
    if r.keys(user_view_news_prefix + "*"):
        r.delete(*r.keys(user_view_news_prefix + "*"))
    if r.keys(user_scan_news_prefix + "*"):
        r.delete(*r.keys(user_scan_news_prefix + "*"))
    if r.keys(user_topic_prefix + "*"):
        r.delete(*r.keys(user_topic_prefix + "*"))
    if r.keys(topic_word_prefix + "*"):
        r.delete(*r.keys(topic_word_prefix + "*"))
    if r.keys(tmp_prefix + "*"):
        r.delete(*r.keys(tmp_prefix + "*"))
    logPrint('清空历史redis数据成功...')
    r.close()


# 清空测试的数据
def clearRedisTest():
    r = get_connection()
    if r.keys(user_view_news_prefix + "*"):
        r.delete(*r.keys(user_view_news_prefix + "*"))
    if r.keys(user_scan_news_prefix + "*"):
        r.delete(*r.keys(user_scan_news_prefix + "*"))
    if r.keys(user_topic_prefix + "*"):
        r.delete(*r.keys(user_topic_prefix + "*"))
    if r.keys(tmp_prefix + "*"):
        r.delete(*r.keys(tmp_prefix + "*"))
    logPrint('清空测试redis数据成功...')
    r.close()


# 保存docid-topic-proc(zset)到redis中
def save_news_topic(docid, topic_num, prop_topic):
    global flagA, rA
    if not flagA:
        rA = get_connection()
    flagA = True
    realkey = news_topic_prefix + docid
    # r.hset(realkey, topic_num, prop_topic)
    rA.zadd(realkey, {topic_num: prop_topic})


# 保存topic-docid-proc(zset)到redis中
def save_topic_news(topic_num, docid, prop_topic):
    global flagB, rB
    if not flagB:
        rB = get_connection()
    flagB = True
    realkey = topic_news_prefix + str(topic_num)
    rB.zadd(realkey, {docid: prop_topic})
    # r.sadd(realkey, docid)


# 保存cat-news(zset)到redis中
def save_cat_news(data):
    r = get_connection()
    for value in data:
        realkey = cat_news_prefix + value["tag"]
        # r.sadd(realkey, value["docid"])
        r.zadd(realkey, {value["docid"]: value["comment_count"]})
    r.close()


# 保存all-news(zset)到redis中
def save_all_news(data):
    r = get_connection()
    for value in data:
        realkey = cat_news_prefix + "all"
        # r.sadd(realkey, value["docid"])
        r.zadd(realkey, {value["docid"]: value["comment_count"]})
    r.close()

# 保存topic-word(zset)到redis中
def save_topic_word(topic_list):
    r = get_connection()
    for index in range(len(topic_list)):
        realkey = topic_word_prefix + str(index)
        now_list = topic_list[index][1].split("+")
        for now_str in now_list:
            val_str = re.findall(r"\d+\.?\d*", now_str)[0]
            word = re.findall(r"\"(.+?)\"", now_str)[0]
            val = float(val_str)
            r.zadd(realkey, {word: val})
    r.close()