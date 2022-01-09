package objects;

import utils.Point2D;

public abstract class Pedra extends GameElement implements Breakable {
    protected Pedra(String name, Point2D position, int layer) {
        super(name, position, layer);
    }

    @Override
    public boolean isTransposable(){
        return false;
    }
}
