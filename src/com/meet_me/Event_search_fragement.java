package com.meet_me;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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

import com.meet_me.Register_login.LocationActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.app.ActionBarActivity;

public class Event_search_fragement extends ListFragment {
	// Progress Dialog
    private ProgressDialog pDialog;
    public HashMap<String, String> map=null;
    public int selectedItem;
    public JSONParser jsonParser = new JSONParser();
    public static ArrayList<HashMap<String, String>> eventList;
    public JSONArray event = null;
 //   public ListView event_list;
    public Register_login object=new Register_login();
   
    
    public static String eid="";
    private static final String EVENT_URL = "http://p2mu.net/meetme/meetme.pl";
    private static final String TAG_EVENT= "EVENTS";
    private static final String TAG_LOCATION = "event_location";
    private static final String TAG_ID = "event_id";
    private static final String USER_ID = "uid";
    private static final String TAG_NAME = "event_name";
    private static final String TAG_DISTANCE = "distance";
    private static final String POSITION = "position";
    private static final String TAG_USER_EVENT = "User";
    private static final String TAG_USER = "username";

/*    public static SharedPreferences my_pref_request;
	public static Editor editor_request;*/
    public ArrayList<HashMap<String, String>> userList;
    public String uid="";
    public String Username="";
    public String event_name="";
    public String event_location="";
    public String event_distance="";
    
    public static boolean reload_Event=true;
    
    public String cid="";
    public ImageView profile_P;
    public static ListAdapter UserAdapter;
	private LocationActivity gps;
	Meet_me meet_object;
	AlertDialog.Builder alertinternet;
	static ListAdapter Event_adapter;
	public static boolean is_Searched=false;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 setHasOptionsMenu(true);
	 return inflater.inflate(R.layout.event_search_fragment, container, false); 
}

@Override
public void onListItemClick(ListView l, View v, int position, long id) {
	// TODO Auto-generated method stub
	super.onListItemClick(l, v, position, id);
	Log.d("debugging", "Am here");
	
		 for(HashMap<String, String> element: eventList){
			 Log.d("for loop", "befor if");
               if(element.get(POSITION).equalsIgnoreCase(""+position)){
            	   eid=element.get(TAG_ID);
            	   event_name=element.get(TAG_NAME);
            	   event_location=element.get(TAG_LOCATION);
            	   event_distance=element.get(TAG_DISTANCE);
            	   Log.d("debugging", ""+eid+" "+event_name+" "+event_location);
            	   break;
               }
            }
		 AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
        	alertinternet.setTitle("Event");
		        alertinternet.setMessage("Event name: "+event_name+"\n"+"Event Location: "+event_location+"\n"+"Distance: "+event_distance+"\n\n"+"Fetch Users?");
		        alertinternet.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(object.isConnectingToInternet(getActivity().getApplicationContext())){
							new LoadUser().execute();
						}
						else{
		        		AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
		            	alertinternet.setTitle("Internet Status");
		    		    alertinternet.setMessage("Enable Internet Connection");
		    		    alertinternet.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
							}
						});
		    		    alertinternet.show();
					}
					}
				});
		        alertinternet.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
	
					}
				});
		        alertinternet.show();
}
@Override
public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onActivityCreated(savedInstanceState);
	meet_object=new Meet_me();
	alertinternet= new AlertDialog.Builder(getActivity());
	//meet_object.Selectiontab(0);
	if(reload_Event||Register_login.is_SignOut_event)
		Event_Auto();
	else{
		Log.d("check", "Checkxs");
		 Event_adapter = new SimpleAdapter(
                 getActivity(), eventList,
                 R.layout.event_search_list, new String[] { TAG_NAME, TAG_LOCATION},
                 new int[] { R.id.search_name, R.id.search_location });	
		setListAdapter(Event_adapter);
		if(Meet_me.actionbar.getSelectedNavigationIndex()!=0){
			Meet_me.Selectiontab(0);
		}
		
	}
	
	
}

