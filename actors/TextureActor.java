package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.RandomXS128;
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

	private static int[][]     map;
	private static RandomXS128 rng;

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
		if (rng == null) rng = new RandomXS128();
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
		
		onMap = map[row][column] == NONE;
		if (onMap) map[row][column] = id;
		return onMap;
		
	}
	
	public static void clearMap() {
		
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
	
	public int getRow() {return row;}
	
	public int getColumn() {return column;}
	
	public int getID() {return id;}
	
	public static int getID(int row, int column) {return map[row][column];}
	
	public int getLastRow() {return lastRow;}
	
	public int getLastColumn() {return lastColumn;}
	
	public Texture getTexture() {return texture;}

	public boolean isAdjacentTo(TextureActor actor) {
		
		int dx = this.column - actor.getColumn();
		int dy = this.row - actor.getRow();
		return dx*dx + dy*dy == 1;
		
	}

	public boolean isAdjacentTo(int row, int column) {

		int dx = this.column - column;		
		int dy = this.row - row;
		return dx*dx + dy*dy == 1;
		
	}

	public boolean isInSamePosition(TextureActor actor) {
		
		return row == actor.getRow() && column == actor.getColumn();
		
	}

	public boolean isOnMap() {return onMap;}

	public static boolean mapSquareIsEmpty(int row, int column) {
		
		return map[row][column] == NONE;
		
	}

	public boolean move(int direction) {
		
		/* Is the actor not at the relevant boundary? */
	
		boolean output = false;
		switch (direction) {
			case DOWN:  output = row > boundary[DOWN];     break;
			case LEFT:  output = column > boundary[LEFT];  break;
			case RIGHT: output = column < boundary[RIGHT]; break;
			case UP:    output = row < boundary[UP];       break;
		}
		if (output) {
			
			/* If so, is the actor's target square empty? */
			
			int targetRow = row + ROW_ADJ[direction];
			int targetColumn = column + COL_ADJ[direction];
			output = map[targetRow][targetColumn] == NONE;
			if (output) {
				
				/* If so, update the actor's position information. */
				
				lastRow = row;
				lastColumn = column;
				row = targetRow;
				column = targetColumn;
				setPosition(GRID_STEP*column,GRID_STEP*row);			
				
				/* If the actor is on the map, update the map with the
				 * actor's new position. */
				
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

	public static int[] position(int id) {
		
		/* Returns the position of the first square with the same id as
		 * the argument. */
		
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

	public static int[] natePosition() {return TextureActor.position(NATE);}
		
	public static int[] dogfishPosition() {return TextureActor.position(DOGFISH);}

	public boolean nextTo(int id) {
		
		boolean output = false;
		int r = getRow();
		int c = getColumn();
		for (int i = DOWN; i <= UP; i++) {
			int targetRow = r + ROW_ADJ[i];
			int targetCol = c + COL_ADJ[i];
			boolean rowBound = targetRow >= 0 && targetRow < GRID_ROWS;
			boolean colBound = targetCol >= 0 && targetCol < GRID_COLUMNS;
			if (rowBound && colBound) {
				output = TextureActor.getID(targetRow,targetCol) == id;
				if (output) break;
			}
		}
		return output;
		
	}
	
	public int randomIndex(boolean[] mask) {
		
		/* Given an array of booleans, this method returns either the
		 * index of a randomly-selected true element or -1 if there are
		 * no true elements.  This method will be used by the Patron
		 * and Matthew classes to select from among available moves. */
		 
		int output = -1;
		int counter = 0;
		for (int i = 0; i < mask.length; i++) {
			if (mask[i]) counter++;
		}
		if (counter > 0) {
			int random = 1 + rng.nextInt(counter);
			counter = 0;
			for (int i = 0; i < mask.length; i++) {
				if (mask[i]) counter++;
				if (counter == random) {
					output = i;
					break;
				}
			}
		}
		return output;
		
	}

	public float randomFloat() {return rng.nextFloat();}

	public int randomInt(int bound) {return rng.nextInt(bound);}
	
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
				
			} else {
				System.out.println("Could not move " + ID[id] + ".");
				System.out.println("Target square already contains: " + ID[TextureActor.getID(row,column)]);
			}
		} else {
			System.out.println("Move failed.");
			System.out.println("Valid target row:    " + validRow);
			System.out.println("Valid target column: " + validColumn);
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
