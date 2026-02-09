package edu.aitu.oop3.PaymentComponent.service;

import edu.aitu.oop3.MemberManagmentComponent.entities.Member;
import edu.aitu.oop3.CoreComponent.exceptions.NotFoundException;

public interface MembershipService {
    Member buyMembership(Long memberId, int durationDays) throws NotFoundException;
    Member extendMembership(Long memberId, int durationDays) throws NotFoundException;
}
