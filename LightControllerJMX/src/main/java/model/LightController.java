package model;

import gui.Gui;

import javax.management.AttributeChangeNotification;
import javax.management.NotificationBroadcasterSupport;
import java.util.ArrayList;
import java.util.List;

public class LightController extends NotificationBroadcasterSupport implements LightControllerMBean {
    private final Gui gui;

    private Sequence sequence = new Sequence(" ");

    private int notificationId = 1;

    public LightController(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void setSequence(String sequence) {
        String[] numbers = sequence.split(" ");
        for (String number : numbers) {
            int lightNumber = Integer.parseInt(number);
            if (lightNumber >= gui.getLightsCount()) {
                var notification = new AttributeChangeNotification(
                        this,
                        notificationId++,
                        System.currentTimeMillis(),
                        "Invalid sequence format!",
                        "sequence",
                        "String",
                        getSequence(),
                        sequence
                );
                sendNotification(notification);
                return;
            }
        }
        var notification = new AttributeChangeNotification(
                this,
                notificationId++,
                System.currentTimeMillis(),
                "Sequence changed",
                "sequence",
                "String",
                getSequence(),
                sequence
        );
        sendNotification(notification);
        this.sequence = new Sequence(sequence);
        gui.setSequence(this.sequence);
    }


    @Override
    public String getSequence() {
        List<String> stringList = new ArrayList<>();
        for (Integer number : sequence.getSequenceList()) {
            stringList.add(number.toString());
        }

        return String.join(" ", stringList);
    }

    @Override
    public void switchLight(int id) {
        boolean lightState = !gui.getLightState(id);
        String statusMessage = "Light " + id + " has been switched " + (lightState ? "on" : "off");

        var notification = new AttributeChangeNotification(
                this,
                notificationId++,
                System.currentTimeMillis(),
                statusMessage,
                "light",
                "boolean",
                lightState,
                !lightState
        );
        sendNotification(notification);
        gui.switchLight(id);
    }


    @Override
    public void switchSimulation() {
        boolean simulationState = !gui.getSimulationState();
        String statusMessage = simulationState ? "The simulation status has been switched On" : "The simulation status has been switched Off";

        var notification = new AttributeChangeNotification(
                this,
                notificationId++,
                System.currentTimeMillis(),
                statusMessage,
                "simulation",
                "boolean",
                simulationState,
                !simulationState
        );
        sendNotification(notification);
        gui.switchSimulation();
    }

}
