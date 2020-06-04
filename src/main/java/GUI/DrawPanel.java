package GUI;

import DataStructures.DrawingInstanceFromFile;
import Main.ControllerDrawing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class DrawPanel {

    private static int bottomSpace = 5;
    private static int headingsPlus = 2;

    private static JFrame parentFrame;

    private static String openFileName;
    private static File openFile;
    private static String filePrefix = "Selected File: ";
    private static String printablePrefix = "Printable: ";
    private static String numberNodesPrefix = "Number of Nodes: ";
    private static String namePrefix = "Name: ";
    private static String lengthPrefix = "Length: ";

    private static ControllerDrawing controller = new ControllerDrawing();
    private static DrawingInstanceFromFile drawingInstanceFromFile;

    //Information for Instance
    private static JTextArea labelFileName = GUI.formatTextArea(filePrefix);
    private static JTextArea labelName = GUI.formatTextArea(namePrefix);
    private static JTextArea labelNumberNodes = GUI.formatTextArea(numberNodesPrefix);
    private static JTextArea labelPrintable = GUI.formatTextArea(printablePrefix);
    private static JTextArea labelLength = GUI.formatTextArea(lengthPrefix);


    /**
     * This function creates the elements that are needed for drawing a problem instance.
     *
     * @param frame the parent frame
     * @return the created Panel
     */
    public static JPanel createDrawPanel(JFrame frame) {
        parentFrame = frame;
        JPanel drawPanel = new JPanel();

        GridBagLayout gridBagLayout = new GridBagLayout();
        drawPanel.setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;

        setupGeneral(drawPanel, gridBagConstraints, 0);


        return drawPanel;
    }


    /**
     * This sets up the general part in a given panel. The messages need 3 lines.
     *
     * @param runPanel           the panel to which the general messages should be added
     * @param gridBagConstraints some Constraint object to format the fields
     * @param start              the line from which to start adding
     */
    private static void setupGeneral(JPanel runPanel, GridBagConstraints gridBagConstraints, int start) {

        JButton openFileButton = new JButton("Choose file");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, bottomSpace, 0);
        runPanel.add(openFileButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new Insets(0, 10, bottomSpace, 0);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        runPanel.add(labelFileName, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.NONE;

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.insets = new Insets(0, 10, bottomSpace, 0);
        runPanel.add(labelName, gridBagConstraints);
        labelName.setVisible(false);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = start + 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 10, bottomSpace, 0);
        runPanel.add(labelNumberNodes, gridBagConstraints);
        labelNumberNodes.setVisible(false);

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = start + 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 10, bottomSpace, 0);
        runPanel.add(labelPrintable, gridBagConstraints);
        labelPrintable.setVisible(false);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = start + 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 10, bottomSpace, 0);
        runPanel.add(labelLength, gridBagConstraints);
        labelLength.setVisible(false);

        openFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int openSuccessful = fc.showOpenDialog(parentFrame);//This blocks the main window
                if (openSuccessful == JFileChooser.APPROVE_OPTION) {
                    openFile = fc.getSelectedFile();
                    openFileName = openFile.getAbsolutePath();
                    labelFileName.setText(filePrefix + openFileName);
                    try {
                        drawingInstanceFromFile = controller.importFile(openFile);
                        fillInformation(drawingInstanceFromFile);
                        if (drawingInstanceFromFile.isPrintable()) {
                            plotGraph(drawingInstanceFromFile.getName(),
                                    drawingInstanceFromFile.getTspTour().getLength()
                                    , drawingInstanceFromFile.getNodeCoordinates(),
                                    drawingInstanceFromFile.getTspTour().getTour());
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(parentFrame, "There was an exception:\n" + ex.toString(),
                                "Exception"
                                , JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }


    /**
     * This method gills out general information on the given instance.
     *
     * @param drawingInstanceFromFile the given isntance
     */
    private static void fillInformation(DrawingInstanceFromFile drawingInstanceFromFile) {
        if (drawingInstanceFromFile != null) {
            labelName.setText(namePrefix + drawingInstanceFromFile.getName());
            labelName.setVisible(true);

            labelNumberNodes.setText(numberNodesPrefix + drawingInstanceFromFile.getNumberNodes());
            labelNumberNodes.setVisible(true);

            labelPrintable.setText(printablePrefix + drawingInstanceFromFile.isPrintable());
            labelPrintable.setVisible(true);

            labelLength.setText(lengthPrefix + drawingInstanceFromFile.getTspTour().getLength());
            labelLength.setVisible(true);
        }
    }


    /**
     * This method opens a new window containing the plot.
     *
     * @param name        the name of the instance
     * @param length      the length of the instance
     * @param coordinates the node coordinates
     * @param tour        the tour
     */
    private static void plotGraph(String name, long length,
                                  double[][] coordinates, int[] tour) {
        JFrame frame = new JFrame();
        JPanel graph = new JPanel() {
            int maxX = 0;
            int maxY = 0;
            double scale = 1;


            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                findMax();
                computeScale();
                for (double[] nodeCoordinate : coordinates) {
                    g2.fillOval((int) (nodeCoordinate[1] * scale), (int) (nodeCoordinate[2] * scale), 10, 10);
                }
                for (int i = 0; i < tour.length - 1; i++) {
                    g2.drawLine((int) (coordinates[tour[i]][1] * scale), (int) (coordinates[tour[i]][2] * scale),
                            (int) (coordinates[tour[i + 1]][1] * scale), (int) (coordinates[tour[i + 1]][2] * scale));
                }
                g2.drawLine((int) (coordinates[tour[tour.length - 1]][1] * scale),
                        (int) (coordinates[tour[tour.length - 1]][2] * scale),
                        (int) (coordinates[tour[0]][1] * scale), (int) (coordinates[tour[0]][2] * scale));
            }


            private void findMax() {
                for (double[] nodeCoordinate : coordinates) {
                    if (nodeCoordinate[1] > maxX) {
                        maxX = (int) nodeCoordinate[1];
                    }
                    if (nodeCoordinate[2] > maxY) {
                        maxY = (int) nodeCoordinate[2];
                    }
                }
            }


            private void computeScale() {
                int width = frame.getWidth();
                int height = frame.getHeight() - 100;
                scale = Math.min((width * 1.0) / maxX, (height * 1.0) / maxY);
            }


            @Override
            public Dimension getPreferredSize() {
                findMax();
                computeScale();
                return new Dimension((int) (maxX * scale), (int) (maxY * scale));
            }
        };
        frame.setTitle(name + ", " + length);
        frame.setSize(1000, 1000);
        frame.add(new JScrollPane(graph, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        frame.repaint();
        frame.setVisible(true);
    }

}
