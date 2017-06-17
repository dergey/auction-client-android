package com.zhuravlev.sergey.auction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuravlev.sergey.auction.adapter.TabsFragmentAdapter;
import com.zhuravlev.sergey.auction.client.Client;
import com.zhuravlev.sergey.auction.dto.User;

import org.springframework.web.client.HttpClientErrorException;

public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;
    private Client client;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;

    private TabsFragmentAdapter tabsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        Client.createClient(this);
        client = Client.getClient();
        initToolbar();
        initNavigationView();
        initTabs();
        if (client.IsAuthorized()) {
            ProgressDialog dialog = ProgressDialog.show(this, "",
                    getString(R.string.loadmessage_signin), true);
            client.loadUser(dialog);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("Auction.Login", "Выход из приложения");
        client.saveSessionID();
        super.onDestroy();
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabsAdapter = new TabsFragmentAdapter(this, getSupportFragmentManager(), client);
        viewPager.setAdapter(tabsAdapter);
        //Привязываем вкладки с "контентом"
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open,  R.string.view_navigation_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.actionNotificationItem:
                        showNotificationTab();
                        return true;
                }

                if (client.IsAuthorized())
                    switch (item.getItemId()){
                        //TODO MAIN Добавить пункты меню и их активити
                        case R.id.actionCreateLot:
                            //NOSING
                            break;
                        case R.id.actionMyProfile:
                            showMyProfile();
                            break;
                    }
                    else showLoginForm();
                return true;
            }
        });
        ImageView imageLogout = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageLogout);
        TextView registerSinginView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView);
        registerSinginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginForm();
            }
        });
        imageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void showMyProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(User.class.getCanonicalName(), client.getUser());
        startActivity(intent);
    }

    private void showLoginForm(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void logout(){
        client.logout();
        navigationView.getHeaderView(0).findViewById(R.id.fullNameTextView).setVisibility(View.INVISIBLE);
        navigationView.getHeaderView(0).findViewById(R.id.imageLogout).setVisibility(View.INVISIBLE);
        TextView registerSinginView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView);
        registerSinginView.setText("Вход | Регестрация");
    }

    public void showConnectionError(){
        Snackbar.make(findViewById(R.id.gridView), R.string.error_unable_to_connect, Snackbar.LENGTH_LONG).show();
    }

    private void showNotificationTab() {
        viewPager.setCurrentItem(Constants.TAB_TWO);
    }

    public void loadUser(){
        TextView emailView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView);
        TextView fullnameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.fullNameTextView);
        ImageView imageLogout = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageLogout);

        imageLogout.setVisibility(View.VISIBLE);
        emailView.setText(client.getUser().getEmail());
        fullnameView.setVisibility(View.VISIBLE);
        fullnameView.setText(client.getUser().getFirstname() + " " + client.getUser().getLastname());
    }


}
