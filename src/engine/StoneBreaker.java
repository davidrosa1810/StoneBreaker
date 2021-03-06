package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import objects.AutoBulldozer;
import objects.Breakable;
import objects.Bulldozer;
import objects.GameElement;
import gui.*;
import observer.*;
import utils.Point2D;
import utils.Direction;

/* Classe com o motor de jogo - e' um solitao. 
 * 
 * Contem metodos que podem ser uteis:
 * - addObject(...)
 * - removeObject(...)
 * - breakablesAt(...)
 * - isPassable(...)
 * 
 * Não é suposto alterar.
 */

public class StoneBreaker implements Observer {

	// Dimensoes da area de jogo
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;

	// Carateres utilizados no ficheiro
	public static final char CHAO = '_';
	public static final char PEDRA_PEQUENA = 'p';
	public static final char PEDRA_GRANDE = 'P';
	public static final char ARVORE = '#';
	public static final char BULLDOZER = 'B';
	public static final char AUTOBULLDOZER = 'A';

	// Instancia de StoneBreaker - e' um solitao
	private static StoneBreaker INSTANCE = null;

	// Lista de elementos de jogo - contem todos excepto os do tipo Chao
	private List<GameElement> gameObjects = new ArrayList<>();
	
	// Numero do nivel de jogo
	private int level = 0;
	
	// Construtor
	private StoneBreaker() {
		readLevel();
		ImageMatrixGUI.getInstance().update();
		ImageMatrixGUI.getInstance().setStatusMessage("Pontuação do Jogador: " + getPlayerPoints() + "       Pontuação do Computador: " + getAutoPoints());
	}

	

	// Devolve uma instancia unica de StoneBreaker 
	public static StoneBreaker getInstance() {
		if (INSTANCE == null)
			INSTANCE = new StoneBreaker();
		return INSTANCE;
	}

	// Invocado sempre que se carrega numa tecla
	@Override
	public void update(Observed arg0) {

		int lastKeyPressed = ((ImageMatrixGUI) arg0).keyPressed();
		
		// Desencadeia o movimento dos bulldozers - nos automaticos a direcao sera' 
		// ignorada e substituida por uma direcao aleatoria
		if (Direction.isDirection(lastKeyPressed)) {
			getBulldozers().forEach(b -> b.move(Direction.directionFor(lastKeyPressed)));
		}
		
		// Verificar se terminou - se sim, apaga a janela
		if (isFinished()){
			String str;
			if(getPlayerPoints() > getAutoPoints()) str = "O Jogador ganhou";
			else if(getPlayerPoints() == getAutoPoints()) str = "O jogo ficou empatado";
			else str = "O computador ganhou";
			ImageMatrixGUI.getInstance().setMessage(str);	
			ImageMatrixGUI.getInstance().dispose();
		}

		ImageMatrixGUI.getInstance().setStatusMessage("Pontuação do Jogador: " + getPlayerPoints() + "       Pontuação do Computador: " + getAutoPoints());
		ImageMatrixGUI.getInstance().update();
	}

	// Insere um novo objeto no jogo
	public void addObject(GameElement obj) {
		gameObjects.add(obj);
		ImageMatrixGUI.getInstance().addImage((ImageTile) obj);
	}

	// Remove um objeto do jogo
	public void removeObject(GameElement obj) {
		gameObjects.remove(obj);
		ImageMatrixGUI.getInstance().removeImage((ImageTile) obj);
	}
	
	// Devolve os objetos Breakable que estao na posicao p
	public List<Breakable> breakablesAt(Point2D p) {
		return select(o -> o.getPosition().equals(p) && o instanceof Breakable);
	}
	
	// Devolve true ou false consoante uma posicao p esteja livre ou ocupada por um objeto "nao atravessavel" 
	public boolean isPassable(Point2D p) {
		List<GameElement> nonTransposablesAt = select(o -> o.getPosition().equals(p) && !o.isTransposable());
		return nonTransposablesAt.size() == 0;
	}
	
	// Verifica se uma posicao esta' ou nao dentro dos limites da area de jogo 
	public static boolean inBounds(Point2D p) {
		if (p.getX() < 0 || p.getY() < 0 || p.getX() >= StoneBreaker.WIDTH
			|| p.getY() >= StoneBreaker.HEIGHT)
			return false;
		return true;
	}
	
	
	
	// Metodos private
	
	// Seleciona elementos de jogo com base num Predicate
	@SuppressWarnings("unchecked")
	private <T> List<T> select(Predicate<GameElement> p) {
		List<T> selection = new ArrayList<>();
		for (GameElement obj : gameObjects)
			if (p.test(obj))
				selection.add((T) obj);
		return selection;		
	}
	
	// Devolve os Bulldozers
	private List<Bulldozer> getBulldozers() {
		return select(obj -> obj instanceof Bulldozer);
	}	
	
	// Devolve os Breakable
	private List<Breakable> getBreakables() {
		return select(obj -> obj instanceof Breakable);
	}
	
	// Le um nivel do jogo
	private void readLevel() {

		try (Scanner s = new Scanner(new File("levels/level" + level + ".txt"))) {
			
			int y = 0;
			while (s.hasNextLine()) {
				String line = s.nextLine();
				for (int x = 0; x != line.length(); x++) {
					ImageMatrixGUI.getInstance().addImage((ImageTile) GameElement.create(CHAO, x, y));
					char c = line.charAt(x);
					if (c!=CHAO) 
						addObject(GameElement.create(c, x, y));
				}
				y++;
			}
			
		} catch (FileNotFoundException e) {
			throw new IllegalStateException();
		}
	}

	// Verifica se o jogo chegou ao fim
	private boolean isFinished() {
		return getBreakables().size() == 0;
	}

	private int getPlayerPoints(){
		List<Bulldozer> bulls = getBulldozers();
		for(Bulldozer b : bulls){
			if(!(b instanceof AutoBulldozer))return b.getPoints();
		}
		return 0;
	}

	private int getAutoPoints() {
		List<Bulldozer> bulls = getBulldozers();
		if(bulls.size() != 0) return bulls.get(0).getPoints();
		return 0;
	}
}
