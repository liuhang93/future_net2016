package com.routesearch.route;

import com.routesearch.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by liuhang on 2017/2/5.
 * branch and bound 分支定界法
 */
public class Search {
    private static final int weightMax = 100;//权重最大值
    private static final int MAX_VALUE = 100000;//一个很大值

    private static Queue<SolutionNode> queue = new PriorityQueue<>(3, new MyComparator());
    private static List<List<Integer>> allRoutes = new ArrayList<>(); //一次ap之后,存放所有的环
    private static int[][] target = new int[Graph.vertexNum][Graph.vertexNum];//ap的目标矩阵
    private static int shortestRingIndex;//一次ap后,所有环中顶点数最少的环的索引
    private static int upBound;//分支定界的上界
    private static long bestTime;//更新上界时的时间
    private static List<Integer> bestRoute;//存放当前权值最低路径
    private static int punishment = 1;//另一条路径经过的边进行惩罚
    public static SolutionNode currentSolutionNode;//从优先队列中取出的解节点

    //分支定界法
    public static List<Integer> branchAndBound(int routeId, long timeLimit) {

        TimeUtil.updateTime();
        boolean onlyOneRing = false;
        upBound = MAX_VALUE;
        queue.clear();

        currentSolutionNode = new SolutionNode();
        initialTargetMatrix(routeId);
        punishment = punishment + 1;//惩罚量递增

        KM.AP(routeId, currentSolutionNode, target);
        getRoute(routeId, currentSolutionNode);
        if (currentSolutionNode.ringNum == 1) {
            if (currentSolutionNode.apSum < upBound) {
                upBound = currentSolutionNode.apSum;
            }
            bestRoute = allRoutes.get(0);
            System.out.println("跳出0");//调试信息,方便看运行过程
//            printRoutes();
            return bestRoute;
        }
        //进行分支,选择最短的环,破开,修改目标矩阵
        getRequiredAndForbidden(currentSolutionNode);

        //用优先队列,遍历节点
        while (!queue.isEmpty()) {
            if (TimeUtil.getTimeDelay() > timeLimit && onlyOneRing) {
                System.out.println("跳出1");//调试信息,方便看运行过程
                break;
            }
            if (onlyOneRing && (TimeUtil.getTimeDelay() - bestTime >= Math.sqrt(Graph.vertexNum
            ) + Graph.inSetNum[0] + Graph.inSetNum[1])) {
                System.out.println("跳出2");//调试信息,方便看运行过程
                break;
            }
            currentSolutionNode = queue.poll();
            if (currentSolutionNode.apSum > upBound) {
                System.out.println("跳出3");//调试信息,方便看运行过程
                break;
            }
            int[][] costMatrix = modifyTargetMatrix(currentSolutionNode);
            KM.AP(routeId, currentSolutionNode, costMatrix);
            getRoute(routeId, currentSolutionNode);
            if (currentSolutionNode.apSum > upBound) {
                continue;
            }
            if (currentSolutionNode.ringNum == 1) {
                onlyOneRing = true;
                if (currentSolutionNode.apSum < upBound) {
                    upBound = currentSolutionNode.apSum;
                    bestTime = TimeUtil.getTimeDelay();
                    bestRoute = allRoutes.get(0);
                }
                continue;
            }
            getRequiredAndForbidden(currentSolutionNode);
        }
//        printRoutes();
        return bestRoute;
    }

    //初始化目标矩阵
    private static void initialTargetMatrix(int routeId) {
        for (int i = 0; i < Graph.vertexNum; i++) {
            for (int j = 0; j < Graph.vertexNum; j++) {
                if (Graph.edgeWeight[i][j] != 0) {
                    target[i][j] = weightMax - Graph.edgeWeight[i][j];
                } else if (i != j || routeId == Graph.nodes[i].state || Graph.nodes[i].state == 3) {
                    target[i][j] = weightMax - MAX_VALUE;
                } else {
                    target[i][j] = weightMax;//不是必经节点时,让它易于同自己连接(自己成环)
                }
            }
        }
        target[Graph.vertexDemand[0][1]][Graph.vertexDemand[0][0]] = weightMax;
        if (Route.bestRoutes.get(2 - routeId) != null) {
            List<Integer> route = Route.bestRoutes.get(2 - routeId);
            for (int i = 0; i < route.size() - 2; i++) {
                int id1 = route.get(i);
                int id2 = route.get(i + 1);
                target[id1][id2] -= punishment;
            }
        }
    }

