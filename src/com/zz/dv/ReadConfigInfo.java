package com.zz.dv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhang Zhen
 * @time 2019年6月3日 上午10:22:30
 */
public class ReadConfigInfo {
    public static int[][] readConfigInfo(String configFilePath) throws Exception {
        List<List<Integer>> infoList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(configFilePath));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            List<Integer> tempList = new ArrayList<>();
            String[] tempStrs = line.trim().split(" ");
            for (String eachStr : tempStrs) {
                tempList.add(Integer.parseInt(eachStr));
            }
            infoList.add(tempList);
        }
        bufferedReader.close();

        int size = infoList.size();
        int[][] configInfoArray = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < infoList.get(i).size(); j++) {
                configInfoArray[i][j] = infoList.get(i).get(j);
            }
        }
        return configInfoArray;
    }
}
