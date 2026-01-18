package edu.aitu.oop3;

import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.Member;
import edu.aitu.oop3.repositories.MemberRepository;
import edu.aitu.oop3.repositories.MemberRepositoryJdbc;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        IDB db = new DatabaseConnection();
        MemberRepository memberRepo = new MemberRepositoryJdbc(db);

        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> addMember(memberRepo);
                case 2 -> showAllMembers(memberRepo);
                case 3 -> findMemberById(memberRepo);
                case 0 -> {
                    System.out.println("Exiting application.");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }

            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("=== Fitness Club Management (Console App) ===");
        System.out.println("1. Add member");
        System.out.println("2. Show all members");
        System.out.println("3. Find member by ID");
        System.out.println("0. Exit");
    }

    private static void addMember(MemberRepository repo) {
        System.out.println("== Add Member ==");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        repo.create(new Member(0, name, email));
        System.out.println("Member added successfully.");
    }

    private static void showAllMembers(MemberRepository repo) {
        System.out.println("== All Members ==");
        for (Member m : repo.findAll()) {
            System.out.println(
                    m.getId() + " | " + m.getName() + " | " + m.getEmail()
            );
        }
    }

    private static void findMemberById(MemberRepository repo) {
        int id = readInt("Enter member ID: ");

        repo.findById(id).ifPresentOrElse(
                m -> System.out.println("Found: " + m.getName() + " (" + m.getEmail() + ")"),
                () -> System.out.println("Member not found.")
        );
    }

    private static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
