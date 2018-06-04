package uqbc;

import java.util.ArrayList;

public class SummaryByEvaluation {

    public String evaluationName;
    public String evaluationType;
    public String groupCourseName;
    public int validStudents;
    public float value;
    public float average;
    public float ecartType;
    public float mode;
    public float median;
    public ArrayList<String> students;

    public SummaryByEvaluation() {
        this.evaluationName = "";
        this.validStudents = 0;
        this.groupCourseName = "";
        this.evaluationType = "";
        this.value = 0.0f;
        this.students = new ArrayList<>();
        this.average = 0.0f;
        this.ecartType = 0.0f;
        this.mode = 0.0f;
        this.median = 0.0f;
    }

    public SummaryByEvaluation(String evaluationName, String groupCourseName, String evaluationType, float value) {
        this.evaluationName = evaluationName;
        this.groupCourseName = groupCourseName;
        this.evaluationType = evaluationType;
        this.value = value;
        this.students = new ArrayList<>();
        this.average = 0.0f;
        this.ecartType = 0.0f;
        this.mode = 0.0f;
        this.median = 0.0f;
        this.validStudents = 0;
    }

    public void addStudentEvaluation(String oneStudent) {
        students.add(oneStudent);
    }

    public void addEvaluationAverage(float average) {
        this.average = average;
    }

    public void addNumberOfValidStudents(int validStudents) {
        this.validStudents = validStudents;
    }

    public void addEcartType(float ecartType) {
        this.ecartType = ecartType;
    }

    public void addMode(float mode) {
        this.mode = mode;
    }

    public void addMedian(float median) {
        this.median = median;
    }
}
