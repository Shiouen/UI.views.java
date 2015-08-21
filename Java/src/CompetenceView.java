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

    private Element grid;
    private Element canvas;

    private CompetenceView() { }
    public CompetenceView(String file) {
        this.file = file;
        this.competences = XmlGenerator.readDocument(this.file, "xml");

        this.init();
    }

    private void init() {
        // grid
        String[] rowSizes = {"50", "1*", "40"};
        String[] colSizes = {"200", "80*", "10*"};
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
    }

    public void write() {
        XmlGenerator.writeDocument(XmlGenerator.getDocument(this.grid), "CompetenceView.xaml", "xaml");
    }

    public static void main(String[] args) {
        CompetenceView cv = new CompetenceView("competences.xml");
        cv.write();
    }
}
