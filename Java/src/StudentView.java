import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Element;

import models.Student;
import utilities.MathUtilities;
import utilities.StudentUtilities;
import xaml.XamlGenerator;
import xml.XmlGenerator;

public class StudentView {
    private String file;

    private Element page;
    private Element grid;
    private Element canvas;

    private Student student;

    public StudentView(String file) {
        // xml sources
        this.file = file;

        // get first student data
        String firstStudent = StudentUtilities.getFirstStudent(this.file);
        this.student = new Student(firstStudent, StudentUtilities.getStudentResults(firstStudent, this.file));

        this.init();
    }

    private void init() {
        // page
        this.page = XamlGenerator.getPage();
        XamlGenerator.setXNamespace(this.page);

        // grid
        String[] rowSizes = { "50", "1*", "40" };
        String[] colSizes = { "110", "8*", "1*" };
        this.grid = XamlGenerator.getGrid(rowSizes, colSizes, false);

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

        this.page.addContent(this.grid);

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

        for (String student : StudentUtilities.getStudents(this.file)) {
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
            effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "0"));
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
        double canvasHeight = Double.parseDouble(this.canvas.getAttributeValue("Height"));

        // graph lines
        String x1 = "0";
        String x2 = this.canvas.getAttributeValue("Width");
        String y;

        Element line;

        for (int i = 20;i >= 0;--i) {
            if (i % 2 != 0) { continue; }

            // line
            // from bottom to top - bottom left is (0, 0) if canvasheight - value
            y = Double.toString(canvasHeight - (canvasHeight / 20 * i));
            line = XamlGenerator.getLine(x1, y, x2, y, "Gray", (i % 10 > 0) ? "1" : "3");

            // line lable
            // from top to bottom
            String labelY = Double.toString((canvasHeight / 20 * i) + 2.5);
            Element textblock = XamlGenerator.getTextBlock(Integer.toString(i), "10");
            textblock.setAttribute("Canvas.Left", "-10");
            textblock.setAttribute("Canvas.Bottom", labelY);

            this.canvas.addContent(textblock);
            this.canvas.addContent(line);
        }
    }
    private void generateStudentBlock() {
        // student name
        Element textblock = XamlGenerator.getTextBlock(this.student.getName(), "32", "Normal", "DemiBold", "Center", "Top");

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

        List<Element> courseBorders = new ArrayList<>();
        List<Element> deviationBorders = new ArrayList<>();
        List<Element> lines = new ArrayList<>();
        List<Element> textblocks = new ArrayList<>();

        int i = 0;
        for (String course : this.student.getCourses()) {
            // get score for course
            double score = Double.parseDouble(this.student.getScore(course));

            // get border and texblock heights/placement values
            borderHeight = score * canvasHeight / 20;
            textblockX = (canvasWidth / courseAmount * i) + (canvasWidth / courseAmount);
            ++i;

            // course border
            this.fillCourseBorder(score, borderWidth, borderHeight, textblockX, 0, canvasHeight, courseBorders);

            // textblock
            this.fillCourseTextBlock(course, textblockX, textblockY, borderWidth, borderHeight, textblocks);

            // mean lines and standard deviation borders
            this.fillCourseMeanAndStandardDeviation(course, borderWidth, textblockX, canvasHeight, lines, deviationBorders);
        }

        this.canvas.addContent(deviationBorders);
        this.canvas.addContent(courseBorders);
        this.canvas.addContent(lines);
        this.canvas.addContent(textblocks);

    }
    private void fillCourseBorder(double score, double borderWidth, double borderHeight, double x2, double y,
                                  double canvasHeight, List<Element> borders) {
        // set correct border bg color
        String bg = "Yellow";
        bg = score > 13 ? "Green" : bg;
        bg = score < 10 ? "Red" : bg;

        double x1 = x2 - borderWidth;

        // border
        Element border = XamlGenerator.getBorder("0", Double.toString(borderWidth),
                Double.toString(borderHeight), bg, "0.5");
        border.setAttribute("Canvas.Bottom", Double.toString(y));
        border.setAttribute("Canvas.Left", Double.toString(x1));

        Element effect = XamlGenerator.getEffect("Border");
        effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "3"));
        border.addContent(effect);

        borders.add(border);
    }
    private void fillCourseMeanAndStandardDeviation(String course, double borderWidth, double x2, double canvasHeight,
                                                    List<Element> lines, List<Element> borders) {
        // mean and standard deviation calculations of results
        List<String> results = StudentUtilities.getCourseResults(course, this.file);
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
        this.student = new Student(student, StudentUtilities.getStudentResults(student, this.file));
        this.init();
        this.write();
    }
    public void write() {
        XmlGenerator.writeDocument(XmlGenerator.getDocument(this.page), "StudentView.xaml", "xaml");
    }

    public static void main(String[] args) {
        StudentView sv = new StudentView("results1.xml");
        sv.write();
    }
}
