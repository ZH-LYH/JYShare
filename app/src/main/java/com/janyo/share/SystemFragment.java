package com.janyo.share;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import com.janyo.share.util.PM;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import com.janyo.share.util.Share;
import com.janyo.share.util.FileOperation;
import android.widget.Toast;
import java.io.IOException;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Adapter;

public class SystemFragment extends Fragment
{
	private View mView;
	private ListView appListV;
	private TextView appPath, appName,versionName;
	private Context context;
	
	public SystemFragment(Context context)
	{
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mView = inflater.inflate(R.layout.fragment_userapp,null);
		appListV = (ListView) mView.findViewById(R.id.fragment_userlistview);

		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				List<Map<String, Object>> applist = new ArrayList<Map<String, Object>>();
				applist = new PM(getActivity()).getSystemAppList();
				final SimpleAdapter sa = new SimpleAdapter(getActivity(), applist, R.layout.view_app_listview, new String[]{"Name", "Dir", "icon", "VersionName"}, new int[]{R.id.tv_app_name, R.id.tv_app_package_name, R.id.iv_app_icon, R.id.tv_app_version_name});
				((Activity)context).runOnUiThread(new Runnable()
					{

						@Override
						public void run()
						{
							appListV.setAdapter(sa);
							sa.setViewBinder(new ViewBinder()
								{

									@Override
									public boolean setViewValue(View p1, Object p2, String p3)
									{
										if(p1 instanceof ImageView && p2 instanceof Drawable)
										{
											ImageView iv = (ImageView)p1;
											iv.setImageDrawable((Drawable)p2);
											return true;
										}
										else return false;
									}
								});
						}
				});
			}
		}).start();
		
		appListV.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					appPath = (TextView)p2.findViewById(R.id.tv_app_package_name);
					appName = (TextView)p2.findViewById(R.id.tv_app_name);
					versionName = (TextView)p2.findViewById(R.id.tv_app_version_name);
					doShare(appPath.getText().toString(), appName.getText().toString(),versionName.getText().toString());
				}
			});
		
		appListV.setOnItemLongClickListener(new OnItemLongClickListener()
		{

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					// TODO: Implement this method
					Toast.makeText(getActivity(),"LongClick",Toast.LENGTH_SHORT).show();
					return true;
				}

			
		});
			
		return mView;
	}
	
	private void doShare(String path, String name,String versionName)
	{
		try
		{
			if (new FileOperation(getActivity(), path, name, versionName).getOperationResult())
				Toast.makeText(getActivity(), "拷贝"+ name +"_" +versionName + "成功", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getActivity(), "拷贝" + name +"_" + versionName +"失败", Toast.LENGTH_SHORT).show();
		}
		catch (IOException e)
		{
			Toast.makeText(getActivity(), "Stream Operation Error", Toast.LENGTH_LONG);
		}
		new Share(getActivity(), "/data/data/com.janyo.share/files/" + name + "_" + versionName +".apk");
	}
}
