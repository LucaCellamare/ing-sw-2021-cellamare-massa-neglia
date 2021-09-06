package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.VaticanSectionEnum;

/**
 * Message used only server side, used when a player reaches a pope space in the faith track and a
 * vatican report occurs, so the faith track handler tells the controller to update all the players faith track
 */
public class VaticanReport {
    private final VaticanSectionEnum activatedSection;

    public VaticanReport(VaticanSectionEnum activatedSection) {
        this.activatedSection = activatedSection;
    }

    public VaticanSectionEnum getActivatedSection() {
        return activatedSection;
    }
}
