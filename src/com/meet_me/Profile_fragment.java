package com.meet_me;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MenuItemCompat;

public class Profile_fragment extends ListFragment {


	public static boolean menu=false;
	Register_login object;
	String[] key= {"name", "msisdn","personal_email","personal_website","twitter_handle","facebook_username","org_name",
			"org_address","org_website","org_email", "username"};
	String[] MOBILE_OS= new String[key.length];
	private ProgressDialog pDialog;
	LoadImage load=new LoadImage();
	
	ImageView profile_pic;
	
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  // Inflate the layout for this fragment
	setHasOptionsMenu(true);
	Log.d("inflate", "inflate thing");
	Log.d("check", key[0]);
  return inflater.inflate(R.layout.profile_fragment, container, false);
  
  
}
@Override
public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onActivityCreated(savedInstanceState);
	for(int i=0; i<key.length; i++){
			MOBILE_OS[i]=(String)Register_login.map.get(key[i]);

		}
	setListAdapter(new Profile_fragment.adapter(getActivity(), MOBILE_OS, key ));   
	Profile_Auto();
	Log.d("Created", "Created thing");
}

public void Profile_Auto(){
	 pDialog= new ProgressDialog(getActivity());
	object= new Register_login();
	profile_pic=(ImageView) getActivity().findViewById(R.id.profile_pic);
	profile_pic.setImageResource(R.drawable.profile);
	/*if(Register_login.profile_update){
			new Update().execute(new String[]{"http://p2mu.net/meetme/meetme.pl"});
	}
	else*/ if (Register_login.image_isset){
		new view_pic().execute();
	}
	else{
		
		if(Register_login.picture==null){
			profile_pic.setImageResource(R.drawable.profile);
			
		}
		else{
			 profile_pic.setImageBitmap(Register_login.picture);
		}
		if(Meet_me.actionbar.getSelectedNavigationIndex()!=4){
			Meet_me.Selectiontab(4);
		}
	}
	profile_pic.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
		
			Fragment fr=Fragment.instantiate(getActivity(), LoadImage.class.getName());
			FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.content_frame, fr);
			transaction.commit();
		}
	});

}

public void Edit(){
	Intent intent= new Intent(getActivity().getApplicationContext(), Edit.class);
	startActivity(intent);
}
@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
	inflater=this.getActivity().getMenuInflater();
	SupportMenu menu_=(SupportMenu) menu;
	
	super.onCreateOptionsMenu(menu_, inflater);
	inflater.inflate(R.menu.profile_fragment, menu_);
		
	}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	if(!Register_login.profile_update){
		if(!Meet_me.is_open){
	switch (item.getItemId()) {	
	case R.id.action_edit:{
		//Switch to edit Tab..
		//Meet_me.Selectiontab(0);
		Fragment fr=Fragment.instantiate(getActivity(), Edit.class.getName());
		FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, fr);
		transaction.commit();
		return true;
		}
	case R.id.action_pic:{
		//Switch to edit Tab..
		//Meet_me.Selectiontab(0);
		Fragment fr=Fragment.instantiate(getActivity(), LoadImage.class.getName());
		FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, fr);
		transaction.commit();
		return true;
		}
	case R.id.action_refresh:{
		for(int i=0; i<key.length; i++){
			MOBILE_OS[i]=(String)Register_login.map.get(key[i]);

		}
	setListAdapter(new Profile_fragment.adapter(getActivity(), MOBILE_OS, key ));   
	Profile_Auto();
		return true;
		}
	}
		}
	}
	return super.onOptionsItemSelected(item);
}

