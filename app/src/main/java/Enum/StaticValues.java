package Enum;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.List;

import Help.CameraPreview;

/**
 * 全局变量类
 * Created by Administrator on 2016/7/1.
 */
public class StaticValues{
	/**
	 * 窗口管理器，用于获取屏幕宽高
	 */
	public static WindowManager mWM;
	/**
	 * 获取屏幕宽高时的中间变量
	 */
	public static DisplayMetrics outMetrics;
	/**
	 * 屏幕宽
	 */
	public static int SCREEN_WIDTH;
	/**
	 * 屏幕高
	 */
	public static int SCREEN_HEIGTH;
	/**
	 * 主活动
	 */
	public static Activity MainActivity;
	/**
	 * 相机对象
	 */
	public static CameraPreview ImagesActivityCamera;
	/**
	 * surfaceview
	 */
	public static SurfaceView mSurfaceView;
	/**
	 * 镜头编号
	 */
	public static int CAMERA_NUM = 0;
	/**
	 * 闪光灯状态（0-2）
	 * 0 不开启
	 * 1 开启
	 * 2 自动
	 */
	public static int FLASH_STATE = 0;
	/**
	 * 闪光灯背景色51,193,240
	 */
	public static int FLASH_BGCOLOR = Color.rgb(51,193,240);
	/**
	 * Image当前旋转位置
	 */
	public static int Image_CUR_ROTATION = 0;
	/**
	 * 屏幕上次所在旋转位置
	 */
	public static int SCREEN_LAST_ROTATION=361;
	/**
	 * 旋转路径记录中间用||分割
	 */
	public static String ROTATE_THROW_PATH = "";
	/**
	 * 屏幕旋转路径的标示下
	 */
	public static final String ROTATE_PATH_FLAG_BOTTOM = "B";
	/**
	 * 屏幕旋转路径的标示右
	 */
	public static final String ROTATE_PATH_FLAG_RIGHT = "R";
	/**
	 * 屏幕旋转路径的标示上
	 */
	public static final String ROTATE_PATH_FLAG_TOP = "T";
	/**
	 * 屏幕旋转路径的标示上
	 */
	public static final String ROTATE_PATH_FLAG_LEFT = "L";
	/**
	 * 旋转方向
	 */
	public static String ROTATE_DIR = "";
	/**
	 * 是否正在录像
	 */
	public static boolean Videoing  = false;
	/**
	 * 保存图片的位置
	 */
	public static String ImagePath;
	/**
	 * 特效处理后的图片位置
	 */
	public static String EfImagePath;
	/**
	 * 特效实时预览界面
	 */
	public static List<SurfaceView> EffectsSurfaceViews = null;
	/**
	 * 临时文件路劲
	 */
	public static String tempPreviewFilePath = Environment
			.getExternalStorageDirectory() + "/馨拍/Images/tempImage/";// 保存图像的路径
	/**
	 * 保存视频文件的路径
	 */
	public static String VideoFilePath = Environment.getExternalStorageDirectory()+"/馨拍/Videos/";
}
