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

	private Texture   texture;
	private int       row, column, lastRow, lastColumn;
	
	// Constructor(s)
	
	public TextureActor(Texture texture) {
		
		super();
		this.texture = texture;
		setSize(GRID_STEP,GRID_STEP);
		row = 0;
		column = 0;
		lastRow = 0;
		lastColumn = 0;
		
	}
	
	// Methods
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a*parentAlpha);
		batch.draw(texture,getX(),getY(),getWidth(),getHeight());
		batch.setColor(Color.WHITE);
		
	}
	
	public int getRow() {return row;}
	
	public int getColumn() {return column;}
	
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

	public void moveDown() {
		
		if (row > 0) {
			lastRow = row;
			row--;
			setY(GRID_STEP*row);
		}
		
	}
	
	public void moveLeft() {
		
		if (column > 0) {
			lastColumn = column;
			column--;
			setX(GRID_STEP*column);
		}
		
	}
	
	public void moveRight() {
	
		if (column < GRID_COLUMNS - 1) {
			lastColumn = column;
			column++;
			setX(GRID_STEP*column);
		}
		
	}
	
	public void moveUp() {
		
		if (row < GRID_ROWS - 1) {
			lastRow = row;
			row++;
			setY(GRID_STEP*row);
		}
		
	}
	
	public void reportPosition() {
		
		System.out.println("row    = " + getRow());
		System.out.println("column = " + getColumn());
		System.out.println("x      = " + getX());
		System.out.println("y      = " + getY());
		
	}
	
	public void revertToLastPosition() {
		
		row = lastRow;
		column = lastColumn;
		setPosition(GRID_STEP*column,GRID_STEP*row);
		
	}
	
	public void setColumn(int column) {
		
		lastColumn = this.column;
		this.column = column;
		setX(column*GRID_STEP);
		
	}
	
	public void setGridPosition(int row, int column) {
		
		lastRow = this.row;
		lastColumn = this.column;
		this.row = row;
		this.column = column;
		setX(column*GRID_STEP);
		setY(row*GRID_STEP);
		
	}
	
	public void setLastColumn(int c) {lastColumn = c;}
	
	public void setLastRow(int r) {lastRow = r;}
	
	public void setRow(int row) {
		
		lastRow = this.row;
		this.row = row;
		setY(row*GRID_STEP);
		
	}
	
	public void setTexture(Texture texture) {this.texture = texture;}
	
	public int textureHeight() {return texture.getHeight();}
	
	public int textureWidth() {return texture.getWidth();}
	
}
