package cn.dolphinsoft.hilife.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.dolphinsoft.hilife.R;
import cn.dolphinsoft.hilife.auth.activity.LoginActivity;
import cn.dolphinsoft.hilife.auth.domain.LoginEntity;
import cn.dolphinsoft.hilife.coupon.fragment.CouponFragment;
import cn.dolphinsoft.hilife.life.fragment.LifeFragment;
import cn.dolphinsoft.hilife.main.fragment.MainFragment;
import cn.dolphinsoft.hilife.order.fragment.OrderFragment;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnFragmentInteractionListener,CouponFragment.OnFragmentInteractionListener,
        LifeFragment.OnFragmentInteractionListener,OrderFragment.OnFragmentInteractionListener{

    private static final int LOGINACTIVITY = 1;
    private NavigationView navigationView;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_main_container,mainFragment);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        /* 判断是否登录 */
        String token = getCurrentUserToken();
        if(token.equals("")){
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_quit).setVisible(false);
        }else{
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_quit).setVisible(true);
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
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
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_coupon) {
        } else if (id == R.id.nav_life) {
        } else if (id == R.id.nav_order) {
        } else if (id == R.id.nav_setting) {

        }  else if (id == R.id.nav_quit) {
            logoutCurrentUser(getCurrentUserToken());
        }else if (id == R.id.nav_login) {
            Intent intent  = new Intent(MainActivity.this,LoginActivity.class);
            startActivityForResult(intent, LOGINACTIVITY);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getCurrentUserToken(){
        FinalDb db = FinalDb.create(MainActivity.this,"cn.dolphinsoft.hilife");

        List<LoginEntity> list = db.findAll(LoginEntity.class);

        if(list != null && list.size() > 0){
            return list.get(0).getToken();
        }else {
            return "";
        }
    }

    public void clearCurrentUserToken(){
        FinalDb db = FinalDb.create(MainActivity.this,"cn.dolphinsoft.hilife");

        db.deleteAll(LoginEntity.class);
    }

    public void logoutCurrentUser(String token){
        String url = (getString(R.string.contextPath) + getString(R.string.logout)).trim();
        StringEntity se = null;
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token",token);
            se = new StringEntity(jsonObject.toString());
        }catch(JSONException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(MainActivity.this,url, se,"application/json", new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(statusCode == 200){
                    try{
                        if(response.getString("code").equals("ACK")){
                            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_quit).setVisible(false);
                            clearCurrentUserToken();
                            Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, response.getString("code"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MainActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LOGINACTIVITY:
                if(resultCode == Activity.RESULT_OK){
                    navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_quit).setVisible(true);
                    Toast.makeText(this,data.getStringExtra("loginStatus").trim(),Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
