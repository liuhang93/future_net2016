package com.routesearch.route;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by liuhang on 2017/2/5.
 * branch and bound 分支定界法
 */
public class Search {

    public static Queue<SolutionNode> queue;
    //分支定界法
    public static void branchAndBound(){
        SolutionNode solutionNode = new SolutionNode();
        KM.AP(1, solutionNode);
        queue = new PriorityQueue<>(3, new MyComparator());
        queue.add(solutionNode);
    }
    //测试分支定界法
    public static void main(String[] args) {

    }
}

