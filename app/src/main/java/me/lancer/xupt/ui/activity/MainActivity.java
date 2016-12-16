package me.lancer.xupt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wuxiaolong.androidutils.library.LogUtil;
import com.wuxiaolong.androidutils.library.SharedPreferencesUtil;

import java.io.File;

import me.lancer.xupt.R;
import me.lancer.xupt.ui.application.ApplicationInstance;
import me.lancer.xupt.ui.application.ApplicationParameter;
import me.lancer.xupt.ui.fragment.CetFragment;
import me.lancer.xupt.ui.fragment.XuptEduFragment;
import me.lancer.xupt.ui.fragment.XuptLibFragment;
import me.lancer.xupt.ui.view.CircleImageView;

public class MainActivity extends BaseActivity {

    ApplicationInstance app = new ApplicationInstance();

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Fragment currentFragment;

    private int currentIndex;

    private final String root = Environment.getExternalStorageDirectory() + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (ApplicationInstance) this.getApplication();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initNavigationViewHeader();
        initFragment(savedInstanceState);
    }

    private void initFragment(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        if (savedInstanceState == null) {
            currentFragment = new XuptEduFragment();
            bundle.putInt("index", 0);
            currentFragment.setArguments(bundle);
            switchContent(currentFragment);
        } else {
            currentIndex = savedInstanceState.getInt(ApplicationParameter.CURRENT_INDEX);
            currentFragment = new XuptEduFragment();
            switch (this.currentIndex) {
                case 0:
                    bundle.putInt("index", currentIndex);
                    currentFragment.setArguments(bundle);
                    break;
                case 1:
                    bundle.putInt("index", currentIndex);
                    currentFragment.setArguments(bundle);
                    break;
                case 2:
                    bundle.putInt("index", currentIndex);
                    currentFragment.setArguments(bundle);
                    break;
            }
            switchContent(currentFragment);
        }
    }

    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            actionBarDrawerToggle.syncState();
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
        }
    }

    private void initNavigationViewHeader() {
        navigationView = (NavigationView) findViewById(R.id.navigation);
        View view = navigationView.inflateHeaderView(R.layout.drawer_header);
        CircleImageView civHead = (CircleImageView) view.findViewById(R.id.civ_head);
        civHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File courseFile = new File(root + "me.lancer.xupt/course_" + app.getNumber());
                File scoreFile = new File(root + "me.lancer.xupt/score_" + app.getNumber());
                File userFile = new File(root + "me.lancer.xupt/user_" + app.getNumber());
                if (courseFile.exists() && scoreFile.exists() && userFile.exists()) {
                    courseFile.delete();
                    scoreFile.delete();
                    userFile.delete();
                }
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });
        TextView tvHead = (TextView) view.findViewById(R.id.tv_head);
        if (app.getName() != null) {
            tvHead.setText(app.getName());
        } else {
            tvHead.setText("点击头像登录");
        }
        tvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File courseFile = new File(root + "me.lancer.xupt/course_" + app.getNumber());
                File scoreFile = new File(root + "me.lancer.xupt/score_" + app.getNumber());
                File userFile = new File(root + "me.lancer.xupt/user_" + app.getNumber());
                if (courseFile.exists() && scoreFile.exists() && userFile.exists()) {
                    courseFile.delete();
                    scoreFile.delete();
                    userFile.delete();
                }
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelected());
    }

    class NavigationItemSelected implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            Bundle bundle = new Bundle();
            drawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.navigation_item_1:
                    currentIndex = 0;
                    menuItem.setChecked(true);
                    currentFragment = new XuptEduFragment();
                    bundle.putInt("index", currentIndex);
                    currentFragment.setArguments(bundle);
                    switchContent(currentFragment);
                    return true;
                case R.id.navigation_item_2:
                    currentIndex = 1;
                    menuItem.setChecked(true);
                    currentFragment = new XuptLibFragment();
                    bundle.putInt("index", currentIndex);
                    currentFragment.setArguments(bundle);
                    switchContent(currentFragment);
                    return true;
                case R.id.navigation_item_3:
                    currentIndex = 2;
                    menuItem.setChecked(true);
                    currentFragment = new CetFragment();
                    bundle.putInt("index", currentIndex);
                    currentFragment.setArguments(bundle);
                    switchContent(currentFragment);
                    return true;
                case R.id.navigation_item_night:
                    SharedPreferencesUtil.setBoolean(mActivity, ApplicationParameter.ISNIGHT, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                    return true;
                case R.id.navigation_item_day:
                    SharedPreferencesUtil.setBoolean(mActivity, ApplicationParameter.ISNIGHT, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                    return true;
                default:
                    return true;
            }
        }
    }

    public void switchContent(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment).commit();
        invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtil.d("onSaveInstanceState=" + currentIndex);
        outState.putInt(ApplicationParameter.CURRENT_INDEX, currentIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_bottomsheetdialog:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
