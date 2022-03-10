package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import john.aquariumassault.actors.TextActor;
import john.aquariumassault.actors.TextureActor;

public class IntroScreen extends ScreenAdapter implements Constants {

	// Fields
	
	private static final String[][] TEXT = {{"This is you.","You work at the aquarium."},
		                                    {"This is an aquarium patron.","Patrons aren't fish,","so they all look alike to you."},
		                                    {"This is Mr. Basil Pesto.","He's a bad man:","tank tapper,","fish-napper,","and a thoroughly bad example."},
		                                    {"Your job is to:","greet patrons as they enter,","stop patrons from tapping on the tank, and ","watch out for Mr. Basil Pesto!"}};

	private static final int[] PORTRAIT_INDEX = {NATE,ALICE,MATTHEW,NATE};

	private AquariumAssault game;
	private Stage           stage;
	private TextureActor[]  portrait;
	private TextActor[][]   text;
	private float[]         dy; // Used to compute coordinates for portraits and text
	private int             scene, line;
	private float           delay = 2f;
	
	// Constructor
	
	public IntroScreen(AquariumAssault game) {
		
		this.game = game;
		
		// Create a stage and configure it to handle input
		
		stage = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT)) {
			@Override
			public boolean keyDown(int keycode) {
				titleScreen();
				return false;
			}
			@Override
			public boolean touchDown(int x, int y, int p, int b) {
				titleScreen();
				return false;
			}
		};
		
		// Create TextureActors to represent Nate, Alice, and Matthew
		
		portrait = new TextureActor[3];
		for (int i = NATE; i <= ALICE; i++) {
			portrait[i] = new TextureActor(game.texture(i));
			portrait[i].setSize(2*GRID_STEP,2*GRID_STEP);
			portrait[i].setX(0.5f*(WORLD_WIDTH - portrait[i].getWidth()));
		}
		
		// Create TextActors to show text
		
		text = new TextActor[TEXT.length][];
		for (int i = 0; i < TEXT.length; i++) {
			text[i] = new TextActor[TEXT[i].length];
			for (int j = 0; j < text[i].length; j++) {
				text[i][j] = new TextActor(game.font(),game.fontShader(),TEXT[i][j]);
				text[i][j].setColor(Color.BLUE);
				text[i][j].setScale(0.5f);
				text[i][j].setSize(WORLD_WIDTH,GRID_STEP);
				text[i][j].align();
				text[i][j].toggleTyping();
			}
		}
		
		// Layout
		
		dy = new float[TEXT.length];
		for (int i = 0; i < TEXT.length; i++) {
			dy[i] = 0.5f*(WORLD_HEIGHT - GRID_STEP*TEXT[i].length - portrait[PORTRAIT_INDEX[scene]].getHeight());
			for (int j = 0; j < TEXT[i].length; j++) {
				text[i][j].setY(WORLD_HEIGHT - dy[i] - portrait[PORTRAIT_INDEX[scene]].getHeight() - (j + 1)*GRID_STEP);
			}
		}
		portrait[NATE].setY(WORLD_HEIGHT - dy[0] - portrait[NATE].getHeight());
		
		// Initial view
		
		stage.addActor(portrait[NATE]);
		stage.addActor(text[0][0]);
		scene = 0;
		line = 0;

	}
	
	// Methods
	
	private void titleScreen() {game.setScreen(new TitleScreen(game));}
		
	@Override
	public void render(float delta) {
		
		//  Logic
		
		stage.act(delta);
		if (text[scene][line].finishedTyping()) {
			if (line == text[scene].length - 1) {
				if (delay > 0f) {
					delay -= delta;
				} else {
					if (scene == text.length - 1) {
						game.setScreen(new TitleScreen(game));
					} else {
						portrait[PORTRAIT_INDEX[scene]].remove();
						for (int i = 0; i < text[scene].length; i++) text[scene][i].remove();
						scene++;
						line = 0;
						portrait[PORTRAIT_INDEX[scene]].setY(WORLD_HEIGHT - dy[scene] - portrait[PORTRAIT_INDEX[scene]].getHeight());
						stage.addActor(portrait[PORTRAIT_INDEX[scene]]);
						stage.addActor(text[scene][0]);
						delay = 2f;
					}
				}
			} else {
				line++;
				stage.addActor(text[scene][line]);				
			}
		}

		//  Rendering
				
		Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {stage.getViewport().update(width,height);}

	@Override
	public void show() {Gdx.input.setInputProcessor(stage);}

}
