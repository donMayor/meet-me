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
import org.json.JSONObject;

import com.meet_me.Register_login.LocationActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.*;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MenuItemCompat;

public class User_fragment extends ListFragment{

	 private ProgressDialog pDialog;
	    public HashMap<String, String> map=null;
	    public int selectedItem;
	    public JSONParser jsonParser = new JSONParser();
	    public ArrayList<HashMap<String, String>> eventList;
	    public JSONArray event = null;
	//    public ListView user_listview;
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
	    public ArrayList<HashMap<String, String>> userList;
	    public String uid="";
	    public String Username="";
	    public String event_name="";
	    public String event_location="";
	    public String event_distance="";
	 
	    public String cid="";
	    public ImageView profile_P;
		private LocationActivity gps;
		Meet_me meet_object;
		
    
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  // Inflate the layout for this fragment
	setHasOptionsMenu(true);
	
  return inflater.inflate(R.layout.contact_fragment, container, false);
}

@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
	inflater=this.getActivity().getMenuInflater();
	SupportMenu menu_=(SupportMenu) menu;
	super.onCreateOptionsMenu(menu_, inflater);
	
	}

@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	if(!Register_login.profile_update){
		if(!Meet_me.is_open){
		switch (item.getItemId()) {
		case R.id.action_refresh:{		
			LoadUser_check();
			return true;
		}
		}
	}
	}
		return false;		
	}

public void LoadUser_check(){

    if(object.isConnectingToInternet(getActivity().getApplicationContext())){
    	new LoadUser().execute();
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
public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onActivityCreated(savedInstanceState);
	meet_object=new Meet_me();	
	Event_Auto();
}

@Override
public void onListItemClick(ListView l, View v, int position, long id) {
	// TODO Auto-generated method stub
	super.onListItemClick(l, v, position, id);
	HashMap<String, String> user_details= (HashMap<String, String>) Event_search_fragement.UserAdapter.getItem(position);
	Log.d("hashmap", ""+user_details);
	cid=user_details.get(USER_ID);
	Username=user_details.get(TAG_USER);
	if(object.isConnectingToInternet(getActivity().getApplicationContext())){
		
		AlertDialog.Builder alert_list= new AlertDialog.Builder(getActivity());
    	alert_list.setTitle("Option");
		alert_list.setMessage("View profile picture before sending request?");
		alert_list.setPositiveButton("View", new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new ViewProfile().execute();
			}
		});
		alert_list.setNeutralButton("Send Request", new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("send to my self", Register_login.map.get("uid")+"   "+cid);
				if(Register_login.map.get("uid")==cid){
					object.Getalert("Notification", "You cannot send Request to your self", getActivity());
				}else{
			new SendRequest().execute();
				}
			}
		});
	
		alert_list.show();
		
		 			
	 }
	 else{
		 object.Getalert("Error", "oops! please check your internet connection",getActivity());
	 }
}
public void Event_Auto(){
//	Event_search_fragement.GetPref(getActivity().getApplicationContext());
	
	if(Event_search_fragement.is_Searched){
	if(!Event_search_fragement.UserAdapter.isEmpty()){
	setListAdapter(Event_search_fragement.UserAdapter);
	
	
    uid=Register_login.map.get("uid");
    pDialog= new ProgressDialog(getActivity());
    profile_P=new ImageView(getActivity());
    profile_P.setLayoutParams(new LayoutParams(100, 100));
    object= new Register_login();

    gps=object.new LocationActivity(getActivity());
    eventList = new ArrayList<HashMap<String, String>>();
 //   user_listview=this.getListView();
    userList = new ArrayList<HashMap<String, String>>();

   
   
	}else{
		
		AlertDialog.Builder alert_list= new AlertDialog.Builder(getActivity());
    	alert_list.setTitle("Result");
		alert_list.setMessage("No User in this Event");
		alert_list.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Meet_me.Selectiontab(0);
				Fragment fr=Fragment.instantiate(getActivity(), Event_search_fragement.class.getName());
			//	Fragment newFragment = new Event_search_fragement();
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.content_frame, fr);
				transaction.commit();
			}
		});
		alert_list.show();
	}
	}else{
		AlertDialog.Builder alert_list= new AlertDialog.Builder(getActivity());
    	alert_list.setTitle("Result");
		alert_list.setMessage("Please join an Event to view Users");
		alert_list.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Meet_me.Selectiontab(0);
				Fragment fr=Fragment.instantiate(getActivity(), Event_search_fragement.class.getName());
				FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.content_frame, fr);
				transaction.commit();
				
				
			}
		});
		alert_list.show();
	}
}



class ViewProfile extends AsyncTask<Void, String, String>{
	
	String responseBody ="failed";
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
           responseBody=responseBody.substring(4);
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
				if(Register_login.map.get("uid")==cid){
					object.Getalert("Notification", "You cannot send Request to yourself", getActivity());
				}else{
				new SendRequest().execute();
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
						new SendRequest().execute();
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
               Log.d("event id", "evnet: "+Event_search_fragement.eid);
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
		 String loop=Event_search_fragement.my_pref_request.getAll().toString();
		 Log.d("meet", ""+Event_search_fragement.my_pref_request.getAll().size());
		 for(int k=0; k<=Event_search_fragement.my_pref_request.getAll().size(); k++){
			 if(!loop.contains("ID"+k)){
				 i=k;
				 break;
			 }
		 }
		 Recent_fragment.map=null;
		 Event_search_fragement.editor_request.putString("ID"+i, cid+";"+uid+";"+Event_search_fragement.eid+";"+Username+"*"+event_name);
		 Event_search_fragement.editor_request.commit();
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
            Event_search_fragement.UserAdapter = new SimpleAdapter(getActivity(), userList,R.layout.activity_recent__activity_list, new String[] { TAG_USER },new int[] { R.id.recieved_username});
            setListAdapter(Event_search_fragement.UserAdapter);
    			
    			}
    			else{
    				AlertDialog.Builder alert_list= new AlertDialog.Builder(getActivity());
    	        	alert_list.setTitle("Result");
    				alert_list.setMessage("No User in this Event");
    				alert_list.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {	
						@Override
						public void onClick(DialogInterface dialog, int which) {
						//	Fragment fr=Fragment.instantiate(getActivity(), Contact_fragment.class.getName());
							Fragment newFragment = new Event_search_fragement();
							FragmentTransaction transaction = getFragmentManager().beginTransaction();
							transaction.replace(getContentViewCompat(), newFragment);
							transaction.commit();
						}
					});
    				alert_list.show();
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
