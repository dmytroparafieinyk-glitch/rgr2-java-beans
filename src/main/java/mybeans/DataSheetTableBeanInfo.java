package mybeans;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class DataSheetTableBeanInfo extends SimpleBeanInfo {
    private final PropertyDescriptor[] propertyDescriptors;

    public DataSheetTableBeanInfo() {
        propertyDescriptors = createPropertyDescriptors();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors.clone();
    }

    private static PropertyDescriptor[] createPropertyDescriptors() {
        try {
            return new PropertyDescriptor[] {
                    new PropertyDescriptor("dataSheet", DataSheetTable.class),
                    new PropertyDescriptor("tableModel", DataSheetTable.class)
            };
        } catch (IntrospectionException ex) {
            return new PropertyDescriptor[0];
        }
    }
}
