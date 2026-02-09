package edu.aitu.oop3.PaymentComponent.exceptions;

import edu.aitu.oop3.CoreComponent.exceptions.AppExceptions;

public class MembershipExpiredException extends AppExceptions {
    public MembershipExpiredException(long memberId) {
        super("Membership expired for member id=" + memberId);
    }
}
