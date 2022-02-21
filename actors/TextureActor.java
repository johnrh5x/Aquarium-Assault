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
	private int       row, column;
	
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
		batch.setColor(Color.WHITE);
		
	}
	
	public int getRow() {return row;}
	
	public int getColumn() {return column;}
	
	public Texture getTexture() {return texture;}

	public boolean isAdjacentTo(TextureActor actor) {
		
		int dx = getColumn() - actor.getColumn();
		int dy = getRow() - actor.getRow();
		return dx*dx + dy*dy <= 1;
		
	}

	public boolean isAdjacentTo(int row, int column) {
		
		int dx = this.row - row;
		int dy = this.column - column;
		return dx*dx + dy*dy <= 1;
		
	}

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
	
	public int textureHeight() {return texture.getHeight();}
	
	public int textureWidth() {return texture.getWidth();}
	
}
