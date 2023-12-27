package leti.oop.coursework.models;

public class Customer {
    public enum CustomerState {
        UNPROCESSED("клиент еще не подстрижен"),
        WAITING("клиент ожидает"),
        PROCESSING("клиента стригут"),
        LEFT("клиент ушел");

        CustomerState(String state) { res = state;};
        private final String res;
        public String getStatus() { return res; }
    }

    private CustomerState state = CustomerState.UNPROCESSED;
    private String customerId;

    public Customer(String id) {
        customerId = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public CustomerState getState() {
        return state;
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
