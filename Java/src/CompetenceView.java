import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import models.Competence;
import models.Student;
import utilities.CompetenceUtilities;
import xaml.XamlGenerator;
import xml.XmlGenerator;

public class CompetenceView {
    private String file;

    private Element page;
    private Element grid;
    private Element canvas;

    private List<String> students;
    private Student student;

    private List<Competence> competences;

    public CompetenceView(String file) {
        this.file = file;

        // get students
        this.students = CompetenceUtilities.getStudents(this.file);
        String firstStudent = CompetenceUtilities.getFirstStudent(this.file);
        this.student = new Student(firstStudent);

        // get competences
        this.competences = CompetenceUtilities.getCompetences(this.file);

        this.init();
    }

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

        this.fillGraphCompetences();
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
        Element textblock = XamlGenerator.getTextBlock(this.student.getName(), "32", "Normal", "DemiBold", "Center", "Top");

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

        for (String student : this.students) {
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

    private void fillGraphCompetences() {
        double canvasHeight = Double.parseDouble(this.canvas.getAttributeValue("Height"));
        double canvasWidth = Double.parseDouble(this.canvas.getAttributeValue("Width"));

        double x;
        double y;

        double x1, x2, x3, x4;
        double y1, y2, y3, y4;

        Element textblock;

        for (Competence competence : this.competences) {
            textblock = XamlGenerator.getTextBlock(
                    competence.getName().equals("AnalyseOntwerp") ? "Analyse en Ontwerp" : competence.getName(), "40");

            // label positioning
            switch (competence.getName()) {
                case "Communiceren":
                    x = (canvasWidth / 2.0) - 90.0;y = canvasHeight - 35.0;
                    break;
                case "Management":
                    x = canvasWidth / 12.0;y = (canvasHeight / 2.0) - 60.0;
                    break;
                case "Implementatie":
                    x = (canvasWidth / 2.0) - 90.0;y = -10.0;
                    break;
                case "AnalyseOntwerp":
                    x = canvasWidth / 1.47;y = (canvasHeight / 2.0) - 60.0;
                    break;
                default:
                    x = 0;y=0;break;
            }
            textblock.setAttribute("Canvas.Bottom", Double.toString(y));
            textblock.setAttribute("Canvas.Left", Double.toString(x));

            this.canvas.addContent(textblock);
        }

        // competence mean lines
        //this.canvas.addContent(XamlGenerator.getLine(this.competences.get("Communiceren"), ));
    }

    public void write() {
        XmlGenerator.writeDocument(XmlGenerator.getDocument(this.page), "CompetenceView.xaml", "xaml");
    }

    public static void main(String[] args) {
        CompetenceView cv = new CompetenceView("competences.xml");
        cv.write();
    }
}
