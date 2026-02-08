package edu.aitu.oop3.reservation;

import edu.aitu.oop3.core.entities.FitnessClass;
import edu.aitu.oop3.core.entities.Member;
import edu.aitu.oop3.core.repositories.BookingRepository;
import edu.aitu.oop3.core.repositories.FitnessClassRepository;
import edu.aitu.oop3.core.repositories.MemberRepository;

import edu.aitu.oop3.core.exceptions.AppExceptions;
import edu.aitu.oop3.core.exceptions.BookingAlreadyExistsException;
import edu.aitu.oop3.core.exceptions.ClassFullException;
import edu.aitu.oop3.core.exceptions.NotFoundException;
import edu.aitu.oop3.core.exceptions.MembershipExpiredException;

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
        Member member = memberRepo.findById(memberId);
        if (member == null) {
            throw new NotFoundException("Member", memberId);
        }

        String endStr = member.getMembershipEnd();
        if (endStr == null || endStr.isBlank()) {
            throw new MembershipExpiredException(memberId);
        }

        LocalDate endDate;
        try {
            endDate = LocalDate.parse(endStr);
        } catch (Exception e) {
            throw new AppExceptions("Invalid membership_end format for member id=" + memberId + ": " + endStr, e);
        }

        if (endDate.isBefore(LocalDate.now())) {
            throw new MembershipExpiredException(memberId);
        }

        FitnessClass fc = classRepo.findById(classId);
        if (fc == null) {
            throw new NotFoundException("FitnessClass", classId);
        }

        int current = bookingRepo.countByClassId(classId);
        if (current >= fc.getCapacity()) {
            throw new ClassFullException(classId);
        }

        boolean inserted = bookingRepo.createIfNotExists((int) memberId, classId);
        if (!inserted) {
            throw new BookingAlreadyExistsException(memberId, classId);
        }
    }
}