public void Event_Auto(){
	//GetPref(getActivity().getApplicationContext());
    uid=Register_login.map.get("uid");
    pDialog= new ProgressDialog(getActivity());
    profile_P=new ImageView(getActivity());
    profile_P.setLayoutParams(new LayoutParams(100, 100));
    object= new Register_login();
    eventList = new ArrayList<HashMap<String, String>>();
    userList = new ArrayList<HashMap<String, String>>();
    LoadEvent_check();
 
}

public void LoadEvent_check(){
	gps=object.new LocationActivity(getActivity());
    if(object.isConnectingToInternet(getActivity().getApplicationContext())&&(!Register_login.no_permission)){
    	
    	if(gps.canGetLocation)
    		new LoadEvent().execute();
    	else{
    		gps.showSettingsAlert();
    	}
    }
    else if(Register_login.no_permission){
    	try{	
			Permission_event();
		}catch(Exception ex){
			ex.printStackTrace();
			}
    }
    else{
    	AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
    	alertinternet.setTitle("Internet Status");
			  
		        // Setting Dialog Message
		        alertinternet.setMessage("Oops! Events could not be fetched."+"\n"+"Enable Internet Connection and Refresh");
		        alertinternet.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		       
		        alertinternet.show();
    }
}
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		// TODO Auto-generated method stub
		
		inflater=this.getActivity().getMenuInflater();
		SupportMenu menu_=(SupportMenu) menu;
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem item = menu_.add(Menu.NONE, R.id.action_add, 200, "Add Event");
		item.setIcon(R.drawable.new_adding);	
		MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			
	}
@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
	if(!Register_login.profile_update){
	if(!Meet_me.is_open){
	switch (item.getItemId()) {
	
	case R.id.action_add:{
		Fragment fr=Fragment.instantiate(getActivity(), Event_adding.class.getName());
			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.content_frame, fr);
			transaction.commit();
		return true;
		}
	case R.id.action_refresh:{
		LoadEvent_check();
		return true;
				}
			}
		}
	}
	
	return super.onOptionsItemSelected(item);
			
	}

public void Permission_event(){
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
    alertDialog.setTitle("Permission");
    alertDialog.setMessage("Permit Application to use your Location details");

    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int which) {
        	Register_login.no_permission=false;
        }
    });
    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int which) {
        	Register_login.no_permission=true;
        }
    });
    
    alertDialog.show();
}

class LoadEvent extends AsyncTask<String, String, String> {
	double longitude=0.0;
	double latitude=0.0;
	 
    private boolean no_event=false;;

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Events....");
        pDialog.setIndeterminate(false);
        pDialog.show();
    }

    /**
     * getting Outbox JSON
     * */
    protected String doInBackground(String... args) {
    	

    	gps.getLocation();
      latitude =gps.latitude;
      longitude =gps.longitude; 
      
        HttpClient httpclient = new DefaultHttpClient();
  
        HttpPost httppost = new HttpPost(EVENT_URL);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "location"));
		Log.d("Event search", Register_login.map.get("uid"));
        params.add(new BasicNameValuePair("uid", Register_login.map.get("uid")));
        params.add(new BasicNameValuePair("long", ""+longitude));
        params.add(new BasicNameValuePair("lat", ""+latitude));
        try {
			httppost.setEntity(new UrlEncodedFormEntity(params));
		
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
       String responseBody;
	
		responseBody = httpclient.execute(httppost,responseHandler);
        Log.d("Event JSON: ", responseBody);
        if(!responseBody.contains("event_name")){
        	
        	no_event=true;
        }
        else{
        	no_event=false;
        	eventList=new ArrayList<HashMap<String,String>>();
        responseBody=responseBody.substring(responseBody.indexOf("{"));
        Log.d("debug", responseBody);
        responseBody="{ Events: ["+responseBody+"}";
        	JSONObject json=new JSONObject(responseBody);
            event = json.getJSONArray("Events");
            Log.d("Array",""+event);
            for (int i = 0; i < event.length(); i++) {
                JSONObject c = event.getJSONObject(i);
                String event_id = c.getString(TAG_ID);
                String event_name = c.getString(TAG_NAME);
                String event_location = c.getString(TAG_LOCATION);
                String distance = c.getString(TAG_DISTANCE);

                if(event_name.length() > 35){
                    event_name = event_name.substring(0, 50) + "..";
                }
                map = new HashMap<String, String>();
                map.put(POSITION, ""+i);
                map.put(TAG_ID, event_id);
                map.put(TAG_LOCATION, event_location);
                map.put(TAG_NAME, event_name);
                map.put(TAG_DISTANCE, distance);
                Log.d("in loog", ""+map);
                eventList.add(map);
            		} 
            Log.d("in loog", ""+eventList);
        		}
        	}
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
  
        pDialog.dismiss();
        if(no_event){ 	
        	alertinternet.setTitle("Events");
		        alertinternet.setMessage("No Event in this Location");
		        alertinternet.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
	
					}
				});
		       
		        alertinternet.show();
		       
        }
        else{
        
            		 Event_adapter = new SimpleAdapter(
                        getActivity(), eventList,
                        R.layout.event_search_list, new String[] { TAG_NAME, TAG_LOCATION},
                        new int[] { R.id.search_name, R.id.search_location});	
         		
            		setListAdapter(Event_adapter);
            		
            		reload_Event=false;
            		Register_login.is_SignOut_event=false;
            		
    }
        if(Meet_me.actionbar.getSelectedNavigationIndex()!=0){
			Meet_me.Selectiontab(0);
		}  
    }
}



