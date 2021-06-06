package top.mar.recommend.utils;

import java.util.ArrayList;
import java.util.List;

public class RandomUtils {

    // 从[0,n)里面选出m个不相同的数据出来
    // 使用平均策略，将数据划成m块，然后块内随机
    public static List<Integer> randomIndexWithAvgStrategy(int n, int m) {
        List<Integer> res = new ArrayList<>();
        if (m >= n) m = n;
        // 块id
        for (int ix = 0; ix < m; ix++) {
            // 利用块id，再次随机出应该选取的块内位置，组装成整体的数组位置
            int realIndex = (n / m) * ix;
            int randomCount = n / m;
            if (ix == m - 1) {
                randomCount += n % m;
            }
            realIndex += (int) (Math.random() * randomCount);
            realIndex = realIndex % n;
            res.add(realIndex);
        }
//        for (int v : res) {
//            System.out.print(v + " ");
//        }
//        System.out.println("");
        return res;
    }

    // 从[0,n)里面选出m个不相同的数据出来
    // 使用完全随机策略
    public static List<Integer> randomIndexWithRandomStrategy(int n, int m) {
        List<Integer> res = new ArrayList<>();
        if (m >= n) m = n;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = i;
        for (int ix = 0; ix < m; ix++) {
            int realIndex = (int) (Math.random() * (n - ix));
            res.add(arr[realIndex]);
            int tmp = arr[realIndex];
            arr[realIndex] = arr[n - ix - 1];
            arr[n - ix - 1] = tmp;
        }
//        for (int v : res) {
//            System.out.print(v + " ");
//        }
//        System.out.println("");
        return res;
    }

    public static void main(String[] args) {
        randomIndexWithAvgStrategy(12, 12);
        randomIndexWithAvgStrategy(12, 10);
        randomIndexWithAvgStrategy(12, 7);
        randomIndexWithAvgStrategy(12, 6);
        randomIndexWithAvgStrategy(12, 5);
        randomIndexWithAvgStrategy(12, 4);
        randomIndexWithAvgStrategy(12, 1);
        randomIndexWithRandomStrategy(12, 12);
        randomIndexWithRandomStrategy(12, 10);
        randomIndexWithRandomStrategy(12, 7);
        randomIndexWithRandomStrategy(12, 6);
        randomIndexWithRandomStrategy(12, 3);
        randomIndexWithRandomStrategy(12, 1);
    }
}
