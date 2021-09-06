package it.polimi.ingsw.exceptions;

public class ResourceNotAvailableException extends Exception{

    public ResourceNotAvailableException(String errorMessage){
        super(errorMessage);
    }
}
