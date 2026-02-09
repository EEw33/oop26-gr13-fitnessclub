package edu.aitu.oop3.ReservationComponent.repository;


import edu.aitu.oop3.ReservationComponent.entities.ClassBooking;
import java.util.List;

public interface BookingRepository {
    boolean createIfNotExists(int memberId, int classId);
    List<ClassBooking> findByMemberId(int memberId);
    int countByClassId(int classId);
}
