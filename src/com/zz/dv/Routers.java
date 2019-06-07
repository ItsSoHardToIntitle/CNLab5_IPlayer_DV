package com.zz.dv;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Zhang Zhen
 * @time 2019年6月3日 下午5:02:05
 */
public class Routers {

    private ArrayList<Router> routers;
    private static HashMap<String, Integer> routerNoMap = new HashMap<>();
    private static HashMap<Integer, String> noRouterMap = new HashMap<>();

    public Routers() {
        routers = new ArrayList<>();
    }

    public void init() {
        int[][] configInfoArray = Gui.getConfigInfoArray();
        // 当拓扑结构改变时，得重新初始化，清空操作勿要总是忘记。
        routers.clear();
        routerNoMap.clear();
        noRouterMap.clear();
        int distance = -1;
        int nextHop = -1;
        int temp = 65; // A - Z
        for (int i = 0; i < configInfoArray.length; i++) {
            routerNoMap.put(String.valueOf((char) (temp)), i);
            noRouterMap.put(i, String.valueOf((char) (temp++)));
            Router router = new Router();
            router.setId(i);
            for (int j = 0; j < configInfoArray[i].length; j++) {
                distance = configInfoArray[i][j];
                nextHop = j;
                router.getRouterTableItems().add(new RouterTableItem(distance, nextHop));
            }
            this.routers.add(router);
        }
    }

    public void calIterations(Router afterUpdate, ArrayList<RouterTableItem> old) {
        ArrayList<RouterTableItem> afterUpdateItems = afterUpdate.getRouterTableItems();
        boolean hasChanged = false;
        for (int i = 0; i < afterUpdateItems.size(); i++) {
            // 已经重写了equals方法
            if (!afterUpdateItems.get(i).equals(old.get(i))) {
                // 只要有一条线路改变，则视为一次迭代
                hasChanged = true;
                break;
            }
        }
        // 更新表格中的迭代次数
        if (hasChanged) {
            int colNum = afterUpdate.getId() + 1;
            int oldIterations = (int) Gui.getDefaultTableModel2().getValueAt(0, colNum);
            Gui.getDefaultTableModel2().setValueAt(oldIterations + 1, 0, colNum);
        }
    }

    public void copy(ArrayList<RouterTableItem> old, ArrayList<RouterTableItem> now) {
        for (int i = 0; i < now.size(); i++) {
            RouterTableItem temp = now.get(i);
            old.add(new RouterTableItem(temp.getDistance(), temp.getNextHop()));
        }
    }

    public void update() {
        int distance = -1;
        Router router = null; // 待更新的路由器
        Router router2 = null; // router的邻居

        ArrayList<RouterTableItem> old = new ArrayList<>();

        int routerSize = routers.size();
        for (int i = 0; i < routerSize; i++) {
            router = routers.get(i);

            old.clear();
            // 不能用=赋值，为引用，除非也重写Routers的equals方法
            // 然而很奇葩，addAll()也不行，可能是因为ArrayList<>中的数据类型
            // 是自己定义的对象，不是基本类型吧？？？ ——Java博大精深
            // old = router.getRouterTableItems();

            copy(old, router.getRouterTableItems());

            for (int j = 0; j < routerSize; j++) {
                router2 = routers.get(j);
                // 使用邻居来更新
                if (Gui.getIsNeighbor()[i][j] != 0) {
                    distance = old.get(j).getDistance();
                    // 更新前保存该路由器的旧的路由表，以计算迭代次数。
                    router.updateRouterTable(router2, distance);
                }
            }
            calIterations(router, old);
        }
    }

    public ArrayList<Router> getRouters() {
        return routers;
    }

    public static HashMap<String, Integer> getRouterNoMap() {
        return routerNoMap;
    }

    public static HashMap<Integer, String> getNoRouterMap() {
        return noRouterMap;
    }
}
