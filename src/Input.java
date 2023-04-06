import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.MaintainRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;

public class Input extends Thread {
    private final Table table;

    public Input(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                table.setEnd(true);
                break;
            } else {
                // a new valid request
                if (request instanceof PersonRequest) {
                    // a PersonRequest
                    // your code here
                    table.put((PersonRequest) request);
                } else if (request instanceof ElevatorRequest) {
                    // an ElevatorRequest
                    // your code here
                    Elevator elevator =
                            new Elevator(table, ((ElevatorRequest) request).getElevatorId(),
                                    ((ElevatorRequest) request).getFloor(),
                                    ((ElevatorRequest) request).getCapacity(),
                                    ((ElevatorRequest) request).getSpeed(),
                                    ((ElevatorRequest) request).getAccess());
                    elevator.start();
                } else if (request instanceof MaintainRequest) {
                    // an MaintainRequest
                    // your code here
                    table.setMaintainable(((MaintainRequest) request).getElevatorId(), true);
                    table.changeAcceptingCntBy(1);
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
