package org.jan.repositorio.excepciones;

public class SaveException extends  RuntimeException{
    public SaveException(String message) {
        super(message);
    }
}
