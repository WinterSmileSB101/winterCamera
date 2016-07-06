package Enum;

import android.Manifest;

/**
 * Created by Administrator on 2016/6/30.
 */
public class PremissionEnum{
	/**
	 * 相机权限
	 */
	public static final String CAMERA = Manifest.permission.CAMERA;
	/**
	 * 写入权限
	 */
	public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

	/**
	 * 读取内部权限
	 */
	public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
	/**
	 * 装载内存卡
	 */
	public static final String MOUNT_UNMOUNT_FILESYSTEMS = Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS;//装载内存卡
	/**
	 * 自动对焦
	 */
	public static final String CAMERA_AUTOFOCOUS = "";//自动对焦
	/**
	 * 录制声音
	 */
	public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
}
