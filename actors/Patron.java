/* This is the source file for the Patron class.  Loosely speaking,
 * a patron's job is to appear at the top of the screen, descend towards
 * the tank at the bottom, interact with the tank, and then exit.
 * The player's job is to intercept patrons before they reach the tank.
 * 
 * Patron behavior is described by the Phase enumeration.  A DESCENDING
 * patron moves downward if there is no other patron waiting at the
 * tank directly below her and laterally towards the nearest unoccupied
 * column otherwise.  A WAITING patron is interacting with the tank.  An
 * EXITING patron is moving toward the nearest vertical edge of the 
 * screen and an OFFSTAGE patron has reached the edge and has 
 * temporarily been removed from the scenegraph.
 * 
 * Patron interaction with the tank is described by the State
 * enumeration.  When patrons are added to the stage they are in the
 * DEFAULT state.  A patron who is intercepted by the player before she
 * reaches the tank is in the INTERCEPTED state.  A patron who reaches
 * the tank in the DEFAULT state has a random chance each turn of
 * transitioning to the TAPPING state.  */

package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import john.aquariumassault.ScoreEvent;

public class Patron extends TextureActor {

	// Enumerations
	
	enum Phase {DESCENDING, WAITING, EXITING, OFFSTAGE};
	
	enum State {
		
		DEFAULT      (Color.WHITE),
		INTERCEPTED  (Color.GREEN),
		TAPPING      (Color.RED);
		
		private final Color color;
		
		State(Color color) {this.color = color;}
		
		private Color color() {return color;}
		
	}

	// Fields
	
	private static float                 turnLength = 0.5f; // Two turns per second
	private static int                   turnWait = 4;      // Number of turns to spend in the waiting phase
	private static float                 tapProb = 0.125f;  // Chance per turn of transitioning from DEFAULT to TAPPING
	private static RandomXS128           rng;
	private static int                   matthewRow, matthewColumn, nateRow, nateColumn, fishRow, fishColumn;
	private        Phase                 phase;
	private        State                 state;
	private        float                 elapsedTime;
	private        int                   turnCounter;
	private        ArrayList<ScoreEvent> events;
	
	// Constructor(s)
	
	public Patron(Texture texture) {
		
		super(texture);
		setID(ALICE);
		setBoundary(DOWN,EXIT_ROW);
		phase = Phase.OFFSTAGE;
		state = State.DEFAULT;
		elapsedTime = 0f;
		turnCounter = 0;
		if (rng == null) rng = new RandomXS128();
		setGridPosition(GRID_ROWS - 1, rng.nextInt(GRID_COLUMNS));
		events = new ArrayList<ScoreEvent>();
		
	}
	
	// Methods
	
	@Override
	public void act(float delta) {
		
		/* At the start of a new turn, act. */
		
		elapsedTime += delta;
		if (elapsedTime > turnLength) {
			switch (phase) {
				case DESCENDING: descend();    break;
				case WAITING:    waitByTank(); break;
				case EXITING:    exit();       break;
			}
			elapsedTime = 0f;
		}
		
		/* If the patron isn't EXITING and hasn't been INTERCEPTED yet,
		 * check to see if the Patron is next to Nate.  If so, change
		 * the patron's state to INTERCEPTED. */
		 
		if (phase != Phase.EXITING && state != State.INTERCEPTED) {
			if (nextTo(NATE)) {
				state = State.INTERCEPTED;
				setColor(State.INTERCEPTED.color());
				events.add(new ScoreEvent(100));
			}
		}
		
		/* If the patron isn't EXITING and is in the DEFAULT state,
		 * check to see if the patron is next to Matthew.  If so,
		 * change the patron's state to TAPPING. */
		
		if (phase != Phase.EXITING && state == State.DEFAULT) {
			if (nextTo(MATTHEW)) {
				state = State.TAPPING;
				setColor(State.TAPPING.color());
			}
		}
		
		/* If the patron is WAITING, check to see if the patron is next 
		 * to the dogfish.  If so, change the patron's state to 
		 * EXITING. */
		 
		if (phase == Phase.WAITING) {
			if (nextTo(DOGFISH)) {
				phase = Phase.EXITING;
				Color c = getColor();
				setColor(c.r, c.g, c.b, 0.5f*c.a);
			}			
		}
		
	}
	
