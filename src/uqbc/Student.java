package uqbc;

public class Student {

    public String id;
    public String lastName;
    public String firstName;
    public String courseName;

    public Student() {
        this.id = "";
        this.lastName = "";
        this.firstName = "";
        this.courseName = "";
    }

    public Student(String id, String lastName, String firstName, String courseName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.courseName = courseName;
    }
}