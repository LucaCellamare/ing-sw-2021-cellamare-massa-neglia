package it.polimi.ingsw.exceptions;

public class CannotBuyException extends Exception {
    Exception e;
    public CannotBuyException(Exception e){
        this.e = e;
    }
}
