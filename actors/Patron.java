/* This is the source file for the Patron class.  Loosely speaking,
 * a patron's job is to appear at the top of the screen, descend towards
 * the tank at the bottom, interact with the tank, and then exit.
 * The player's job is to intercept patrons before they reach the tank.
 * 
 * Patron behavior is described by the Phase enumeration.  A DESCENDING
 * patron moves downward if there is no other patron waiting at the
 * tank directly below her and laterally towards the nearest unoccupied
 * column otherwise.  A WAITING patron is interacting with the tank.  At
 * present, the patron waits for a certain number of turns.  (Later, the
 * patron will wait for the dogfish to pass by a certain number of
 * times.)  An EXITING patron is moving toward the nearest vertical edge
 * of the screen and an OFFSTAGE patron has reached the edge and has
 * temporarily been removed from the scenegraph.
 * 
 * Patron interaction with the tank is described by the State
 * enumeration.  When patrons are added to the stage they are in the
 * DEFAULT state.  A patron who is intercepted by the player before she
 * reaches the tank is in the INTERCEPTED state.  A patron who reaches
 * the tank in the DEFAULT state has a random chance each turn of
 * transitioning to the TAPPING state.  (Later, each turn the patron
 * spends in the TAPPING state will cost the player valuable points.)  */

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
	
	private static boolean[]             emptyColumn;
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
		phase = Phase.OFFSTAGE;
		state = State.DEFAULT;
		elapsedTime = 0f;
		turnCounter = 0;
		if (rng == null) rng = new RandomXS128();
		setGridPosition(GRID_ROWS - 1, rng.nextInt(GRID_COLUMNS));
		if (emptyColumn == null) {
			emptyColumn = new boolean[GRID_COLUMNS];
			for (int i = 0; i < emptyColumn.length; i++) emptyColumn[i] = true;
		}
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
			if (isAdjacentTo(nateRow,nateColumn)) {
				state = State.INTERCEPTED;
				setColor(State.INTERCEPTED.color());
				events.add(new ScoreEvent(100));
			}
		}
		
		/* If the patron isn't EXITING and is in the DEFAULT state,
		 * check to see if the patron is next to Matthew.  If so,
		 * change the patron's state to TAPPING. */
		
		if (phase != Phase.EXITING && state == State.DEFAULT) {
			if (isAdjacentTo(matthewRow,matthewColumn)) {
				state = State.TAPPING;
				setColor(State.TAPPING.color());
			}
		}
		
		/* If the patron is WAITING, check to see if the patron is next 
		 * to the dogfish.  If so, change the patron's state to 
		 * EXITING. */
		 
		if (phase == Phase.WAITING) {
			if (isAdjacentTo(fishRow,fishColumn)) {
				phase = Phase.EXITING;
				Color c = getColor();
				setColor(c.r, c.g, c.b, 0.5f*c.a);
				emptyColumn[getColumn()] = true;
			}			
		}
		
	}
	
	public void descend() {

		int r = getRow();
		int c = getColumn();
		boolean blocked = matthewRow == r - 1 && matthewColumn == c;
		if (!blocked) blocked = nateRow == r - 1 && nateColumn == c;
		if (emptyColumn[c] && !blocked) {
			
			/* If the patron is in a column where no other patrons are
			 * waiting and is not directly above Nate or Matthew, then 
			 * the patron should move down a row.  If the patron reaches
			 * the bottom row, they should transition to the waiting
			 * phase. */
			 
			moveDown();
			if (getRow() == EXIT_ROW) {
				phase = Phase.WAITING;
				emptyColumn[c] = false;
			}
			
		} else {
			
			/* If there is another patron waiting in this column or the
			 * patron is directly above Nate or Matthew, then the patron
			 * should move in the direction of the closest column
			 * without a waiting patron. */
			 
			int left = -1;
			for (int i = c - 1; i >= 0; i--) {
				if (emptyColumn[i]) {
					left = i;
					break;
				}
			}
			int right = -1;
			for (int i = c + 1; i < GRID_COLUMNS; i++) {
				if (emptyColumn[i]) {
					right = i;
					break;
				}
			}
			if (left == -1 && right == -1) {
				System.out.println("Something has gone horribly wrong.");
				remove();
			} else if (left == -1 && right != -1) {
				moveRight();
			} else if (left != -1 && right == -1) {
				moveLeft();
			} else {
				if (c - left > right - c) {
					moveRight();
				} else if (c - left > right - c) {
					moveLeft();
				} else {
					switch (rng.nextInt(1)) {
						case 0:  moveLeft();  break;
						default: moveRight(); break;
					}
				}
			}
		}
		
	}
	
	public void exit() {
	
		/* An exiting patron moves toward the left or right side of the
		 * screen, whichever is closer.  A patron who has left the grid
		 * should be removed from the stage. */
		
		int c = getColumn();
		if (c == 0 || c == GRID_COLUMNS - 1) {
			remove();
			phase = Phase.OFFSTAGE;
		} else if (c - 1 < GRID_COLUMNS - c) {
			moveLeft();
		} else {
			moveRight();
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
	
	public boolean isOffstage() {return phase == Phase.OFFSTAGE;}

	public void reset() {
		
		setGridPosition(GRID_ROWS - 1, rng.nextInt(GRID_COLUMNS));
		setColor(Color.WHITE);
		phase = Phase.DESCENDING;
		state = State.DEFAULT;
		elapsedTime = 0f;
		turnCounter = 0;
		
	}

	public static void setFishPosition(TextureActor fish) {
		
		fishRow = fish.getRow();
		fishColumn = fish.getColumn();
		
	}

	public static void setMatthewPosition(TextureActor matthew) {
		
		matthewRow = matthew.getRow();
		matthewColumn = matthew.getColumn();
		
	}

	public static void setNatePosition(TextureActor nate) {
		
		nateRow = nate.getRow();
		nateColumn = nate.getColumn();
		
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
					setColor(State.TAPPING.color());
				}
				break;
				
		}
		
	}

}
