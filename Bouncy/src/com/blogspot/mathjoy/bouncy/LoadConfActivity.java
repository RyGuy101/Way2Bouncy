package com.blogspot.mathjoy.bouncy;

import java.util.ArrayList;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LoadConfActivity extends Activity
{
	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;
	Spinner chooseConf;
	String[] confNames;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_conf);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		button = spool.load(this, R.raw.button, 1);
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		confNames = new String[sp.getInt("numOfConfs", 0)];
		int i2 = 0;
		for (int i = sp.getInt("numOfConfs", 0) - 1; i >= 0; i--)
		{
			confNames[i2] = sp.getString(i + "name", " ");
			i2++;
		}
		chooseConf = (Spinner) findViewById(R.id.chooseConf);
		if (sp.getInt("numOfConfs", 0) > 0)
		{
			ArrayAdapter<String> confNamesAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, confNames);
			chooseConf.setAdapter(confNamesAd);
		} else
		{
			ArrayAdapter<String> confNamesAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]
			{ "You have no saved configurations!" });
			chooseConf.setAdapter(confNamesAd);
		}
	}

	public void goToMainSettings(View v)
	{
		onBackPressed();
	}

	public void loadConf(View v)
	{
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		int n = -1;
		for (int i = 0; i < sp.getInt("numOfConfs", 0); i++)
		{
			if (chooseConf.getSelectedItem().equals(sp.getString(i + "name", " ")))
			{
				n = i;
			}
		}
		if (!sp.getString(n + "name", " ").equals(" "))
		{
			spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
			MyView.ballX = sp.getFloat(n + "startBallX", 0);
			MyView.ballY = sp.getFloat(n + "startBallY", 0);
			MyView.ballXSpeed = sp.getFloat(n + "startBallXSpeed", 0);
			MyView.ballYSpeed = sp.getFloat(n + "startBallYSpeed", 0);
			MyView.startBallX = sp.getFloat(n + "startBallX", 0);
			MyView.startBallY = sp.getFloat(n + "startBallY", 0);
			MyView.startBallXSpeed = sp.getFloat(n + "startBallXSpeed", 0);
			MyView.startBallYSpeed = sp.getFloat(n + "startBallYSpeed", 0);
			SavePrefs("gravityValue", sp.getInt(n + "gravityValue", 100));
			SavePrefs("bounceLevelValue", sp.getInt(n + "bounceLevelValue", 100));
			ArrayList<Platform> platforms = new ArrayList<Platform>();
			for (int i = 0; i < sp.getInt(n + "platformsSize", 0); i++)
			{
				platforms.add(new Platform(sp.getFloat(n + "platformStartX" + i, 0), sp.getFloat(n + "platformStartY" + i, 0), sp.getFloat(n + "platformEndX" + i, 0), sp.getFloat(n + "platformEndY" + i, 0)));
			}
			MyView.platforms = platforms;
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("fromLoad", true);
			startActivity(intent);
		}
	}

	@Override
	public void onBackPressed()
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		super.onBackPressed();
	}

	private void SavePrefs(String key, float value)
	{
		SharedPreferences sp = getSharedPreferences(MyMenu.settingsSP, 0);
		Editor edit = sp.edit();
		edit.putFloat(key, value);
		edit.commit();
	}
}