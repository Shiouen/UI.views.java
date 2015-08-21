import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import models.Student;
import utilities.ListUtilities;
import utilities.MathUtilities;
import xaml.XamlGenerator;
import xml.XmlGenerator;

public class CompetenceView {
    private Document competences;
    private String file;

    private Element page;
    private Element grid;
    private Element canvas;

    private Student student;

    public CompetenceView(String file) {
        this.file = file;
        this.competences = XmlGenerator.readDocument(this.file, "xml");

        this.init();
    }


/*    private String getFirstStudent() {
        String student = XmlGenerator.searchAttribute("//Student/@FullName", this.file);
        return student;
    }
    private List<String> getStudents() {
        List<String> students = XmlGenerator.searchAttributes("//Students/Student/@FullName", this.file);
        return students;
    }*/

    private void init() {
        // page
        this.page = XamlGenerator.getPage();
        XamlGenerator.setXNamespace(this.page);

        // grid
        String[] rowSizes = {"50", "1*", "40"};
        String[] colSizes = {"200", "80*", "10*"};
        this.grid = XamlGenerator.getGrid(rowSizes, colSizes, true);

        // viewbox
        Element viewbox = XamlGenerator.getViewBox();
        viewbox.setAttribute("Grid.Column", "1");
        viewbox.setAttribute("Grid.Row", "1");

        // canvas
        this.canvas = XamlGenerator.getCanvas("700", "700");
        this.canvas.setAttribute("Margin", "40");

        viewbox.addContent(this.canvas);
        XamlGenerator.setXName(viewbox, "CompetenceGraph");
        this.grid.addContent(viewbox);

        this.page.addContent(this.grid);

        this.generateGraphStructure();
        this.generateStudentBlock();
        this.generateStudentScrollViewer();
    }

    private void generateGraphStructure() {
        double canvasHeight = Double.parseDouble(this.canvas.getAttributeValue("Height"));
        double canvasWidth = Double.parseDouble(this.canvas.getAttributeValue("Width"));

        double x1, x2;
        double y1, y2;

        List<Element> graphLines = new ArrayList<>();

        // horizontal line
        // 95% of total
        x2 = 95.0 / 100.0 * canvasWidth;
        // 95% - 90% of total
        x1 = x2 - (90.0 / 100.0 * canvasWidth);
        y1 = y2 = canvasHeight / 2.0;

        Element horizontalLine = XamlGenerator.getLine(Double.toString(x1), Double.toString(y1),
                Double.toString(x2), Double.toString(y2), "Black", "1.5");

        // horizontal line - graph lines
        double incr = (x2 - x1) / 20.0;
        double y = canvasHeight / 2.0;
        for (double i = x1; i <= x2; i += incr) {
            if (i == x1 || i == canvasWidth / 2 || i == x2) {
                y1 = y + 10.0;
                y2 = y - 10.0;
            } else {
                y1 = y + 6.0;
                y2 = y - 6.0;
            }

            Element graphLine = XamlGenerator.getLine(Double.toString(i), Double.toString(y1), Double.toString(i),
                    Double.toString(y2), "Black", "1.5");
            graphLines.add(graphLine);
        }

        // vertical line
        // 95% of total
        y2 = 95.0 / 100.0 * canvasHeight;
        // 95% - 90% of total
        y1 = y2 - (90.0 / 100.0 * canvasHeight);
        x1 = x2 = canvasWidth / 2;

        Element verticalLine = XamlGenerator.getLine(Double.toString(x1), Double.toString(y1),
                Double.toString(x2), Double.toString(y2), "Black", "1.5");

        // horizontal line - graph lines
        incr = (y2 - y1) / 20.0;
        double x = canvasWidth / 2.0;
        for (double i = y1; i <= y2; i += incr) {
            System.out.println(i);

            if (i == y1 || i == canvasHeight / 2 || i == y2) {
                x1 = x + 10.0;
                x2 = x - 10.0;
            } else {
                x1 = x + 6.0;
                x2 = x - 6.0;
            }

            Element graphLine = XamlGenerator.getLine(Double.toString(x1), Double.toString(i), Double.toString(x2),
                    Double.toString(i), "Black", "1.5");
            graphLines.add(graphLine);
        }

        this.canvas.addContent(horizontalLine);
        this.canvas.addContent(verticalLine);
        this.canvas.addContent(graphLines);
    }
    private void generateStudentBlock() {
        // student name
        Element textblock = XamlGenerator.getTextBlock("hihihi", "32", "Normal", "DemiBold", "Center", "Top");

        textblock.setAttribute("Grid.Column", "1");
        textblock.setAttribute("Grid.Row", "0");

        XamlGenerator.setXName(textblock, "StudentNameBlock");
        this.grid.addContent(textblock);
    }
    private void generateStudentScrollViewer() {
        Element scrollViewer = XamlGenerator.getScrollViewer();

        Element stack = XamlGenerator.getStackPanel("5", "Vertical");
        Element border;
        Element textblock;

        for (String student : Arrays.asList("Student1", "Student2")) {
            border = XamlGenerator.getBorder("0.5", "White", "Hand");
            textblock = XamlGenerator.getTextBlock(student, "12", "Normal", "Normal", "Center", "Center");

            textblock.setAttribute("Margin", "0,1.5,0,1");
            border.addContent(textblock);

            border.setAttribute("Margin", "0,0,0,8.5");

            Element effect = XamlGenerator.getEffect("Border");
            effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "0"));
            border.addContent(effect);

            stack.addContent(border);
        }

        scrollViewer.addContent(stack);
        scrollViewer.setAttribute("Grid.Column", "0");
        scrollViewer.setAttribute("Grid.Row", "1");
        XamlGenerator.setXName(scrollViewer, "StudentScroller");
        this.grid.addContent(scrollViewer);
    }

    public void write() {
        XmlGenerator.writeDocument(XmlGenerator.getDocument(this.page), "CompetenceView.xaml", "xaml");
    }

    public static void main(String[] args) {
        CompetenceView cv = new CompetenceView("competences.xml");
        cv.write();
    }
}
