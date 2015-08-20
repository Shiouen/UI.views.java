import com.sun.org.apache.xalan.internal.XalanConstants;
import models.Student;
import org.jdom2.Document;
import org.jdom2.Element;
import xaml.XamlGenerator;
import xml.XmlGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentView {
    private Document results;
    private String file;

    private Element grid;
    private Element canvas;

    private Student student;

    public StudentView(String name) {
        // xml sources
        this.file = name;
        this.results = XmlGenerator.readDocument(this.file, "xml");

        // get first student data
        String firstStudent = this.getFirstStudent();
        this.student = new Student(firstStudent, this.getResults(firstStudent));

        this.init();
    }

    private String getFirstStudent() {
        String student = XmlGenerator.searchAttribute("//Student/@FullName", this.file);
        return student;
    }
    private List<String> getStudents() {
        List<String> students = XmlGenerator.searchAttributes("//Students/Student/@FullName", this.file);
        return students;
    }
    private List<Element> getResults(String studentName) {
        String xpath = "//Student[@FullName = \"" + studentName + "\"]//Result";
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
        XamlGenerator.setXName(viewbox, "StudentGraph");
        this.grid.addContent(viewbox);

        this.generateFunctionButtons();
        this.generateGraphStructure();
        this.generateStudentBlock();
        this.generateStudentPanel();

        this.fillStudentGraph();
    }

    private void generateStudentPanel() {
        // student names overview
        Element stack = XamlGenerator.getStackPanel("5", "Vertical");
        Element border;
        Element textblock;

        for (String student : this.getStudents()) {
            border = XamlGenerator.getBorder("0.5", "White", "Hand");
            textblock = XamlGenerator.getTextBlock(student, "12", "Normal", "Normal", "Center", "Center");

            textblock.setAttribute("Margin", "0,1.5,0,1");
            border.addContent(textblock);

            border.setAttribute("Margin", "0,0,0,8.5");

            Element effect = XamlGenerator.getEffect("Border");
            effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "0")); // moet 15 radius zijn van opdracht

            border.addContent(effect);
            stack.addContent(border);
        }

        stack.setAttribute("Grid.Column", "0");
        stack.setAttribute("Grid.Row", "1");
        XamlGenerator.setXName(stack, "StudentPanel");
        this.grid.addContent(stack);
    }
    private void generateFunctionButtons() {
        // function buttons
        Element stack = XamlGenerator.getStackPanel("5", "Horizontal");
        Element border;
        Element textblock;

        for (String function : Arrays.asList("Standaardafwijking en gemiddelde", "Minimum en maximum")) {
            border = XamlGenerator.getBorder("0.5", "White", "Hand");
            textblock = XamlGenerator.getTextBlock(function, "12", "Normal", "Normal", "Center", "Center");

            textblock.setAttribute("Margin", "5,0,5,0");
            border.addContent(textblock);

            border.setAttribute("Margin", "0,0,8.5,0");

            Element effect = XamlGenerator.getEffect("Border");
            effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "0")); // radius moet 15 zijn volgens opdracht

            border.addContent(effect);
            stack.addContent(border);
        }

        stack.setAttribute("Grid.Column", "1");
        stack.setAttribute("Grid.Row", "2");
        stack.setAttribute("HorizontalAlignment", "Center");
        XamlGenerator.setXName(stack, "FunctionButtons");
        this.grid.addContent(stack);
    }
    private void generateGraphStructure() {
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
            Element textblock = XamlGenerator.getTextBlock(Integer.toString(i), "10", "Normal", "Normal");
            textblock.setAttribute("Canvas.Left", "-10");
            textblock.setAttribute("Canvas.Top", labelY);

            this.canvas.addContent(textblock);
            this.canvas.addContent(line);
        }
    }
    private void generateStudentBlock() {
        // student name
        Element textblock = XamlGenerator.getTextBlock(this.student.getName(), "25", "Normal", "Bold", "Center", "Top");
        textblock.setAttribute("Grid.Column", "1");
        textblock.setAttribute("Grid.Row", "0");
        XamlGenerator.setXName(textblock, "StudentNameBlock");
        this.grid.addContent(textblock);
    }

    private void fillStudentGraph() {
        double borderHeight;
        double borderWidth = 50;
        double canvasHeight = Double.parseDouble(this.canvas.getAttributeValue("Height"));
        double canvasWidth = Double.parseDouble(this.canvas.getAttributeValue("Width"));
        double textblockY = 0;
        double textblockX;

        double score;
        String bg;

        Element border;
        Element textblock;

        // borderstack
        Element stack = XamlGenerator.getStackPanel("15,0,15,0", "Horizontal");
        stack.setAttribute("Canvas.Bottom", "0");
        stack.setAttribute("Width", this.canvas.getAttributeValue("Width"));

        // list of textblocks
        List<Element> textblocks = new ArrayList<>();

        int i = 0;
        for (String course : this.student.getCourses()) {
            // get score for course
            score = Double.parseDouble(this.student.getScore(course));

            // set correct border bg color
            if (score > 13) {
                bg = "Green";
            } else if (score < 10) {
                bg = "Red";
            } else { bg = "Yellow"; }

            // get border and texblock heights/placement values
            borderHeight = score * canvasHeight / 20;
            textblockX = (canvasWidth / this.student.getCourses().size() * i) + (canvasWidth / this.student.getCourses().size());
            ++i;

            System.out.println(borderHeight);

            // border
            border = XamlGenerator.getBorder("0", Double.toString(borderWidth), Double.toString(borderHeight), bg);

            border.setAttribute("Opacity", "0.5");
            border.setAttribute("Margin", "15,0,15,0");
            border.setAttribute("VerticalAlignment", "Bottom");

            Element effect = XamlGenerator.getEffect("Border");
            effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "3"));
            border.addContent(effect);


            // textblock
            textblock = XamlGenerator.getTextBlock(course, "16", "Normal", "Bold", "White");

            Element layoutTransform = XamlGenerator.getLayoutTransform("TextBlock");
            layoutTransform.addContent(XamlGenerator.getRotateTransform("90"));
            textblock.addContent(layoutTransform);

            Element renderTransform = XamlGenerator.getRenderTransform("TextBlock");
            renderTransform.addContent(XamlGenerator.getTranslateTransform(Double.toString(-(borderWidth)), "0"));
            textblock.addContent(renderTransform);

            textblock.setAttribute("Canvas.Bottom", Double.toString(textblockY));
            textblock.setAttribute("Canvas.Left", Double.toString(textblockX));

            textblock.setAttribute("TextWrapping","Wrap");
            textblock.setAttribute("Width", Double.toString(borderHeight));
            textblock.setAttribute("Height", Double.toString(borderWidth));
            textblock.setAttribute("TextAlignment", "Center");

            XamlGenerator.setXName(textblock, course.replaceAll(" ", "_"));

            // add border and textblock to parents
            stack.addContent(border);
            textblocks.add(textblock);
        }

        XamlGenerator.setXName(stack, "CoursePanel");
        this.canvas.addContent(stack);
        this.canvas.addContent(textblocks);
    }

    public void update(String student) {
        this.student = new Student(student, this.getResults(student));
        this.init();
        this.write();
    }
    public void write() {
        XmlGenerator.writeDocument(XmlGenerator.getDocument(this.grid), "StudentView.xaml", "xaml");
    }

    public static void main(String[] args) {
        StudentView sv = new StudentView("results1.xml");
        sv.write();
    }
}
