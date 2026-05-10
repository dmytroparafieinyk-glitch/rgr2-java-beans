package mybeans;

import java.util.EventListener;

public interface DataSheetChangeListener extends EventListener {
    void dataChanged(DataSheetChangeEvent event);
}
