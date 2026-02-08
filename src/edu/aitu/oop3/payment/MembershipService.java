package edu.aitu.oop3.payment;

import edu.aitu.oop3.core.entities.Member;
import edu.aitu.oop3.core.exceptions.NotFoundException;

public interface MembershipService {
    Member buyMembership(Long memberId, int durationDays) throws NotFoundException;
    Member extendMembership(Long memberId, int durationDays) throws NotFoundException;
}
