package john.aquariumassault;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import john.aquariumassault.actors.Patron;

public class AquariumAssault extends Game implements Constants {

	// Fields
	
	private Texture[]         textures;
	private DistanceFieldFont font;
	private ShaderProgram     fontShader;
	private int               score;
	private boolean           dogfish;
	private Sound             tap;
	
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
		
		// Create a scaleable font and the corresponding shader
		
		BitmapFont temp = new BitmapFont(Gdx.files.internal("font/droid_sans.fnt"),Gdx.files.internal("font/droid_sans.png"),false);
		font = new DistanceFieldFont(temp.getData(),temp.getRegions(),false);
		fontShader = font.createDistanceFieldShader();
		
		// Initialize results variables
		
		score = 0;
		dogfish = false;
						
		// Create a tapping sound effect
		
		tap = Gdx.audio.newSound(Gdx.files.internal("audio/single tap.wav"));
		Patron.setTap(tap);
					
		// Start the game
		
		setScreen(new IntroScreen(this));

	}
	
	@Override
	public void dispose () {
		
		for (Texture t: textures) t.dispose();
		font.dispose();
		fontShader.dispose();
		tap.dispose();
		
	}

	public DistanceFieldFont font() {return font;}
	
	public ShaderProgram fontShader() {return fontShader;}

	public int getScore() {return score;}

	public boolean lostDogfish() {return dogfish;}

	public void setLostDogfish(boolean b) {dogfish = b;}

	public void setScore(int score) {this.score = score;}
	
	public Sound tap() {return tap;}

	public Texture texture(int i) {
		
		Texture output = null;
		if (i >= 0 && i < textures.length) {
			output = textures[i];
		}
		return output;
		
	}
	
}
