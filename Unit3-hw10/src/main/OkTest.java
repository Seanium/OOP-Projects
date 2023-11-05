package main;

import java.util.HashMap;

public class OkTest {

    public static int modifyRelationOkTest(
            int id1, int id2, int value,
            HashMap<Integer, HashMap<Integer, Integer>> beforeData,
            HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (!beforeData.containsKey(id1) || !beforeData.containsKey(id2)) {
            if (beforeData.equals(afterData)) {
                return 0;
            } else {
                return -1;
            }
        }
        if (!beforeData.get(id1).containsKey(id2)) {
            if (beforeData.equals(afterData)) {
                return 0;
            } else {
                return -1;
            }
        }
        if (beforeData.size() != afterData.size()) {
            return 1;
        }
        for (Integer beforeId : beforeData.keySet()) {
            if (!afterData.containsKey(beforeId)) {
                return 2;
            }
        }
        for (Integer beforeId : beforeData.keySet()) {
            if (beforeId != id1 && beforeId != id2) {
                if (!beforeData.get(beforeId).equals(afterData.get(beforeId))) {
                    return 3;
                }
            }
        }
        int oldValue = beforeData.get(id1).get(id2);
        int newValue = oldValue + value;
        if (newValue > 0) {
            return modifyRelationOkTestBranch1(id1, id2, beforeData, afterData, newValue);
        } else {
            return modifyRelationOkTestBranch2(id1, id2, beforeData, afterData);
        }
    }

    private static int modifyRelationOkTestBranch1(
            int id1, int id2, HashMap<Integer, HashMap<Integer, Integer>> beforeData,
            HashMap<Integer, HashMap<Integer, Integer>> afterData, int newValue) {
        if (!afterData.get(id1).containsKey(id2) || !afterData.get(id2).containsKey(id1)) {
            return 4;
        }
        if (afterData.get(id1).get(id2) != newValue) {
            return 5;
        }
        if (afterData.get(id2).get(id1) != newValue) {
            return 6;
        }
        if (afterData.get(id1).size() != beforeData.get(id1).size()) {
            return 7;
        }
        if (afterData.get(id2).size() != beforeData.get(id2).size()) {
            return 8;
        }
        for (Integer i : beforeData.get(id1).keySet()) {
            if (!afterData.get(id1).containsKey(i)) {
                return 9;
            }
        }
        for (Integer i : beforeData.get(id2).keySet()) {
            if (!afterData.get(id2).containsKey(i)) {
                return 10;
            }
        }
        for (Integer i : beforeData.get(id1).keySet()) {
            if (i != id2) {
                if (!beforeData.get(id1).get(i).equals(afterData.get(id1).get(i))) {
                    return 11;
                }
            }
        }
        for (Integer i : beforeData.get(id2).keySet()) {
            if (i != id1) {
                if (!beforeData.get(id2).get(i).equals(afterData.get(id2).get(i))) {
                    return 12;
                }
            }
        }
        return 0;
    }

    private static int modifyRelationOkTestBranch2(
            int id1, int id2, HashMap<Integer, HashMap<Integer, Integer>> beforeData,
            HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (afterData.get(id1).containsKey(id2) || afterData.get(id2).containsKey(id1)) {
            return 15;
        }
        if (afterData.get(id1).size() != beforeData.get(id1).size() - 1) {
            return 16;
        }
        if (afterData.get(id2).size() != beforeData.get(id2).size() - 1) {
            return 17;
        }
        for (Integer i : afterData.get(id1).keySet()) {
            if (!beforeData.get(id1).containsKey(i)) {
                return 20;
            }
            if (!beforeData.get(id1).get(i).equals(afterData.get(id1).get(i))) {
                return 20;
            }
        }
        for (Integer i : afterData.get(id2).keySet()) {
            if (!beforeData.get(id2).containsKey(i)) {
                return 21;
            }
            if (!beforeData.get(id2).get(i).equals(afterData.get(id2).get(i))) {
                return 21;
            }
        }
        return 0;
    }
}
