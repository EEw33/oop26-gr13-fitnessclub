package edu.aitu.oop3.services;

import edu.aitu.oop3.entities.Member;
import edu.aitu.oop3.exceptions.NotFoundException;

public interface MembershipService {
    Member buyMembership(Long memberId, int durationDays) throws NotFoundException;
    Member extendMembership(Long memberId, int durationDays) throws NotFoundException;
}
