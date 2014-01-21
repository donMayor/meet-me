package com.meet_me;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;




import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;




import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;


public class Register_login extends ActionBarActivity{
	
/*	//Twitter declaration
    static String TWITTER_CONSUMER_KEY = "tC8jwC5VYWlTExdW0ff0yQ";
    static String TWITTER_CONSUMER_SECRET = "HjsNunT3Zr5JiCgPfP8ipt2KPrjRqsVtf4dHhL0BY";
   
    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
 
    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
 
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER="oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    private static Twitter twitter;
    private static RequestToken requestToken;*/
	
	
	/*public static SharedPreferences my_pref;
	public static Editor editor;*/
	ProgressDialog pDialog;
	
	public static double latitude;
	public static double longitude;
	public static HashMap<String, String> map;
	public static HashMap<String, String> tempmap;
	private ImageView FacebookButton;
	String facebook_id="";
	private boolean is_facebook=true;
	static boolean no_permission=true;
	public static boolean Load_from_Registration=true;
	GraphUser Guser=null;
	LocationActivity gps;
	//public ArrayList<HashMap<String, String>> userinfo;
	static boolean reload_sent_cache=true;
	static ListAdapter adapter;
	static boolean is_SignOut_event=true;
	static boolean is_SignOut_contact=true;
	static boolean is_SignOut_recent=true;
	public static boolean image_isset=true;
	
	static boolean profile_update=false;
	
	public static  Bitmap picture=null;
	public static boolean from_login=false;
	
	
    static ArrayList<HashMap<String, String>> userHistoryList=new ArrayList<HashMap<String,String>>();
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_login);
		FacebookButton=(ImageView) findViewById(R.id.facebookBuuton);
		
	//	twitterButton=(Button) findViewById(R.id.twitterButton);
		map= new HashMap<String, String>();
		tempmap= new HashMap<String, String>();
		
	//	Button signUpbutton=(Button) findViewById(R.id.registerButton);
	//	Button loginButton=(Button) findViewById(R.id.LoginButton);
		gps=new LocationActivity(Register_login.this);
		
		/*loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent sign_in= new Intent(getApplicationContext() ,Sing_in.class);
				startActivity(sign_in);
			}
		});
		signUpbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent facebook_twitter= new Intent(getApplicationContext(),Facebook_twitter_signup.class);
				startActivity(facebook_twitter);
			}
		});*/
		//Until emmeka makes it work..
		FacebookButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				Log.d("activesession", "b4getActive session");
				
				if(isConnectingToInternet(getApplicationContext())){
					Log.d("connection to internet", ""+isConnectingToInternet(getApplicationContext()));
					getActiveSession();
				}
				
			else {
				Getalert("Error", "oops! please check your internet connection");
			}
		
			}
		});
		
