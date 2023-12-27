package leti.oop.coursework.models;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javax.security.auth.login.AccountExpiredException;
import java.util.ArrayList;

public class Salon {
    public ObservableList<HairDresser> hairDressers = FXCollections.observableArrayList();
    public WaitingRoom waitingRoom = new WaitingRoom();
    public ObservableList<Customer> customers = FXCollections.observableArrayList();



    public Salon(int amountOfMasters, int amountOfApprentices) {
        changeAmountOfHairdressers(amountOfMasters, amountOfApprentices);
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

    public void changeAmountOfHairdressers(Integer amountOfMasters, Integer amountOfApprentices) {
        if(amountOfMasters == null) amountOfMasters = 0;
        if(amountOfApprentices == null) amountOfApprentices = 0;

        ArrayList<HairDresser> hairdressersNew = new ArrayList<>(amountOfApprentices + amountOfMasters);
        for(int i = 0; i < amountOfMasters; ++i)
            hairdressersNew.add(HairDresser.createMaster());
        for(int i = 0; i < amountOfApprentices; ++i)
            hairdressersNew.add(HairDresser.createApprentice());
        hairDressers.setAll(hairdressersNew);
    }
}
