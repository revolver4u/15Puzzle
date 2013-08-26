package app.evgeniym.Game15;

/**
 * This is the main game class that will draw
 * all the sprites at the surface view.
 * @author Evgeniy Mishustin
 */
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

public class Main extends Activity {
	private static final int SELECT_PHOTO = 100;
	private SharedPreferences prefs;
	public Bitmap mSelectedImage = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String s = prefs.getString("view", "classic");
		if(s.equalsIgnoreCase("custom"))
			{
			loadCamera();
			}
		else setContentView(new GameView(this, null));
    }
    
    private void loadCamera() {
    	Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
    	photoPickerIntent.setType("image/*");
    	startActivityForResult(photoPickerIntent, SELECT_PHOTO); 
    	
	}
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

        switch(requestCode) { 
        case SELECT_PHOTO:
            if(resultCode == RESULT_OK){  
                Uri selectedImage = imageReturnedIntent.getData();
                InputStream imageStream = null;
				try {
					imageStream = getContentResolver().openInputStream(selectedImage);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                 mSelectedImage = BitmapFactory.decodeStream(imageStream);
                 int width = mSelectedImage.getWidth();
                 int height = mSelectedImage.getHeight();
                 int newWidth = 480;
                 int newHeight = 480;
                 float scaleWidth = ((float) newWidth) / width;
                 float scaleHeight = ((float) newHeight) / height;
                 // create a matrix for the manipulation
                 Matrix matrix = new Matrix();
                 // resize the bit map
                 matrix.postScale(scaleWidth, scaleHeight);
                 Bitmap resizedBitmap = Bitmap.createBitmap(mSelectedImage, 0, 0,
                         width, height, matrix, true);
     			setContentView (new GameView(this, resizedBitmap));

            }
        }
    }
	@Override
    protected void onResume() {
		super.onResume();
		Boolean b = prefs.getBoolean("music", false);
		if(b)BackMusic.play(this, R.raw.instr);
    }
    @Override
    protected void onPause() {
    	super.onPause();
    	BackMusic.stop(this);
    }
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	BackMusic.stop(this);
    }

}