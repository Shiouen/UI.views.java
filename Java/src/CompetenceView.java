import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import models.Student;
import utilities.MathUtilities;
import xaml.XamlGenerator;
import xml.XmlGenerator;

public class CompetenceView {
    private Document competences;
    private String file;

    private Element page;
    private Element grid;
    private Element canvas;

    private CompetenceView() { }
    public CompetenceView(String file) {
        this.file = file;
        this.competences = XmlGenerator.readDocument(this.file, "xml");

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

        this.generateStudentScrollViewer();
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
