package objects;

import utils.Point2D;

public class Arvore extends GameElement {

	public Arvore(Point2D position) {
		super("pine", position, 1);
	}

	@Override
	public boolean isTransposable() {
		return false;
	}
}
