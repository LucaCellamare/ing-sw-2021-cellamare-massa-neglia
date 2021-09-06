package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Player;

/**
 * Message used to notify the Client that the Game has just ended.
 */
public class GameOver extends Message {
    private Player winnerPlayer;
    private boolean winner;
    private int yourFaithPoints;

    private String lorenzoIlMagnifico;
    private String terminator;

    public GameOver(Player winnerPlayer, boolean winner, int yourFaithPoints) {
        this.winnerPlayer = winnerPlayer;
        this.winner = winner;
        this.yourFaithPoints = yourFaithPoints;
    }

    public GameOver(Player winnerPlayer, boolean winner) {
        this(winnerPlayer, winner, winnerPlayer.getTotalVictoryPoints());
//        this.winnerPlayer = winnerPlayer;
//        this.winner = winner;
//        this.yourFaithPoints = winnerPlayer.getTotalVictoryPoints();
    }

    public GameOver(String lorenzoIlMagnifico, String terminator) {
        this.lorenzoIlMagnifico = lorenzoIlMagnifico;
        this.terminator = terminator;
    }

    public Player getWinnerPlayer() {
        return winnerPlayer;
    }

    public String getWinnerPlayerNickname() {
        return winnerPlayer.getNickname();
    }

    public int getYourFaithPoints() {
        return yourFaithPoints;
    }

    public boolean isWinner() {
        return winner;
    }

    public String getTerminator() {
        return terminator;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
    }
}
