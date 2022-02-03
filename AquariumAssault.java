package john.aquariumassault;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AquariumAssault extends Game implements Constants {

	// Fields
	
	private Texture[]  textures;
	
	// Methods
	
	@Override
	public void create () {

		// Create textures

		textures = new Texture[6];
		textures[NATE] = new Texture(Gdx.files.internal("Nate.png"));
		textures[MATTHEW] = new Texture(Gdx.files.internal("Matthew.jpg"));
		textures[ALICE] = new Texture(Gdx.files.internal("Alice.png"));
		textures[BACKGROUND] = new Texture(Gdx.files.internal("background.png"));
		textures[FISHTANK] = new Texture(Gdx.files.internal("fishtank.png"));
		textures[DOGFISH] = new Texture(Gdx.files.internal("dogfish.png"));
				
		// Start the game
		
		setScreen(new TitleScreen(this, textures));

	}
	
	@Override
	public void dispose () {
		
		for (Texture t: textures) t.dispose();
		
	}

	
}
