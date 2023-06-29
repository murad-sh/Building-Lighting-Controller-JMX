package gui;

import com.formdev.flatlaf.FlatLightLaf;
import model.LightController;
import model.Sequence;

import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class Gui implements NotificationListener {

    private final JFrame frame;
    private final LightController controller;
    private final LightButton[] lightButtons;
    private final boolean[] lightStates;
    private Sequence sequence;
    private ScheduledExecutorService simulation;

    private static final int LIGHTS_COUNT = 10;

    public Gui() {
        controller = new LightController(this);
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName controllerName = new ObjectName("model:type=LightController");
            mbs.registerMBean(controller, controllerName);
            controller.addNotificationListener(this, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Create GUI
        frame = new JFrame("Light Controller");
        frame.setSize(850, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Press the buttons below to switch the lights on and off :");
        label.setFont(new Font("Consolas", Font.PLAIN, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        int padding = 15;
        EmptyBorder border = new EmptyBorder(padding, padding, padding, padding);
        label.setBorder(border);
        frame.add(label, BorderLayout.NORTH);


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3));

        lightButtons = new LightButton[LIGHTS_COUNT];
        lightStates = new boolean[LIGHTS_COUNT];
        for (int i = 0; i < LIGHTS_COUNT; i++) {
            lightButtons[i] = new LightButton(i + " : OFF");
            lightStates[i] = false;
            lightButtons[i].setFocusable(false);
            lightButtons[i].setBackground(Color.LIGHT_GRAY);
            int id = i;
            lightButtons[i].addActionListener(e -> controller.switchLight(id));
            panel.add(lightButtons[i]);
        }

        JButton switchSimulationButton = new JButton("Switch Simulation");
        switchSimulationButton.addActionListener(e -> controller.switchSimulation());
        switchSimulationButton.setFocusable(false);
        switchSimulationButton.setFont(new Font("Arial", Font.PLAIN, 16));
        switchSimulationButton.setPreferredSize(new Dimension(200, 50));

        JButton setSequenceButton = new JButton("Set Sequence");
        setSequenceButton.addActionListener(e -> setSequenceGUI());
        setSequenceButton.setFocusable(false);
        setSequenceButton.setFont(new Font("Arial", Font.PLAIN, 16));
        setSequenceButton.setPreferredSize(new Dimension(200, 50));

        JButton showSequenceButton = new JButton("Show Sequence");
        showSequenceButton.addActionListener(e -> showSequence());
        showSequenceButton.setFocusable(false);
        showSequenceButton.setFont(new Font("Arial", Font.PLAIN, 16));
        showSequenceButton.setPreferredSize(new Dimension(200, 50));


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(switchSimulationButton);
        buttonPanel.add(setSequenceButton);
        buttonPanel.add(showSequenceButton);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

    }

    @Override
    public void handleNotification(Notification notification, Object handback) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, notification.getMessage(), "Notification", JOptionPane.INFORMATION_MESSAGE));
    }

    private void setSequenceGUI() {
        String maxLights = String.valueOf(LIGHTS_COUNT - 1);
        String input = JOptionPane.showInputDialog(frame, "Enter the sequence (0-" + maxLights + ") with spaces):");
        if (input != null) {
            try {
                String[] numbers = input.split(" ");
                for (String number : numbers) {
                    int lightNumber = Integer.parseInt(number);
                    if (lightNumber >= LIGHTS_COUNT) {
                        throw new IllegalArgumentException("Invalid sequence format!");
                    }
                }
                controller.setSequence(input);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid sequence format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void showSequence() {
        String currentSequence = controller.getSequence();
        JOptionPane.showMessageDialog(frame, "Current Sequence : " + currentSequence, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void switchLight(int id) {
        lightStates[id] = !lightStates[id];
        lightButtons[id].setText(lightStates[id] ? id + " : ON" : id + " : OFF");
        lightButtons[id].setBackground(lightStates[id] ? Color.GREEN : Color.LIGHT_GRAY);
    }

    public boolean getLightState(int id) {
        return lightStates[id];
    }

    public boolean getSimulationState() {
        if (simulation == null || simulation.isShutdown()) return false;
        return true;
    }

    public void switchSimulation() {
        if (sequence == null) return;

        if (simulation == null || simulation.isShutdown()) {
            simulation = Executors.newSingleThreadScheduledExecutor();
            simulation.scheduleAtFixedRate(() -> switchLight(sequence.getNextNumber()), 0, 1000, TimeUnit.MILLISECONDS);

        } else {
            simulation.shutdown();
        }
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }



    public int getLightsCount() {
        return LIGHTS_COUNT;
    }
}




