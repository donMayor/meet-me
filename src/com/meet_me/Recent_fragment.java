package com.meet_me;



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
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MenuItemCompat;

public class Recent_fragment extends ListFragment{


	 	private ProgressDialog pDialog;
	    private boolean view_status;
	    public Register_login object;
	    JSONParser jsonParser = new JSONParser();
	    static HashMap<String, String> map = new HashMap<String, String>();
	    HashMap<String, String> maping = new HashMap<String, String>();
	    private ArrayList<HashMap<String, String>> userList;
	    private  ArrayList<HashMap<String, String>> recievedUserList=new ArrayList<HashMap<String,String>>();
	    private  ArrayList<HashMap<String, String>> sentUserList=new ArrayList<HashMap<String,String>>();
	    private boolean reload_sent=true;
	    
	    private boolean reload_recieved=true;
	    private static final String EVENT_URL = "http://p2mu.net/meetme/meetme.pl";
	    boolean Accept_decline;
	    String eid;
	    String uid;
	    String cid;
	    private static final String TAG_USER_EVENT = "User";
	    private static final String TAG_EVENT="event";
	    private static final String TAG_ID = "id";
	    private static final String TAG_DATE="date";
	    private static final String TAG_USER = "username";
	    private static ArrayList<String> UIDlist;
	    private static SharedPreferences recievedpref;
	    private static Editor recievededitor;    
		private boolean recieve_check=false;
		//ListAdapter Register_login.adapter;
		private Menu menu;
		
		
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  // Inflate the layout for this fragment
	setHasOptionsMenu(true);
	object=new Register_login();
  return inflater.inflate(R.layout.recent_fragment, container, false); 
}

@Override
public void onActivityCreated(Bundle savedInstanceState) {
super.onActivityCreated(savedInstanceState);
	Auto_Recent();
}

public void Auto_Recent(){
    object=new Register_login();
  //  Event_search_fragement.GetPref(getActivity().getApplicationContext());
    GetPref(getActivity().getApplicationContext());
    userList = new ArrayList<HashMap<String, String>>();
 //   map=(HashMap<String, String>)Event_search_fragement.my_pref_request.getAll();
    UIDlist= new ArrayList<String>();
    
    if(Register_login.is_SignOut_recent)
    	Recieved();
    else{
    	setListAdapter(Register_login.adapter);
    }
}


	
@Override
   public void onPrepareOptionsMenu(Menu menu) {
		this.menu=menu;
       this.menu.findItem(R.id.action_in).setVisible(recieve_check);
       this.menu.findItem(R.id.action_out).setVisible(!recieve_check);
   }
@Override
public void onListItemClick(ListView l, View v, int position, long id) {
// TODO Auto-generated method stub
super.onListItemClick(l, v, position, id);
	
	
	if(recieve_check){
		final int positn=position;
		AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
    	alertinternet.setTitle("Notification");
    	alertinternet.setMessage("Do you want to check your request status?");
	    alertinternet.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	view_status=true;
            	Accept_decline=false;
            /*	cid=UIDlist.get(positn).substring(0, UIDlist.get(positn).indexOf(";"));
	                uid=UIDlist.get(positn).substring(UIDlist.get(positn).indexOf(";")+1, UIDlist.get(positn).indexOf(";",UIDlist.get(positn).indexOf(";")+1));
	                Log.d("cid---pref", cid);
	                eid=UIDlist.get(positn).substring(UIDlist.get(positn).lastIndexOf(";")+1);
	                Log.d("status to check", view_status+uid+cid+eid);*/
            	 uid=Register_login.map.get("uid");
            	 cid=sentUserList.get(positn).get("id");
	                if(object.isConnectingToInternet(getActivity().getApplicationContext()))
	                	new View_CheckStatus().execute();
	                else{
	           		AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
	            	alertinternet.setTitle("Error");
	    		    alertinternet.setMessage("oops! Check Internet connection");
	    		    alertinternet.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
	                }
            }
        });
	    
	    alertinternet.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
	    alertinternet.show();
	}
	else{
		final int positn=position;
		view_status=false;
		Accept_decline=true;
		AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
    	alertinternet.setTitle("Request");
	    alertinternet.setMessage("Accept Request?");
	    alertinternet.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
           
	    	public void onClick(DialogInterface dialog,int which) {
               Accept_decline=true;
               uid=Register_login.map.get("uid");
               cid=recievedUserList.get(positn).get("id");
               Log.d("contact to view", view_status+uid);
               if(object.isConnectingToInternet(getActivity().getApplicationContext()))
               new View_CheckStatus().execute();
               else{
           		AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
            	alertinternet.setTitle("Error");
    		    alertinternet.setMessage("oops!  check Internet connection  ");
               }
	    	}
        });
	    alertinternet.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
	           
	    	public void onClick(DialogInterface dialog,int which) {
	    		Accept_decline=false;
	    		view_status=false;
           
	    	}
        });
	    alertinternet.show();
	}
	//
}

