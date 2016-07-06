package Help;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.*;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.*;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import org.xml.sax.helpers.LocatorImpl;

import Enum.*;
import SelfWidget.RoundImageView;
import us.pinguo.edit.sdk.PGEditActivity;
import us.pinguo.edit.sdk.base.PGEditSDK;
import winter.zxb.smilesb101.winterCamer.ImagesActivity;
import winter.zxb.smilesb101.winterCamer.MainActivity;
import winter.zxb.smilesb101.winterCamer.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 预览面板的类
 * Created by Administrator on 2016/6/29.
 */
public class CameraPreview extends ViewGroup implements SurfaceHolder.Callback{

	private SurfaceView mSurfaceView;
	private Camera mCamera;
	private SurfaceHolder mSurfaceHolder;
	private Size mPreviewSize;
	/**
	 * 建立预览回调对象,用于接收相机采集到的数据
	 */
	private previewCallBack pre = new previewCallBack();//建立预览回调对象,用于接收相机采集到的数据
	private List<Size> mSupportPreviewSize;
	private MediaRecorder mediaRecorder;

	/**
	 * 准备录像
	 * @param SurfaceView
	 */
	public boolean PreperRecordVideo(SurfaceView SurfaceView)
	{
		if(new PremissionGETandCHECK(StaticValues.MainActivity).checkPremissionAndRequest(new String[]{PremissionEnum.RECORD_AUDIO},new int[]{PremissionREQUSETCODE.RECORD_AUDIO})) {
			try {
				stopCamera();
				mCamera = Camera.open(StaticValues.CAMERA_NUM);
				if(mCamera != null) {
					Size mSize = null;
					switch(StaticValues.CAMERA_NUM) {
						case 0:
							if(PhoneSupport.BACKCAMERAPhoneSupportVideoSize == null)
								PhoneSupport.BACKCAMERAPhoneSupportVideoSize = mCamera.getParameters().getSupportedVideoSizes();
							for(Size t : PhoneSupport.BACKCAMERAPhoneSupportVideoSize) {
								Log.i("t","width: " + t.width + " height: " + t.height);
							}
							mSize = PhoneSupport.BACKCAMERAPhoneSupportVideoSize.get(0);
							break;
						case 1:
							if(PhoneSupport.FORWORDCAMERAPhoneSupportPicSize == null)
								PhoneSupport.FORWORDCAMERAPhoneSupportPicSize = mCamera.getParameters().getSupportedVideoSizes();
							for(Size t : PhoneSupport.FORWORDCAMERAPhoneSupportPicSize) {
								Log.i("t","width: " + t.width + " height: " + t.height);
							}
							mSize = PhoneSupport.FORWORDCAMERAPhoneSupportPicSize.get(0);
							break;
					}
					mSurfaceView = SurfaceView;
					mSurfaceHolder = mSurfaceView.getHolder();
					mCamera.setPreviewDisplay(mSurfaceHolder);
					mCamera.setDisplayOrientation(90);
					mSurfaceHolder.addCallback(this);
//					pre = new previewCallBack();
//					mCamera.setPreviewCallback(pre); //设置预览回调对象
					mCamera.startPreview();
					mediaRecorder = new MediaRecorder();// 创建mediarecorder对象

					//mCamera.unlock();//不要设置，否者容易锁死摄像机导致无法使用
// 设置录制视频源为Camera(相机)
					mediaRecorder.setCamera(mCamera);
					//setAudioSource()- 设置音频source，使用MediaRecorder.AudioSource.CAMCORDER．
					mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
					mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

					mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_2160P));
					mediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
// 设置视频文件输出的路径
					/*获取文字*/
					String timeStamp = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date());
					mediaRecorder.setOutputFile(StaticValues.VideoFilePath+timeStamp+".mp4");
					mediaRecorder.prepare();
				} else {
					//没有这个摄像头
					return false;
				}
			} catch(IOException e) {
				Log.i("录像中错误",e.getMessage());
			}
		}
		return false;
	}

	/**
	 * 开始录像
	 */
	public void stratVideo()
	{
		try {
			mCamera.lock();
			mCamera.unlock();
			//没有上面两句会报错start fail
			mediaRecorder.start();
			//改变录像状态
			StaticValues.Videoing = true;
		}catch(Exception e)
		{
			Log.i("开始录像错误",e.getMessage());
			stopVideo();
		}
	}
	/**
	 * 停止录像
	 */
	public void stopVideo()
	{
		try {
			mediaRecorder.stop();
			mediaRecorder.reset();//不设置会报错mediarecorder went away with unhandled events
			mediaRecorder.release();
			mCamera.lock();
			//改变录像状态
			StaticValues.Videoing = false;
			//重新准备录像
			PreperRecordVideo(mSurfaceView);
		}catch(Exception e)
		{
			Log.i("停止相机错误",e.getMessage());
			mediaRecorder.release();
			mCamera.lock();
		}

	}

	/**
	 * 准备拍照
	 */
	public void PrepertakeImage(SurfaceView sur)
	{
		mediaRecorder.release();
		mCamera.lock();
		mSurfaceView = sur;
		mSurfaceHolder = sur.getHolder();
		mediaRecorder = null;
		initCamera();
	}

	/**
	 * AutoFocusCallback自动对焦的回调对象
	 */
	private AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback()
	{
		@Override
		public void onAutoFocus(boolean success, Camera camera) {

			if(success){
				initCamera();//实现相机的参数初始化
				camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
			}
		}
	};// AutoFocusCallback自动对焦的回调对象
	private Context mContext;
	private String strCaptureFilePath = Environment
			.getExternalStorageDirectory() + "/馨拍/Images/";// 保存图像的路径
	private String tempPreviewFilePath = Environment
			.getExternalStorageDirectory() + "/馨拍/Images/tempImage/";// 保存图像的路径
	private RoundImageView store_Image;

	public CameraPreview(Context context,SurfaceView SurfaceView){
		super(context);
		mContext = context;
		this.mSurfaceView = SurfaceView;
		//addView(mSurfaceView);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		createSDCardDir("/馨拍/Images/");
		createSDCardDir("/馨拍/Images/tempImage/");
		createSDCardDir("/馨拍/Videos/");
		Log.i("相机实例化","onRequestPermissionsResult: ");
		setmCamera(Camera.open(StaticValues.CAMERA_NUM));
	}

	public void setmCamera(Camera camera)
	{
		stopCamera();
		mCamera = camera;
		if(mCamera != null)
		{
			Log.i("sa","setmCamera: ");
			mSupportPreviewSize = mCamera.getParameters().getSupportedPreviewSizes();
			/**
			 * 重新调整层级
			 */
		    requestLayout();
		}
		else
		{
			mCamera = Camera.open(0);
		}
		initCamera();
	}
	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder){
		try {
			initCamera();
			Log.i("实例化","createCamera");
		}catch(Exception e)
		{
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder,int format,int width,int height){
		/**
		 * 初始化相机
		 */
		initCamera();
		//实现自动对焦
		mCamera.autoFocus(mAutoFocusCallback);
}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder){
		stopCamera();
		mCamera.release();
		mCamera = null;
	}

	@Override
	protected void onLayout(boolean b,int i,int i1,int i2,int i3){
	}

	/* 相机初始化的method */
	private void initCamera() {
		if (mCamera == null) {
			mCamera = Camera.open(StaticValues.CAMERA_NUM);
		}
		else
		{
			stopCamera();
		}
		try {
			Parameters parameters = mCamera.getParameters();
			// parameters.setPictureFormat(PixelFormat.JPEG);
			switch(StaticValues.CAMERA_NUM) {
				case 0://后置
				if(PhoneSupport.BACKCAMERAPhoneSupportPicSize == null) {
					PhoneSupport.BACKCAMERAPhoneSupportPicSize = parameters.getSupportedPictureSizes();
					for(Size t : PhoneSupport.BACKCAMERAPhoneSupportPicSize) {
						Log.i("后置摄像头支持的分辨率",
								t.width + "  " + t.height);
					}
				}
					parameters.setPictureSize(PhoneSupport.BACKCAMERAPhoneSupportPicSize.get(3).width,PhoneSupport.BACKCAMERAPhoneSupportPicSize.get(3).height);
					break;
				case 1://前置摄像头
					if(PhoneSupport.FORWORDCAMERAPhoneSupportPicSize == null) {
						PhoneSupport.FORWORDCAMERAPhoneSupportPicSize = parameters.getSupportedPictureSizes();
						for(Size t : PhoneSupport.FORWORDCAMERAPhoneSupportPicSize) {
							Log.i("前置摄像头支持的分辨率",
									t.width + "  " + t.height);
						}
					}
					parameters.setPictureSize(PhoneSupport.FORWORDCAMERAPhoneSupportPicSize.get(0).width,PhoneSupport.FORWORDCAMERAPhoneSupportPicSize.get(0).height);
					break;
			}
			mCamera.setFaceDetectionListener(FaceDectection.mFaceDetectionLis);//设置脸型检测控制器
			mCamera.setPreviewDisplay(mSurfaceHolder);//设置显示面板控制器，
			parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);//开自动闪光灯
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
			//setDispaly(parameters,camera);
			mCamera.setParameters(parameters);
			/**
			 * 设置返回的图像旋转90度，为了正常显示
			 */
			mCamera.setDisplayOrientation(90);
			mCamera.setParameters(parameters);
			//mCamera.getParameters().setPreviewFormat(ImageFormat.JPEG);
//			pre = new previewCallBack();
//			mCamera.setPreviewCallback(pre);
			mCamera.startPreview();//开始预览，这步操作很重要
			mCamera.startFaceDetection();//开启脸型检测
			mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
		} catch(IOException e) {
			Log.i("初始化相机错误",e.getMessage());
			e.printStackTrace();
		}
	}

	/* 停止相机的method */
	private void stopCamera() {
		if (mCamera != null) {
			try {
                /* 停止预览 */
				mCamera.stopPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	// 检测摄像头是否存在的私有方法
	public boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// 摄像头存在
			return true;
		} else {
			// 摄像头不存在
			return false;
		}
	}

	/* 拍照的method */
	public void takePicture(RoundImageView store_image) {
		store_Image = store_image;
		if (mCamera != null) {
			mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
		}
	}

	private ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
            /* 按下快门瞬间会调用这里的程序 */
			Log.i("快门","onShutter: ");
		}
	};

	private PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] _data, Camera _camera) {
            /* 要处理raw data?写?否 */
		}
	};

	//在takepicture中调用的回调方法之一，接收jpeg格式的图像
	private PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] _data, Camera _camera) {
			/* 取得相片 */
			Bitmap bm = BitmapFactory.decodeByteArray(_data, 0,
					_data.length);

			if(StaticValues.CAMERA_NUM==0) {
				//Log.i("后置摄像头","onPictureTaken: ");
				/*旋转图片*/
				bm = rotateToDegrees(bm,90);
			}
			else
			{
				//Log.i("前置摄像头","onPictureTaken: ");
				bm = rotateToDegrees(bm,-90);
			}
			/*获取文字*/
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date());
						/*添加文字*/
			bm = drawTextToBitmap(StaticValues.MainActivity.getApplicationContext(),bm,timeStamp);

			new AsyncTask<Bitmap,Void,Bitmap>()
			{
				@Override
				protected Bitmap doInBackground(Bitmap... bitmaps){
					try {
                /* 创建文件 */
						String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
						String imageFileName = "IMG_"+ timeStamp + ".jpg";
						StaticValues.ImagePath =strCaptureFilePath+imageFileName;
						StaticValues.EfImagePath =strCaptureFilePath+ "IMG_"+ timeStamp+"_EF.jpg";
						File myCaptureFile = new File(strCaptureFilePath, StaticValues.ImagePath);
						BufferedOutputStream bos = new BufferedOutputStream(
								new FileOutputStream(myCaptureFile));
                /* 采用压缩转档方法 */
						bitmaps[0].compress(Bitmap.CompressFormat.JPEG,100, bos);
				/*写入临时预览文件*/
						File previewImageFile = new File(tempPreviewFilePath,"tempPreview.jpg");
						if(previewImageFile.exists())
						{
							previewImageFile.createNewFile();
						}
						BufferedOutputStream pos = new BufferedOutputStream( new FileOutputStream(previewImageFile));
						bitmaps[0].compress(Bitmap.CompressFormat.JPEG,10,pos);
						pos.flush();
						pos.close();
                /* 调用flush()方法，更新BufferStream */
						bos.flush();
                /* 结束OutputStream */
						bos.close();
                /* 让相片显示3秒后圳重设相机 */
						// Thread.sleep(2000);
						return BitmapFactory.decodeFile(tempPreviewFilePath+"tempPreview.jpg");
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(Bitmap bm){
					 /*设置控件预览图片*/
					store_Image.setImageBitmap(bm);
					//Log.i("调用之前","onPostExecute: ");
					//调用Camera360SDk进行美化操作,回调方法在下面指定的MainActivity中
					PGEditSDK.instance().startEdit(StaticValues.MainActivity, PGEditActivity.class, StaticValues.ImagePath, StaticValues.EfImagePath);
					 /* 重新设定Camera */
					stopCamera();
					initCamera();
				}
			}.execute(bm);


		}
	};

	/* 自定义class AutoFocusCallback */
	public class AutoFocusCallback implements
			android.hardware.Camera.AutoFocusCallback {
		public void onAutoFocus(boolean focused, Camera camera) {
            /* 对到焦点拍照 */
			if (focused) {
				takePicture(null);
				camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
			}
		}
	};

	/**
	 * 可以用来制作多界面同步预览
	 * 每次cam采集到新图像时调用的回调方法，前提是必须开启预览
	 */
	class previewCallBack implements Camera.PreviewCallback {

		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			// Log.w("wwwwwwwww", data[5] + "");
			 Log.w("支持格式", mCamera.getParameters().getPreviewFormat()+"");
			//decodeToBitMap(data, camera);
//这里添加多view显示实时预览
		}
	};

	/**
	 * 解码到Bitmap（位图）,此操作太过于费时，需要加入异步执行
	 * @param data
	 * @param _camera
	 * @return 位图或者null
	 */
	public Bitmap decodeToBitMap(byte[] data, Camera _camera) {
		Size size = mCamera.getParameters().getPreviewSize();
		try {
			YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width,
					size.height, null);
			//Log.w("wwwwwwwww", size.width + " " + size.height);
			if (image != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				image.compressToJpeg(new Rect(0, 0, size.width, size.height),
						80, stream);
				Bitmap bmp = BitmapFactory.decodeByteArray(
						stream.toByteArray(), 0, stream.size());
				//Log.w("wwwwwwwww", bmp.getWidth() + " " + bmp.getHeight());
//				Log.w("wwwwwwwww",
//						(bmp.getPixel(100, 100) & 0xff) + "  "
//								+ ((bmp.getPixel(100, 100) >> 8) & 0xff) + "  "
//								+ ((bmp.getPixel(100, 100) >> 16) & 0xff));

				stream.close();
				return bmp;
			}
		} catch (Exception ex) {
			Log.e("Sys", "Error:" + ex.getMessage());
		}
		return null;
	}

	public void createSDCardDir(String Mpath){
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir = Environment.getExternalStorageDirectory();
			//得到一个路径，内容是sdcard的文件夹路径和名字
			String path = sdcardDir.getPath() + Mpath;
			File path1 = new File(path);
			if(! path1.exists()) {
				//若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();
			}
		} else {
			return;

		}
	}

	/**
	 * 创建图片
	 * @return
	 * @throws IOException
	 */
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "IMG_"+ timeStamp + "_";
		File albumF = new File(strCaptureFilePath);
		File imageF = File.createTempFile(imageFileName, "jpg", albumF);
		return imageF;
	}

	/**
	 * 获取文件
	 * @return
	 * @throws IOException
	 */
	private File setUpPhotoFile() throws IOException {

		File f = createImageFile();
		strCaptureFilePath = f.getAbsolutePath();
		return f;
	}
	/**
	 * 图片旋转
	 * @param tmpBitmap
	 * @param degrees
	 * @return
	 */
	public static Bitmap rotateToDegrees(Bitmap tmpBitmap, float degrees) {
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.setRotate(degrees);
		tmpBitmap =
				Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight(), matrix,true);
		return tmpBitmap;
	}
	/**
	 * 图片缩小
	 */
	public Bitmap scaleToSmale(Bitmap tmpBitmap) {
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.setRotate(0);
		tmpBitmap =
				Bitmap.createBitmap(tmpBitmap, 0, 0, 176, 176, matrix,true);
		return tmpBitmap;
	}
	/**
	 * 读取照片exif信息中的旋转角度
	 * @param path 照片路径
	 * @return角度
	 */
	public int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation =
					exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 交换摄像头
	 * @return
	 */
	public boolean switchCamrea()
	{
		try {
			if(mCamera != null) {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}
			StaticValues.CAMERA_NUM = StaticValues.CAMERA_NUM == 0 ? 1 : 0;
			mCamera = Camera.open(StaticValues.CAMERA_NUM);
			initCamera();
			return true;
		}
		catch(Exception e)
		{
			Log.w("切换镜头错误",e.getMessage());
		}
		return false;
	}

	/**
	 * 定点对焦的代码
	 * @param x
	 * @param y
	 */
	public void pointFocus(int x,int y) {

		Rect focusRect = calculateTapArea(x, y, 1f);
		Rect meteringRect = calculateTapArea(x, y, 1.5f);

		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

		if (parameters.getMaxNumFocusAreas() > 0) {
			List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
			focusAreas.add(new Camera.Area(focusRect, 1000));

			parameters.setFocusAreas(focusAreas);
		}

		if (parameters.getMaxNumMeteringAreas() > 0) {
			List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
			meteringAreas.add(new Camera.Area(meteringRect, 1000));

			parameters.setMeteringAreas(meteringAreas);
		}
		mCamera.setParameters(parameters);
		mCamera.autoFocus(mAutoFocusCallback);
	}
	/**
	 * Convert touch position x:y to {@link Camera.Area} position -1000:-1000 to 1000:1000.
	 */
	private Rect calculateTapArea(float x, float y, float coefficient) {
		float focusAreaSize = 300;
		int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();

		int centerX = (int) (x / StaticValues.SCREEN_WIDTH * 2000 - 1000);
		int centerY = (int) (y / StaticValues.SCREEN_HEIGTH * 2000 - 1000);

		int left = clamp(centerX - areaSize / 2, -1000, 1000);
		int right = clamp(left + areaSize, -1000, 1000);
		int top = clamp(centerY - areaSize / 2, -1000, 1000);
		int bottom = clamp(top + areaSize, -1000, 1000);

		return new Rect(left, top, right, bottom);
	}
	private int clamp(int x, int min, int max) {
		if (x > max) {
			return max;
		}
		if (x < min) {
			return min;
		}
		return x;
	}

	/**
	 * 检查相机是否有制定特性
	 * @param o 检查的特性 （格式：Parameters.FOCUS_MODE_AUTO）
	 * @return 是否存在
	 */
	public boolean checkAttuible(Objects o)
	{
		Parameters params = mCamera.getParameters();
		List<String> focusModes = params.getSupportedFocusModes();
		if(focusModes.contains(o)){
		// Autofocus mode is supported  
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 脸型检测
	 */
	public void faceDetection()
	{
		Parameters parameters = mCamera.getParameters();
		if(parameters.getMaxNumDetectedFaces()>0)//检查是否有脸型检测特性
		{
			mCamera.startFaceDetection();
		}
	}



	public void switchFlash(ImageView flash,int FlashID)
	{
		Drawable D = null;
		Parameters p = mCamera.getParameters();
		switch(FlashID)
		{
			case 0://不开启
				D = getResourcesbyName("flash_off");
				p.setFlashMode(Parameters.FLASH_MODE_OFF);
				break;
			case 1://开启
				D = getResourcesbyName("flash");
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
				break;
			case 2://自动
				D = getResourcesbyName("flash_auto");
				p.setFlashMode(Parameters.FLASH_MODE_AUTO);
				break;
			default://不改变
				//D = getResourcesbyName("flash_off");
				break;
		}
		if(D!=null)
		{
			//获取到了文件ID
			flash.setImageDrawable(D);
			mCamera.setParameters(p);
		}
	}

	/**
	 * 获取图片资源通过资源名称
	 * @param name 资源名称
	 * @return
	 */
	public static Drawable getResourcesbyName(String name)
	{
		Resources r = StaticValues.MainActivity.getResources();
		int indentify = r.getIdentifier(StaticValues.MainActivity.getPackageName()+":mipmap/"+name, null, null);
		if(indentify>0)
		{
			//获取到了文件
			return r.getDrawable(indentify);
		}
		return null;
	}

	/**
	 * 给图片添加文字到右下角
	 * @param gContext
	 * @param bm
	 * @param gText
	 * @return
	 */
	public Bitmap drawTextToBitmap(Context gContext,Bitmap bm,String gText) {
		if(bm==null||gText=="")return bm;
		Resources resources = gContext.getResources();
		float scale = resources.getDisplayMetrics().density;
		android.graphics.Bitmap.Config bitmapConfig =
				bm.getConfig();
		// set default bitmap config if none
		if(bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		// resource bitmaps are imutable,
		// so we need to convert it to mutable one
		bm = bm.copy(bitmapConfig, true);

		Canvas canvas = new Canvas(bm);
		// new antialised Paint
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// text color - #3D3D3D
		paint.setColor(Color.WHITE);
		paint.setTextSize((int) (28 * scale));
		paint.setDither(true); //获取跟清晰的图像采样
		paint.setFilterBitmap(true);//过滤一些
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = bm.getWidth()-bounds.width()-100;
		int y = bm.getHeight()-100;
		//canvas.drawText(gText, x * scale, y * scale, paint);
		canvas.drawText(gText,x,y,paint);
		return bm;
	}
}
