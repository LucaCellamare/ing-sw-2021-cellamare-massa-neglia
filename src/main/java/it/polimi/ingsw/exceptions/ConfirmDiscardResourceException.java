package it.polimi.ingsw.exceptions;

public class ConfirmDiscardResourceException extends Exception{
    int resourceQty;
    public ConfirmDiscardResourceException(int resourceQty){
        this.resourceQty = resourceQty;
    }

    public int getResourceQty(){

        return resourceQty;
    }
}
