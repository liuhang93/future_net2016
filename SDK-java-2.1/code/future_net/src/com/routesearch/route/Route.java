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

import java.util.List;

public final class Route {
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
        List<Integer> route1 = Search.branchAndBound(1, 2000);
        List<Integer> route2 = Search.branchAndBound(2, 2000);
        String[] routes = new String[2];
        routes[0] = saveRoute(route1);
        routes[1] = saveRoute(route2);
        return routes;
//        return new String[]{"0|1|2", "5|6|2"};
    }

    //保存路径
    private static String saveRoute(List<Integer> route) {
        String str="";
        for (int i = 0; i < route.size() - 2; i++) {
            int id1 = route.get(i);
            int id2 = route.get(i + 1);
            str = str+Graph.edgeId[id1][id2]+"|";
        }
        str = str.substring(0, str.length()-1);
        return  str;
    }

}
