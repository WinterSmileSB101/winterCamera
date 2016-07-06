package Help;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.hardware.Camera;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileInputStream;

import Enum.*;
import SelfWidget.RectWidget;
import winter.zxb.smilesb101.winterCamer.ImagesActivity;
import winter.zxb.smilesb101.winterCamer.R;

/**
 * 人脸识别相关操作类
 * Created by Administrator on 2016/7/2.
 */
public class FaceDectection{

	/**
	 * 脸型检测监听
	 */
	static Camera.FaceDetectionListener  mFaceDetectionLis = new Camera.FaceDetectionListener(){
		@Override
		public void onFaceDetection(Camera.Face[] faces,Camera camera){
			if(faces.length>0)
				Log.d("脸型检测","face detected: "+faces.length+" Face 1 Location X: "+faces[0].rect.centerX()+ " Y: "+faces[0].rect.centerY());
			if(faces.length > 0) {
				RectWidget rw = new RectWidget(StaticValues.MainActivity.getApplicationContext(),faces[0].rect.centerX(),
						faces[0].rect.centerY(),100,100,Color.blue(100));
				RelativeLayout rl = new RelativeLayout(StaticValues.MainActivity.getApplicationContext());
				RelativeLayout.LayoutParams rll =   new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT);
				//rl.addView(rw);
				RelativeLayout mrl = (RelativeLayout)ImagesActivity.mView.findViewById(R.id.main_content);
				ImageView iw = new ImageView(StaticValues.MainActivity);

				iw.setImageDrawable(ImagesActivity.mView.getResources().getDrawable(R.drawable.my_ring_shape));
				//mrl.addView(iw,rll);
			}
		}

	};

}
