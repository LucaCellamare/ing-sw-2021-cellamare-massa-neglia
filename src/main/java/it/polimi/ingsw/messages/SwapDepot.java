package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;

/**
 * Bi-directional message:
 * - from client to server: the player wants to swap two depots of the warehouse
 * - from server to client: the result of the swap (success / fail)
 */
public class SwapDepot extends Message {

    private int depotIndex1;
    private String messageResult;
    private int depotIndex2;
    private boolean successOperation;
    private Warehouse warehouse; //the update warehouse if depots are swapped

    public SwapDepot(String message, boolean successOperation, Warehouse warehouse) {
        this.messageResult = message;
        this.successOperation = successOperation;
        this.warehouse = warehouse;
    }

    public SwapDepot(int depotIndex1, int depotIndex2) {
        this.depotIndex1 = depotIndex1;
        this.depotIndex2 = depotIndex2;
    }

    public int getDepotIndex1() {
        return depotIndex1;
    }

    public int getDepotIndex2() {
        return depotIndex2;
    }

    public String getMessageResult() {
        return messageResult;
    }

    public boolean getSuccessOperation() {
        return successOperation;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    @Override
    public void read(ServerVisitor visitor) {
        visitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor visitor) {
        visitor.elaborate(this);
    }


}
