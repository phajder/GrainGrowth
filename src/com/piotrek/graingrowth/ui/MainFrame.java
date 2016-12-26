package com.piotrek.graingrowth.ui;

import com.piotrek.graingrowth.model.GrainStructure;
import com.piotrek.graingrowth.model.RecrystallizationParams;
import com.piotrek.graingrowth.model.ca.CaFactory;
import com.piotrek.graingrowth.model.mc.Mc2d;
import com.piotrek.graingrowth.model.mc.McFactory;
import com.piotrek.graingrowth.type.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User interface implementation using Swing.
 * Created by Piotr on 18.10.2016.
 * @author Piotr Hajder
 */
public class MainFrame extends JFrame {
    private static final String GRAIN_GROWTH = "grain_growth";
    private static final String RECRYSTALLIZATION = "recrystallization";

    private static final int DEFAULT_SIZE = 250;

    /**
     * Object used for visual representation of generated microstructure.
     */
    private GridStatus gridStatus;

    /**
     * DMR object.
     */
    private GrainStructure structure;

    /**
     * Static recrystallization parameters.
     */
    private RecrystallizationParams params;

    /**
     * Calculation space dimension.
     */
    private Dimension caSize;

    /**
     * List of grains, selected to stay in substructure.
     */
    private List<Integer> grainList;

    /**
     * Grain growth method, selected in main panel (by button).
     */
    private MethodType basicMethod;

    /**
     * If false, dual phase or substructure is prepared.
     * Otherwise microstructure generation is proceeded.
     */
    private boolean selectedGrainsPainted;

    /**
     * Checks if recrystallization has been set.
     */
    private boolean setup;

    //=====GUI elements=====//
    private JPanel mainPanel;
    private JPanel optionPanel;
    private JPanel processPanel;
    private JComboBox<CaNeighbourhood> caNeighbourhoodComboBox;
    private JComboBox<Object> boundaryComboBox;
    private JButton createCaButton;
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
    private JComboBox mcNeighbourhoodComboBox;
    private JButton createMcButton;
    private JPanel createButtonPanel;
    private JSpinner maxIterSpinner;
    private JPanel dmrButtonPanel;
    private JPanel recrystallizationButtonPanel;
    private JButton showEnergyButton;
    private JComboBox energyComboBox;
    private JComboBox nucleationPlacementComboBox;
    private JComboBox nucleationTypeComboBox;
    private JButton recrystallizationButton;
    //=====END OF GUI ELEMENTS=====//

    private class ProcessWorker extends SwingWorker {
        private String simulationType;

        ProcessWorker(String simulationType) {
            super();
            this.simulationType = simulationType;
        }

        @Override
        protected Object doInBackground() throws Exception {
            switch (simulationType) {
                case GRAIN_GROWTH:
                    doGrainGrowth();
                    break;
                case RECRYSTALLIZATION:
                    doRecrystallization();
                    break;
                default:
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Program internal error.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    break;
            }
            return null;
        }

        private void doGrainGrowth() {
            processButton.setEnabled(false);
            gridStatus.proceed();
            do {
                structure.process();
                processPanel.repaint();
            } while(structure.isNotEnd());
            processButton.setEnabled(true);
        }

        private void doRecrystallization() {
            if(!setup) {
                structure = McFactory.getMc2dInstance(boundaryComboBox.getSelectedIndex() == 1,
                        gridStatus.getStates(),
                        (McNeighbourhood) mcNeighbourhoodComboBox.getSelectedItem());
                ((Mc2d)structure).setMaxIterations((int) maxIterSpinner.getValue());

                if(energyComboBox.getSelectedIndex() == 0) {
                    structure.distributeHomogeneous();
                } else {
                    structure.distributeOnGrainBoundaries();
                }

                structure.recrystallizationSetup();
                params.setup();

                setup = true;
            }

            recrystallizationButton.setEnabled(false);
            do {
                structure.recrystallize(params);
                gridStatus.generateRecrystallizationColors();
                processPanel.repaint();
            } while(structure.isNotEnd());

            recrystallizationButton.setEnabled(true);
        }
    }

