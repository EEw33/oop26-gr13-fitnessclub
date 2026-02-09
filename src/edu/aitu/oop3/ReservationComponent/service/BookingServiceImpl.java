package edu.aitu.oop3.ReservationComponent.service;

import edu.aitu.oop3.CatalogComponent.entities.FitnessClass;
import edu.aitu.oop3.MemberManagmentComponent.entities.Member;
import edu.aitu.oop3.ReservationComponent.repository.BookingRepository;
import edu.aitu.oop3.CatalogComponent.repository.FitnessClassRepository;
import edu.aitu.oop3.MemberManagmentComponent.repository.MemberRepository;

import edu.aitu.oop3.CoreComponent.exceptions.AppExceptions;
import edu.aitu.oop3.ReservationComponent.exceptions.BookingAlreadyExistsException;
import edu.aitu.oop3.ReservationComponent.exceptions.ClassFullException;
import edu.aitu.oop3.CoreComponent.exceptions.NotFoundException;
import edu.aitu.oop3.PaymentComponent.exceptions.MembershipExpiredException;

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
