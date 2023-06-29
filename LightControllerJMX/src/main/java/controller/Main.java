package controller;

import com.formdev.flatlaf.FlatLightLaf;
import gui.Gui;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.install();
        new Gui();
    }
}
