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

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.R.drawable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MenuItemCompat;
import android.annotation.TargetApi;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;

public class Contact_list_Frageent extends ListFragment {

	
	static boolean reload_contact=false;
	
	HashMap<String, String> menuHash;
	HashMap<String, drawable> picture;
	private ProgressDialog pDialog;
	static ListAdapter adapter=null;
	Register_login object;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 setHasOptionsMenu(true);
		 object= new Register_login();
		 return inflater.inflate(R.layout.contact_list__frageent, container, false); 
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	if(!reload_contact||Register_login.is_SignOut_contact)
			Contact_Auto();
	else{
		
			setListAdapter(adapter);
	}
		if(Meet_me.actionbar.getSelectedNavigationIndex()!=3){
			Meet_me.Selectiontab(3);
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		final int post=position;
		CharSequence[] option= {"Add to Phonebook", "Call", "Email", "Send SMS", "View details"};
		 AlertDialog.Builder alert= new AlertDialog.Builder(getActivity());
		 alert.setTitle("Options");
		 alert.setItems(option, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int num) {
				// TODO Auto-generated method stub
				Log.d("Check type", ""+Register_login.userHistoryList.get(post));
				HashMap<String, String> item=(HashMap<String, String>)Register_login.userHistoryList.get(post);
				if(num==0){
					SavetoContact(item.get("firstname")+" "+item.get("lastname"), item.get("username"),item.get("msisdn"), item.get("personal_email"), item.get("personal_website"), item.get("org_name"), item.get("org_email"),item.get("website"), item.get("org_address"));
				}
				else if(num==1){
						Method_Call(item.get("msisdn"));
				}
				else if(num==2){
					final CharSequence[] option= {item.get("personal_email"), item.get("org_email")};
					 
					AlertDialog.Builder alert= new AlertDialog.Builder(getActivity());
					 alert.setTitle("Send Email");
					 alert.setSingleChoiceItems(option, 0, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String email=(String)option[which];
							SendEmail(email);
						}
					});
					 alert.show();
				}
				else if(num==3){
					SendSMS(item.get("msisdn"));
				}
				else if(num==4){
				
					AlertDialog.Builder alert= new AlertDialog.Builder(getActivity());
					 alert.setTitle(item.get("username"));
					 String[] key= {"name", "msisdn","personal_email","personal_website","twitter_handle","facebook_username","org_name",
								"org_address","org_website","org_email", "username"};
						String[] view_details=new String[key.length];
						for(int i=0; i<key.length; i++){
							view_details[i]=item.get(key[i]);

						}
					 alert.setAdapter(new adapter_view(getActivity(),view_details, key), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					 alert.show();
				}
			}
		});
		 alert.show();
	   
	}
	public void Contact_Auto(){
		
		pDialog = new ProgressDialog(getActivity());
		object= new Register_login();
		if(object.isConnectingToInternet(getActivity().getApplicationContext()))
    	{
    		if(adapter==null){
    		  	new LoadHistory().execute();
    		}
    		else{
    			setListAdapter(adapter);
    		}
    	}
    	else{
    		object.Getalert("Error", "oops! Enable Internet Connection", getActivity());
    	}
	}
	
	private class adapter_view extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;
		private final String[] keys;
	 
		public adapter_view(Context context, String[] values, String[] keys) {
			super(context, R.layout.profile, values);
			this.context = context;
			this.values = values;
			this.keys=keys;
		}
	 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 

			View rowView=inflater.inflate(R.layout.profile, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.label);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
			textView.setText(values[position]);
	 
			// Change icon based on name
			String s = keys[position];
	 
			System.out.println(s);
			if (s.equals("name")) {
				imageView.setImageResource(R.drawable.profile);
			} else if (s.equals("msisdn")) {
				imageView.setImageResource(R.drawable.phone);
			} else if (s.equals("personal_email")) {
				imageView.setImageResource(R.drawable.email);
			}
			else if (s.equals("personal_website")) {
				imageView.setImageResource(R.drawable.org_web);
			} else if (s.equals("facebook_username")) {
				imageView.setImageResource(R.drawable.facebook_logo);
			} else if (s.equals("username")) {
				imageView.setImageResource(R.drawable.meet_logo);
			} 
			else if (s.equals("twitter_handle")) {
				imageView.setImageResource(R.drawable.twitter_logo);
			} 
			else if (s.equals("org_name")) {
				imageView.setImageResource(R.drawable.org_name);
			} 
			else if (s.equals("org_website")) {
				imageView.setImageResource(R.drawable.org_web);
			} 
			else if (s.equals("org_email")) {
				imageView.setImageResource(R.drawable.email);
			} 
			else if (s.equals("org_address")) {
				imageView.setImageResource(R.drawable.org_add);
			} 
			
			return rowView;
		}
	}
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		// TODO Auto-generated method stub
		
		
		super.onCreateOptionsMenu(menu, inflater);
		
	}
