package john.aquariumassault.actors;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Nate extends TextureActor {

	// Fields
	
	
		
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
		
	}
	
	// Methods

}
