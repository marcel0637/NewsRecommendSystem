# coding=UTF-8


import codecs  # 用来存储爬取到的信息
import time
from pybloom_live import ScalableBloomFilter  # 用于URL去重的
import requests  # 用于发起请求，获取网页信息
import json  # 处理json格式的数据
from mysql_utils import save_news

from common import getdetailpagebybs, getConfigValue, logPrint
from lda import start_lda
from redis_utils import clearRedisCache
from mysql_utils import clearOldData

page_num = int(getConfigValue('pageNum'))


def start_scraper():
    # 清空之前的redis数据
    clearRedisCache()
    # 清空之前的mysql数据
    clearOldData()
    # 使用ScalableBloomFilter模块，对获取的URL进行去重处理
    urlbloomfilter = ScalableBloomFilter(initial_capacity=100, error_rate=0.001,
                                         mode=ScalableBloomFilter.LARGE_SET_GROWTH)
    data_list = []
    # 按分类进行数据爬取
    tag_list = ['2511', '2669', '2512', '2513', '2514', '2515', '2516', '2517']
    # tag_list = ['2669']
    # tag_val = ['国际', '社会', '体育', '娱乐', '军事', '科技', '财经', '股市']
    tag_val = ['world', 'society', 'sports', 'entertainment', 'military', 'technology', 'finance', 'stock']
    # tag_val = ['society']
    st_time = int(time.mktime(time.strptime(getConfigValue("news_st_date"), "%Y-%m-%d.%H:%M:%S")))
    ed_time = int(time.mktime(time.strptime(getConfigValue("news_ed_date"), "%Y-%m-%d.%H:%M:%S")))
    requests.adapters.DEFAULT_RETRIES = 2
    s = requests.session()
    s.keep_alive = False  # 关闭多余连接
    headers = {'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36'}
    while (st_time + 86400) <= ed_time:
        for index, tag in enumerate(tag_list):
            page = 1  # 设置爬虫初始爬取的页码
            # 使用BeautifulSoup抽取模块和存储模块
            # 设置爬取页面的上限，
            while page <= page_num:
                # 以API为index开始获取url列表
                try:
                    url = "https://feed.mix.sina.com.cn/api/roll/get?pageid=153&lid=" + tag + \
                          "&etime=" + str(st_time) + "&stime=" + str(st_time+86400) + \
                          "&k=&num=50&page=" + str(page)
                    data = s.get(url, headers=headers)  # 需要的网址
                    if data.status_code == 200:  # 当请求页面返回200（代表正确）时，获取网页数据
                        # 将获取的数据json化
                        data_json = json.loads(data.content)
                        news = data_json.get("result").get("data")  # 获取result节点下data节点中的数据，此数据为新闻详情页的信息
                        # 从新闻详情页信息列表news中，使用for循环遍历每一个新闻详情页的信息
                        for new in news:
                            # 查重，从new中提取URL，并利用ScalableBloomFilter查重
                            if new["url"] not in urlbloomfilter:
                                urlbloomfilter.add(new["url"])  # 将爬取过的URL放入urlbloomfilter中
                                try:
                                    # logPrint(new)
                                    # 抽取模块使用bs4
                                    if new["img"]["u"] == None:
                                        continue
                                    detail = getdetailpagebybs(new["url"])
                                    detail['docid'] = new['docid'][-14:]
                                    detail['tag'] = tag_val[index]
                                    detail['comment_count'] = new.get("comment_total", 0)
                                    detail['img'] = new["img"]["u"]
                                    data_list.append(detail)
                                except Exception as e:
                                    logPrint('get url content fail... url:' + new["url"] + 'exception:' + e)
                            # break
                    page += 1  # 页码自加1
                except Exception as ee:
                    logPrint(
                        'HTTPSConnectionPool(host=\'feed.mix.sina.com.cn\', port=443): Max retries exceeded with url')
                    page += 1
            logPrint(url + " " + tag_val[index] + ' done... ' + 'now news count:' + str(len(data_list)))

        st_time = st_time + 86400

    start_lda(data_list)
    save_news(data_list)