@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
	if(!Register_login.profile_update){
		if(!Meet_me.is_open){
			switch (item.getItemId()) {
			
			case R.id.action_refresh:{
				if(object.isConnectingToInternet(getActivity().getApplicationContext()))
		    	{		    		
		    		  	new LoadHistory().execute();
		    	}
		    	else{
		    		object.Getalert("Error", "oops! Enable Internet Connection", getActivity());
		    	}
				return true;
				}
		
			}
		}
	}
		return super.onOptionsItemSelected(item);
}

public void OptionMethod(){
	
}

class LoadHistory extends AsyncTask<String, String, String>{

	private static final String EVENT_URL = "http://p2mu.net/meetme/meetme.pl";
	
	private JSONArray user;
	private HashMap<String, String> map;

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		 pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Contacts ...");
            pDialog.setIndeterminate(false);
   //         pDialog.setCancelable(false);
            pDialog.show();
	}
	
	@Override
	protected String doInBackground(String... args) {
		 	HttpClient httpclient = new DefaultHttpClient();
		 	String responseBody ="";
   	        HttpPost httppost = new HttpPost(EVENT_URL);
   			
   			List<NameValuePair> params = new ArrayList<NameValuePair>();
   			params.add(new BasicNameValuePair("action", "view"));
               params.add(new BasicNameValuePair("uid", Register_login.map.get("uid")));
               try{
            httppost.setEntity(new UrlEncodedFormEntity(params));
			
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpclient.execute(httppost,responseHandler);
        //    responseBody="[{\"firstname\":\"Mayowa\",\"personal_website\":\"www.mayor.com\",\"lastname\":\"Egbewunmi\",\"personal_email\":\"talk2mayor30@gmail.com\",\"msisdn\":\"070234567 0805634244\",\"id\":\"7\",\"org_name\":\"iQube Lab\",\"org_address\":\"23 Cresceent Avenu Lagos State\",\"org_website\":\"www.iQube.com\",\"org_email\":\"mayor@iQube.com\",\"twitter_handle\":\"@mayor\",\"facebook_username\":\"don-mayowa\",\"username\":\"don-mayowa@.meet@me\"},{\"firstname\":\"Seun\",\"personal_website\":\"seun.com\",\"lastname\":\"Dayo\",\"personal_email\":\"talk2seun@gmail.com\",\"msisdn\":\"070234567 0805634244\",\"id\":\"2\",\"org_name\":\"Konga Lab\",\"org_address\":\"23 Cresceent Ariel Lagos State\",\"org_website\":\"www.iQube.com\",\"org_email\":\"mayor@konga.com\",\"twitter_handle\":\"@seun\",\"facebook_username\":\"don-seun\",\"username\":\"don-seun@.meet@me\"}]";
            return responseBody;
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
	
		if(!result.contains("failed")){
		Log.d("result",result);
		if(result.contains("{")){
		  result=result.substring(result.indexOf("{"));
            result="{ History: ["+result+"}";
        
            try{
		JSONObject json=new JSONObject(result);
        user = json.getJSONArray("History");
        	Log.d("gateway", "gateway");
        	Log.d("json", user+"");
        	Register_login.userHistoryList=new ArrayList<HashMap<String,String>>();
            for (int i = 0; i < user.length(); i++) {
                JSONObject c = user.getJSONObject(i);
  
                
                map = new HashMap<String, String>();
                //lastname firstname, and uid could be removed here.
                map.put("firstname", c.getString("firstname"));
                map.put("uid",c.getString("id"));
                map.put("lastname",  c.getString("lastname"));
                map.put("username",  c.getString("username"));	
               
                map.put("name", c.getString("firstname")+" "+c.getString("lastname"));
//                map.put("shareparameter",  c.getString("share_parameter"));
                map.put("personal_email",  c.getString("email"));
                map.put("msisdn",  "0"+c.getString("msisdn"));
                map.put("twitter_handle", "twitter");
                map.put("personal_website",  c.getString("website"));
                map.put("org_email",  c.getString("org_email"));
                map.put("org_website",  c.getString("org_website"));
                map.put("org_address",  c.getString("org_address"));
                map.put("org_name",  c.getString("organisation"));
                map.put("facebook_username", "http://www.facebook.com/"+c.getString("username"));

                
               Register_login.userHistoryList.add(map);
            }
       		adapter = new SimpleAdapter(
                    getActivity(), Register_login.userHistoryList,
                    R.layout.activity_recent__activity_list, new String[] { "name", "personal_email", "msisdn" },
                    new int[] { R.id.recieved_username,R.id.recieved_event, R.id.recieved_date });   
       				reload_contact=true;
       				Register_login.is_SignOut_contact=false;
		    		setListAdapter(adapter);
            }
            catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
		 AlertDialog.Builder alert= new AlertDialog.Builder(getActivity());
		 alert.setTitle("Contacts Log");
		 alert.setMessage("Contacts log is empty");
		 alert.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		 alert.show();
		 
		}
}else{
	object.Getalert("Error", "Poor Internet connection",getActivity());
}
	}
}

