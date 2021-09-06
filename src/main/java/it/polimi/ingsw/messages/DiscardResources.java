package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

import java.util.EnumMap;

/**
 * Message used to discard the new resources obtained from the Resource Market.
 */
public class DiscardResources extends Message {

    private final EnumMap<ResourceEnum, Integer> resourceToDiscard;

    public DiscardResources(EnumMap<ResourceEnum, Integer> resourceToDiscard) {
        this.resourceToDiscard = resourceToDiscard;
    }

    public EnumMap<ResourceEnum, Integer> getResourceToDiscard() {
        return resourceToDiscard;
    }


    @Override
    public void read(ServerVisitor serverVisitor) {
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