/*//start here
public static Editor GetPref(Context context){
	my_pref_request=context.getSharedPreferences("Request",context.MODE_PRIVATE);
	editor_request=my_pref_request.edit();
	
	return editor_request;
}
*/

class ViewProfile extends AsyncTask<Void, String, String>{
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
            pDialog.setMessage("Loading User Profile ....");
            pDialog.setIndeterminate(false);
            pDialog.show();
	}
	
	@Override
	protected String doInBackground(Void... args) {
		 profile_P=new ImageView(getActivity());
	     profile_P.setLayoutParams(new LayoutParams(100, 100));
		 	String responseBody ="";
		 	HttpClient httpclient = new DefaultHttpClient();
   		
	   	        HttpPost httppost = new HttpPost("http://p2mu.net/meetme/profile_pix.pl");
	   			
	   			List<NameValuePair> params = new ArrayList<NameValuePair>();
	   			params.add(new BasicNameValuePair("action", "get_image"));
	               params.add(new BasicNameValuePair("uid", Register_login.map.get("uid")));
	               params.add(new BasicNameValuePair("cid", cid));
 	
	               try {  
	             
	          httppost.setEntity(new UrlEncodedFormEntity(params));
				
	           ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            responseBody = httpclient.execute(httppost,responseHandler);
	           
		 	Log.d("response_log", responseBody);
            downloadImage url= new downloadImage();
            Bitmap image=null;
            Log.d("usercid", ""+cid);
           image=url.DownloadImage("http://p2mu.net/meetme/images/"+responseBody);
       
            if(image!=null){
            	profile_P.setImageBitmap(image);
            }else{
            	profile_P.setImageResource(R.drawable.profile);
            	profile_P.setMaxHeight(100);
            	profile_P.setMaxHeight(100);
            }
            return "success";
               }
               catch(Exception e){
            	   e.printStackTrace();
               }
		return "failed";
	}
	
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		pDialog.cancel();
		AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
    	alertinternet.setTitle(Username);
		if(result.contains("success")){
    	
	   alertinternet.setView(profile_P);
    	//alertinternet.setMessage("Alert is showing");
	    alertinternet.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(cid!=Register_login.map.get("uid")){
					new SendRequest().execute();
				}
				else{
					AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
			    	alertinternet.setTitle("Request Status");
				    alertinternet.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
				    alertinternet.show();
				}
			}
		});
	    alertinternet.setNegativeButton("Back", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		}
		else{
			profile_P.setImageResource(R.drawable.profile);
        	profile_P.setMaxHeight(100);
        	profile_P.setMaxHeight(100);
			   alertinternet.setView(profile_P);
	        	//alertinternet.setMessage("Alert is showing");
			    alertinternet.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(cid!=Register_login.map.get("uid")){
							new SendRequest().execute();
						}
						else{
							AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
					    	alertinternet.setTitle("Request Status");
						    alertinternet.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							});
						    alertinternet.show();
						}
					}
				});
			    alertinternet.setNegativeButton("Back", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
		}
	    alertinternet.show();
		super.onPostExecute(result);

	}
}
class SendRequest extends AsyncTask<String, String, String>{

