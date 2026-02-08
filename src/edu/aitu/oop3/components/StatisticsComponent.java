package edu.aitu.oop3.components;

import edu.aitu.oop3.entities.Member;
import edu.aitu.oop3.repositories.BookingRepository;

import java.time.LocalDate;
import java.util.List;

public class StatisticsComponent {

    private final BookingRepository bookingRepository;

    public StatisticsComponent(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public long countActiveMembers(List<Member> members) {
        LocalDate today = LocalDate.now();

        return members.stream()
                .filter(m -> m.getMembershipEnd() != null && !m.getMembershipEnd().isBlank())
                .filter(m -> !LocalDate.parse(m.getMembershipEnd()).isBefore(today))
                .count();
    }

    public int countBookingsForClass(int classId) {
        return bookingRepository.countByClassId(classId);
    }
}