	public void descend() {

		/* As a general rule, a DESCENDING patron moves down until they
		 * reach the fishtank (i.e., are in the exit row).  If there
		 * is another actor in the square below them, then they
		 * will attempt to move to the side, preferably in the direction
		 * of the nearest empty column in the exit row.  If they cannot 
		 * move to the side, then they will remain stationary.  A patron
		 * who reaches the exit row should transition to the WAITING
		 * phase.*/

		if (moveDown()) {
			if (getRow() == EXIT_ROW) phase = Phase.WAITING;
		} else {
			int r = getRow();
			int c = getColumn();
			int left = GRID_COLUMNS;
			for (int i = c - 1; i >= 0; i++) {
				if (TextureActor.mapSquareIsEmpty(EXIT_ROW,i)) {
					left = i - c;
					break;
				}
			}
			int right = GRID_COLUMNS;
			for (int i = c + 1; i < GRID_COLUMNS; i++) {
				if (TextureActor.mapSquareIsEmpty(EXIT_ROW,i)) {
					right = c - i;
					break;
				}
			}
			if (left < right) {
				if (!moveLeft()) moveRight();
			} else if (left > right) {
				if (!moveRight()) moveLeft();
			} else {
				switch (rng.nextInt(2)) {
					case 1:  if (!moveRight()) moveLeft(); break;
					default: if (!moveLeft()) moveRight(); break;
				}
			}
		}
		
	}
	
	public void exit() {
	
		/* An exiting patron moves toward the left or right side of the
		 * screen, whichever is closer.  If the patron is blocked, then
		 * she may move up or down, if possible, in order to try to
		 * go around the blockage.  A patron who has left the grid
		 * should be removed from the stage. */
		
		int r = getRow();
		int c = getColumn();
		if (c == getBoundary(LEFT) || c == getBoundary(RIGHT)) {
			remove();
			removeFromMap();
			phase = Phase.OFFSTAGE;
		} else {
			int dl = c - getBoundary(LEFT);
			int dr = getBoundary(RIGHT) - c;
			boolean sidestep = false;
			if (dl < dr) {
				sidestep = !TextureActor.mapSquareIsEmpty(r,c-1);
				if (!sidestep) moveLeft();
			} else if (dl > dr) {
				sidestep = !TextureActor.mapSquareIsEmpty(r,c+1);
				if (!sidestep) moveRight();
			} else {
				boolean left = TextureActor.mapSquareIsEmpty(r,c-1);
				boolean right = TextureActor.mapSquareIsEmpty(r,c+1);
				if (left && ! right) {
					moveLeft();
				} else if (!left && right) {
					moveRight();
				} else if (left && right) {
					switch (rng.nextInt(2)) {
						case 1:  moveRight(); break;
						default: moveLeft();  break;
					}
				} else {
					sidestep = true;
				}
			}
			if (sidestep) {
				boolean up = r < getBoundary(UP);
				if (up) up = TextureActor.mapSquareIsEmpty(r+1,c);
				boolean down = r > getBoundary(DOWN);
				if (down) down = TextureActor.mapSquareIsEmpty(r-1,c);
				if (up && !down) {
					moveUp();
				} else if (!up && down) {
					moveDown();
				} else if (up && down) {
					switch (rng.nextInt(2)) {
						case 1:  moveDown(); break;
						default: moveUp();   break;
					}
				}
			}
			
		}
		
	}

	public int incrementScore() {
		
		/* This method returns the total of the points associated with 
		 * each ScoreEvent the patron has accumulated and removes
		 * all ScoreEvents from the events array. */
		
		int total = 0;
		if (events.size() > 0) {
			for (int i = events.size() - 1; i >= 0; i--) {
				total += events.get(i).points();
				events.remove(i);
			}
		}
		return total;
		
	}
	
	public boolean isDefault() {return state == State.DEFAULT;}
	
	public boolean isDescending() {return phase == Phase.DESCENDING;}
	
	public boolean isOffstage() {return phase == Phase.OFFSTAGE;}

	public void reset() {
		
		/* Set the Patron's position, taking care not to spawn in the
		 * same space as another actor. */
		
		boolean[] spawnable = new boolean[GRID_COLUMNS];
		int counter = 0;
		for (int i = 0; i < GRID_COLUMNS; i++) {
			spawnable[i] = TextureActor.mapSquareIsEmpty(GRID_ROWS - 1, i);
			if (spawnable[i]) counter++;
		}
		int random = rng.nextInt(counter);
		int index = -1;
		for (int i = 0; i < spawnable.length; i++) {
			if (spawnable[i]) {
				if (++index == random) break;
			}
		}
		setGridPosition(GRID_ROWS - 1, index);
		
		/* Set the Patron's other properties. */
		
		phase = Phase.DESCENDING;
		state = State.DEFAULT;
		setColor(state.color());
		elapsedTime = 0f;
		turnCounter = 0;
		addToMap();
		
	}

	public void waitByTank() {
		
		switch (state) {
			
			case TAPPING:
			
				/* Add a new score event. */
				
				events.add(new ScoreEvent(-200));
				break;
				
			case DEFAULT:
			
				/* Chance of transitioning to TAPPING state. */
				
				if (rng.nextFloat() < tapProb) {
					state = State.TAPPING;
					setColor(state.color());
				}
				break;
				
		}
		
	}

}
