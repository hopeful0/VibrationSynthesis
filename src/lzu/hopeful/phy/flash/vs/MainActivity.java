package lzu.hopeful.phy.flash.vs;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;
import android.content.*;

public class MainActivity extends Activity implements OnClickListener
{
	MainView mainView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		mainView = new MainView(this);
		((LinearLayout)findViewById(R.id.mainLinearLayout1)).addView(mainView);
		findViewById(R.id.btn_list).setOnClickListener(this);
    }
	
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.btn_list :
				showListDialog();
			break;
		}
	}
	
	private void showListDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("振动列表");
		String[] items = new String[2+mainView.fView.vibrations.size()];
		items[0] = "添加振动";
		items[1] = "删除振动";
		for(int i = 0;i < mainView.fView.vibrations.size();i++)
			items[i+2] = "振动"+i;
		builder.setItems(items, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO: Implement this method
				switch(which)
				{
					case 0:
						showCreateDialog();
					break;
					case 1:

					break;
					default:
						showVibrationDialog(which-2);
					break;
				}
			}
		});
		builder.create().show();
	}
	
	private void showCreateDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View view = getLayoutInflater().inflate(R.layout.vibration,(ViewGroup)findViewById(R.id.layout_creat));
		view.findViewById(R.id.btn_delete).setVisibility(View.GONE);
		builder.setTitle("振动列表").setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO: Implement this method
				Vibration v = new Vibration();
				v.setA(Double.valueOf(((EditText)view.findViewById(R.id.et_A)).getText().toString()));
				v.setOmega(Double.valueOf(((EditText)view.findViewById(R.id.et_omega)).getText().toString()));
				v.setPhi0(Double.valueOf(((EditText)view.findViewById(R.id.et_phi0)).getText().toString()));
				v.setDirection(Double.valueOf(((EditText)view.findViewById(R.id.et_direction)).getText().toString()));
				mainView.fView.addVibration(v);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO: Implement this method
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	private void showVibrationDialog(int index)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View view = getLayoutInflater().inflate(R.layout.vibration,(ViewGroup)findViewById(R.id.layout_creat));
		final Vibration v = mainView.fView.vibrations.get(index);
		view.findViewById(R.id.btn_delete).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View p1)
			{
				// TODO: Implement this method
				mainView.fView.removeVibration(v);
			}
		});
		((EditText)view.findViewById(R.id.et_A)).setText(""+v.getA());
		((EditText)view.findViewById(R.id.et_omega)).setText(""+v.getOmega());
		((EditText)view.findViewById(R.id.et_phi0)).setText(""+v.getPhi0());
		((EditText)view.findViewById(R.id.et_direction)).setText(""+v.getDirection());
		builder.setTitle("振动"+index).setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO: Implement this method
					v.setA(Double.valueOf(((EditText)view.findViewById(R.id.et_A)).getText().toString()));
					v.setOmega(Double.valueOf(((EditText)view.findViewById(R.id.et_omega)).getText().toString()));
					v.setPhi0(Double.valueOf(((EditText)view.findViewById(R.id.et_phi0)).getText().toString()));
					v.setDirection(Double.valueOf(((EditText)view.findViewById(R.id.et_direction)).getText().toString()));
					mainView.fView.restart();
				}
			});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO: Implement this method
					dialog.dismiss();
				}
			});
		builder.create().show();
	}
}
