package utilities;

import java.util.List;

import org.jdom2.Element;

import xml.XmlGenerator;

public class StudentUtilities {
    public static List<String> getCourses(String file) {
        List<String> courses = XmlGenerator.searchUniqueAttributes("//Result/@course", file);
        return courses;
    }
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

    public static String getInteractivityId(String element, String student) {
        return String.format("%s_%s", student.replaceAll(" ", "_"), element);
    }
    public static String getInteractivityId(String element, String student, String course) {
        return String.format("%s_%s_%s", student.replaceAll(" ", "_"), course.replaceAll(" ", "_"), element);
    }
}
