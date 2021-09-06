package it.polimi.ingsw.exceptions;

public class MaxLeaderCardsException extends Exception {
    public MaxLeaderCardsException() {
        super("The player already has two cards");
    }
}
