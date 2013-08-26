package app.evgeniym.Game15;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
public class Prefs extends PreferenceActivity implements OnSharedPreferenceChangeListener {
@SuppressWarnings("deprecation")
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	addPreferencesFromResource(R.xml.prefs);
	}


@Override
public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
		String key) {
	// TODO Auto-generated method stub
	
}

}
