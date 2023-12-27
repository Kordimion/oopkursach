package leti.oop.coursework.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class WaitingRoom {

    private static final int INITIAL_CAPACITY = 5;
    public ObservableList<Customer> chairs = FXCollections.observableArrayList();
    public IntegerProperty maxChairs = new SimpleIntegerProperty(WaitingRoom.INITIAL_CAPACITY);
    public IntegerProperty currentChairs = new SimpleIntegerProperty(0);

    public Integer searchForChair(Customer customer) {
        if(chairs.size() < maxChairs.get()) {
            chairs.add(customer);
            return chairs.size();
        }
        else
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
