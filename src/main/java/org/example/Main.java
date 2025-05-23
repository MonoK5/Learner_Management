package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



// Abstract
abstract  class StudentMain{
    abstract void  addNewStudent(Student newStudents) throws SQLException;
    abstract List<Student> displayAllStudent() throws  SQLException;
    abstract void updateStudentDetails(Student updateDetails) throws  SQLException;
    abstract double calculateAllMarksAvg (List<Student> calculateAvg) throws  SQLException;
    abstract List<Student> searchStudent(Student search) throws  SQLException;
    abstract void deleteStudent(int studentId) throws SQLException;
    abstract boolean studentExists(Student exists) throws  SQLException;
}

class Student {
    //Variables
    private static int idCounter = 1;
    private int id;
    private int score;
    private int grade;
    private String name;

    // Constructor
    public Student(String sName, int sMarks) {
        setId(idCounter++);
        setName(sName);
        setScore(sMarks);
        setGrade(10);
    }

    //Setters
    public void setId(int sId) {
        if (sId <= 0) {
            System.out.println("Invalid ID, must be positive");
            return;
        }
        this.id = sId;
    }

    public void setName(String sName) {
        if (sName == null || sName.trim().isEmpty()) {
            System.out.println("Name cannot be empty");
            return;
        }

        if (!sName.matches("[a-zA-Z ]+")) {
            System.out.println("Only letters and spaces are allowed");
            return;
        }

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
                " Student ID: " + getId() +
                ", Name: " + getName() +
                ", Marks: " + getScore() +
                ", Grade: " + getGrade() +
                " }";
    }
}


class StudentMainExtend extends StudentMain{
    @Override
    public  void  addNewStudent(Student newStudents) throws  SQLException{}
    @Override
    public  List<Student> displayAllStudent() throws SQLException {


        var students = new ArrayList<Student>();
        String sql  = "SELECT * FROM stud ORDER BY id";

        try (Connection conn =  Database.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                students.add(new Student(
                        rs.getString("name"),
                        rs.getInt("marks")
                ));
            }
        } catch (SQLException e){
            System.out.println("Student details not available " + e.getMessage());
        }
        return students;
    }
    @Override
    public void updateStudentDetails(Student updateDetails) throws SQLException{}
    @Override
    public double calculateAllMarksAvg(List<Student> calculateAvg) throws  SQLException{
        return  0;
    }
    @Override
    public List<Student> searchStudent(Student search) throws  SQLException{
        return  new ArrayList<>();
    }
    @Override
    public void deleteStudent(int studentId) throws SQLException{}
    @Override
    public boolean studentExists(Student student)throws SQLException{
        return  false;
    }
}


class Database {

    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "Letsdoit!";

    public static void initDB() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String createDB = """
                CREATE TABLE IF NOT EXISTS Students (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50),
                    score INT,
                    grade INT
                );
            """;

            stmt.executeUpdate(createDB);
            System.out.println("Table created");

        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}


public class Main {
    public static void main(String[] args) {
        Database.initDB();
        ArrayList<Student> students = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        students.add(new Student("Jade", 35));
        students.add(new Student("Life", 60));
        students.add(new Student("Jack", 77));

        int select;
        do {
            System.out.println("\n--- Grade 10 Students ---");
            System.out.println("1. Display All Students");
            System.out.println("2. Add Student");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Average Score");
            System.out.println("6. Search Student by Name");
            System.out.println("0. Exit");
            System.out.print("Select a number: ");
            select = Integer.parseInt(input.nextLine());

            switch (select) {
                case 1 -> displayAllStudents(students);
                case 2 -> addStudent(students, input);
                case 3 -> updateStudent(students, input);
                case 4 -> deleteStudent(students, input);
                case 5 -> calculateAverage(students);
                case 6 -> searchStudent(students, input);
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice.");
            }

        } while (select != 0);
    }

