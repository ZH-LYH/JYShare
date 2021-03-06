package com.janyo.share;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.janyo.share.MainActivity;
import com.janyo.share.util.CleanFileDir;
import java.io.File;
import android.os.Environment;
import com.janyo.share.util.DirExist;

public class MainActivity extends AppCompatActivity
{
	private Toolbar mToolbar;
	private TabLayout mTabLayout;
	private ViewPager mViewPager;

	private long oneClickTime;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		initView();
		new DirExist("JYShare");
    }
	
	private void initView()
	{
		mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
		mTabLayout = (TabLayout) findViewById(R.id.tl_main_tabs);
		mViewPager = (ViewPager) findViewById(R.id.vp_main_content);
		initToolBar();
		initMainContent();
	}
	
	private void initToolBar()
	{
        mToolbar.setTitle("JYShare");
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
	
	private void initMainContent()
	{
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment userFragment = new UserFragment();
        Fragment systemFragment = new SystemFragment();
        adapter.addFragment(userFragment, "User Apps");
        adapter.addFragment(systemFragment, "System Apps");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
	}
	
	@Override
	public void onBackPressed()
	{
		long doubleClickTime = System.currentTimeMillis();
        if (doubleClickTime - oneClickTime > 2000)
		{
            Toast.makeText(MainActivity.this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            oneClickTime = doubleClickTime;
        }
		else
		{
			CleanFileDir.cleanFiles(getApplicationContext());
			CleanFileDir.cleanCaches(getApplicationContext());
            System.exit(0);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.clear:
				CleanFileDir.cleanFiles(getApplicationContext());
				CleanFileDir.cleanCaches(getApplicationContext());
				break;
			case R.id.about:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("JanYo Studio\nDickLight\nZH_LYH\nCieyGet\nMystery0\nGsoy\nCopyright @2016-2017 JanYo Studio,All Right Reserved");
				builder.setPositiveButton("关闭",new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							p1.dismiss();
						}


					});
				builder.create().show();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
