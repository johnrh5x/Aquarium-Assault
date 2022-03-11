package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import john.aquariumassault.actors.TextActor;
import john.aquariumassault.actors.TextureActor;

public class TutorialScreen extends ScreenAdapter implements Constants {

	// Fields
	
	private static final String[][] PROMPT = {{"Move by pressing the arrow keys or WASD.","You'll move once per key-press.","Try moving now.", "Hit Enter when you're ready to continue."},
		                                      {"Greet new patrons to earn points,","and prevent them from tapping on the tank.","Greet this patron now."," Hit Enter when you're ready to continue."},
		                                      {"You lose points when patrons tap on the tank.","Stop this patron from tapping now.","Hit Enter when you're ready to continue."},
		                                      {"If Mr. Basil Pesto runs off with the dogfish,","then you'll be sacked and lose the game.","Catch Mr. Basil Pesto now.","Hit Enter when you're ready to continue."}};
	
	private AquariumAssault game;
	private Stage           stage;
	private TextureActor    nate, alice, matthew, dogfish, fishtank;
	private int[]           bounds;
	private TextActor[][]   text;
	private int             scene = 0;
	private float           elapsedTime = 0f;
	private int             direction = TextureActor.UP;
	
	// Constructor
	
	public TutorialScreen(AquariumAssault game) {
		
		this.game = game;
		
		// Stage
		
		stage = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT)) {
			@Override
			public boolean keyDown(int keycode) {
				super.keyDown(keycode);
				if (keycode == Keys.ENTER) {
					switch (scene) {
						case 0:
							setScene(++scene);
							break;
						case 3:
							if (stage.getKeyboardFocus() == null) returnToTitle();
							break;
						default:
							if (alice.getColor().equals(Color.GREEN)) setScene(++scene);
							break;
					}
				}
				return true;
			}
		};
		stage.getCamera().position.x = WORLD_WIDTH/2;
		stage.getCamera().position.y = WORLD_HEIGHT/2;
		stage.getCamera().update();
		
		// Nate
		
		nate = new TextureActor(game.texture(NATE));
		nate.addListener(new InputListener () {
			@Override
			public boolean keyDown(InputEvent e, int keycode) {
				switch (keycode) {
					case Keys.A:     move(nate,TextureActor.LEFT);  break;
					case Keys.D:     move(nate,TextureActor.RIGHT); break;
					case Keys.DOWN:  move(nate,TextureActor.DOWN);  break;
					case Keys.LEFT:  move(nate,TextureActor.LEFT);  break;
					case Keys.RIGHT: move(nate,TextureActor.RIGHT); break;
					case Keys.S:     move(nate,TextureActor.DOWN);  break;
					case Keys.UP:    move(nate,TextureActor.UP);    break;
					case Keys.W:     move(nate,TextureActor.UP);    break;
				}
				return false;
			}
		});
		
		// Fishtank

		fishtank = new TextureActor(game.texture(FISHTANK));
		fishtank.setSize(WORLD_WIDTH,2*GRID_STEP);
		fishtank.setPosition(0f,0f);
		stage.addActor(fishtank);
		
		// Other TextureActors
		
		alice = new TextureActor(game.texture(ALICE));
		matthew = new TextureActor(game.texture(MATTHEW));
		dogfish = new TextureActor(game.texture(DOGFISH));

		// TextActors
		
		text = new TextActor[PROMPT.length][];
		for (int i = 0; i < PROMPT.length; i++) {
			text[i] = new TextActor[PROMPT[i].length];
			for (int j = 0; j < text[i].length; j++) {
				text[i][j] = new TextActor(game.font(),game.fontShader(),PROMPT[i][j]);
				text[i][j].setColor(Color.BLUE);
				text[i][j].setScale(0.5f);
				text[i][j].setSize(WORLD_WIDTH,GRID_STEP);
				text[i][j].setPosition(0f,WORLD_HEIGHT - (j + 1)*GRID_STEP);
			}
		}

		// Set the first scene
		
		bounds = new int[4];
		bounds[TextureActor.DOWN] = 2;
		bounds[TextureActor.LEFT] = 0;
		bounds[TextureActor.RIGHT] = GRID_COLUMNS - 1;
		bounds[TextureActor.UP] = GRID_ROWS - text[0].length;
		nate.setGridPosition((GRID_ROWS - text[0].length)/2, GRID_COLUMNS/2); // Center Nate in available space
		stage.addActor(nate);
		stage.setKeyboardFocus(nate);
		for (int i = 0; i < text[0].length; i++) stage.addActor(text[0][i]);
		
	}
	
	// Methods

	@Override
	public void dispose() {stage.dispose();}
	
	private void move(TextureActor actor, int direction) {
		
		int r = actor.getRow();
		int c = actor.getColumn();
		switch (direction) {
			case TextureActor.DOWN:  if (r > bounds[TextureActor.DOWN])  actor.setRow(r - 1);    break;
			case TextureActor.LEFT:  if (c > bounds[TextureActor.LEFT])  actor.setColumn(c - 1); break;
			case TextureActor.RIGHT: if (c < bounds[TextureActor.RIGHT]) actor.setColumn(c + 1); break;
			case TextureActor.UP:    if (r < bounds[TextureActor.UP])    actor.setRow(r + 1);    break;
		}
		
	}
	
	public void returnToTitle() {game.setScreen(new TitleScreen(game));}
	
	@Override
	public void render(float delta) {
		
		// Logic
		
		stage.act(delta);
		switch (scene) {
			case 1: 
				if (nate.isAdjacentTo(alice)) alice.setColor(Color.GREEN);
				break;
			case 2:
				if (nate.isAdjacentTo(alice)) alice.setColor(Color.GREEN);
				break;
			case 3:
				if (stage.getKeyboardFocus() != null) {
					if (nate.isAdjacentTo(matthew)) {
						stage.setKeyboardFocus(null);
					} else {
						elapsedTime += delta;
						if (elapsedTime > 0.5f) {
							if (dogfish.getRow() == bounds[TextureActor.UP]) {
								direction = TextureActor.DOWN;
							} else if (matthew.getRow() == EXIT_ROW) {
								direction = TextureActor.UP;
							}
							move(dogfish,direction);
							move(matthew,direction);
							elapsedTime = 0f;
						}
					}
				}
				break;
		}
		
		// Rendering
		
		Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        		
	}
	
	@Override
	public void resize(int width, int height) {stage.getViewport().update(width,height);}
	
	private void setScene(int sceneIndex) {
		
		// Change prompts and adjust upper bound
		
		for (TextActor t: text[sceneIndex - 1]) t.remove();
		for (TextActor t: text[sceneIndex]) stage.addActor(t);
		bounds[TextureActor.UP] = GRID_ROWS - text[sceneIndex].length;
		
		// Adjust, add, and remove actors
		
		switch (sceneIndex) {
			case 1:
				nate.setGridPosition(EXIT_ROW,GRID_COLUMNS/2);
				alice.setGridPosition(bounds[TextureActor.UP],GRID_COLUMNS/2);
				stage.addActor(alice);
				break;
			case 2:
				nate.setGridPosition(bounds[TextureActor.UP],GRID_COLUMNS/2);
				alice.setGridPosition(EXIT_ROW,GRID_COLUMNS/2);
				alice.setColor(Color.RED);
				break;
			case 3:
				nate.setGridPosition(GRID_ROWS/2,0);
				alice.remove();
				matthew.setGridPosition(EXIT_ROW,GRID_COLUMNS-1);
				dogfish.setGridPosition(EXIT_ROW+1,GRID_COLUMNS-1);
				stage.addActor(matthew);
				stage.addActor(dogfish);
				break;
		}
		
		
	}
	
	@Override
	public void show() {Gdx.input.setInputProcessor(stage);}
	
}
