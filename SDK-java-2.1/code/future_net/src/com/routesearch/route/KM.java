package com.routesearch.route;

import java.util.Arrays;

/**
 * Created by liuhang on 2017/1/13.
 * KM算法,求目标最大化
 */
public class KM {
    private static final int INF = Integer.MAX_VALUE;

    private static int N = Graph.vertexNum;
    private static int[][] weight = new int[N][N];
    private static int[] lx = new int[N];
    private static int[] ly = new int[N];
    private static boolean[] visitx = new boolean[N];
    private static boolean[] visity = new boolean[N];
    private static int[] link = new int[N];//(i,link[i])表示顶点i被顶点link[i]所连接;
    private static int[] slack = new int[N];

    private static int routeId;//路径Id,第几条路径

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
                        slack[i] -= d;
                }
            }
        }
    }

    public static void AP(int routeid, SolutionNode solutionNode, int[][] target) {
        routeId = routeid;
        for (int i = 0; i < target.length; i++) {
            weight[i] = Arrays.copyOf(target[i], target[i].length);
        }
        KM();
        solutionNode.link = Arrays.copyOf(link, link.length);
    }
}