/*public void Sent(){
	recieve_check=true;
	map=(HashMap<String, String>)Event_search_fragement.my_pref_request.getAll();
	 userList.clear();
	   ListAdapter Register_login.adapter = new SimpleAdapter(
             getActivity(), userList,
             R.layout.activity_recent__activity_list, new String[] { TAG_USER, TAG_EVENT, TAG_DATE },
             new int[] { R.id.recieved_username,R.id.recieved_event, R.id.recieved_date});
     setListAdapter(Register_login.adapter);
     
	 if(!map.isEmpty()){
		 userList= new ArrayList<HashMap<String,String>>();
	        for(int i=(map.size()-1); i>=0; i--){
	        	 maping = new HashMap<String, String>();
	        maping.put("USERNAME", map.get("ID"+i).substring((map.get("ID"+i).lastIndexOf(";")+1), map.get("ID"+i).indexOf("*")));
	        maping.put("EVENT_NAME", map.get("ID"+i).substring(map.get("ID"+i).indexOf("*")+1));
	        UIDlist.add(map.get("ID"+i).substring(0, map.get("ID"+i).lastIndexOf(";")));

	        userList.add(maping); 
	        }
	   Log.d("list", userList.toString());
	        Register_login.adapter = new SimpleAdapter (
	                getActivity(), userList,
	                R.layout.activity_recent__activity_list, new String[] { "USERNAME", "EVENT_NAME"},
	                new int[] { R.id.recieved_username, R.id.recieved_event});
	        setListAdapter(Register_login.adapter);
}
}*/

public void Sent(){
	
	 userList.clear();
	    Register_login.adapter = new SimpleAdapter(
              getActivity(), userList,
              R.layout.activity_recent__activity_list, new String[] { TAG_USER, TAG_EVENT, TAG_DATE },
              new int[] { R.id.recieved_username,R.id.recieved_event, R.id.recieved_date});
      setListAdapter(Register_login.adapter);
      
	   recieve_check=true;
	   if(reload_sent|| Register_login.is_SignOut_recent){
		   if(object.isConnectingToInternet(getActivity().getApplicationContext()))
			   new LoadSentRequest().execute(); 
		   else
			   object.Getalert("Error", "oops! Check Internet connection");
	   }
	   else{
		   
		   Register_login.adapter = new SimpleAdapter(
               getActivity(), sentUserList,
               R.layout.activity_recent__activity_list, new String[] { TAG_USER, TAG_EVENT, TAG_DATE },
               new int[] { R.id.recieved_username,R.id.recieved_event, R.id.recieved_date});
       setListAdapter(Register_login.adapter);
	   }
	   
}

