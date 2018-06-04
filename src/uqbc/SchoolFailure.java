package uqbc;

public class SchoolFailure {

    public Student student;
    public float cumulativeScore;
    public float cumulativeValue;
    public float convertScoreOn100;

    public SchoolFailure() {
        this.student = null;
        this.cumulativeScore = 0.0f;
        this.cumulativeValue = 0.0f;
        this.convertScoreOn100 = 0.0f;
    }
    
    public SchoolFailure(Student student, float cumulativeScore, float cumulativeValue) {
        this.student = student;
        this.cumulativeScore = cumulativeScore;
        this.cumulativeValue = cumulativeValue;
        this.convertScoreOn100 = cumulativeScore;
    }

    public SchoolFailure(Student student, float cumulativeScore, float cumulativeValue, float convertScoreOn100) {
        this.student = student;
        this.cumulativeScore = cumulativeScore;
        this.cumulativeValue = cumulativeValue;
        this.convertScoreOn100 = convertScoreOn100;
    }
}
