package exceptions;

import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int id;
    private static ExceptionCounter exceptionCounter = new ExceptionCounter();

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        exceptionCounter.addTotalCount();
        exceptionCounter.addIdCount(id);
    }

    @Override
    public void print() {
        System.out.println("pinf-" + exceptionCounter.getTotalCount() +
                ", " + id + "-" + exceptionCounter.getIdCount(id));
    }
}
