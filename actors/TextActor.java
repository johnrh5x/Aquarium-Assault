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
	
	// Constructor
	
	public TextActor(DistanceFieldFont font, ShaderProgram shader, String text) {
		
		this.font = font;
		this.shader = shader;
		this.text = text;
		horizontal = HorizontalAlignment.CENTER;
		vertical = VerticalAlignment.CENTER;
		
	}
	
	// Methods
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		font.setColor(getColor());
		font.getData().setScale(getScaleX());
		batch.setShader(shader);
		font.draw(batch,text,drawX,drawY);
		batch.setShader(null);
		font.getData().setScale(1f);
		font.setColor(Color.WHITE);

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
		System.out.println("Draw at x = " + drawX + ", y = " + drawY);
			
	}

	public void setHorizontalAlignment(HorizontalAlignment ha) {
		
		horizontal = ha;
		
	}

	@Override
	public void setPosition(float x, float y) {
		
		super.setPosition(x,y);
		align();
		
	}

	public void setText(String text) {this.text = text;}
	
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
	
}
