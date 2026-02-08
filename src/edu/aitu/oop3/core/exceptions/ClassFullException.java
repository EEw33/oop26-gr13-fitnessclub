package edu.aitu.oop3.core.exceptions;

public class ClassFullException extends AppExceptions{
    public ClassFullException(int classId) {
        super("Class is full. classId=" + classId);
    }
}
