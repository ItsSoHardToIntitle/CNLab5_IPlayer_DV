package com.zz.dv;

import java.util.ArrayList;

/**
 * @author Zhang Zhen
 * @time 2019��6��3�� ����2:19:56
 */
public class Router {
    private int id;
    private ArrayList<RouterTableItem> routerTableItems;

    public Router() {
        routerTableItems = new ArrayList<>();
    }

    // ��ÿ��·�ɱ����ھӵ�·�ɱ���Ϣ�������Լ���·�ɱ�
    public void updateRouterTable(Router neighborRouter, int distance) {
        RouterTableItem routerTable = null;
        RouterTableItem neighborRouterTable = null;
        for (int i = 0; i < routerTableItems.size(); i++) {
            routerTable = routerTableItems.get(i);
            neighborRouterTable = neighborRouter.getRouterTableItems().get(i);
            // �����µ�·��Ŀǰ�޷�����ĳ·��
            if (routerTable.getDistance() == 16) {
                // �ھ�Ŀǰ���Ե����·�ɣ��򣺸��£��˴��������࣬��ֹ��10 + 13 > 16 ��δ���£�
                if (neighborRouterTable.getDistance() != 16) {
                    routerTable.setDistance(distance + neighborRouterTable.getDistance());
                    routerTable.setNextHop(neighborRouter.getId());
                }
                // �ھ�ĿǰҲ�޷������·��, �Ը�·����˵���˴θ����ޱ仯
            } else { // �������չ�ʽ����
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
