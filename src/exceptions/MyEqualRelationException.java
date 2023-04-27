package exceptions;

import com.oocourse.spec2.exceptions.EqualRelationException;

public class MyEqualRelationException extends EqualRelationException {
    private final int smallId;
    private final int bigId;
    private static ExceptionCounter exceptionCounter = new ExceptionCounter();

    public MyEqualRelationException(int id1, int id2) {
        this.smallId = Math.min(id1, id2);
        this.bigId = Math.max(id1, id2);
        exceptionCounter.addTotalCount();
        if (smallId == bigId) {
            exceptionCounter.addIdCount(smallId);
        } else {
            exceptionCounter.addIdCount(smallId);
            exceptionCounter.addIdCount(bigId);
        }
    }

    @Override
    public void print() {
        System.out.println("er-" + exceptionCounter.getTotalCount() +
                ", " + smallId + "-" + exceptionCounter.getIdCount(smallId) +
                ", " + bigId + "-" + exceptionCounter.getIdCount(bigId));
    }
}
