package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import john.aquariumassault.actors.Dogfish;
import john.aquariumassault.actors.Matthew;
import john.aquariumassault.actors.Nate;
import john.aquariumassault.actors.Patron;
import john.aquariumassault.actors.TextActor;
import john.aquariumassault.actors.TextureActor;
import john.aquariumassault.actors.TimeKeeper;

public class PlayScreen extends ScreenAdapter implements Constants {

	// Fields
	
	private AquariumAssault game;
	private Stage           stage;
	private Nate            nate;
	private Dogfish         dogfish;
	private int             numberOfPatrons;
	private Patron[]        patrons;
	private Matthew         matthew;
	private float           elapsedTime;
	private float           newPatronInterval;
	private int             score;
	private TextActor       scoreKeeper;
	private TimeKeeper      timeKeeper;
	private boolean         paused = false;
	private boolean         over = false;
	
	// Constructor(s)
	
	public PlayScreen(AquariumAssault game) {
		
		this.game = game;
		
		/* Create a stage to hold the actors, configure the stage so
		 * that play pauses when the player hits ESC, and center the 
		 * camera in the middle of the stage. */
		
		stage = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT)) {
			@Override
			public boolean keyDown(int keycode) {
				if (!paused && !over) super.keyDown(keycode);          // Allow normal keyboard input while game is unpaused and not yet over
				if (!over && keycode == Keys.ESCAPE) paused = !paused; // Toggle pause while game is not yet over
				return true;
			}
		};		
		stage.getCamera().position.x = WORLD_WIDTH/2;
		stage.getCamera().position.y = WORLD_HEIGHT/2;
		stage.getCamera().update();
		
		/* Create a background for the stage */
		
		TextureActor background = new TextureActor(game.texture(BACKGROUND));
		background.setPosition(0,0);
		background.setSize(GRID_COLUMNS*GRID_STEP,GRID_ROWS*GRID_STEP);
		stage.addActor(background);
		
		/* Create a fishtank for the stage */
		
		TextureActor fishtank = new TextureActor(game.texture(FISHTANK));
		fishtank.setPosition(0,0);
		fishtank.setSize(GRID_COLUMNS*GRID_STEP,EXIT_ROW*GRID_STEP);
		stage.addActor(fishtank);
		
		/* Create a dogfish */
		
		dogfish = new Dogfish(game.texture(DOGFISH));
		stage.addActor(dogfish);
		dogfish.addToMap();
		
		/* Create Nate */
		
		nate = new Nate(game.texture(NATE));
		nate.setGridPosition(7,5);
		stage.addActor(nate);
		stage.setKeyboardFocus(nate);
		nate.addToMap();
		
		/* Create patrons */
		
		numberOfPatrons = GRID_COLUMNS;
		patrons = new Patron[numberOfPatrons];
		for (int i = 0; i < numberOfPatrons; i++) patrons[i] = new Patron(game.texture(ALICE));
		
		/* Create Matthew */
		
		matthew = new Matthew(game.texture(MATTHEW));
		stage.addActor(matthew);
		matthew.addToMap();
		
		/* Set timers and timing */
		
		newPatronInterval = 2f;
		elapsedTime = newPatronInterval; // Add a new patron immediately
		
		/* Initialize the score */
		
		score = 0;
		
		/* Create an actor to show the score */
		
		scoreKeeper = new TextActor(game.font(),game.fontShader(),"Score: 0");
		scoreKeeper.setWidth(WORLD_WIDTH/2);
		scoreKeeper.setHeight(GRID_STEP);
		scoreKeeper.setPosition(0f, WORLD_HEIGHT - GRID_STEP);
		stage.addActor(scoreKeeper);
		
		/* Create an actor to show the time */
		
		timeKeeper = new TimeKeeper(game.font(),game.fontShader(),60f);
		timeKeeper.setWidth(WORLD_WIDTH/2);
		timeKeeper.setHeight(GRID_STEP);
		timeKeeper.setPosition(WORLD_WIDTH/2, WORLD_HEIGHT - GRID_STEP);
		stage.addActor(timeKeeper);
		
	}
	
	// Methods

	@Override
	public void dispose() {stage.dispose();}
	
	private void goToScoreScreen() {
		
		TextureActor.clearMap();
		game.setScore(score);
		game.setScreen(new ScoreScreen(game));
		
	}
	
	@Override
	public void render(float delta) {

		if (!paused) {

			if (!over) {
				
				// Update actors' information
				
				matthew.getNearestDefaultPatron(patrons);
				
				// Move actors
				
				stage.act(delta);
								
				// Add new patron
				
				elapsedTime += delta;
				if (elapsedTime > newPatronInterval) {
					for (Patron p: patrons) {
						if (p.isOffstage()) {
							p.reset();
							stage.addActor(p);
							break;
						}
					}
					elapsedTime = 0f;			
				}
				
				// Update score
				
				for (Patron p: patrons) {
					score += p.incrementScore();
				}
				scoreKeeper.setText("Score: " + score);
				scoreKeeper.align();
				
				// Check state of Matthew & dogfish
				
				if (dogfish.isSwimming()) {			
					if (matthew.isAdjacentTo(dogfish)) {
						matthew.setEscaping(true);
						dogfish.setSwimming(false);
					}
				}
				
				// Check for game over
				
				if (!over) {
					boolean escape = !dogfish.isSwimming() && matthew.getRow() == GRID_ROWS - 1;
					boolean capture = !dogfish.isSwimming() && nate.isAdjacentTo(matthew);
					over = escape || capture || timeKeeper.timeExpired();
					if (over) {
						game.setLostDogfish(escape);
						elapsedTime = 0f;
					}
				}

			} else {
				
				/* Breifly freeze the screen when the game ends,
				 * then go to the score screen. */
								
				elapsedTime += delta;
				if (elapsedTime > 2f) goToScoreScreen();
				
			}

		}
		
        // Clear screen (black)
        
        Gdx.gl.glClearColor(0,0,0,0);
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
