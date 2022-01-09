package objects;

import java.util.List;

import engine.StoneBreaker;
import utils.Direction;
import utils.Point2D;

//Se necessario, mudar o construtor
//
public class AutoBulldozer extends Bulldozer {

	private static int teamPoints;

	public AutoBulldozer(Point2D position) {
		super(position);
		teamPoints = 0;
	}

	public int getPoints() {
		return teamPoints;
	}

	public void addPoints(int i) {
		super.addPoints(i);
		teamPoints += i;
	}

	@Override
	public void move(Direction d){
		Direction dir = Direction.random();
		Point2D newPosition = getPosition().plus(dir.asVector());
		if(StoneBreaker.inBounds(getPosition().plus(dir.asVector())) && StoneBreaker.getInstance().isPassable(newPosition)) setPosition(newPosition);

		List<Breakable> breakables = StoneBreaker.getInstance().breakablesAt(newPosition);
		breakables.forEach(b -> b.brokenBy(this));

	}
	
	@Override
	public String getName(){
		return "autobulldozer_"+direction.name().charAt(0);
	}
}
