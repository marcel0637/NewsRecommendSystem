package top.mar.recommend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import top.mar.recommend.listener.InitDataListener;

import java.util.*;

@Component
public class RedisUtils {

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    public boolean existsKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public void delKey(String key) {
        stringRedisTemplate.delete(key);
    }

    // 获取set内的元素数量
    public Set<String> getAllKeys(String key) {
        return stringRedisTemplate.keys(key);
    }

    // 获取set内的元素数量
    public int getSetCounts(String key) {
        if (!existsKey(key)) return 0;
        SetOperations<String, String> set = stringRedisTemplate.opsForSet();
        return Math.toIntExact(set.size(key));
    }

    // 根据排名区间获取zSet的值
    public Set<ZSetOperations.TypedTuple<String>> getZSetByRank(String key, long mi, long mx) {
        ZSetOperations<String, String> zSet = stringRedisTemplate.opsForZSet();
        return zSet.reverseRangeWithScores(key, mi, mx);
    }

    // zSet和set作差集，并返回差集
    public Set<ZSetOperations.TypedTuple<String>> zSetSubSet(String key1, String key2, String tmpKey) {

        SetOperations<String, String> set = stringRedisTemplate.opsForSet();
        Set<String> tmpSet = set.members(key2);

        ZSetOperations<String, String> zSet = stringRedisTemplate.opsForZSet();
        delKey(tmpKey);
        zSet.add(tmpKey, zSet.rangeWithScores(key1, 0, -1)); // 分类-新闻不可能为空，所以不管
        // 已经推送的新闻 可能为空
        if (!RedisUtils.isEmpty(tmpSet)) {
            zSet.remove(tmpKey, tmpSet.toArray());
        }
        return zSet.reverseRangeWithScores(tmpKey, 0, -1);
    }

    // 获取两个zSet的交集，并以第一个zSet的score为score
    public Set<ZSetOperations.TypedTuple<String>> interZSet(String key1, Set<String> key2) {
        Set<ZSetOperations.TypedTuple<String>> result = new LinkedHashSet<>();
        Set<ZSetOperations.TypedTuple<String>> sourceResult = InitDataListener.topicNewsGlobalMap.get(key1);
        // 取出key1中的值，看2中有的话，就作为交集存储
        for (ZSetOperations.TypedTuple<String> keyValue : sourceResult) {
            if (key2.contains(keyValue.getValue())) {
                result.add(keyValue);
            }
        }
        return result;
    }

    public void addListToSet(String key, List<String> newsList) {
        SetOperations<String, String> set = stringRedisTemplate.opsForSet();
        for (String v : newsList) {
            set.add(key, v);
        }
    }

    public void addKeyToSet(String key, String value) {
        SetOperations<String, String> set = stringRedisTemplate.opsForSet();
        set.add(key, value);
    }

    public void addZSetScore(String key, String topic, Double score) {
        ZSetOperations<String, String> zSet = stringRedisTemplate.opsForZSet();
        if (zSet.rank(key, topic) != null) {
            zSet.incrementScore(key, topic, score);
        } else {
            zSet.add(key, topic, score);
        }
    }

    public static Set<String> transferSetWithScoreToNormalSet(Set<ZSetOperations.TypedTuple<String>> set) {
        Set<String> res = new HashSet<>();
        for (ZSetOperations.TypedTuple<String> keyValue : set) {
            res.add(keyValue.getValue());
        }
        return res;
    }

    public static boolean isEmpty(Collection<String> collection) {
        return collection == null || collection.size() == 0;
    }

}
