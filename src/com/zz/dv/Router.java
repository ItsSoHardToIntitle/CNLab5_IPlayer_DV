package com.zz.dv;

import java.util.ArrayList;

/**
 * @author Zhang Zhen
 * @time 2019年6月3日 下午2:19:56
 */
public class Router {
    private int id;
    private ArrayList<RouterTableItem> routerTableItems;

    public Router() {
        routerTableItems = new ArrayList<>();
    }

    // 对每个路由表，用邻居的路由表信息来更新自己的路由表
    public void updateRouterTable(Router neighborRouter, int distance) {
        RouterTableItem routerTable = null;
        RouterTableItem neighborRouterTable = null;
        for (int i = 0; i < routerTableItems.size(); i++) {
            routerTable = routerTableItems.get(i);
            neighborRouterTable = neighborRouter.getRouterTableItems().get(i);
            // 待更新的路由目前无法到达某路由
            if (routerTable.getDistance() == 16) {
                // 邻居目前可以到达该路由，则：更新（此处单独分类，防止：10 + 13 > 16 而未更新）
                if (neighborRouterTable.getDistance() != 16) {
                    routerTable.setDistance(distance + neighborRouterTable.getDistance());
                    routerTable.setNextHop(neighborRouter.getId());
                }
                // 邻居目前也无法到达该路由, 对该路由来说，此次更新无变化
            } else { // 正常按照公式更新
                int tempDistance = distance + neighborRouterTable.getDistance();
                if (routerTable.getDistance() > tempDistance) {
                    routerTable.setDistance(tempDistance);
                    routerTable.setNextHop(neighborRouter.getId());
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<RouterTableItem> getRouterTableItems() {
        return routerTableItems;
    }
}
