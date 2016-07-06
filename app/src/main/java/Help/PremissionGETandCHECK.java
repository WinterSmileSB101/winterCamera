package Help;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import Enum.*;

import winter.zxb.smilesb101.winterCamer.MainActivity;

/**
 * 权限管理类
 * Created by Administrator on 2016/6/29.
 */
public class PremissionGETandCHECK extends AppCompatActivity{

	private Context mContext;
	/**
	 * 主活动
	 */
	private Activity MainActivity;


	public PremissionGETandCHECK(Activity activity)
	{
		MainActivity = activity;
	}
	/**
	 * 检查和请求权限
	 * @param Premission
	 * @param requestCode
	 */
	public boolean checkPremissionAndRequest(String[] Premission,int[] requestCode)
	{
		int j = 0;
		for(String i : Premission) {
			if(! checkPremission(i)) {
				requestPremission(new String[] {i},requestCode[j]);
				return false;
			}
			j++;
		}
		return true;
	}
	/**
	 * 检查权限
	 * @param Premission 权限
	 * @return 是否存在权限
	 */
	public boolean checkPremission(String Premission)
	{
		if ( (ContextCompat.checkSelfPermission(MainActivity, Premission)
				!= PackageManager.PERMISSION_GRANTED))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public boolean requestPremission(String[] Premission,int requestCode)
	{
		ActivityCompat.requestPermissions(MainActivity,Premission,requestCode);
		return true;
	}

	/**
	 * 权限请求回调方法
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults){
		switch (requestCode)
		{
			case PremissionREQUSETCODE.CAMERA://GPS权限申请成功
				if (permissions[0].equals(Manifest.permission.CAMERA)
						&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
					//用户同意使用PremissionREQUSETCODE.CAMERA
					ShowMessageInAPP("照相权限获取成功！");
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
		Toast.makeText(mContext, Message, Toast.LENGTH_SHORT).show();
	}
}
