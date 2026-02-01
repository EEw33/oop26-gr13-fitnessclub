package edu.aitu.oop3;

import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.entities.ClassBooking;
import edu.aitu.oop3.entities.FitnessClass;
import edu.aitu.oop3.entities.Member;
import edu.aitu.oop3.entities.MembershipType;
import edu.aitu.oop3.exceptions.NotFoundException;
import edu.aitu.oop3.factories.MembershipTypeFactory;
import edu.aitu.oop3.repositories.BookingRepository;
import edu.aitu.oop3.repositories.jdbc.BookingRepositoryJdbc;
import edu.aitu.oop3.repositories.FitnessClassRepository;
import edu.aitu.oop3.repositories.jdbc.FitnessClassRepositoryJdbc;
import edu.aitu.oop3.repositories.MemberRepository;
import edu.aitu.oop3.repositories.jdbc.MemberRepositoryJdbc;
import edu.aitu.oop3.services.MembershipService;
import edu.aitu.oop3.services.impl.MembershipServiceImpl;
import edu.aitu.oop3.utils.Filter;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Scanner scanner = new Scanner(System.in)) {

            MemberRepository memberRepo = new MemberRepositoryJdbc((DatabaseConnection) connection);
            FitnessClassRepository classRepo = new FitnessClassRepository(connection);
            BookingRepository bookingRepo = new BookingRepositoryJdbc(connection);

            while (true) {
                printMenu();
                System.out.print("Choose an option: ");
                String input = scanner.nextLine().trim();

                switch (input) {
                    MembershipService membershipService = new MembershipServiceImpl(memberRepo);
                    case "1" -> listMembers(memberRepo);
                    case "2" -> listClasses(classRepo);
                    case "3" -> bookClass(scanner, memberRepo, classRepo, bookingRepo);
                    case "4" -> viewHistory(scanner, bookingRepo);
                    case "5" -> addMemberWithBuilderAndFactory(scanner, memberRepo);



                    case "6" -> {
                        long memberId = readInt(scanner, "Member ID: ");
                        int days = readInt(scanner, "Duration (days): ");

                        try {
                            membershipService.buyMembership(memberId, days);
                            System.out.println("Membership activated.");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    case "7" -> {
                        long memberId = readInt(scanner, "Member ID: ");
                        int days = readInt(scanner, "Extend by days: ");

                        try {
                            membershipService.extendMembership(memberId, days);
                            System.out.println("Membership extended.");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    case "8" -> {
                        List<Member> members = memberRepo.findAll();

                        List<Member> activeMembers = Filter.filter(members, m -> {
                            String end = m.getMembershipEnd();
                            if (end == null || end.isBlank()) return false;
                            return !LocalDate.parse(end).isBefore(LocalDate.now());
                        });

                        System.out.println("Active members:");
                        for (Member m : activeMembers) {
                            System.out.println(m.getId() + " | " + m.getName() + " | until: " + m.getMembershipEnd());
                        }
                    }


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
        System.out.println("5) Add member (Builder+Factory)");
        System.out.println("6) Buy membership");
        System.out.println("7) Extend membership");
        System.out.println("8) Show active members (Lambda + Predicate)");
        System.out.println("0) Exit");
    }

    private static void listMembers(MemberRepository memberRepo) {
        System.out.println("\n--- MEMBERS ---");
        List<Member> members = memberRepo.findAll();

        members.sort(Comparator.comparing(Member::getName, String.CASE_INSENSITIVE_ORDER));

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

    private static void addMemberWithBuilderAndFactory(Scanner scanner, MemberRepository memberRepo) {
        System.out.println("\n--- ADD MEMBER (Builder+Factory) ---");

        System.out.print("Enter full name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter membership type (BASIC / PREMIUM / STUDENT): ");
        String type = scanner.nextLine().trim();

        MembershipType mt = MembershipTypeFactory.create(type);

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(mt.getDurationDays());

        Member member = new Member.Builder()
                .name(name)
                .email(email)
                .membershipTypeId((long) mt.getId())
                .membershipStart(start.toString())
                .membershipEnd(end.toString())
                .build();

        Member created = memberRepo.create(member);
        System.out.println("Created member id=" + created.getId() + " | " + mt.getName());
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
