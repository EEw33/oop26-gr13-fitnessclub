package edu.aitu.oop3.services.impl;

import edu.aitu.oop3.entities.Member;
import edu.aitu.oop3.exceptions.NotFoundException;
import edu.aitu.oop3.repositories.MemberRepository;
import edu.aitu.oop3.services.MembershipService;

import java.time.LocalDate;

public class MembershipServiceImpl implements MembershipService {

    private final MemberRepository memberRepo;

    public MembershipServiceImpl(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    @Override
    public Member buyMembership(Long memberId, int durationDays) throws NotFoundException {
        Member m = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found: " + memberId));

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(durationDays);

        m.setMembershipStart(start.toString()); // ISO yyyy-MM-dd
        m.setMembershipEnd(end.toString());

        memberRepo.update(m);
        return m;
    }

    @Override
    public Member extendMembership(Long memberId, int durationDays) throws NotFoundException {
        Member m = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found: " + memberId));

        LocalDate today = LocalDate.now();

        // Parse existing end date (String -> LocalDate)
        LocalDate currentEnd = null;
        String endStr = m.getMembershipEnd();
        if (endStr != null && !endStr.isBlank()) {
            currentEnd = LocalDate.parse(endStr); // expects yyyy-MM-dd
        }

        LocalDate base = (currentEnd == null || currentEnd.isBefore(today)) ? today : currentEnd;
        LocalDate newEnd = base.plusDays(durationDays);

        // If membershipStart missing, set it
        String startStr = m.getMembershipStart();
        if (startStr == null || startStr.isBlank()) {
            m.setMembershipStart(today.toString());
        }

        m.setMembershipEnd(newEnd.toString());

        memberRepo.update(m);
        return m;
    }
}
