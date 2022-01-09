package objects;
import utils.Point2D;

public class Chao extends GameElement {

	public static final int FLOOR_LAYER=0; 
	
	public Chao(Point2D position) {
		super("land", position, FLOOR_LAYER);
	}
}
