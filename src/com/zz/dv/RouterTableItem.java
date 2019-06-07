package com.zz.dv;

/**
 * @author Zhang Zhen
 * @time 2019年6月3日 下午2:22:57
 */
public class RouterTableItem {
    private int distance;
    private int nextHop;

    public RouterTableItem(int distance, int nextHop) {
        this.distance = distance;
        this.nextHop = nextHop;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getNextHop() {
        return nextHop;
    }

    public void setNextHop(int nextHop) {
        this.nextHop = nextHop;
    }

    @Override
    public boolean equals(Object obj) {
        // 好久不用，都要忘记了
        if (obj instanceof RouterTableItem) {
            RouterTableItem routerTableItem = (RouterTableItem) obj;
            return distance == routerTableItem.distance && nextHop == routerTableItem.nextHop;
        }
        return false;
    }

    // just for test!!!
    @Override
    public String toString() {
        return "[distance, nextHop]: [" + distance + ", " + nextHop + "]";
    }
}
