package john.aquariumassault.actors;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

public class Nate extends TextureActor {

	// Fields
	
	private boolean[] canMove;
		
	// Constructor(s)
	
	public Nate(Texture texture) {
		
		super(texture);
		
		/* Allow Nate to accept keyboard input */
		
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
		
		/* Initialize array denoting valid moves */
		
		canMove = new boolean[4];
		
	}
	
	// Methods

	@Override
	public void moveDown() {if (canMove[DOWN]) setRow(getRow() - 1);}

	@Override
	public void moveLeft() {if (canMove[LEFT]) setColumn(getColumn() - 1);}

	@Override
	public void moveRight() {if (canMove[RIGHT]) setColumn(getColumn() + 1);}

	@Override
	public void moveUp() {if (canMove[UP]) setRow(getRow() + 1);}

	public void getValidMoves(Matthew matthew, Patron[] patrons) {
		
		int r = getRow();
		int c = getColumn();	
		
		// Check for gameplay area boundaries
		
		canMove[DOWN] = r > EXIT_ROW;
		canMove[LEFT] = c > 0;
		canMove[RIGHT] = c < GRID_COLUMNS - 1;
		canMove[UP] = r < GRID_ROWS - 1;
		
		// Check for collisions with Matthew and patrons
		
		for (int i = DOWN; i <= UP; i++) {
			if (canMove[i]) {			
				canMove[i] = !(matthew.getRow() == r + ROW_ADJ[i] && matthew.getColumn() == c + COL_ADJ[i]);
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

}
