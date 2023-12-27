package leti.oop.coursework;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import leti.oop.coursework.models.Customer;
import leti.oop.coursework.models.HairDresser;
import leti.oop.coursework.models.Salon;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainController implements Initializable {
    @FXML public TextField newClientName;
    @FXML public VBox hairdressersVbox;
    @FXML public VBox clientsVbox;
    @FXML public VBox chairsVbox;
    @FXML public VBox hairdressersState;
    @FXML public VBox clientsState;
    @FXML public VBox chairsState;
    @FXML public TextField mastersAmount;
    @FXML public TextField apprenticesAmount;
    @FXML public TextField mastersTime;
    @FXML public TextField apprenticesTime;
    @FXML public RadioButton prefersMasters;
    @FXML public TextField chairsAmount;
    public Button createClient;

    Salon salon;

    private void updateClientsState(ObservableList<Customer> list) {
        List<HBox> children = list.stream().map(customer -> {
            HBox res = new HBox();
            res.setSpacing(10);

            Label customerIdLabel = new Label();
            customerIdLabel.setText(customer.getCustomerId());

            Label customerStatusLabel = new Label();
            customerStatusLabel.setText(customer.getState().getStatus());

            res.getChildren().addAll(customerIdLabel, customerStatusLabel);
            return res;
        }).toList();

        clientsState.getChildren().setAll(children);
    }

    private void updateChairState(ObservableList<Customer> list, int size) {
        List<HBox> children = IntStream
                .range(0, size)
                .mapToObj(i -> {
                    HBox res = new HBox();
                    res.setSpacing(10);

                    Label label = new Label();
                    try {
                        var customer = list.get(i);
                        label.setText(String.format("Chair %d is occupied by %s",i, customer.getCustomerId()));
                    } catch (IndexOutOfBoundsException ex) {
                        label.setText(String.format("Chair %d is empty", i));
                    }

                    res.getChildren().addAll(label);
                    return res;
                }).toList();
        List<Button> buttons = IntStream
                .range(0, list.size())
                .mapToObj(i -> {
                    var customer = list.get(i);
                    Button button = new Button();
                    button.setText(String.format("free chair %d occupied by customer %s", i, customer.getCustomerId()));
                    return button;
                })
                .toList();

        chairsState.getChildren().setAll(children);
        chairsVbox.getChildren().setAll(buttons);

    }
    private void updateHairdressersState(ObservableList<HairDresser> list) {
        List<HBox> children = list.stream().map(hairDresser -> {
            HBox res = new HBox();
            res.setSpacing(10);

            Label label = new Label();
            String text = hairDresser.getIsMaster() ? "Master hairdresser" : "Apprentice hairdresser";
            text += hairDresser.getIsBusy() ? " is processing user " + hairDresser.getProcessingCustomer().getCustomerId() : " is free";
            label.setText(text);

            res.getChildren().addAll(label);
            return res;
        }).toList();
        List<Button> buttons = list.stream().filter(HairDresser::getIsBusy).map(hairDresser -> {
            Button button = new Button();
            button.setOnAction(actionEvent -> {
                hairDresser.finishHairDressing();
            });
            button.setText(String.format("Complete processing customer %s", hairDresser.getProcessingCustomer().getCustomerId()));

            return button;
        }).toList();

        hairdressersState.getChildren().setAll(children);
        hairdressersVbox.getChildren().setAll(buttons);
    }

    private Integer parseNullableInt(String num) {
        if(num == null || num.isEmpty()) return 0;
        else return Integer.parseInt(num);
    }

    private IntegerProperty amountOfMasters = new SimpleIntegerProperty(1);
    private IntegerProperty amountOfApprentices = new SimpleIntegerProperty(3);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        salon = new Salon(amountOfMasters.get(), amountOfApprentices.get());

        updateChairState(salon.waitingRoom.chairs, salon.waitingRoom.maxChairs.get());
        updateClientsState(salon.customers);
        updateHairdressersState(salon.hairDressers);

        salon.customers.addListener((ListChangeListener<Customer>) change -> {
            updateClientsState((ObservableList<Customer>) change.getList());
        });

        salon.hairDressers.addListener((ListChangeListener<HairDresser>) change -> {
            updateHairdressersState((ObservableList<HairDresser>) change.getList());
        });

        salon.waitingRoom.chairs.addListener((ListChangeListener<Customer>) change -> {
            updateChairState((ObservableList<Customer>) change.getList(), salon.waitingRoom.maxChairs.get());
        });

        StringConverter<? extends Number> converter = new IntegerStringConverter();
        Bindings.bindBidirectional(chairsAmount.textProperty(), salon.waitingRoom.maxChairs,  (StringConverter<Number>)converter);
        chairsAmount.textProperty().addListener((observableValue, s, t1) -> {
            updateChairState(salon.customers, salon.waitingRoom.maxChairs.get());
        });

        Bindings.bindBidirectional(apprenticesAmount.textProperty(), amountOfApprentices,  (StringConverter<Number>)converter);
        Bindings.bindBidirectional(mastersAmount.textProperty(), amountOfMasters,  (StringConverter<Number>)converter);

        mastersAmount.textProperty().addListener((observableValue, s, t1) -> {
            salon.changeAmountOfHairdressers(amountOfMasters.get(), amountOfApprentices.get());
        });

        apprenticesAmount.textProperty().addListener((observableValue, s, t1) -> {
            salon.changeAmountOfHairdressers(amountOfMasters.get(), amountOfApprentices.get());
        });

        createClient.setOnAction(actionEvent -> {
            var name = newClientName.getText();
            if(name == null || name.isEmpty()) name = "unknown";
            salon.inviteCustomer(name);
        });
    }
}