package com.routesearch.route;

/**
 * Created by liuhang on 2017/1/5.
 * 顶点类
 */
public class Node {
    public int state;
    public int inDeg; //入度
    public int outDeg;//出度
    public int[] out = new int[Graph.outDegMax];
    public int[] in = new int[Graph.vertexNumMax];

}
