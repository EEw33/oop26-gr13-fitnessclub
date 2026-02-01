package edu.aitu.oop3.exceptions;

public class BookingAlreadyExistsException extends AppExceptions {
    public BookingAlreadyExistsException(long memberId, int classId){
        super("Booking already exists for member " + memberId + " and class " + classId);
    }
}
