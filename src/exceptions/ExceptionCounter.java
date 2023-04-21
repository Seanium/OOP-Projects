package exceptions;

import java.util.HashMap;

public class ExceptionCounter {
    private int totalExceptionCount = 0;
    private final HashMap<Integer, Integer> idExceptionCount = new HashMap<>();

    public void addTotalExceptionCount() {
        totalExceptionCount++;
    }

    public void addIdExceptionCount(int personId) {
        if (!idExceptionCount.containsKey(personId)) {
            idExceptionCount.put(personId, 1);
        } else {
            idExceptionCount.replace(personId, idExceptionCount.get(personId) + 1);
        }
    }

    public int getTotalExceptionCount() {
        return totalExceptionCount;
    }

    public int getIdExceptionCount(int personId) {
        return idExceptionCount.get(personId);
    }
}
