package exceptions;

import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private final int id;
    private static ExceptionCounter exceptionCounter = new ExceptionCounter();

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        exceptionCounter.addTotalCount();
        exceptionCounter.addIdCount(id);
    }

    @Override
    public void print() {
        System.out.println("minf-" + exceptionCounter.getTotalCount() +
                ", " + id + "-" + exceptionCounter.getIdCount(id));
    }
}
