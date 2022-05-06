package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.GlyphLayout;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import john.aquariumassault.actors.TextActor;

public class CreditsScreen extends ScreenAdapter implements Constants {
	
	// Fields
	
	private static final String[][] TEXT = {{"Cast"},
		                                    {"You", "Nate Crowley"},
		                                    {"Aquarium Patrons","Alice Bell"},
		                                    {"Mr. Basil Pesto", "Matthew Castle"},
		                                    {"Questionable Design Decisions"},
		                                    {"John Harris"},
											{"Programming"},
											{"John Harris"},
											{"Portraits Yoinked From"},
											{"www.rockpapershotgun.com"},
											{"Additional Crude Artwork By"},
											{"John Harris"}};                         
	
	private static final int   RED  = 0;
	private static final int   BLUE  = 1;
	private static final int[] COLOR = {RED,BLUE,BLUE,BLUE,RED,BLUE,RED,BLUE,RED,BLUE,RED,BLUE};
	
	private AquariumAssault game;
	//private BitmapFont[]    font;
	//private SpriteBatch     batch;
	//private float[][]       x;
	//private float[]         y;
	private Stage         stage;
	private TextActor[][] text;	
	
	// Constructor
	
	public CreditsScreen(AquariumAssault game) {
		
		this.game = game;
		
		// Create a stage and configure it to handle input
		
		stage = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT)) {
			@Override
			public boolean keyDown(int keyCode) {
				nextScreen();
				return true;
			}
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				nextScreen();
				return true;
			}
		};
		
		// Create text actors
		
		text = new TextActor[TEXT.length][];
		for (int i = 0; i < text.length; i++) {
			text[i] = new TextActor[TEXT[i].length];
			for (int j = 0; j < text[i].length; j++) {
				text[i][j] = new TextActor(game.font(),game.fontShader(),TEXT[i][j]);
				switch (COLOR[i]) {
					case RED:  text[i][j].setColor(Color.RED);  break;
					case BLUE: text[i][j].setColor(Color.BLUE); break;
				}
				switch (text[i].length) {
					case 1:
						text[i][j].setSize(WORLD_WIDTH,GRID_STEP);
						text[i][j].setFontScale(0.5f);
						break;
					case 2:
						text[i][j].setSize(0.5f*WORLD_WIDTH,GRID_STEP);
						text[i][j].setFontScale(0.4f);
						switch (j) {
							case 0: text[i][j].setHorizontalAlignment(TextActor.HorizontalAlignment.LEFT);  break;
							case 1: text[i][j].setHorizontalAlignment(TextActor.HorizontalAlignment.RIGHT); break;
						}
						break;
				}
				stage.addActor(text[i][j]);
			}
		}
		
		// Layout
		
		float dv = WORLD_HEIGHT/(text.length + 1f);
		float dh = 20f; // Margin
		for (int i = 0; i < text.length; i++) {
			
			// Horizontal
			
			switch (text[i].length) {
				case 1:
					text[i][0].setX(0f);
					break;
				case 2:
					text[i][0].setX(dh);
					text[i][1].setX(WORLD_WIDTH - dh - text[i][1].getWidth());
					break;
			}
			
			// Vertical
			
			for (int j = 0; j < text[i].length; j++) {
				text[i][j].setY(WORLD_HEIGHT - (i + 1)*dv - 0.5f*text[i][j].getHeight());
			}
			
		}
		
	}
	
	// Methods
	
	@Override
	public void dispose() {stage.dispose();}
	
	private void nextScreen() {game.setScreen(new TitleScreen(game));}
	
	@Override
	public void render(float delta) {
        
        Gdx.gl.glClearColor(1,1,1,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		
	}
	
	@Override
	public void resize(int width, int height) {stage.getViewport().update(width,height);}

	
	@Override
	public void show() {Gdx.input.setInputProcessor(stage);}
	
}
