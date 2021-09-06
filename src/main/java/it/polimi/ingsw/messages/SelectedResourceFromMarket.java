package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeLineEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;

import java.util.EnumMap;

/**
 * It's also used as a response to be sent to the server.
 *
 * @author Antonio Massa
 */
public class SelectedResourceFromMarket extends Message {


    private TypeLineEnum typeSelected;
    private int selectedLine;

    private EnumMap<ResourceEnum,Integer> newResourceList;
    private Warehouse warehouse;


    public SelectedResourceFromMarket(TypeLineEnum typeSelected, int selectedLine) {
        this.selectedLine = selectedLine;
        this.typeSelected = typeSelected;
    }

    public SelectedResourceFromMarket(EnumMap<ResourceEnum,Integer> newResourceList,Warehouse warehouse) {
        this.newResourceList = newResourceList;
        this.warehouse = warehouse;
    }

    public TypeLineEnum getTypeSelected() {
        return typeSelected;
    }

    public int getSelectedLine() {
        return selectedLine;
    }

    public EnumMap<ResourceEnum, Integer> getNewResourceList() {
        return newResourceList;
    }

    public Warehouse getWarehouse(){
        return warehouse;
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
