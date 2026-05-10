package xml;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import mybeans.DataSheet;

public final class SAXRead {
    private SAXRead() {
    }

    public static DataSheet XMLReadData(String fileName) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            DataHandler handler = new DataHandler();
            parser.parse(new File(fileName), handler);
            return handler.getDataSheet();
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot read XML data from " + fileName, ex);
        }
    }
}
