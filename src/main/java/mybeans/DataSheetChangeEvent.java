package mybeans;

import java.util.EventObject;

public class DataSheetChangeEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    public DataSheetChangeEvent(Object source) {
        super(source);
    }
}
