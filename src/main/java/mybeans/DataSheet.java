package mybeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataSheet implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Data> dataItems = new ArrayList<>();

    public DataSheet() {
    }

    public int size() {
        return dataItems.size();
    }

    public boolean isEmpty() {
        return dataItems.isEmpty();
    }

    public Data getDataItem(int index) {
        return dataItems.get(index);
    }

    public void addDataItem(Data data) {
        dataItems.add(data == null ? new Data() : data);
    }

    public void addDataItem(int index, Data data) {
        dataItems.add(index, data == null ? new Data() : data);
    }

    public Data removeDataItem(int index) {
        return dataItems.remove(index);
    }

    public void clear() {
        dataItems.clear();
    }

    public List<Data> getDataItems() {
        return Collections.unmodifiableList(dataItems);
    }

    public void setDataItems(List<Data> dataItems) {
        this.dataItems.clear();
        if (dataItems != null) {
            for (Data item : dataItems) {
                addDataItem(item);
            }
        }
    }
}
