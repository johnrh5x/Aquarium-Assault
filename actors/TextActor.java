package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

import john.aquariumassault.Constants;

public class TextActor extends Actor implements Constants {

	// Fields
	
	private BitmapFont font;
	private String     text;
	
	// Constructors
	
	public TextActor(BitmapFont font, String text) {
		
		super();
		this.font = font;
		this.text = text;
		
	}
	
	// Methods

	public void center(float x0, float x1, float y0, float y1) {
		
		/* Positions the text so that it is centered on both the
		 * horizontal interval [x0,x1] and the vertical interval
		 * [y0,y1].
		 * 
		 * Note that libGDX renders text with the origin at the upper
		 * left corner instead of at the lower left corner as in most
		 * other cases. */
		 
		GlyphLayout layout = new GlyphLayout(font,text);
		setX(x0 + 0.5f*(x1 - x0 - layout.width));
		setY(y1 - 0.5f*(y1 - y0 - layout.height));
		
	}

	public void centerHorizontally(float x0, float x1) {
		
		GlyphLayout layout = new GlyphLayout(font,text);
		setX(x0 + 0.5f*(x1 - x0 - layout.width));
		
	}

	public void centerVertically(float y0, float y1) {
		
		GlyphLayout layout = new GlyphLayout(font,text);
		setY(y1 - 0.5f*(y1 - y0 - layout.height));
		
	}

	public void setFont(BitmapFont font) {this.font = font;}

	public void setText(String text) {this.text = text;}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		font.draw(batch, text, getX(), getY());		
		
	}

}
