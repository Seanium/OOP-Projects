package exceptions;

import com.oocourse.spec3.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private final int id;
    private static ExceptionCounter exceptionCounter = new ExceptionCounter();

    public MyEqualMessageIdException(int id) {
        this.id = id;
        exceptionCounter.addTotalCount();
        exceptionCounter.addIdCount(id);
    }

    @Override
    public void print() {
        System.out.println("emi-" + exceptionCounter.getTotalCount() +
                ", " + id + "-" + exceptionCounter.getIdCount(id));
    }
}
