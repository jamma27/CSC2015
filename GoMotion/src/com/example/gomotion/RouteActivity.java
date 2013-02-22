package com.example.gomotion;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class RouteActivity extends Activity
{

	private GoogleMap map;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		int id = getIntent().getIntExtra(ListCardioExercisesActivity.EXERCISE_ID, -1);
		
		if(id != -1)
		{		
			OfflineDatabase db = new OfflineDatabase(this);
			Cursor waypoints = db.getWaypoints(id);
			
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	
	        CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(new LatLng(55.016361, -1.425734), (float) 15);
	        map.moveCamera(cam);	
	        
	        PolylineOptions lineOptions = new PolylineOptions();
	        do {
	        	double lat = waypoints.getDouble(0);
	        	double lng = waypoints.getDouble(1);
	        	
	        	lineOptions.add(new LatLng(lat, lng));
	        }
	        while(waypoints.moveToNext());
	        
	        lineOptions.color(Color.argb(50, 0, 0, 255));
	
	        Polyline polyline = map.addPolyline(lineOptions);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
