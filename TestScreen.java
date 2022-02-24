package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import john.aquariumassault.actors.TextActor;
import john.aquariumassault.actors.TextureActor;

public class TestScreen extends ScreenAdapter implements Constants {

	// Fields
	
	private AquariumAssault game;
	private Stage           stage;
	//private TextureActor    nate, alice;
	private TextActor       text1, text2;
	
	// Constructor
	
	public TestScreen(AquariumAssault game) {
		
		this.game = game;
		
		// Stage
		
		stage = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT));
		stage.getCamera().position.x = 0.5f*WORLD_WIDTH;
		stage.getCamera().position.y = 0.5f*WORLD_HEIGHT;
		stage.getCamera().update();
		
		// Nate
		
		//nate = new TextureActor(game.texture(NATE));
		//nate.setGridPosition(0,0);

		// Text 1
		
		text1 = new TextActor(game.font(),game.fontShader(),"Text 1");
		text1.setHeight(GRID_STEP);
		text1.setPosition(GRID_STEP,WORLD_HEIGHT - 2*GRID_STEP);
		text1.setColor(0f,1f,0f,1f);
				
		// Alice
		
		//alice = new TextureActor(game.texture(ALICE));
		//alice.setGridPosition(0,GRID_COLUMNS - 1);

		// Text 2
		
		text2 = new TextActor(game.font(),game.fontShader(),"Text 2");
		text2.setHeight(GRID_STEP);
		text2.setPosition(GRID_STEP,WORLD_HEIGHT - 4*GRID_STEP);
		text2.setColor(1f,0f,0f,1f);
		
		// Add actors to stage
		
		//stage.addActor(nate);
		stage.addActor(text1);
		//stage.addActor(alice);
		stage.addActor(text2);
		
	}
	
	// Methods
	
	@Override
	public void dispose() {}
	
	@Override
	public void render(float delta) {
		
		stage.act(delta);
		
		// Rendering

        Gdx.gl.glClearColor(1f,1f,1f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		stage.draw();
		
	}

}
