package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Dogfish extends TextureActor {

	// Fields
	
	private       float   elapsedTime;
	private final float   turnLength = 0.5f;
	private       boolean swimming;
	
	// Constructor(s)
	
	public Dogfish(Texture texture) {
		
		super(texture);
		setID(DOGFISH);
		elapsedTime = 0f;
		swimming = true;
		
	}
	
	// Methods
	
	@Override
	public void act(float delta) {
		
		/* When left to its own devices (i.e., if swimming == true) the 
		 * dogfish swims around the tank in a counter-clockwise 
		 * fashion. */
		
		if (swimming) {
			elapsedTime += delta;
			if (elapsedTime > turnLength) {
				switch (getRow()) {
					case 0:
						switch (getColumn()) {
							case GRID_COLUMNS - 1: moveUp();    break;
							default:               moveRight(); break;
						}
						break;
					case EXIT_ROW - 1:
						switch (getColumn()) {
							case 0:  moveDown(); break;
							default: moveLeft(); break;
						}
						break;
					default:
						switch (getColumn()) {
							case 0:                moveDown(); break;
							case GRID_COLUMNS - 1: moveUp();   break;
						}
				}
				elapsedTime = 0f;
			}
		} else {
			
			/* If the dogfish is not swimming, then it has been
			 * captured and should be one square above Matthew. */
			
			int[] mp = TextureActor.position(MATTHEW);
			int targetRow = mp[0] + 1;
			int targetColumn = mp[1];
			if (targetRow != getRow() || targetColumn != getColumn()) {
				setGridPosition(targetRow,targetColumn);
			}
			
		}
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.draw(getTexture(),    // Texture to draw
		           getX(),          // X-coordinate of actor (world coordinates)
		           getY(),          // Y-coordinate of actor (world coordinates)
		           getWidth(),      // Actor width (world coordinates)
		           getHeight(),     // Actor height (world coordinates)
		           0,               // X-coordinate of the lower-left corner of the rectangle to draw (pixels)
		           0,               // Y-coordinate of the lower-left corner of the rectangle to draw (pixels)
		           textureWidth(),  // Width of rectangle to draw (pixels)
		           textureHeight(), // Height of rectangle to draw (pixels)
		           getRow() == 0,   // Flip texture horizontally if the dogfish is in the bottom row
		           false);          // Do not flip vertically
		
	}

	public boolean isSwimming() {return swimming;}

	public void setSwimming(boolean b) {
		
		swimming = b;
		if (!swimming) removeFromMap();
		
	}

}
