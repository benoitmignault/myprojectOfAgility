package uqbc;

public class Evaluation {

    public Student student;
    public String type;
    public String name;
    public String courseName;
    public float value;
    public int score;

    public Evaluation() {
        this.student = null;
        this.type = "";
        this.name = "";
        this.courseName = "";
        this.value = 0.0f;
        this.score = 0;
    }

    public Evaluation(Student student, String type, String name, float value, int score, String courseName) {
        this.student = student;
        this.type = type;
        this.courseName = courseName;
        this.name = name;
        this.value = value;
        this.score = score;
    }
}
