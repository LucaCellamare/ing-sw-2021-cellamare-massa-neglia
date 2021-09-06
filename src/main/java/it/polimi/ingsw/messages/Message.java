package it.polimi.ingsw.messages;


import java.io.Serializable;

/**
 * Generic message sent through the net
 *
 * @author Roberto Neglia
 */
public abstract class Message implements Serializable {

    private int sender;

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getSender() {
        return sender;
    }

    /**
     * Read method used to implement the visitor pattern client side
     *
     * @param serverVisitor server visitor that will read the message
     */
    public abstract void read(ServerVisitor serverVisitor);

    /**
     * Read method to implement the visitor pattern server side
     *
     * @param clientVisitor client visitor that will read the message
     */
    public abstract void read(ClientVisitor clientVisitor);
}
