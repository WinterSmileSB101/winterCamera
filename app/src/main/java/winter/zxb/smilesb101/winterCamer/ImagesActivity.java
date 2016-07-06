package winter.zxb.smilesb101.winterCamer;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.health.TimerStat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import SelfAni.ProvertyAnimation;
import SelfWidget.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Timer;

import Enum.*;
import Help.*;
import SelfWidget.RoundImageView;

/**
 * 拍照活动类
 * Created by Administrator on 2016/6/29.
 */
public class ImagesActivity extends Fragment{

	/**
	 * 起始位置
	 */
	public static final int flash_POS = -28;
	/**
	 * 最后位置
	 */
	public static final int flash_off_POS = 180;
	/**
	 * 最后位置
	 */
	public static final int flash_on_POS = 580;
	/**
	 * 最后位置
	 */
	public static final int flash_auto_POS = 380;

	/**
	 * 是否是录像状态
	 */
	public static boolean ISVIDEO = false;
	private  CameraPreview cameraPreview;//照相逻辑对象
	private Camera mCamera;// Camera对象
	private ImageView mButton;// 右侧条框，点击出发保存图像（拍照）的事件
	private SurfaceView mSurfaceView;//预览视图
	private RoundImageView store_ImageView;//查看已经拍摄图片按钮
	private ImageView GoToVideobtn;//发送图像按钮
	private ImageView take_image_btn;//拍照按钮
	private ImageView effectsBtn;//特效按钮
	private TextView modelBtn;//模式选择按钮
	private ImageView switch_cameraBtn;//切换镜头按钮
	private ImageView flash_btn;//闪光灯按钮
	private TextView flash_off;//关闭闪光灯
	private TextView flash_auto;//自动闪光灯
	private TextView flash_on;//打开闪光灯
	private ImageView HDR_btn;//HDR按钮
	public ImageView take_image_Ring;//中间按钮的外圈圆环
	public ImageView ring_shape_L;//左边圆环
	public ImageView ring_shape_R;//右边圆环
	public ProgressBar horzationtal_Line;//下面显示框的水平分界线
	public LinearLayout bottom_tool_BG;//下面显示框的背景色
	public LinearLayout My_timer_Lin;//计时器主框架
	public TextView My_timer;//计时器
	public Handler handler;//
	private Activity mAct;
	public static View mView;
	public PremissionGETandCHECK PREMISSION_REQUEST;
	public Animation mAniclick;
	public Animation mAnitoRight;
	public Animation mAnitoCenter;
	private static int h1 = 0;
 	private static int h2 = 0;
	private static int timer1 = 0;
	/**
	 * 用于对焦检测的孩子数量
	 */
	public int childCount = 0;
	/**
	 * 录像时间
	 */
	private static int timer = 0;
	public ImagesActivity(Activity activity)
	{
		mAct = activity;
		new OrientationListener(StaticValues.MainActivity){
			@Override
			public void onOrientationChanged(int rotation){
				//Log.i("rotation",rotation+"");
				int Rrotation = 0;
				if(rotation >= 0 && rotation <= 15 || rotation <= 360 && rotation >= 345) {
					checkPath(StaticValues.ROTATE_PATH_FLAG_BOTTOM);
					Rrotation = 0;
				} else if(rotation <= 90 && rotation >= 75 || rotation >= 90 && rotation <= 105) {
					checkPath(StaticValues.ROTATE_PATH_FLAG_RIGHT);
					Rrotation = 270;
				} else if(rotation <= 180 && rotation >= 165 || rotation >= 180 && rotation <= 175) {
					checkPath(StaticValues.ROTATE_PATH_FLAG_TOP);
					Rrotation = 180;
				} else if(rotation <= 270 && rotation >= 255 || rotation >= 270 && rotation <= 265) {
					checkPath(StaticValues.ROTATE_PATH_FLAG_LEFT);
					Rrotation = 90;
				} else {
					return;
				}
				//注意图片旋转方向和手机屏幕方向相反
				if(Math.abs(StaticValues.Image_CUR_ROTATION )!= Rrotation) {
					String[] s = StaticValues.ROTATE_THROW_PATH.split("||");
					if(StaticValues.ROTATE_DIR=="ANTICLOCK") {
						//顺时针
						switch(s[0])
						{
							case StaticValues.ROTATE_PATH_FLAG_TOP:
								StaticValues.Image_CUR_ROTATION = 180;
								switch(s[1])
								{
									case StaticValues.ROTATE_PATH_FLAG_LEFT:
										Rrotation = 450;
										break;
									case StaticValues.ROTATE_PATH_FLAG_RIGHT:
										Rrotation = 270;
										break;
									case StaticValues.ROTATE_PATH_FLAG_BOTTOM:
										Rrotation = 360;
										break;
									default:
										break;
								}
								break;
							case StaticValues.ROTATE_PATH_FLAG_LEFT:
								StaticValues.Image_CUR_ROTATION = 90;
								switch(s[1])
								{
									case StaticValues.ROTATE_PATH_FLAG_TOP:
										Rrotation = 180;
										break;
									case StaticValues.ROTATE_PATH_FLAG_RIGHT:
										Rrotation = 270;
										break;
									case StaticValues.ROTATE_PATH_FLAG_BOTTOM:
										Rrotation = 360;
										break;
									default:
										break;
								}
								break;
							case StaticValues.ROTATE_PATH_FLAG_RIGHT:
								StaticValues.Image_CUR_ROTATION = 270;
								switch(s[1])
								{
									case StaticValues.ROTATE_PATH_FLAG_LEFT:
										Rrotation = 450;
										break;
									case StaticValues.ROTATE_PATH_FLAG_TOP:
										Rrotation = 540;
										break;
									case StaticValues.ROTATE_PATH_FLAG_BOTTOM:
										Rrotation = 360;
										break;
									default:
										break;
								}
								break;
							case StaticValues.ROTATE_PATH_FLAG_BOTTOM:
								StaticValues.Image_CUR_ROTATION = 0;
								switch(s[1])
								{
									case StaticValues.ROTATE_PATH_FLAG_LEFT:
										Rrotation = 90;
										break;
									case StaticValues.ROTATE_PATH_FLAG_RIGHT:
										Rrotation = 270;
										break;
									case StaticValues.ROTATE_PATH_FLAG_TOP:
										Rrotation = 180;
										break;
									default:
										break;
								}
								break;
							default:
								break;
						}
					}
					else if(StaticValues.ROTATE_DIR=="CLOCK")
					{
						//逆时针
						switch(s[0])
						{
							case StaticValues.ROTATE_PATH_FLAG_TOP:
								StaticValues.Image_CUR_ROTATION = 180;
								switch(s[1])
								{
									case StaticValues.ROTATE_PATH_FLAG_LEFT:
										Rrotation = 90;
										break;
									case StaticValues.ROTATE_PATH_FLAG_RIGHT:
										Rrotation = -90;
										break;
									case StaticValues.ROTATE_PATH_FLAG_BOTTOM:
										Rrotation = 0;
										break;
									default:
										break;
								}
								break;
							case StaticValues.ROTATE_PATH_FLAG_LEFT:
								StaticValues.Image_CUR_ROTATION = 90;
								switch(s[1])
								{
									case StaticValues.ROTATE_PATH_FLAG_TOP:
										Rrotation = -180;
										break;
									case StaticValues.ROTATE_PATH_FLAG_RIGHT:
										Rrotation = -90;
										break;
									case StaticValues.ROTATE_PATH_FLAG_BOTTOM:
										Rrotation = 0;
										break;
									default:
										break;
								}
								break;
							case StaticValues.ROTATE_PATH_FLAG_RIGHT:
								StaticValues.Image_CUR_ROTATION = 270;
								switch(s[1])
								{
									case StaticValues.ROTATE_PATH_FLAG_LEFT:
										Rrotation = 90;
										break;
									case StaticValues.ROTATE_PATH_FLAG_TOP:
										Rrotation = 180;
										break;
									case StaticValues.ROTATE_PATH_FLAG_BOTTOM:
										Rrotation = 0;
										break;
									default:
										break;
								}
								break;
							case StaticValues.ROTATE_PATH_FLAG_BOTTOM:
								StaticValues.Image_CUR_ROTATION = 0;
								switch(s[1])
								{
									case StaticValues.ROTATE_PATH_FLAG_LEFT:
										Rrotation = -270;
										break;
									case StaticValues.ROTATE_PATH_FLAG_RIGHT:
										Rrotation = -90;
										break;
									case StaticValues.ROTATE_PATH_FLAG_TOP:
										Rrotation = -180;
										break;
									default:
										break;
								}
								break;
							default:
								break;
						}
					}
					ImagesRotate(store_ImageView,StaticValues.Image_CUR_ROTATION,Rrotation);
					ImagesRotate(switch_cameraBtn,StaticValues.Image_CUR_ROTATION,Rrotation);
					ImagesRotate(flash_btn,StaticValues.Image_CUR_ROTATION,Rrotation);
					ImagesRotate(effectsBtn,StaticValues.Image_CUR_ROTATION,Rrotation);
					//不是录像状态下，可以旋转，否者不旋转（因为中心点药出错待解决！！！！！！！）
					if(!ISVIDEO) {
						ImagesRotate(GoToVideobtn,StaticValues.Image_CUR_ROTATION,Rrotation);
						ImagesRotate(take_image_btn,StaticValues.Image_CUR_ROTATION,Rrotation);
					}
					ImagesRotate(HDR_btn,StaticValues.Image_CUR_ROTATION,Rrotation);
					StaticValues.Image_CUR_ROTATION = Rrotation;
					//旋转完成，初始化路径
					StaticValues.ROTATE_THROW_PATH = "";
					StaticValues.ROTATE_DIR = "";
					}
				}
			public void checkPath(String s)
			{
				if(StaticValues.ROTATE_DIR=="")
				{
					if(StaticValues.ROTATE_THROW_PATH!="")
					{
						switch(StaticValues.ROTATE_THROW_PATH)
						{
							case StaticValues.ROTATE_PATH_FLAG_TOP:
								switch(s)
								{
									case StaticValues.ROTATE_PATH_FLAG_LEFT:
										StaticValues.ROTATE_DIR = "ANTICLOCK";//逆时针
										break;
									case StaticValues.ROTATE_PATH_FLAG_RIGHT:
										StaticValues.ROTATE_DIR = "CLOCK";//顺时针
										break;
									default:
										break;
								}
								break;
							case StaticValues.ROTATE_PATH_FLAG_LEFT:
								switch(s)
								{
									case StaticValues.ROTATE_PATH_FLAG_TOP:
										StaticValues.ROTATE_DIR = "CLOCK";
										break;
									case StaticValues.ROTATE_PATH_FLAG_BOTTOM:
										StaticValues.ROTATE_DIR = "ANTICLOCK";
										break;
									default:
										break;
								}
								break;
							case StaticValues.ROTATE_PATH_FLAG_BOTTOM:
								switch(s)
								{
									case StaticValues.ROTATE_PATH_FLAG_LEFT:
										StaticValues.ROTATE_DIR = "CLOCK";
										break;
									case StaticValues.ROTATE_PATH_FLAG_RIGHT:
										StaticValues.ROTATE_DIR = "ANTICLOCK";
										break;
									default:
										break;
								}
								break;
							case StaticValues.ROTATE_PATH_FLAG_RIGHT:
								switch(s)
								{
									case StaticValues.ROTATE_PATH_FLAG_BOTTOM:
										StaticValues.ROTATE_DIR = "CLOCK";//逆时针
										break;
									case StaticValues.ROTATE_PATH_FLAG_TOP:
										StaticValues.ROTATE_DIR = "ANTICLOCK";//顺时针
										break;
									default:
										break;
								}
								break;
							default:
								break;
						}
						StaticValues.ROTATE_THROW_PATH+=s;
					}
					else
					{
						StaticValues.ROTATE_THROW_PATH+=s+"||";
					}
				}
			}
		}.enable();
		PREMISSION_REQUEST = new PremissionGETandCHECK(mAct);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.my_cmera_layout,container,false);
		mView = v;
		store_ImageView = (RoundImageView)v.findViewById(R.id.Store_Image_Preview);
		store_ImageView.setOnClickListener(store_Image);
		/**
		 * 设置特效实时预览
		 */
//		StaticValues.EffectsSurfaceViews  = new List<SurfaceView>;
//		StaticValues.EffectsSurfaceViews.add((SurfaceView)v.findViewById(R.id.EFFECTS_common));
//		StaticValues.EffectsSurfaceViews.add((SurfaceView)v.findViewById(R.id.EFFECTS_LOMO));
		//设置预览图
		setPreviewImage(null);
		RelativeLayout rl = (RelativeLayout)ImagesActivity.mView.findViewById(R.id.main_content);
		childCount = rl.getChildCount();
		mSurfaceView = (SurfaceView)v.findViewById(R.id.PreviewImage);
		mSurfaceView.setOnTouchListener(AutoFocous);
		StaticValues.mSurfaceView = mSurfaceView;
		GoToVideobtn = (RoundImageView)v.findViewById(R.id.GoToVideo);
		GoToVideobtn.setOnClickListener(GoToVideo);
		take_image_btn = (RoundImageView)v.findViewById(R.id.take_image_btn);
		take_image_btn.setOnClickListener(take_Image);
		ring_shape_L = (ImageView)v.findViewById(R.id.ring_shape_L);
		ring_shape_R = (ImageView)v.findViewById(R.id.ring_shape_R);
		My_timer_Lin = (LinearLayout)v.findViewById(R.id.My_timer_Lin);
		My_timer = (TextView)v.findViewById(R.id.My_timer);
		horzationtal_Line = (ProgressBar)v.findViewById(R.id.horzationtal_Line);
		bottom_tool_BG = (LinearLayout)v.findViewById(R.id.bottom_tool_BG);
		take_image_Ring = (ImageView)v.findViewById(R.id.take_image_Ring);
		effectsBtn = (ImageView)v.findViewById(R.id.Image_Effects);
		effectsBtn.setOnClickListener(Effectlis);
		modelBtn = (TextView)v.findViewById(R.id.camera_model);
		modelBtn.setOnClickListener(ModelLis);
		switch_cameraBtn = (ImageView)v.findViewById(R.id.switch_camera);
		switch_cameraBtn.setOnClickListener(SwitchCameraLis);
		flash_btn = (ImageView)v.findViewById(R.id.flash_btn);
		flash_btn.setOnClickListener(FlashLis);
		flash_off = (TextView)v.findViewById(R.id.flash_off);
		flash_off.setOnClickListener(FlashoffBtnLis);
		flash_off.setBackgroundColor(Color.TRANSPARENT);
		flash_auto = (TextView)v.findViewById(R.id.flash_auto);
		flash_auto.setOnClickListener(FlashautoBtnLis);
		flash_on = (TextView)v.findViewById(R.id.flash_on);
		flash_on.setOnClickListener(FlashonBtnLis);
		HDR_btn = (ImageView)v.findViewById(R.id.HDR_btn);
		HDR_btn.setOnClickListener(HDRLis);
		mAniclick = AnimationUtils.loadAnimation(mAct,R.anim.my_btn_click);
		mAnitoCenter = AnimationUtils.loadAnimation(mAct,R.anim.my_change_to_center);
		mAnitoRight = AnimationUtils.loadAnimation(mAct,R.anim.my_change_to_right);
		if(PREMISSION_REQUEST.checkPremissionAndRequest(new String[]{PremissionEnum.CAMERA,PremissionEnum.WRITE_EXTERNAL_STORAGE,PremissionEnum.READ_EXTERNAL_STORAGE},new int[]{PremissionREQUSETCODE.CAMERA,PremissionREQUSETCODE.WRITE_EXTERNAL_STORAGE,PremissionREQUSETCODE.READ_EXTERNAL_STORAGE}))
		{
			StaticValues.ImagesActivityCamera = new CameraPreview(mAct.getApplicationContext(),mSurfaceView);
		};
		//cameraPreview = new CameraPreview(mAct.getApplicationContext(),mSurfaceView);
		return v;
	}

	/**
	 * 自动对焦的事件监听
	 */
	View.OnTouchListener AutoFocous = new View.OnTouchListener(){
		@Override
		public boolean onTouch(View view,MotionEvent motionEvent){
			//Log.i("对焦按下",motionEvent.getX()+"  "+motionEvent.getY()+"");
			// 点击屏幕后 半径设为0,alpha设置为255
			final ImageView iw = new ImageView(StaticValues.MainActivity);
			final ImageView iw1 = new ImageView(StaticValues.MainActivity);
			final RelativeLayout mrl = (RelativeLayout)ImagesActivity.mView.findViewById(R.id.main_content);
			//Log.i("孩子数量",mrl.getChildCount()+"");
			iw.setImageDrawable(view.getResources().getDrawable(R.drawable.my_ring_shape));
			iw.setTranslationX(motionEvent.getX()-100);
			iw.setTranslationY(motionEvent.getY()-100);
			ImageView v = (ImageView)ImagesActivity.mView.findViewById(R.id.focousImage);
			iw1.setImageDrawable(v.getDrawable());
			iw1.setTranslationX(motionEvent.getX()-70);
			iw1.setTranslationY(motionEvent.getY()-70);
			RelativeLayout.LayoutParams rll = new RelativeLayout.LayoutParams(200,200);
			RelativeLayout.LayoutParams rll2 = new RelativeLayout.LayoutParams(140,140);
			//孩子数量少于=7才增加控件否则不增加
			if(mrl.getChildCount() <= childCount) {
				mrl.addView(iw,rll);
				mrl.addView(iw1,rll2);
				ProvertyAnimation.ScaleSmaleToLarge(iw,1.6f,1f);
				ProvertyAnimation.ScaleSmaleToLarge(iw1,0.1f,1f);
				//cameraPreview.pointFocus((int)motionEvent.getX(),(int)motionEvent.getY());
				StaticValues.ImagesActivityCamera.pointFocus((int)motionEvent.getX(),(int)motionEvent.getY());
				Handler hander = new Handler();
				hander.postDelayed(new Runnable(){
					@Override
					public void run(){
						mrl.removeView(iw);
						mrl.removeView(iw1);
					}
				},1000);
			}
			return true;
		}
	};

	/**
	 * 闪光灯事件监听
	 */
	View.OnClickListener FlashLis = new View.OnClickListener()
	{
		@Override
		public void onClick(View view){
			//cameraPreview.switchFlash(flash_btn);
			//弹出动画（此按钮后面的选择）
			ProvertyAnimation.FlashOutAni(flash_off,flash_off_POS);
			ProvertyAnimation.FlashOutAni(flash_auto,flash_auto_POS);
			ProvertyAnimation.FlashOutAni(flash_on,flash_on_POS);
		}
	};
	/**
	 * 闪光灯后面的按钮的事件
	 */
	View.OnClickListener FlashoffBtnLis = new View.OnClickListener(){
		@Override
		public void onClick(View view){
			//改变背景动画到这里
			flash_on.setBackgroundColor(Color.TRANSPARENT);
			flash_auto.setBackgroundColor(Color.TRANSPARENT);
			flash_off.setBackgroundColor(StaticValues.FLASH_BGCOLOR);
			//收回动画
			ProvertyAnimation.FlashOutAni(flash_off,flash_POS);
			ProvertyAnimation.FlashOutAni(flash_on,flash_POS);
			ProvertyAnimation.FlashOutAni(flash_auto,flash_POS);
			//cameraPreview.switchFlash(flash_btn,0);
			StaticValues.ImagesActivityCamera.switchFlash(flash_btn,0);
		}
	};
	View.OnClickListener FlashautoBtnLis = new View.OnClickListener(){
		@Override
		public void onClick(View view){
			//改变背景动画到这里
			flash_off.setBackgroundColor(Color.TRANSPARENT);
			flash_on.setBackgroundColor(Color.TRANSPARENT);
			flash_auto.setBackgroundColor(StaticValues.FLASH_BGCOLOR);
			//收回动画
			ProvertyAnimation.FlashOutAni(flash_auto,flash_POS);
			ProvertyAnimation.FlashOutAni(flash_off,flash_POS);
			ProvertyAnimation.FlashOutAni(flash_on,flash_POS);
			//cameraPreview.switchFlash(flash_btn,2);
			StaticValues.ImagesActivityCamera.switchFlash(flash_btn,2);
		}
	};
	View.OnClickListener FlashonBtnLis = new View.OnClickListener(){
		@Override
		public void onClick(View view){
			//改变背景动画到这里
			flash_off.setBackgroundColor(Color.TRANSPARENT);
			flash_auto.setBackgroundColor(Color.TRANSPARENT);
			flash_on.setBackgroundColor(StaticValues.FLASH_BGCOLOR);
			//收回动画
			ProvertyAnimation.FlashOutAni(flash_on,flash_POS);
			ProvertyAnimation.FlashOutAni(flash_off,flash_POS);
			ProvertyAnimation.FlashOutAni(flash_auto,flash_POS);
			//cameraPreview.switchFlash(flash_btn,1);
			StaticValues.ImagesActivityCamera.switchFlash(flash_btn,1);
		}
	};
	/**
	 * HDR事件监听
	 */
	View.OnClickListener HDRLis = new View.OnClickListener(){
		@Override
		public void onClick(View view){

		}
	};
	/**
	 * 特效监听
	 */
     View.OnClickListener Effectlis = new View.OnClickListener()
    {

		@Override
		public void onClick(View view){

		}
	};
	/**
	 * 模式监听
	 */
	View.OnClickListener ModelLis = new View.OnClickListener()
	{

		@Override
		public void onClick(View view){

		}
	};
	/**
	 * 切换镜头监听
	 */
	View.OnClickListener SwitchCameraLis = new View.OnClickListener()
	{
		@Override
		public void onClick(View view){
			//cameraPreview.switchCamrea();
			StaticValues.ImagesActivityCamera.switchCamrea();
		}
	};
	/**
	 * 图库的事件监听
	 */
	View.OnClickListener store_Image = new View.OnClickListener(){
		@Override
		public void onClick(View view){
			//设置按下动画
			//store_ImageView.startAnimation(mAni);
			//跳转到图库界面
		}
	};
	/**
	 * 拍照（快门）的事件监听
	 */
	View.OnClickListener take_Image = new View.OnClickListener(){
		@Override
		public void onClick(View view){
			take_image_btn.startAnimation(mAniclick);
			//cameraPreview.takePicture(store_ImageView);
			StaticValues.ImagesActivityCamera.takePicture(store_ImageView);
		}
	};
	/**
	 * 去到录像事件
	 */
	View.OnClickListener GoToVideo = new View.OnClickListener(){
		@Override
		public void onClick(View view){
			//ExchangeBtn(take_image_btn,GoToVideobtn);
			ProvertyAnimation.ExchangeBtn(take_image_btn,GoToVideobtn);
			if(take_image_btn.getTranslationX() == GoToVideobtn.getTranslationX())//注意如果有延时的话，这里的判断条件要改为不等于，因为已经为控件赋值了新的位置值
			{
				//准备录像状态
				//cameraPreview.PreperRecordVideo(mSurfaceView);
				StaticValues.ImagesActivityCamera.PreperRecordVideo(StaticValues.mSurfaceView);
				ISVIDEO = true;
				take_image_btn.setOnClickListener(GoToVideo);
				take_image_btn.setImageDrawable(ImagesActivity.mView.getResources().getDrawable(R.mipmap.btn_ring_camera_normal));
				GoToVideobtn.setOnClickListener(videoClick);//设置监听为录像监听
				GoToVideobtn.setImageDrawable(ImagesActivity.mView.getResources().getDrawable(R.drawable.btn_shutter_video_normal));
				//播放动画，把不用控件设置隐藏
				ProvertyAnimation.AlphaTo0(effectsBtn);
				ProvertyAnimation.AlphaTo0(HDR_btn);
				return;
			} else {
				//准备拍照状态
				//cameraPreview.PrepertakeImage(mSurfaceView);
				StaticValues.ImagesActivityCamera.PrepertakeImage(StaticValues.mSurfaceView);
				ISVIDEO = false;
				take_image_btn.setOnClickListener(take_Image);//设置监听为录像监听
				take_image_btn.setImageDrawable(ImagesActivity.mView.getResources().getDrawable(R.mipmap.btn_shutter_photo_pressed));
				GoToVideobtn.setOnClickListener(GoToVideo);
				GoToVideobtn.setImageDrawable(ImagesActivity.mView.getResources().getDrawable(R.mipmap.btn_ring_video_normal));
				//播放动画，把控件设置显示
				ProvertyAnimation.AlphaToMax(effectsBtn);
				ProvertyAnimation.AlphaToMax(HDR_btn);
				take_image_Ring.setImageDrawable(ImagesActivity.mView.getResources().getDrawable(R.drawable.my_ring_shape));
				return;
			}
		}
	};
	/**
	 * 录像按钮按下
	 */
	View.OnClickListener videoClick = new View.OnClickListener(){
		@Override
		public void onClick(View view){
			if(!StaticValues.Videoing) {
				//更换图片,现在在中间的控件是原来去到录像界面的控件
				StaticValues.Videoing = true;
				GoToVideobtn.setImageDrawable(ImagesActivity.mView.getResources().getDrawable(R.mipmap.btn_shutter_video_pressed));
				take_image_Ring.setImageDrawable(ImagesActivity.mView.getResources().getDrawable(R.drawable.my_ring_red));
				//隐藏其他控件
				ProvertyAnimation.AlphaTo0(switch_cameraBtn);
				ProvertyAnimation.AlphaTo0(take_image_btn);
				ProvertyAnimation.AlphaTo0(store_ImageView);
				ProvertyAnimation.AlphaTo0(store_ImageView);
				ProvertyAnimation.AlphaTo0(flash_btn);
				ProvertyAnimation.AlphaTo0(modelBtn);
				ProvertyAnimation.AlphaTo0(ring_shape_L);
				ProvertyAnimation.AlphaTo0(ring_shape_R);
				ProvertyAnimation.AlphaTo0(horzationtal_Line);
				//背景不能设置成不可见，否者布局会乱掉，只能设置控件的alpha为 0来实现隐藏
				bottom_tool_BG.setAlpha(0);
				ProvertyAnimation.AlphaToMax(My_timer_Lin);
				//执行录像逻辑
				//cameraPreview.stratVideo();
				StaticValues.ImagesActivityCamera.stratVideo();
				//打开计时器
				handler = new Handler();
				handler.postDelayed(run,1000);
			}
			else
			{
				//在录像，故现在按下就是暂停录像或者停止录像
				//cameraPreview.stopVideo();
				StaticValues.ImagesActivityCamera.stopVideo();
				GoToVideobtn.setImageDrawable(ImagesActivity.mView.getResources().getDrawable(R.mipmap.btn_shutter_video_normal));
				take_image_Ring.setImageDrawable(ImagesActivity.mView.getResources().getDrawable(R.drawable.my_ring_shape));
				//显示其他控件,
				bottom_tool_BG.setAlpha(0.2f);
				ProvertyAnimation.AlphaToMax(switch_cameraBtn);
				ProvertyAnimation.AlphaToMax(take_image_btn);
				ProvertyAnimation.AlphaToMax(store_ImageView);
				ProvertyAnimation.AlphaToMax(store_ImageView);
				ProvertyAnimation.AlphaToMax(flash_btn);
				ProvertyAnimation.AlphaToMax(modelBtn);
				ProvertyAnimation.AlphaToMax(ring_shape_L);
				ProvertyAnimation.AlphaToMax(ring_shape_R);
				ProvertyAnimation.AlphaToMax(horzationtal_Line);
				ProvertyAnimation.AlphaTo0(My_timer_Lin);
				//终止计时器
				handler.removeCallbacks(run);
				timer = 0;
				StaticValues.Videoing = false;
			}
		}
	};
	/**
	 * 另外开一个线程来计时
	 */
	Runnable run = new Runnable(){
		@Override
		public void run(){
			timer++;
			if(timer>9)
			{
				timer = 0;
				timer1++;
				if(timer1>6)
				{
					h2++;
					if(h2>9)
					{
						h1++;
						if(h1>6)
						{
							h1 = 0;
							h2 = 0;
							timer1 = 0;
							timer = 0;
						}
					}
				}
			}
			My_timer.setText(h1+""+h2+":"+timer1+timer);
			handler.postDelayed(run,1000);
		}
	};
	/**
	 * 获取预览的小图片
	 * @return
	 */
	public Bitmap getPreviewImage()
	{
		try {
		File f = new File(Environment.getExternalStorageDirectory() + "/馨拍/Images/tempImage/","tempPreview.jpg");
		if(f.exists())
		{
			//文件存在,读入输出流
			FileInputStream fos = new FileInputStream(f);
			return BitmapFactory.decodeStream(fos);
		}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean setPreviewImage(Bitmap bm)
	{
		if(bm==null) {
			bm = getPreviewImage();
		}
		if(bm!=null)
		{
			store_ImageView.setImageBitmap(bm);
			return true;
		}
		return false;
	}
	public void ImagesRotate(ImageView o,int fromdegree,int todegree)
	{
		//0.5f代表一半
		RotateAnimation r = new RotateAnimation(fromdegree,todegree,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		r.setInterpolator(new AccelerateDecelerateInterpolator());
		r.setDuration(200);
		r.setFillAfter(true);
		o.startAnimation(r);
	}

	/**
	 * 模式选择面板虚化背景
	 */
	public void model_ChoseBlur()
	{
		RelativeLayout model_Chose = (RelativeLayout)mView.findViewById(R.id.model_Chose);
	}
}
