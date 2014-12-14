/*
 * YourPD
 * Copyright (C) 2014 YourPD team
 * This is a modification of source code from: 
 * Pixel Dungeon
 * Copyright (C) 2012-2014 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
*/
package com.dit599.customPD.editorUI;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dit599.customPD.Dungeon;
import com.dit599.customPD.R;
import com.dit599.customPD.actors.hero.HeroClass;
import com.watabou.noosa.Game;

public class MapSelectorActivity extends Activity {
	public EditText edv=null;
	public Button bt=null;
	public LinearLayout mlayout=null;
	ArrayList<String> files;


	@Override  
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.mapselect);  
		if(Game.instance == null){
			finish();
		}
		else{
			edv=(EditText)findViewById(R.id.mapselectet);
			bt=(Button)this.findViewById(R.id.mapselectbut);
			files = new ArrayList<String>();

			for(String f : Game.instance.fileList()){
				if(f.endsWith(".map")){
					files.add(f.substring(0, f.length()-4));
				}
			}
			mlayout= (LinearLayout) findViewById(R.id.mapselectlinear);
			bt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					String temp = edv.getText().toString();
					if(files.size() < 10 && !temp.equals("") && !files.contains(temp)){
						Button but=new Button (MapSelectorActivity.this);
						but.setOnClickListener(getListener());
						but.setOnLongClickListener(getLongListener());
						but.setText(temp);
						mlayout.addView(but);
						files.add(temp);
						TemplateHandler.getInstance(temp);
					}}});
		}
	}   
	@Override
	public void onStart(){
		super.onStart();
		if(files == null){
			files = new ArrayList<String>();

			for(String f : Game.instance.fileList()){
				if(f.endsWith(".map")){
					files.add(f.substring(0, f.length()-4));
				}
			}
		}
		if(mlayout.getChildCount() == 0){
			for(String f : files){
				Button but=new Button (MapSelectorActivity.this);
				but.setOnClickListener(getListener());
				but.setOnLongClickListener(getLongListener());
				but.setText(f);
				mlayout.addView(but);
			}
		}
	}
	private OnClickListener getListener(){
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				String temp = ((Button)v).getText().toString();
				Intent intent = new Intent();  
				intent.setClass(MapSelectorActivity.this, MapEditActivity.class);  
				intent.putExtra(MapEditActivity.EXTRA_FILENAME, temp);
				Log.d("NAME PARAM", temp);
				startActivity(intent);  
			}};
	}
	private OnLongClickListener getLongListener(){
		return new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext())
				.setTitle("Delete Map")
				.setPositiveButton("Yes", deleteMap(v))
				.setNegativeButton("No", null);
				AlertDialog alert = builder.create(); // create one
				alert.show(); //display it 
				return true;
			}};
	}
	private DialogInterface.OnClickListener deleteMap(final View v){
		return new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface d, int i) {
				String temp = ((Button)v).getText().toString();
				files.remove(temp);
				mlayout.removeView(v);
				for(String f : Game.instance.fileList()){
					if(f.equals(temp + ".map")){
						Dungeon.template = (TemplateHandler.getInstance(temp))
								.getDungeon();
						Dungeon.deleteGame(HeroClass.WARRIOR, true);
						Dungeon.deleteGame(HeroClass.MAGE, true);
						Dungeon.deleteGame(HeroClass.ROGUE, true);
						Dungeon.deleteGame(HeroClass.HUNTRESS, true);
						Game.instance.deleteFile(f);
						Dungeon.template = null;
						break;
					}
				}
			}};
	}
}
