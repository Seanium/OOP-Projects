package exceptions;

import com.oocourse.spec2.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private final int id;
    private static ExceptionCounter exceptionCounter = new ExceptionCounter();

    public MyEqualGroupIdException(int id) {
        this.id = id;
        exceptionCounter.addTotalCount();
        exceptionCounter.addIdCount(id);
    }

    @Override
    public void print() {
        System.out.println("egi-" + exceptionCounter.getTotalCount() +
                ", " + id + "-" + exceptionCounter.getIdCount(id));
    }
}