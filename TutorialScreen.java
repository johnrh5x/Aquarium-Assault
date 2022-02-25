package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import john.aquariumassault.actors.Nate;
import john.aquariumassault.actors.TextActor;
import john.aquariumassault.actors.TextureActor;

public class TutorialScreen extends ScreenAdapter implements Constants {

	// Fields
	
	private static final String[][] TEXT = {{"This patron could be a tank-tapper.","Tell her the rules before something awful happens!"},
                                            {"This patron is definitely a tank-tapper.","Don't let her get away with it!"},
                                            {"Mr. Basil Pesto is running away with the dogfish.","Catch him, or you'll be sacked!"}};
	
	private AquariumAssault game;
	private Stage           stage;
	private TextureActor[]  actor;
	private TextActor[][]   text;
	private int             scene = 0;
	private float           elapsedTime = 0f;
	
	// Constructor
	
	public TutorialScreen(AquariumAssault game) {
		
		this.game = game;
		
		// Stage
		
		stage = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT)) {
			@Override
			public boolean keyDown(int keycode) {
				// Do stuff
				return true;
			}
			@Override
			public boolean touchDown(int x, int y, int p, int b) {
				// Do stuff
				return true;
			}
		};
		stage.getCamera().position.x = WORLD_WIDTH/2;
		stage.getCamera().position.y = WORLD_HEIGHT/2;
		stage.getCamera().update();
		
		// Actors
		
		actor = new TextureActor[4];
		for (int i = NATE; i <= DOGFISH; i++) {
			actor[i] = new TextureActor(game.texture(i));
			actor[i].setSize(GRID_STEP,GRID_STEP);
		}
		
		// Movement instructions
		
		TextActor movement = new TextActor(game.font(),game.fontShader(),"Press an arrow key, W, A, S, or D, to move.");
		movement.setScale(0.5f);
		movement.setColor(Color.BLACK);
		movement.setSize(WORLD_WIDTH, GRID_STEP);
		movement.setPosition(0f, WORLD_HEIGHT - GRID_STEP);
		movement.align();
		stage.addActor(movement);
		
		// Scene instructions
		
		text = new TextActor[TEXT.length][];
		for (int i = 0; i < TEXT.length; i++) {
			text[i] = new TextActor[TEXT[i].length];
			for (int j = 0; j < TEXT[i].length; j++) {
				text[i][j] = new TextActor(game.font(),game.fontShader(),TEXT[i][j]);
				text[i][j].setSize(WORLD_WIDTH,GRID_STEP);
				text[i][j].setX(0f);
				text[i][j].setY((1 - j)*GRID_STEP);
				text[i][j].setScale(0.5f);
				text[i][j].setColor(Color.BLACK);
				text[i][j].align();
				text[i][j].toggleTyping();
			}
		}
		
		// Set the first scene
		
		actor[NATE].setGridPosition(7,6);
		actor[ALICE].setGridPosition(7,4);
		stage.addActor(actor[NATE]);
		stage.addActor(actor[ALICE]);
		stage.addActor(text[0][0]);
		
	}
	
	// Methods

	@Override
	public void dispose() {stage.dispose();}
	
	@Override
	public void render(float delta) {
		
		// Logic
		
		stage.act(delta);
		switch (scene) {
			case 0: scene0(delta); break;
			case 1: scene1(delta); break;
			case 2: scene2(delta); break;
		}
		
		// Rendering
		
		Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
		
	}
	
	private void scene0(float delta) {
		
		/* In this scene, Nate moves toward Alice, turning her green.
		 * He then returns to his initial position and Alice returns
		 * to the default color.  The animation loops until all the
		 * text at the bottom of the screen has been displayed. */
		
		if (!text[0][1].finishedTyping()) {
			
			/* Display an animation in the middle of the screen while
			 * instructions type out below. */
			
			elapsedTime += delta;
			if (elapsedTime > 0.5f) {
				if (actor[NATE].isAdjacentTo(actor[ALICE])) {
					actor[ALICE].setColor(Color.WHITE);
					actor[NATE].moveRight();				
				} else {
					actor[NATE].moveLeft();
					if (actor[NATE].isAdjacentTo(actor[ALICE])) {
						actor[ALICE].setColor(Color.GREEN);
					}
				}
				elapsedTime = 0f;
			}
			if (text[0][0].finishedTyping() && text[0][1].getStage() == null) {
				stage.addActor(text[0][1]);
			}
			
		} else if (elapsedTime < 2f) {
			
			/* Wait a bit before switching to the next scene. */
			
			elapsedTime += delta;
			
		} else {
			
			/* Set up the next scene. */
			
			text[0][0].remove();
			text[0][1].remove();
			stage.addActor(text[1][0]);
			actor[NATE].setGridPosition(8,5);
			actor[ALICE].setGridPosition(6,5);
			actor[ALICE].setColor(Color.RED);
			elapsedTime = 0f;
			scene = 1;
			
		}
		
	}
	
	private void scene1(float delta) {
		
		if (!text[1][1].finishedTyping()) {
			
			/* In this animation, Nate moves down towards a red Alice.
			 * When he reaches her, she turns green and the animation
			 * loops. */
			 
			elapsedTime += delta;
			if (elapsedTime > 0.5f) {
				if (actor[NATE].isAdjacentTo(actor[ALICE])) {
					actor[ALICE].setColor(Color.RED);
					actor[NATE].moveUp();
				} else {
					actor[NATE].moveDown();
					if (actor[NATE].isAdjacentTo(actor[ALICE])) {
						actor[ALICE].setColor(Color.GREEN);
					}
				}
				if (text[1][0].finishedTyping() && text[1][1].getStage() == null) {
					stage.addActor(text[1][1]);
				}
				elapsedTime = 0f;
			}
			
		} else if (elapsedTime < 2f) {
			
			// Delay a bit
			
			elapsedTime += delta;
			
		} else {
			
			// Set the next scene
			
			text[1][0].remove();
			text[1][1].remove();
			actor[ALICE].remove();
			stage.addActor(text[2][0]);
			actor[NATE].setGridPosition(7,4);
			actor[MATTHEW].setGridPosition(6,6);
			actor[DOGFISH].setGridPosition(7,6);
			stage.addActor(actor[MATTHEW]);
			stage.addActor(actor[DOGFISH]);
			scene = 2;
			elapsedTime = 0f;
			
		}
		
	}
	
	private void scene2(float delta) {
		
		if (!text[2][1].finishedTyping()) {
			
			/* In this animation, Nate moves right to intercept
			 * Matthew and the dogfish, who are moving up.  When
			 * Nate catches up to Matthew, the animation loops. */
			
			elapsedTime += delta; 
			if (elapsedTime > 0.5f) {
				if (actor[NATE].isAdjacentTo(actor[MATTHEW])) {
					actor[NATE].moveLeft();
					actor[MATTHEW].moveDown();
					actor[DOGFISH].moveDown();
				} else {
					actor[NATE].moveRight();
					actor[MATTHEW].moveUp();
					actor[DOGFISH].moveUp();
				}
				if (text[2][0].finishedTyping() && text[2][1].getStage() == null) {
					stage.addActor(text[2][1]);
				}
				elapsedTime = 0f;
			}
			
		} else if (elapsedTime < 2f) {
			
			// Delay a bit
			
			elapsedTime += delta;
			
		} else {
			
			// Return to the title screen
			
			game.setScreen(new TitleScreen(game));
			
		}
		
	}
	
	@Override
	public void show() {
		
		Gdx.input.setInputProcessor(stage);
		
	}
	
}
