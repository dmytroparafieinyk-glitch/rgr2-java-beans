package xml;

import mybeans.Data;
import mybeans.DataSheet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DataHandler extends DefaultHandler {
    private final DataSheet dataSheet = new DataSheet();
    private Data currentData;
    private StringBuilder text;

    public DataSheet getDataSheet() {
        if (dataSheet.isEmpty()) {
            dataSheet.addDataItem(new Data());
        }
        return dataSheet;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        String element = qName == null || qName.isEmpty() ? localName : qName;
        text = new StringBuilder();

        if ("data".equals(element) || "item".equals(element)) {
            currentData = new Data();
            currentData.setDate(attributes.getValue("date"));
            currentData.setX(parseDouble(attributes.getValue("x")));
            currentData.setY(parseDouble(attributes.getValue("y")));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (text != null) {
            text.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String element = qName == null || qName.isEmpty() ? localName : qName;

        if (currentData != null) {
            String value = text == null ? "" : text.toString().trim();
            switch (element) {
                case "date":
                    currentData.setDate(value);
                    break;
                case "x":
                    currentData.setX(parseDouble(value));
                    break;
                case "y":
                    currentData.setY(parseDouble(value));
                    break;
                case "data":
                case "item":
                    dataSheet.addDataItem(currentData);
                    currentData = null;
                    break;
                default:
                    break;
                }
        }
        text = null;
    }

    private static double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value.trim().replace(',', '.'));
    }
}
