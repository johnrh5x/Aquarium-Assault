package john.aquariumassault;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import john.aquariumassault.actors.TextActor;
import john.aquariumassault.actors.TextureActor;

public class TitleScreen extends ScreenAdapter implements Constants {
	
	//  Fields
	
	private AquariumAssault   game;
	private TextureActor[]    actors;
	private Stage             stage;
	private float             elapsedTime = 0f;
	
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

		TextActor title = new TextActor(game.font(),game.fontShader(),"Aquarium Assault");
		title.setColor(1f,0f,0f,1f);
		title.setWidth(WORLD_WIDTH);
		title.setHeight(2f*GRID_STEP);
		title.setPosition(0f, WORLD_HEIGHT - title.getHeight() - GRID_STEP);
		stage.addActor(title);
		
		// Create subtitle text
		
		TextActor subtitle = new TextActor(game.font(),game.fontShader(),"Dark Day For a Dogfish");
		subtitle.setColor(0f,0f,1f,1f);
		subtitle.setWidth(WORLD_WIDTH);
		subtitle.setHeight(GRID_STEP);
		subtitle.setScale(0.5f);
		subtitle.setPosition(0f,title.getY() - GRID_STEP);
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

		TextActor tagline = new TextActor(game.font(),game.fontShader(),"A Cheery EWS Fan Game");
		tagline.setColor(0f,0f,0f,1f);
		tagline.setWidth(WORLD_WIDTH);
		tagline.setHeight(GRID_STEP);
		tagline.setScaleX(0.5f);
		tagline.setPosition(0,GRID_STEP);
		stage.addActor(tagline); 
		
	}	
	
	// Methods
	
	@Override
	public void dispose() {
	
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
