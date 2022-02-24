package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

import john.aquariumassault.Constants;

public class TextActor extends Actor implements Constants {

	// Enumerations
	
	public enum HorizontalAlignment {LEFT, CENTER, RIGHT};
	public enum VerticalAlignment   {TOP, CENTER, BOTTOM};
	
	// Fields
	
	private DistanceFieldFont   font;
	private ShaderProgram       shader;
	private String              text;
	private float               drawX, drawY;
	private HorizontalAlignment horizontal;
	private VerticalAlignment   vertical;
	private boolean             typing;         // If true, text appears one letter at a time when first drawn
	private float               typingInterval; // Number of seconds until next letter appears during typing 
	private int                 typingIndex;    // The index of the last letter typed
	private float               elapsedTime;    // Number of seconds since the last letter was typed
	
	// Constructor
	
	public TextActor(DistanceFieldFont font, ShaderProgram shader, String text) {
		
		this.font = font;
		this.shader = shader;
		this.text = text;
		horizontal = HorizontalAlignment.CENTER;
		vertical = VerticalAlignment.CENTER;
		typing = false;
		typingInterval = 0.125f;
		if (text != null) {
			typingIndex = text.length();
		} else {
			typingIndex = 0;
		}
		elapsedTime = 0f;
		
	}
	
	// Methods
	
	@Override
	public void act(float delta) {

		elapsedTime += delta;		
		if (typingIndex < text.length()) {
			if (elapsedTime > typingInterval) {
				typingIndex++;
				elapsedTime = 0f;
			}
		}
		
	}

	public void align() {
		
		/* BitmapFont draw methods draw text to the right of the x
		 * coordinate and below the y coordinate.  We override Actor's
		 * setX, setY, and setPosition methods to account for that
		 * fact and to make positioning TextActors consistent with the
		 * way that we position TextureActors. */
		
		font.getData().setScale(getScaleX());
		GlyphLayout l = new GlyphLayout(font,text);
		font.getData().setScale(1f);
		switch (horizontal) {
			case LEFT:
				drawX = getX();
				break;
			case CENTER:
				drawX = getX() + 0.5f*(getWidth() - l.width);
				break;
			case RIGHT:
				drawX = getX() + getWidth() - l.width;
				break;
		}
		switch (vertical) {
			case TOP:
				drawY = getY() + getHeight();
				break;
			case CENTER:
				drawY = getY() + 0.5f*(getHeight() + l.height);
				break;
			case BOTTOM:
				drawY = getY() + l.height;
				break;
		}
			
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		font.setColor(getColor());
		font.getData().setScale(getScaleX());
		batch.setShader(shader);
		font.draw(batch,text.substring(0,typingIndex),drawX,drawY);
		batch.setShader(null);
		font.getData().setScale(1f);
		font.setColor(Color.WHITE);

	}

	public boolean finishedTyping() {
		
		return typing && typingIndex == text.length() && elapsedTime > 2*typingInterval;
		
	}

	public void setHorizontalAlignment(HorizontalAlignment ha) {
		
		horizontal = ha;
		
	}

	@Override
	public void setPosition(float x, float y) {
		
		super.setPosition(x,y);
		align();
		
	}

	public void setText(String text) {
		
		this.text = text;
		if (typing) {
			typingIndex = 1;
			elapsedTime = 0f;
		} else {
			typingIndex = text.length();
		}
		
	}
	
	public void setVerticalAlignment(VerticalAlignment va) {
		
		vertical = va;
		
	}

	@Override
	public void setX(float x) {
		
		super.setX(x);
		align();
		
	}
	
	@Override
	public void setY(float y) {
		
		super.setY(y);
		align();
		
	}

	public void toggleTyping() {
		
		typing = !typing;
		if (typing) {
			typingIndex = 1;
			elapsedTime = 0f;
		} else {
			typingIndex =  text.length();
		}
		
	}
	
}
