package lzu.hopeful.phy.flash.vs;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;
import android.content.*;
import android.text.*;

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
		findViewById(R.id.btn_stime).setOnClickListener(this);
    }
	
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.btn_list :
				showListDialog();
			break;
			case R.id.btn_stime :
				showSTimeDialog();
			break;
		}
	}
	
	private void showSTimeDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("调整精度");
		builder.setMessage("注意:此项将调整屏幕每秒绘图次数，进而影响绘图精度，高精度意味着高耗电！数据越小精度越高，建议在10-50之间！");
		final EditText et_stime = new EditText(this);
		et_stime.setInputType(InputType.TYPE_CLASS_NUMBER);
		et_stime.setText(""+MainView.SLEEP_TIME);
		builder.setView(et_stime);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO: Implement this method
					String sValue = et_stime.getText().toString();
					MainView.SLEEP_TIME = Integer.valueOf(sValue.length()<=0?"0":sValue);
					mainView.fView.restart();
					//dialog.dismiss();
				}
			});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO: Implement this method
					//dialog.dismiss();
				}
			});
		builder.create().show();
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
				if(isNullEditText(view)) return;
				Vibration v = new Vibration();
				setVibrationFormView(view,v);
				mainView.fView.addVibration(v);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO: Implement this method
				//dialog.dismiss();
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
					if(isNullEditText(view)) return;
					setVibrationFormView(view,v);
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
	
	private boolean isNullEditText(View view)
	{
		int ids[] = new int[]{R.id.et_A,R.id.et_omega,R.id.et_phi0,R.id.et_direction};
		for(int id : ids)
		{
			if(((EditText)view.findViewById(id)).getText().toString().length() <= 0)
			{
				//Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
				new AlertDialog.Builder(this).setTitle("错误").setMessage("部分属性为空！").create().show();
				return true;
			}
		}
		return false;
	}
	
	private void setVibrationFormView(View view,Vibration v)
	{
		v.setA(Double.valueOf(((EditText)view.findViewById(R.id.et_A)).getText().toString()));
		v.setOmega(Double.valueOf(((EditText)view.findViewById(R.id.et_omega)).getText().toString()));
		v.setPhi0(Double.valueOf(((EditText)view.findViewById(R.id.et_phi0)).getText().toString()));
		v.setDirection(Double.valueOf(((EditText)view.findViewById(R.id.et_direction)).getText().toString()));
		v.setA(v.getA() * (((ToggleButton)view.findViewById(R.id.tb_A)).isChecked() ? Math.PI : 1));
		v.setOmega(v.getOmega() * (((ToggleButton)view.findViewById(R.id.tb_omega)).isChecked() ? Math.PI : 1));
		v.setPhi0(v.getPhi0() * (((ToggleButton)view.findViewById(R.id.tb_phi0)).isChecked() ? Math.PI : 1));
		v.setDirection(v.getDirection() * (((ToggleButton)view.findViewById(R.id.tb_direction)).isChecked() ? Math.PI : 1));
	}
}
