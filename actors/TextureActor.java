package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import john.aquariumassault.Constants;

public abstract class TextureActor extends Actor implements Constants {
	
	// Fields
	
	private Texture texture;
	private int     row, column;
	
	// Constructor(s)
	
	public TextureActor(Texture texture) {
		
		super();
		this.texture = texture;
		setSize(GRID_STEP,GRID_STEP);
		row = 0;
		column = 0;
		
	}
	
	// Methods
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a*parentAlpha);
		batch.draw(texture,getX(),getY(),getWidth(),getHeight());
		
	}
	
	public int getRow() {return row;}
	
	public int getColumn() {return column;}

	public void moveDown() {
		
		int r = getRow();
		if (r > 0) setRow(r - 1);
		
	}
	
	public void moveLeft() {
		
		int c = getColumn();
		if (c > 0) setColumn(c - 1);
		
	}
	
	public void moveRight() {
	
		int c = getColumn();
		if (c < GRID_COLUMNS - 1) setColumn(c + 1);
		
	}
	
	public void moveUp() {
		
		int r = getRow();
		if (r < GRID_ROWS - 1) setRow(r + 1);
		
	}
	
	public void reportPosition() {
		
		System.out.println("row    = " + getRow());
		System.out.println("column = " + getColumn());
		System.out.println("x      = " + getX());
		System.out.println("y      = " + getY());
		
	}
	
	public void setColumn(int column) {
		
		this.column = column;
		setX(column*GRID_STEP);
		//reportPosition();
		
	}
	
	public void setGridPosition(int row, int column) {
		
		this.row = row;
		this.column = column;
		setX(column*GRID_STEP);
		setY(row*GRID_STEP);
		
	}
	
	public void setRow(int row) {
		
		this.row = row;
		setY(row*GRID_STEP);
		
	}
	
	public void setTexture(Texture texture) {this.texture = texture;}
	
}
