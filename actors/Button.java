package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Button extends Actor {

	// Fields
	
	private static final int   DEFAULT  = 0;
	private static final int   SELECTED = 1;
	private static final float INSET = 2;
	
	private DistanceFieldFont font;
	private ShaderProgram     shader;
	private String            text;
	private float             fontScale, dx, dy;
	private Color[]           color;
	private Texture           texture;
	
	// Constructor
	
	public Button(DistanceFieldFont font, ShaderProgram shader, String text) {
		
		super();
		this.font = font;
		this.shader = shader;
		this.text = text;
		color = new Color[2];
		color[DEFAULT] = Color.WHITE;
		color[SELECTED] = Color.RED;
		setColor(color[DEFAULT]);
		texture = null;
		final String finalText = text;
		addListener(new InputListener() {
			@Override
			public void enter(InputEvent e, float x, float y, int p, Actor a) {
				setColor(color[SELECTED]);
			}
			@Override
			public void exit(InputEvent e, float x, float y, int p, Actor a) {
				setColor(color[DEFAULT]);
			}
		});
	
	}
	
	// Methods
	
	public void addTexture(Texture texture) {this.texture = texture;}

	public static void centerHorizontally(float x0, float x1, Button[] buttons) {
		
		/* Centers an array of Buttons horizontally on the the
		 * interval (min{x0,x1},max{x0,x1}). */
		
		float w = x1 - x0;
		if (w < 0) w *= -1;
		for (Button b: buttons) b.setX(0.5f*(w - b.getWidth()));
		
	}

	public static void conformSizes(Button[] buttons) {
		
		float maxWidth = 0f;
		float maxHeight = 0f;
		for (Button b: buttons) {
			float w = b.getWidth();
			float h = b.getHeight();
			if (maxWidth < w) maxWidth = w;
			if (maxHeight < h) maxHeight = h;
		}
		for (Button b: buttons) {
			b.setSize(maxWidth,maxHeight);
		}
		
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		// Draw texture
		
		if (texture != null) batch.draw(texture,getX(),getY(),getWidth(),getHeight());
		
		// Draw text
		
		font.getData().setScale(fontScale);
		font.setColor(getColor());
		batch.setShader(shader);
		font.draw(batch, text, getX() + dx, getY() + dy);
		batch.setShader(null);
		font.getData().setScale(1f);
		font.setColor(Color.WHITE);
		
	}


	public void setFontScale(float scale) {
		
		fontScale = scale;
		font.getData().setScale(fontScale);
		GlyphLayout l = new GlyphLayout(font,text);
		font.getData().setScale(1f);
		setWidth(2*INSET + l.width);
		setHeight(2*INSET + l.height);
		dx = 0.5f*(getWidth() - l.width);
		dy = 0.5f*(getHeight() + l.height);

	}

	@Override
	public void setSize(float w, float h) {
		
		/* Modified to keep text centered. */
		
		super.setSize(w,h);
		font.getData().setScale(fontScale);
		GlyphLayout l = new GlyphLayout(font,text);
		dx = 0.5f*(w - l.width);
		dy = 0.5f*(h + l.height);
		font.getData().setScale(1f);
		
	}

}
