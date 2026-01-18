package edu.aitu.oop3;

import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.ClassBooking;
import edu.aitu.oop3.entities.FitnessClass;
import edu.aitu.oop3.entities.Member;
import edu.aitu.oop3.repositories.*;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        IDB db = new DatabaseConnection();

        try (Connection connection = db.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            MemberRepository memberRepo = new MemberRepositoryJdbc(connection);
            FitnessClassRepository classRepo = new FitnessClassRepositoryJdbc(connection);
            BookingRepository bookingRepo = new BookingRepositoryJdbc(connection);

            while (true) {
                printMenu();
                System.out.print("Choose an option: ");
                String input = scanner.nextLine().trim();

                switch (input) {
                    case "1" -> listMembers(memberRepo);
                    case "2" -> listClasses(classRepo);
                    case "3" -> bookClass(scanner, memberRepo, classRepo, bookingRepo);
                    case "4" -> viewHistory(scanner, bookingRepo);
                    case "0" -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }

                System.out.println();
            }

        } catch (Exception e) {
            System.out.println("ERROR:");
            e.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("=== FITNESS CLUB MENU ===");
        System.out.println("1) List members");
        System.out.println("2) List classes");
        System.out.println("3) Book a class");
        System.out.println("4) View attendance history");
        System.out.println("0) Exit");
    }

    private static void listMembers(MemberRepository memberRepo) {
        System.out.println("\n--- MEMBERS ---");
        List<Member> members = memberRepo.findAll();
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        for (Member m : members) {
            System.out.println(m.getId() + " | " + m.getName() + " | " + m.getEmail());
        }
    }

    private static void listClasses(FitnessClassRepository classRepo) {
        System.out.println("\n--- CLASSES ---");
        List<FitnessClass> classes = classRepo.findAll();
        if (classes.isEmpty()) {
            System.out.println("No classes found.");
            return;
        }
        for (FitnessClass c : classes) {
            System.out.println(c.getId() + " | " + c.getName() + " | capacity=" + c.getCapacity());
        }
    }

    private static void bookClass(Scanner scanner,
                                  MemberRepository memberRepo,
                                  FitnessClassRepository classRepo,
                                  BookingRepository bookingRepo) {

        System.out.println("\n--- BOOK A CLASS ---");

        System.out.print("Enter member ID: ");
        long memberIdLong = readLong(scanner);
        if (memberRepo.findById(memberIdLong) == null) {
            System.out.println("Member not found.");
            return;
        }
        if (memberIdLong > Integer.MAX_VALUE) {
            System.out.println("Member ID is too large for current booking repository (int).");
            return;
        }
        int memberId = (int) memberIdLong;

        System.out.print("Enter class ID: ");
        int classId = readInt(scanner);
        FitnessClass fc = classRepo.findById(classId);
        if (fc == null) {
            System.out.println("Class not found.");
            return;
        }

        // class full check
        int current = bookingRepo.countByClassId(classId);
        if (current >= fc.getCapacity()) {
            System.out.println("Class is full.");
            return;
        }

        boolean inserted = bookingRepo.createIfNotExists(memberId, classId);
        if (!inserted) {
            System.out.println("Booking already exists.");
        } else {
            System.out.println("Booked successfully!");
        }
    }

    private static void viewHistory(Scanner scanner, BookingRepository bookingRepo) {
        System.out.println("\n--- ATTENDANCE HISTORY ---");
        System.out.print("Enter member ID: ");
        long memberIdLong = readLong(scanner);

        if (memberIdLong > Integer.MAX_VALUE) {
            System.out.println("Member ID is too large for current booking repository (int).");
            return;
        }
        int memberId = (int) memberIdLong;

        List<ClassBooking> bookings = bookingRepo.findByMemberId(memberId);
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (ClassBooking b : bookings) {
            System.out.println("Booking ID=" + b.getId() +
                    " | memberId=" + b.getMemberId() +
                    " | classId=" + b.getClassId());
        }
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            String s = scanner.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer: ");
            }
        }
    }

    private static long readLong(Scanner scanner) {
        while (true) {
            String s = scanner.nextLine().trim();
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
