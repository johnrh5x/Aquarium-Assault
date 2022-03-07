package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import john.aquariumassault.actors.TextActor;
import john.aquariumassault.actors.TextureActor;

public class ScoreScreen extends ScreenAdapter implements Constants {

	// Fields
	
	private AquariumAssault game;
	private Stage           stage;
	private TextActor[]     line;
	private int             index;
	private boolean         inputFlag;
	
	// Constructor
	
	public ScoreScreen(AquariumAssault game) {
		
		super();
		this.game = game;
		stage = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT)) {
			@Override
			public boolean keyDown(int keycode) {
				nextScreen();
				return true;
			}
			@Override
			public boolean touchDown(int x, int y, int p, int b) {
				nextScreen();
				return true;
			}
		};
		stage.getCamera().position.x = 0.5f*WORLD_WIDTH;
		stage.getCamera().position.y = 0.5f*WORLD_HEIGHT;
		stage.getCamera().update();
		if (game.lostDogfish()) {
			line = new TextActor[5];
			line[0] = new TextActor(game.font(),game.fontShader(),"Mr. Basil Pesto escaped with the dogfish!");
			line[1] = new TextActor(game.font(),game.fontShader(),"You have been sacked.");
			line[2] = new TextActor(game.font(),game.fontShader(),"Cast out of the aquarium,");
			line[3] = new TextActor(game.font(),game.fontShader(),"you spend the rest of your life");
			line[4] = new TextActor(game.font(),game.fontShader(),"writing for obscure financial trade publications.");
			float dy = WORLD_HEIGHT/(line.length + 1f);
			for (int i = 0; i < line.length; i++) {
				line[i].setSize(WORLD_WIDTH,GRID_STEP);
				line[i].setScale(0.5f);
				line[i].setColor(Color.BLACK);
				line[i].setPosition(0f,WORLD_HEIGHT - (i + 1)*dy - 0.5f*line[i].getHeight());
				line[i].toggleTyping();
			}
		} else {
			line = new TextActor[2];
			line[0] = new TextActor(game.font(),game.fontShader(),"Your score is " + game.getScore() + ",");
			String title = "Vitamin H";
			if (game.getScore() < 0) {
				title = "Tobias Beckford";
			} else if (game.getScore() < 500) {
				title = "Electricity Cop";
			} else if (game.getScore() < 1000) {
				title = "Sir Antony Hopkins";
			} else if (game.getScore() < 1500) {
				title = "Alabaster  Titan";
			}
			line[1] = new TextActor(game.font(),game.fontShader(),"which earns you the title '" + title + "'.");
			for (int i = 0; i < line.length; i++) {
				line[i].setSize(WORLD_WIDTH,GRID_STEP);
				line[i].setScale(0.5f);
				line[i].setColor(Color.BLACK);
				line[i].toggleTyping();
			}
			line[0].setPosition(0f,0.5f*WORLD_HEIGHT);
			line[1].setPosition(0f,0.5f*WORLD_HEIGHT - line[1].getHeight());
		}
		index = 0;
		stage.addActor(line[0]);
		inputFlag = false;
		
	}
	
	// Methods

	@Override
	public void dispose() {stage.dispose();}

	@Override
	public void render(float delta) {
		
		stage.act(delta);
		if (line[index].finishedTyping()) {
			if (index == line.length - 1) {
				inputFlag = true;
			} else {
				stage.addActor(line[++index]);
			}
		}
		Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
		
	}

	private void nextScreen() {
	
		if (inputFlag) {
			game.setScore(0);
			game.setLostDogfish(false);
			game.setScreen(new CreditsScreen(game));
		} else {
			inputFlag = true;
			for (TextActor t: line) {
				t.toggleTyping();
				if (t.getStage() == null) stage.addActor(t);
			}
		}
		
	}

	@Override
	public void show() {Gdx.input.setInputProcessor(stage);}

}
