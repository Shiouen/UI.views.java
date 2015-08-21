package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

public class Student {
    private String name;
    private List<String> courses;
    private Map<String, String> results;

    public Student(String name) { this.name = name; }
    public Student(String name, List<Element> results) {
        this.name = name;

        this.results = new HashMap<>();
        this.courses = new ArrayList<>();

        for (Element result : results) {
            String course = result.getAttribute("course").getValue();
            String score = result.getValue();
            this.results.put(course, score);
            this.courses.add(course);
        }
    }

    public String getName() { return this.name; }
    public String getScore(String course) { return this.results.get(course); }
    public List<String> getCourses() { return this.courses; }
}
