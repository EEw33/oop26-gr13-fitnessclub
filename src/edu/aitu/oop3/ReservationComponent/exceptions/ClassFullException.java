package edu.aitu.oop3.ReservationComponent.exceptions;

import edu.aitu.oop3.CoreComponent.exceptions.AppExceptions;

public class ClassFullException extends AppExceptions {
    public ClassFullException(int classId) {
        super("Class is full. classId=" + classId);
    }
}
