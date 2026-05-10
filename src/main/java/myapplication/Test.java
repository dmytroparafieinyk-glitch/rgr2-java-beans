package myapplication;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import mybeans.Data;
import mybeans.DataSheet;
import mybeans.DataSheetGraph;
import mybeans.DataSheetTable;
import xml.DataSheetToXML;
import xml.SAXRead;

public class Test extends JFrame {
    private static final long serialVersionUID = 1L;

    private final JFileChooser fileChooser = new JFileChooser();
    private final DataSheetTable dataSheetTable = new DataSheetTable();
    private final DataSheetGraph dataSheetGraph = new DataSheetGraph();
    private DataSheet dataSheet;

    public Test() {
        super("RGR2 JavaBeans Data Viewer");
        initializeData();
        initializeFrame();
        initializeComponents();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new Test().setVisible(true);
        });
    }

    private void initializeData() {
        dataSheet = new DataSheet();
        dataSheet.addDataItem(new Data());
    }

    private void initializeFrame() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));
    }

    private void initializeComponents() {
        dataSheetTable.setPreferredSize(new Dimension(300, 360));
        dataSheetGraph.setPreferredSize(new Dimension(460, 360));
        dataSheetGraph.setDataSheet(dataSheet);
        dataSheetTable.setDataSheet(dataSheet);
        dataSheetTable.getTableModel().addDataSheetChangeListener(event -> dataSheetGraph.repaint());

        add(dataSheetTable, BorderLayout.WEST);
        add(dataSheetGraph, BorderLayout.EAST);
        add(createButtonPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));

        JButton readButton = new JButton("Open XML");
        JButton saveButton = new JButton("Save XML");
        JButton clearButton = new JButton("Clear");
        JButton exitButton = new JButton("Exit");
        JCheckBox connectedBox = new JCheckBox("Connect points");

        readButton.addActionListener(event -> openXml());
        saveButton.addActionListener(event -> saveXml());
        clearButton.addActionListener(event -> clearData());
        exitButton.addActionListener(event -> dispose());
        connectedBox.addActionListener(event -> dataSheetGraph.setConnected(connectedBox.isSelected()));

        panel.add(readButton);
        panel.add(saveButton);
        panel.add(clearButton);
        panel.add(connectedBox);
        panel.add(exitButton);

        return panel;
    }

    private void openXml() {
        if (JFileChooser.APPROVE_OPTION != fileChooser.showOpenDialog(this)) {
            return;
        }

        try {
            dataSheet = SAXRead.XMLReadData(fileChooser.getSelectedFile().getPath());
            dataSheetTable.setDataSheet(dataSheet);
            dataSheetGraph.setDataSheet(dataSheet);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "XML read error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveXml() {
        if (JFileChooser.APPROVE_OPTION != fileChooser.showSaveDialog(this)) {
            return;
        }

        File selected = fileChooser.getSelectedFile();
        if (!selected.getName().toLowerCase().endsWith(".xml")) {
            selected = new File(selected.getParentFile(), selected.getName() + ".xml");
        }

        try {
            DataSheetToXML.saveXMLDoc(DataSheetToXML.createDataSheetDOM(dataSheet), selected.getPath());
            JOptionPane.showMessageDialog(this,
                    "File " + selected.getPath().trim() + " saved!",
                    "Results saved",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "XML save error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearData() {
        dataSheet = new DataSheet();
        dataSheet.addDataItem(new Data());
        dataSheetTable.setDataSheet(dataSheet);
        dataSheetGraph.setDataSheet(dataSheet);
    }
}
