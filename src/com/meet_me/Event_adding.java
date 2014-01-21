package com.meet_me;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.meet_me.Register_login.LocationActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract.EventsEntity;
import android.provider.Settings.Global;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Event_adding extends Fragment implements OnItemSelectedListener{
	
	//private ImageView add;
//	private ImageView back;
	private EditText event_name;
	private EditText event_hashtag;
	private Button startdate;
	private Button starttime;
	private Button enddate;
	private Button endtime;
	private Button broadcast;
	private TextView date_time;
	private TextView date_time1;
	public String LocationSelected;
	public Register_login object;
	DatePickerDialog.OnDateSetListener date;
	TimePickerDialog.OnTimeSetListener time;
	Calendar myCalendar = Calendar.getInstance();
	DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
	private boolean dateboolean=false;
	LocationActivity object2;
	private ProgressDialog pDialog;
	
/*	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_adding);
		

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.event_adding, menu);
		
		return true;
	}*/
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  // Inflate the layout for this fragment
		setHasOptionsMenu(true);
	  return inflater.inflate(R.layout.event_adding, container, false);
	  
	  
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		object=new Register_login();
		 pDialog= new ProgressDialog(getActivity());
		object2=object.new LocationActivity(getActivity());
		event_name=(EditText) getActivity().findViewById(R.id.event_name);
		event_hashtag=(EditText) getActivity().findViewById(R.id.event_hash);
		startdate=(Button) getActivity().findViewById(R.id.startdate);
		starttime=(Button) getActivity().findViewById(R.id.starttime);
		enddate=(Button) getActivity().findViewById(R.id.enddate);
		endtime=(Button) getActivity().findViewById(R.id.endtime);
		broadcast=(Button) getActivity().findViewById(R.id.bradcast);
		date_time=(TextView) getActivity().findViewById(R.id.lblDateAndTime);
		date_time1=(TextView) getActivity().findViewById(R.id.lblDateAndTime1);
		dateTimemethod();
		Adding_auto();
		
		
	}
	

@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
	inflater=this.getActivity().getMenuInflater();
	SupportMenu menu_=(SupportMenu) menu;
	super.onCreateOptionsMenu(menu_, inflater);
	inflater.inflate(R.menu.event_adding, menu_);
		
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.action_home:{
			Event_search_fragement.reload_Event=false;
			Fragment fr=Fragment.instantiate(getActivity(), Event_search_fragement.class.getName());
    		FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
    		transaction.replace(R.id.content_frame, fr);
    		transaction.commit();
			
			}
		
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	public void Adding_auto(){
	
	
		startdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dateboolean=true;
				new DatePickerDialog(getActivity(), date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		starttime.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		dateboolean=true;
		new TimePickerDialog(getActivity(), time, myCalendar
				.get(Calendar.HOUR_OF_DAY), myCalendar
				.get(Calendar.MINUTE), true).show();
		
	}
});
		enddate.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		dateboolean=false;
		new DatePickerDialog(getActivity(), date, myCalendar
				.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
				myCalendar.get(Calendar.DAY_OF_MONTH)).show();
		
	}
});
		endtime.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		dateboolean=false;
		new TimePickerDialog(getActivity(), time, myCalendar
				.get(Calendar.HOUR_OF_DAY), myCalendar
				.get(Calendar.MINUTE), true).show();
		
	}
});
		broadcast.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		
		Log.d("broadcast", "broadcast");
		if(object.isConnectingToInternet(getActivity().getApplicationContext())&&object2.canGetLocation){
			
			if(event_name.getText().toString()!=""&&date_time.getText()!=null&&date_time.getText()!=null)
					new  BroadcastEvent().execute(new String[]{"http://p2mu.net/meetme/meetme.pl"});
			else{
				object.Getalert("Error", "Field not set", getActivity());
			}
		}
		else if(!object2.canGetLocation){
			object2.showSettingsAlert();
		}
		else{
			object.Getalert("Error", "oops! please check your internet connection", getActivity());
		}
	}
});
		updateLabel();
	}

	public void dateTimemethod(){
		 date = new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, monthOfYear);
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
			}
			
			};

			 time = new TimePickerDialog.OnTimeSetListener() {
				
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				myCalendar.set(Calendar.MINUTE, minute);
				updateLabel();
			}

			
			};
		
	}
	
	private void updateLabel() {
		if(dateboolean){
		date_time.setText(fmtDateAndTime.format(myCalendar.getTime()));
		}
		else {
		date_time1.setText(fmtDateAndTime.format(myCalendar.getTime()));	
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> spinner, View arg1, int arg2,
			long arg3) {
	    
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private class BroadcastEvent extends AsyncTask<String, Void, String>{
		
		long diffHours=0;
		
		private double latitude;
		private double longitude;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//calculate the time difference
			super.onPreExecute();
			String dateStart =date_time.getText().toString();
			String dateStop =date_time1.getText().toString();      
				object2.getLocation();
                 latitude = object2.latitude;
                 longitude = object2.longitude;
                 Toast.makeText(getActivity(), "location :"+latitude+" "+longitude, Toast.LENGTH_LONG).show();
			Date d1 = null;
			Date d2 = null;
	 
			try {
				d1 = fmtDateAndTime.parse(dateStart);
				d2 = fmtDateAndTime.parse(dateStop);
	 
				//in milliseconds
				long diff = d2.getTime() - d1.getTime();
	 
				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				diffHours = diff / (60 * 60 * 1000) % 24;
				long diffDays = diff / (24 * 60 * 60 * 1000);
	 
				System.out.print(diffDays + " days, ");
				System.out.print(diffHours + " hours, ");
				System.out.print(diffMinutes + " minutes, ");
				System.out.print(diffSeconds + " seconds.");
	 
			} catch (Exception e) {
				e.printStackTrace();
			}
			 pDialog = new ProgressDialog(getActivity());
	         pDialog.setMessage("Adding Event...");
	         pDialog.setIndeterminate(false);
	         pDialog.show();

	          
	
		}
		@Override
		protected String doInBackground(String... url) {
		String Address=getAddress(longitude,latitude);

			 String responseBody=" ";
			 if(longitude!=0.0&&latitude!=0.0){
			try {

		        HttpClient httpclient = new DefaultHttpClient();
		  
		        HttpPost httppost = new HttpPost(url[0]);
		            Log.i(getClass().getSimpleName(), "send  task - start");
		            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		              nameValuePairs.add(new BasicNameValuePair("action", "add_event"));
		              nameValuePairs.add(new BasicNameValuePair("uid", Register_login.map.get("uid")));
		              nameValuePairs.add(new BasicNameValuePair("event_name",event_name.getText().toString() ));
		           //   nameValuePairs.add(new BasicNameValuePair("event_hashtag",event_hashtag.getText().toString() ));
		              nameValuePairs.add(new BasicNameValuePair("event_location", Address));
		              nameValuePairs.add(new BasicNameValuePair("lat", ""+latitude));
		              nameValuePairs.add(new BasicNameValuePair("long", ""+longitude));
		              nameValuePairs.add(new BasicNameValuePair("duration", ""+diffHours));
		              
		              httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		              ResponseHandler<String> responseHandler = new BasicResponseHandler();
		              responseBody = httpclient.execute(httppost,responseHandler);
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
			 }else{
				 return "lat_error";
			 }
		        	return responseBody;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
			pDialog.cancel();
			if(result.contains("200")){
        	alertinternet.setTitle("Event Status");

		        alertinternet.setMessage("Event Added");
		        alertinternet.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Event_search_fragement.reload_Event=true;
						Fragment fr=Fragment.instantiate(getActivity(), Event_search_fragement.class.getName());
						//	Fragment newFragment = new Event_search_fragement();
						FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
						transaction.replace(R.id.content_frame, fr);
						transaction.commit();
					}
				});
		}
			else if(result.contains("lat_error")){
				alertinternet.setTitle("Error");

		        alertinternet.setMessage("Your Location could not be fetched, Ensure GPS is enabled or Change position");
		        alertinternet.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
			}
		else{
			alertinternet.setTitle("Error");

	        alertinternet.setMessage("Unable to Add Event, Check GPS and Internet status");
	        alertinternet.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
		}
	        alertinternet.show();
	}
