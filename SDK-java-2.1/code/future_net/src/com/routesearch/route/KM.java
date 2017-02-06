package com.routesearch.route;

import java.util.Arrays;

/**
 * Created by liuhang on 2017/1/13.
 * KM算法,求目标最大化
 */
public class KM {
    private static final int INF = Integer.MAX_VALUE;
    private static final int weightMax = 100;//权重最大值

    private static int N;
    private static int[][] weight;
    private static int[] lx;
    private static int[] ly;
    private static boolean[] visitx;
    private static boolean[] visity;
    private static int[] link;//(i,link[i])表示顶点i被顶点link[i]所连接;
    private static int[] slack;

    private static int routeId;

    private static void init() {
        Arrays.fill(link, -1);
        Arrays.fill(ly, 0);
        for (int i = 0; i < N; i++) {
            lx[i] = -INF;
            for (int j = 0; j < N; j++) {
                if (weight[i][j] > lx[i]) {
                    lx[i] = weight[i][j];
                }
            }
        }

    }

    private static boolean hungary(int x) {
        visitx[x] = true;
        int outDeg = Graph.nodes[x].outDeg;
        if (Graph.nodes[x].state != routeId && Graph.nodes[x].state != 3) {
            outDeg += 1;//不是非必经节点,则可与自己配对
        }
        for (int i = 0; i < outDeg; i++) {
            int y = Graph.nodes[x].out[i];
            if (visity[y]) {
                continue;
            }
            int t = lx[x] + ly[y] - weight[x][y];//t=0,在相等子图中找匹配
            if (t == 0) {
                visity[y] = true;
                if (link[y] == -1 || hungary(link[y])) {
                    link[y] = x;
                    return true;
                }
            } else if (slack[y] > t) {
                slack[y] = t; //寻找增广路径时,顺便将slack值算出
            }
        }
        return false;
    }

    private static void KM() {
        init();
        for (int x = 0; x < N; x++) {
            Arrays.fill(slack, INF);
            while (true) {
                Arrays.fill(visitx, false);
                Arrays.fill(visity, false);
                if (hungary(x)) {
                    break;//找到增广轨,退出
                }
                int d = INF;
                for (int i = 0; i < N; i++) {
                    if (!visity[i] && d > slack[i]) {
                        d = slack[i];
                    }
                }
                for (int i = 0; i < N; i++) {
                    if (visitx[i])
                        lx[i] -= d;//修改x的顶标
                }
                for (int i = 0; i < N; i++) {
                    if (visity[i])
                        ly[i] += d;
                    else
                        slack[i] -= d;//
                }
            }
        }
        int result = 0;
        for (int i = 0; i < N; i++) {
            if (link[i] > -1) {
                System.out.println(link[i] + "->" + i);
                result += Graph.edgeWeight[link[i]][i];
            }
        }
        System.out.println("weight sum: " + result);
    }

    public static void AP(int id, SolutionNode solutionNode) {
        N = Graph.vertexNum;
        weight = new int[N][N];
        link = new int[N];
        lx = new int[N];
        ly = new int[N];
        visitx = new boolean[N];
        visity = new boolean[N];
        slack = new int[N];
        routeId = id;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (Graph.edgeWeight[i][j] != 0) {
                    weight[i][j] = weightMax - Graph.edgeWeight[i][j];
                } else if (i != j || routeId == Graph.nodes[i].state || Graph.nodes[i].state == 3) {
                    weight[i][j] = 0;
                } else {
                    weight[i][j] = weightMax;//不是必经节点时,让它易于同自己连接(自己成环)
                }
            }
        }
        KM();
        solutionNode.link = Arrays.copyOf(link, link.length);
    }
}
