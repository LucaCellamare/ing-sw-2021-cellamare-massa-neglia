package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Cards.LeaderCard;

import java.util.List;

/**
 * Message that tells the Client to choose 2 out of 4 Leader Cards before the game starts.
 * It's also used to notify the server the chosen Leader Cards.
 */
public class ChooseLeaderCard extends Message {
    private List<LeaderCard> toChoose;

    private int firstChosenCard;

    private int secondChosenCard;

    public ChooseLeaderCard(List<LeaderCard> leaderCards) {
        this.toChoose = leaderCards;
    }

    public ChooseLeaderCard(int firstChosenCard, int secondChosenCard){
        this.firstChosenCard = firstChosenCard;
        this.secondChosenCard = secondChosenCard;
    }

    public int getFirstChosenCard() {
        return firstChosenCard;
    }

    public int getSecondChosenCard() {
        return secondChosenCard;
    }

    public List<LeaderCard> getToChoose() {
        return toChoose;
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
