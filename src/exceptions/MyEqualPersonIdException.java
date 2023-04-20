package exceptions;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.ExceptionCounter;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private final int id;
    private static ExceptionCounter exceptionCounter = new ExceptionCounter();

    public MyEqualPersonIdException(int id) {
        this.id = id;
        exceptionCounter.addTotalExceptionCount();
        exceptionCounter.addIdExceptionCount(id);
    }

    @Override
    public void print() {
        System.out.println("epi-" + exceptionCounter.getTotalExceptionCount() +
                ", " + id + "-" + exceptionCounter.getIdExceptionCount(id));
    }
}