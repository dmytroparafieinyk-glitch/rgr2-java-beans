package mybeans;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

public class DataSheetTable extends JPanel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final JTable table;
    private DataSheetTableModel tableModel;

    public DataSheetTable() {
        super(new BorderLayout(0, 8));

        tableModel = new DataSheetTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 4));
        JButton addButton = new JButton("Add");
        JButton delButton = new JButton("Delete");
        panelButtons.add(addButton);
        panelButtons.add(delButton);

        add(scrollPane, BorderLayout.CENTER);
        add(panelButtons, BorderLayout.SOUTH);

        addButton.addActionListener(event -> tableModel.addEmptyRow());
        delButton.addActionListener(event -> {
            int selectedRow = table.getSelectedRow();
            int modelRow = selectedRow >= 0 ? table.convertRowIndexToModel(selectedRow) : tableModel.getRowCount() - 1;
            tableModel.removeRow(modelRow);
        });
    }

    public JTable getTable() {
        return table;
    }

    public DataSheetTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DataSheetTableModel tableModel) {
        this.tableModel = tableModel == null ? new DataSheetTableModel() : tableModel;
        table.setModel(this.tableModel);
        revalidate();
        repaint();
    }

    public DataSheet getDataSheet() {
        return tableModel.getDataSheet();
    }

    public void setDataSheet(DataSheet dataSheet) {
        tableModel.setDataSheet(dataSheet);
    }
}
