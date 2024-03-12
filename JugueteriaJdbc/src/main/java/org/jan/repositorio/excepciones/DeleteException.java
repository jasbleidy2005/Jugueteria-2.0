package org.jan.repositorio.excepciones;

public class DeleteException extends RuntimeException{
    public DeleteException(String message) {
        super(message);
    }
}
