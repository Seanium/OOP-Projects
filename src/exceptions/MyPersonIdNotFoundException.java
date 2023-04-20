package exceptions;

import com.oocourse.spec1.exceptions.ExceptionCounter;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int id;
    private static ExceptionCounter exceptionCounter = new ExceptionCounter();

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        exceptionCounter.addTotalExceptionCount();
        exceptionCounter.addIdExceptionCount(id);
    }

    @Override
    public void print() {
        System.out.println("pinf-" + exceptionCounter.getTotalExceptionCount() +
                ", " + id + "-" + exceptionCounter.getIdExceptionCount(id));
    }
}
