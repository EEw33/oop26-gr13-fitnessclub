package edu.aitu.oop3.core.exceptions;

public class NotFoundException extends AppExceptions {
    public NotFoundException(String entity, Object id) {
        super(entity + " not found: " + id);
    }
}
