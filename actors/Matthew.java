package john.aquariumassault.actors;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Matthew extends TextureActor {

	// Enumerations
	
	

	// Fields
	
	private boolean[]   canMove;
	
	// Constructor
	
	public Matthew(Texture texture) {
		
		// Declarations & initialization
		
		super(texture);
		canMove = new boolean[4];
		setGridPosition(GRID_ROWS - 2, GRID_COLUMNS/2);
 		
 		// Allow Matthew to accept keyboard input
 				
		addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
					case Keys.W:     moveUp();    break;
					case Keys.A:     moveLeft();  break;
					case Keys.S:     moveDown();  break;
					case Keys.D:     moveRight(); break;
					case Keys.UP:    moveUp();    break;
					case Keys.LEFT:  moveLeft();  break;
					case Keys.DOWN:  moveDown();  break;
					case Keys.RIGHT: moveRight(); break;
				}
				return true;
			}
		});
 		
	}
	
	// Methods
	
	public void getValidMoves(Nate nate, Patron[] patrons) {
		
		int r = getRow();
		int c = getColumn();
		
		// Check for gameplay area boundaries
		
		canMove[DOWN] = r > EXIT_ROW;
		canMove[LEFT] = c > 0;
		canMove[RIGHT] = c < GRID_COLUMNS - 1;
		canMove[UP] = r < GRID_ROWS - 1;
		
		// Check for collisions with Nate and patrons
		
		for (int i = DOWN; i <= UP; i++) {
			if (canMove[i]) {
				canMove[i] = !(nate.getRow() == r + ROW_ADJ[i] && nate.getColumn() == c + COL_ADJ[i]);
				if (canMove[i]) {
					for (Patron p: patrons) {
						if (!p.isOffstage()) {
							canMove[i] = !(p.getRow() == r + ROW_ADJ[i] && p.getColumn() == c + COL_ADJ[i]);
							if (!canMove[i]) break;
						}
					}
				}
			}
		}

	}

	@Override
	public void moveDown() {if (canMove[DOWN]) setRow(getRow() - 1);}

	@Override
	public void moveLeft() {if (canMove[LEFT]) setColumn(getColumn() - 1);}

	@Override
	public void moveRight() {if (canMove[RIGHT]) setColumn(getColumn() + 1);}

	@Override
	public void moveUp() {if (canMove[UP]) setRow(getRow() + 1);}

}
