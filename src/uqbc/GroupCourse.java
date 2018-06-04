package uqbc;

import java.util.ArrayList;

public class GroupCourse {

    public String courseName;
    public ArrayList<String> listOfStudents;

    public GroupCourse() {
        this.courseName = "";
        this.listOfStudents = new ArrayList<>();
    }
    
    public GroupCourse(String courseName) {
        this.courseName = courseName;
        this.listOfStudents = new ArrayList<>();
    }
    
    public void addIdStudent(String student) {
        this.listOfStudents.add(student);
    }
}
