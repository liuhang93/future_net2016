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
        Graph.makeGraph(graphContent,condition);
        System.out.println("read graph: "+TimeUtil.getTimeDelay()+"ms");
        Search.branchAndBound(1);
        return new String[]{"0|1|2", "5|6|2"};
    }

}
