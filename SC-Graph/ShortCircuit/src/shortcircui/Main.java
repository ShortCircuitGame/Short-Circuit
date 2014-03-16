package shortcircui;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ShortCircuit";
		cfg.width = 1440;
		cfg.height = 960;
		
		new LwjglApplication(new ShortCircuit(), cfg);
	}
}
