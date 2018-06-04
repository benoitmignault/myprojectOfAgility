package uqbc;

import java.util.ArrayList;

public class AllEvaluationsByStudent {

    public Student student;
    public ArrayList<Evaluation> evaluationsByStudent;
    public float cumulativeScore;
    public float cumulativeValue;
    public float groupAverage;

    public AllEvaluationsByStudent() {
        this.student = null;
        this.evaluationsByStudent = new ArrayList<>();
        this.cumulativeValue = 0.0f;
        this.cumulativeScore = 0.0f;
        this.groupAverage = 0.0f;
    }

    public AllEvaluationsByStudent(Student student, Evaluation evaluation, float cumulativeValue, float cumulativeScore, float groupAverage) {
        this.student = student;
        this.cumulativeValue = cumulativeValue;
        this.cumulativeScore = cumulativeScore;
        this.groupAverage = groupAverage;
        this.evaluationsByStudent = new ArrayList<>();
        this.evaluationsByStudent.add(evaluation);
    }

    public void addStudentEvaluation(Evaluation evaluation) {
        this.evaluationsByStudent.add(evaluation);
    }

    public void addEvaluation(float cumulativeValue) {
        this.cumulativeValue += cumulativeValue;
    }

    public void addCumulativeScore(float cumulativeScore) {
        this.cumulativeScore += cumulativeScore;
    }

    public void addCumulativeGroupScore(float groupAverage) {
        this.groupAverage += groupAverage;
    }    
    
}