public void Recieved(){
	
	 userList.clear();
	    Register_login.adapter = new SimpleAdapter(
               getActivity(), userList,
               R.layout.activity_recent__activity_list, new String[] { TAG_USER, TAG_EVENT, TAG_DATE },
               new int[] { R.id.recieved_username,R.id.recieved_event, R.id.recieved_date});
       setListAdapter(Register_login.adapter);
       
	   recieve_check=false;
	   if(reload_recieved|| Register_login.is_SignOut_recent){
		   if(object.isConnectingToInternet(getActivity().getApplicationContext()))
			   new LoadRequest().execute(); 
		   else
			   object.Getalert("Error", "oops! Check Internet connection");
	   }
	   else{
		   Log.d("inrecieved", recievedUserList.toString());
		   Register_login.adapter = new SimpleAdapter(
                getActivity(), recievedUserList,
                R.layout.activity_recent__activity_list, new String[] { TAG_USER, TAG_EVENT, TAG_DATE },
                new int[] { R.id.recieved_username,R.id.recieved_event, R.id.recieved_date});
        setListAdapter(Register_login.adapter);
	   }
	   
}

@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
	
	inflater=this.getActivity().getMenuInflater();
	SupportMenu menu_=(SupportMenu) menu;
	super.onCreateOptionsMenu(menu, inflater);
	inflater.inflate(R.menu.recent_fragment, menu_);
	
	
		
	}

@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	if(!Register_login.profile_update){
	if(!Meet_me.is_open){
	switch (item.getItemId()) {
	
	case R.id.action_out:{
		Log.d("sent", "sent");
			Sent();
			onPrepareOptionsMenu(menu);
			return true;
		}
	case R.id.action_in:{
		Log.d("recieevd", "recieevd");
			Recieved();
			onPrepareOptionsMenu(menu);
			return true;
	}
	case R.id.action_refresh:{
	if(!recieve_check){	
		Log.d("rref", "refer");
	if(object.isConnectingToInternet(getActivity().getApplicationContext()))
			   new LoadRequest().execute(); 
	else
			   object.Getalert("Error", "oops! Check Internet connection");
	}
	else{
		if(object.isConnectingToInternet(getActivity().getApplicationContext()))
			   new LoadSentRequest().execute(); 
	else
			   object.Getalert("Error", "oops! Check Internet connection");
	}
		return true;
	}
		
	}
	}
	}
	return super.onOptionsItemSelected(item);
    
	}

class LoadSentRequest extends AsyncTask<String, String, String> {
	 
    private JSONArray user;

	/**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Request...");
        pDialog.setIndeterminate(false);
       // pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * getting Inbox JSON
     * */
    @Override
    protected String doInBackground(String... args) {
        // Building Parameters
    	   HttpClient httpclient = new DefaultHttpClient();
    		  
	        HttpPost httppost = new HttpPost(EVENT_URL);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("action", "sent_share_request"));
           params.add(new BasicNameValuePair("uid", Register_login.map.get("uid")));
           try {  
        httppost.setEntity(new UrlEncodedFormEntity(params));	
        ResponseHandler<String> responseHandler = new BasicResponseHandler();    
		String responseBody = httpclient.execute(httppost,responseHandler);	
		
		
		Log.d("Request", responseBody);
		if(responseBody.contains("{")){
		 responseBody=responseBody.substring(responseBody.indexOf("["));
		 Log.d("array", responseBody);
           responseBody="{ Users: "+responseBody+"}";
            Log.d("responsebody", ""+responseBody);
		JSONObject json=new JSONObject(responseBody);
        user = json.getJSONArray("Users");
        	Log.d("gateway", "gateway");
        	Log.d("json", user+"");
        	sentUserList=new ArrayList<HashMap<String,String>>();
            for (int i=(user.length()-1); i>=0; i--) {
                JSONObject c = user.getJSONObject(i);
                String id = c.getString(TAG_ID);                
                String user = c.getString(TAG_USER);
                String date=c.getString(TAG_DATE);
                String event=c.getString(TAG_EVENT);
                map = new HashMap<String, String>();
                map.put(TAG_DATE, date);
                map.put("POSITION", ""+i);
                map.put(TAG_ID, id);
                map.put(TAG_USER, user);
                map.put(TAG_EVENT, event);
                Log.d("map", map.toString());
              
               sentUserList.add( map);
          
            }
           
            
        } 
		return "success";
           }catch (Exception e) {
            e.printStackTrace();
        }

