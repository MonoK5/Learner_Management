package org.example;

public class Student {
    //Variables
    private int id;
    private int score;
    private int grade;
    private String name;


    // Constructor
    public Student(int sId, String sName, int sMarks) {
        setId(sId);
        setName(sName);
        setScore(sMarks);
        setGrade(10);
    }

    //Setters
    public void setId(int sId) {
        if (sId <= 0) {
//            System.out.println("Invalid ID, must be positive");
            return;
        }
        this.id = sId;
    }

    public void setName(String sName) {
        if (sName == null || sName.trim().isEmpty()) {
            System.out.println("Name cannot be empty");
            return;
        }

//        if (!sName.matches("[a-zA-Z ]+")) {
//            System.out.println("Only letters and spaces are allowed");
//            return;
//        }

        if (sName.length() > 50) {
            System.out.println("Maximum 50 characters are allowed");
            return;
        }

        this.name = sName;
    }

    public void setScore(int sMarks) {
        if (sMarks < 0 || sMarks > 100) {
            System.out.println("Score must be between 0-100.");
            return;
        }
        this.score = sMarks;
    }

    public void setGrade(int sGrade) {
        if (sGrade != 10) {
            System.out.println("Only grade 10 students allowed");
            return;
        }
        this.grade = sGrade;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Student Details {" +
                "  ID: " + getId() +
                ", Name: " + getName() +
                ", Marks: " + getScore() +
                ", Grade: " + getGrade() +
                " }";
    }
}