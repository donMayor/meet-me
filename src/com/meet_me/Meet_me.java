package com.meet_me;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Meet_me extends ActionBarActivity{


	static ActionBar  actionbar;
	static Activity meet_activity;
	Register_login object;
	
	//navigation drawer
	 	private DrawerLayout mDrawerLayout;
	    private ListView mDrawerList;
	    private ActionBarDrawerToggle mDrawerToggle;

	    private CharSequence mDrawerTitle;
	    private CharSequence mTitle;
	    private String[] label={"home","contact", "profile", "help", "about", "exit"};
	    private String[]  drawer_menu={"Meet@me Home", "Contact Log","View Profile","help", "About", "sign out"};
		public static boolean is_open=false;
	    
	// ListAdapter Event_adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meet_me);
			object=new Register_login();
	       mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	         mDrawerList = (ListView) findViewById(R.id.left_drawer);
	        
	         // Set the adapter for the list view
	         mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	        /* mDrawerList.setAdapter(new ArrayAdapter<String>(this,
	                android.R.layout.simple_list_item_1, mPlanetTitles));*/
	         // Set the list's click listener
	        
	         mDrawerList.setAdapter(new Meet_me.adapter(this,drawer_menu, label));
	         mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	         
	       
		  actionbar = this.getSupportActionBar();
		  meet_activity=getParent();
         actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


         
         ActionBar.Tab Event = actionbar.newTab()
                 .setText("Event")
                 .setTabListener(new MyTabsListener<Event_search_fragement>(
                         this, "Events", Event_search_fragement.class));
	
         ActionBar.Tab user= actionbar.newTab()
                 .setText("Users")
                 .setTabListener(new MyTabsListener<User_fragment>(
                         this, "Users", User_fragment.class));
         ActionBar.Tab Recent = actionbar.newTab()
                 .setText("Requests")
                 .setTabListener(new MyTabsListener<Recent_fragment>(
                         this, "Requests", Recent_fragment.class));
         ActionBar.Tab contact_list = actionbar.newTab()
                 .setText("Contacts")
                 .setTabListener(new MyTabsListener<Contact_list_Frageent>(
                         this, "Contacts", Contact_list_Frageent.class));
         
         ActionBar.Tab profile = actionbar.newTab()
                 .setText("Profile")
                 .setTabListener(new MyTabsListener<Profile_fragment>(
                         this, "Profile", Profile_fragment.class));
         // add the tabs to the action bar
         actionbar.addTab(Event);
         actionbar.addTab(user);
         actionbar.addTab(Recent);
         actionbar.addTab(contact_list);
         actionbar.addTab(profile);
         
         actionbar.setDisplayHomeAsUpEnabled(true);
         actionbar.setHomeButtonEnabled(true);
         
         mDrawerToggle = new ActionBarDrawerToggle(
                 this,                  /* host Activity */
                 mDrawerLayout,         /* DrawerLayout object */
                 R.drawable.nav,  /* nav drawer image to replace 'Up' caret */
                 R.string.drawer_open,  /* "open drawer" description for accessibility */
                 R.string.drawer_close /* "close drawer" description for accessibility */
                 ) {
             public void onDrawerClosed(View view) {
            	 is_open=false;
                 getSupportActionBar().setTitle(mTitle);
                 supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
             }

             public void onDrawerOpened(View drawerView) {
            	 
            	 is_open=true;
                 getSupportActionBar().setTitle(mDrawerTitle);
                 supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
             }
         };
         
         mDrawerLayout.setDrawerListener(mDrawerToggle);

         if(Register_login.profile_update){
         	
         	Fragment fr=Fragment.instantiate(this,Edit.class.getName());
      		FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
      		transaction.replace(R.id.content_frame, fr);
      		transaction.commit();
          }
         
	}
