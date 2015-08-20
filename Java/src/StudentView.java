import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import utilities.MathUtilities;
import models.Student;
import xaml.XamlGenerator;
import xml.XmlGenerator;

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
        this.student = new Student(firstStudent, this.getStudentResults(firstStudent));

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
    private List<Element> getStudentResults(String name) {
        String xpath = "//Student[@FullName = \"" + name + "\"]//Result";
        List<Element> results = XmlGenerator.searchElements(xpath, this.file);
        return results;
    }
    private List<String> getCourseResults(String name) {
        String xpath = String.format("//Result[@course = \"%s\"]", name);
        List<String> results = XmlGenerator.searchContents(xpath, this.file);
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
            Element textblock = XamlGenerator.getTextBlock(Integer.toString(i), "10");
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
        double textblockY = 0;
        double textblockX;

        double canvasHeight = Double.parseDouble(this.canvas.getAttributeValue("Height"));
        double canvasWidth = Double.parseDouble(this.canvas.getAttributeValue("Width"));
        double courseAmount = this.student.getCourses().size();

        List<Element> borders = new ArrayList<>();
        List<Element> lines = new ArrayList<>();
        List<Element> textblocks = new ArrayList<>();

        // borderstack
        Element borderStack = XamlGenerator.getStackPanel("15,0,15,0", "Horizontal");
        borderStack.setAttribute("Canvas.Bottom", "0");
        borderStack.setAttribute("Width", this.canvas.getAttributeValue("Width"));
        XamlGenerator.setXName(borderStack, "CoursePanel");

        int i = 0;
        for (String course : this.student.getCourses()) {
            // get score for course
            double score = Double.parseDouble(this.student.getScore(course));

            // get border and texblock heights/placement values
            borderHeight = score * canvasHeight / 20;
            textblockX = (canvasWidth / courseAmount * i) + (canvasWidth / courseAmount);
            ++i;

            // border
            this.fillCourseBorder(course, borderWidth, borderHeight, borderStack);

            // textblock
            this.fillCourseTextBlock(course, textblockX, textblockY, borderWidth, borderHeight, textblocks);

            // mean lines and standard deviations
            this.fillCourseMeanAndStandardDeviation(course, borderWidth, textblockX, canvasHeight, lines, borders);
        }

        this.canvas.addContent(borders);
        this.canvas.addContent(lines);
        this.canvas.addContent(borderStack);
        this.canvas.addContent(textblocks);

    }
    private void fillCourseBorder(String course, double borderWidth, double borderHeight,
                                  Element stack) {
        // get score for course
        double score = Double.parseDouble(this.student.getScore(course));

        // set correct border bg color
        String bg = "Yellow";
        bg = score > 13 ? "Green" : bg;
        bg = score < 10 ? "Red" : bg;

        // border
        Element border = XamlGenerator.getBorder("0", Double.toString(borderWidth),
                Double.toString(borderHeight), bg, "0.5");

        border.setAttribute("Margin", "15,0,15,0");
        border.setAttribute("VerticalAlignment", "Bottom");

        Element effect = XamlGenerator.getEffect("Border");
        effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "3"));
        border.addContent(effect);

        stack.addContent(border);
    }
    private void fillCourseMeanAndStandardDeviation(String course, double borderWidth, double x2, double canvasHeight,
                                                    List<Element> lines, List<Element> borders) {
        // mean and standard deviation calculations of results
        List<String> results = this.getCourseResults(course);
        double mean = MathUtilities.calculateMean(results);
        double standardDeviation = MathUtilities.calculateStandardDeviation(results);

        // mean line - adjust mean to canvas size
        double y = mean * canvasHeight / 20;
        // lines are drawn from top to bottom, while we work from bottom to top
        y = canvasHeight - y;
        double x1 = x2 - borderWidth;

        Element line = XamlGenerator.getLine(Double.toString(x1), Double.toString(y),
                Double.toString(x2), Double.toString(y), "Black", "3");
        lines.add(line);

        // standard deviation border
        double borderHeight = standardDeviation * 2;
        // adjust to canvas sizes
        borderHeight = borderHeight * canvasHeight / 20;

        Element border = XamlGenerator.getBorder("0", Double.toString(borderWidth),
                Double.toString(borderHeight), "Gray", "0.5");
        border.setAttribute("Canvas.Bottom",Double.toString(canvasHeight - (y + (standardDeviation * canvasHeight / 20))));
        border.setAttribute("Canvas.Left", Double.toString(x1));

        Element effect = XamlGenerator.getEffect("Border");
        effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "3"));
        border.addContent(effect);

        borders.add(border);
    }
    private void fillCourseTextBlock(String course, double x, double y, double textblockWidth, double textblockHeight,
                                     List<Element> textblocks) {
        Element textblock = XamlGenerator.getTextBlock(course, "16", "White", "Wrap");

        textblock.setAttribute("Width", Double.toString(textblockHeight));
        textblock.setAttribute("Height", Double.toString(textblockWidth));
        textblock.setAttribute("TextAlignment", "Center");

        textblock.setAttribute("Canvas.Bottom", Double.toString(y));
        textblock.setAttribute("Canvas.Left", Double.toString(x));

        Element layoutTransform = XamlGenerator.getLayoutTransform("TextBlock");
        layoutTransform.addContent(XamlGenerator.getRotateTransform("90"));
        textblock.addContent(layoutTransform);

        Element renderTransform = XamlGenerator.getRenderTransform("TextBlock");
        renderTransform.addContent(XamlGenerator.getTranslateTransform(Double.toString(-(textblockWidth)), "0"));
        textblock.addContent(renderTransform);

        XamlGenerator.setXName(textblock, course.replaceAll(" ", "_"));

        textblocks.add(textblock);
    }

    public void update(String student) {
        this.student = new Student(student, this.getStudentResults(student));
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
