package leti.oop.coursework.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.concurrent.*;

public class HairDresser {

    private boolean isMaster;
    private BooleanProperty isBusy = new SimpleBooleanProperty(false);
    private Customer customer;
    private double customerProcessingTimeInSeconds;

    public static HairDresser createMaster() {
        var res = new HairDresser();
        res.isMaster = true;
        res.customerProcessingTimeInSeconds = 5;
        return res;
    }

    public boolean getIsBusy() {
        return isBusy.get();
    }

    public Customer getProcessingCustomer() {
        return customer;
    }

    public boolean getIsMaster() {
        return isMaster;
    }

    public BooleanProperty getIsBusyProperty() {
        return isBusy;
    }

    public static HairDresser createApprentice() {
        var res = new HairDresser();
        res.isMaster = false;
        res.customerProcessingTimeInSeconds = 8;
        return res;
    }

    public void finishHairDressing() {
        if(customer != null) customer.finishHairDressing();
        isBusy.set(false);
        this.customer = null;
    }

    public Customer processCustomer(Customer customer) {
        this.customer = customer;

        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

        isBusy.set(true);
        customer.startHairDressing();
        exec.schedule(this::finishHairDressing, (long) customerProcessingTimeInSeconds, TimeUnit.SECONDS);

        return customer;
    }
}