/*	
	 @Override
	    public boolean onPrepareOptionsMenu(Menu menu) {
	        // If the nav drawer is open, hide action items related to the content view
		 	
	        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	        menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
	     
	        return super.onPrepareOptionsMenu(menu);
	    }*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
			
		getMenuInflater().inflate(R.menu.meet_me, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		 if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
		
		return super.onOptionsItemSelected(item);
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void selectItem(int position) {
        // update the main content by replacing fragments
    	if(!Register_login.profile_update){
    	if(position==0){
    		Fragment fr=Fragment.instantiate(this,Event_search_fragement.class.getName());
    		FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
    		transaction.replace(R.id.content_frame, fr);
    		transaction.commit();
    	}
    	else if(position==1){
    		Fragment fr=Fragment.instantiate(this,Contact_list_Frageent.class.getName());
    		FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
    		transaction.replace(R.id.content_frame, fr);
    		transaction.commit();
    	}
    	else if(position==2){
    		
        	Fragment fr=Fragment.instantiate(this, Profile_fragment.class.getName());
    		FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
    		transaction.replace(R.id.content_frame, fr);
    		transaction.commit();
    	}
    	else if(position==3){
    		object.Getalert("Help", "1 Create an Event at Event Location\n2 Search for the Event within the Location\n3 Find Attendees in the Event\n4 Select Attendee to Exchange Business card",Meet_me.this);
    	}else if(position==4){
    		object.Getalert("About", "Meet@me is an Application that helps you Exchange Bussiness card easily with Attendees in an Event\nDeveloper: iQube Lab ", Meet_me.this);
    	}
    	else if(position==5){
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
	        alertDialog.setTitle("Log Out");
	        alertDialog.setMessage("You are about to Log out, \nContinue?");
	        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog,int which) {
	            	Register_login.no_permission=true;
	            	Register_login.is_SignOut_contact=true;
	            	Register_login.image_isset=true;
	            	Register_login.is_SignOut_event=true;
	            	Register_login.is_SignOut_recent=true;
	            	Event_search_fragement.is_Searched=false;
	            
	            Intent intent= new Intent(getApplicationContext(), Register_login.class);
	            startActivity(intent);
	            }
	        });
	  
	        // on pressing cancel button
	        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            
	            }
	        });
	  
	        // Showing Alert Message
	        alertDialog.show();
    	}
    	}else{
    	 	Toast.makeText(Meet_me.this, "Phone Number is compulsory, Update before proceeding", Toast.LENGTH_LONG).show();
    	}
    	mDrawerLayout.closeDrawer(mDrawerList);
    	
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	
	class MyTabsListener<T extends Fragment> implements ActionBar.TabListener {
		
			private Fragment mFragment;
		    private final Activity mActivity;
		    private final String mTag;
		    private final Class<T> mClass; 


        public MyTabsListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            
        }
        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
        
        		if(!Register_login.profile_update){
                  mFragment = Fragment.instantiate(mActivity, mClass.getName());
                  ft.replace(R.id.content_frame, mFragment);
                  if(is_open)
                  mDrawerLayout.closeDrawer(mDrawerList);
        		}else{
        		 	Toast.makeText(Meet_me.this, "Phone Number is compulsory, Update before proceeding", Toast.LENGTH_LONG).show();
        		}
        		
       }        

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        	 if (mFragment != null) {
                 // Detach the fragment, because another one is being attached
                 ft.detach(mFragment);
             }
        	 
        }
        
 /*       public  int getContentViewCompat() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH?
                       android.R.id.content : R.id.action_bar_activity_content;
        }
	*/
}
	
	public static void Selectiontab(int index){
		actionbar.setSelectedNavigationItem(index);
		
	}
	
	//Private class
	
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
	 
			if (s.equals("home")) {
			//	if(Register_login.picture==null)
				imageView.setImageResource(R.drawable.new_home);
				/*else
				imageView.setImageBitmap(Register_login.picture);*/
			} else if (s.equals("contact")) {
				imageView.setImageResource(R.drawable.new_contact);
			} else if (s.equals("profile")) {
				imageView.setImageResource(R.drawable.new_pic);
			}
			else if (s.equals("help")) {
				imageView.setImageResource(R.drawable.new_help);
			} else if (s.equals("about")) {
				imageView.setImageResource(R.drawable.new_about);
			} else if(s.equals("exit")){
				imageView.setImageResource(R.drawable.new_lock);
			} 
			
			return rowView;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		Toast.makeText(this, "Sign Out to exit Application", Toast.LENGTH_LONG).show();
	}
}
