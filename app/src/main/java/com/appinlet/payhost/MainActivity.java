package com.appinlet.payhost;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.appinlet.payhost.Api.FollowUpApi;
import com.appinlet.payhost.Api.PayGateResponseCallback;
import com.appinlet.payhost.Api.SinglePaymentApi;
import com.appinlet.payhost.Api.TokenPaymentApi;
import com.appinlet.payhost.Api.VaultCreateApi;
import com.appinlet.payhost.Api.VaultDeleteApi;
import com.appinlet.payhost.Api.VaultLookupApi;
import com.appinlet.payhost.Api.WebPaymentApi;
import com.appinlet.payhost.Demo.CardPaymentFragment;
import com.appinlet.payhost.Demo.FollowUpRequestFragment;
import com.appinlet.payhost.Demo.TokenPaymentFragment;
import com.appinlet.payhost.Demo.VaultCreateFragment;
import com.appinlet.payhost.Demo.VaultDeleteFragment;
import com.appinlet.payhost.Demo.VaultLookupFragment;
import com.appinlet.payhost.Demo.WebPaymentFragment;
import com.appinlet.payhost.Model.Address;
import com.appinlet.payhost.Model.BillingDetails;
import com.appinlet.payhost.Model.Card;
import com.appinlet.payhost.Model.Customer;
import com.appinlet.payhost.Model.Language;
import com.appinlet.payhost.Model.Order;
import com.appinlet.payhost.Model.OrderItems;
import com.appinlet.payhost.Model.Redirect;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.content)
    FrameLayout content;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadFragment(CardPaymentFragment.newInstance());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        toolbar.setTitle(item.getTitle() + " Request");

        if (id == R.id.card) {
            loadFragment(CardPaymentFragment.newInstance());
        } else if (id == R.id.web) {
            loadFragment(WebPaymentFragment.newInstance());
        } else if (id == R.id.token) {
            loadFragment(TokenPaymentFragment.newInstance());
        } else if (id == R.id.vcreate) {
            loadFragment(VaultCreateFragment.newInstance());
        } else if (id == R.id.vdelete) {
            loadFragment(VaultDeleteFragment.newInstance());
        } else if (id == R.id.vlook) {
            loadFragment(VaultLookupFragment.newInstance());
        } else if (id == R.id.query) {
            loadFragment(FollowUpRequestFragment.newInstance());
        } else if (id == R.id.rvoid) {
            loadFragment(FollowUpRequestFragment.newInstance());
        } else if (id == R.id.st) {
            loadFragment(FollowUpRequestFragment.newInstance());
        } else if (id == R.id.refund) {
            loadFragment(FollowUpRequestFragment.newInstance());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
    }
}
