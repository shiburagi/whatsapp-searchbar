package com.app.infideap.whatsappsearchtoolbar;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.app.infideap.whatsappsearchtoolbar.list.ItemFragment;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private AppBarLayout appBar;
    private AppBarLayout searchAppBar;
    private ViewPager viewPager;
    private Toolbar searchToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBar = (AppBarLayout) findViewById(R.id.appBar);
        searchAppBar = (AppBarLayout) findViewById(R.id.appBar_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        initSearchBar();
        initTabLayout();
        initViewPager();

        tabLayout.getTabAt(1).select();
    }

    private void initSearchBar() {
        searchToolBar = (Toolbar) findViewById(R.id.toolbar_search);
        if (searchToolBar != null) {
            searchToolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
//            searchToolBar.setVisibility(View.GONE);
            searchAppBar.setVisibility(View.GONE);
            searchToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideSearchBar();
                }
            });
        }
    }


    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = ItemFragment.newInstance(1);
                return fragment;
            }

            @Override
            public int getCount() {
                return tabLayout.getTabCount();
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        TabLayout.Tab[] tabs = {
                tabLayout.newTab().setText(R.string.calls),
                tabLayout.newTab().setText(R.string.chats),
                tabLayout.newTab().setText(R.string.contacts),
        };

        for (TabLayout.Tab tab : tabs) {
            tabLayout.addTab(tab);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getSupportActionBar().setTitle(tab.getText());
                viewPager.setCurrentItem(tab.getPosition());

                if (searchAppBar.getVisibility() == View.VISIBLE)
                    hideSearchBar();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                getSupportActionBar().setTitle(tab.getText());
            }
        });
    }


    private void showSearchBar() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(appBar, "translationY", -tabLayout.getHeight()),
                ObjectAnimator.ofFloat(viewPager, "translationY", -tabLayout.getHeight()),
                ObjectAnimator.ofFloat(appBar, "alpha", 0)
        );
        set.setDuration(100).addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                appBar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        set.start();


        int cx = toolbar.getWidth() - toolbar.getHeight() / 2;
        int cy = (toolbar.getTop() + toolbar.getBottom()) / 2;

        int dx = Math.max(cx, toolbar.getWidth() - cx);
        int dy = Math.max(cy, toolbar.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        final Animator animator;
        animator = io.codetail.animation.ViewAnimationUtils
                .createCircularReveal(searchAppBar, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(200);
        searchAppBar.setVisibility(View.VISIBLE);
        animator.start();
    }


    private void hideSearchBar() {

        int cx = toolbar.getWidth() - toolbar.getHeight() / 2;
        int cy = (toolbar.getTop() + toolbar.getBottom()) / 2;

        int dx = Math.max(cx, toolbar.getWidth() - cx);
        int dy = Math.max(cy, toolbar.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        Animator animator;
        animator = io.codetail.animation.ViewAnimationUtils
                .createCircularReveal(searchAppBar, cx, cy, finalRadius, 0);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                searchAppBar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animator.start();


        appBar.setVisibility(View.VISIBLE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(appBar, "translationY", 0),
                ObjectAnimator.ofFloat(appBar, "alpha", 1),
                ObjectAnimator.ofFloat(viewPager, "translationY", 0)
        );
        set.setDuration(100).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            showSearchBar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchAppBar.getVisibility() == View.VISIBLE)
            hideSearchBar();
        else
            super.onBackPressed();

    }
}
