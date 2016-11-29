package com.piotrek.graingrowth.ui;

import com.piotrek.graingrowth.model.GrainStructure;
import com.piotrek.graingrowth.model.ca.Ca2d;
import com.piotrek.graingrowth.model.ca.CaFactory;
import com.piotrek.graingrowth.model.mc.Mc2d;
import com.piotrek.graingrowth.model.mc.McFactory;
import com.piotrek.graingrowth.type.InclusionType;
import com.piotrek.graingrowth.type.CaNeighbourhood;
import com.piotrek.graingrowth.type.McNeighbourhood;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User interface implementation using Swing.
 * Created by Piotrek on 18.10.2016.
 * @author Piotrek
 */
public class MainFrame extends JFrame {
    private static final int DEFAULT_SIZE = 250;
    private static final int DEFAULT_GRAINS = 50;
    private GridStatus gridStatus;
    private GrainStructure structure;
    private Ca2d ca;
    private Mc2d mc;
    private Dimension caSize;
    private List<Integer> grainList;
    private boolean selectedGrainsPainted;

    //=====GUI elements=====//
    private JPanel mainPanel;
    private JPanel optionPanel;
    private JPanel processPanel;
    private JComboBox<CaNeighbourhood> caNeighbourhoodCombobox;
    private JComboBox<Object> boundaryCombobox;
    private JButton createCaButton;
    private JLabel neighTypeLabel;
    private JLabel boundaryLabel;
    private JPanel cardPanel;
    private JPanel buttonPanel;
    private JButton returnButton;
    private JButton processButton;
    private JButton drawRandomButton;
    private JButton drawInclusionsButton;
    private JPanel actionButtonPanel;
    private JButton substructureButton;
    private JButton dualPhaseButton;
    private JButton resetButton;
    private JComboBox mcNeighbourhoodCombobox;
    private JLabel mcNeighbourhoodLabel;
    private JButton createMcButton;
    private JPanel createButtonPanel;
    //=====END OF GUI ELEMENTS=====//

    private class ProcessWorker extends SwingWorker {

        @Override
        protected Object doInBackground() throws Exception {
            processButton.setEnabled(false);
            gridStatus.proceed();
            do {
                structure.process();
                processPanel.repaint();
            } while(structure.isNotEnd());
            processButton.setEnabled(true);
            return null;
        }
    }

    public MainFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        optionPanel.setMinimumSize(new Dimension(800, 600));
        createCaButton.addActionListener(e -> createCaButtonActionPerformed());
        createMcButton.addActionListener(e -> createMcButtonActionPerformed());
        returnButton.addActionListener(e -> returnButtonActionPerformed());
        processButton.addActionListener(e -> processButtonActionPerformed());
        drawRandomButton.addActionListener(e -> drawRandomButtonActionPerformed());
        drawInclusionsButton.addActionListener(e -> drawInclusionsButtonActionPerformed());
        substructureButton.addActionListener(e -> generateNewStructure(grainList.size(), 0));
        dualPhaseButton.addActionListener(e -> generateNewStructure(1, 1));
        resetButton.addActionListener(e -> resetGrainGrowth());
        processPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double cellWidth = (double) processPanel.getWidth() / caSize.width,
                        cellHeight = (double) processPanel.getHeight() / caSize.height;
                double mX = e.getX(),
                        mY = e.getY();

