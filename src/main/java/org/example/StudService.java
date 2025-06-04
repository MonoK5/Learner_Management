package org.example;

import java.sql.SQLException;
import java.util.*;

public class StudService {

    // Encapsulation: Using private input to restrict direct access from other classes
    private static final Scanner input = new Scanner(System.in);

    /**
     * Displays all students.
     * Demonstrates: Encapsulation and Abstraction
     * - Encapsulation: hides how students are displayed.
     * - Abstraction: uses StudentDB to fetch data, hiding database details.
     */
    public static void displayAll() {
        List<Student> students = StudentDB.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("------------------------------------------------");
        System.out.printf("| %-5s | %-15s | %-7s | %-7s |\n", "ID", "Name", "Score", "Grade");
        System.out.println("------------------------------------------------");
        students.forEach(s -> System.out.printf("| %-5d | %-15s | %-7d | %-7d |\n",
                s.getId(), s.getName(), s.getScore(), s.getGrade()));
        System.out.println("------------------------------------------------");
        System.out.printf("| Total Students: %-27d |\n", students.size());
        System.out.println("------------------------------------------------");
    }

    /**
     * Adds a new student after validation.
     * Demonstrates: Encapsulation and Abstraction
     */
    public static void addStudent(Scanner input) throws SQLException {
        do {
            String name = getValidName("Enter name (or 0 to cancel): ");
            if (name == null) return;

            // Abstraction: uses StudentDB to query student existence
            if (StudentDB.getAllStudents().stream().anyMatch(s -> s.getName().equalsIgnoreCase(name))) {
                System.out.println("Student already exists.");
                continue;
            }

            int score = getValidScore("Enter score (0–100): ");

            // Abstraction: uses StudentDB to add new student
            StudentDB.addStudent(new Student(capitalizeName(name), score));
        } while (askYesNo("Add another student?"));
    }

    /**
     * Updates a student’s name or score or both.
     * Demonstrates: Encapsulation and Abstraction
     */
    public static void updateStudent(Scanner input) {
        List<Student> students = StudentDB.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students to update.");
            return;
        }

        do {
            displayAll(); // Encapsulation: separate display logic

            int id = getValidId("Enter student ID to update (or 0 to cancel): ");
            if (id == 0) return;

            Student student = StudentDB.getStudentById(id);
            if (student == null) {
                System.out.println("Student not found.");
                continue;
            }

            System.out.println("1. Update Name\n2. Update Score\n3. Update Both");
            int choice = getValidOption("Choose option (1-3): ", 1, 3);

            if (choice == 1 || choice == 3) {
                String newName = getValidName("Enter new name (or 0 to cancel): ");
                if (newName != null) {
                    student.setName(capitalizeName(newName));
                    System.out.println("Name updated successfully.");
                }
            }

            if (choice == 2 || choice == 3) {
                int newScore = getValidScore("Enter new score (0–100): ");
                student.setScore(newScore);
                System.out.println("Score updated successfully.");
            }

            StudentDB.updateStudent(student); // Abstraction
        } while (askYesNo("Update another student?"));
    }

    /**
     * Deletes a student by ID.
     * Demonstrates: Encapsulation and Abstraction
     */
    public static void deleteStudent(Scanner input) throws SQLException {
        do {
            displayAll();

            int id = getValidId("Enter student ID to delete (or 0 to cancel): ");
            if (id == 0) return;

            if (askYesNo("Are you sure you want to delete student ID " + id + "?")) {
                StudentDB.deleteStudent(id); // Abstraction
                System.out.println("Student deleted.");
            }
        } while (askYesNo("Delete another student?"));
    }

    /**
     * Searches for students by name.
     * Demonstrates: Encapsulation and Abstraction
     */
    public static void searchStudent(Scanner input) throws SQLException {
        boolean searchAgain = false;
        do {
            System.out.println("\n\t--- Search Student ---");
            System.out.print("Enter student name to search: ");
            String nameToSearch = input.nextLine().trim();

            if (nameToSearch.isEmpty()) {
                System.out.println("Search term cannot be empty.");
                continue;
            }

            List<Student> results = StudentDB.searchStudentByName(nameToSearch); // Abstraction

            System.out.println("------------------------------------------------");
            System.out.println("| ID   | NAME           | GRADE | SCORE/MARKS |");
            System.out.println("------------------------------------------------");

            if (results.isEmpty()) {
                System.out.printf("| %-4d | %-14s | %-5d | \t%-8d |\n", 0, capitalizeName(nameToSearch), 0, 0);
            } else {
                for (Student s : results) {
                    System.out.printf("| %-4d | %-14s | %-5d | \t%-8d |\n",
                            s.getId(), capitalizeName(s.getName()), s.getGrade(), s.getScore());
                }
            }

            System.out.println("------------------------------------------------");
            System.out.print("Search again? (1 = Yes, 2 = No): ");
            String again = input.nextLine();
            searchAgain = again.equals("1");

        } while (searchAgain);
    }

    /**
     * Calculates and displays the average score of all students.
     * Demonstrates: Encapsulation and Abstraction
     */
    public static void calculateAverage() {
        List<Student> students = StudentDB.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        double average = students.stream().mapToInt(Student::getScore).average().orElse(0);

        System.out.println("------------------------------------------------");
        System.out.println("| TOTAL STUDENTS | AVERAGE SCORE/MARKS          |");
        System.out.println("------------------------------------------------");
        System.out.printf("| %-14d | \t%-26.2f |\n", students.size(), average);
        System.out.println("------------------------------------------------");
    }

    // ----------------- Helper Methods (Encapsulation) -----------------

    // Capitalizes the first letter of a name
    static String capitalizeName(String name) {
        name = name.trim().toLowerCase();
        return name.isEmpty() ? name : Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    // Validates name input
    private static String getValidName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String inputName = input.nextLine().trim();
            if (inputName.equals("0")) return null;

            if (!inputName.matches("[a-zA-Z ]{2,30}") || inputName.contains("  ")) {
                System.out.println("Invalid name. Use letters only, 2–30 characters, no double spaces.");
            } else {
                return inputName;
            }
        }
    }

    // Validates score input
    private static int getValidScore(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int score = Integer.parseInt(input.nextLine());
                if (score >= 0 && score <= 100) return score;
                System.out.println("Score must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid score. Enter a number.");
            }
        }
    }

    // Validates numeric ID input
    private static int getValidId(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
            }
        }
    }

    // Yes/No prompt
    private static boolean askYesNo(String question) {
        while (true) {
            System.out.print(question + " (1: Yes, 2: No): ");
            String inputChoice = input.nextLine();
            if (inputChoice.equals("1")) return true;
            if (inputChoice.equals("2")) return false;
            System.out.println("Please enter 1 or 2.");
        }
    }

    // Validates choice from a range
    private static int getValidOption(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int option = Integer.parseInt(input.nextLine());
                if (option >= min && option <= max) return option;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid option. Choose between " + min + " and " + max + ".");
        }
    }
}
