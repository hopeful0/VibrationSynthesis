package lzu.hopeful.phy.flash.vs;
import android.content.*;
import android.view.*;
import android.view.SurfaceHolder.Callback;
import android.util.*;
import android.graphics.*;

public class MainView extends SurfaceView implements Callback,Runnable
{
	private static final int SLEEP_TIME = 20;
	
	boolean flag;
	
	Thread mThread;
	
	SurfaceHolder mHolder;
	
	FlashView fView;
	
	long lastTime;
	
	public MainView(Context c)
	{
		super(c);
		mHolder = this.getHolder();
		mHolder.addCallback(this);
		fView = new FlashView();
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder p1)
	{
		flag = true;
		mThread = new Thread(this);
		mThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4)
	{
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder p1)
	{
		mThread = null;
		//mThread.destroy();
	}
	
	public void run()
	{
		initDrawTools();
		lastTime = System.currentTimeMillis();
		while (flag)
		{
			try
			{
				Thread.sleep(SLEEP_TIME);
			}
			catch (InterruptedException e)
			{}
			try
			{
				logic();
			}
			catch (Exception e)
			{
				Log.i("MainView_LogicError",e.toString());
			}
			try
			{
				mCanavs = mHolder.lockCanvas();
				if(mCanavs != null)
					draw();
			}
			catch (Exception e)
			{
				Log.i("MainView_DrawError",e.toString());
			}
			finally
			{
				if(mCanavs!=null)
					mHolder.unlockCanvasAndPost(mCanavs);
			}
		}
	}
	
	private void logic() throws Exception
	{
		long time = System.currentTimeMillis();
		fView.run(time - lastTime);
		lastTime = time;
	}
	
	Canvas mCanavs;
	
	Paint mPaint;
	
	private void initDrawTools()
	{
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.RED);
	}
	
	private void draw() throws Exception
	{
		
		mCanavs.drawColor(0,PorterDuff.Mode.CLEAR);
		fView.draw(mCanavs);
		
		//mHolder.unlockCanvasAndPost(mCanavs);
	}
}
