package john.aquariumassault;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AquariumAssault extends Game implements Constants {

	// Fields
	
	private Texture[]  textures;
	private BitmapFont font;
	
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
		
		// Create a font and scale to fit
		
		font = new BitmapFont();                // 15 pt Arial
		font.getData().setScale(GRID_STEP/15f); // Scales font to fit in grid row 
		
		// Start the game
		
		setScreen(new PlayScreen(textures, font));

	}
	
	@Override
	public void dispose () {
		
		for (Texture t: textures) t.dispose();
		font.dispose();
		
	}

	
}
