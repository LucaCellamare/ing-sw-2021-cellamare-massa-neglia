package it.polimi.ingsw.server.Controller;

import it.polimi.ingsw.exceptions.CannotInsertException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.Controller.Handlers.DevelopmentSlotsHandler;
import it.polimi.ingsw.server.Controller.Handlers.FaithTrackHandler;
import it.polimi.ingsw.server.Controller.Handlers.StrongboxHandler;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.server.Model.MultiplayerGame;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.VirtualView;

import java.util.Iterator;
import java.util.List;

/**
 * Controller part of the MVC pattern, is used along the Controller to control the multiplayer game
 */
public class MultiPlayerController extends Controller {
    /**
     * Instance of the multiplayer game
     */
    private final MultiplayerGame game;
    /**
     * Instance of the virtual view
     */
    private final VirtualView virtualView;

    /**
     * Class constructor
     *
     * @param virtualView instance of the virtual view
     * @param nPlayers    number of players that will be playing
     */
    public MultiPlayerController(VirtualView virtualView, int nPlayers) {
        super(virtualView);
        FaithTrackHandler.setController(this);
        this.virtualView = virtualView;
        game = new MultiplayerGame(nPlayers);
        super.setNPlayers(nPlayers);
        super.setupController = new SetupController(virtualView, game);
        super.isSetup = true;
        super.phaseController = new PhaseController(virtualView, game);
        super.endGameController = new EndGameController(virtualView, game, phaseController);
        super.setGame(game);
        virtualView.setGame(game);
    }

    /**
     * Adds a player to the game if his nickname isn't already taken
     *
     * @param e the message sent from the client with the player's name
     */
    @Override
    public void update(AskName e) {
        if (nicknameAlreadyTaken(e.getPlayerName()))
            virtualView.nameAlreadyTaken(e.getSender());
        else {
            boolean hasInkwell = e.getSender() == 0;
            Player newPlayer = new Player(e.getPlayerName(), hasInkwell);
//            newPlayer.getFaithTrack().subscribeObserver(this);
            game.addPlayer(e.getSender(), newPlayer);
            super.setupController.pickLeaderCard(e.getSender());
        }
    }

    /**
     * Keeps the game going if a player disconnects
     */
    @Override
    public void continueGameAfterDisconnection() {
        checkCardDevelopmentToInsert();

        //if the player has any resource obtained from production powers, they will automatically inserted in the stronbox
        StrongboxHandler.finallyInsert(game.getCurrentPlayer().getStrongbox());

        game.nextPlayer();
        if (game.getCurrentPlayer().isConnected())
            phaseController.playTurn();
    }

    /**
     * if the player is disconnected while is buying a dev card, if he has already paid it, automatically it
     * will insert in the first available slot
     */
    private void checkCardDevelopmentToInsert(){
        DevelopmentCard cardToInsert = DevelopmentSlotsHandler.getCardToInsert();
        if(cardToInsert != null)
        {
            DevelopmentSlots playerSlots = game.getCurrentPlayer().getSlots();
            if(playerSlots.canInsert(cardToInsert,0))
            {
                try {
                    game.getCurrentPlayer().getSlots().addCard(cardToInsert,0);
                } catch (CannotInsertException e) {
                    System.out.println("not insertable in slot 1");
                }
            }else if(playerSlots.canInsert(cardToInsert,1))
            {
                try {
                    game.getCurrentPlayer().getSlots().addCard(cardToInsert,1);
                } catch (CannotInsertException e) {
                    System.out.println("not insertable in slot 1");
                }
            }else if(playerSlots.canInsert(cardToInsert,2))
            {
                try {
                    game.getCurrentPlayer().getSlots().addCard(cardToInsert,2);
                } catch (CannotInsertException e) {
                    System.out.println("not insertable in slot 1");
                }
            }

            DevelopmentSlotsHandler.setCardToInsert(null);
        }
    }

    /**
     * Checks if the given nickname is already taken by another player
     *
     * @param nickname name to be checked
     * @return true if the name is already taken, false if not
     */
    private boolean nicknameAlreadyTaken(String nickname) {
        boolean alreadyTaken = false;
        List<Player> players = game.getPlayers();
        Iterator<Player> i = players.listIterator();

        while (i.hasNext() && !alreadyTaken) {
            Player currPlayer = i.next();
            if (currPlayer != null)
                alreadyTaken = currPlayer.getNickname().equals(nickname);
        }
        return alreadyTaken;
    }

    /**
     * Updates the faith track of all the players when a vatican section is activated
     *
     * @param e message that contains the info about the vatican section activated
     */
    public void update(VaticanReport e) {
        for (Player p : game.getPlayers()) {
            FaithTrack pFaithTrack = p.getFaithTrack();
            pFaithTrack.vaticanReport(e.getActivatedSection());
        }
    }

    /**
     * Discards all the resources that the player has not inserted in the warehouse,
     * and gives the same amount of faith points of the number of the discarded resources to the other players
     *
     * @param playerIndex index of the player that discarded the resources
     * @param quantity    quantity of resources discarded
     */
    @Override
    public void discardResource(int playerIndex, int quantity) {
        for (Player p : game.getPlayers()) {
//            FaithTrack pFaithTrack = p.getFaithTrack();
            if (!p.getNickname().equals(game.getCurrentPlayer().getNickname()))
                FaithTrackHandler.updatePlayerPosition(p.getFaithTrack(), quantity);
//                pFaithTrack.updatePlayerPosition(quantity);
        }
    }

    /**
     * Notify the controller that the player has ended his turn
     */
    public void update(EndTurn m) {
        endTurn(m.getSender());
    }

    /**
     * Ends the turn of the player
     *
     * @param playerIndex index of the player that has finished his turn
     */
    public void endTurn(int playerIndex) {
        virtualView.update(playerIndex, new FaithTrackUpdate(game.getCurrentPlayer().getFaithTrack(), false));
        if (multiGameFinished())
            virtualView.update(new EndGame());
        else {
            game.nextPlayer();
            phaseController.playTurn();
        }
    }

    /**
     * Checks if the multi player game is finished (if any player has won the game)
     *
     * @return true if a player has won the game, false if not
     */
    public boolean multiGameFinished() {
        List<Player> players = game.getPlayers();
        for (Player player : players) {
            if (player.isTerminator())
                return true;
        }
        return false;
    }

}
