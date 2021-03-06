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
package com.dit599.customPD.levels.painters;

import com.dit599.customPD.Dungeon;
import com.dit599.customPD.actors.blobs.WaterOfHealth;
import com.dit599.customPD.actors.blobs.WellWater;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.Terrain;
import com.watabou.utils.Point;
/**
 * Paints a room containing a well of health.
 */
public class HealthWellPainter extends Painter {

	@SuppressWarnings("unchecked")
	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );

		Point c = room.center();
		set( level, c.x, c.y, Terrain.WELL );
		//set( level, c.x, c.y-1, Terrain.SIGN );

		Class<? extends WellWater> waterClass = WaterOfHealth.class;
		WellWater water = (WellWater)level.blobs.get( waterClass);
		if (water == null) {
			try {
				water = waterClass.newInstance();
			} catch (Exception e) {
				water = null;
			}
		}
		water.seed( c.x + Level.WIDTH * c.y, 1 );
		level.blobs.put( waterClass, water );

		if(Dungeon.template == null){
			int pos;
			do {
				pos = room.random();
			} while (
					level.map[pos] == Terrain.WELL);
			set(level, pos, Terrain.SIGN);
		}
		room.entrance().set( Room.Door.Type.REGULAR );
	}
	/**
	 * Returns the string to display on a sign found in this room type.
	 */
	public static String tip() {
		return "This is a well of health, drink it to heal yourself, or throw in your dew vial (found on tutorial floor 1) to fully charge it!";
	}
	/**
	 * Returns the string to display on the prompt that appears when entering this room.
	 */
	public static String prompt() {
		return "Dew Vial Charging Room\n\n " +
				"This is a well of health, drink it to heal yourself, or throw in your dew vial (found on tutorial floor 1) to fully charge it!";
	}
}
