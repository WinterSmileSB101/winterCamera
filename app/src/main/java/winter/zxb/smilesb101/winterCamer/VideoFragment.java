package winter.zxb.smilesb101.winterCamer;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import Enum.*;
import Help.CameraPreview;
import Help.PremissionGETandCHECK;
import SelfWidget.RoundImageView;

/**
 * 录像Fragment类
 * Created by Administrator on 2016/6/30.
 */
public class VideoFragment extends Fragment{

	private Camera mCamera;// Camera对象
	private ImageView mButton;// 右侧条框，点击出发保存图像（拍照）的事件
	private SurfaceView mSurfaceView;//预览视图
	private RoundImageView store_ImageView;//保存后的文件的小的预览图
	private ImageView share_btn;//分享按钮
	private ImageView send_image_btn;//发送图像按钮
	private ImageView take_image_btn;//拍照按钮
	private CameraPreview cameraPreview;//照相逻辑对象
	private Activity mAct;
	public View mView;
	public PremissionGETandCHECK PREMISSION_REQUEST;
	public Animation mAni;

	public VideoFragment(Activity activity){
		mAct = activity;
		PREMISSION_REQUEST = new PremissionGETandCHECK(mAct);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState){
		Log.i("录像创建","onCreate: ");
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.my_images_preview,container,false);
		mView = v;
		store_ImageView = (RoundImageView)v.findViewById(R.id.Store_Image_Preview);
		mSurfaceView = (SurfaceView)v.findViewById(R.id.PreviewImage);
		take_image_btn = (ImageView)v.findViewById(R.id.take_image_btn);
		take_image_btn.setOnClickListener(take_Image);
		mAni = AnimationUtils.loadAnimation(mAct,R.anim.my_btn_click);
		if(PREMISSION_REQUEST.checkPremissionAndRequest(new String[]{PremissionEnum.CAMERA,
				PremissionEnum.MOUNT_UNMOUNT_FILESYSTEMS,PremissionEnum.WRITE_EXTERNAL_STORAGE,PremissionEnum.READ_EXTERNAL_STORAGE},new int[]{PremissionREQUSETCODE.CAMERA,PremissionREQUSETCODE.MOUNT_UNMOUNT_FILESYSTEMS,PremissionREQUSETCODE.WRITE_EXTERNAL_STORAGE,
				PremissionREQUSETCODE.READ_EXTERNAL_STORAGE}));
		cameraPreview = new CameraPreview(mAct.getApplicationContext(),mSurfaceView);
		return v;
	}

	/**
	 * 拍照（快门）的事件监听
	 */
	View.OnClickListener take_Image = new View.OnClickListener(){
		@Override
		public void onClick(View view){
			take_image_btn.startAnimation(mAni);
			//cameraPreview.takePicture();
		}
	};
}