        return "failed";
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {

        pDialog.cancel();
        Log.d("success", ""+(file_url.contains("success")));
        if(file_url.contains("success")){
        if(!sentUserList.isEmpty()){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
           
               Register_login.adapter = new SimpleAdapter(
                        getActivity(), sentUserList,
                        R.layout.activity_recent__activity_list, new String[] { TAG_USER, TAG_EVENT, TAG_DATE },
                        new int[] { R.id.recieved_username,R.id.recieved_event, R.id.recieved_date});
                setListAdapter(Register_login.adapter);
                reload_sent=false;
                Register_login.is_SignOut_recent=false;
                
            }
        });
        }
        else{
        	AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
        	alertinternet.setTitle("Notification");
		    alertinternet.setMessage("No sent Request");
		    alertinternet.setNegativeButton("Dismiss",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
		    
		    alertinternet.show();
        }
        
        }
        else{
        	AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
        	alertinternet.setTitle("Error");
		    alertinternet.setMessage("Check your Internet connection");
		    alertinternet.setNegativeButton("Dismiss",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				      Register_login.adapter = new SimpleAdapter(
	                            getActivity(), sentUserList,
	                            R.layout.activity_recent__activity_list, new String[] { },
	                            new int[] {});
	                    setListAdapter(Register_login.adapter);	
				}
			});
		    
		    alertinternet.show();
        }
    }

}

class LoadRequest extends AsyncTask<String, String, String> {
	 
    private JSONArray user;

	/**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Request...");
        pDialog.setIndeterminate(false);
       // pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * getting Inbox JSON
     * */
    @Override
    protected String doInBackground(String... args) {
        // Building Parameters
    	   HttpClient httpclient = new DefaultHttpClient();
    		  
	        HttpPost httppost = new HttpPost(EVENT_URL);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("action", "received_share_request"));
           params.add(new BasicNameValuePair("uid", Register_login.map.get("uid")));
           try {  
        httppost.setEntity(new UrlEncodedFormEntity(params));	
        ResponseHandler<String> responseHandler = new BasicResponseHandler();    
		String responseBody = httpclient.execute(httppost,responseHandler);	
		
		
		Log.d("Request", responseBody);
		if(responseBody.contains("{")){
		 responseBody=responseBody.substring(responseBody.indexOf("["));
		 Log.d("array", responseBody);
           responseBody="{ Users: "+responseBody+"}";
            Log.d("responsebody", ""+responseBody);
		JSONObject json=new JSONObject(responseBody);
        user = json.getJSONArray("Users");
        	Log.d("gateway", "gateway");
        	Log.d("json", user+"");
        	recievedUserList=new ArrayList<HashMap<String,String>>();
            for (int i=(user.length()-1); i>=0; i--) {
                JSONObject c = user.getJSONObject(i);
                String id = c.getString(TAG_ID);                
                String user = c.getString(TAG_USER);
                String date=c.getString(TAG_DATE);
                String event=c.getString(TAG_EVENT);
                map = new HashMap<String, String>();
                map.put(TAG_DATE, date);
                map.put("POSITION", ""+i);
                map.put(TAG_ID, id);
                map.put(TAG_USER, user);
                map.put(TAG_EVENT, event);
                Log.d("map", map.toString());
              
               recievedUserList.add( map);
               Log.d("recievd", recievedUserList.toString());
            }
            Log.d("recievd", recievedUserList.toString());
            
            
        } 
		return "success";
           }catch (Exception e) {
            e.printStackTrace();
        }

