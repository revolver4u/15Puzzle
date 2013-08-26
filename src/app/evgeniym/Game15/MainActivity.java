package app.evgeniym.Game15;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;


public class MainActivity extends Activity implements OnClickListener {

	
	
	ImageButton NewGame, Settings, About, Exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        NewGame = (ImageButton) this.findViewById(R.id.button1);
        NewGame.setOnClickListener(this);
        Settings = (ImageButton) this.findViewById(R.id.button3);
        Settings.setOnClickListener(this);
        About = (ImageButton) this.findViewById(R.id.button4);
        About.setOnClickListener(this);
      /*  Exit = (ImageButton) this.findViewById(R.id.button5);
        Exit.setOnClickListener(this);*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.xml.prefs, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		switch (v.getId()){
		
		case R.id.button1:
			//CLASSIC GAME CASE
			Intent intent1 = new Intent();
            intent1.setClass(this, Main.class);
            startActivity(intent1);
            break;
		
		case R.id.button3:
			//SETTINGS
			Intent intent3 = new Intent();
			intent3.setClass(this, Prefs.class);
			startActivity(intent3);
			break;
		case R.id.button4:
			//ABOUT CASE
			break;
		}
		/*case R.id.button5:
			onBackPressed();
			break;
		}*/
	}
	public void onBackPressed() {
	     finish();
	}
	
	public void onPause(){
    	super.onPause();
    }
    
}
