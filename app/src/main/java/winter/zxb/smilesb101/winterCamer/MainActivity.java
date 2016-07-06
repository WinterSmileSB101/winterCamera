package winter.zxb.smilesb101.winterCamer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import Enum.*;
import Help.CameraPreview;
import us.pinguo.edit.sdk.PGEditImageLoader;
import us.pinguo.edit.sdk.base.PGEditResult;
import us.pinguo.edit.sdk.base.PGEditSDK;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.xml.sax.helpers.LocatorImpl;

public class MainActivity extends AppCompatActivity{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setVaules();
		/* 隐藏状态栏 */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* 隐藏标题栏 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		PGEditImageLoader.initImageLoader(this.getApplication());
		PGEditSDK.instance().initSDK(this.getApplication());
		setContentView(R.layout.activity_main);

//		Toolbar toolbar = ( Toolbar )findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = ( ViewPager )findViewById(R.id.container);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		/*TabLayout tabLayout = ( TabLayout )findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(mViewPager);*/

		/*FloatingActionButton fab = ( FloatingActionButton )findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				Snackbar.make(view,"Replace with your own action",Snackbar.LENGTH_LONG)
						.setAction("Action",null).show();
			}
		});*/

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if( id == R.id.action_settings ) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * 设置值
	 */
	public void setVaules()
	{
		StaticValues.MainActivity = this;
		StaticValues.mWM = this.getWindowManager();
		StaticValues.outMetrics = new DisplayMetrics();
		StaticValues.mWM.getDefaultDisplay().getMetrics(StaticValues.outMetrics);
		StaticValues.SCREEN_HEIGTH = StaticValues.outMetrics.heightPixels;
		StaticValues.SCREEN_WIDTH = StaticValues.outMetrics.widthPixels;
	}
	/**
	 * 权限请求回调方法
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults){
		Log.i("实例化","onRequestPermissionsResult: ");
		switch (requestCode)
		{
			case PremissionREQUSETCODE.CAMERA://GPS权限申请成功
				if (permissions[0].equals(Manifest.permission.CAMERA)
						&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
					//用户同意使用PremissionREQUSETCODE.CAMERA
					Log.i("实例化","111111111");
					ShowMessageInAPP("照相权限获取成功！");
					//打开照相机
					StaticValues.ImagesActivityCamera = new CameraPreview(StaticValues.MainActivity.getApplicationContext(),StaticValues.mSurfaceView);
				}else{
					//用户不同意，自行处理即可
					ShowMessageInAPP("照相权限获取失败相关操作无法进行！");
					finish();
				}

				break;
			case PremissionREQUSETCODE.WRITE_EXTERNAL_STORAGE://写入权限
				if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
						&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
					//用户同意使用PremissionREQUSETCODE.CAMERA
					ShowMessageInAPP("写入权限获取成功！");
				}else{
					//用户不同意，自行处理即可
					ShowMessageInAPP("写入权限获取失败相关操作无法进行！");
					finish();
				}
				break;
			case PremissionREQUSETCODE.READ_EXTERNAL_STORAGE:
				if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
						&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
					//用户同意使用PremissionREQUSETCODE.CAMERA
					ShowMessageInAPP("读取权限获取成功！");
				}else{
					//用户不同意，自行处理即可
					ShowMessageInAPP("读取权限获取失败相关操作无法进行！");
					finish();
				}
				break;
			case PremissionREQUSETCODE.MOUNT_UNMOUNT_FILESYSTEMS://装载文件系统
				if (permissions[0].equals(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
						&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
					//用户同意使用PremissionREQUSETCODE.CAMERA
					ShowMessageInAPP("装载文件系统权限获取成功！");
				}else{
					//用户不同意，自行处理即可
					ShowMessageInAPP("装载文件系统权限获取失败相关操作无法进行！");
					finish();
				}
				break;
			case PremissionREQUSETCODE.RECORD_AUDIO://录制声音
				if (permissions[0].equals(Manifest.permission.RECORD_AUDIO)
						&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
					//用户同意使用PremissionREQUSETCODE.CAMERA
					ShowMessageInAPP("录制声音权限获取成功！");
				}else{
					//用户不同意，自行处理即可
					ShowMessageInAPP("录制声音权限获取失败相关操作无法进行！");
					finish();
				}
				break;
			default:
				break;
		}
	}

	/**
	 * 显示提示消息
	 * @param Message
	 */
	public void ShowMessageInAPP(String Message)
	{
		Toast.makeText(this.getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Camer360美化
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(
			int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		//Log.i("Camera360里面的回调","onActivityResult: ");
		if (requestCode == PGEditSDK.PG_EDIT_SDK_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK) {

			PGEditResult editResult = PGEditSDK.instance().handleEditResult(data);

// 获取编辑后的缩略图
			Bitmap thumbNail = editResult.getThumbNail();
// 获取编辑后的大图路径
			String resultPhotoPath = editResult.getReturnPhotoPath();
		}

		if (requestCode == PGEditSDK.PG_EDIT_SDK_REQUEST_CODE
				&& resultCode == PGEditSDK.PG_EDIT_SDK_RESULT_CODE_CANCEL) {
//用户取消编辑
		}

		if (requestCode == PGEditSDK.PG_EDIT_SDK_REQUEST_CODE
				&& resultCode == PGEditSDK.PG_EDIT_SDK_RESULT_CODE_NOT_CHANGED) {
// 照片没有修改
		}
	}


}
