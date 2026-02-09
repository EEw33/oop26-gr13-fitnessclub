package edu.aitu.oop3.ReservationComponent.exceptions;

import edu.aitu.oop3.CoreComponent.exceptions.AppExceptions;

public class BookingAlreadyExistsException extends AppExceptions {
    public BookingAlreadyExistsException(long memberId, int classId){
        super("Booking already exists for member " + memberId + " and class " + classId);
    }
}