//Implementation of Call , SMS , Add to Phone book, Email start here

//Adding Here
public void SavetoContact(String name, String username, String msisdn,String personal_email, String personal_website, String org_name,String org_email, String org_website, String org_address){
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
            ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            .build()
        );

        //------------------------------------------------------ Names
        if(name != null)
        {           
            ops.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)              
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,     
                    name).build()
            );
        } 

        //------------------------------------------------------ Mobile Number                      
        if(msisdn != null)
        {
            ops.add(ContentProviderOperation.
                newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, msisdn)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, 
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build()
            );
        }

                          
                            //------------------------------------------------------ Email
                            if(personal_email!= null)
                            {
                                 ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Email.DATA, personal_email)
                                            .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                                            .build());
                            }
                            if(org_email!= null)
                            {
                                 ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Email.DATA, org_email)
                                            .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                                            .build());
                            }
                            //-------------------------website--------------------//
                            if(personal_website!= null)
                            {
                                 ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Website.DATA, personal_website)
                                            .withValue(ContactsContract.CommonDataKinds.Website.TYPE, ContactsContract.CommonDataKinds.Website.TYPE_HOME)
                                            .build());
                            }
                            if(org_website!= null)
                            {
                                 ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Website.DATA, org_website)
                                            .withValue(ContactsContract.CommonDataKinds.Website.TYPE, ContactsContract.CommonDataKinds.Website.TYPE_WORK)
                                            .build());
                            }
                           
                            //--------------------------Photo
                          
                            //-----------------NickName
                            if(username!= null)
                            {
                                 ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Nickname.DATA, username)
                                            .withValue(ContactsContract.CommonDataKinds.Nickname.TYPE, ContactsContract.CommonDataKinds.Website.TYPE_HOME)
                                            .build());
                            }
                            //------------------------------------------------------ Organization
                            if(org_name!=null);
                            {
                                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                        .withValue(ContactsContract.Data.MIMETYPE,
                                                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, org_name)
                                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                                        .withValue(ContactsContract.CommonDataKinds.Organization.OFFICE_LOCATION, org_address)
                                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                                        .build());
                            }

                            // Asking the Contact provider to create a new contact                  
                            try 
                            {
                                getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                             Toast.makeText(getActivity(), "User Profile has been added to your Contacts list", Toast.LENGTH_LONG).show();
                            } 
                            catch (Exception e) 
                            {               
                                e.printStackTrace();
                              //  Toast.makeText(myContext, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
}
//Adding Ends
//Call Start
private void Method_Call(String number){
 
 Intent callIntent = new Intent(Intent.ACTION_CALL);
	callIntent.setData(Uri.parse("tel:"+number));
	startActivity(callIntent);	
}

/* private class PhoneCallListener extends PhoneStateListener {
 
	private boolean isPhoneCalling = false;

	String LOG_TAG = "LOGGING 123";

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {

		if (TelephonyManager.CALL_STATE_RINGING == state) {
			// phone ringing
			Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
		}

		if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
			// active
			Log.i(LOG_TAG, "OFFHOOK");

			isPhoneCalling = true;
		}
		
		if (TelephonyManager.CALL_STATE_IDLE == state) {
			// run when class initial and phone call ended, 
			// need detect flag from CALL_STATE_OFFHOOK
			Log.i(LOG_TAG, "IDLE");

			if (isPhoneCalling) {

				Log.i(LOG_TAG, "restart app");

				// restart app
				Intent i = getBaseContext().getPackageManager()
					.getLaunchIntentForPackage(
						getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);

				isPhoneCalling = false;
			}

		}
	}
}*/
/* public void Phone_listerner(){
 PhoneCallListener phoneListener = new PhoneCallListener();
	TelephonyManager telephonyManager = (TelephonyManager) this
		.getSystemService(Context.TELEPHONY_SERVICE);
	telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
}*/
//Call End

//SMS start
public void SendSMS(String number){
 try {
	 
     Intent sendIntent = new Intent(Intent.ACTION_VIEW);
     sendIntent.putExtra("sms_body", ""); 
     sendIntent.putExtra("address", number);
    
     sendIntent.setType("vnd.android-dir/mms-sms");
     startActivity(sendIntent);

} catch (Exception e) {
	Toast.makeText(getActivity().getApplicationContext(),
		"SMS faild, please try again later!",
		Toast.LENGTH_LONG).show();
	e.printStackTrace();
}
}
//SMS end
//Email Start
public void SendEmail(String Email){
 Intent email = new Intent(Intent.ACTION_SEND);
  email.putExtra(Intent.EXTRA_EMAIL, new String[]{ Email});

  email.setType("message/rfc822");
  startActivity(Intent.createChooser(email, "Choose an Email client :"));
}
//Email Ends

}
