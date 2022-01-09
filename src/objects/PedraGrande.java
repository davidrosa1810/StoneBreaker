package objects;

import engine.StoneBreaker;
import utils.Point2D;

// TODO
// Pode ser necessario mudar a declaracao da classe e/ou construtor
//
public class PedraGrande extends Pedra {

	public PedraGrande(Point2D position) {
		super("bigstone", position, 1);
	}

	@Override
	public void brokenBy(Bulldozer b) {
		StoneBreaker.getInstance().removeObject(this);
		StoneBreaker.getInstance().addObject(new PedraPequena(getPosition()));
		givePointsTo(b);
	}

	@Override
	public void givePointsTo(Bulldozer b) {
		b.addPoints(2);
	}
	
	// TODO
}
