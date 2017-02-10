package com.routesearch.route;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuhang on 2017/2/6.
 * 解空间的结点
 */
public class SolutionNode implements Cloneable {

    public int ringNum;//环的数量
    public int apSum;//ap问题的解的权值
    public int[] link;//ap问题的解(路径匹配)

    public Map<Integer, Integer> forbidden = new HashMap<>();//必须不可出现在路径中的边
    public Map<Integer, Integer> required = new HashMap<>();//必须出现在路径中的边

}

class MyComparator implements Comparator<SolutionNode> {
    @Override
    public int compare(SolutionNode o1, SolutionNode o2) {
        if (Search.currentSolutionNode.ringNum > 5) {
            return o1.apSum*o1.ringNum - o2.apSum*o2.ringNum;
        }
        return o1.apSum - o2.apSum;
    }
}