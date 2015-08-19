import models.Student;
import org.jdom2.Document;
import org.jdom2.Element;
import xaml.XamlGenerator;
import xml.XmlGenerator;

import java.util.Arrays;
import java.util.List;

public class StudentView {
    private Document results;
    private String file;

    private Element grid;
    private Element canvas;

    private Student student;

    public StudentView(String name) {
        this.file = name;
        this.results = XmlGenerator.readDocument(name, "xml");

        this.init();

        String firstStudent = this.getFirstStudent();
        this.student = new Student(firstStudent, this.getStudentResults(firstStudent));
        this.fillStudentView();
        this.fillGraph();
    }

    private String getFirstStudent() {
        String student = XmlGenerator.searchAttribute("//Student/@FullName", this.file);
        return student;
    }
    private List<String> getAllStudents() {
        List<String> students = XmlGenerator.searchAttributes("//Students/Student/@FullName", this.file);
        return students;
    }
    private List<Element> getStudentResults(String name) {
        String xpath = "//Student[@FullName = \"" + name + "\"]//Result";
        List<Element> results = XmlGenerator.searchElements(xpath, this.file);
        return results;
    }

    private void init() {
        // grid
        String[] rowSizes = { "50", "1*", "40" };
        String[] colSizes = { "110", "8*", "1*" };
        this.grid = XamlGenerator.getGrid(rowSizes, colSizes, true);

        // viewbox
        Element viewbox = XamlGenerator.getViewBox();
        viewbox.setAttribute("Grid.Column", "1");
        viewbox.setAttribute("Grid.Row", "1");

        // canvas
        this.canvas = XamlGenerator.getCanvas("800", "400");
        this.canvas.setAttribute("Margin", "40");

        viewbox.addContent(this.canvas);
        this.grid.addContent(viewbox);

        this.fillStudentOverview();
        this.fillFunctions();
    }

    private void fillStudentOverview() {
        // student names overview
        Element stack = XamlGenerator.getStackPanel("5", "Vertical");
        Element border;
        Element textblock;

        for (String student : this.getAllStudents()) {
            border = XamlGenerator.getBorder("0.5", "", "", "White");
            textblock = XamlGenerator.getTextBlock(student, "12", "Normal", "Normal", "Center", "Center");

            textblock.setAttribute("Margin", "0,1.5,0,1");
            border.addContent(textblock);

            border.removeAttribute("Height");
            border.removeAttribute("Width");
            border.setAttribute("Margin", "0,0,0,8.5");
            border.setAttribute("Cursor", "Hand");

            Element effect = XamlGenerator.getEffect("Border");
            effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "0")); // moet 15 radius zijn van opdracht

            border.addContent(effect);
            stack.addContent(border);
        }

        stack.setAttribute("Grid.Column", "0");
        stack.setAttribute("Grid.Row", "1");
        this.grid.addContent(stack);
    }
    private void fillFunctions() {
        // function buttons
        Element stack = XamlGenerator.getStackPanel("5", "Horizontal");
        Element border;
        Element textblock;

        for (String function : Arrays.asList("Standaardafwijking en gemiddelde", "Minimum en maximum")) {
            border = XamlGenerator.getBorder("0.5", "", "", "White");
            textblock = XamlGenerator.getTextBlock(function, "12", "Normal", "Normal", "Center", "Center");

            textblock.setAttribute("Margin", "5,0,5,0");
            border.addContent(textblock);

            border.removeAttribute("Height");
            border.removeAttribute("Width");
            border.setAttribute("Margin", "0,0,8.5,0");
            border.setAttribute("Cursor", "Hand");

            Element effect = XamlGenerator.getEffect("Border");
            effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "0")); // radius moet 15 zijn volgens opdracht

            border.addContent(effect);
            stack.addContent(border);
        }

        stack.setAttribute("Grid.Column", "1");
        stack.setAttribute("Grid.Row", "2");
        stack.setAttribute("HorizontalAlignment", "Center");
        this.grid.addContent(stack);
    }
    private void fillStudentView() {
        // student name
        Element textblock = XamlGenerator.getTextBlock(this.student.getName(), "25", "Normal", "Bold", "Center", "Top");
        textblock.setAttribute("Grid.Column", "1");
        textblock.setAttribute("Grid.Row", "0");
        this.grid.addContent(textblock);

        // graph lines
        String x1 = "0";
        String x2 = this.canvas.getAttributeValue("Width");
        String y;

        Element line;

        double canvasHeight = Double.parseDouble(this.canvas.getAttributeValue("Height"));
        for (int i = 20;i >= 0;--i) {
            if (i % 2 != 0) { continue; }

            // line
            y = Double.toString(canvasHeight - (canvasHeight / 20 * i)); // as if (0,0) of canvas was bottom left
            line = XamlGenerator.getLine(x1, y, x2, y, "Gray", (i % 10 > 0) ? "1" : "3");

            // line lable
            String labelY = Double.toString(Double.parseDouble(y) - 18);
            textblock = XamlGenerator.getTextBlock(Integer.toString(i), "10","Normal", "Normal");
            textblock.setAttribute("Canvas.Left", "-10");
            textblock.setAttribute("Canvas.Top", labelY);

            this.canvas.addContent(textblock);
            this.canvas.addContent(line);
        }

        this.fillGraph();
    }
    private void fillGraph() {
        String score;
        String bg;

        double s;
        double canvasHeight = Double.parseDouble(this.canvas.getAttributeValue("Height"));

        Element border;
        Element textblock;
        Element stack = XamlGenerator.getStackPanel("15,0,15,0", "Horizontal");
        stack.setAttribute("Canvas.Bottom", "0");
        stack.setAttribute("Width", this.canvas.getAttributeValue("Width"));

        for (String course : this.student.getCourses()) {
            score = this.student.getScore(course);

            textblock = XamlGenerator.getTextBlock(course, "18.5", "Normal", "Bold", "Center", "Center");
            textblock.setAttribute("Foreground", "White");

            s = Double.parseDouble(score);
            if (s > 13) {
                bg = "Green";
            } else if (s < 10) {
                bg = "Red";
            } else { bg = "Yellow"; }

            border = XamlGenerator.getBorder("0", "50", Double.toString(s * canvasHeight / 20), bg);
            border.setAttribute("Opacity", "0.5");
            border.setAttribute("Margin", "15,0,15,0");
            border.setAttribute("VerticalAlignment", "Bottom");

            Element effect = XamlGenerator.getEffect("Border");
            effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "3"));
            border.addContent(effect);

            stack.addContent(border);
        }
        this.canvas.addContent(stack);
    }

    public void update(String student) {
        this.student = new Student(student, this.getStudentResults(student));
        this.init();
        this.fillStudentView();
    }
    public void write() {
        XmlGenerator.writeDocument(XmlGenerator.getDocument(this.grid), "StudentView.xaml", "xaml");
    }

    public static void main(String[] args) {
        StudentView sv = new StudentView("results1.xml");
        sv.write();
    }
}