	@Override
	protected String doInBackground(String... args) {
		 	HttpClient httpclient = new DefaultHttpClient();
		 	String responseBody ="";
   	        HttpPost httppost = new HttpPost(EVENT_URL);
   			
   			List<NameValuePair> params = new ArrayList<NameValuePair>();
   			params.add(new BasicNameValuePair("action", "connect"));
               params.add(new BasicNameValuePair("uid", Register_login.map.get("uid")));
               params.add(new BasicNameValuePair("cid", cid));
               params.add(new BasicNameValuePair("eid", Event_search_fragement.eid));
               try {  
            httppost.setEntity(new UrlEncodedFormEntity(params));
			
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpclient.execute(httppost,responseHandler);
               }
               catch(Exception e){
            	   
               }
		return responseBody;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
            pDialog.setMessage("Sending Request ...");
            pDialog.setIndeterminate(false);
            pDialog.show();
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		pDialog.cancel();
		Log.d("before going into pref", "uid="+uid+"cid="+cid+"eid="+eid);
		super.onPostExecute(result);
	/*	if(!result.contains("previously")){
		 Integer i=0;
		 String loop=my_pref_request.getAll().toString();
		 Log.d("meet", ""+my_pref_request.getAll().size());
		 for(int k=0; k<=my_pref_request.getAll().size(); k++){
			 if(!loop.contains("ID"+k)){
				 i=k;
				 break;
			 }
		 }
		 Recent_fragment.map=null;
		 editor_request.putString("ID"+i, cid+";"+uid+";"+Event_search_fragement.eid+";"+Username+"*"+event_name);
		 editor_request.commit();
		}*/
		AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
    	alertinternet.setTitle("Request Status");
	    alertinternet.setMessage(result.substring(result.indexOf(":")+2));
	    alertinternet.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
	    alertinternet.show();
	}
}


class LoadUser extends AsyncTask<String, String, String> {

