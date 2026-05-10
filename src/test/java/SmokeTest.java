import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import mybeans.Data;
import mybeans.DataSheet;
import mybeans.DataSheetGraph;
import xml.DataSheetToXML;
import xml.SAXRead;

public final class SmokeTest {
    private SmokeTest() {
    }

    public static void main(String[] args) {
        DataSheet sheet = SAXRead.XMLReadData("data/sample.xml");
        if (sheet.size() != 5) {
            throw new IllegalStateException("Expected 5 rows, got " + sheet.size());
        }

        sheet.addDataItem(new Data("2026-05-06", 6.0, 12.0));
        DataSheetToXML.saveXMLDoc(DataSheetToXML.createDataSheetDOM(sheet), "build/test-output.xml");

        DataSheet reread = SAXRead.XMLReadData("build/test-output.xml");
        if (reread.size() != 6) {
            throw new IllegalStateException("Expected 6 rows, got " + reread.size());
        }

        DataSheetGraph graph = new DataSheetGraph();
        graph.setSize(320, 240);
        graph.setDataSheet(reread);
        BufferedImage image = new BufferedImage(320, 240, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graph.showGraph(graphics);
        } finally {
            graphics.dispose();
        }

        System.out.println("Smoke test OK: " + reread.size() + " rows");
    }
}
