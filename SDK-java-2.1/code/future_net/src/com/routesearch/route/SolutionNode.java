package com.routesearch.route;

import java.util.Comparator;

/**
 * Created by liuhang on 2017/2/6.
 * 解空间的结点
 */
public class SolutionNode {
//    public static final int

    public int ringNum;//环的数量
    public int apSum;//ap问题的解的权值
    public int mustNum;//经过的必经点数目
    public int[] link;//ap问题的解(路径匹配)
}

class MyComparator implements Comparator<SolutionNode> {
    @Override
    public int compare(SolutionNode o1, SolutionNode o2) {
        return o1.apSum - o2.apSum;
    }
}