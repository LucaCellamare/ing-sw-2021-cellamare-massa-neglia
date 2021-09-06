package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Cards.ProductionPower;

/**
 * Message sent only from client to server when the player wants to activate a production power,
 * it contains the position of the production power inside the list previously sent from the server
 */
public class UseProductionPower extends Message {
    private final ProductionPower selectedProductionPower;

    public UseProductionPower(ProductionPower selectedProductionPower) {
        this.selectedProductionPower = selectedProductionPower;
    }

    public ProductionPower getSelectedProductionPower() {
        return selectedProductionPower;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {

    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
