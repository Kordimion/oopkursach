package leti.oop.coursework.models;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Salon {
    private ObservableList<HairDresser> hairDressers = FXCollections.observableArrayList();
    private WaitingRoom waitingRoom = new WaitingRoom();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();



    public Salon(int amountOfMasters, int amountOfApprentices) {
        for(int i = 0; i < amountOfMasters; i++)
            hairDressers.add(HairDresser.createMaster());

        for(int i = 0; i < amountOfApprentices; i++)
            hairDressers.add(HairDresser.createApprentice());

        hairDressers.addListener(new ListChangeListener<HairDresser>() {
            @Override
            public void onChanged(Change<? extends HairDresser> change) {
                if(change.wasUpdated()) {

                }
            }
        });
    }

    public void inviteCustomer(String customerId) {
        var customer = new Customer(customerId);
        customers.add(customer);
        var availableMaster = hairDressers.stream()
                .filter(HairDresser::getIsBusy)
                .sorted((o1, o2) -> {
                    if(o1.getIsMaster() && !o2.getIsMaster()) return 1;
                    if(o2.getIsMaster() && !o1.getIsMaster()) return -1;
                    return 0;
                })
                .findFirst();

        if(availableMaster.isEmpty()) {
            var chair = waitingRoom.searchForChair(customer);
            if(chair == null) customer.leaveBecauseWaitingRoomFull();
            else customer.waitForAvailableHairDresser();
        } else {
            availableMaster.get().processCustomer(customer);
        }

    }

    public void changeAmountOfHairdressers(int amountOfMasters, int amountOfApprentices) {
    }
}