    static void displayAllStudents(ArrayList<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\n--- Student List ---");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.println((i + 1) + ". Name: " + s.getName() + ", Score: " + s.getScore()+ ", Grade: " + s.getGrade());
        }
    }


    static void addStudent(ArrayList<Student> students, Scanner input) {
        System.out.print("Enter student name: ");
        String name = input.nextLine();

        if (name.trim().isEmpty() || !name.matches("[^0-9]+")) {
            System.out.println("Invalid name. Name must not be empty or contain numbers.");
            return;
        }

        System.out.print("Enter student score (0-100): ");
        String scoreInput = input.nextLine();

        if (!scoreInput.matches("[0-9]+")) {
            System.out.println("Invalid score. Score must be a number.");
            return;
        }

        int score = Integer.parseInt(scoreInput);
        if (score < 0 || score > 100) {
            System.out.println("Score must be between 0 and 100.");
            return;
        }

        students.add(new Student(name, score));
        System.out.println("Student added successfully.");
    }




    static void updateStudent(ArrayList<Student> students, Scanner input) {
        displayAllStudents(students);
        if (students.isEmpty()) return;


        System.out.print("Are you sure you want to update the student ? (y/n): ");
        String choose = input.nextLine().trim().toLowerCase();
        if (choose.contains("n"))
        {
            System.out.print("Updating student details cancelled ");
        }
        else if(choose.contains("y")) {

            System.out.print("Enter student option to update: (name/score): ");
            String choose1 = input.nextLine().trim().toLowerCase();
            int index;
            String newName;

            switch (choose1) {
                case "name":
                    System.out.println("Choose from number (1 - " + students.size() + ") to update:");
                    index = Integer.parseInt(input.nextLine()) -1;

                    if (index >= 0 && index < students.size()) {

                        System.out.print("Enter new name: ");
                        newName = input.nextLine();

                        students.get(index).setName(newName);

                        System.out.println("Student updated " + newName);
                    }else {
                        System.out.println("Invalid student number.");
                    }

                    break;
                case "score":

                    System.out.println("Choose from number (1 - " + students.size() + ") to update:");
                    index = Integer.parseInt(input.nextLine()) - 1;

                    if (index >= 0 && index < students.size()) {

                        System.out.print("Enter new score: ");
                        int score = Integer.parseInt(input.nextLine());

                        students.get(index).setScore(score);

                        System.out.println("Student updated score" );
                    } else {
                        System.out.println("Invalid number.");
                    }

                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;


            }
        }else {
            System.out.print("Invalid choice. ");
        }

    }

    static void deleteStudent(ArrayList<Student> students, Scanner input) {
        if (students.isEmpty()) {
            System.out.println("No students to delete.");
            return;

        }

        while (true) {
            displayAllStudents(students);
            System.out.println("Choose from number (1 - " + students.size() + ") to delete:");
            int index;

            try {
                index = Integer.parseInt(input.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }

            if (index >= 0 && index < students.size()) {
                Student toDelete = students.get(index);
                System.out.print("Are you sure you want to delete " + toDelete.getName() + "? (y/n): ");
                String confirm = input.nextLine().trim().toLowerCase();

                if (confirm.equals("y")) {
                    students.remove(index);
                    System.out.println("Student deleted!");
                } else {
                    System.out.println("Deletion cancelled.");
                }

                if (students.isEmpty()) {
                    System.out.println("No students left.");
                    break;
                }

                System.out.print("Do you want to delete another student? (y/n): ");
                String again = input.nextLine().trim().toLowerCase();
                if (!again.equals("y")) {
                    System.out.println("Returning to main menu.");
                    break;
                }
            } else {
                System.out.println("Invalid student number.");
            }
        }
    }


    static void calculateAverage(ArrayList<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students to calculate average.");
            return;
        }

        int total = 0;
        for (Student s : students) {
            total += s.getScore();
        }

        double average = (double) total / students.size();
        System.out.printf("Average Score of all students: %.2f%%", average);
    }




    static void searchStudent(ArrayList<Student> students, Scanner input) {
        System.out.print("Enter student name to search: ");
        String nameToSearch = input.nextLine().toLowerCase();
        boolean found = false;

        for (Student s : students) {
            if (s.getName().toLowerCase().equals(nameToSearch)) {
                System.out.println("Found: Name: " + s.getName() + ", Score: " + s.getScore() + ", Grade: " + s.getGrade());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No student found with the name containing: " + nameToSearch);
        }
    }

}