    public MainFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 600));
        optionPanel.setMinimumSize(new Dimension(1024, 600));
        createCaButton.addActionListener(e -> createCaButtonActionPerformed());
        createMcButton.addActionListener(e -> createMcButtonActionPerformed());
        returnButton.addActionListener(e -> returnButtonActionPerformed());
        processButton.addActionListener(e -> processButtonActionPerformed());
        drawRandomButton.addActionListener(e -> drawRandomButtonActionPerformed());
        drawInclusionsButton.addActionListener(e -> drawInclusionsButtonActionPerformed());
        substructureButton.addActionListener(e -> generateNewStructure(grainList.size(), 0));
        dualPhaseButton.addActionListener(e -> generateNewStructure(1, 1));
        resetButton.addActionListener(e -> resetGrainGrowth());

        showEnergyButton.addActionListener(e -> showEnergyButtonActionPerformed());
        recrystallizationButton.addActionListener(e -> recrystallizationButtonActionPerformed());
        nucleationPlacementComboBox.addItemListener(e ->
                params.setNucleationOnBoundaries(nucleationPlacementComboBox.getSelectedIndex() == 1));
        nucleationTypeComboBox.addItemListener(e -> {
            params.setNucleationType((NucleationType) nucleationTypeComboBox.getSelectedItem());
            if(nucleationTypeComboBox.getSelectedItem().equals(NucleationType.AT_START)) {
                String val = JOptionPane.showInputDialog(this,
                        "Select number of nucleons to generate at the beginning.",
                        "Maximum nucleons to generate",
                        JOptionPane.QUESTION_MESSAGE);
                try {
                    int parsed = Integer.valueOf(val);
                    params.setMaxNucleons(parsed);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Wrong number format. Assuming default value.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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
        setup = false;
        params = new RecrystallizationParams();
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
        caNeighbourhoodComboBox = new JComboBox<>(CaNeighbourhood.values());
        mcNeighbourhoodComboBox = new JComboBox<>(McNeighbourhood.values());
        energyComboBox = new JComboBox<>(new Object[] { "Homogeneous", "On grain boundaries" });
        nucleationPlacementComboBox = new JComboBox<>(new Object[] { "Anywhere", "On grain boundaries" });
        nucleationTypeComboBox = new JComboBox<>(NucleationType.values());
        boundaryComboBox = new JComboBox<>(new Object[] {
           "Non-periodic", "Periodic"
        });
    }

    private void resetGrainGrowth() {
        if(basicMethod != null) {
            if(basicMethod.equals(MethodType.CA)) {
                structure = CaFactory.getCa2dInstance(
                        boundaryComboBox.getSelectedIndex() == 1,
                        gridStatus.getStates(),
                        (CaNeighbourhood) caNeighbourhoodComboBox.getSelectedItem());
            } else if(basicMethod.equals(MethodType.MC)) {
                structure = McFactory.getMc2dInstance(
                        boundaryComboBox.getSelectedIndex() == 1,
                        gridStatus.getStates(),
                        (McNeighbourhood) mcNeighbourhoodComboBox.getSelectedItem());
            }
        }
        selectedGrainsPainted = false;
        setup = false;
        structure.reset();
        gridStatus.reset();
        grainList.clear();
        processPanel.repaint();
    }

    private void generateNewStructure(int boundaryVal, int type) {
        if(gridStatus.isProceeded()) {
            if(selectedGrainsPainted) {
                processButtonActionPerformed();
            } else {
                MethodType method = (MethodType) JOptionPane.showInputDialog(
                        this,
                        "Select grain growth method:",
                        "Method selection",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        MethodType.values(),
                        null);
                if(method.equals(MethodType.CA)) {
                    structure = CaFactory.getCa2dInstance(boundaryComboBox.getSelectedIndex() == 1,
                            gridStatus.getStates(),
                            (CaNeighbourhood) caNeighbourhoodComboBox.getSelectedItem());
                } else {
                    structure = McFactory.getMc2dInstance(boundaryComboBox.getSelectedIndex() == 1,
                            gridStatus.getStates(),
                            (McNeighbourhood) mcNeighbourhoodComboBox.getSelectedItem());
                    ((Mc2d) structure).setMaxIterations((int) maxIterSpinner.getValue());
                }

                if(type == 0) gridStatus.createSubstructure(grainList);
                else gridStatus.createDualPhase(grainList);

                structure.setBoundaryValue(boundaryVal);
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
                boundaryComboBox.getSelectedIndex() == 1,
                gridStatus.getStates(),
                (CaNeighbourhood) caNeighbourhoodComboBox.getSelectedItem());

        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "processCard");

        actionButtonPanel.setVisible(true);
        createButtonPanel.setVisible(false);
        basicMethod = MethodType.CA;
    }

    private void createMcButtonActionPerformed() {
        structure = McFactory.getMc2dInstance(boundaryComboBox.getSelectedIndex() == 1,
                gridStatus.getStates(),
                (McNeighbourhood) mcNeighbourhoodComboBox.getSelectedItem());
        ((Mc2d) structure).setMaxIterations((int) maxIterSpinner.getValue());
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "processCard");

        actionButtonPanel.setVisible(true);
        createButtonPanel.setVisible(false);
        basicMethod = MethodType.MC;
    }

    private void returnButtonActionPerformed() {
        resetGrainGrowth();

        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "optionCard");

        actionButtonPanel.setVisible(false);
        createButtonPanel.setVisible(true);
    }

    private void processButtonActionPerformed() {
        ProcessWorker worker = new ProcessWorker(GRAIN_GROWTH);
        worker.execute();
    }

    private void recrystallizationButtonActionPerformed() {
        ProcessWorker worker = new ProcessWorker(RECRYSTALLIZATION);
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
            gridStatus.generateRandomColors();
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
                structure.drawInclusionsAfter(type, radius);
            else
                structure.drawInclusionBefore(type, radius);
            processPanel.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input value is not a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEnergyButtonActionPerformed() {
        if(energyComboBox.getSelectedIndex() == 0) {
            structure.distributeHomogeneous();
        } else {
            structure.distributeOnGrainBoundaries();
        }
        EnergyVisualisation visualisation = new EnergyVisualisation(structure.getStoredEnergyH());
        visualisation.setVisible(true);
        visualisation.visualiseEnergy();
    }
}
