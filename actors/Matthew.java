package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.RandomXS128;

public class Matthew extends TextureActor {

	// Enumerations
	
	

	// Fields
	
	private static final boolean[] LATERAL_MASK  = {false, true, true, false};
	private static final boolean[] VERTICAL_MASK = {true, false, false, true};
	private static final int       THRESHOLD     = (GRID_ROWS - EXIT_ROW)/2;
	
	private float       elapsedTime;
	private float       turnLength = 0.5f; // Two turns per second
	private int[]       nearestDefaultPatron;
	private boolean     escaping;
	
	// Constructor
	
	public Matthew(Texture texture) {
		
		// Declarations & initialization
		
		super(texture);
		setID(MATTHEW);
		setBoundary(DOWN,EXIT_ROW);
		elapsedTime = 0f;
		nearestDefaultPatron = new int[2];
		setGridPosition(GRID_ROWS - 2, GRID_COLUMNS/2);
		escaping = false;
 		
	}
	
	// Methods
	
	@Override
	public void act(float delta) {
		
		elapsedTime += delta;
		if (elapsedTime > turnLength) {
			int index = -1;
			if (escaping) {
				index = escape();                     // Run for it
			} else if (getRow() > THRESHOLD) {
				index = chasePatron();                // If high up, incite a patron to tap the tank
				if (index == -1) index = chaseFish(); // If there are no patrons to incite, go for the fish
			} else {
				index = chaseFish();                  // If low down, go for the fish
			}
			if (index != -1) move(index);
			elapsedTime = 0f;
		}

	}

	private int chaseFish() {
		
		/* This method returns the index of the move that puts Matthew
		 * closest to grid square where the dogfish will be on its next 
		 * turn or -1 if there is no valid move that would put him 
		 * closer to the dogfish. */
		
		// Predict the fish's next position
		
		int[] fishPosition = TextureActor.dogfishPosition();
		int nextRow = fishPosition[0];
		int nextColumn = fishPosition[1];
		if (fishPosition[0] == 0 && fishPosition[1] > 0) {
			nextRow--;
		} else if (fishPosition[1] == GRID_COLUMNS - 1 && fishPosition[0] < EXIT_ROW - 1) {
			nextRow++;
		} else if (fishPosition[0] == 0) {
			nextColumn++;
		} else if (fishPosition[0] == EXIT_ROW - 1) {
			nextColumn--;
		} else {
			System.out.println("Something has gone horribly wrong in Matthew's fish-prediction code.");
		}
		
		// Identify desirable moves
		
		int dy = getRow() - nextRow;
		int dx = getColumn() - nextColumn;
		boolean[] canMove = validMoves();
		boolean[] closer = new boolean[4];
		closer[DOWN]  = dy > 0 && canMove[DOWN];
		closer[LEFT]  = dx > 0 && canMove[LEFT];
		closer[RIGHT] = dx < 0 && canMove[RIGHT];
		closer[UP]    = dy < 0 && canMove[UP];
		
		// If Matthew can go down, go down; otherwise go left or right
		
		if (closer[DOWN]) {
			return DOWN;
		} else if (closer[LEFT]) {
			return LEFT;
		} else if (closer[RIGHT]) {
			return RIGHT;
		} else {
			return -1;
		}
		
	}
	
	private int chasePatron() {
		
		/* This method returns the index of the move that puts Matthew
		 * closest to the nearest patron in the DEFAULT state or -1 if
		 * there is no such patron or no valid move that puts Matthew
		 * closer to said patron. */
		
		int output = -1;
		if (nearestDefaultPatron[0] != -1 && nearestDefaultPatron[1] != -1) {
			
			/* Determine which moves get Matthew closer to the patron. */
			
			int dy = getRow() - nearestDefaultPatron[0];
			int dx = getColumn() - nearestDefaultPatron[1];
			boolean[] canMove = validMoves();
			boolean[] closer = new boolean[4];
			closer[DOWN]  = dy > 0 && canMove[DOWN];
			closer[LEFT]  = dx > 0 && canMove[LEFT];
			closer[RIGHT] = dx < 0 && canMove[RIGHT];
			closer[UP]    = dy < 0 && canMove[UP];
			
			/* Pick a direction, with preference to left or right over
			 * up. */

			if (closer[LEFT]) {
				output = LEFT;
			} else if (closer[RIGHT]) {
				output = RIGHT;
			} else {
				output = randomIndex(closer);
			}
			
		}
		return output;
		
	}
	
	private int distanceTo(int row, int column) {
		
		/* This method returns the distance from Matthew's position to
		 * the row and column in the arguments. */
		 
		int dy = getRow() - row;
		int dx = getColumn() - column;
		if (dy < 0f) dy *= -1;
		if (dx < 0f) dx *= -1;
		return dx + dy;
		
	}
	
	private int escape() {
		
		/* This method returns the index of the move that best allows
		 * Matthew to flee with the dogfish.  Matthew escapes when he
		 * reaches the top of the screen, so he generally wants to move
		 * up, but if Nate is close and above him, he may move to the
		 * side. */

		// Get positions for Nate and Matthew

		int[] natePosition = TextureActor.natePosition();
		int nr = natePosition[0]; // Nate row
		int nc = natePosition[1]; // Nate column
		int mr = getRow();        // Matthew row
		int mc = getColumn();     // Matthew column
		
		// Determine which moves are possible
		
		boolean[] canMove = validMoves();
		
		// Determine which moves are a good idea
		
		int output = -1;		
		if (distanceTo(nr,nc) < 4) {
			
			/* If Nate is close, try to avoid him. */
			
			output = fleeNate();
			
		} else {
			
			/* If Nate is not close, try to move up.  If it is not
			 * possible to move up, try moving to the side. */
			
			if (canMove[UP]) {
				output = UP;
			} else {
				canMove[DOWN] = false;
				output = randomIndex(canMove);
			}
			
		}
		
		return output;
		
	}
	
	private int fleeNate() {
		
		int r = getRow();
		int c = getColumn();
		
		/* Determine which directions would put Matthew further from 
		 * Nate. */
		
		int[] natePosition = TextureActor.natePosition();
		int nateRow = natePosition[0];
		int nateColumn = natePosition[1];
		boolean[] canMove = validMoves();
		boolean[] farther = new boolean[4];
		farther[DOWN]  = r <= nateRow    && canMove[DOWN];
		farther[LEFT]  = c <= nateColumn && canMove[LEFT];
		farther[RIGHT] = c >= nateColumn && canMove[RIGHT];
		farther[UP]    = r >= nateRow    && canMove[UP];
		
		/* Choose one of those directions at random. */
		
		return randomIndex(farther);
		
	}
	
	public void getNearestDefaultPatron(Patron[] patrons) {
		
		int r = getRow();
		int c = getColumn();
		nearestDefaultPatron[0] = -1;
		nearestDefaultPatron[1] = -1;
		int minDistance = GRID_ROWS + GRID_COLUMNS;
		for (Patron p: patrons) {
			if (!p.isOffstage() && p.isDefault()) {
				int pr = p.getRow();
				int pc = p.getColumn();
				int d = distanceTo(pr,pc);
				if (d < minDistance) {
					minDistance = d;
					nearestDefaultPatron[0] = pr;
					nearestDefaultPatron[1] = pc;
				}
			}
		}
		
	}
	
	public boolean isEscaping() {return escaping;}
	
	private int randomMove() {
		
		/* This method returns the index of a valid move or -1 if there
		 * are no valid moves. */
		
		boolean[] canMove = validMoves();
		return randomIndex(canMove);
		
	}

	public void setEscaping(boolean b) {escaping = b;}

}
