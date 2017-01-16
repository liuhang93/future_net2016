package com.routesearch.route;

import com.filetool.util.FileUtil;

import java.util.Arrays;

/**
 * Created by liuhang on 2017/1/13.
 * KM算法
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
    private static int[] link;
    private static int[] slack;

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
//        int outDeg = Graph.nodes[x].outDeg;
//        for (int i = 0; i < outDeg; i++) {
        for (int y = 0; y < N; y++) {
//            int y = Graph.nodes[x].out[i];
            if (visity[y]) {
                continue;
            }
            int t = lx[x] + ly[y] - weight[x][y];
            if (t == 0) {
                visity[y] = true;
                if (link[y] == -1 || hungary(link[y])) {
                    link[y] = x;
                    return true;
                }
            } else if (slack[y] > t) {
                slack[y] = t;
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
        int result = 0;
        for (int i = 0; i < N; i++) {
            if (link[i] > -1) {
                System.out.println(i + "->" + link[i]);
                result += weight[link[i]][i];
            }
        }
        System.out.println("weight sum: " + result);
    }

    public static void AP() {
        N = Graph.vertexNum;
        weight = new int[N][N];
        link = new int[N];
        lx = new int[N];
        ly = new int[N];
        visitx = new boolean[N];
        visity = new boolean[N];
        slack = new int[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (Graph.edgeWeight[i][j] == 0) {//ij之间不连通
                    weight[i][j] = weightMax - INF;
                } else {
                    weight[i][j] = weightMax - Graph.edgeWeight[i][j];
                }
            }
        }
        KM();
    }

    public static void main(String[] args) {
        String graphPath = args[0];
        String[] matrix = FileUtil.read(graphPath, null);
        int i = 0;
        int n = 100;
        weight = new int[n][n];
        for (String line : matrix) {
            String[] contents = line.split(",");
            for (int j = 0; j < contents.length; j++) {
                weight[i][j] = Integer.parseInt(contents[j]);
            }
            i++;
        }
        N = i;
        link = new int[N];
        lx = new int[N];
        ly = new int[N];
        visitx = new boolean[N];
        visity = new boolean[N];
        slack = new int[N];
        KM();
    }
}
