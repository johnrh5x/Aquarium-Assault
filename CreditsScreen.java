package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CreditsScreen extends ScreenAdapter {
	
	// Fields
	
	private static final String[][] TEXT = {{"Cast"},
		                                    {"Aquarium Patrons","Alice Bell"},
		                                    {"Mr. Basil Pesto", "Matthew Castle"},
		                                    {"Underwritten Game Character", "Nate Crowley"},
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
	private BitmapFont[]    font;
	private SpriteBatch     batch;
	private float[][]       x;
	private float[]         y;
	
	// Constructor
	
	public CreditsScreen(AquariumAssault game) {
		
		this.game = game;

		// Create a sprite batch
		
		batch = new SpriteBatch();
		
		// Create fonts
		
		font = new BitmapFont[2];
		font[RED] = new BitmapFont();
		font[RED].setColor(1f,0f,0f,1f);
		font[BLUE] = new BitmapFont();
		font[BLUE].setColor(0f,0f,1f,1f);
		
		// Layout
		
		layout();
		
		// Handle input
		
		Gdx.input.setInputProcessor(new InputAdapter() {
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
		}); 
		
	}
	
	// Methods
	
	@Override
	public void dispose() {
		
		font[RED].dispose();
		font[BLUE].dispose();
		batch.dispose();

	}
	
	private void layout() {
		
		x = new float[TEXT.length][];
		y = new float[TEXT.length];
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		float dv = height/(2f*TEXT.length + 1f); 
		float dh = 20f;
		float wmax = 0f;
		for (int i = 0; i < TEXT.length; i++) {
			float[] w = new float[TEXT[i].length];
			x[i] = new float[TEXT[i].length];
			float h = 0f;
			for (int j = 0; j < TEXT[i].length; j++) {
				GlyphLayout l = new GlyphLayout(font[COLOR[i]],TEXT[i][j]);
				w[j] = l.width;
				if (j > 0) {
					if (wmax < w[j]) wmax = w[j];
				}
				if (h < l.height) h = l.height;
				x[i][j] = 0.5f*(width - w[j]);
			}
			y[i] = height - (2*i + 1)*dv - 0.5f*(dv - h);
		}
		for (int i = 0; i < TEXT.length; i++) {
			if (TEXT[i].length == 2) {
				x[i][0] = dh;
				x[i][1] = width - dh - wmax;
			}
		}
		
	}
	
	private void nextScreen() {game.setScreen(new TitleScreen(game));}
	
	@Override
	public void render(float delta) {
        
        Gdx.gl.glClearColor(1,1,1,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        for (int i = 0; i < TEXT.length; i++) {
			for (int j = 0; j < TEXT[i].length; j++) {
				font[COLOR[i]].draw(batch,TEXT[i][j],x[i][j],y[i]);
			}
		}
		batch.end();
		
	}
	
}
