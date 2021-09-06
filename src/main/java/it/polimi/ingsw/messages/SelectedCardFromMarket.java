package it.polimi.ingsw.messages;

public class SelectedCardFromMarket extends Message {
    private final int level;
    private final int color;


    public SelectedCardFromMarket(int levelSelected, int colorSelected) {
        this.level = levelSelected;
        this.color = colorSelected;
    }

    public int getLevel() {
        return level;
    }

    public int getColor() {
        return color;
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