public class adapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	private final String[] key;
 
	public adapter(Context context, String[] values, String[] key) {	
		super(context, R.layout.profile, values);
		
		this.context = context;
		this.values = values;
		this.key=key;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.profile, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(values[position]);
 
		// Change icon based on name
		String s = key[position];
 
		System.out.println(s);
 
		if (s.equals("username")) {
		//	if(Register_login.picture==null)
			imageView.setImageResource(R.drawable.meet_logo);
			/*else
			imageView.setImageBitmap(Register_login.picture);*/
		} else if (s.equals("name")) {
			imageView.setImageResource(R.drawable.profile);
		} else if (s.equals("personal_website")) {
			imageView.setImageResource(R.drawable.org_web);
		}
		else if (s.equals("personal_email")) {
			imageView.setImageResource(R.drawable.user_email);
		} else if (s.equals("msisdn")) {
			imageView.setImageResource(R.drawable.phone_num);
		} else if(s.equals("org_name")){
			imageView.setImageResource(R.drawable.org_name);
		} else if (s.equals("org_address")) {
			imageView.setImageResource(R.drawable.org_add);
		} else if (s.equals("org_website")) {
			imageView.setImageResource(R.drawable.org_web);
		} else if(s.equals("org_email")){
			imageView.setImageResource(R.drawable.user_email);
		} else if (s.equals("twitter_handle")) {
			imageView.setImageResource(R.drawable.twitter_logo);
		} else if (s.equals("facebook_username")) {
			imageView.setImageResource(R.drawable.facebook_logo);
		} 
		return rowView;
	}
}

private class Update extends AsyncTask<String, Void, String>{

//	private ProgressDialog pDialog;
	private JSONArray user;
	
	@Override
	protected String doInBackground(String... url) {
		

		 String responseBody="failed";
		 HttpPost httppost = new HttpPost("http://p2mu.net/meetme/profile_pix.pl");
		 HttpClient httpclient = new DefaultHttpClient();
   			List<NameValuePair> params = new ArrayList<NameValuePair>();
   			params.add(new BasicNameValuePair("action", "get_image"));
               params.add(new BasicNameValuePair("uid", Register_login.map.get("uid")));
               params.add(new BasicNameValuePair("cid",  Register_login.map.get("uid")));
               try {  
           httppost.setEntity(new UrlEncodedFormEntity(params));
			
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpclient.execute(httppost,responseHandler);
		 	ViewProfile(responseBody);
		 			responseBody="200:";
	        }catch(Exception e){
	        	
	        }
	        	return responseBody;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		pDialog.cancel();
		
		
		if(result.contains("200:")){
			 

                   /* Register_login.map.put("firstname",Register_login.tempmap.get("firstname"));
                    Register_login.map.put("lastname", Register_login.tempmap.get("lastname"));
                    Register_login.map.put("username", Register_login.tempmap.get("username"));
                    Register_login.map.put("name", Facebook_twitter_signup.Name.getString("name", ""));	  
                    Log.d("PREF", Facebook_twitter_signup.Name.getString("name", ""));
                    Register_login.map.put("personal_email",  Register_login.tempmap.get("personal_email"));
                    Register_login.map.put("msisdn", "0"+Register_login.tempmap.get("msisdn"));
                    Register_login.map.put("twitter_handle", Register_login.tempmap.get("twitter_handle"));
                    Register_login.map.put("personal_website", Register_login.tempmap.get("personal_website"));
                    Register_login.map.put("org_email", Register_login.tempmap.get("org_email"));
                    Register_login.map.put("org_website",Register_login.tempmap.get("org_website"));
                    Register_login.map.put("org_address", Register_login.tempmap.get("org_address"));
                    Register_login.map.put("org_name", Register_login.tempmap.get("org_name"));
                    Register_login.map.put("facebook_username",Register_login.tempmap.get("facebook_username"));*/
			
                    
					 if(Register_login.picture!=null){
		                 profile_pic.setImageBitmap(Register_login.picture);
		                 }
		                 else{
		                 profile_pic.setImageResource(R.drawable.profile);
		                 }
		/*		getActivity().runOnUiThread(new Runnable() {
			     
				public void run() {	
                
			            }
			            });*/
		}
		else{
			object.Getalert("Error", "Something Went wrong,  profile picture could not be loaded, Check Internet status ",getActivity());
			profile_pic.setImageResource(R.drawable.profile);
		}

}
}



