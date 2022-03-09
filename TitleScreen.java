package john.aquariumassault;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import john.aquariumassault.actors.Button;
import john.aquariumassault.actors.TextActor;
import john.aquariumassault.actors.TextureActor;

public class TitleScreen extends ScreenAdapter implements Constants {
	
	//  Fields
	
	private static final String[] BUTTON_TEXT = {"New Game","Introduction","Instructions","Credits","Quit"};
	
	private AquariumAssault   game;
	private Stage             stage;
	private float             elapsedTime = 0f;
	
	// Constructor
	
	public TitleScreen(AquariumAssault game) {
		
		this.game = game;
		
		// Create a stage
		
		stage = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT));
		stage.getCamera().position.x = WORLD_WIDTH/2;
		stage.getCamera().position.y = WORLD_HEIGHT/2;
		stage.getCamera().update();

		// Create title text

		TextActor title = new TextActor(game.font(),game.fontShader(),"Aquarium Assault");
		title.setColor(Color.RED);
		title.setWidth(WORLD_WIDTH);
		title.setHeight(GRID_STEP);
		stage.addActor(title);
		
		// Create subtitle text
		
		TextActor subtitle = new TextActor(game.font(),game.fontShader(),"Dark Day For a Dogfish");
		subtitle.setColor(Color.RED);
		subtitle.setWidth(WORLD_WIDTH);
		subtitle.setHeight(0.5f*GRID_STEP);
		subtitle.setScale(0.5f);
		stage.addActor(subtitle);
		
		// Create tag text

		TextActor tagline = new TextActor(game.font(),game.fontShader(),"A Cheery EWS Fan Game");
		tagline.setColor(Color.BLUE);
		tagline.setWidth(WORLD_WIDTH);
		tagline.setHeight(0.5f*GRID_STEP);
		tagline.setScaleX(0.5f);
		tagline.setX(0f);
		stage.addActor(tagline); 
		
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
		
		// Create buttons
		
		Button[] buttons = new Button[BUTTON_TEXT.length];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Button(game.font(),game.fontShader(),BUTTON_TEXT[i]);
			buttons[i].setFontScale(0.5f);
			buttons[i].addTexture(game.texture(FISHTANK));
			stage.addActor(buttons[i]);
		}
		Button.conformSizes(buttons);
		Button.centerHorizontally(0f,WORLD_WIDTH,buttons);
		
		// New game functionality
		
		buttons[0].addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
				playScreen();
				return false;
			}
		});
		
		// Introduction functionality
		
		buttons[1].addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
				introScreen();
				return false;
			}
		});
		
		// Instructions funtionality
		
		buttons[2].addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
				tutorialScreen();
				return false;
			}
		});
		
		// Credits functionality
		
		buttons[3].addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
				creditsScreen();
				return false;
			}
		});
		
		// Quit functionality
		
		buttons[4].addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
				Gdx.app.exit();
				return false;
			}
		});
		
		// Layout
		
		title.setY(WORLD_HEIGHT - GRID_STEP - title.getHeight());
		subtitle.setY(title.getY() - 0.25f*GRID_STEP - subtitle.getHeight());
		tagline.setY(subtitle.getY() - 0.25f*GRID_STEP - tagline.getHeight());	
		float portraitY = tagline.getY() - 2*GRID_STEP;
		for (int i = 0; i < actors.length; i++) actors[i].setY(portraitY);
		buttons[0].setY(portraitY - 2*GRID_STEP);
		for (int i = 1; i < buttons.length; i++) buttons[i].setY(buttons[i-1].getY() - GRID_STEP);
		
	}	
	
	// Methods
	
	private void creditsScreen() {game.setScreen(new CreditsScreen(game));}
	
	@Override
	public void dispose() {stage.dispose();}
	
	private void introScreen() {game.setScreen(new IntroScreen(game));}
	
	private void playScreen() {game.setScreen(new PlayScreen(game));}
		
	@Override
	public void render(float delta) {
		
		// Logic
		
		stage.act(delta);
		
		// Clear screen (white)
        
        Gdx.gl.glClearColor(1f,1f,1f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Draw game objects
        
        stage.draw();
		
	}
	
	@Override
	public void resize(int width, int height) {stage.getViewport().update(width,height);}
	
	@Override
	public void show() {Gdx.input.setInputProcessor(stage);}

	private void tutorialScreen() {game.setScreen(new TutorialScreen(game));}
	
}
