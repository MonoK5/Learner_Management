package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



// Abstract

public class Main {
static int idCount = 1;
    public static void main(String[] args) throws SQLException {

        Connection connection =  Database.initDB();
//        ArrayList<Student> students = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        StudentMainExtend studentMainExtend = new StudentMainExtend(connection);


//        students.add(new Student("Jade", 35));
//        students.add(new Student("Life", 60));
//        students.add(new Student("Jack", 77));

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
            select = input.nextInt();


//            System.out.println("INPUT NAME");
//            String sName = input.next();
//
//            System.out.println("INPUT NUMBER");
//            int sMarks = input.nextInt();
//
//
//            Student student = new Student(sName, sMarks);



//            switch (select){
//                case 1: System.out.println(" Display");
//                break;
//                case 2: System.out.println("Enter student name");
//                      String name = input.next();
//            }

            switch (select) {
                case 1 ->{

                    List<Student> studentList = studentMainExtend.displayAllStudent();

                    for (Student s: studentList){
                        System.out.println(s.getName());
                    }
                }
//                case 2 -> studentMainExtend.addNewStudent(newStudents);
                case 2 -> {
//
//                      StudentMainExtend studentMainExtend1 = new StudentMainExtend();
//
                        System.out.println("Add student name: ");
                        String sName = input.next();
                        System.out.println("Add student marks: ");
                        int sMarks = input.nextInt();

                        Student newStudents = new Student(idCount, sName, sMarks);
                        studentMainExtend.addNewStudent(newStudents);

//                case 3 -> updateStudent(students, input);
//                case 4 -> deleteStudent(students, input);
//                case 5 -> calculateAverage(students);
//                case 6 -> searchStudent(students, input);
                }
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice.");
            }

        } while (select != 0);
    }
}




