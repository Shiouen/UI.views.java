package utilities;

import java.util.List;

import org.jdom2.Element;
import xml.XmlGenerator;

public class StudentUtilities {
    public static String getFirstStudent(String file) {
        String student = XmlGenerator.searchAttribute("//Student/@FullName", file);
        return student;
    }
    public static List<String> getStudents(String file) {
        List<String> students = XmlGenerator.searchAttributes("//Students/Student/@FullName", file);
        return students;
    }
    public static List<Element> getStudentResults(String name, String file) {
        String xpath = "//Student[@FullName = \"" + name + "\"]//Result";
        List<Element> results = XmlGenerator.searchElements(xpath, file);
        return results;
    }
    public static List<String> getCourseResults(String name, String file) {
        String xpath = String.format("//Result[@course = \"%s\"]", name);
        List<String> results = XmlGenerator.searchContents(xpath, file);
        return results;
    }
}
