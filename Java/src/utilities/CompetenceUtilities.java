package utilities;

import java.util.ArrayList;
import java.util.List;

import models.Competence;
import xml.XmlGenerator;

public class CompetenceUtilities {
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
