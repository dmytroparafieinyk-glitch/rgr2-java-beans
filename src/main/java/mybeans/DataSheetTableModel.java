package mybeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class DataSheetTableModel extends AbstractTableModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int COLUMN_COUNT = 3;
    private static final String[] COLUMN_NAMES = {"Date", "X Value", "Y Value"};

    private DataSheet dataSheet = createDefaultDataSheet();
    private final List<DataSheetChangeListener> listenerList = new ArrayList<>();

    public DataSheetTableModel() {
    }

    public DataSheet getDataSheet() {
        return dataSheet;
    }

    public void setDataSheet(DataSheet dataSheet) {
        this.dataSheet = normalizeDataSheet(dataSheet);
        fireTableDataChanged();
        fireDataSheetChange();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public int getRowCount() {
        return dataSheet.size();
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? String.class : Double.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 0 && columnIndex < COLUMN_COUNT;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Data item = dataSheet.getDataItem(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getDate();
            case 1:
                return item.getX();
            case 2:
                return item.getY();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= dataSheet.size()) {
            return;
        }

        Data item = dataSheet.getDataItem(rowIndex);
        try {
            switch (columnIndex) {
                case 0:
                    item.setDate(value == null ? "" : value.toString());
                    break;
                case 1:
                    item.setX(toDouble(value));
                    break;
                case 2:
                    item.setY(toDouble(value));
                    break;
                default:
                    return;
            }
            fireTableCellUpdated(rowIndex, columnIndex);
            fireDataSheetChange();
        } catch (NumberFormatException ex) {
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    public void addEmptyRow() {
        int row = dataSheet.size();
        dataSheet.addDataItem(new Data());
        fireTableRowsInserted(row, row);
        fireDataSheetChange();
    }

    public void removeRow(int rowIndex) {
        if (dataSheet.size() <= 1) {
            Data item = dataSheet.getDataItem(0);
            item.setDate("");
            item.setX(0.0);
            item.setY(0.0);
            fireTableRowsUpdated(0, 0);
        } else if (rowIndex >= 0 && rowIndex < dataSheet.size()) {
            dataSheet.removeDataItem(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
        fireDataSheetChange();
    }

    public void addDataSheetChangeListener(DataSheetChangeListener listener) {
        if (listener != null) {
            listenerList.add(listener);
        }
    }

    public void removeDataSheetChangeListener(DataSheetChangeListener listener) {
        listenerList.remove(listener);
    }

    protected void fireDataSheetChange() {
        DataSheetChangeEvent event = new DataSheetChangeEvent(this);
        List<DataSheetChangeListener> listeners = new ArrayList<>(listenerList);
        for (DataSheetChangeListener listener : listeners) {
            listener.dataChanged(event);
        }
    }

    private static double toDouble(Object value) {
        if (value instanceof Number) {
            Number number = (Number) value;
            return number.doubleValue();
        }
        return Double.parseDouble(value == null ? "0" : value.toString().trim().replace(',', '.'));
    }

    private static DataSheet normalizeDataSheet(DataSheet dataSheet) {
        if (dataSheet == null || dataSheet.isEmpty()) {
            return createDefaultDataSheet();
        }
        return dataSheet;
    }

    private static DataSheet createDefaultDataSheet() {
        DataSheet sheet = new DataSheet();
        sheet.addDataItem(new Data());
        return sheet;
    }
}
