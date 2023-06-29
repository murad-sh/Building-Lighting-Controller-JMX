package gui;

import javax.swing.*;
import java.awt.*;

public class LightButton extends JButton {
    private static final int PADDING_X = 3;
    private static final int PADDING_Y = 10;

    public LightButton(String label) {
        super(label);
        Dimension size = getPreferredSize();
        int buttonSize = Math.min(size.width, size.height);
        size.width = size.height = Math.max(buttonSize, 30) + (2 * PADDING_Y);
        setPreferredSize(size);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isArmed()) {
            g2d.setColor(Color.LIGHT_GRAY);
        } else {
            g2d.setColor(getBackground());
        }

        int diameter = Math.min(getWidth(), getHeight()) - (2 * Math.max(PADDING_X, PADDING_Y));
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;
        g2d.fillOval(x, y, diameter, diameter);

        g2d.setFont(new Font("Consolas", Font.PLAIN, 15));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(getForeground());
        String label = getText();
        int labelX = (getWidth() - fm.stringWidth(label)) / 2;
        int labelY = (getHeight() + fm.getAscent()) / 2;
        g2d.drawString(label, labelX, labelY);
        g2d.dispose();
    }


    @Override
    public boolean contains(int x, int y) {
        int diameter = Math.min(getWidth(), getHeight()) - (2 * Math.max(PADDING_X, PADDING_Y));
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        return Point.distance(x, y, centerX, centerY) < diameter;
    }
}



