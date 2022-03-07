package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import john.aquariumassault.actors.TextActor;
import john.aquariumassault.actors.TextureActor;

public class TutorialScreen extends ScreenAdapter implements Constants {

	// Fields
	
	private static final String[][] TEXT = {{"This patron could be a tank-tapper.","Tell her the rules before something awful happens!"},
                                            {"This patron is definitely a tank-tapper.","Don't let her get away with it!"},
                                            {"Mr. Basil Pesto is running away with the dogfish.","Catch him, or you'll be sacked!"},
                                            {"Tap an arrow key, W, A, S, or D to move.","Press Space to pause.","Press ESC to return to the main menu."}};
	
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
				returnToTitle();
				return true;
			}
			@Override
			public boolean touchDown(int x, int y, int p, int b) {
				returnToTitle();
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
		
		// Scene instructions
		
		text = new TextActor[TEXT.length][];
		for (int i = 0; i < TEXT.length; i++) {
			text[i] = new TextActor[TEXT[i].length];
			for (int j = 0; j < TEXT[i].length; j++) {
				text[i][j] = new TextActor(game.font(),game.fontShader(),TEXT[i][j]);
				text[i][j].setSize(WORLD_WIDTH,GRID_STEP);
				text[i][j].setX(0f);
				text[i][j].setY(WORLD_HEIGHT - (j + 1)*GRID_STEP);
				text[i][j].setScale(0.5f);
				text[i][j].setColor(Color.BLACK);
				text[i][j].align();
				text[i][j].toggleTyping();
			}
		}
		
		// Set the first scene
		
		actor[ALICE].setGridPosition(GRID_ROWS - 3, 5);
		actor[NATE].setGridPosition(GRID_ROWS - 7, 5);
		stage.addActor(actor[ALICE]);
		stage.addActor(text[0][0]);
		
	}
	
	// Methods

	@Override
	public void dispose() {stage.dispose();}
	
	public void returnToTitle() {game.setScreen(new TitleScreen(game));}
	
	@Override
	public void render(float delta) {
		
		// Logic
		
		stage.act(delta);
		switch (scene) {
			case 0:  scene0(delta); break;
			case 1:  scene1(delta); break;
			case 2:  scene2(delta); break;
			default: scene3(delta); break;
		}
		
		// Rendering
		
		Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
		
	}
	
	private void scene0(float delta) {
		
		if (text[0][0].finishedTyping() && text[0][1].getStage() == null) {
			stage.addActor(text[0][1]);
		}
		if (text[0][1].finishedTyping() && actor[NATE].getStage() == null) {
			stage.addActor(actor[NATE]);
		}
		if (actor[NATE].getStage() != null && !actor[NATE].isAdjacentTo(actor[ALICE])) {
			elapsedTime += delta;
			if (elapsedTime > 0.5f) {
				actor[NATE].moveUp();
				elapsedTime = 0f;
				if (actor[NATE].isAdjacentTo(actor[ALICE])) {
					actor[ALICE].setColor(Color.GREEN);
				}
			}
		}
		if (actor[NATE].isAdjacentTo(actor[ALICE])) {
			if (elapsedTime < 2f) {
				elapsedTime += delta;
			} else {
				text[0][0].remove();
				text[0][1].remove();
				actor[NATE].remove();
				actor[NATE].setGridPosition(GRID_ROWS - 7, 5);
				actor[ALICE].setColor(Color.RED);
				elapsedTime = 0f;
				scene = 1;
				stage.addActor(text[1][0]);
			}
		}
		
	}
	
	private void scene1(float delta) {
	
		if (text[1][0].finishedTyping() && text[1][1].getStage() == null) {
			stage.addActor(text[1][1]);
		}
		if (text[1][1].finishedTyping() && actor[NATE].getStage() == null) {
			stage.addActor(actor[NATE]);
		}
		if (actor[NATE].getStage() != null && !actor[NATE].isAdjacentTo(actor[ALICE])) {
			elapsedTime += delta;
			if (elapsedTime > 0.5f) {
				actor[NATE].moveUp();
				elapsedTime = 0f;
				if (actor[NATE].isAdjacentTo(actor[ALICE])) {
					actor[ALICE].setColor(Color.GREEN);
				}
			}
		}
		if (actor[NATE].isAdjacentTo(actor[ALICE])) {
			if (elapsedTime < 2f) {
				elapsedTime += delta;
			} else {
				text[1][0].remove();
				text[1][1].remove();
				actor[NATE].remove();
				actor[ALICE].remove();
				actor[DOGFISH].setGridPosition(GRID_ROWS - 3, 5);
				actor[MATTHEW].setGridPosition(GRID_ROWS - 4, 5);
				actor[NATE].setGridPosition(GRID_ROWS - 7, 1);
				elapsedTime = 0f;
				scene = 2;
				stage.addActor(text[2][0]);
				stage.addActor(actor[DOGFISH]);
				stage.addActor(actor[MATTHEW]);
			}
		}
		
	}
	
	private void scene2(float delta) {

		if (text[2][0].finishedTyping() && text[2][1].getStage() == null) {
			stage.addActor(text[2][1]);
		}
		if (text[2][1].finishedTyping() && actor[NATE].getStage() == null) {
			stage.addActor(actor[NATE]);
		}
		if (actor[NATE].getStage() != null && !actor[NATE].isAdjacentTo(actor[MATTHEW])) {
			elapsedTime += delta;
			if (elapsedTime > 0.5f) {
				actor[NATE].moveRight();
				actor[MATTHEW].moveDown();
				actor[DOGFISH].moveDown();
				elapsedTime = 0f;
			}
		}
		if (actor[NATE].isAdjacentTo(actor[MATTHEW])) {
			if (elapsedTime < 2f) {
				elapsedTime += delta;
			} else {
				text[2][0].remove();
				text[2][1].remove();
				actor[NATE].remove();
				actor[MATTHEW].remove();
				actor[DOGFISH].remove();
				stage.addActor(text[3][0]);
				elapsedTime = 0f;
				scene = 3;
			}
		}
		
	}
	
	private void scene3(float delta) {
	
		if (text[3][0].finishedTyping() && text[3][1].getStage() == null) {
			stage.addActor(text[3][1]);
		}
		if (text[3][1].finishedTyping() && text[3][2].getStage() == null) {
			stage.addActor(text[3][2]);
		}
		if (text[3][2].finishedTyping()) {
			if (elapsedTime < 2f) {
				elapsedTime += delta;
			} else {
				returnToTitle();
			}
		}
		
	}
	
	@Override
	public void show() {Gdx.input.setInputProcessor(stage);}
	
}
