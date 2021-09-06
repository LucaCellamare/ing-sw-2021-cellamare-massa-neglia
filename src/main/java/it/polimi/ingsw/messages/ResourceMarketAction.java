package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.MarbleEnum;

/**
 * It's also used as a response to be sent to the server.
 * Used on the client side to request the current market structure and be displayed and on the server side to send
 * the information as the market
 * @author Antonio Massa
 */
public class ResourceMarketAction extends Message {


    MarbleEnum[][] resourceMarketStructure;

    public ResourceMarketAction(){

    }

    public ResourceMarketAction(MarbleEnum[][] resourceMarketStructure) {
        this.resourceMarketStructure = resourceMarketStructure;
    }


    public MarbleEnum[][] getResourceMarketStructure() {
        return resourceMarketStructure;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
