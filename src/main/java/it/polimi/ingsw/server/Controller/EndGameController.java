package it.polimi.ingsw.server.Controller;

import it.polimi.ingsw.messages.GameOver;
import it.polimi.ingsw.messages.TotalVictoryPoints;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Cards.LeaderCard;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.server.Model.Game;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;
import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.server.Model.Player;

import java.util.List;

/**
 * Sub-controller that handles the end of a Game,
 * in particular it computes the totalVictoryPoints of every player
 * and choose the winner.
 */
public class EndGameController {
    /**
     * Reference to the virtual view, used to communicate with the real view Client side
     */
    private final VirtualView virtualView;
    /**
     * Reference to the game, where all the data is stored
     */
    private final Game game;
    /**
     * Controls the turn cycle during the game (after the setup)
     */
    private final PhaseController phaseController;

    /**
     * Class constructor
     *
     * @param virtualView reference to the virtual view
     * @param game        reference to the game
     * @param phaseController reference to the phaseController
     */
    public EndGameController(VirtualView virtualView, Game game, PhaseController phaseController) {
        this.virtualView = virtualView;
        this.game = game;
        this.phaseController = phaseController;
    }


    /**
     * Method that ensures that all the players play the same number of turns in a game,
     * in particular if the current player is the first one,
     * the victoryPoints computation must proceed otherwise the other player must start their last turn.
     */
    public void playLastTurn() {
        List<Player> players = game.getPlayers();
        game.nextPlayer();
        Player currentPlayer = game.getCurrentPlayer();
        if (players.indexOf(currentPlayer) == 0) {
            countVictoryPoints();
        } else {
            phaseController.playTurn();
        }
    }

    /**
     * Private method used to compare the victoryPoints cumulated by all the players
     * and select the winner.
     */
    private void countVictoryPoints() {
        List<Player> players = game.getPlayers();
        Player currentWinner = game.getCurrentPlayer();

        int playerVictoryPoints;
        int maxVictoryPoints = 0;
        int currentWinnerNResources = 0;
        int tempPlayerNResources;
        int winnerIndex = 0;

        int i = 0;
        for (Player p : players) {
            countPlayerVictoryPoints(p);
            playerVictoryPoints = p.getTotalVictoryPoints();
            virtualView.update(i, new TotalVictoryPoints(playerVictoryPoints));
            if (playerVictoryPoints > maxVictoryPoints) {
                maxVictoryPoints = playerVictoryPoints;
                winnerIndex = i;
                currentWinner = p;
                currentWinnerNResources = currentWinner.getStrongbox().getTotalResourcesCount() + currentWinner.getWarehouse().getTotalResourcesAmount();
            } else if (playerVictoryPoints == maxVictoryPoints) {
                tempPlayerNResources = p.getWarehouse().getTotalResourcesAmount() + p.getStrongbox().getTotalResourcesCount();
                if (tempPlayerNResources > currentWinnerNResources) {
                    winnerIndex = i;
                    currentWinner = p;
                    currentWinnerNResources = tempPlayerNResources;
                }
            }
            i++;
        }

        theWinnerIs(winnerIndex);
    }

    /**
     * Method used to compute the victoryPoints cumulated by all the players,
     * in particular the victoryPoints corresponding to the bought cards, to the leaderCard activated, to the points cumulated on the faithTrack
     * and to the resources left in the strongbox and in the warehouse.
     */
    public void countPlayerVictoryPoints(Player p) {
        int playerVictoryPoints = 0;

        DevelopmentSlots playerDevSlots = p.getSlots();
        List<DevelopmentCard> cards = playerDevSlots.getCards();
        List<LeaderCard> playerLeaderCards = p.getLeaderCards();
        Strongbox playerStrongbox = p.getStrongbox();
        Warehouse playerWarehouse = p.getWarehouse();
        FaithTrack playerFaithTrack = p.getFaithTrack();

        playerVictoryPoints += cards.stream().mapToInt(DevelopmentCard::getVictoryPoints).sum();
        playerVictoryPoints += playerLeaderCards.stream().filter(LeaderCard::isActive).mapToInt(LeaderCard::getVictoryPoints).sum();
        playerVictoryPoints += Math.floorDiv(playerStrongbox.getTotalResourcesCount() + playerWarehouse.getTotalResourcesAmount(), 5);
        playerVictoryPoints += playerFaithTrack.getPlayerVictoryPoints();


        p.setTotalVictoryPoints(playerVictoryPoints);
    }

    /**
     * Private method used to notify the players at the end of the game about the winner of the game and
     * the victoryPoints cumulated in the course of the Game.
     */
    private void theWinnerIs(int winnerIndex) {
        List<Player> players = game.getPlayers();
        Player winner = players.get(winnerIndex);
        int i = 0;
        for (Player p : players) {
            virtualView.update(i, new GameOver(winner, i == winnerIndex, p.getTotalVictoryPoints()));
            i++;
        }
    }
}
