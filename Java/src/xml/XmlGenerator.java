package xml;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class XmlGenerator {
    private XmlGenerator() { }

    public static Document getDocument() {
        Document doc = new Document();
        return doc;
    }
    public static Document getDocument(Element root) {
        Document doc = new Document();
        doc.addContent(root);
        return doc;
    }

    public static void writeDocument(Document doc, String name, String res) {
        try {
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());

            File f = new File("res/" + res + "/" + name);

            xmlOutput.output(doc, new FileWriter(f.getAbsoluteFile()));
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    public static Document readDocument(String name, String res) {
        SAXBuilder builder =  new SAXBuilder();
        Document doc = new Document();

        File f = new File("res/" + res + "/" + name);

        try {
            doc = builder.build(f.getAbsoluteFile());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return doc;
    }

    public static String searchContent(String xpathString, String doc) {
        XPathExpression<Element> xpath = XPathFactory.instance().compile(xpathString, Filters.element());
        Element e = xpath.evaluateFirst(readDocument(doc, "xml"));
        return e.getValue();
    }
    public static List<String> searchContents(String xpathString, String doc) {
        XPathExpression<Element> xpath = XPathFactory.instance().compile(xpathString, Filters.element());
        List<Element> es = xpath.evaluate(readDocument(doc, "xml"));

        List<String> values = new ArrayList<>();
        for (Element e : es) { values.add(e.getValue()); }

        return values;
    }

    public static Element searchElement(String xpathString, String doc) {
        XPathExpression<Element> xpath = XPathFactory.instance().compile(xpathString, Filters.element());
        Element element = xpath.evaluateFirst(readDocument(doc, "xml"));
        return element;
    }
    public static List<Element> searchElements(String xpathString, String doc) {
        XPathExpression<Element> xpath = XPathFactory.instance().compile(xpathString, Filters.element());
        List<Element> elements = xpath.evaluate(readDocument(doc, "xml"));
        return elements;
    }

    public static String searchAttribute(String xpathString, String doc) {
        XPathExpression<Attribute> xpath = XPathFactory.instance().compile(xpathString, Filters.attribute());
        Attribute attribute = xpath.evaluateFirst(readDocument(doc, "xml"));
        return attribute.getValue();
    }
    public static List<String> searchAttributes(String xpathString, String doc) {
        XPathExpression<Attribute> xpath = XPathFactory.instance().compile(xpathString, Filters.attribute());
        List<Attribute> attributes = xpath.evaluate(readDocument(doc, "xml"));

        List<String> values = new ArrayList<>();
        for (Attribute a : attributes) { values.add(a.getValue()); }

        return values;
    }
}
