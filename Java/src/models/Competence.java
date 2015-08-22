package models;

import utilities.CompetenceUtilities;

public class Competence {
    private double mean;
    private String name;

    public Competence(String name, double mean) {
        this.name = name;
        this.mean = mean;
    }
    public Competence(String name, String student, String file) {
        this.name = name;
        this.mean = CompetenceUtilities.getStudentCompetenceMean(student, name, file);
    }

    public String getName() { return this.name; }
    public double getMean() { return this.mean; }
}
