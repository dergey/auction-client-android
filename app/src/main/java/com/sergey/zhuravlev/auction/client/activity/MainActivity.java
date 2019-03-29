package com.sergey.zhuravlev.auction.client.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.adapter.TabsFragmentAdapter;
import com.sergey.zhuravlev.auction.client.client.Client;
import com.sergey.zhuravlev.auction.client.client.SimpleCallback;
import com.sergey.zhuravlev.auction.client.constrain.RequestActivityCodes;
import com.sergey.zhuravlev.auction.client.dto.AccountResponseDto;
import com.sergey.zhuravlev.auction.client.dto.UserDto;

import org.springframework.http.HttpHeaders;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ContextWithCallback {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TabsFragmentAdapter tabsAdapter;
    private TextView emailView, nameView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        final View loginHeader = navigationView.inflateHeaderView(R.layout.login_header_main);
        final View accountHeader = navigationView.inflateHeaderView(R.layout.account_header_main);
        navigationView.removeHeaderView(accountHeader);

        TextView loginView = loginHeader.findViewById(R.id.login);
        emailView = accountHeader.findViewById(R.id.emailView);
        nameView = accountHeader.findViewById(R.id.nameView);
        imageView = accountHeader.findViewById(R.id.imageView);

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.removeHeaderView(loginHeader);
                navigationView.addHeaderView(accountHeader);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, RequestActivityCodes.LOGIN_REQUEST);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent()
                        .setType("image/*")
                        .setAction(Intent.ACTION_GET_CONTENT);
                MainActivity.this.startActivityForResult(Intent.createChooser(intent, "Select a image"), RequestActivityCodes.IMAGE_SELECT_REQUEST);
            }
        });

        Client.getInstance().init(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initTabs();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestActivityCodes.LOGIN_REQUEST:
                if (resultCode != RESULT_OK) break;
                Client.getInstance().getCurrentUser(new SimpleCallback<UserDto>() {
                    @Override
                    public void onResponse(UserDto response) {
                        loadUser(response);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        MainActivity.this.showErrorMessage("User load error", t);
                    }
                });
                break;
            case RequestActivityCodes.IMAGE_SELECT_REQUEST:
                if (resultCode != RESULT_OK) break;
                Uri selectedFile = data.getData();
                Client.getInstance().imageUpload(selectedFile, new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        if (response.code() == 201) {
                            String url = response.headers().get(HttpHeaders.LOCATION);
                            String name = null;
                            if (url != null && !url.isEmpty()) {
                                name = url.substring(url.lastIndexOf('/') + 1);
                            }
                            Client.getInstance().accountUpdatePhoto(name, new SimpleCallback<AccountResponseDto>() {
                                @Override
                                public void onResponse(AccountResponseDto response) {
                                    loadUser(Client.getInstance().getCurrentUser());
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    MainActivity.this.showErrorMessage("Error while update account photo!", t);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                        MainActivity.this.showErrorMessage("Error while upload account photo!", t);
                    }
                });
                break;
        }
    }

    public void loadUser(UserDto user) {
        emailView.setText(user.getEmail());
        imageView.setVisibility(View.VISIBLE);
        nameView.setVisibility(View.VISIBLE);
        if (user.getAccount() != null) {
            nameView.setText(String.format("%s %s", user.getAccount().getFirstname(), user.getAccount().getLastname()));

            if (user.getAccount().getPhoto() != null) {
                Client.getInstance().imageDownload(user.getAccount().getPhoto(), new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                            imageView.setImageBitmap(bmp);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });
            }

        }
        tabsAdapter.getCategoriesFragment().refresh();
    }

    private void initTabs() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        tabsAdapter = new TabsFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, CreateLotActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showConnectionError() {
        Snackbar.make(drawer, R.string.error_unable_to_connect, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage(String message, Throwable throwable) {
        Snackbar.make(drawer, message, Snackbar.LENGTH_LONG).show();
        Log.w("MainActivity", throwable);
    }

}
