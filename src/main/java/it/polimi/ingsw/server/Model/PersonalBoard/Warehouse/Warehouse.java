package it.polimi.ingsw.server.Model.PersonalBoard.Warehouse;

import it.polimi.ingsw.exceptions.ConfirmDiscardResourceException;
import it.polimi.ingsw.exceptions.DepotNotFoundException;
import it.polimi.ingsw.exceptions.IllegalDepotResourceTypeException;
import it.polimi.ingsw.exceptions.IllegalDepotSwapException;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The place where a Player stores the resources obtained from the market
 *
 * @author Antonio Massa
 */

public class Warehouse implements Serializable {

    /**
     * Depots of the warehouse
     */
    private final ArrayList<Depot> depots;

    /**
     * Class Builder
     * Init the depots and add them to the list
     */
    public Warehouse() {
        depots = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            Depot depot = new Depot(i);
            depots.add(depot);
        }
    }

    /**
     * Getter
     *
     * @return list of the depots
     */
    public ArrayList<Depot> getDepots() {
        return depots;
    }

    /**
     * @param capacity of the depot searched
     * @return depot
     * @throws IllegalArgumentException the capacity is not a number between 1 and 4 or is 4 but not leader ability activated
     */
    public Depot getDepotFromCapacity(int capacity) throws IllegalArgumentException { // quindi niente eccezione, controlliamo sopra
        if (capacity <= 0 || capacity >= 4) throw new IllegalArgumentException("Capacity out of range");
        return depots.get(capacity - 1);
    }

    public Depot getDepotFromResourceType(ResourceEnum resource) throws DepotNotFoundException, IllegalArgumentException {
        if (resource.equals(ResourceEnum.NONE)) throw new IllegalArgumentException("Resource not valid");
        Depot depotFound = this.depots.stream()
                .filter(depot -> depot.getResource().equals(resource))
                .findAny()
                .orElse(null);

        if (depotFound == null) throw new DepotNotFoundException("Depot not found for this resource type");
        return depotFound;
    }

    public ArrayList<Depot> getLeaderAbilityDepots() throws DepotNotFoundException {
        if (depots.size() == 3) throw new DepotNotFoundException("Depot non ottenuto");
        return new ArrayList<>(depots.subList(3, depots.size()));
    }

    public void remove(ResourceEnum resource, int quantity) {
        Depot depot;
        try {
            depot = getDepotFromResourceType(resource);
            depot.remove(quantity);
        } catch (DepotNotFoundException ignored) {
        }
    }

    /**
     * Removes resources from depots
     *
     * @param listResources list of resources to remove
     */
    public void remove(HashMap<ResourceEnum, Integer> listResources) {
        depots.forEach(depot -> {
            ResourceEnum resource = depot.getResource();
            if (listResources.containsKey(resource)) {
                depot.remove(listResources.get(resource));
            }
        });
    }

    /**
     * Insert resources into depot
     *
     * @param resource   type
     * @param quantity   of resource
     * @param depotIndex of depot where the player wants to place the resources
     * @throws IllegalArgumentException if arguments are not valid
     */
    // quantity è corretta, viene modificata nel client nel caso in cui il tryInsert entri nel ConfirmDiscardResourceException, altrimenti è il valore iniziale
    public void insert(ResourceEnum resource, int quantity, int depotIndex) throws IllegalArgumentException {
        if (quantity <= 0 || depotIndex < 0) throw new IllegalArgumentException("");

        Depot depotSelected = depots.get(depotIndex);
        if (depotSelected.getResource().equals(ResourceEnum.NONE)) {
            depotSelected.setResource(resource);
        }
        depotSelected.put(quantity);
    }

    /**
     * @param resource Type of resource
     * @return the quantity of the resource into depot
     * @throws IllegalArgumentException the resource is not in any depot
     */
    public int getResourceAmount(ResourceEnum resource) {

        int qty;

        qty = depots.stream()
                .filter(depot -> depot.getResource().equals(resource))
                .findFirst()
                .map(Depot::getAmountStored)
                .orElse(0);
        return qty;
    }

    /**
     * Exchange type and qty of depot1 and depot2
     *
     * @param depot1 first depot
     * @param depot2 second depot
     * @throws IllegalDepotSwapException if depots can't swapped
     */
    public void move(Depot depot1, Depot depot2) throws IllegalDepotSwapException {
        if (!canSwap(depot1, depot2)) throw new IllegalDepotSwapException("No switchable depots");
        swap(depot1, depot2);
    }

    /**
     * Exchange type and qty of depot1 and depot2
     *
     * @param dpt1 first depot
     * @param dpt2 second depot
     */
    private void swap(Depot dpt1, Depot dpt2) {

        Depot tempDepot = new Depot(dpt1.getSize());
        tempDepot.setResource(dpt1.getResource());
        tempDepot.put(dpt1.getAmountStored());

        if (!dpt1.isLeaderAbility())
            dpt1.setResource(dpt2.getResource());
        else dpt1.remove(dpt1.getAmountStored());
        dpt1.put(dpt2.getAmountStored());

        if (!dpt2.isLeaderAbility())
            dpt2.setResource(tempDepot.getResource());
        else dpt2.remove(dpt2.getAmountStored());
        dpt2.put(tempDepot.getAmountStored());

    }

    /**
     * check if depots can swapped
     *
     * @param dpt1 depot1
     * @param dpt2 depot2
     * @return true if depots can swapped, else false
     */
    private boolean canSwap(Depot dpt1, Depot dpt2) {
        boolean resourceCheck = true, quantityCheck;

        if (dpt1.isLeaderAbility()) {
            resourceCheck = dpt2.getResource() == ResourceEnum.NONE || dpt2.getResource() == dpt1.getResource();
        }
        if (dpt2.isLeaderAbility()) {
            resourceCheck = dpt1.getResource() == ResourceEnum.NONE || dpt1.getResource() == dpt2.getResource();
        }
        quantityCheck = dpt1.getAmountStored() <= dpt2.getSize() && dpt2.getAmountStored() <= dpt1.getSize();
        return resourceCheck && quantityCheck;
    }


    /**
     * Method used to add new depot because of an leader card ability activated
     *
     * @param depotResource the resource that can be stored in this depot
     * @param size          the size of the depot
     */
    public void addDepotFromAbility(ResourceEnum depotResource, int size) {
        Depot depot = new Depot(size, true);
        depot.setResource(depotResource);
        this.depots.add(depot);
    }


    //indice del deposito lo controlliamo da client, appena effettuata la selezione
    public void tryInsert(ResourceEnum resource, int quantity, int depotIndex) throws IllegalDepotResourceTypeException, ConfirmDiscardResourceException {
        Depot depotSelected, alreadyPresentDepot;

        depotSelected = depots.get(depotIndex);
        try {
            depotSelected = depots.get(depotIndex);
            alreadyPresentDepot = getDepotFromResourceType(resource);
            if (!depotSelected.equals(alreadyPresentDepot))
                throw new IllegalDepotResourceTypeException();
            else {
                int discardedResourceQty = quantity - depotSelected.getFreeSpace();
                if (!depotSelected.getResource().equals(resource) && !depotSelected.getResource().equals(ResourceEnum.NONE))
                    throw new IllegalDepotResourceTypeException(depotSelected.getResource());
                if (discardedResourceQty > 0) {
                    throw new ConfirmDiscardResourceException(discardedResourceQty);
                }
            }
        } catch (DepotNotFoundException e) {
            int discardedResourceQty = quantity - depotSelected.getFreeSpace();
            if (!depotSelected.getResource().equals(resource) && !depotSelected.getResource().equals(ResourceEnum.NONE))
                throw new IllegalDepotResourceTypeException(depotSelected.getResource());
            if (discardedResourceQty > 0) {
                throw new ConfirmDiscardResourceException(discardedResourceQty);
            }
        }

    }

    public int getTotalResourcesAmount() {
        return depots.stream().mapToInt(Depot::getAmountStored).sum();
    }

    /**
     * Check if warehouse is all empty
     *
     * @return true if warehouse is empty
     */
    public boolean isEmpty() {
        boolean empty;

        empty = depots.stream()
                .mapToInt(Depot::getAmountStored)
                .noneMatch(qty -> qty > 0);


//        empty = this.depots.stream()
//                .filter(Predicate.not(Depot::isLeaderAbility))
//                .map(Depot::getResource)
//                .allMatch(ResourceEnum.NONE::equals);


        return empty;
    }

}
