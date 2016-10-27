package com.piotrek.ca2d.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by pioot on 18.10.2016.
 */
class GridPanel extends JPanel {
    private final GridStatus gridStatus;

    GridPanel(GridStatus gridStatus) {
        this.gridStatus = gridStatus;
    }

    @Override
    public void paintComponent(Graphics g) {
        float height = (float) getHeight() / gridStatus.getDim().height;
        float width = (float) getWidth() / gridStatus.getDim().width;

        Graphics2D g2d = (Graphics2D) g;

        int counter;
        Integer[][] tab = gridStatus.getStates();
        ArrayList<Color> cl = gridStatus.getColorList();

        for (int y = 0; y < gridStatus.getDim().height; y++) {
            for (int x = 0; x < gridStatus.getDim().width; x++) {
                //set cell colour
                if(tab != null) {
                    counter = tab[y][x];
                    if(counter == 0) { //empty space
                        g2d.setColor(Color.WHITE);
                    } else if(counter == -1) { //inclusions
                        g2d.setColor(Color.BLACK);
                    } else { //else
                        g2d.setColor(cl.get(counter - 1));
                    }
                } else {
                    g2d.setColor(Color.WHITE);
                }
                // defines the rectangle painted
                Rectangle2D r2d = new Rectangle2D.Float(x * width, y
                        * height, width, height);
                // fills the rectangle, sets the color to black and then
                // draws the grid
                g2d.fill(r2d);
                //g2d.setStroke(new BasicStroke(1));
                //g2d.setColor(Color.GRAY);
                g2d.draw(r2d);
            }
        }
    }
}
