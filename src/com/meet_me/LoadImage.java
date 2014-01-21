package com.meet_me;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.internal.view.SupportMenu;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoadImage extends Fragment {

private ImageView image;
private Button uploadButton;
private Bitmap bitmap;
String picturePath=null;
private Button selectImageButton;
//private TextView skip;
Register_login object;
private ProgressDialog dialog;

 // number of images to select
 private static final int PICK_IMAGE = 1;

 /**
  * called when the activity is first created
  * 
  */
 
 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  // Inflate the layout for this fragment
		setHasOptionsMenu(true);
	  return inflater.inflate(R.layout.load_image, container, false);
	  
	  
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		image = (ImageView)getActivity().findViewById(R.id.uploadImage);
		object=new Register_login();
		LoadImage_Auto();
	}

	public void LoadImage_Auto(){
		 dialog = new ProgressDialog(getActivity());
		  // find the views
		  
		  if(Register_login.picture==null){
				image.setImageResource(R.drawable.profile);
			}
			else{
				image.setImageBitmap(Register_login.picture);
			}
		  uploadButton = (Button) getActivity().findViewById(R.id.uploadButton);
		 // skip=(TextView) getActivity().findViewById(R.id.skip);
		  // on click select an image
		  selectImageButton = (Button) getActivity().findViewById(R.id.selectImageButton);
		  selectImageButton.setOnClickListener(new OnClickListener() {

		   @Override
		   public void onClick(View v) {
		    selectImageFromGallery();

		   }
		  });

		  // when uploadButton is clicked
		  uploadButton.setOnClickListener(new OnClickListener() {

		   @Override
		   public void onClick(View v) {
			   if(picturePath!=null)
		    new ImageUploadTask().execute();
			   else{
				object.Getalert("Error", "You ve not changed your profile picture", getActivity());   
			   }
		   }
		  });
		
		  
		 /* back.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(Register_login.Load_from_Registration){
						Intent intent= new Intent(getApplicationContext(), Organisation_profile.class);
						startActivity(intent);
					}else{
						Intent intent= new Intent(getApplicationContext(),Profile.class);
						startActivity(intent);	
					}
				}
			});*/
	}

@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
	inflater=this.getActivity().getMenuInflater();
	SupportMenu menu_=(SupportMenu) menu;
	
	super.onCreateOptionsMenu(menu_, inflater);
	inflater.inflate(R.menu.load_image, menu_);
		
	}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
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
	return super.onOptionsItemSelected(item);
}

	
 /**
  * Opens dialog picker, so the user can select image from the gallery. The
  * result is returned in the method <code>onActivityResult()</code>
  */
 public void selectImageFromGallery() {
  Intent intent = new Intent();
  intent.setType("image/*");
  intent.setAction(Intent.ACTION_GET_CONTENT);
  startActivityForResult(Intent.createChooser(intent, "Select Picture"),
    PICK_IMAGE);
 }

 /**
  * Retrives the result returned from selecting image, by invoking the method
  * <code>selectImageFromGallery()</code>
  */
 
 
 @Override
 public void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);

  if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK
    && null != data) {
   Uri selectedImage = data.getData();
   String[] filePathColumn = { MediaStore.Images.Media.DATA };

   Cursor cursor = getActivity().getContentResolver().query(selectedImage,
     filePathColumn, null, null, null);
   cursor.moveToFirst();

   int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
   picturePath = cursor.getString(columnIndex);
   Log.d("picture path", picturePath);
   
  
   cursor.close();
 
  Register_login.picture=decodeFile(picturePath);

  }
 }

 /**
  * The method decodes the image file to avoid out of memory issues. Sets the
  * selected image in to the ImageView.
  * 
  * @param filePath
  */
 public Bitmap decodeFile(String filePath) {
  // Decode image size
  BitmapFactory.Options o = new BitmapFactory.Options();
  o.inJustDecodeBounds = true;
  BitmapFactory.decodeFile(filePath, o);

  // The new size we want to scale to
  final int REQUIRED_SIZE = 1024;

  // Find the correct scale value. It should be the power of 2.
  int width_tmp = o.outWidth, height_tmp = o.outHeight;
  int scale = 1;
  while (true) {
   if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
    break;
   width_tmp /= 2;
   height_tmp /= 2;
   scale *= 2;
  }

  // Decode with inSampleSize
  BitmapFactory.Options o2 = new BitmapFactory.Options();
  o2.inSampleSize = scale;
  bitmap = BitmapFactory.decodeFile(filePath, o2);

  image.setImageBitmap(bitmap);
  
  return bitmap;
 }

 /**
  * The class connects with server and uploads the photo
  * 
  * 
  */
 class ImageUploadTask extends AsyncTask<Void, Void, String> {
  private String webAddressToPost = "http://p2mu.net/meetme/upload.pl";

  // private ProgressDialog dialog;
  private ProgressDialog dialog = new ProgressDialog(getActivity());

  @Override
  protected void onPreExecute() {
   dialog.setMessage("Uploading...");
   dialog.setIndeterminate(false);
   dialog.setCancelable(false);
   dialog.show();
  }

  @Override
  protected String doInBackground(Void... params) {
   try {
    HttpClient httpClient = new DefaultHttpClient();
    HttpContext localContext = new BasicHttpContext();
    HttpPost httpPost = new HttpPost(webAddressToPost);

    Log.d("path", picturePath);
    File file = new File(picturePath);
    MultipartEntity entity = new MultipartEntity();
    
    entity.addPart("image", new FileBody(file));
    entity.addPart("action", new StringBody("upload"));
    Log.d("UID", Register_login.map.get("uid"));
    entity.addPart("uid", new StringBody(Register_login.map.get("uid")));
    
    
    httpPost.setEntity(entity);
    HttpResponse response = httpClient.execute(httpPost,
      localContext);
    
    BufferedReader reader = new BufferedReader(
      new InputStreamReader(
        response.getEntity().getContent(), "UTF-8"));

    String sResponse = reader.readLine();
    Log.d("image", sResponse);
    return sResponse;
  
   } catch (Exception e) {
    // something went wrong. connection with the server error
   }
   return null;
  }

  @Override
  protected void onPostExecute(String result) {
   dialog.dismiss();
   Fragment fr=Fragment.instantiate(getActivity(), Profile_fragment.class.getName());
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, fr);
		transaction.commit();
  }

 }
 
}