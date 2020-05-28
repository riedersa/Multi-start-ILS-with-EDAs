package GUI;

import EDA.EdgeBasedEDA;
import EDA.PositionBasedEDA_UMDA;
import LocalSearch.OrOpt;
import LocalSearch.TwoOpt;

import javax.swing.*;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GUI {

    private static int rowsTextArea = 2;
    private static int columnsTextArea = 30;

    private static JFrame frame;


    /**
     * This function creates the GUI.
     */
    public static void create() {
        frame = new JFrame("Multi-start ILS with EDA");

        JTabbedPane tabbedPane = new JTabbedPane
                (JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        frame.add(tabbedPane);

        JScrollPane runPanel = new JScrollPane(RunPanel.createRunPanel(frame),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab("Run", runPanel);

        JScrollPane drawPanel = new JScrollPane(DrawPanel.createDrawPanel(frame),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab("Draw", drawPanel);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    /**
     * This method returns a JTextArea that looks like a JLabel
     *
     * @param text the text on the area
     * @return the JTextArea
     */
    public static JTextArea formatTextArea(String text) {
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

}
