package mybeans;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class DataSheetGraphBeanInfo extends SimpleBeanInfo {
    private final PropertyDescriptor[] propertyDescriptors;

    public DataSheetGraphBeanInfo() {
        propertyDescriptors = createPropertyDescriptors();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors.clone();
    }

    private static PropertyDescriptor[] createPropertyDescriptors() {
        try {
            return new PropertyDescriptor[] {
                    new PropertyDescriptor("color", DataSheetGraph.class),
                    new PropertyDescriptor("connected", DataSheetGraph.class),
                    new PropertyDescriptor("deltaX", DataSheetGraph.class),
                    new PropertyDescriptor("deltaY", DataSheetGraph.class),
                    new PropertyDescriptor("pointRadius", DataSheetGraph.class)
            };
        } catch (IntrospectionException ex) {
            return new PropertyDescriptor[0];
        }
    }
}
