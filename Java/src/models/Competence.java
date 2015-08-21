package models;

public class Competence {
    private double mean;
    private String name;

    public Competence(String name, double mean) {
        this.name = name;
        this.mean = mean;
    }

    public String getName() { return this.name; }
    public String getMean() {
        return Double.toString(this.mean);
    }
}
