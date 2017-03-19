package me.lancer.xupt.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.PresenterFragment;
import me.lancer.xupt.mvp.loginedu.ILoginEduView;
import me.lancer.xupt.mvp.loginedu.LoginEduPresenter;
import me.lancer.xupt.ui.activity.MainActivity;

public class XuptEduFragment extends PresenterFragment<LoginEduPresenter> implements ILoginEduView {
    
    private Toolbar toolbar;
    private int index = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_xupt_edu, container, false);
    }

    @Override
    protected LoginEduPresenter onCreatePresenter() {
        return new LoginEduPresenter(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.edu_title);
        ((MainActivity) getActivity()).initDrawer(toolbar);
        initTabLayout(view);
        inflateMenu();
        initSearchView();
    }

    private void initTabLayout(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Fragment newfragment = new CourseFragment();
        Bundle data = new Bundle();
        data.putInt(getString(R.string.id), 0);
        data.putString(getString(R.string.title), getString(R.string.edu_course));
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, getString(R.string.edu_course));

        newfragment = new ScoreFragment();
        data = new Bundle();
        data.putInt(getString(R.string.id), 1);
        data.putString(getString(R.string.title), getString(R.string.edu_score));
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, getString(R.string.edu_score));

        newfragment = new UserEduFragment();
        data = new Bundle();
        data.putInt(getString(R.string.id), 2);
        data.putString(getString(R.string.title), getString(R.string.edu_info));
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, getString(R.string.edu_info));

        viewPager.setAdapter(adapter);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            index = bundle.getInt(getString(R.string.index));
        }
        viewPager.setCurrentItem(index, true);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void inflateMenu() {
        toolbar.inflateMenu(R.menu.menu_search);
    }

    private void initSearchView() {
        final SearchView searchView = (SearchView) toolbar.getMenu()
                .findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showToast(getString(R.string.lazy));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void showCheckCode(String cookie) {

    }

    @Override
    public void login(String cookie) {

    }

    @Override
    public void home(String number, String name) {

    }

    @Override
    public void showMsg(String log) {

    }

    @Override
    public void showLoad() {

    }

    @Override
    public void hideLoad() {

    }
}
