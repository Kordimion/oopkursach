package leti.oop.coursework.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

public class WaitingRoom {

    private static final int INITIAL_CAPACITY = 5;
    private ArrayList<Customer> chairs = new ArrayList<>(WaitingRoom.INITIAL_CAPACITY);
    private IntegerProperty maxChairs = new SimpleIntegerProperty(WaitingRoom.INITIAL_CAPACITY);
    private IntegerProperty currentChairs = new SimpleIntegerProperty(0);

    public Integer searchForChair(Customer customer) {
        chairs.add(customer);
        for(int i = 0; i < maxChairs.get(); ++i) {
            if(chairs.get(i) == null) {
                chairs.set(i, customer);
                currentChairs.add(1);
                return i;
            }
        }
        return null;
    }

    public void resize(int newSize) {
        var prevSize = maxChairs.get();
        maxChairs.set(newSize);
        for(int i = newSize; i < prevSize; ++i) {
            var res = searchForChair(chairs.get(i));
            if(res == null) chairs.get(i).leaveBecauseWaitingRoomFull();
        }
    }
}
