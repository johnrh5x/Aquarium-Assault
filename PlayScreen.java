package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;

import john.aquariumassault.actors.Nate;
import john.aquariumassault.actors.Patron;

public class PlayScreen extends ScreenAdapter implements Constants {

	// Fields
	
	private Texture[]     textures;
	private Stage         stage;
	private Nate          nate;
	private int           numberOfPatrons;
	private Array<Patron> patrons;
	private float         elapsedTime;
	private float         newPatronInterval;
	
	// Constructor(s)
	
	public PlayScreen(Texture[] textures) {
		
		this.textures = textures;
		
		/* Create a stage to hold the actors and center the camera in
		 * the middle of the stage. */
		
		stage = new Stage(new FitViewport(GRID_ROWS*GRID_STEP,GRID_COLUMNS*GRID_STEP));		
		stage.getCamera().position.x = GRID_COLUMNS*GRID_STEP/2;
		stage.getCamera().position.y = GRID_ROWS*GRID_STEP/2;
		stage.getCamera().update();
		
		/* Create Nate */
		
		nate = new Nate(textures[NATE]);
		nate.setGridPosition(5,5);
		stage.addActor(nate);
		stage.setKeyboardFocus(nate);
		
		/* Create patrons */
		
		numberOfPatrons = 10;
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
		for (Patron p: patrons) {

			/* If Nate is next to a descending patron, note that the
			 * patron has been intercepted. */
			
			if (p.isDescending()) p.setIntercepted(p.isAdjacentTo(nate));			
		
		}
		
        // Clear screen (white)
        
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Actual rendering
        
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
