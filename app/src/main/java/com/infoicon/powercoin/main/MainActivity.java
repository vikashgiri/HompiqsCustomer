package com.infoicon.powercoin.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.infoicon.powercoin.bottomNavigation.App;
import com.infoicon.powercoin.bottomNavigation.CardListResponce;
import com.infoicon.powercoin.bottomNavigation.fragment.HealthTipsFragment;
import com.infoicon.powercoin.bottomNavigation.fragment.HomesFragment;
import com.infoicon.powercoin.login.CityListActivitys;
import com.infoicon.powercoin.login.LoginActivity;
import com.infoicon.powercoin.navigationdrawer.fragments.AboutUsFragment;
import com.infoicon.powercoin.navigationdrawer.fragments.TermsAndConditionFragment;
import com.infoicon.powercoin.utils.BottomNavigationViewHelper;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.MyClass;
import com.infoicon.powercoin.utils.SavePref;
import com.infoicon.powercoin.utils.UpdateCardListInterface;
import com.razorpay.Checkout;

import java.util.ArrayList;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ActivityMainBinding;


/**
 * Created by info on 23/2/18.
 */

public class MainActivity extends AppCompatActivity implements UpdateCardListInterface
{
   // index to identify current nav menu item
    public static int navItemIndex = 0;
    private static final String TAG_HOME = "home";
    private ActivityMainBinding activityMainBinding;
    private static final String TAG = MyClass.class.getSimpleName();
    Toolbar toolbar;
    public static String CURRENT_TAG = TAG_HOME;
    // toolbar titles respected to selected nav menu item
    /*  Method for Navigation View item selection  */

    private void onMenuItemSelected() {
        activityMainBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                android.support.v4.app.Fragment fragment;
                Bundle bundle;
                //Closing drawer on item click
                activityMainBinding.mainToolbar.cityName.setVisibility(View.GONE);
                activityMainBinding.mainToolbar.txtTitle.setVisibility(View.VISIBLE);
                activityMainBinding.drawer.closeDrawers();
                switch (item.getItemId()) {
                   /* case R.id.nav_home:
                        activityMainBinding.drawer.closeDrawers();
                        activityMainBinding.appBar.bottomNavigation.setSelectedItemId(R.id.navigation_home);
                        break;*/
                    case R.id.nav_about_us:
                        fragment =new AboutUsFragment();
                        HelperMethods.loadFragment(MainActivity.this,fragment);
                        activityMainBinding.mainToolbar.txtTitle.setText(getString(R.string.label_about_us));
                        break;
                    case R.id.nav_share:
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
   "https://play.google.com/store/apps/details?id=com.ahoy.ureward");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));
                        activityMainBinding.mainToolbar.txtTitle.setText(getString(R.string.label_share));
                        break;

                   /* case R.id.nav_coins_help:

                        fragment =new AboutUsFragment();
                        HelperMethods.loadFragment(MainActivity.this,fragment);
                        activityMainBinding.mainToolbar.txtTitle.setText(getString(R.string.label_help));

                        break;*/
                    case R.id.nav_notification:

                        Intent intents = new Intent(MainActivity.this, NotificationActivity.class);
// set the new task and clear flags
                            startActivity(intents);
                        break;
                    /*case R.id.nav_rate_us:

                        activityMainBinding.mainToolbar.txtTitle.setText(getString(R.string.label_rate_us));
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse("market://details?id=com.ahoy.ureward")));
                        break;*/
                    case R.id.nav_logout:
                        Menu menu = activityMainBinding.navigationView.getMenu();
                        MenuItem log_out = menu.findItem(R.id.nav_logout);
                        if(log_out.getTitle().toString().equalsIgnoreCase("Logout")) {
                            log_out.setTitle("Login");
                            SavePref.saveStringPref(MainActivity.this, SavePref.is_loogedin, "false");
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
// set the new task and clear flags
                            i.putExtra("logout","logout");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                        else
                        {
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
// set the new task and clear flags
                            startActivity(i);
                        }
                        break;
                    case R.id.nav_coins_tearms_and_condition:

                        fragment =new TermsAndConditionFragment();
                        HelperMethods.loadFragment(MainActivity.this,fragment);
                        activityMainBinding.mainToolbar.txtTitle.setText(getString(R.string.label_tearms_and_condition));
                       break;

                    case R.id.nav_call_us:

                        onCall();

                                         break;

                    case R.id.navigation_prescription_list:
                        if(SavePref.getPref(MainActivity.this,SavePref.is_loogedin).equalsIgnoreCase("true")) {
                            Intent intent = new Intent(MainActivity.this, ShowPrescriptionsActivity.class);
                            startActivity(intent);
                        }
                        else {

                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }
break;
                    /*case R.id.nav_label_change_password:
                        if(SavePref.getPref(MainActivity.this,SavePref.is_loogedin).equalsIgnoreCase("true")) {
                            Intent intent = new Intent(MainActivity.this, ChangePassword.class);
                            startActivity(intent);

                        }
                        else {

                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }

                        break;*/


                }
                return false;
            }
        });
    }

    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    123);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:12345678901")));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    Toast.makeText(getApplicationContext(), "Call Permission Not Granted", Toast.LENGTH_SHORT).show();
                }
            break;
            default:
                break;
        }
    }


    public void clearBackStackInclusive()
    {
    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, getLayoutResource());
      // Initializing Drawer Layout and ActionBarToggle
        Checkout.preload(getApplicationContext());

        ImageView slider = (ImageView) findViewById(R.id.toolbar_back);
