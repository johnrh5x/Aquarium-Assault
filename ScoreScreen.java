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
	
	private static final String[] TITLES = {"Vitamin H",
		                                    "Alabaster Titan",
		                                    "Me, Blorko!",
		                                    "Sir Anthony Hopkins",
		                                    "Electricity Cop",
		                                    "Tobias Beckford"};
		                                  
	private static final int[] CUTOFFS = {1667,1333,1000,667,333,0};
	
	private AquariumAssault game;
	private Stage           stage;
	private TextActor[]     line;
	private int             index;
	private boolean         inputFlag = false;
	private float           elapsedTime = 0f;
	
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
			for (int i = 0; i < line.length; i++) {
				line[i].setColor(Color.RED);
				line[i].toggleTyping();
			}
			stage.addActor(line[0]);
			index = 0;
		} else {
			line = new TextActor[3 + TITLES.length];
			line[0] = new TextActor(game.font(),game.fontShader(),"Your score of");
			line[1] = new TextActor(game.font(),game.fontShader(),String.valueOf(game.getScore()));
			line[2] = new TextActor(game.font(),game.fontShader(),"earns you the title of:");
			for (int i = 0; i < 3; i++) line[i].setColor(Color.RED);
			for (int i = 3; i < line.length; i++) {
				line[i] = new TextActor(game.font(),game.fontShader(),TITLES[i-3]);
				line[i].setColor(Color.BLUE);
			}
			index = TITLES.length - 1;
			line[index + 3].setColor(Color.RED);
			for (int i = 0; i < line.length; i++) stage.addActor(line[i]);
		}
		float dy = WORLD_HEIGHT/(line.length + 1f);
		for (int i = 0; i < line.length; i++) {
			line[i].setSize(WORLD_WIDTH,GRID_STEP);
			line[i].setScale(0.5f);
			line[i].setPosition(0f, WORLD_HEIGHT - (i + 1)*dy - 0.5f*GRID_STEP);
		}
		
	}
	
	// Methods

	@Override
	public void dispose() {stage.dispose();}

	private void nextScreen() {
	
		if (inputFlag) {
			game.setScore(0);
			game.setLostDogfish(false);
			game.setScreen(new CreditsScreen(game));
		} else {
			inputFlag = true;
			if (game.lostDogfish()) {
				for (TextActor t: line) {
					t.toggleTyping();
					if (t.getStage() == null) stage.addActor(t);
				}
			} else {
				line[index + 3].setColor(Color.BLUE);
				while (game.getScore() > CUTOFFS[index] && index > 0) index--;
				line[index + 3].setColor(Color.RED);
			}
		}
		
	}

	@Override
	public void render(float delta) {
		
		stage.act(delta);
		if (game.lostDogfish()) {
			if (line[index].finishedTyping()) {
				if (index == line.length - 1) {
					inputFlag = true;
				} else {
					stage.addActor(line[++index]);
				}
			}
		} else {
			if (!inputFlag) {
				elapsedTime += delta;
				if (elapsedTime > 0.5f && game.getScore() > CUTOFFS[index]) {
					line[index + 3].setColor(Color.BLUE);
					index--;
					line[index + 3].setColor(Color.RED);
					elapsedTime = 0f;
					inputFlag = game.getScore() <= CUTOFFS[index] || index == 0;
				}
			}
		}
		Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {stage.getViewport().update(width,height);}

	@Override
	public void show() {Gdx.input.setInputProcessor(stage);}

}
