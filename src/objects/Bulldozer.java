package objects;

import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

//TODO
//Pode ser necessario mudar o construtor
//
public class Bulldozer extends GameElement {

	public static final int BULLDOZER_LAYER = 2;
		
	public Bulldozer(Point2D position) {
		super("bulldozer_U", position, BULLDOZER_LAYER);
	}
	
	public void move(Direction d) {	
		// TODO
	}
	
	// TODO
}
