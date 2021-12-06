package john.aquariumassault;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AquariumAssault extends Game implements Constants {

	// Fields
	
	private Texture[] textures;
	
	// Methods
	
	@Override
	public void create () {

		textures = new Texture[4];
		textures[NATE] = new Texture(Gdx.files.internal("Nate.png"));
		textures[MATTHEW] = new Texture(Gdx.files.internal("Matthew.jpg"));
		textures[ALICE] = new Texture(Gdx.files.internal("Alice.png"));
		textures[BACKGROUND] = new Texture(Gdx.files.internal("background.png"));
		setScreen(new PlayScreen(textures));

	}
	
	@Override
	public void dispose () {
		
		for (Texture t: textures) t.dispose();
		
	}

	
}
