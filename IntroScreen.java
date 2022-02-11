package john.aquariumassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class IntroScreen extends ScreenAdapter implements Constants {

	// Fields
	
	private static final String[][] TEXT = {{"This is you.","You work at the aquarium."},
		                                    {"This is an aquarium patron.","Patrons aren't fish,","so they all look alike to you."},
		                                    {"This is Mr. Basil Pesto.","He's a bad man:","tank tapper,","fish-napper,","and a thoroughly bad example."},
		                                    {"Your job is to:","greet patrons as they enter,","stop patrons from tapping on the tank, and ","watch out for Mr. Basil Pesto!"}};

	private static final int[] TEXTURES = {NATE,ALICE,MATTHEW,NATE};

	private static final float TEXTURE_WIDTH  = 60f;
	private static final float TEXTURE_HEIGHT = 60f;
	
	private static final float DURATION = 5f;
	
	private AquariumAssault game;
	private BitmapFont      font;
	private float[][]       x, y;
	private SpriteBatch     batch;
	private int             index = 0;
	private float           elapsedTime = 0f;
	
	// Constructor
	
	public IntroScreen(AquariumAssault game) {
		
		this.game = game;
		batch = new SpriteBatch();
		
		// Font
		
		font = new BitmapFont();
		font.setColor(0f,0f,0f,1f); // Black
		
		// Layout
		
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		x = new float[TEXT.length][];
		y = new float[TEXT.length][];
		for (int i = 0; i < TEXT.length; i++) {
			int size = TEXT[i].length + 1;
			x[i] = new float[size];
			y[i] = new float[size];
			float[] h = new float[TEXT[i].length];
			float total = 0f;
			for (int j = 0; j < TEXT[i].length; j++) {
				GlyphLayout l = new GlyphLayout(font,TEXT[i][j]);
				x[i][j] = 0.5f*(width - l.width);
				h[j] = l.height;
				total += l.height;
			}
			x[i][size - 1] = 0.5f*(width - TEXTURE_WIDTH);
			total += TEXTURE_HEIGHT;
			float dy = (height - total)/(size + 1);
			y[i][size - 1] = height - dy - TEXTURE_HEIGHT;
			for (int j = 0; j < TEXT[i].length; j++) {
				switch (j) {
					case 0:
						y[i][j] = y[i][size - 1] - dy;
						break;
					default:
						y[i][j] = y[i][j - 1] - h[j - 1] - dy;
				}
			}
		}
		
		// Make skippable
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keyCode) {
				skip();
				return true;
			}
			@Override
			public boolean touchDown(int x, int y, int p, int b) {
				skip();
				return true;
			}
		});
	}
	
	// Methods
	
	@Override
	public void dispose() {
	
		font.dispose();
		
	}
	
	private void nextScreen() {game.setScreen(new TitleScreen(game));}
	
	@Override
	public void render(float delta) {
		
		elapsedTime += delta;
		if (elapsedTime > DURATION) {
			index++;
			elapsedTime = 0f;
		}
        Gdx.gl.glClearColor(1,1,1,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (index < TEXT.length) {
			batch.begin();
			int end = TEXT[index].length;
			for (int i = 0; i < end; i++) {
				font.draw(batch,TEXT[index][i],x[index][i],y[index][i]);
			}
			batch.draw(game.texture(TEXTURES[index]),x[index][end],y[index][end],TEXTURE_WIDTH,TEXTURE_HEIGHT);
			batch.end();
		} else {
			nextScreen();
		}
		
	}

	private void skip() {
		
		if (index < TEXT.length - 1) {
			index ++;
		} else {
			nextScreen();
		}
		
	}

}
