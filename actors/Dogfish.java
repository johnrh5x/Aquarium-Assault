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
		elapsedTime = 0f;
		swimming = true;
		
	}
	
	
	// Methods
	
	@Override
	public void act(float delta) {
		
		elapsedTime += delta;
		if (elapsedTime > turnLength) {
			switch (getRow()) {
				case 0:
					switch (getColumn()) {
						case GRID_COLUMNS - 1: moveUp();   break;
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
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.draw(getTexture(),
		           getX(),
		           getY(),
		           getWidth(),
		           getHeight(),
		           0,
		           0,
		           textureWidth(),
		           textureHeight(),
		           getRow() == 0,   // Flip texture horizontally if the dogfish is in the bottom row
		           false);
		
	}

}
