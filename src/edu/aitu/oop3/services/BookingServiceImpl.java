package edu.aitu.oop3.edu.aitu.oop3.services;

import edu.aitu.oop3.entities.FitnessClass;
import edu.aitu.oop3.entities.Member;
import edu.aitu.oop3.repositories.BookingRepository;
import edu.aitu.oop3.repositories.FitnessClassRepository;
import edu.aitu.oop3.repositories.MemberRepository;

import edu.aitu.oop3.exceptions.AppExceptions;
import edu.aitu.oop3.exceptions.BookingAlreadyExistsException;
import edu.aitu.oop3.exceptions.ClassFullException;
import edu.aitu.oop3.exceptions.NotFoundException;
import edu.aitu.oop3.exceptions.MembershipExpiredException;

import java.time.LocalDate;

public class BookingServiceImpl implements BookingService {
    private final MemberRepository memberRepo;
    private final FitnessClassRepository classRepo;
    private final BookingRepository bookingRepo;

    public BookingServiceImpl(MemberRepository memberRepo,
                              FitnessClassRepository classRepo,
                              BookingRepository bookingRepo) {
        this.memberRepo = memberRepo;
        this.classRepo = classRepo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public void bookClass(long memberId, int classId) {
        // 1) Member exists?
        Member member = memberRepo.findById(memberId);
        if (member == null) {
            throw new NotFoundException("Member", memberId);
        }

        // 2) Membership expired?
        // DB DATE type into JAVA STRING type
        String endStr = member.getMembershipEnd();
        if (endStr == null || endStr.isBlank()) {
            throw new MembershipExpiredException(memberId);
        }

        LocalDate endDate;
        try {
            endDate = LocalDate.parse(endStr); // expects yyyy-MM-dd
        } catch (Exception e) {
            throw new AppExceptions("Invalid membership_end format for member id=" + memberId + ": " + endStr, e);
        }

        if (endDate.isBefore(LocalDate.now())) {
            throw new MembershipExpiredException(memberId);
        }

        // 3) Class exists?
        FitnessClass fc = classRepo.findById(classId);
        if (fc == null) {
            throw new NotFoundException("FitnessClass", classId);
        }

        // 4) Class full?
        int current = bookingRepo.countByClassId(classId);
        if (current >= fc.getCapacity()) {
            throw new ClassFullException(classId);
        }

        // 5) Booking already exists? (your repo returns false if conflict)
        boolean inserted = bookingRepo.createIfNotExists((int) memberId, classId);
        if (!inserted) {
            throw new BookingAlreadyExistsException(memberId, classId);
        }
    }
}
