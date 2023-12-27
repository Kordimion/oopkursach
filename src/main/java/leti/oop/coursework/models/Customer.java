package leti.oop.coursework.models;

public class Customer {
    public enum CustomerState {
        UNPROCESSED,
        WAITING,
        PROCESSING,
        LEFT;
    }

    private CustomerState state = CustomerState.UNPROCESSED;
    private String customerId;

    public Customer(String id) {
        customerId = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String newId) {
        customerId = newId;
    }

    public void startHairDressing() {
        state = CustomerState.PROCESSING;
    }
    public void waitForAvailableHairDresser() {
        state = CustomerState.WAITING;
    }
    public void finishHairDressing() {
        state = CustomerState.LEFT;
    }
    public void leaveBecauseWaitingRoomFull() {
        state = CustomerState.LEFT;
    }
}
