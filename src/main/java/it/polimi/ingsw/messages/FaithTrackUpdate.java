package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;

/**
 * Message used to notify the Client when there have been an update in his/her FaithTrack.
 */
public class FaithTrackUpdate extends Message {

    private final FaithTrack updatedFaithTrack;

    private final boolean lorenzo;

    public FaithTrackUpdate(FaithTrack updatedFaithTrack, boolean lorenzo) {
        this.updatedFaithTrack = updatedFaithTrack;
        this.lorenzo = lorenzo;
    }

    public boolean isLorenzo() {
        return lorenzo;
    }

    public FaithTrack getUpdatedFaithTrack() {
        return updatedFaithTrack;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