/*		twitterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				Log.d("activesession", "b4getActive session");
				
				if(isConnectingToInternet(getApplicationContext())){
					Log.d("connection to internet", ""+isConnectingToInternet(getApplicationContext()));
					
				}
				
			else {
				Getalert("Error", "oops! please check your internet connection");
			}
		
			}
		});*/
		Register_login.map.put("firstname", "");
        Register_login.map.put("uid","");
        Register_login.map.put("lastname", "");
        Register_login.map.put("username", "");
        Register_login.map.put("name","");	                    
        Register_login.map.put("personal_email",  "");
        Register_login.map.put("msisdn", "");
        Register_login.map.put("twitter_handle", "@twitter");
        Register_login.map.put("personal_website","");
        Register_login.map.put("org_email","");
        Register_login.map.put("org_website","");
        Register_login.map.put("org_address","");
        Register_login.map.put("org_name","");
        Register_login.map.put("facebook_username","");

	}
	
	public void Twitter(){
		is_facebook=false;
	}
	
	/* private void loginToTwitter() {
	        // Check if already logged in

	            ConfigurationBuilder builder = new ConfigurationBuilder();
	            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
	            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
	            Configuration configuration = builder.build();
	             
	            TwitterFactory factory = new TwitterFactory(configuration);
	            twitter = factory.getInstance();
	 
	            try {
	                requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
	                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
	                Uri uri = getIntent().getData();
	                if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
	                    // oAuth verifier
	                    String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
	         
	                    try {
	                        // Get the access token
	                        AccessToken accessToken = twitter.getOAuthAccessToken(
	                                requestToken, verifier);
	         
	                        // Shared Preferences
	                   //     Editor e = mSharedPreferences.edit();
	         
	                        // After getting access token, access token secret
	                        // store them in application preferences
	                        e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
	                        e.putString(PREF_KEY_OAUTH_SECRET,
	                                accessToken.getTokenSecret());
	                        // Store login status - true
	                        e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
	                        e.commit(); // save changes
	         
	                        Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
	         
	                        // Hide login button
	                         
	                        // Getting user details from twitter
	                        // For now i am getting his name only
	                        long userID = accessToken.getUserId();
	                        User user = twitter.showUser(userID);
	                        String username = user.getName();
	                        
	                    }
	                catch (Exception e) {
	                    // Check log for login errors
	                    Log.e("Twitter Login Error", "> " + e.getMessage());
	                }
	            } 
	            }catch (TwitterException e) {
	                e.printStackTrace();
	            }
	        
	 } 
	 
	 */
	
    public void Permission_event(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Register_login.this);
        alertDialog.setTitle("Permission");
        alertDialog.setMessage("Permit Application to use your Location details");
  
        // On pressing Settings button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {	
            	Intent intent= new Intent(getApplicationContext(), Meet_me.class);
            	startActivity(intent);
            	no_permission=false;
               
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            no_permission=true;
            Intent intent= new Intent(getApplicationContext(), Meet_me.class);
        	startActivity(intent);
            }
        });
        alertDialog.show();
    }
	public GraphUser getActiveSession(){
		
		is_facebook=true;
		Log.d("activesession", "Open Method");
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			
	        // callback when session changes state
			
	        @Override
	        public void call(Session session, SessionState state, Exception exception) {
	        	Log.d("activesession", "    "+session.isOpened());
	        	 pDialog= new ProgressDialog(Register_login.this);
	             pDialog.setMessage("Fetching Data ...");
	             pDialog.setIndeterminate(false);
	             pDialog.show();
	        	printHashKey();
	        	Log.d("App_id", ""+R.string.app_id);
	            
	          if (session.isOpened()) {
	            Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
	            //To put a dialog here
	            	
	              @Override
	              public void onCompleted(GraphUser user, Response response) {
	            	
	            	//  pDialog.cancel();
	            	  Log.d("completed", "Request");
	                if (user != null) {
	                	 Log.d("completed", user.getUsername());
	                  getUser(user);
	                  Guser=user;
	                  
	                  getFacebookSharedData(user.getFirstName(), user.getLastName(), user.getLink(), user.getUsername(), user.getId());	                  
	                  facebook_id=user.getId();
	                  httpdatacall data=new httpdatacall();
					  data.execute(new String[]{"http://p2mu.net/meetme/meetme.pl"});
	                  
	                  }
	                else{
	                	pDialog.cancel();
	                	Getalert("Error", "oops! please check your internet connection");
	                }
	                }
	              
	            });
	          }
	        }
	      });
   	 Log.d("Aftercompleted", "schecking");
		return Guser;
	}
	  @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }
	
	public GraphUser getUser(GraphUser user){
		return user;
	}
	public void getFacebookSharedData(String firstname, String Lastname, String facebook_url, String meet_username, String fbkid){
		
		
		Register_login.tempmap.put("firstname", firstname);
    	Register_login.tempmap.put("lastname", Lastname);
    	Register_login.tempmap.put("facebook_username", facebook_url);
    	Register_login.tempmap.put("fbkid", fbkid);
    	Register_login.tempmap.put("username", meet_username+".meet@me");

		} 
	  
	  public void printHashKey() {

	        try {
	            PackageInfo info = getPackageManager().getPackageInfo("com.meet_me",
	                    PackageManager.GET_SIGNATURES);
	            for (Signature signature : info.signatures) {
	                MessageDigest md = MessageDigest.getInstance("SHA");
	                md.update(signature.toByteArray());
	                Log.d("TEMPTAGHASH KEY:",
	                		
	                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	        } catch (NameNotFoundException e) {
	        	e.printStackTrace();
	        } catch (NoSuchAlgorithmException e) {
	        	e.printStackTrace();
	        }

	    }
	
	public boolean isConnectingToInternet(Context context){
		 ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
	}
	public void Getalert(String header, String content, Context context){
    	AlertDialog.Builder alertinternet= new AlertDialog.Builder(context);
    	alertinternet.setTitle(header);
		  
	        // Setting Dialog Message
	        alertinternet.setMessage(content);
	        alertinternet.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
	        alertinternet.show();
    }
	
	public void Getalert(String header, String content){
    	AlertDialog.Builder alertinternet= new AlertDialog.Builder(Register_login.this);
    	alertinternet.setTitle(header);
		  
	        // Setting Dialog Message
	        alertinternet.setMessage(content);
	        alertinternet.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
	        alertinternet.show();
    }
	
	public class LocationActivity extends Service implements LocationListener {
		private final Context mContext;
		 
	    // flag for GPS status
	    boolean isGPSEnabled = false;
	 
	    // flag for network status
	    boolean isNetworkEnabled = false;
	 
	    // flag for GPS status
	    boolean canGetLocation = false;
	 
	    Location location; // location
	     double  latitude; // latitude
	    double longitude; // longitude
	 
	    // The minimum distance to change Updates in meters
	    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000*1000*1000; // 10 meters
	 
	    // The minimum time between updates in milliseconds
	    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 *3; // 24 hours
	 
	    // Declaring a Location Manager
	    protected LocationManager locationManager;

	
	 
	    public LocationActivity(Context context) {
	        this.mContext = context;
	      
	        getLocation();
	       
	    }
	 
	    public Location getLocation() {
	        try {
	            locationManager = (LocationManager) mContext
	                    .getSystemService(LOCATION_SERVICE);
	 
	            // getting GPS status
	            isGPSEnabled = locationManager
	                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
	 
	            // getting network status
	            isNetworkEnabled = locationManager
	                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	 
	            if (!isGPSEnabled) {
	            	
	                showSettingsAlert();
	            } else {
	                this.canGetLocation = true;
	                if (isGPSEnabled) {
	                    if (location == null) {
	                        locationManager.requestLocationUpdates(
	                                LocationManager.GPS_PROVIDER,
	                                MIN_TIME_BW_UPDATES,
	                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                        Log.d("GPS Enabled", "GPS Enabled");
	                    
	                        if (locationManager != null) {
	                        Log.d("Location Manager", "Maneger Locatio ");	
	                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                      
	                            Log.d("Location", "not gotten");
	                            if (location != null) {
	                            	Log.d("Location itself", "ehen location");
	                                latitude = location.getLatitude();
	                                longitude = location.getLongitude();
	                                Log.d("Location", "gotten");
	                     
	                            }
	                        }
	                    }
	                }
	            }
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 
	        return location;
	    }
	     
	    public double getLatitude(){
	        if(location != null){
	            latitude = location.getLatitude();
	        }
	         
	        // return latitude
	        return latitude;
	    }
	     
	    /**
	     * Function to get longitude
	     * */
	    public double getLongitude(){
	        if(location != null){
	            longitude = location.getLongitude();
	        }
	         
	        // return longitude
	        return longitude;
	    }
	     
	    /**
	     * Function to check GPS/wifi enabled
	     * @return boolean
	     * */
	    public boolean canGetLocation() {
	        return this.canGetLocation;
	    }
	     
	    /**
	     * Function to show settings alert dialog
	     * On pressing Settings button will lauch Settings Options
	     * */
	    
	    public void showSettingsAlert(){
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
	      
	        // Setting Dialog Title
	        alertDialog.setTitle("GPS is settings");
	  
	        // Setting Dialog Message
	        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
	  
	        // On pressing Settings button
	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                mContext.startActivity(intent);
	                no_permission=false;
	            }
	        });
	  
	        // on pressing cancel button
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	            }
	        });
	  
	        // Showing Alert Message
	        alertDialog.show();
	    }
	    
	/*    public void turnGPSOn()
	    {
	         Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
	         intent.putExtra("enabled", true);
	         this.mContext.sendBroadcast(intent);

	        String provider = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	        if(!provider.contains("gps"))
	            { 
	            //if gps is disabled
	            final Intent poke = new Intent();
	            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	            poke.setData(Uri.parse("3")); 
	            this.mContext.sendBroadcast(poke);
	        }
	    }*/
	/*    public void Permission(){
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
	      
	        // Setting Dialog Title
	        alertDialog.setTitle("Permission");
	  
	        // Setting Dialog Message
	        alertDialog.setMessage("Permit Application to use your Location details");
	  
	        // On pressing Settings button
	        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	            	//if(!canGetLocation())
	            	//	turnGPSOn();
	            	no_permission=false;
	            }
	        });
	  
	        // on pressing cancel button
	        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            no_permission=true;
	            }
	        });
	  
	        // Showing Alert Message
	        alertDialog.show();
	    }*/
	    

	    @Override
	    public void onLocationChanged(Location location) {
	    }
	 
	    @Override
	    public void onProviderDisabled(String provider) {
	    }
	 
	    @Override
	    public void onProviderEnabled(String provider) {
	    }
	 
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    }
	 
	    @Override
	    public IBinder onBind(Intent arg0) {
	        return null;
	    }
	 
	}
	
	private class httpdatacall extends AsyncTask<String, Void, String>{
		
	
		private JSONArray info= null;
		private JSONObject c;
		
		@Override
		protected String doInBackground(String... url) {
			 String responseBody=" ";
			try {
		        HttpClient httpclient = new DefaultHttpClient();	  
		        HttpPost httppost = new HttpPost(url[0]);
		            Log.i(getClass().getSimpleName(), "send  task - start");
		            //
		            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
		            nameValuePairs.add(new BasicNameValuePair("action", "login"));
		            nameValuePairs.add(new BasicNameValuePair("name", Register_login.tempmap.get("firstname")+" "+Register_login.tempmap.get("lastname")));
		              nameValuePairs.add(new BasicNameValuePair("email"," "));
		              nameValuePairs.add(new BasicNameValuePair("fbkid", Register_login.tempmap.get("fbkid")));
		              nameValuePairs.add(new BasicNameValuePair("organisation", " "));
		              nameValuePairs.add(new BasicNameValuePair("website", " "));
		            nameValuePairs.add(new BasicNameValuePair("type", "facebook"));
		             
		              httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		              ResponseHandler<String> responseHandler = new BasicResponseHandler();
		             
		              responseBody = httpclient.execute(httppost,responseHandler);
		             
		              return responseBody;
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
		        	return "internet";
		}
		@Override
		protected void onPostExecute(String result) {
			pDialog.cancel();
			Log.d("json response", result);
			if(result.contains("exist")){
		     Log.d("exist....", result);
		     Getalert("Error", "User does not Exist, use the Register Option");
			}
			else if(result.contains("confirmed")){
				Log.d("confirm....", result);
			Getalert("Error", "Confirm your Registration from your Email Address");
			}
			else if(result.contains("bad")){
				Getalert("Error", "Wrong Password or Email");
			}
			else if(result.contains("internet")){
				Getalert("Error", "Check Internet Service");
			}
			else{
				try{
					Log.d("try", "In the try block");
	            result=result.substring(result.indexOf("{"));
	            result="{Info: ["+result+"]}";
	            	JSONObject json=new JSONObject(result);
	                info = json.getJSONArray("Info");
	                Log.d("Info", ""+info);
	                for (int i = 0; i < info.length(); i++) {
	                     c = info.getJSONObject(i);
	                     Register_login.map.put("uid", c.getString("id"));
	                     Register_login.map.put("firstname",Register_login.tempmap.get("firstname"));
	                     Register_login.map.put("lastname",Register_login.tempmap.get("lastname"));

	                     Register_login.map.put("facebook_username",Register_login.tempmap.get("facebook_username"));
	                    Register_login.map.put("name", c.getString("firstname")+" "+c.getString("lastname"));	                  
	                }
	                if(c.getString("username").length()<=1||c.getString("msisdn").length()!=10){
	                	from_login=true;
	                	Register_login.profile_update=true;
                		 if(Register_login.no_permission){
                     		Permission_event();
                     }
                		 else{
                			 Intent intent= new Intent(getApplicationContext(),Meet_me.class);
                     		startActivity(intent); 	
                		 }
/*	                	
	                	AlertDialog.Builder alertDialog = new AlertDialog.Builder(Register_login.this);
	         	        alertDialog.setTitle("Notification");
	         	        alertDialog.setMessage("Your Profile is empty, Update your profile now!");
	         	        Log.d("Profile empty", "Empty here");
	         	        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
							@Override
	         				public void onClick(DialogInterface dialog, int which) {
	         					// TODO Auto-generated method stub
	         					
		                    		
	         				}
	         			});
	         	       alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	         				
	         				@Override
	         				public void onClick(DialogInterface dialog, int which) {
	         					// TODO Auto-generated method stub
	         					Register_login.profile_update=false;
	         					try {
									Register_login.map.put("firstname", c.getString("firstname"));
								
	    	                    Register_login.map.put("uid",c.getString("id"));
	    	                    Register_login.map.put("lastname",  c.getString("lastname"));
	    	                    Register_login.map.put("username",  c.getString("username"));
	    	                    Register_login.map.put("name", c.getString("firstname")+" "+c.getString("lastname"));	                    //Register_login.map.put("shareparameter",  c.getString("share_parameter"));
	    	                    Register_login.map.put("personal_email",  c.getString("email"));
	    	                    Register_login.map.put("msisdn",  "0"+c.getString("msisdn"));
	    	                    Register_login.map.put("twitter_handle",  c.getString("twitter_handle"));
	    	                    Register_login.map.put("personal_website",  c.getString("website"));
	    	                    Register_login.map.put("org_email",  c.getString("org_email"));
	    	                    Register_login.map.put("org_website",  c.getString("org_website"));
	    	                    Register_login.map.put("org_address",  c.getString("org_address"));
	    	                    Register_login.map.put("org_name",  c.getString("organisation"));
	    	                 //   Register_login.map.put("facebook_username",  c.getString("facebook_username"));
	    	                    Register_login.map.put("facebook_username", c.getString("username"));
	    	                    Log.d("map", ""+Register_login.map);
	    	                   if(Register_login.no_permission){
    	                    		Permission_event();
	    	                   }

	         					} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	         				}
	         			});
	         	       alertDialog.show();*/
	                }
	                else{
	                	Log.d("Profile","Not Empty");
	                  	Register_login.profile_update=false;
	                  	Register_login.from_login=false;
    					try {
						Register_login.map.put("firstname", c.getString("firstname"));
	                    Register_login.map.put("uid",c.getString("id"));
	                    Register_login.map.put("lastname",  c.getString("lastname"));
	                    Register_login.map.put("username",  c.getString("username"));
	                    Register_login.map.put("name", c.getString("firstname")+" "+c.getString("lastname"));	                 
	                    Register_login.map.put("personal_email",  c.getString("email"));
	                    Register_login.map.put("msisdn",  "0"+c.getString("msisdn"));
	                    Register_login.map.put("twitter_handle",  c.getString("twitter_handle"));
	                    Register_login.map.put("personal_website",  c.getString("website"));
	                    Register_login.map.put("org_email",  c.getString("org_email"));
	                    Register_login.map.put("org_website",  c.getString("org_website"));
	                    Register_login.map.put("org_address",  c.getString("org_address"));
	                    Register_login.map.put("org_name",  c.getString("organisation"));
	                 
	                    Register_login.map.put("facebook_username", Register_login.tempmap.get("facebook_username"));
	                	
	                    if(Register_login.no_permission){
                    		Permission_event();
                    }else{
                    	Intent intent= new Intent(getApplicationContext(),Meet_me.class);
                		startActivity(intent); 	
                    }
    					} catch (JSONException e) {
							e.printStackTrace();
						}
	                }
				} 
	                catch (Exception e) {
					e.printStackTrace();
					}
	                		
			}
		}
		@Override
		protected void onPreExecute() {
			
			/*super.onPreExecute();   
            pDialog = new ProgressDialog(Register_login.this);
            pDialog.setMessage("Loading Profile ...");
            pDialog.setIndeterminate(false);
          //  pDialog.setCancelable(false);
            pDialog.show();*/
		}
	
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		System.runFinalizersOnExit(true);
		System.exit(0);
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_help:

	    		Getalert("Help", "1 Create an Event at Event Location\n2 Search for the Event within the Location\n3 Find Attendees in the Event\n4 Select Attendee to Exchange Business card",Register_login.this);
	    		return true;
	    	
		case R.id.action_about:
	    		Getalert("About", "Meet@me is an Application that helps you Exchange Bussiness card easily with Attendees in an Event\nDeveloper: iQube Lab ", Register_login.this);
	    		return true;		
		}
	
		return super.onOptionsItemSelected(item);
	}

}
