package exceptions;

import com.oocourse.spec3.exceptions.RelationNotFoundException;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private final int smallId;
    private final int bigId;
    private static ExceptionCounter exceptionCounter = new ExceptionCounter();

    public MyRelationNotFoundException(int id1, int id2) {
        this.smallId = Math.min(id1, id2);
        this.bigId = Math.max(id1, id2);
        exceptionCounter.addTotalCount();
        exceptionCounter.addIdCount(smallId);
        exceptionCounter.addIdCount(bigId);
    }

    @Override
    public void print() {
        System.out.println("rnf-" + exceptionCounter.getTotalCount() +
                ", " + smallId + "-" + exceptionCounter.getIdCount(smallId) +
                ", " + bigId + "-" + exceptionCounter.getIdCount(bigId));
    }
}
