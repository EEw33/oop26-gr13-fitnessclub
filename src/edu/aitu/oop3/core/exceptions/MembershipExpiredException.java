package edu.aitu.oop3.core.exceptions;

public class MembershipExpiredException extends AppExceptions{
    public MembershipExpiredException(long memberId) {
        super("Membership expired for member id=" + memberId);
    }
}
