package GUI;

import EDA.EdgeBasedEDA;
import EDA.PositionBasedEDA_UMDA;
import LocalSearch.OrOpt;
import LocalSearch.TwoOpt;
import Main.Controller;
import Main.DefaultValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GUI {

    private static int rowsTextArea = 2;
    private static int columnsTextArea = 30;
    private static int bottomSpace = 5;

    private static JFrame frame;

    private static String filePrefix = "Selected File: ";
    private static String openFileName;
    private static File openFile;

    private static int headingsPlus = 2;

    private static NumberFormat integerFormatter = NumberFormat.getIntegerInstance();

    private static NumberFormat doubleFormatter = DecimalFormat.getInstance();


    private static JDialog runningDialog;

    //General
    private static JCheckBox storeCheckBox = new JCheckBox("Store computation");


    //MultiStart ILS Fields
    private static JTextArea msilsNumberLS = formatTextArea("The maximum number of invocations of a local search " +
            "algorithm: ");
    private static JFormattedTextField msilsNumberLSField = new JFormattedTextField(integerFormatter);
    private static JTextArea msilsNumberStuck = formatTextArea("The maximum number of times the algorithm can get " +
            "stuck " +
            "before moving to a new initial solution: ");
    private static JFormattedTextField msilsNumberStuckField = new JFormattedTextField(integerFormatter);


    //Local Search Fields
    private static String lsComboBoxListe[] = {TwoOpt.getName(), OrOpt.getName()};
    private static JComboBox lsComboBox = new JComboBox(lsComboBoxListe);
    private static JComboBox lsMethodComboBox = new JComboBox(TwoOpt.Method.values());


    //EDA Fields
    private static String aPrioriStringEdge = "The value, which edges already in a tour, should get. " +
            "This should be an integer: ";
    private static String getaPrioriStringPosition = "The probability an node in the given tour should get to appear " +
            "on the sae spot:";
    private static String edaComboBoxListe[] = {EdgeBasedEDA.getName(), PositionBasedEDA_UMDA.getName()};
    private static JComboBox edaComboBox = new JComboBox(edaComboBoxListe);
    private static JTextArea sampledPopulationSize = formatTextArea("Sampled Population size: ");
    private static JFormattedTextField sampledPopulationSizeField = new JFormattedTextField(integerFormatter);
    private static JTextArea selectedPopulationSize = formatTextArea("Selected Population size: ");
    private static JFormattedTextField selectedPopulationSizeField = new JFormattedTextField(integerFormatter);
    private static JTextArea edaMaxIterations = formatTextArea("Maximum number of Iterations in an EDA: ");
    private static JFormattedTextField edaMaxIterationsField = new JFormattedTextField(integerFormatter);
    private static JTextArea edaValueAPriori = formatTextArea(aPrioriStringEdge);
    private static JFormattedTextField edaValueAPrioriEdgesField = new JFormattedTextField(integerFormatter);
    private static JFormattedTextField edaValueAPrioriPositionField = new JFormattedTextField(doubleFormatter);
    private static JTextArea bRatio = formatTextArea("BRatio (If this value is high, the perturbation in one " +
            "iteration will " +
            "be high):");
    private static JFormattedTextField bRatioField = new JFormattedTextField(doubleFormatter);


    /**
     * This function creates the GUI.
     */
    public static void create() {
        frame = new JFrame("Multi-start ILS with EDA");

        JTabbedPane tabbedPane = new JTabbedPane
                (JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        frame.add(tabbedPane);

        JScrollPane runPanel = new JScrollPane(createRunPanel(),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab("Run", runPanel);

        JScrollPane drawPanel = new JScrollPane(createDrawPanel(),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab("Draw", drawPanel);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }


    /**
     * This function creates the elements that are needed for running a multi-start ILS from the UI.
     *
     * @return the created Panel
     */
    private static JPanel createRunPanel() {
        JPanel runPanel = new JPanel();

        GridBagLayout gridBagLayout = new GridBagLayout();
        runPanel.setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;

        setupGeneral(runPanel, gridBagConstraints, 0);

        setupMultiStartILS(runPanel, gridBagConstraints, 4);

        setupEDA(runPanel, gridBagConstraints, 7);

        setupLS(runPanel, gridBagConstraints, 14);

        setupButton(runPanel, gridBagConstraints, 17);

        return runPanel;
    }


    /**
     * This sets up the general part in a given panel. The messages need 3 lines.
     *
     * @param runPanel           the panel to which the general messages should be added
     * @param gridBagConstraints some Constraint object to format the fields
     * @param start              the line from which to start adding
     */
    private static void setupGeneral(JPanel runPanel, GridBagConstraints gridBagConstraints, int start) {
        JLabel generalLabel = new JLabel("General");
        generalLabel.setFont(new Font(generalLabel.getFont().getName(), Font.BOLD,
                generalLabel.getFont().getSize() + headingsPlus));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, 10, 0);
        runPanel.add(generalLabel, gridBagConstraints);

        JButton openFileButton = new JButton("Choose file");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 1;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(openFileButton, gridBagConstraints);

        JTextArea labelFileName = formatTextArea(filePrefix);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 10, bottomSpace, 0);
        runPanel.add(labelFileName, gridBagConstraints);

        openFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int openSuccessful = fc.showOpenDialog(frame);//This blocks the main window
                if (openSuccessful == JFileChooser.APPROVE_OPTION) {
                    openFile = fc.getSelectedFile();
                    openFileName = openFile.getAbsolutePath();
                    labelFileName.setText(filePrefix + openFileName);
                }
            }
        });

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 3;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        storeCheckBox.setSelected(true);
        runPanel.add(storeCheckBox, gridBagConstraints);
    }


    /**
     * This sets up the multi-start ILS part in a given panel. The messages need 3 lines.
     *
     * @param runPanel           the panel to which the multi-start ILS messages should be added
     * @param gridBagConstraints some Constraint object to format the fields
     * @param start              the line from which to start adding
     */
    private static void setupMultiStartILS(JPanel runPanel, GridBagConstraints gridBagConstraints, int start) {
        JLabel msilsLabel = new JLabel("Multi-Start Local Search");
        msilsLabel.setFont(new Font(msilsLabel.getFont().getName(), Font.BOLD,
                msilsLabel.getFont().getSize() + headingsPlus));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(20, 0, 10, 0);
        runPanel.add(msilsLabel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 1;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(msilsNumberLS, gridBagConstraints);

        msilsNumberLSField.setColumns(10);
        msilsNumberLSField.setToolTipText("Default is " + DefaultValues.maxTimesLS);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = start + 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(msilsNumberLSField, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 2;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(msilsNumberStuck, gridBagConstraints);

        msilsNumberStuckField.setColumns(10);
        msilsNumberStuckField.setToolTipText("Default is " + DefaultValues.maxTimesStuck);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = start + 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(msilsNumberStuckField, gridBagConstraints);

    }


    /**
     * This sets up the eda part in a given panel. The messages need 9 lines.
     *
     * @param runPanel           the panel to which the eda messages should be added
     * @param gridBagConstraints some Constraint object to format the fields
     * @param start              the line from which to start adding
     */
    private static void setupEDA(JPanel runPanel, GridBagConstraints gridBagConstraints, int start) {
        JLabel edaLabel = new JLabel("Configure EDA");
        edaLabel.setFont(new Font(edaLabel.getFont().getName(), Font.BOLD,
                edaLabel.getFont().getSize() + headingsPlus));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(20, 0, 10, 0);
        runPanel.add(edaLabel, gridBagConstraints);


        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 1;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(edaComboBox, gridBagConstraints);
        edaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (edaComboBox.getSelectedItem().equals(EdgeBasedEDA.getName())) {
                    edaValueAPriori.setText(aPrioriStringEdge);
                    edaValueAPrioriPositionField.setVisible(false);
                    edaValueAPrioriEdgesField.setVisible(true);
                    bRatio.setVisible(true);
                    bRatioField.setVisible(true);
                } else {
                    edaValueAPriori.setText(getaPrioriStringPosition);
                    edaValueAPrioriPositionField.setVisible(true);
                    edaValueAPrioriEdgesField.setVisible(false);
                    bRatio.setVisible(false);
                    bRatioField.setVisible(false);
                }
            }
        });

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 2;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(sampledPopulationSize, gridBagConstraints);

        sampledPopulationSizeField.setColumns(10);
        sampledPopulationSizeField.setToolTipText("Default is " + DefaultValues.sampledPopulationSize);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = start + 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(sampledPopulationSizeField, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 3;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(selectedPopulationSize, gridBagConstraints);

        selectedPopulationSizeField.setColumns(10);
        selectedPopulationSizeField.setToolTipText("Default is " + DefaultValues.selectedPopulationSize);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = start + 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(selectedPopulationSizeField, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 4;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(edaMaxIterations, gridBagConstraints);

        edaMaxIterationsField.setColumns(10);
        edaMaxIterationsField.setToolTipText("Default is " + DefaultValues.maxCounterOtIterations);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = start + 4;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(edaMaxIterationsField, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 5;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(edaValueAPriori, gridBagConstraints);

        edaValueAPrioriEdgesField.setColumns(10);
        edaValueAPrioriEdgesField.setToolTipText("Default is " + DefaultValues.valueForAPrioriEdges);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = start + 5;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(edaValueAPrioriEdgesField, gridBagConstraints);

        edaValueAPrioriPositionField.setColumns(10);
        edaValueAPrioriPositionField.setToolTipText("Default is " + DefaultValues.probForPriorTour);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = start + 5;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(edaValueAPrioriPositionField, gridBagConstraints);
        edaValueAPrioriPositionField.setVisible(false);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 6;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(bRatio, gridBagConstraints);

        bRatioField.setColumns(10);
        bRatioField.setToolTipText("Default is " + DefaultValues.bRatio);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = start + 6;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(bRatioField, gridBagConstraints);

    }


    /**
     * This sets up the local search part in a given panel. The messages need 3 lines.
     *
     * @param runPanel           the panel to which the local search messages should be added
     * @param gridBagConstraints some Constraint object to format the fields
     * @param start              the line from which to start adding
     */
    private static void setupLS(JPanel runPanel, GridBagConstraints gridBagConstraints, int start) {
        JLabel edaLabel = new JLabel("Configure Local Search");
        edaLabel.setFont(new Font(edaLabel.getFont().getName(), Font.BOLD,
                edaLabel.getFont().getSize() + headingsPlus));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(20, 0, 10, 0);
        runPanel.add(edaLabel, gridBagConstraints);


        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 1;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(lsComboBox, gridBagConstraints);
        lsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lsComboBox.getSelectedItem().equals(TwoOpt.getName())) {
                    lsMethodComboBox.setVisible(true);
                } else {
                    lsMethodComboBox.setVisible(false);
                }
            }
        });

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 2;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(lsMethodComboBox, gridBagConstraints);
    }


    /**
     * Sets up the run button.
     *
     * @param runPanel           the panel for which to add the button
     * @param gridBagConstraints the layout
     * @param start              the top left position of the button
     */
    private static void setupButton(JPanel runPanel, GridBagConstraints gridBagConstraints, int start) {
        JButton runButton = new JButton("Run");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(10, 0, bottomSpace, 0);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        runPanel.add(runButton, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.NONE;

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (openFileName != null) {
                    try {
                        int numberLS;
                        try {
                            numberLS = msilsNumberLSField.getText() != null ?
                                    Integer.parseInt(msilsNumberLSField.getText()) : DefaultValues.maxTimesLS;
                        } catch (Exception ex) {
                            numberLS = DefaultValues.maxTimesLS;
                        }


                        int numberStuck;
                        try {
                            numberStuck = msilsNumberStuckField.getText() != null ?
                                    Integer.parseInt(msilsNumberStuckField.getText()) : DefaultValues.maxTimesStuck;
                        } catch (Exception ex) {
                            numberStuck = DefaultValues.maxTimesStuck;
                        }
                        String eda = (String) edaComboBox.getSelectedItem();

                        int sampledPopulationSize;
                        try {
                            sampledPopulationSize = sampledPopulationSizeField.getText() != null ?
                                    Integer.parseInt(sampledPopulationSizeField.getText()) :
                                    DefaultValues.sampledPopulationSize;
                        } catch (Exception ex) {
                            sampledPopulationSize = DefaultValues.sampledPopulationSize;
                        }
                        int selectedPopulationSize;
                        try {
                            selectedPopulationSize = selectedPopulationSizeField.getText() != null ?
                                    Integer.parseInt(selectedPopulationSizeField.getText()) :
                                    DefaultValues.selectedPopulationSize;
                        } catch (Exception ex) {
                            selectedPopulationSize = DefaultValues.selectedPopulationSize;
                        }
                        int maxIterationsEDA;
                        try {
                            maxIterationsEDA = edaMaxIterationsField.getText() != null ?
                                    Integer.parseInt(edaMaxIterationsField.getText()) :
                                    DefaultValues.maxCounterOtIterations;
                        } catch (Exception ex) {
                            maxIterationsEDA = DefaultValues.maxCounterOtIterations;
                        }
                        int aPrioriEdges;
                        try {
                            aPrioriEdges = edaValueAPrioriEdgesField.getText() != null ?
                                    Integer.parseInt(edaValueAPrioriEdgesField.getText()) :
                                    DefaultValues.valueForAPrioriEdges;
                        } catch (Exception ex) {
                            aPrioriEdges = DefaultValues.valueForAPrioriEdges;
                        }
                        double bRatio;
                        try {
                            bRatio = bRatioField.getText() != null ? Integer.parseInt(bRatioField.getText()) :
                                    DefaultValues.bRatio;
                        } catch (Exception ex) {
                            bRatio = DefaultValues.bRatio;
                        }
                        double probPriorTour;
                        try {
                            probPriorTour = edaValueAPrioriPositionField.getText() != null ?
                                    Integer.parseInt(edaValueAPrioriPositionField.getText()) :
                                    DefaultValues.probForPriorTour;
                        } catch (Exception ex) {
                            probPriorTour = DefaultValues.probForPriorTour;
                        }
                        String ls = (String) lsComboBox.getSelectedItem();
                        TwoOpt.Method method = (TwoOpt.Method) lsMethodComboBox.getSelectedItem();

                        Controller controller = new Controller();


                        controller.run(openFileName, numberLS, numberStuck, eda, sampledPopulationSize,
                                selectedPopulationSize, maxIterationsEDA, aPrioriEdges, bRatio, probPriorTour, ls,
                                method, storeCheckBox.isSelected());

                        createRunningDialog(controller);


                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "There was an exception:\n" + ex.toString(), "Exception"
                                , JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "No file specified");
                }
            }
        });
    }


    /**
     * This method returns a JTextArea that looks like a JLabel
     *
     * @param text the text on the area
     * @return the JTextArea
     */
    private static JTextArea formatTextArea(String text) {
        JTextArea textArea = new JTextArea(rowsTextArea, columnsTextArea);
        textArea.setText(text);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBackground(UIManager.getColor("Label.background"));
        textArea.setFont(UIManager.getFont("Label.font"));
        textArea.setBorder(UIManager.getBorder("Label.border"));
        return textArea;
    }


    /**
     * This is the dialog, that is shown while the program is running
     *
     * @param controller the connection to the executiong unit
     */
    private static void createRunningDialog(Controller controller) {
        runningDialog = new JDialog(frame);
        runningDialog.setTitle("Running...");
        runningDialog.setSize(200, 200);
        runningDialog.setModal(true);

        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        runningDialog.add(scrollPane);

        GridBagLayout gridBagLayout = new GridBagLayout();
        panel.setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;

        JTextArea text = formatTextArea("The computation is running. If you click Stop, the thread will be " +
                "stopped at the " +
                "next possible moment. If you click stop immediately, the thread will be " +
                "killed and might not exit as it should. If you want to let it run, just do " +
                "nothing. This message will disappear when the thread is finished.\n" +
                "Please notice, that Stop may take some time.");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(text, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.NONE;

        JButton stop = new JButton("Stop");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.stopRunning();
            }
        });
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        panel.add(stop, gridBagConstraints);

        JButton stopImmediately = new JButton("Stop immediately");
        stopImmediately.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.kill();
            }
        });
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 100, bottomSpace, 0);
        panel.add(stopImmediately, gridBagConstraints);
        runningDialog.setDefaultCloseOperation(
                JDialog.DO_NOTHING_ON_CLOSE);
        runningDialog.pack();
        runningDialog.setVisible(true);

    }


    /**
     * This function creates the elements that are needed for drawing a problem instance.
     *
     * @return the created Panel
     */
    private static JPanel createDrawPanel() {
        JPanel drawPanel = new JPanel();
        drawPanel.add(new JLabel("This is the draw panel"));

        JButton openFileButton = new JButton("Choose file");


        return drawPanel;
        //TODO: add more stuff
    }


    public static void programFinished() {
        if (runningDialog != null) {
            runningDialog.dispose();
        }
    }

}
