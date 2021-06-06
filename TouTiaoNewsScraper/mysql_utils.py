import pymysql
from common import logPrint

host = "xxx"
user = "root"
password = "xxx"
database = "news_recommend"


def get_connection():
    conn = pymysql.connect(host=host, user=user, password=password, database=database)
    return conn


def clearOldData():
    conn = get_connection()
    cursor = conn.cursor()
    table_list = ["news", "news_topic", "user", "user_news", "comment"]
    try:
        for name in table_list:
            sql = "TRUNCATE " + name
            cursor.execute(sql)
        conn.commit()
    except:
        conn.rollback()
        logPrint("clear mysql data fail...")
    conn.close()
    logPrint('清空历史mysql数据成功...')


def clearMySQLTest():
    conn = get_connection()
    cursor = conn.cursor()
    table_list = ["user", "user_news", "comment"]
    try:
        for name in table_list:
            sql = "TRUNCATE " + name
            cursor.execute(sql)
        conn.commit()
    except:
        conn.rollback()
        logPrint("clear mysql data fail...")
    conn.close()
    logPrint('清空测试mysql数据成功...')


def save_news(data):
    conn = get_connection()
    cursor = conn.cursor()
    try:
        sql = "INSERT INTO news(docid,title,cat,comment_count,newstime,url,img,content,newsFrom) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s)"
        T = []
        for value in data:
            # sql = "INSERT INTO news(docid,title,cat,comment_count,newstime,url) VALUES ('" + \
            #       value["docid"] + "','" + value["title"] + "','" + value["tag"] + "'," + \
            #       str(value["comment_count"]) + ",'" + value["newstime"] + "','" + value["url"] + "')"
            tmpT = (
            value["docid"], value["title"], value["tag"], str(value["comment_count"]), value["newstime"], value["url"],
            value["img"], value["artibody"],value["newsfrom"]);
            T.append(tmpT)
        # cursor.execute(sql)
        cursor.executemany(sql, T)
        conn.commit()
    except:
        conn.rollback()
        logPrint("save mysql data fail...")
        return
    cursor.close()
    conn.close()
    logPrint('新闻 数据存储到mysql成功...')


def save_news_topic_mysql(docid_list, topic_num_list, prop_topic_list):
    conn = get_connection()
    cursor = conn.cursor()
    try:
        sql = "INSERT INTO news_topic(docid,topicid,weight) VALUES (%s,%s,%s)"
        T = []
        for index in range(len(docid_list)):
            # sql = "INSERT INTO news_topic(docid,topicid,weight) VALUES ('" + \
            #       docid_list[index] + "'," + str(topic_num_list[index]) + "," + str(prop_topic_list[index]) + ")"
            # sql = "INSERT INTO news_topic(docid,topicid,weight) VALUES ('" + \
            #       docid_list[index] + "'," + str(topic_num_list[index]) + "," + str(prop_topic_list[index]) + ")"
            tmpT = (docid_list[index], str(topic_num_list[index]), str(prop_topic_list[index]));
            T.append(tmpT)
        # cursor.execute(sql)
        cursor.executemany(sql, T)
        conn.commit()
    except:
        conn.rollback()
        logPrint("save mysql data fail...")
        return
    cursor.close()
    conn.close()
    logPrint('新闻-主题 数据存储到mysql成功...')
