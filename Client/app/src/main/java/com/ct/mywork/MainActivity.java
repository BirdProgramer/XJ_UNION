package com.ct.mywork;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static String  TAG="MainActivity";
    private View headerView;
    private TextView textView_name;
    private TextView textView_email;
    private TextView textView_credit;
    public  Handler uiHandler=new Handler(){
        public void handleMessage(Message msg){
            Log.e(TAG,msg.toString());
            switch (msg.what){
                case 1:                                                    //reset nav_header
                    resetNavHeader();
                    break;
                case 100:                                                   //connect error
                    AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("Warning");
                    dialog.setMessage("Sorry,the connection is cut off.Please check your network and login again.");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(MainActivity.this,FirstActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SocketThread.setMainActivity(this);
        headerView = navigationView.getHeaderView(0);
        textView_name=(TextView)headerView.findViewById(R.id.textView_name);
        textView_credit=(TextView)headerView.findViewById(R.id.textView_credit);
        textView_email=(TextView)headerView.findViewById(R.id.textView_email);
        textView_email.setText(ServerData.email);
        Log.e(TAG,String.valueOf(ServerData.credit));
        textView_credit.setText("credit:   "+String.valueOf(ServerData.credit));
        textView_name.setText(ServerData.name);
    }
    private void finishApp(){
        SocketThread.timer.cancel();
        this.finish();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Warning");
            dialog.setMessage("Are you sure to exit?");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishApp();
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }
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
        if (id == R.id.nav_distributeTask) {
            // Handle the TaskDistribution action

        } else if (id == R.id.nav_viewDistributeTask) {

        } else if (id == R.id.nav_viewReceivedTasks) {

        } else if (id == R.id.nav_editInformation) {
            Intent intent=new Intent(MainActivity.this,AlterInformattionActivity.class);
            startActivity(intent);
        }else if(id==R.id.nav_editPassword){
            Intent intent=new Intent(MainActivity.this,AlterPasswordActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void resetNavHeader(){
        textView_email.setText(ServerData.email);
        textView_credit.setText("credit:   "+String.valueOf(ServerData.credit));
        textView_name.setText(ServerData.name);
    }
}
