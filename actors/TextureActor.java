package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import john.aquariumassault.Constants;

public class TextureActor extends Actor implements Constants {
	
	// Fields

	public static final int   DOWN    = 0;
	public static final int   LEFT    = 1;
	public static final int   RIGHT   = 2;
	public static final int   UP      = 3;
	public static final int[] ROW_ADJ = {-1,0,0,1};
	public static final int[] COL_ADJ = {0,-1,1,0};

	private static int[][] map;

	private Texture   texture;
	private int       id, row, column, lastRow, lastColumn;
	private int[]     boundary;
	private boolean   onMap;
	
	// Constructor(s)
	
	public TextureActor(Texture texture) {
		
		super();
		this.texture = texture;
		setSize(GRID_STEP,GRID_STEP);
		id = NONE;
		row = 0;
		column = 0;
		lastRow = 0;
		lastColumn = 0;
		if (map == null) {
			map = new int[GRID_ROWS + 1][GRID_COLUMNS];
			clearMap();
		}
		boundary = new int[4];
		boundary[DOWN] = 0;
		boundary[LEFT] = 0;
		boundary[RIGHT] = GRID_COLUMNS - 1;
		boundary[UP] = GRID_ROWS - 1;
		onMap = false;
		
	}
	
	// Methods
	
	public boolean addToMap() {

		/* Attempts to add the actor to the collision map.  Returns true
		 * if successful and false otherwise.  Will return false if
		 * the actor's row or column is outside the actor's boundaries
		 * or if there is already an actor on the map in the same
		 * square as this actor. */

		boolean validRow = row >= boundary[LEFT] && row <= boundary[RIGHT];
		boolean validColumn = column >= boundary[DOWN] && column <= boundary[UP];
		boolean output = validRow && validColumn;
		if (output) {
			output = map[row][column] == NONE;
			if (output) map[row][column] = id;
		}
		return output;
		
	}
	
	public static void clearMap() {
		
		/* Fills every square on the map with NONE. */
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = NONE;
			}
		}
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a*parentAlpha);
		batch.draw(texture,getX(),getY(),getWidth(),getHeight());
		batch.setColor(Color.WHITE);
		
	}
	
	public int getBoundary(int direction) {return boundary[direction];}

	public int getColumn() {return column;}

	public static int getID(int row, int column) {return map[row][column];}

	public int getLastColumn() {return lastColumn;}
	
	public int getLastRow() {return lastRow;}
	
	public int getRow() {return row;}
	
	public Texture getTexture() {return texture;}

	public boolean isAdjacentTo(TextureActor actor) {
		
		int dx = this.column - actor.getColumn();
		int dy = this.row - actor.getRow();
		return dx*dx + dy*dy == 1;
		
	}

	public static boolean mapSquareIsEmpty(int row, int column) {
		
		return map[row][column] == NONE;
		
	}

	public boolean move(int direction) {
	
		/* Attempt to move the actor in the indicated direction.  Return
		 * true if the move executes successfully and false otherwise. 
		 * Return false if the direction argument is not valid. */
	
		boolean output = false;	
		switch (direction) {
			case DOWN:  output = row > boundary[DOWN];     break; // Can move down
			case LEFT:  output = column > boundary[LEFT];  break; // Can move left
			case RIGHT: output = column < boundary[RIGHT]; break; // Can move right
			case UP:    output = row < boundary[UP];       break; // Can move up
		}
		if (output) {
			int targetRow = row + ROW_ADJ[direction];       // Row index for target grid square
			int targetColumn = column + COL_ADJ[direction]; // Column index for target grid square
			output = map[targetRow][targetColumn] == NONE;  // Target grid square is empty
			if (output) {
				lastRow = row;
				lastColumn = column;
				row = targetRow;
				column = targetColumn;
				setPosition(GRID_STEP*column,GRID_STEP*row);
				if (onMap) {
					map[lastRow][lastColumn] = NONE;
					map[row][column] = id;
				}
			}
		}
		return output;
		
	}

	public boolean moveDown() {return move(DOWN);}
	
	public boolean moveLeft() {return move(LEFT);}
	
	public boolean moveRight() {return move(RIGHT);}
	
	public boolean moveUp() {return move(UP);}

	public boolean nextTo(int id) {
		
		/* Returns true if the actor is next to at least one other actor
		 * with the id specified in the argument. */
		
		boolean output = false;
		for (int i = DOWN; i <= UP; i++) {
			int tr = row + ROW_ADJ[i];
			int tc = column + COL_ADJ[i];
			boolean rowBound = tr >= 0 && tr < GRID_ROWS;
			boolean colBound = tc >= 0 && tc < GRID_COLUMNS;
			if (rowBound && colBound) {
				output = TextureActor.getID(tr,tc) == id;
				if (output) break;
			}
		}
		return output;
		
	}

	public static int[] position(int id) {
		
		/* Returns the position of the first square with the same id as
		 * the argument.  For use with NATE, MATTHEW, and DOGFISH.  
		 * Results for PATRON will be arbitrary. */
		
		int[] output = null;
		boolean match = false;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				match = map[i][j] == id;
				if (match) {
					output = new int[2];
					output[0] = i;
					output[1] = j;
					break;
				}
			}
			if (match) break;
		}
		return output;
		
	}
	
	public void removeFromMap() {
		
		if (onMap) map[row][column] = NONE;
		onMap = false;
		
	}
	
	public void setBoundary(int direction, int value) {boundary[direction] = value;}
	
	public boolean setGridPosition(int row, int column) {
		
		boolean validRow = row >= boundary[DOWN] && row <= boundary[UP];
		boolean validColumn = column >= boundary[LEFT] && column <= boundary[RIGHT];
		boolean output = validRow && validColumn;
		if (output) {
			output = TextureActor.mapSquareIsEmpty(row,column);
			if (output) {
				lastRow = this.row;
				lastColumn = this.column;
				this.row = row;
				this.column = column;
				setPosition(GRID_STEP*column,GRID_STEP*row);
				
				/* If the actor is on the map, update the character's
				 * location on the map.  (Actors who are not on
				 * the map are immune to collisions.) */
				
				if (onMap) {
					map[lastRow][lastColumn] = NONE;
					map[this.row][this.column] = id;
				}
				
			}
		}
		return output;
		
	}
	
	public void setID(int id) {this.id = id;}
	
	public void setLastColumn(int c) {lastColumn = c;}
	
	public void setLastRow(int r) {lastRow = r;}
	
	public void setTexture(Texture texture) {this.texture = texture;}
	
	public int textureHeight() {return texture.getHeight();}
	
	public int textureWidth() {return texture.getWidth();}
	
	public boolean[] validMoves() {
		
		boolean[] output = new boolean[4];
		output[DOWN] = row > boundary[DOWN];
		output[LEFT] = column > boundary[LEFT];
		output[RIGHT] = column < boundary[RIGHT];
		output[UP] = column < boundary[UP];
		for (int i = DOWN; i <= UP; i++) {
			if (output[i]) output[i] = TextureActor.mapSquareIsEmpty(row + ROW_ADJ[i], column + COL_ADJ[i]);
		}
		return output;
		
	}
	
}