    //获取破开的边,并加入队列
    private static void getRequiredAndForbidden(SolutionNode solutionNode) {
        List<Integer> route = allRoutes.get(shortestRingIndex);
        Map<Integer, Integer> forbidden = new HashMap<>();
        Map<Integer, Integer> required = new HashMap<>();
        for (int i = 1; i < route.size(); i++) {
            forbidden.clear();
            forbidden.put(route.get(i - 1), route.get(i));
            if (i > 1) {
                required.put(route.get(i - 2), route.get(i - 1));
            }
            // 新建子节点,完全复制父节点
            SolutionNode subSolutionNode = new SolutionNode();
            subSolutionNode.apSum = solutionNode.apSum;
            subSolutionNode.ringNum = solutionNode.ringNum;
            subSolutionNode.link = Arrays.copyOf(solutionNode.link, solutionNode.link.length);
            subSolutionNode.forbidden.putAll(solutionNode.forbidden);
            subSolutionNode.required.putAll(solutionNode.required);

            subSolutionNode.forbidden.putAll(forbidden);
            subSolutionNode.required.putAll(required);

            //加入队列
            queue.add(subSolutionNode);
        }

    }

    //对目标矩阵ap(指派)后,对所有必须经过的节点,求环的数目和指派后的权值
    private static void getRoute(int routeId, SolutionNode solutionNode) {
        allRoutes.clear();
        int ringNum = 0;//环数
        int apSum = 0;//权值
        int ringLength = Graph.vertexNum;
        boolean[] visit = new boolean[Graph.vertexNumMax];
        for (int i = 0; i < Graph.inSetNum[routeId - 1]; i++) {
            List<Integer> route = new ArrayList<>();
            int v = Graph.vertexDemand[routeId - 1][i];
            if (visit[v]) {
                continue;
            }
            route.add(v);
//            visit[v] = true;
            int m = solutionNode.link[v];
            while (m > -1) {
                if (visit[m]) {
                    break;
                }
                route.add(m);
                visit[m] = true;
                m = solutionNode.link[m];
            }

            if (route.size() < ringLength) {
                ringLength = route.size();
                shortestRingIndex = ringNum;
            }
            ringNum++;
            Collections.reverse(route);
            for (int k = 0; k < route.size() - 1; k++) {
                apSum += Graph.edgeWeight[route.get(k)][route.get(k + 1)];
            }
            allRoutes.add(route);
        }
        solutionNode.apSum = apSum;
        solutionNode.ringNum = ringNum;
//        System.out.println("ringNum:" + ringNum + ";apSum:" + apSum);
    }

    //根据破开的环(forbidden)与必须留在路径中的环,来修改ap问题中的目标矩阵
    private static int[][] modifyTargetMatrix(SolutionNode solutionNode) {
        int[][] modifiedTargetMatrix = new int[Graph.vertexNum][Graph.vertexNum];
        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target.length; j++) {
                modifiedTargetMatrix[i][j] = target[i][j];
            }
        }
        for (Map.Entry<Integer, Integer> entry : solutionNode.forbidden.entrySet()) {
            int id1 = entry.getKey();
            int id2 = entry.getValue();
            modifiedTargetMatrix[id1][id2] -= MAX_VALUE;
        }
        for (Map.Entry<Integer, Integer> entry : solutionNode.required.entrySet()) {
            int id1 = entry.getKey();
            int id2 = entry.getValue();
            for (int i = 0; i < Graph.vertexNum; i++) {
                if (i != id2) {
                    modifiedTargetMatrix[id1][i] -= MAX_VALUE;
                }
            }
        }
        return modifiedTargetMatrix;
    }

    //打印路径
    private static void printRoutes() {
        for (int i = 0; i < bestRoute.size() - 2; i++) {
            int id1 = bestRoute.get(i);
            int id2 = bestRoute.get(i + 1);
            System.out.print(Graph.edgeId[id1][id2] + "->");
        }
        System.out.print("\n");
        System.out.println("sum:" + upBound);
    }

}

