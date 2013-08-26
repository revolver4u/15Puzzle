package app.evgeniym.Game15;
import android.content.Context;
/**
 * class to play background music
 * in the game, could be switched on/off
 * in the game settings menu
 */
import android.media.MediaPlayer;

public class BackMusic   {
	
	private static MediaPlayer player = null;

	/** Stop old song and start new one */
	public static void play(Context context, int resource) {
	stop(context);
	player = MediaPlayer.create(context, resource);
	player.setLooping(true);
	player.start();
	}
	
	/** Stop the music */
	public static void stop(Context context) {
	if (player != null) {
		player.stop();
		player.release();
		player = null;
	}
	}



}
