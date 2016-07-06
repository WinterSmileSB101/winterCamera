package SelfAni;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import winter.zxb.smilesb101.winterCamer.ImagesActivity;

/**
 * 属性动画类
 * Created by Administrator on 2016/7/2.
 */
public class ProvertyAnimation{
	/**
	 * 从小到大放缩的动画
	 * @param v
	 * @param fromScale
	 * @param toScale
	 */
	public static void ScaleSmaleToLarge(final ImageView v,float fromScale,float toScale)
	{
		ValueAnimator va = ValueAnimator.ofFloat(fromScale,toScale);
		va.setDuration(300);
		va.setInterpolator(new AccelerateDecelerateInterpolator());
		va.start();
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator){
				//frameValue是通过startValue和endValue以及Fraction计算出来的
				Float frameValue = (Float)valueAnimator.getAnimatedValue();
				v.setPivotX(0.5f);
				v.setPivotY(0.5f);
				v.setScaleX(frameValue);
				v.setScaleY(frameValue);
			}
		});
	}

	/**
	 * Alpha值变化到0
	 * @param v
	 */
	public static void AlphaTo0(final ImageView v)
	{
		int Duration = 300;
		ValueAnimator va = ValueAnimator.ofFloat(255,0);
		va.setDuration(Duration);
		va.setInterpolator(new AccelerateDecelerateInterpolator());
		va.start();
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator){
				//frameValue是通过startValue和endValue以及Fraction计算出来的
				Float frameValue = (Float)valueAnimator.getAnimatedValue();
				v.setAlpha(frameValue);
			}
		});
		Handler mHander = new Handler();
		mHander.postDelayed(new Runnable(){
			@Override
			public void run(){
				v.setVisibility(View.INVISIBLE);
			}
		},300);
	}

	/**
	 * Alpha值变化到0
	 * @param v
	 */
	public static void AlphaTo0(final ProgressBar v)
	{
		int Duration = 300;
		ValueAnimator va = ValueAnimator.ofFloat(255,0);
		va.setDuration(Duration);
		va.setInterpolator(new AccelerateDecelerateInterpolator());
		va.start();
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator){
				//frameValue是通过startValue和endValue以及Fraction计算出来的
				Float frameValue = (Float)valueAnimator.getAnimatedValue();
				v.setAlpha(frameValue);
			}
		});
		Handler mHander = new Handler();
		mHander.postDelayed(new Runnable(){
			@Override
			public void run(){
				v.setVisibility(View.INVISIBLE);
			}
		},300);
	}

	/**
	 * Alpha值变化到0
	 * @param v
	 */
	public static void AlphaTo0(final TextView v)
	{
		int Duration = 300;
		ValueAnimator va = ValueAnimator.ofFloat(255,0);
		va.setDuration(Duration);
		va.setInterpolator(new AccelerateDecelerateInterpolator());
		va.start();
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator){
				//frameValue是通过startValue和endValue以及Fraction计算出来的
				Float frameValue = (Float)valueAnimator.getAnimatedValue();
				v.setAlpha(frameValue);
			}
		});
		Handler mHander = new Handler();
		mHander.postDelayed(new Runnable(){
			@Override
			public void run(){
				v.setVisibility(View.INVISIBLE);
			}
		},300);
	}

	/**
	 * Alpha值变化到0
	 * @param v
	 */
	public static void AlphaTo0(final LinearLayout v)
	{
		int Duration = 300;
		ValueAnimator va = ValueAnimator.ofFloat(255,0);
		va.setDuration(Duration);
		va.setInterpolator(new AccelerateDecelerateInterpolator());
		va.start();
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator){
				//frameValue是通过startValue和endValue以及Fraction计算出来的
				Float frameValue = (Float)valueAnimator.getAnimatedValue();
				v.setAlpha(frameValue);
			}
		});
		Handler mHander = new Handler();
		mHander.postDelayed(new Runnable(){
			@Override
			public void run(){
				v.setVisibility(View.INVISIBLE);
			}
		},300);
	}

	/**
	 * Alpha值从0变化到255
	 * @param v
	 */
	public static void AlphaToMax(final ImageView v)
	{
		int Duration = 300;
		ValueAnimator va = ValueAnimator.ofFloat(0,255);
		va.setDuration(Duration);
		va.setInterpolator(new AccelerateDecelerateInterpolator());
		v.setVisibility(View.VISIBLE);
		va.start();
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator){
				//frameValue是通过startValue和endValue以及Fraction计算出来的
				Float frameValue = (Float)valueAnimator.getAnimatedValue();
				v.setAlpha(frameValue);
			}
		});
	}

	/**
	 * Alpha值从0变化到255
	 * @param v
	 */
	public static void AlphaToMax(final ProgressBar v)
	{
		int Duration = 300;
		ValueAnimator va = ValueAnimator.ofFloat(0,255);
		va.setDuration(Duration);
		va.setInterpolator(new AccelerateDecelerateInterpolator());
		v.setVisibility(View.VISIBLE);
		va.start();
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator){
				//frameValue是通过startValue和endValue以及Fraction计算出来的
				Float frameValue = (Float)valueAnimator.getAnimatedValue();
				v.setAlpha(frameValue);
			}
		});
	}

	/**
	 * Alpha值从0变化到255
	 * @param v
	 */
	public static void AlphaToMax(final TextView v)
	{
		int Duration = 300;
		ValueAnimator va = ValueAnimator.ofFloat(0,255);
		va.setDuration(Duration);
		va.setInterpolator(new AccelerateDecelerateInterpolator());
		v.setVisibility(View.VISIBLE);
		va.start();
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator){
				//frameValue是通过startValue和endValue以及Fraction计算出来的
				Float frameValue = (Float)valueAnimator.getAnimatedValue();
				v.setAlpha(frameValue);
			}
		});
	}

	/**
	 * Alpha值从0变化到255
	 * @param v
	 */
	public static void AlphaToMax(final LinearLayout v)
	{
		int Duration = 300;
		ValueAnimator va = ValueAnimator.ofFloat(0,255);
		va.setDuration(Duration);
		va.setInterpolator(new AccelerateDecelerateInterpolator());
		v.setVisibility(View.VISIBLE);
		va.start();
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator){
				//frameValue是通过startValue和endValue以及Fraction计算出来的
				Float frameValue = (Float)valueAnimator.getAnimatedValue();
				v.setAlpha(frameValue);
			}
		});
	}
	/**
	 * 闪光灯按钮按下后弹出选择面板(属性动画实现)
	 * @param text
	 * @param toX
	 */
	public static void FlashOutAni(final TextView text,int toX)
	{
		int[] t = new int[2];
		text.getLocationOnScreen(t);
		//Log.i("asdad",t[0]+"");
		if(t[0]!= ImagesActivity.flash_POS) {
			toX = 0;
		}
		ValueAnimator anim= ValueAnimator.ofInt(t[0], toX);
		anim.setDuration(100);
		anim.setInterpolator(new DecelerateInterpolator());
		anim.start();
		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				//frameValue是通过startValue和endValue以及Fraction计算出来的
				int frameValue = (Integer)animation.getAnimatedValue();
				//Log.i("asdasdd",frameValue+"");
				text.setPivotX(0.5f);
				text.setPivotY(0.5f);
				text.setTranslationX(frameValue);
				//利用每一帧返回的值，可以做一些改变View大小，坐标，透明度等等操作
			}
		});
	}

	/**
	 * 两个按钮交换位置
	 * @param center
	 * @param right
	 */
	public static void ExchangeBtn(final ImageView center,final ImageView right)
	{
		//Log.i("asda",center.getTranslationX()+"  "+right.getTranslationX());
		if(center.getTranslationX() == right.getTranslationX()) {
			PropertyValuesHolder cmove = PropertyValuesHolder.ofInt("cmove",0,398);
			PropertyValuesHolder cscale = PropertyValuesHolder.ofFloat("cscale",1,0.56f);

			PropertyValuesHolder rmove = PropertyValuesHolder.ofInt("rmove",0,- 345);
			PropertyValuesHolder rscale = PropertyValuesHolder.ofFloat("rscale",1,1.6f);

			ValueAnimator va = new ValueAnimator().ofPropertyValuesHolder(cmove,cscale,rmove,rscale);
			va.setInterpolator(new AccelerateDecelerateInterpolator());
			va.setDuration(300);
			va.start();
			va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator){
					int cmove = (Integer)valueAnimator.getAnimatedValue("cmove");
					float cscale = (Float)valueAnimator.getAnimatedValue("cscale");
					//Log.i("Clis",""+cmove+"  "+cscale);
					center.setPivotX(0.5f);
					center.setPivotY(0.5f);
					center.setTranslationX(cmove);
					center.setTranslationY(68);
					center.setScaleX(cscale);
					center.setScaleY(cscale);

					int rmove = (Integer)valueAnimator.getAnimatedValue("rmove");
					float rscale = (Float)valueAnimator.getAnimatedValue("rscale");
					//Log.i("Rlis",""+rmove+"  "+rscale);
					right.setTranslationX(rmove);
					right.setScaleX(1.5f);
					right.setScaleY(1.5f);
					right.setTranslationY(- 12);
				}
			});
		}
		else
		{
			PropertyValuesHolder rmove = PropertyValuesHolder.ofInt("rmove",(int)right.getTranslationX(),0);
			PropertyValuesHolder rscale = PropertyValuesHolder.ofFloat("rscale",0.56f,1f);

			PropertyValuesHolder cmove = PropertyValuesHolder.ofInt("cmove",(int)center.getTranslationX(),0);
			PropertyValuesHolder cscale = PropertyValuesHolder.ofFloat("cscale",1.6f,1f);

			ValueAnimator va = new ValueAnimator().ofPropertyValuesHolder(cmove,cscale,rmove,rscale);
			va.setInterpolator(new AccelerateDecelerateInterpolator());
			va.setDuration(200);
			va.start();
			va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator){
					int rmove = (Integer)valueAnimator.getAnimatedValue("rmove");
					float rscale = (Float)valueAnimator.getAnimatedValue("rscale");
					//Log.i("Clis",""+cmove+"  "+cscale);
					right.setTranslationX(rmove);
					right.setTranslationY(-3);
					right.setScaleX(rscale);
					right.setScaleY(rscale);

					int cmove = (Integer)valueAnimator.getAnimatedValue("cmove");
					float cscale = (Float)valueAnimator.getAnimatedValue("cscale");
					//Log.i("Rlis",""+rmove+"  "+rscale);
					center.setPivotX(0.5f);
					center.setPivotY(0.5f);
					center.setTranslationX(cmove);
					center.setScaleX(cscale);
					center.setScaleY(cscale);
					center.setTranslationY(3);
				}
			});
		}
	}
}
