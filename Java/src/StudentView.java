import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Element;

import models.Student;
import utilities.ListUtilities;
import utilities.MathUtilities;
import utilities.StudentUtilities;
import xaml.XamlGenerator;
import xml.XmlGenerator;

public class StudentView {
    private String file;

    private Element grid;
    private Element canvas;

    private Student student;

    public StudentView(String file) {
        // xml sources
        this.file = file;
        this.init();
    }

    private void init() {
        // grid
        String[] rowSizes = { "50", "1*", "40" };
        String[] colSizes = { "110", "8*", "1*" };
        this.grid = XamlGenerator.getGrid(rowSizes, colSizes, true);
        XamlGenerator.setXNamespace(this.grid);
        this.grid.setAttribute("Loaded", "onLoaded");

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

        // load layout
        this.generateFunctionButtons();
        this.generateGraphStructure();
        this.generateStudentPanel();

        // load all student data
        for (String student : StudentUtilities.getStudents(this.file)) {
            this.student = new Student(student, StudentUtilities.getStudentResults(student, this.file));
            this.generateStudentBlock();
            this.fillStudentGraph();
        }

        this.generateData();
    }

    private void generateStudentPanel() {
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

            // interactivity
            XamlGenerator.setXName(border,
                    StudentUtilities.getInteractivityId("Student_Panel", student));
            border.setAttribute("MouseLeftButtonUp", "selectStudent");

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

            // interactivity
            String functionName = function.equals("Minimum en maximum") ? "Min_Max" : "Mean_StdDev";

            XamlGenerator.setXName(textblock, functionName + "_Text");
            XamlGenerator.setXName(border, functionName + "_Border");

            border.setAttribute("MouseLeftButtonUp", functionName.equals("Min_Max") ? "toggleMinMax" : "toggleMeansStdDevs");

            stack.addContent(border);
        }

