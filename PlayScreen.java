package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;

import john.aquariumassault.actors.Dogfish;
import john.aquariumassault.actors.TextureActor;
import john.aquariumassault.actors.Nate;
import john.aquariumassault.actors.Patron;

public class PlayScreen extends ScreenAdapter implements Constants {

	// Fields
	
	private Texture[]     textures;
	private Stage         stage;
	private Nate          nate;
	private Dogfish       dogfish;
	private int           numberOfPatrons;
	private Array<Patron> patrons;
	private float         elapsedTime;
	private float         newPatronInterval;
	
	// Constructor(s)
	
	public PlayScreen(Texture[] textures) {
		
		this.textures = textures;
		
		/* Create a stage to hold the actors and center the camera in
		 * the middle of the stage. */
		
		stage = new Stage(new FitViewport(GRID_COLUMNS*GRID_STEP,GRID_ROWS*GRID_STEP));		
		stage.getCamera().position.x = GRID_COLUMNS*GRID_STEP/2;
		stage.getCamera().position.y = GRID_ROWS*GRID_STEP/2;
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
		patrons = new Array<Patron>(numberOfPatrons);
		for (int i = 0; i < numberOfPatrons; i++) patrons.add(new Patron(textures[ALICE]));
		
		/* Set a timer for the addition of a new patron */
		
		newPatronInterval = 2f;
		elapsedTime = newPatronInterval;
		
	}
	
	// Methods

	@Override
	public void dispose() {
	
		stage.dispose();
		
	}
	
	@Override
	public void render(float delta) {

		// Game logic
		
		Patron.setNatePosition(nate);
		stage.act(delta);
		elapsedTime += delta;
		if (elapsedTime > newPatronInterval) {
			
			/* Find the first patron in the array who's offstage and 
			 * add them to the stage. */
			 
			for (int i = 0; i < patrons.size; i++) {
				Patron p = patrons.get(i);
				if (p.isOffstage()) {
					p.reset();
					stage.addActor(p);
					break;
				}
			}
			elapsedTime = 0f;
			
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
	public void show() {
		
		Gdx.input.setInputProcessor(stage);
		
	}

}
