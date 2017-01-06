package com.routesearch.route;

import java.util.Arrays;

/**
 * Created by liuhang on 2017/1/5.
 * 图类
 */
public class Graph {

    public static final int vertexNumMax = 2000;//最大顶点数
    public static final int outDegMax = 20;//最大出度
    public static final int demandNumMax = 102;//最大必经节点数(算上起点终点)

    public static int vertexNum = 0;//顶点数
    public static int[] inSetNum = new int[2];//两条路的必经点数目
    public static int[] vertexId = new int[vertexNumMax];//节点重新编号,顶点索引范围为[0，2000),索引可能不连续
    public static int[][] vertexDemand = new int[2][demandNumMax];
    public static int[][] edgeId = new int[vertexNumMax][vertexNumMax];
    public static int[][] edgeWeight = new int[vertexNumMax][vertexNumMax];
    public static Node[] nodes = new Node[vertexNumMax];

    public static void makeGraph(String[] graphContent, String[] condition) {
        readGraph(graphContent);
        readCondition(condition);
    }

    public static void readGraph(String[] graphContent) {
        Arrays.fill(vertexId, -1);//数组填充为-1;
        for (String edge : graphContent) {
            String[] edgeContent = edge.split(",");
            int id = Integer.parseInt(edgeContent[0]);
            int from = Integer.parseInt(edgeContent[1]);
            int to = Integer.parseInt(edgeContent[2]);
            int weight = Integer.parseInt(edgeContent[3]);
            if (vertexId[from] == -1) {
                vertexId[from] = vertexNum++;
            }
            if (vertexId[to] == -1) {
                vertexId[to] = vertexNum++;
            }
            int start = vertexId[from], end = vertexId[to];
            if (edgeWeight[start][end] == 0) {
                edgeWeight[start][end] = weight;
                edgeId[start][end] = id;
                if (nodes[start] == null) {
                    nodes[start] = new Node();
                }
                nodes[start].out[nodes[start].outDeg++] = end;
                if (nodes[end] == null) {
                    nodes[end] = new Node();
                }
                nodes[end].in[nodes[end].inDeg++] = start;
            }
        }
        System.out.println("顶点数:" + vertexNum);
    }

    public static void readCondition(String[] condition) {
        for (String route : condition) {
            String[] content = route.split("[,|]");
            int routeId = Integer.parseInt(content[0]);
            for (int i = 1; i < content.length; i++) {
                if (content[i] == "NA") {
                    break;
                }
                int num = Integer.parseInt(content[i]);
                int id = vertexId[num];
                vertexDemand[routeId - 1][inSetNum[routeId - 1]++] = id;
                nodes[id].state = routeId;
            }
        }
        System.out.println("route1: "+inSetNum[0]+"; route2: "+inSetNum[1]);
    }
}