/*	public static JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
        	Log.d("Address", "checking");
        address = address.replaceAll(" ","%20");    

        HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
        Log.d("Address", "checking1");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
                Log.d("Address", "checking"+b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("json", ""+jsonObject);
        return jsonObject;
    }*/
	
	
	/*public static String[] getLatLong(JSONObject jsonObject) {
		
        try {
        	Log.d("Address", "checkinggettting");
          longitude   = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");
          Log.d("long", ""+longitude);
            latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");
            Log.d("lat",""+latitude);
            String[] location={""+latitude, ""+longitude};
            
            return location;
        } catch (JSONException e) {
            return null;

        }
    }*/
	}
/*public String GetAdress(double longitude, double latitude){
	 Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
	 String address=" ";
     try {
    	 List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

if(addresses != null) {
 Address returnedAddress = addresses.get(0);
 StringBuilder strReturnedAddress = new StringBuilder("Location: ");
 for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
  strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
 }
address=new String(strReturnedAddress);
}
else{
 return " ";
}
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
     return address;
}*/

public String getAddress(double longitude, double latitude) {
  
  String Address="";
    try {

        JSONObject jsonObj = Parser_Json.getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + ","
                + longitude + "&sensor=true");
        String Status = jsonObj.getString("status");
        if (Status.equalsIgnoreCase("OK")) {
            JSONArray Results = jsonObj.getJSONArray("results");
            JSONObject zero = Results.getJSONObject(0);
            JSONArray address_components = zero.getJSONArray("address_components");

            for (int i = 0; i < address_components.length(); i++) {
                JSONObject zero2 = address_components.getJSONObject(i);
                String long_name = zero2.getString("long_name");
                JSONArray mtypes = zero2.getJSONArray("types");
                String Type = mtypes.getString(0);

                if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                    if (Type.equalsIgnoreCase("street_number")) {
                        Address = long_name + " ";
                    } else if (Type.equalsIgnoreCase("sublocality")) {
                        Address =Address+ long_name+" ";
                    } else if (Type.equalsIgnoreCase("locality")) {
                        Address = Address + long_name + ", ";
                    } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                    	 Address = Address + long_name + ". ";
                    }
                }
            }
        }
        	return Address;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "Location";
}

}