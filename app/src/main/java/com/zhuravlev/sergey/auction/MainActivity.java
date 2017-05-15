package com.zhuravlev.sergey.auction;


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

import com.zhuravlev.sergey.auction.adapter.TabsFragmentAdapter;
import com.zhuravlev.sergey.auction.dto.Lot;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    private TabsFragmentAdapter tabsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        initNavigationView();
        initTabs();
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
        tabsAdapter = new TabsFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);
        //Привязываем вкладки с "контентом"
        new AuctionTask().execute();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open,  R.string.view_navigation_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.actionNotificationItem:
                        showNotificationTab();
                }
                return true;
            }
        });
    }

    private void showNotificationTab() {
        viewPager.setCurrentItem(Constants.TAB_TWO);
    }

    private class AuctionTask extends AsyncTask<Void, Void, List<Lot>> {

        @Override
        protected List<Lot> doInBackground (Void... voids) {
            RestTemplate template = new RestTemplate();
            List<Lot> listLots = null;
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                Lot[] lots = template.getForObject(Constants.GET_ALL_LOTS, Lot[].class);
                Log.d("AuctionD", "Размер полученого массива " + lots.length);
                listLots = Arrays.asList(lots);
            } catch (Exception ignored) {
                Snackbar.make(findViewById(R.id.drawer_layout), R.string.error_unable_to_connect, Snackbar.LENGTH_LONG).show();
                Log.d("AuctionD", "Ошибка подключения к серверу - " + ignored.getLocalizedMessage());
            }

            return listLots;
        }

        @Override
        protected void onPostExecute(List<Lot> lotList) {
            if (lotList != null)
                tabsAdapter.setLotList(lotList);
            else
                Log.d("AuctionD", "lotList ссылается на несуществующий объект");
        }
    }

}
