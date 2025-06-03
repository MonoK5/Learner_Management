package org.example;

import java.util.ArrayList;
import java.util.Scanner;
public class StudService {

    public static void displayAll() {
        ArrayList<Student> students = StudentDB.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("------------------------------------------------");
        System.out.printf("| %-5s | %-15s | %-7s | %-7s |\n", "ID", "Name", "Score", "Grade");
        System.out.println("------------------------------------------------");

        for (Student s : students) {
            System.out.printf("| %-5d | %-15s | %-7d | %-7d |\n", s.getId(), s.getName(), s.getScore(), s.getGrade());
        }

        System.out.println("------------------------------------------------");
        System.out.println("Total Students: " + students.size());

    }

    private static String capitalizeName(String name) {
        name = name.trim().toLowerCase();
        if (name.isEmpty()) return name;
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }



    public static void addStudent(Scanner input) {
        while (true) {
            String name;
            int score;

            // Validate name input
            while (true) {
                System.out.print("Enter name (or 0 to cancel): ");
                String nameInput = input.nextLine().trim();

                if (nameInput.equals("0")) {
                    System.out.println("Cancelled. Returning to main menu.");
                    return;
                }

                if (nameInput.isEmpty() || !nameInput.matches("[a-zA-Z ]+")) {
                    System.out.println("Invalid name. Use letters only.");
                } else if (nameInput.length() < 2) {
                    System.out.println("Name too short.");
                } else if (nameInput.length() > 30) {
                    System.out.println("Name too long. Please keep it under 30 characters.");
                } else if (nameInput.contains("  ")) { // Beginner-friendly check
                    System.out.println("Remove extra spaces between names.");
                } else if (nameInput.startsWith(" ") || nameInput.endsWith(" ")) {
                    System.out.println("Name cannot start or end with a space.");
                } else {
                    final String validatedName = nameInput;
                    boolean exists = StudentDB.getAllStudents().stream()
                            .anyMatch(s -> s.getName().equalsIgnoreCase(validatedName));
                    {
                        name = validatedName;
                        break;
                    }
                }
            }

            // Capitalize the name properly before saving
            name = capitalizeName(name);

            // Validate score input
            while (true) {
                System.out.print("Enter score (0–100): ");
                String scoreInput = input.nextLine();
                try {
                    score = Integer.parseInt(scoreInput);
                    if (score < 0 || score > 100) {
                        System.out.println("Score must be between 0 and 100.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            // Add student to DB
            Student student = new Student(name, score);
            StudentDB.addStudent(student);
            System.out.println("Student added successfully!");

            // Ask if user wants to add another
            int again;
            while (true) {
                System.out.println("Do you want to add another student?");
                System.out.println("1. Yes");
                System.out.println("2. No (Return to main menu)");
                System.out.print("Choose (1 or 2): ");
                String inputChoice = input.nextLine();
                try {
                    again = Integer.parseInt(inputChoice);
                    if (again == 1 || again == 2) break;
                    else System.out.println("Please enter 1 or 2.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number.");
                }
            }

            if (again == 2) break;
        }
    }


    public static void updateStudent(Scanner input) {
        ArrayList<Student> students = StudentDB.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students to update.");
            return;
        }

        boolean repeat;
        do {
            displayAll();

            int id;
            while (true) {
                System.out.print("Enter student ID to update (or 0 to cancel): ");
                String idInput = input.nextLine();
                if (idInput.equals("0")) {
                    System.out.println("Update cancelled.");
                    return;
                }
                try {
                    id = Integer.parseInt(idInput);
                    if (StudentDB.getStudentById(id) == null) {
                        System.out.println("Student ID not found.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }

            Student student = StudentDB.getStudentById(id);

            int choice;
            while (true) {
                System.out.println("What do you want to update?");
                System.out.println("1. Name");
                System.out.println("2. Score");
                System.out.println("3. Both Name and Score");
                System.out.print("Choose (1-3): ");
                String inputChoice = input.nextLine();
                try {
                    choice = Integer.parseInt(inputChoice);
                    if (choice >= 1 && choice <= 3) break;
                    else System.out.println("Choose 1, 2, or 3.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }

            if (choice == 1 || choice == 3) {
                String newName;
                while (true) {
                    System.out.print("Enter new name (or 0 to cancel): ");
                    newName = input.nextLine().trim();

                    if (newName.equals("0")) return;

                    if (newName.isEmpty() || !newName.matches("[a-zA-Z ]+")) {
                        System.out.println("Invalid name.");
                    } else {
                        break;
                    }
                }
                student.setName(capitalizeName(newName));
            }

            if (choice == 2 || choice == 3) {
                int newScore;
                while (true) {
                    System.out.print("Enter new score (0–100): ");
                    String scoreInput = input.nextLine();
                    try {
                        newScore = Integer.parseInt(scoreInput);
                        if (newScore >= 0 && newScore <= 100) {
                            student.setScore(newScore);
                            break;
                        } else {
                            System.out.println("Score must be 0–100.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid score.");
                    }
                }
            }

            // Save the updated student using StudentDB
            StudentDB.updateStudent(student);

            System.out.println("Student updated.");

            System.out.println("Update another student? (1: Yes, 2: No)");
            String againInput = input.nextLine();
            repeat = againInput.equals("1");

        } while (repeat);
    }






    public static void deleteStudent(Scanner input) {
        ArrayList<Student> students = StudentDB.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students to delete.");
            return;
        }

        boolean repeat;
        do {
            displayAll();

            int id;
            while (true) {
                System.out.print("Enter student ID to delete (or 0 to cancel): ");
                String idInput = input.nextLine();
                try {
                    id = Integer.parseInt(idInput);

                    if (id == 0) {
                        System.out.println("Cancelled. Returning to main menu.");
                        return;
                    }

                    boolean idExists = false;
                    for (Student s : students) {
                        if (s.getId() == id) {
                            idExists = true;
                            break;
                        }
                    }

                    if (!idExists) {
                        System.out.println("Student ID not found. Please try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            // Confirm deletion
            int confirmDelete;
            while (true) {
                System.out.println("Are you sure you want to delete student ID " + id + "?");
                System.out.println("1. Yes, delete");
                System.out.println("2. No, cancel and return to main menu");
                System.out.print("Choose (1-2): ");
                String confirmInput = input.nextLine();
                try {
                    confirmDelete = Integer.parseInt(confirmInput);
                    if (confirmDelete == 1 || confirmDelete == 2) {
                        break;
                    } else {
                        System.out.println("Please choose 1 or 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number.");
                }
            }

            if (confirmDelete == 2) {
                System.out.println("Cancelled. Returning to main menu.");
                return;
            }

            StudentDB.deleteStudent(id);
            System.out.println("Student deleted successfully.");

            int again;
            while (true) {
                System.out.println("Do you want to delete another student?");
                System.out.println("1. Yes");
                System.out.println("2. No (Return to Main Menu)");
                System.out.print("Choose (1-2): ");
                String againInput = input.nextLine();
                try {
                    again = Integer.parseInt(againInput);
                    if (again == 1 || again == 2) {
                        break;
                    } else {
                        System.out.println("Please choose 1 or 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number.");
                }
            }

            repeat = (again == 1);
        } while (repeat);
    }





    public static void calculateAverage() {
        ArrayList<Student> students = StudentDB.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        // Display the table first
        displayAll();

        // Calculate average score
        int totalScore = 0;
        for (Student s : students) {
            totalScore += s.getScore();
        }

        double average = (double) totalScore / students.size();
        System.out.printf("Average Score: %.2f%%\n", average);
        System.out.println("------------------------------------------------");
    }




    public static void searchStudent(Scanner input) {
        while (true) {
            System.out.print("Enter name to search (or 0 to exit): ");
            String name = input.nextLine();

            if (name.equals("0")) {
                System.out.println("Exiting search.");
                break;
            }

            ArrayList<Student> results = StudentDB.searchStudentByName(name);

            if (results.isEmpty()) {
                System.out.println("No matching students found.");
            } else {
                // Print table header
                System.out.printf("%-5s %-15s %-7s %-7s%n", "ID", "Name", "Score", "Grade");
                System.out.println("----------------------------------------");

                // Print each student in a formatted row
                for (Student s : results) {
                    System.out.printf("%-5d %-15s %-7d %-7d%n", s.getId(), s.getName(), s.getScore(), s.getGrade());
                    System.out.println("----------------------------------------");
                }
                break;  // exit after showing results
            }
        }
    }








}