// DONOT change the package path.
// DONOT change the file name and the class declaration.
// DONOT change the function signature.
//请勿修改包路径
//请勿修改文件名和类声明
//请勿修改函数签名
/**
 * 实现代码文件
 *
 * @author XXX
 * @version V1.0
 * @since 2016-4-11
 */
package com.routesearch.route;

import com.routesearch.util.TimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Route {
    public static Map<Integer, List<Integer>> bestRoutes = new HashMap<>();

    /**
     * 你需要完成功能的入口
     *
     * @author XXX
     * @version V1
     * @since 2016-3-4
     */
    public static String[] searchRoute(String[] graphContent, String[] condition) {
        /**do your work here**/
        TimeUtil.updateTime();
        Graph.makeGraph(graphContent, condition);
        System.out.println("read graph: " + TimeUtil.getTimeDelay() + "ms");
        int routeId = 1;

        bestRoutes.put(routeId - 1, Search.branchAndBound(routeId, 4000));
        bestRoutes.put(2 - routeId, Search.branchAndBound(3 - routeId, 4000));

        while (true) {
            System.out.println("repeat:" + getRepeatNum());
            if (TimeUtil.getUsedTime() >= 9000) {
                System.out.println("跳1");
                break;
            }
            if (getRepeatNum() == 0) {
                System.out.println("跳2");
                break;
            }
            bestRoutes.put(routeId - 1, Search.branchAndBound(routeId, 2000));
            routeId = 3 - routeId;

        }
        String[] routes = new String[2];
        routes[0] = saveRoute(bestRoutes.get(0));
        routes[1] = saveRoute(bestRoutes.get(1));
        printRoute();
        return routes;
//        return new String[]{"0|1|2", "5|6|2"};
    }

    private static int getRepeatNum() {
        List<Integer> route1 = bestRoutes.get(0);
        List<Integer> route2 = bestRoutes.get(1);
        int repeatNum = 0;
        for(int i=0;i<route1.size()-2;i++) {
            int edgeId1 =Graph.edgeId[route1.get(i)][route1.get(i + 1)];
            for(int j=0;j<route2.size()-2;j++) {
                int edgeId2 = Graph.edgeId[route2.get(j)][route2.get(j + 1)];
                if (edgeId1 == edgeId2) {
                    repeatNum++;
                }
            }
        }
        return repeatNum;
    }

    //保存路径
    private static String saveRoute(List<Integer> route) {
        String str = "";
        for (int i = 0; i < route.size() - 2; i++) {
            int id1 = route.get(i);
            int id2 = route.get(i + 1);
            str = str + Graph.edgeId[id1][id2] + "|";
        }
        str = str.substring(0, str.length() - 1);
        return str;
    }

    private static void printRoute(){
        int[] sum = new int[2];
        int j = 0;
        for (Map.Entry<Integer,List<Integer>> entry : bestRoutes.entrySet()) {
            sum[j] = 0;
            List<Integer> route = entry.getValue();
            System.out.println("路径"+j+":");
            for(int i=0;i<route.size()-2;i++) {
                int id1 = route.get(i);
                int id2 = route.get(i + 1);
                sum[j] += Graph.edgeWeight[id1][id2];
                System.out.print(Graph.edgeId[id1][id2] + "->");
            }
            System.out.print("\nsum:"+sum[j]+"\n");
            j++;
        }
        System.out.println("sumTotal:"+(sum[0]+sum[1]));
    }
}
