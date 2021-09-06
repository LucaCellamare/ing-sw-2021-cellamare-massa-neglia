package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.ActionToken;

/**
 * Message that tells the Client which token Lorenzo has just picked during his turn.
 */
public class LorenzoActionToken extends Message {

    private final ActionToken lorenzoActionToken;

    public LorenzoActionToken(ActionToken lorenzoActionToken) {
        this.lorenzoActionToken = lorenzoActionToken;
    }

    public ActionToken getLorenzoActionToken() {
        return lorenzoActionToken;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
