package exceptions;

import com.oocourse.spec1.exceptions.EqualRelationException;

public class MyEqualRelationException extends EqualRelationException {
    private final int smallId;
    private final int bigId;
    private static ExceptionCounter exceptionCounter = new ExceptionCounter();

    public MyEqualRelationException(int id1, int id2) {
        this.smallId = Math.min(id1, id2);
        this.bigId = Math.max(id1, id2);
        exceptionCounter.addTotalExceptionCount();
        if (smallId == bigId) {
            exceptionCounter.addIdExceptionCount(smallId);
        } else {
            exceptionCounter.addIdExceptionCount(smallId);
            exceptionCounter.addIdExceptionCount(bigId);
        }
    }

    @Override
    public void print() {
        System.out.println("er-" + exceptionCounter.getTotalExceptionCount() +
                ", " + smallId + "-" + exceptionCounter.getIdExceptionCount(smallId) +
                ", " + bigId + "-" + exceptionCounter.getIdExceptionCount(bigId));
    }
}
