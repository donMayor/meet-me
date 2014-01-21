package com.meet_me;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Edit extends Fragment {

	EditText change_name;
	EditText change_mail;
	EditText change_web;
	EditText change_mobile;
	//EditText change_work;
	//EditText change_meet_name;
	EditText change_org_name;
	EditText change_f_name;
	EditText change_org_web;
	EditText change_org_mail;
	EditText change_t_name;
	EditText change_org_add;
	Button update_button;
	Register_login object;
	private ProgressDialog pDialog;
//	ImageView back;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  // Inflate the layout for this fragment
		setHasOptionsMenu(true);
	  return inflater.inflate(R.layout.activity_edit, container, false);
	  
	  
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if(Meet_me.actionbar.getSelectedNavigationIndex()!=4){
			Meet_me.Selectiontab(4);
		}
		Edit_Auto();
	}
	

@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
	inflater=this.getActivity().getMenuInflater();
	SupportMenu menu_=(SupportMenu) menu;
	super.onCreateOptionsMenu(menu_, inflater);
	inflater.inflate(R.menu.edit, menu_);
		
	}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	if(!Register_login.profile_update){
	switch (item.getItemId()) {	
	case R.id.action_pic:{
		//Switch to edit Tab..
		//Meet_me.Selectiontab(0);
		Fragment fr=Fragment.instantiate(getActivity(), LoadImage.class.getName());
		FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, fr);
		transaction.commit();
		return true;
		}
	case R.id.action_home:{
		//Switch to edit Tab..
		//Meet_me.Selectiontab(0);
		Fragment fr=Fragment.instantiate(getActivity(), Profile_fragment.class.getName());
		FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, fr);
		transaction.commit();
		return true;
		}
	}
	}else{
	 	Toast.makeText(getActivity(), "Verify and Update your profile", Toast.LENGTH_LONG).show();
	}
	return super.onOptionsItemSelected(item);
}

	public void Edit_Auto(){
		object = new Register_login();
		 pDialog= new ProgressDialog(getActivity());
		change_name=(EditText)getActivity().findViewById(R.id.change_name);
		change_mail=(EditText) getActivity().findViewById(R.id.change_email);
		change_web=(EditText) getActivity().findViewById(R.id.change_web);
		change_mobile=(EditText) getActivity().findViewById(R.id.change_mobile);
		//change_work=(EditText) findViewById(R.id.change_work);
	//	change_meet_name=(EditText) findViewById(R.id.change_meet_name);
		change_org_name=(EditText) getActivity().findViewById(R.id.change_org_name);
		change_f_name=(EditText) getActivity().findViewById(R.id.change_facebook);
		change_org_web=(EditText) getActivity().findViewById(R.id.change_org_web);
		change_org_mail=(EditText) getActivity().findViewById(R.id.change_org_email);
		change_t_name=(EditText) getActivity().findViewById(R.id.change_twitter);
		change_org_add=(EditText)getActivity().findViewById(R.id.change_org_add);
		//back=(ImageView) findViewById(R.id.back);
		
		change_mail.setText(Register_login.map.get("personal_email"));
		change_web.setText(Register_login.map.get("personal_website"));
		change_f_name.setText(Register_login.map.get("facebook_username"));
		change_name.setText(Register_login.map.get("firstname")+" "+Register_login.map.get("lastname"));
		change_mobile.setText(Register_login.map.get("msisdn"));
		//change_work.setText(Register_login.map.get("msisdn"));
		//change_meet_name.setText(Register_login.map.get("username"));
		change_t_name.setText(Register_login.map.get("twitter_handle"));
		change_org_mail.setText(Register_login.map.get("org_email"));
		change_org_name.setText(Register_login.map.get("org_name"));
		change_org_web.setText(Register_login.map.get("org_website"));
		change_org_add.setText(Register_login.map.get("org_address"));
		
		change_f_name.setClickable(false);
		change_t_name.setClickable(false);
		update_button=(Button) getActivity().findViewById(R.id.updateButton );
		
		/*back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent= new Intent(getApplicationContext(), Profile.class);
				startActivity(intent);
			}
		});*/
		update_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {


				AlertDialog.Builder alertinternet= new AlertDialog.Builder(getActivity());
	        	alertinternet.setTitle("Notification");
				  
			        // Setting Dialog Message
			        alertinternet.setMessage("You are about to make changes to your profile, Continue?");
			        alertinternet.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			        	
						@Override
						public void onClick(DialogInterface dialog, int which) {
						
							if(object.isConnectingToInternet(getActivity().getApplicationContext())){
								 if(change_mobile.getText().toString().length()==11){	
								new Update().execute(new String[]{"http://p2mu.net/meetme/meetme.pl"});
							}
				              else{
				            	  Toast.makeText(getActivity(),"Only msisdn number is allowed", Toast.LENGTH_LONG).show();
				              }
							}
							else{
								object.Getalert("Internet Status", "oops! Check Internet Status",getActivity());
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
		});
		
	
	}
	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		
	}
*/

private class Update extends AsyncTask<String, Void, String>{

		
		@Override
		protected void onPreExecute() {
	

			super.onPreExecute();
			if(Meet_me.actionbar.getSelectedNavigationIndex()!=4)
	       		 Meet_me.Selectiontab(4);
			
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Updating Profile ...");
            pDialog.setIndeterminate(false);
          //  pDialog.setCancelable(false);
            pDialog.show();
		}