        return "failed";
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {

        pDialog.cancel();
        Log.d("success", ""+(file_url.contains("success")));
        if(file_url.contains("success")){
        if(!recievedUserList.isEmpty()){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
           
                Register_login.adapter = new SimpleAdapter(
                        getActivity(), recievedUserList,
                        R.layout.activity_recent__activity_list, new String[] { TAG_USER, TAG_EVENT, TAG_DATE },
                        new int[] { R.id.recieved_username,R.id.recieved_event, R.id.recieved_date});
                setListAdapter(Register_login.adapter);
                reload_recieved=false;
                Register_login.is_SignOut_recent=false;
            }
        });
        }
        else{
        	AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
        	alertinternet.setTitle("Notification");
		    alertinternet.setMessage("No recieved Request");
		    alertinternet.setNegativeButton("Dismiss",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
		    
		    alertinternet.show();
        }
        
        }
        else{
        	AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
        	alertinternet.setTitle("Error");
		    alertinternet.setMessage("Check your Internet connection");
		    alertinternet.setNegativeButton("Dismiss",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				      Register_login.adapter = new SimpleAdapter(
	                            getActivity(), recievedUserList,
	                            R.layout.activity_recent__activity_list, new String[] { },
	                            new int[] {});
	                    setListAdapter(Register_login.adapter);	
				}
			});
		    
		    alertinternet.show();
        }
    }

}
class View_CheckStatus extends AsyncTask<String, String, String> {

    private JSONArray user;      
	private boolean responsebody_isfull;

	/**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait ...");
        pDialog.setIndeterminate(false);
        pDialog.show();
    }

    /**
     * getting Inbox JSON
     * */
    @Override
    protected String doInBackground(String... args) {
        // Building Parameters
    	   HttpClient httpclient = new DefaultHttpClient();
    		  
	        HttpPost httppost = new HttpPost(EVENT_URL);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if(view_status){
				Log.d("view_status", ""+view_status);
			params.add(new BasicNameValuePair("action", "view"));
        params.add(new BasicNameValuePair("uid", uid));
    //    params.add(new BasicNameValuePair("cid", cid));
    //    params.add(new BasicNameValuePair("eid", eid));
        Log.d("idsssss", "uid="+uid+"cid="+cid+"eid="+eid);
			}
				if(Accept_decline){
					Log.d("accept_decline", ""+Accept_decline);
	   			params.add(new BasicNameValuePair("action", "accept"));
	            params.add(new BasicNameValuePair("uid", uid));
	            params.add(new BasicNameValuePair("cid", cid));
	            params.add(new BasicNameValuePair("share_parameter", "1"));
				}

           try {  
        	   httppost.setEntity(new UrlEncodedFormEntity(params));
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = httpclient.execute(httppost,responseHandler);
		return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed";
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String result) {
    	
    	Log.d("result", result);
    	Log.d("contains", ""+(result.contains("\"id\":"+"\""+cid+"\"")));
        pDialog.dismiss();
        if((result.contains("\"id\":"+"\""+cid+"\""))&&view_status){
        	
        	object.Getalert("Status","Request has been Accepted", getActivity());
        }
        else if(result.contains("200")&&Accept_decline){
        	object.Getalert("Status","Your Contact has been shared succesfully", getActivity());
        }
        else if(result.contains("400")&&Accept_decline){
        	object.Getalert("Error","Error Sharing Contact, Pls try again", getActivity());
        }
        else if((!result.contains("\"id\":"+"\""+cid+"\""))&&view_status){
        	object.Getalert("Status","Your request is still pending", getActivity());
        }
        else{
        	object.Getalert("Error","Some wrong happened, pls retry", getActivity());
        }
    }

}


public static Editor GetPref(Context context){
		recievedpref=context.getSharedPreferences("Request", context.MODE_PRIVATE);
		recievededitor=recievedpref.edit();
		
		return recievededitor;
}
}