    private JSONArray user;
    private String responseBody="null";
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Users ...");
        pDialog.setIndeterminate(false);
        pDialog.show();
    }

    protected String doInBackground(String... args) {
    	
    	   HttpClient httpclient = new DefaultHttpClient();
    	
	        HttpPost httppost = new HttpPost(EVENT_URL);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("action", "join"));
			params.add(new BasicNameValuePair("uid", Register_login.map.get("uid")));
			Log.d("Event_uid", Register_login.map.get("uid")+" "+Event_search_fragement.eid);
			params.add(new BasicNameValuePair("eid", Event_search_fragement.eid));
           try {  
        	   httppost.setEntity(new UrlEncodedFormEntity(params));
		
        	   ResponseHandler<String> responseHandler = new BasicResponseHandler();
        	   responseBody = httpclient.execute(httppost,responseHandler);
		 return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
        }
          
        return responseBody;
    }
    protected void onPostExecute(String result) {

        pDialog.dismiss();
        try{
        if(!result.contains("username")){
			AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
        	alertinternet.setTitle("Users");
 
		        alertinternet.setMessage("No users in this Event");
		        alertinternet.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
	
					}
				});
		        alertinternet.show();
		     
		}
        else if(result.contains("null")){
        	AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
        	alertinternet.setTitle("Error");
 
		        alertinternet.setMessage("Users could not be fetched, Please check your internet connection");
		        alertinternet.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		        alertinternet.show();
        }
		else{ 
			result=result.substring(result.indexOf("{"));
            result="{ User: ["+result+"}";
            JSONObject json=new JSONObject(result);
            user = json.getJSONArray(TAG_USER_EVENT);
        	userList= new ArrayList<HashMap<String,String>>();
            for (int i = 0; i < user.length(); i++) {
                JSONObject c = user.getJSONObject(i);
                
                String id = c.getString(USER_ID);
                String user = c.getString(TAG_USER);               
                HashMap<String, String> map = new HashMap<String, String>();
                
                if(!(user.length()==0)){
                	Log.d("smae", "i Enter");
                map.put("POSITION", ""+i);
                map.put(USER_ID, id);
                map.put(TAG_USER, user);
               userList.add(map);
               }
            }
     
            if(!userList.isEmpty()){
           UserAdapter = new SimpleAdapter(getActivity(), userList,R.layout.activity_recent__activity_list, new String[] { TAG_USER },new int[] { R.id.recieved_username});
           Fragment newFragment = new User_fragment();
           
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			is_Searched=true;
			Meet_me.Selectiontab(1);
			transaction.replace(R.id.content_frame, newFragment);
			transaction.commit();
           	
    			/*selectedItem = -1;

    			alert_list.setAdapter(adapter , 
    			new DialogInterface.OnClickListener() {
    				
    			public void onClick(DialogInterface dialog, int item) {
    			selectedItem = item;
    				 
    				 for(HashMap<String, String> element: userList){
      	               if(element.get("POSITION").equalsIgnoreCase(""+selectedItem)){
      	            	   cid=element.get(USER_ID);
      	            	   Log.d("uid_from search", uid);
      	            	   Username=element.get(TAG_USER);
      	            	   Log.d("debugging", ""+Username);
      	            	   break;
      	               }
      	            }
    				 if(object.isConnectingToInternet(getActivity().getApplicationContext())){
    					 new ViewProfile().execute();			
    				 }
    				 else{
    					 object.Getalert("Error", "oops! please check your internet connection",getActivity());
    				 }
    			}
    			});*/
    			}
    			else{
    			       AlertDialog.Builder alert_list = new AlertDialog.Builder(getActivity());
    					alert_list.setTitle("Select User");
    				alert_list.setMessage("No User in this Event");
    				alert_list.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {	
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub	
						}
					});
    				AlertDialog alert = alert_list.create();
        			alert.show();  
    			}
    			
		}
 
        }
        catch(Exception e){
        	e.printStackTrace();
        }
    }
}
private class downloadImage {
    /** Called when the activity is first created. */
	HttpURLConnection httpConn=null;
   
    private InputStream OpenHttpConnection(String urlString) 
    throws IOException
    {
        InputStream in = null;
        int response = -1;
                
        URL url = new URL(urlString); 
        URLConnection conn = url.openConnection();
                  
        if (!(conn instanceof HttpURLConnection))                     
            throw new IOException("Not an HTTP connection");
         
        try{
        	httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect(); 
            Log.d("Httpconnection", "connection success");
            response = httpConn.getResponseCode();                 
            if (response == HttpURLConnection.HTTP_OK) {
            	 Log.d("Httpconnection", ""+response);
                in = httpConn.getInputStream();                                 
            }                     
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
           return null;         
        }
        return in;     
    }
    private Bitmap DownloadImage(String URL)
    {        
        Bitmap bitmap = null;
        InputStream in = null;        
        try {
            in = OpenHttpConnection(URL);
            bitmap = decodeFile(in, URL);
            in.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
        return bitmap;                
    }
    private Bitmap decodeFile(InputStream f, String urlstring){
    	int response=-1;
           try{
         
            BitmapFactory.Options option2 = new BitmapFactory.Options();
            option2.inSampleSize=8;
            
            
            Log.d("decode", "second_level");
            Bitmap bitmap=BitmapFactory.decodeStream(f, null, option2);
            Log.d("bitmap", ""+bitmap);
            return bitmap;
        }
        catch (Exception ex)
        {
           ex.printStackTrace();      
        }
       
        return null;
    }
}

public  int getContentViewCompat() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH?
               android.R.id.content : R.id.action_bar_activity_content;
}


}
