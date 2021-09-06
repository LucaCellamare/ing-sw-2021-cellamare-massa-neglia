package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Cards.ProductionPower;

import java.util.List;

/**
 * Bi-directional message, Used to ask the client which power to use and by the server to identify
 * the power and manage its activation
 */
public class ProductionPowerAction extends Message {

    private List<ProductionPower> playerProductionPowers;

    public ProductionPowerAction() {
    }

    public ProductionPowerAction(List<ProductionPower> playerProductionPowers) {
        this.playerProductionPowers = playerProductionPowers;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }

    public List<ProductionPower> getPlayerProductionPowers() {
        return playerProductionPowers;
    }
}