                int cellY = (int) Math.floor(mX/cellWidth),
                        cellX = (int) Math.floor(mY/cellHeight);
                if(gridStatus.isProceeded()) processPanelAfterGrowthMouseClicked(cellX, cellY);
                else processPanelBeforeGrowthMouseClicked(cellX, cellY);
            }
        });
        grainList = new ArrayList<>();
        actionButtonPanel.setVisible(false);
        selectedGrainsPainted = false;
        add(mainPanel);
    }

    private void createUIComponents() {
        String tmp = JOptionPane.showInputDialog(this, "Initial cellular automaton size:", "Input", JOptionPane.QUESTION_MESSAGE);
        int dimX = DEFAULT_SIZE, dimY = DEFAULT_SIZE;
        try {
            String[] values;
            if(tmp != null && (values = tmp.split(",")).length == 2) {
                dimX = Integer.parseInt(values[0]);
                dimY = Integer.parseInt(values[1]);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Incorrect number of dimensions. Assumed default value " + DEFAULT_SIZE + ".",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Input value is not a number. Assumed default value " + DEFAULT_SIZE + ".",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            caSize = new Dimension(dimX, dimY);
        }
        gridStatus = new GridStatus(caSize);
        processPanel = new GridPanel(gridStatus);
        caNeighbourhoodCombobox = new JComboBox<>(CaNeighbourhood.values());
        mcNeighbourhoodCombobox = new JComboBox<>(McNeighbourhood.values());
        boundaryCombobox = new JComboBox<>(new Object[] {
           "Non-periodic", "Periodic"
        });
    }

    private void resetGrainGrowth() {
        selectedGrainsPainted = false;
        if(structure != null) {
            structure.setDefaultBoundaryValue();
            if(structure.getClass().getSuperclass().equals(Mc2d.class)) {
                ((Mc2d) structure).resetIterations();
            }
        }
        gridStatus.reset();
        grainList.clear();
        processPanel.repaint();
    }

    private void generateNewStructure(int boundaryVal, int type) {
        if(gridStatus.isProceeded()) {
            if(selectedGrainsPainted) {
                if(structure.getClass().getSuperclass().equals(Mc2d.class)) {
                    ((Mc2d) structure).resetIterations();
                }
                structure.setBoundaryValue(boundaryVal);
                processButtonActionPerformed();
            } else {
                if(type == 0) gridStatus.createSubstructure(grainList);
                else gridStatus.createDualPhase(grainList);
                processPanel.repaint();
                selectedGrainsPainted = true;
            }
        } else JOptionPane.showMessageDialog(
                this,
                "First you have to grow grains. Press Process button to do so.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    private void createCaButtonActionPerformed() {
        structure = CaFactory.getCa2dInstance(
                boundaryCombobox.getSelectedIndex() == 1,
                gridStatus.getStates(),
                (CaNeighbourhood) caNeighbourhoodCombobox.getSelectedItem());

        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "processCard");

        actionButtonPanel.setVisible(true);
        createButtonPanel.setVisible(false);
    }

    private void createMcButtonActionPerformed() {
        structure = McFactory.getMc2dInstance(gridStatus.getStates(),
                boundaryCombobox.getSelectedIndex() == 1,
                (McNeighbourhood) mcNeighbourhoodCombobox.getSelectedItem());
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "processCard");

        actionButtonPanel.setVisible(true);
        createButtonPanel.setVisible(false);
    }

    private void returnButtonActionPerformed() {
        resetGrainGrowth();

        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "optionCard");

        actionButtonPanel.setVisible(false);
        createButtonPanel.setVisible(true);
    }

    private void processButtonActionPerformed() {
        ProcessWorker worker = new ProcessWorker();
        worker.execute();
    }

    private void processPanelBeforeGrowthMouseClicked(int x, int y) {
        gridStatus.addSeed(new Point(x, y));
        processPanel.repaint();
    }

    private void processPanelAfterGrowthMouseClicked(int x, int y) {
        int val = gridStatus.getGrainId(x, y);
        if(!grainList.contains(val))
            grainList.add(val);
    }

    private void drawRandomButtonActionPerformed() {
        try {
            String val = JOptionPane.showInputDialog(this, "Input number of grains to be drawn:", "Input", JOptionPane.QUESTION_MESSAGE);
            structure.drawGrains(Integer.valueOf(val));
            gridStatus.generateColors();
            processPanel.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawInclusionsButtonActionPerformed() {
        InclusionType type = (InclusionType) JOptionPane.showInputDialog(this, "Choose inclusion type:", "Inclusions",
                JOptionPane.QUESTION_MESSAGE, null, InclusionType.values(), InclusionType.SQUARE);
        try {
            int radius = Integer.valueOf(JOptionPane.showInputDialog(this, "Inclusion radius:", "Inclusions", JOptionPane.QUESTION_MESSAGE));
            if(gridStatus.isProceeded())
                ((Ca2d)structure).drawInclusionsAfter(type, radius);
            else
                ((Ca2d)structure).drawInclusionBefore(type, radius);
            processPanel.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input value is not a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