		@Override
		protected String doInBackground(String... url) {
			

			 String responseBody=" ";
			try {

		        HttpClient httpclient = new DefaultHttpClient();
		  
		        HttpPost httppost = new HttpPost(url[0]);
		            Log.i(getClass().getSimpleName(), "send  task - start");
		            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(15);
		              nameValuePairs.add(new BasicNameValuePair("action", "update"));
		              nameValuePairs.add(new BasicNameValuePair("uid", Register_login.map.get("uid")));
		              if(change_name.getText().length()!=0)
		              nameValuePairs.add(new BasicNameValuePair("firstname",change_name.getText().toString().substring(0, change_name.getText().toString().indexOf(" ")) ));
		              else{
		              nameValuePairs.add(new BasicNameValuePair("firstname",""));  
		              }
		              if(change_name.getText().toString().contains(" ")){
		            	  
		              nameValuePairs.add(new BasicNameValuePair("lastname",change_name.getText().toString().substring(change_name.getText().toString().indexOf(" "))));
		             
		              }else{
		            	  nameValuePairs.add(new BasicNameValuePair("lastname",""));  
		              }
		              nameValuePairs.add(new BasicNameValuePair("username", Register_login.map.get("facebook_username").substring(Register_login.map.get("facebook_username").lastIndexOf("/")+1)));
		              nameValuePairs.add(new BasicNameValuePair("twitter_handle", change_t_name.getText().toString()));
		              
		              nameValuePairs.add(new BasicNameValuePair("msisdn",change_mobile.getText().toString()));
		              nameValuePairs.add(new BasicNameValuePair("organisation", change_org_name.getText().toString()));
		              nameValuePairs.add(new BasicNameValuePair("org_address",change_org_add.getText().toString()));
		              nameValuePairs.add(new BasicNameValuePair("org_website",change_org_web.getText().toString()));
		              nameValuePairs.add(new BasicNameValuePair("org_email",change_org_mail.getText().toString()));
		              nameValuePairs.add(new BasicNameValuePair("email",change_mail.getText().toString()));
		              nameValuePairs.add(new BasicNameValuePair("website",change_web.getText().toString()));
		              nameValuePairs.add(new BasicNameValuePair("share_parameter", "1"));
		              nameValuePairs.add(new BasicNameValuePair("bio", "cool"));
		             
		              httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		              ResponseHandler<String> responseHandler = new BasicResponseHandler();
		              responseBody = httpclient.execute(httppost,responseHandler);
		            
		              //Use responsebody here
		              if(responseBody.contains("200")){
		            	
		      			Register_login.map.put("personal_email", change_mail.getText().toString());
		      			Register_login.map.put("personal_website", change_web.getText().toString());
		      			Register_login.map.put("facebook_username", change_f_name.getText().toString());
		      			if(change_name.getText().length()!=0)
		      			Register_login.map.put("firstname", change_name.getText().toString().substring(0, change_name.getText().toString().indexOf(" ")));
		      			else{
		      				Register_login.map.put("firstname"," ");	
		      			}
		      			 if(change_name.getText().toString().contains(" "))
		      			Register_login.map.put("lastname", change_name.getText().toString().substring(change_name.getText().toString().indexOf(" ")));
		      			 else{
		      				Register_login.map.put("lastname"," "); 
		      			 }
		      			 Register_login.map.put("name", Register_login.map.get("firstname")+" "+Register_login.map.get("lastname"));
		      			Register_login.map.put("msisdn", change_mobile.getText().toString());
		      			Register_login.map.put("twitter_handle", change_t_name.getText().toString());
		      			Register_login.map.put("org_email", change_org_mail.getText().toString());
		      			Register_login.map.put("org_name", change_org_name.getText().toString());
		      			Register_login.map.put("org_website", change_org_web.getText().toString());
		      			Register_login.map.put("org_address", change_org_add.getText().toString());
		      			Register_login.map.put("username", change_f_name.getText().toString().substring(change_f_name.getText().toString().lastIndexOf("/")+1));
		      			Register_login.profile_update=false;
		              }
		              }catch(Exception e){
		            	responseBody="failed";
		        	e.printStackTrace();
		        }
		        	return responseBody;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pDialog.dismiss();
		
			
			if(!result.contains("failed")){
				
				
				if(Register_login.from_login){
					Fragment fr=Fragment.instantiate(getActivity(), Event_search_fragement.class.getName());
						FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
						transaction.replace(R.id.content_frame, fr);
						transaction.commit();
					Register_login.from_login=false;
				}else{
				Fragment fr=Fragment.instantiate(getActivity(), Profile_fragment.class.getName());
				//	Fragment newFragment = new Event_search_fragement();
					FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
					transaction.replace(R.id.content_frame, fr);
					transaction.commit();
				}
			}
			else{
				 Toast.makeText(getActivity(), "Please try again, Ensure Internet connectivity", Toast.LENGTH_LONG ).show();
				 Fragment fr=Fragment.instantiate(getActivity(), Edit.class.getName());
					FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
					transaction.replace(R.id.content_frame, fr);
					transaction.commit();
				object.Getalert("Error", "Something Went wrong, Check Internet status and Try Again", getActivity());
			
			}
	}
}
}