        stack.setAttribute("Grid.Column", "1");
        stack.setAttribute("Grid.Row", "2");
        stack.setAttribute("HorizontalAlignment", "Center");
        this.grid.addContent(stack);
    }
    private void generateGraphStructure() {
        double canvasHeight = Double.parseDouble(this.canvas.getAttributeValue("Height"));

        String x1 = "0";
        String x2 = this.canvas.getAttributeValue("Width");
        double y;

        Element line;
        Element textblock;

        for (int lineLabel : ListUtilities.range(0, 21)) {
            if (lineLabel % 2 != 0) { continue; }

            // line
            y = canvasHeight - (canvasHeight / 20 * lineLabel);
            line = XamlGenerator.getLine(x1, Double.toString(y), x2, Double.toString(y),
                    "Gray", (lineLabel % 10 > 0) ? "1" : "3");

            // line textblock label
            textblock = XamlGenerator.getTextBlock(Integer.toString(lineLabel), "10");
            textblock.setAttribute("Canvas.Left", "-10");
            textblock.setAttribute("Canvas.Top", Double.toString(y - 16));

            this.canvas.addContent(textblock);
            this.canvas.addContent(line);
        }
    }
    private void generateStudentBlock() {
        // student name
        Element textblock = XamlGenerator.getTextBlock(this.student.getName(), "32", "Normal", "SemiBold", "Center", "Top");

        textblock.setAttribute("Grid.Column", "1");
        textblock.setAttribute("Grid.Row", "0");

        // interactivity
        XamlGenerator.hide(textblock);
        XamlGenerator.setXName(textblock,
                StudentUtilities.getInteractivityId("Student_Block", this.student.getName()));

        this.grid.addContent(textblock);
    }

    private void generateData() {
        List<String> courses = StudentUtilities.getCourses(this.file);
        List<String> students = StudentUtilities.getStudents(this.file);

        courses = ListUtilities.replace(courses, " ", "_");
        students = ListUtilities.replace(students, " ", "_");

        Element textblock = XamlGenerator.getTextBlock(ListUtilities.join(courses, ' '));
        XamlGenerator.setXName(textblock, "Courses");
        XamlGenerator.hide(textblock);
        this.canvas.addContent(textblock);

        textblock = XamlGenerator.getTextBlock(ListUtilities.join(students, ' '));
        XamlGenerator.setXName(textblock, "Students");
        XamlGenerator.hide(textblock);
        this.canvas.addContent(textblock);
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
            this.fillCourseBorder(course, score, borderWidth, borderHeight, textblockX, canvasHeight, courseBorders);

            // textblock
            this.fillCourseTextBlock(course, textblockX, textblockY, borderWidth, borderHeight, canvasHeight, textblocks);

            // mean lines and standard deviation borders
            this.fillCourseMeanAndStandardDeviation(course, borderWidth, textblockX, canvasHeight, lines, deviationBorders);
        }

        this.canvas.addContent(deviationBorders);
        this.canvas.addContent(courseBorders);
        this.canvas.addContent(lines);
        this.canvas.addContent(textblocks);

    }
    private void fillCourseBorder(String course, double score, double borderWidth, double borderHeight, double x2,
                                  double canvasHeight, List<Element> borders) {
        // set correct border bg color
        String bg = "Yellow";
        bg = score > 13 ? "Green" : bg;
        bg = score < 10 ? "Red" : bg;

        double x1 = x2 - borderWidth;

        // border
        Element border = XamlGenerator.getBorder("0", Double.toString(borderWidth),
                Double.toString(borderHeight), bg, "0.5");
        border.setAttribute("Canvas.Top", Double.toString(canvasHeight - borderHeight));
        border.setAttribute("Canvas.Left", Double.toString(x1));

        Element effect = XamlGenerator.getEffect("Border");
        effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "3"));
        border.addContent(effect);

        // interactivity
        XamlGenerator.hide(border);
        XamlGenerator.setXName(border,
                StudentUtilities.getInteractivityId("Score_Border", this.student.getName(), course));

        borders.add(border);
    }
    private void fillCourseMeanAndStandardDeviation(String course, double borderWidth, double x2, double canvasHeight,
                                                    List<Element> lines, List<Element> borders) {
        // mean and standard deviation calculations of results
        List<String> results = StudentUtilities.getCourseResults(course, this.file);
        double mean = MathUtilities.calculateMean(results);
        double standardDeviation = MathUtilities.calculateStandardDeviation(results);

        // mean line - adjust mean to canvas size
        double y = mean * canvasHeight / 20.0;
        y = canvasHeight - y;
        double x1 = x2 - borderWidth;

        Element line = XamlGenerator.getLine(Double.toString(x1), Double.toString(y),
                Double.toString(x2), Double.toString(y), "Black", "3");

        // interactivity
        XamlGenerator.hide(line);
        XamlGenerator.setXName(line,
                StudentUtilities.getInteractivityId("Mean_Line", this.student.getName(), course));

        lines.add(line);

        // standard deviation border
        double borderHeight = standardDeviation * 2.0;
        // adjust to canvas sizes
        borderHeight = borderHeight * canvasHeight / 20.0;

        Element border = XamlGenerator.getBorder("0", Double.toString(borderWidth),
                Double.toString(borderHeight), "Gray", "0.5");
        border.setAttribute("Canvas.Top",Double.toString(y - (borderHeight / 2.0)));
        border.setAttribute("Canvas.Left", Double.toString(x1));

        Element effect = XamlGenerator.getEffect("Border");
        effect.addContent(XamlGenerator.getDropShadowEffect("15", "60", "3"));
        border.addContent(effect);

        // interactivity
        XamlGenerator.hide(border);
        XamlGenerator.setXName(border,
                StudentUtilities.getInteractivityId("StdDev_Border", this.student.getName(), course));

        borders.add(border);
    }
    private void fillCourseTextBlock(String course, double x, double y, double textblockWidth, double textblockHeight,
                                     double canvasHeight, List<Element> textblocks) {
        Element textblock = XamlGenerator.getTextBlock(course, "16", "White", "Wrap");

        textblock.setAttribute("Width", Double.toString(textblockHeight));
        textblock.setAttribute("Height", Double.toString(textblockWidth));
        textblock.setAttribute("Padding", "10,0,10,0");
        textblock.setAttribute("TextAlignment", "Center");

        textblock.setAttribute("Canvas.Top", Double.toString(canvasHeight - y - textblockHeight));
        textblock.setAttribute("Canvas.Left", Double.toString(x));

        Element renderTransform = XamlGenerator.getRenderTransform("TextBlock");
        renderTransform.addContent(XamlGenerator.getRotateTransform("90"));
        textblock.addContent(renderTransform);

        // interactivity
        XamlGenerator.hide(textblock);
        XamlGenerator.setXName(textblock,
                StudentUtilities.getInteractivityId("Text", this.student.getName(), course));

        textblocks.add(textblock);
    }

    public void write() {
        // write to resources
        //XmlGenerator.writeDocument(XmlGenerator.getDocument(this.grid), "StudentView.xaml", "xaml");
        XmlGenerator.writeStudentViewDocument(XmlGenerator.getDocument(this.grid));
    }

    public static void main(String[] args) {
        StudentView sv = new StudentView("results1.xml");
        sv.write();
    }
}
