package com.piotrek.graingrowth.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Energy visualisation form. Shows energy distribution before recrystallisation in material (after DMR).
 * Created by Piotr on 11.12.2016.
 * @author Piotr Hajder
 */
class EnergyVisualisation extends JFrame {
    private JPanel energyPanel;

    EnergyVisualisation(Integer[][] states) {
        GridStatus gridStatus = new GridStatus(states);
        gridStatus.generateRandomColors();
        energyPanel = new GridPanel(gridStatus);
        add(energyPanel);
    }

    private void createUIComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
    }

    void visualiseEnergy() {
        energyPanel.repaint();
    }
}
