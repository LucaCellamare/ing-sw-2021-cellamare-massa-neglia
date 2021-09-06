package it.polimi.ingsw.exceptions;

public class DepotNotFoundException extends Exception {
    public DepotNotFoundException(String errorMessage){
        super(errorMessage);
    }

}
