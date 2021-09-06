package it.polimi.ingsw.server.Controller;

import it.polimi.ingsw.client.handlers.ProductionPowersHandler;
import it.polimi.ingsw.exceptions.CannotInsertException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.Controller.Handlers.DevelopmentSlotsHandler;
import it.polimi.ingsw.server.Controller.Handlers.FaithTrackHandler;
import it.polimi.ingsw.server.Controller.Handlers.StrongboxHandler;
import it.polimi.ingsw.server.Model.ActionToken;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Enums.CellTypeEnum;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.SinglePlayerGame;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardMarket;
import it.polimi.ingsw.server.VirtualView;

/**
 * Controller part of the MVC pattern, is used along the Controller to control the singleplayer game
 */
public class SinglePlayerController extends Controller {
    /**
     * Instance of the multiplayer game
     */
    private final VirtualView virtualView;
    /**
     * Instance of the virtual view
     */
    private final SinglePlayerGame game;

    /**
     * Class constructor
     *
     * @param virtualView instance of the virtual view
     */
    public SinglePlayerController(VirtualView virtualView) {
        super(virtualView);
        FaithTrackHandler.setController(this);
        this.virtualView = virtualView;
        game = new SinglePlayerGame();
        super.setNPlayers(1);
        super.setupController = new SetupController(virtualView, game);
        super.isSetup = true;
        super.phaseController = new PhaseController(virtualView, game);
        super.endGameController = new EndGameController(virtualView, game, super.phaseController);
        super.setGame(game);
        virtualView.setGame(game);
    }

    /**
     * Adds the single player to the game
     *
     * @param e the message sent from the client with the player's name
     */
    public void update(AskName e) {
        Player newPlayer = new Player(e.getPlayerName(), true);

        game.addPlayer(e.getSender(), newPlayer);
        super.setupController.pickLeaderCard(e.getSender());
    }

    /**
     * Ends the turn of the player if the disconnects
     */
    @Override
    public void continueGameAfterDisconnection() {
        checkCardDevelopmentToInsert();

        //if the player has any resource obtained from production powers, they will automatically inserted in the stronbox
        StrongboxHandler.finallyInsert(game.getCurrentPlayer().getStrongbox());

        playActionToken();
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
     * Discards all the resources that the player has not inserted in the warehouse,
     * and gives the same amount of faith points of the number of the discarded resources to Lorenzo
     *
     * @param playerIndex index of the player that discarded the resources
     * @param quantity    quantity of resources discarded
     */
    @Override
    public void discardResource(int playerIndex, int quantity) {
        FaithTrack lorenzoFaithTrack = game.getLorenzoFaithTrack();

        FaithTrackHandler.updatePlayerPosition(lorenzoFaithTrack, quantity);
    }

    /**
     * Notify the controller that the player has ended his turn
     */
    public void update(EndTurn m) {
        endTurn(0);
    }

    /**
     * Plays the single player action token on top of the deck,
     * which indicates a Lorenzo's action
     */
    private void playActionToken() {
        ActionToken lorenzoToken = game.pickToken();
        lorenzoToken.playActionEffect(game);
        if (game.getCurrentPlayer().isConnected()) {
            virtualView.update(0, new LorenzoActionToken(lorenzoToken));
            virtualView.update(0, new FaithTrackUpdate(game.getLorenzoFaithTrack(), true));
            virtualView.update(0, new FaithTrackUpdate(game.getCurrentPlayer().getFaithTrack(), false));
        }
    }

    /**
     * Ends the turn of the player
     *
     * @param playerIndex index of the player that has finished his turn
     */
    public void endTurn(int playerIndex) {
        virtualView.update(0, new FaithTrackUpdate(game.getCurrentPlayer().getFaithTrack(), false));
        virtualView.update(0, new FaithTrackUpdate(game.getLorenzoFaithTrack(), true));

        if (blackCrossFinished()) {
            endGameController.countPlayerVictoryPoints(game.getCurrentPlayer());
            virtualView.update(0, new GameOver("Lorenzo il Magnifico", "Lorenzo's black cross reached the end of the faith track"));
        } else if (game.getCurrentPlayer().isTerminator()) {
            endGameController.countPlayerVictoryPoints(game.getCurrentPlayer());
            virtualView.update(0, new GameOver(game.getCurrentPlayer(), true));
        } else {
            playActionToken();
            if (emptyColumn()) {
                endGameController.countPlayerVictoryPoints(game.getCurrentPlayer());
                virtualView.update(0, new GameOver("Lorenzo il Magnifico", "Empty column in card market"));
            } else if (blackCrossFinished()) {
                endGameController.countPlayerVictoryPoints(game.getCurrentPlayer());
                virtualView.update(0, new GameOver("Lorenzo il Magnifico", "Lorenzo's black cross reached the end of the faith track"));
            } else {
                if (game.getCurrentPlayer().isConnected())
                    phaseController.playTurn();
            }
        }
    }

    /**
     * Checks if the card market has a empty column
     *
     * @return true if the card market has a empty column, false if not
     */
    private boolean emptyColumn() {
        CardMarket cardMarket = game.getTable().getCardMarket();

        boolean endGame = false;

        for (int i = 0; i < 4 && !endGame; i++) {
            if (cardMarket.emptyColumn(i))
                endGame = true;
        }

        return endGame;
    }

    /**
     * Checks if Lorenzo reached the last cell of his faith track
     *
     * @return true if Lorenzo reached the end of the faith track, false if not
     */
    private boolean blackCrossFinished() {
        FaithTrack lorenzoFaithTrack = game.getLorenzoFaithTrack();
        return lorenzoFaithTrack.getCell().getCellType() == CellTypeEnum.LAST;
    }

    /**
     * Update the player's or Lorenzo's faith track when one of the two reaches a Pope cell
     *
     * @param e message that contains the info about the vatican section activated
     */
    public void update(VaticanReport e) {
        game.getCurrentPlayer().getFaithTrack().vaticanReport(e.getActivatedSection());
        game.getLorenzoFaithTrack().vaticanReport(e.getActivatedSection());
    }
}
