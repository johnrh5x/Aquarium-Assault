package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;

import john.aquariumassault.actors.Dogfish;
import john.aquariumassault.actors.Matthew;
import john.aquariumassault.actors.Nate;
import john.aquariumassault.actors.Patron;
import john.aquariumassault.actors.TextActor;
import john.aquariumassault.actors.TextureActor;

public class PlayScreen extends ScreenAdapter implements Constants {

	// Fields
	
	private Texture[]     textures;
	private Stage         stage;
	private Nate          nate;
	private Dogfish       dogfish;
	private int           numberOfPatrons;
	private Patron[]      patrons;
	private Matthew       matthew;
	private float         elapsedTime;
	private float         newPatronInterval;
	private int           score;
	private TextActor     scoreKeeper;
	
	// Constructor(s)
	
	public PlayScreen(Texture[] textures, BitmapFont font) {
		
		this.textures = textures;
		
		/* Create a stage to hold the actors and center the camera in
		 * the middle of the stage. */
		
		stage = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT));		
		stage.getCamera().position.x = WORLD_WIDTH/2;
		stage.getCamera().position.y = WORLD_HEIGHT/2;
		stage.getCamera().update();
		
		/* Create a background for the stage */
		
		TextureActor background = new TextureActor(textures[BACKGROUND]);
		background.setPosition(0,0);
		background.setSize(GRID_COLUMNS*GRID_STEP,GRID_ROWS*GRID_STEP);
		stage.addActor(background);
		
		/* Create a fishtank for the stage */
		
		TextureActor fishtank = new TextureActor(textures[FISHTANK]);
		fishtank.setPosition(0,0);
		fishtank.setSize(GRID_COLUMNS*GRID_STEP,EXIT_ROW*GRID_STEP);
		stage.addActor(fishtank);
		
		/* Create a dogfish */
		
		dogfish = new Dogfish(textures[DOGFISH]);
		stage.addActor(dogfish);
		
		/* Create Nate */
		
		nate = new Nate(textures[NATE]);
		nate.setGridPosition(7,5);
		stage.addActor(nate);
		stage.setKeyboardFocus(nate);
		
		/* Create patrons */
		
		numberOfPatrons = GRID_COLUMNS;
		patrons = new Patron[numberOfPatrons];
		for (int i = 0; i < numberOfPatrons; i++) patrons[i] = new Patron(textures[ALICE]);
		
		/* Create Matthew */
		
		matthew = new Matthew(textures[MATTHEW]);
		stage.addActor(matthew);
		
		/* Set a timer for the addition of a new patron */
		
		newPatronInterval = 2f;
		elapsedTime = newPatronInterval;
		
		/* Initialize the score */
		
		score = 0;
		
		/* Create an actor to show the score */
		
		scoreKeeper = new TextActor(font,"Score: 0");
		scoreKeeper.setPosition(0,(GRID_ROWS + 1)*GRID_STEP);
		stage.addActor(scoreKeeper);
		
	}
	
	// Methods

	@Override
	public void dispose() {stage.dispose();}
	
	@Override
	public void render(float delta) {

		// Game logic
		
		Patron.setMatthewPosition(matthew);
		Patron.setNatePosition(nate);
		Patron.setFishPosition(dogfish);
		nate.getValidMoves(matthew, patrons);
		matthew.getValidMoves(nate, patrons, dogfish);
		stage.act(delta);
		elapsedTime += delta;
		if (elapsedTime > newPatronInterval) {
			
			/* Find the first patron in the array who's offstage and 
			 * add them to the stage. */
			 
			for (Patron p: patrons) {
				if (p.isOffstage()) {
					p.reset();
					stage.addActor(p);
					break;
				}
			}
			elapsedTime = 0f;
			
		}
		for (Patron p: patrons) score += p.incrementScore();
		scoreKeeper.center(0,WORLD_WIDTH,GRID_ROWS*GRID_STEP,WORLD_HEIGHT);
		scoreKeeper.setText("Score: " + score);
		if (matthew.isAdjacentTo(dogfish)) {
			System.out.println("Matthew caught the dogfish.");
			Gdx.app.exit();
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