public static class downloadImage {
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
        public Bitmap DownloadImage(String URL)
        {        
            Bitmap bitmap = null;
            InputStream in = null;        
            try {
                in = OpenHttpConnection(URL);
                bitmap = decodeFile(in, URL);
                Register_login.image_isset=false;
                in.close();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            }
            return bitmap;                
        }
        private Bitmap decodeFile(InputStream f, String urlstring){
        	int response=-1;
            /* try {
             	
                 //decode image size
                 BitmapFactory.Options option = new BitmapFactory.Options();
                 option.inJustDecodeBounds = true;
                 BitmapFactory.decodeStream(f,null,option);
                 Log.d("decode", "first_level");
                 //Find the correct scale value. It should be the power of 2.
                 final int REQUIRED_SIZE=200;
                 int width_tmp=option.outWidth, height_tmp=option.outHeight;
                 int scale=1;
                 while(true){
                     if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                         break;
                     width_tmp/=2;
                     height_tmp/=2;
                     scale*=2;
                     Log.d("scale", ""+scale);
                 }
             } catch (Exception e) {
             	e.printStackTrace();
             
             }
                 //first option ends
 */            try{
                /* InputStream in = null;      
                 URL url = new URL(urlstring); 
                 URLConnection conn = url.openConnection();
                           
                 if (!(conn instanceof HttpURLConnection))                     
                     throw new IOException("Not an HTTP connection");
                  
                 
                 	httpConn = (HttpURLConnection) conn;
                     httpConn.setAllowUserInteraction(false);
                     httpConn.setInstanceFollowRedirects(true);
                     httpConn.setRequestMethod("GET");
                     httpConn.connect(); 
                     Log.d("Httpconnection", "connection successAgain");
                     response = httpConn.getResponseCode();                 
                     if (response == HttpURLConnection.HTTP_OK) {
                     	 Log.d("Httpconnection", ""+response);
                          in = httpConn.getInputStream();                                 
                     }            */         
                 
                 //decode with inSampleSize
                 //Find the value of scale to remove double loading
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
public void ViewProfile(String response){
HttpClient httpclient = new DefaultHttpClient();
	downloadImage url= new downloadImage();
	Register_login.picture=url.DownloadImage("http://p2mu.net/meetme/images/"+response);

   Log.d("picture", ""+Register_login.picture);	
}

private class view_pic extends AsyncTask<Void, Void,String>{


//private ProgressDialog pDialog;
@Override
protected void onPreExecute() {
    super.onPreExecute();
    pDialog = new ProgressDialog(getActivity());
    pDialog.setMessage("Loading Profile picture....");
    pDialog.setIndeterminate(false);
    pDialog.show();
}
@Override
protected String doInBackground(Void... params) {
	String responseBody="fail";

	 HttpPost httppost = new HttpPost("http://p2mu.net/meetme/profile_pix.pl");
	 HttpClient httpclient = new DefaultHttpClient();
			List<NameValuePair> parameter = new ArrayList<NameValuePair>();
			parameter.add(new BasicNameValuePair("action", "get_image"));
          parameter.add(new BasicNameValuePair("uid", Register_login.map.get("uid")));
          parameter.add(new BasicNameValuePair("cid",  Register_login.map.get("uid")));
          try {  
      httppost.setEntity(new UrlEncodedFormEntity(parameter));
		
       ResponseHandler<String> responseHandler = new BasicResponseHandler();
       responseBody = httpclient.execute(httppost,responseHandler);
       responseBody=responseBody.substring(4);
	 	ViewProfile(responseBody);
	 			return "200";
       }catch(Exception e){
       	
       }
       	return responseBody;
}
@Override
protected void onPostExecute(String result) {
	// TODO Auto-generated method stub
	pDialog.cancel();
	super.onPostExecute(result);
	Register_login.image_isset=false;
	if(result=="200"){
	if(Register_login.picture==null){
		profile_pic.setImageResource(R.drawable.profile);
	}
	else{
		 profile_pic.setImageBitmap(Register_login.picture);
	}
	}
	else{
	object.Getalert("Error", "Something Went wrong,  profile picture could not be loaded, Check Internet status ",getActivity());
	profile_pic.setImageResource(R.drawable.profile);
	}
		if(Meet_me.actionbar.getSelectedNavigationIndex()!=4){
			Meet_me.Selectiontab(4);
		}
}
}
}
