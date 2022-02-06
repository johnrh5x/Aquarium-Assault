package john.aquariumassault;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import john.aquariumassault.actors.TextActor;
import john.aquariumassault.actors.TextureActor;

public class TitleScreen extends ScreenAdapter implements Constants {
	
	//  Fields
	
	private AquariumAssault game;
	private BitmapFont      titleFont, subtitleFont, tagFont;
	private TextureActor[]  actors;
	private Stage           stage;
	private float           elapsedTime = 0f;
	
	// Constructor
	
	public TitleScreen(AquariumAssault game) {
		
		this.game = game;
		
		// Create a stage
		
		stage = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT)) {
			@Override
			public boolean keyDown(int keyCode) {
				nextScreen();
				return true;
			}
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				nextScreen();
				return true;
			}
		};
		stage.getCamera().position.x = WORLD_WIDTH/2;
		stage.getCamera().position.y = WORLD_HEIGHT/2;
		stage.getCamera().update();
		
		// Create title text
		
		titleFont = new BitmapFont();
		titleFont.setColor(1f,0f,0f,1f); // Opaque red
		titleFont.getData().setScale(1.25f);
		TextActor title = new TextActor(titleFont, "Aquarium Assault");
		title.setWidth(WORLD_WIDTH);
		title.setHeight(2f*GRID_STEP);
		title.setPosition(0f, WORLD_HEIGHT - title.getHeight() - GRID_STEP);
		title.centerHorizontally();
		stage.addActor(title);
		
		// Create subtitle text
		
		subtitleFont = new BitmapFont();
		subtitleFont.setColor(0f,0f,1f,1f); // Opaque blue
		subtitleFont.getData().setScale(0.5f);
		TextActor subtitle = new TextActor(subtitleFont,"Dark Day For a Dogfish");
		subtitle.setWidth(WORLD_WIDTH);
		subtitle.setHeight(GRID_STEP);
		subtitle.setPosition(0f,title.getY() - GRID_STEP);
		subtitle.centerHorizontally();
		stage.addActor(subtitle);
		
		// Create portraits
		
		TextureActor[] actors = new TextureActor[3];
		for (int i = NATE; i <= ALICE; i++) {
			actors[i] = new TextureActor(game.texture(i));
			actors[i].setSize(GRID_STEP, GRID_STEP);
			actors[i].setY(subtitle.getY() - 2*GRID_STEP);
		}
		actors[ALICE].setX((WORLD_WIDTH - 5*GRID_STEP)/2);
		actors[MATTHEW].setX(actors[ALICE].getX() + 2*GRID_STEP);
		actors[NATE].setX(actors[MATTHEW].getX() + 2*GRID_STEP);
		for (int i = 0; i < actors.length; i++) stage.addActor(actors[i]);
		
		// Create tag text
		
		BitmapFont tagFont = new BitmapFont();
		tagFont.getData().setScale(0.5f);
		tagFont.setColor(0f,0f,0f,1f); // Opaque black
		TextActor tagline = new TextActor(tagFont,"A Cheery EWS Fan Game");
		tagline.setWidth(WORLD_WIDTH);
		tagline.setHeight(GRID_STEP);
		tagline.setPosition(0,GRID_STEP);
		tagline.centerHorizontally();
		stage.addActor(tagline); 
		
	}	
	
	// Methods
	
	@Override
	public void dispose() {
	
		titleFont.dispose();
		subtitleFont.dispose();
		tagFont.dispose();
		stage.dispose();
		
	}
	
	private void nextScreen() {
		
		game.setScreen(new PlayScreen(game));
		
	}
		
	@Override
	public void render(float delta) {
		
		// After ten seconds, go to next screen
		
		elapsedTime += delta;
		if (elapsedTime > 10f) nextScreen();
			
		// Clear screen (white)
        
        Gdx.gl.glClearColor(1f,1f,1f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Draw game objects
        
        stage.draw();
		
	}
	
	@Override
	public void resize(int width, int height) {
	
		stage.getViewport().update(width,height);
		
	}
	
	@Override
	public void show() {Gdx.input.setInputProcessor(stage);}
	
}
