package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

import java.util.EnumMap;

/**
 * Message that is only sent from client to server to tell him which resource the player selected
 * when he has activated two white marble leader abilities
 */
public class WhiteMarbleSelection extends Message {
    private final EnumMap<ResourceEnum, Integer> newResources;

    public WhiteMarbleSelection(EnumMap<ResourceEnum, Integer> newResources) {
        this.newResources = newResources;
    }

    public EnumMap<ResourceEnum, Integer> getNewResources() {
        return newResources;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
