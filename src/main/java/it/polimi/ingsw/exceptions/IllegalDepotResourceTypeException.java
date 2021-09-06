package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

public class IllegalDepotResourceTypeException extends Exception {
    private ResourceEnum resource;
    private boolean resourceAlreadyPresent;

    public IllegalDepotResourceTypeException() {
        super("resource already present");
        resourceAlreadyPresent = true;
    }

    public IllegalDepotResourceTypeException(ResourceEnum resource) {
        this.resource = resource;
    }

    public ResourceEnum getActualResourceType() {
        return resource;
    }

    public boolean isResourceAlreadyPresent() {
        return resourceAlreadyPresent;
    }
}
