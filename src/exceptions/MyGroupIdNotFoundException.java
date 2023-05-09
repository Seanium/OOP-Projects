package exceptions;

import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private final int id;
    private static ExceptionCounter exceptionCounter = new ExceptionCounter();

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        exceptionCounter.addTotalCount();
        exceptionCounter.addIdCount(id);
    }

    @Override
    public void print() {
        System.out.println("ginf-" + exceptionCounter.getTotalCount() +
                ", " + id + "-" + exceptionCounter.getIdCount(id));
    }
}
