package utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Competence;
import org.jdom2.Element;
import xaml.XamlGenerator;
import xml.XmlGenerator;

public class CompetenceUtilities {
    public static void scaleMeans(double usedCanvasWidth, double usedCanvasHeight,
                                  List<Competence> competences, Map<String, Double> means) {
        // get scaled means, according to graph sizes
        for (Competence competence : competences) {
            String compName = competence.getName();
            if (compName.equals("Management") || compName.equals("AnalyseOntwerp")) {
                means.put(competence.getName(), competence.getMean() * usedCanvasWidth / 2.0 / 20.0);
            } else {
                means.put(competence.getName(), competence.getMean() * usedCanvasHeight / 2.0 / 20.0);
            }
        }
    }
    public static void fillLine(double centerX, double centerY, double x, double y,
                                List<Element> lines, String brush, String brushThickness) {
        String x1 = Double.toString(centerX + x);
        String x2 = Double.toString(centerX);
        String y1 = Double.toString(centerY);
        String y2 = Double.toString(centerY + y);
        lines.add(XamlGenerator.getLine(x1, y1, x2, y2, brush, brushThickness));
    }
    public static void fillLine(double centerX, double centerY, double x, double y,
                                List<Element> lines, String brush, String brushThickness,
                                String visibility, String xName) {
        String x1 = Double.toString(centerX + x);
        String x2 = Double.toString(centerX);
        String y1 = Double.toString(centerY);
        String y2 = Double.toString(centerY + y);

        Element line = XamlGenerator.getLine(x1, y1, x2, y2, brush, brushThickness, visibility);
        XamlGenerator.setXName(line, xName);

        lines.add(line);
    }
    public static List<Competence> getCompetences(String file) {
        List<String> competenceNames = XmlGenerator.searchUniqueContents("//Competence", file);
        List<Competence> competences  = new ArrayList<>();

        double mean;
        for (String competence : competenceNames) {
            mean = getCompetenceMean(competence, file);
            competences.add(new Competence(competence, mean));
        }

        return competences;
    }
    public static double getCompetenceMean(String competence, String file) {
        List<String> results = XmlGenerator.searchContents("//Course//Competence[. = \"" + competence +
                "\"]/../..//Result", file);
        return MathUtilities.calculateMean(results);
    }
    public static String getFirstStudent(String file) {
        String student = XmlGenerator.searchAttribute("//Result/@Student", file);
        return student;
    }
    public static List<String> getStudents(String file) {
        List<String> students = XmlGenerator.searchUniqueAttributes("//Result/@Student", file);
        return students;
    }
    public static double getStudentCompetenceMean(String student, String competence, String file) {
        List<String> results = XmlGenerator.searchContents("//Course//Competence[. = \"" + competence +
                "\"]/../..//Result[@Student = \"" + student + "\"]", file);
        return MathUtilities.calculateMean(results);
    }

}
