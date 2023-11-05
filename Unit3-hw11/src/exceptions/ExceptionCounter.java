package exceptions;

import java.util.HashMap;

public class ExceptionCounter {
    private int totalCount = 0;
    private final HashMap<Integer, Integer> idCount = new HashMap<>();

    public void addTotalCount() {
        totalCount++;
    }

    public void addIdCount(int personId) {
        if (!idCount.containsKey(personId)) {
            idCount.put(personId, 1);
        } else {
            idCount.replace(personId, idCount.get(personId) + 1);
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getIdCount(int id) {
        return idCount.get(id);
    }
}
