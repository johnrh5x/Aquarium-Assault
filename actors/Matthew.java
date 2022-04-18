package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.RandomXS128;

public class Matthew extends TextureActor {

	// Enumerations
	
	

	// Fields
	
	private RandomXS128 rng;
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
		rng = new RandomXS128();
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
				index = escape();
			} else {
				index = chaseFish();
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
		 
		int output = -1;
		
		// Future fish position
		
		int[] fishPosition = TextureActor.position(DOGFISH);
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
		
		// Choose randomly from among the desirable moves, if any
		
		int moves = 0;
		for (int i = 0; i < closer.length; i++) {
			if (closer[i]) moves++;
		}
		if (moves > 0) {
			int random = rng.nextInt(moves);
			int counter = 0;
			for (int i = 0; i < closer.length; i++) {
			if (closer[i]) {
					if (counter == random) {
						output = i;
						break;
					}
					counter++;
				}
			}
		}
		
		// Return output
		
		return output;
		
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
			
			/* Pick one of those moves at random. */

			int moves = 0;
			for (int i = 0; i < closer.length; i++) {
				if (closer[i]) moves++;
			}
			if (moves > 0) {
				int random = rng.nextInt(moves);
				int counter = 0;
				for (int i = 0; i < closer.length; i++) {
					if (closer[i]) {
						if (counter == random) {
							output = i;
							break;
						}
						counter++;
					}
				}
			}
			
		}
		return output;
		
	}
	
	private int distanceTo(int row, int column) {
		
		/* This method returns the distance from Matthew's position to
		 * the row and column in the arguments. */
		 
		int dx = getRow() - row;
		int dy = getColumn() - column;
		if (dx < 0f) dx *= -1;
		if (dy < 0f) dy *= -1;
		return dx + dy;
		
	}
	
	private int escape() {
		
		/* This method returns the index of the move that best allows
		 * Matthew to flee with the dogfish.  Matthew escapes when he
		 * reaches the top of the screen, so he generally wants to move
		 * up, but if Nate is close and above him, he may move to the
		 * side. */
		 
		int output = -1;
		int r = getRow();
		int c = getColumn();
		int[] natePosition = TextureActor.position(NATE);
		int nateRow = natePosition[0];
		int nateColumn = natePosition[1];
		boolean[] canMove = validMoves();
		if (canMove[UP]) output = UP;
		if (distanceTo(nateRow,nateColumn) < 4 && nateRow > getRow()) {
			if (nateColumn < c && canMove[RIGHT]) {
				output = RIGHT;
			} else if (nateColumn > c && canMove[LEFT]) {
				output = LEFT;
			} else if (nateColumn == c) {
				if (canMove[LEFT] && !canMove[RIGHT]) {
					output = LEFT;
				} else if (!canMove[LEFT] && canMove[RIGHT]) {
					output = RIGHT;
				} else if (canMove[LEFT] && canMove[RIGHT]) {
					switch (rng.nextInt(2)) {
						case 0: output = LEFT;  break;
						case 1: output = RIGHT; break;
					}
				}
			}
		}
		return output;
		
	}
	
	private int fleeNate() {
		
		int r = getRow();
		int c = getColumn();
		
		/* Determine which directions would put Matthew further from 
		 * Nate. */
		
		int[] natePosition = TextureActor.position(NATE);
		int nateRow = natePosition[0];
		int nateColumn = natePosition[1];
		boolean[] canMove = validMoves();
		boolean[] farther = new boolean[4];
		farther[DOWN]  = r <= nateRow    && canMove[DOWN];
		farther[LEFT]  = c <= nateColumn && canMove[LEFT];
		farther[RIGHT] = c >= nateColumn && canMove[RIGHT];
		farther[UP]    = r >= nateRow    && canMove[UP];
		
		/* Choose one of those directions at random. */
		
		int output = -1;
		int moves = 0;
		for (int i = 0; i < farther.length; i++) {
			if (farther[i]) moves++;
		}
		if (moves > 0) {
			int random = rng.nextInt(moves);
			int counter = 0;
			for (int i = 0; i < farther.length; i++) {
				if (farther[i]) {
					if (counter == random) {
						output = i;
						break;
					}
					counter++;
				}
			}
		}
		
		// Return output
		
		return output;
		
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
		
		int output = -1;
				
		// Count the number of valid moves
		
		boolean[] canMove = validMoves();
		int moves = 0;
		for (int i = 0; i < canMove.length; i++) {
			if (canMove[i]) moves++;
		}
		
		// If there are valid moves, pick one at random
		
		if (moves > 0) {
			int random = rng.nextInt(moves);
			int counter = 0;
			for (int i = 0; i < canMove.length; i++) {
				if (canMove[i]) {
					if (counter == random) {
						output = i;
						break;
					}
					counter++;
				}
			}			
		}
		
		// Return the result
		
		return output;
		
	}

	public void setEscaping(boolean b) {escaping = b;}

}
