package xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import mybeans.Data;
import mybeans.DataSheet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class DataSheetToXML {
    private DataSheetToXML() {
    }

    public static Document createDataSheetDOM(DataSheet dataSheet) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("dataSheet");
            document.appendChild(root);

            if (dataSheet != null) {
                for (Data item : dataSheet.getDataItems()) {
                    Element dataElement = document.createElement("data");
                    dataElement.setAttribute("date", item.getDate());
                    dataElement.setAttribute("x", Double.toString(item.getX()));
                    dataElement.setAttribute("y", Double.toString(item.getY()));
                    root.appendChild(dataElement);
                }
            }

            return document;
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot create XML document", ex);
        }
    }

    public static void saveXMLDoc(Document document, String fileName) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot save XML data to " + fileName, ex);
        }
    }
}
