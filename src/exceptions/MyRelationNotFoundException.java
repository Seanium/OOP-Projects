package exceptions;

import com.oocourse.spec1.exceptions.RelationNotFoundException;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private final int smallId;
    private final int bigId;
    private static ExceptionCounter exceptionCounter = new ExceptionCounter();

    public MyRelationNotFoundException(int id1, int id2) {
        this.smallId = Math.min(id1, id2);
        this.bigId = Math.max(id1, id2);
        exceptionCounter.addTotalExceptionCount();
        exceptionCounter.addIdExceptionCount(smallId);
        exceptionCounter.addIdExceptionCount(bigId);
    }

    @Override
    public void print() {
        System.out.println("rnf-" + exceptionCounter.getTotalExceptionCount() +
                ", " + smallId + "-" + exceptionCounter.getIdExceptionCount(smallId) +
                ", " + bigId + "-" + exceptionCounter.getIdExceptionCount(bigId));
    }
}
