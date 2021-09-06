package it.polimi.ingsw.server.Model;

import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;
import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerStub extends Player {
    HashMap<ResourceEnum, Integer> resources;
    ArrayList<ProductionPower> leaderProductionPower;
    DevelopmentSlots slots;
    Warehouse warehouse;

    public PlayerStub() {
        super("testPlayer", true);
        resources = new HashMap<>();
        leaderProductionPower = new ArrayList<>();
        slots = new DevelopmentSlots();
        resources.put(ResourceEnum.COIN, 0);
        resources.put(ResourceEnum.SERVANT, 0);
        resources.put(ResourceEnum.SHIELD, 0);
        resources.put(ResourceEnum.STONE, 0);
        warehouse = new Warehouse();
    }

    public PlayerStub(DevelopmentSlots slots) {
        this();
        this.slots = slots;
    }

    public PlayerStub(HashMap<ResourceEnum, Integer> resourcesToInsert) {
        this();
        insertResources(resourcesToInsert);
    }

    public void insertResources(HashMap<ResourceEnum, Integer> resourcesToInsert) {
        Strongbox playerStrongbox = super.getStrongbox();
        resourcesToInsert.forEach(playerStrongbox::insertResource);
        //resourcesToInsert.forEach((res, q) -> resources.replace(res, q));
    }

    @Override
    public List<ProductionPower> getLeaderProductionPowers() {
        return super.getLeaderProductionPowers();
    }

    @Override
    public void addLeaderProductionPower(ProductionPower productionPower) {
        super.addLeaderProductionPower(productionPower);
    }

    @Override
    public DevelopmentSlots getSlots() {
        return super.getSlots();
    }

    @Override
    public Warehouse getWarehouse() {
        return warehouse;
    }
}