activityMainBinding.mainToolbar.cityName.setText(""+ SavePref.getPref(MainActivity.this,SavePref.city_locality));
        slider.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                activityMainBinding.drawer.openDrawer(Gravity.LEFT);
            }
        });

        activityMainBinding.mainToolbar.cartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    if(((App)getApplication()).getCardList().size()>0)
                    {
                        Intent intent = new Intent(MainActivity.this, CartActivity.class);
                        startActivity(intent);
                    }

                    else
                    {
                        HelperMethods.showSnackBar(MainActivity.this,"Please Select " +
                                "Test First");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });
        activityMainBinding.mainToolbar.cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityListActivitys.class);
                startActivity(intent);
            }
        });

    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, activityMainBinding.drawer, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        activityMainBinding.drawer.addDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        onMenuItemSelected();
        setListnerOnToggelMenu();
      BottomNavigationViewHelper.removeShiftMode(activityMainBinding.appBar.bottomNavigation);
        activityMainBinding.appBar.bottomNavigation.
                setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        activityMainBinding.appBar.bottomNavigation.setSelectedItemId(R.id.navigation_home);
    }

    private void setListnerOnToggelMenu()
    {
        activityMainBinding.mainToolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainBinding.drawer.openDrawer(Gravity.LEFT);
            }
        });
        HelperMethods.getCartItems(MainActivity.this);
    }

    protected int getLayoutResource() {
        return R.layout.activity_main;
    }



    @Override
    protected void onResume() {
        super.onResume();
        activityMainBinding.mainToolbar.attachement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onCall();
            }
        });
        try
        {
            activityMainBinding.mainToolbar.cartCount.setText("" + ((App) this.getApplication()).getCardList().size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        };

        // get menu from navigationView
        Menu menu = activityMainBinding.navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem log_out = menu.findItem(R.id.nav_logout);
        if (SavePref.getPref(MainActivity.this,SavePref.is_loogedin).equalsIgnoreCase("true")) {
            log_out.setTitle("Logout");
        }
        else {
            log_out.setTitle("Login");
        }

    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fm = getSupportFragmentManager();
        int count=fm.getBackStackEntryCount();
        if (count == 1)
        {
            super.onBackPressed();
            finish();
        }
        else
            {
            fm.popBackStack();
        }
    }

    void ClearStack()
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
         clearBackStackInclusive();
            android.support.v4.app.Fragment fragment;
            activityMainBinding.mainToolbar.cityName.setVisibility(View.GONE);
            activityMainBinding.mainToolbar.txtTitle.setVisibility(View.VISIBLE);

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //  toolbar.setTitle("Shop");
                    fragment = new HomesFragment();
                    HelperMethods.loadFragment(MainActivity.this, fragment);
                    activityMainBinding.mainToolbar.txtTitle.setText(getString(R.string.home));
                    activityMainBinding.mainToolbar.cityName.setVisibility(View.VISIBLE);
                    activityMainBinding.mainToolbar.txtTitle.setVisibility(View.GONE);

                    return true;
                case R.id.navigation_my_account:
                    // toolbar.setTitle("My Gifts");
                    if (SavePref.getPref(MainActivity.this, SavePref.is_loogedin).equalsIgnoreCase("true")) {
                        fragment = new UpdateProfile();
                        HelperMethods.loadFragment(MainActivity.this, fragment);
                        activityMainBinding.mainToolbar.txtTitle.setText(getString(R.string.menu_bottom_label_my_account));
                        return true;
                    } else {

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }
                    return false;

                case R.id.navigation_explore:
                    ///toolbar.setTitle("Cart");
                    fragment = new SearchFragment();
                    HelperMethods.loadFragment(MainActivity.this, fragment);
                    activityMainBinding.mainToolbar.txtTitle.setText(getString(R.string.menu_bottom_label_explore));

                    return true;
                case R.id.navigation_health_tips:
                    // toolbar.setTitle("Profile");
                    fragment = new HealthTipsFragment();
                    HelperMethods.loadFragment(MainActivity.this, fragment);
                    activityMainBinding.mainToolbar.txtTitle.setText(getString(R.string.menu_bottom_label_health_tips));
                    return true;

                case R.id.navigation_my_order:
                    if (SavePref.getPref(MainActivity.this, SavePref.is_loogedin).equalsIgnoreCase("true")) {
                        fragment = new ViewAndTrackOrderActivity();
                        HelperMethods.loadFragment(MainActivity.this, fragment);
                        activityMainBinding.mainToolbar.txtTitle.setText(getString(R.string.menu_my_order));
                    } else {

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }



                       return true;
           }
            return false;
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        try
        {
            SearchTestFragments searchTestFragments=new SearchTestFragments();
            SearchTestFragments test = (SearchTestFragments) getSupportFragmentManager().findFragmentByTag( searchTestFragments.getClass().getName());
                //DO STUFF
                test.refress();
                   }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCard(ArrayList<CardListResponce> cardListResponceArrayList) {
        SearchTestFragments searchTestFragments=new SearchTestFragments();
        SearchTestFragments test = (SearchTestFragments) getSupportFragmentManager().findFragmentByTag( searchTestFragments.getClass().getName());
        if (test != null) {
            //DO STUFF
            test.refress();
        }

        activityMainBinding.mainToolbar.cartCount.setText(""+cardListResponceArrayList.size());



    }
}