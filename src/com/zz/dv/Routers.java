package com.zz.dv;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Zhang Zhen
 * @time 2019��6��3�� ����5:02:05
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
        // �����˽ṹ�ı�ʱ�������³�ʼ������ղ�����Ҫ�������ǡ�
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
            // �Ѿ���д��equals����
            if (!afterUpdateItems.get(i).equals(old.get(i))) {
                // ֻҪ��һ����·�ı䣬����Ϊһ�ε���
                hasChanged = true;
                break;
            }
        }
        // ���±���еĵ�������
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
        Router router = null; // �����µ�·����
        Router router2 = null; // router���ھ�

        ArrayList<RouterTableItem> old = new ArrayList<>();

        int routerSize = routers.size();
        for (int i = 0; i < routerSize; i++) {
            router = routers.get(i);

            old.clear();
            // ������=��ֵ��Ϊ���ã�����Ҳ��дRouters��equals����
            // Ȼ�������⣬addAll()Ҳ���У���������ΪArrayList<>�е���������
            // ���Լ�����Ķ��󣬲��ǻ������Ͱɣ����� ����Java������
            // old = router.getRouterTableItems();

            copy(old, router.getRouterTableItems());

            for (int j = 0; j < routerSize; j++) {
                router2 = routers.get(j);
                // ʹ���ھ�������
                if (Gui.getIsNeighbor()[i][j] != 0) {
                    distance = old.get(j).getDistance();
                    // ����ǰ�����·�����ľɵ�·�ɱ��Լ������������
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
