package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

import john.aquariumassault.Constants;

public class TextActor extends Actor implements Constants {

	// Fields
	
	private static final float TOLERANCE = 0.01f;
	
	private BitmapFont font;
	private String     text;
	private float      drawX, drawY;
	
	// Constructors
	
	public TextActor(BitmapFont font, String text) {
		
		super();
		this.font = font;
		this.text = text;
		drawX = 0f;
		drawY = 0f;
		
	}
	
	// Methods

	public void centerHorizontally() {
		
		GlyphLayout l = new GlyphLayout(font,text);
		drawX = getX() + 0.5f*(getWidth() - l.width);
		
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		font.draw(batch, text, drawX, drawY);		
		
	}

	public void setFont(BitmapFont font) {this.font = font;}

	@Override
	public void setPosition(float x, float y) {
		
		/* We need to override the setPosition and setY methods because
		 * The BitmapFont draw method draws text below the vertical
		 * coordinate argument.  Batch draw methods draw above the
		 * vertical coordinate argument.  This way, TextActors and 
		 * TextureActors can be positioned in a consistent way. */
		
		super.setPosition(x,y);
		GlyphLayout l = new GlyphLayout(font,text);
		drawY = y + 0.5f*(getHeight() + l.height);
		
	}

	@Override
	public void setY(float y) {
		
		/* We need to override the setPosition and setY methods because
		 * The BitmapFont draw method draws text below the vertical
		 * coordinate argument.  Batch draw methods draw above the
		 * vertical coordinate argument.  This way, TextActors and 
		 * TextureActors can be positioned in a consistent way. */
		
		super.setY(y);
		GlyphLayout l = new GlyphLayout(font,text);
		drawY = y + 0.5f*(getHeight() + l.height);
		
	}

	public void setText(String text) {this.text = text;}

	
}
