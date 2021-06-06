# coding=UTF-8
from news_scraper import start_scraper
import sys
import time
import redis
from redis_utils import clearRedisTest
from mysql_utils import clearMySQLTest
from common import logPrint, __init__

news_topic_prefix = "news:topic::"

if __name__ == '__main__':
    __init__()
    ops = sys.argv[1]
    if ops == 'reuse':
        logPrint(' clear part redis&mysql for reuse start...')
        clearRedisTest()
        clearMySQLTest()
        logPrint(' clear part redis&mysql for reuse success...')
    elif ops == 'start':
        logPrint(' start scrape news data and handle...')
        start_scraper()
        logPrint(' news data handle success...')
    else:
        logPrint('请输入运行参数：start/reuse')

    # r = redis.Redis(host="47.111.112.196", port=6379, password="1796466322@Aly")
    # print(r.keys("*"))
    # clearRedisTest()
    # print(r.get)
    # r.sadd("tq","a")
    # r.sadd("tq","b")
    # r.sadd("tqq","kkntiam0203863")
    # r.sadd("tqq","kkntiak9451672")
    # r.sadd("tqq","kkntiak9451676")
    # print(r.smembers("tqq"))
    # print(r.zrevrange("tmp:",0,-1,withscores=True))
    # print(r.zrevrange("tmp::",0,-1,withscores=True))
    # print(r.zrevrange("ddd",0,-1,withscores=True))
    # print(r.sdiff("ccc","bbb"))
    # print(r.zinterstore("ddd",["bbb","ccc"], aggregate='min'))
    # r.sadd("aaa",1)
    # r.sadd("aaa",2)
    # r.zadd("bbb",{2:2.0})
    # r.zadd("bbb",{3:1.0})
    # r.zadd("ccc",{3:2.0})
    # r.zadd("ccc",{4:1.0})
    # print(r.hgetall(news_topic_prefix+"kknscsi2795708"))
    # print(r.zrevrange("news:topic::kkntiak9142789",0,-1,withscores=True))
    # print(r.zrevrange("topic:news::19",0,-1,withscores=True))
    # print(r.zrevrange("cat:news::military", 0, -1, withscores=True))
    # print(r.zrevrange("cat:news::all", 0, -1, withscores=True))
    # if r.keys(news_topic_prefix + "*"):
    #     r.delete(*r.keys(news_topic_prefix + "*"))
