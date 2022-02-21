package john.aquariumassault.actors;

import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class TimeKeeper extends TextActor {

	// Fields
	
	private float   time, limit;
	private boolean countdown;

	// Constructor
	
	public TimeKeeper(DistanceFieldFont font, ShaderProgram shader) {
		
		/* Use this constructor when you want the timer to count up from
		 * zero. */
		
		super(font,shader,null);
		time = 0f;
		countdown = false;
		
	}
	
	public TimeKeeper(DistanceFieldFont font, ShaderProgram shader, float time) {
		
		/* Use this constructor when you want the timer to count down
		 * from the argument, which should be specified in seconds. */
		
		super(font,shader,null);
		this.time = time;
		int minutes = (int)Math.floor(time/60);
		int seconds = (int)Math.floor(time - minutes*60);
		StringBuilder sb = new StringBuilder("Time: ");
		sb.append(minutes);
		sb.append(":");
		if (seconds < 10) sb.append("0");
		sb.append(seconds);
		setText(sb.toString());
		countdown = true;
		
	}
	
	// Methods
	
	@Override
	public void act(float delta) {
		
		if (countdown) {
			time -= delta;
		} else {
			time += delta;
		}
		int minutes = (int)Math.floor(time/60);
		int seconds = (int)Math.floor(time - minutes*60);
		StringBuilder sb = new StringBuilder("Time: ");
		sb.append(minutes);
		sb.append(":");
		if (seconds < 10) sb.append("0");
		sb.append(seconds);
		setText(sb.toString());
		
	}

	public boolean timeExpired() {
		
		boolean output = false;
		if (countdown) output = time < 0f;
		return output;
		
	}

}
