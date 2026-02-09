package edu.aitu.oop3.CoreComponent.exceptions;

public class NotFoundException extends AppExceptions {
    public NotFoundException(String entity, Object id) {
        super(entity + " not found: " + id);
    }
}
