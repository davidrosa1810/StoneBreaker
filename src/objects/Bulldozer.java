package objects;

import java.util.List;

import engine.StoneBreaker;
import utils.Direction;
import utils.Point2D;

//TODO
//Pode ser necessario mudar o construtor
//
public class Bulldozer extends GameElement {

	public static final int BULLDOZER_LAYER = 2;
	Direction direction = Direction.UP;
	private int points;
		
	public Bulldozer(Point2D position) {
		super("bulldozer_U", position, BULLDOZER_LAYER);
		points = 0;
	}

	public int getPoints(){
		return points;
	}

	public void addPoints(int i){
		points+=i;
	}
	
	public void move(Direction d) {
		direction = d;
		Point2D newPosition = getPosition().plus(d.asVector());
		if(StoneBreaker.inBounds(getPosition().plus(d.asVector())) && StoneBreaker.getInstance().isPassable(newPosition)) setPosition(newPosition);

		List<Breakable> breakables = StoneBreaker.getInstance().breakablesAt(newPosition);
		breakables.forEach(b -> b.brokenBy(this));
	}

	@Override
	public String getName(){
		return "bulldozer_" + direction.name().charAt(0);
	}
	
	@Override
	public boolean isTransposable() {
		return false;
	}
}
