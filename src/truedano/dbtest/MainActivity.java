package truedano.dbtest;

import java.util.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends Activity {
	BaseAdapter listdb = new BaseAdapter(){

		@Override
		public int getCount() {
			return getDBCount("fav", "fav00");
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.listdb_item,null);
			TextView devicename_tv = (TextView) convertView.findViewById(R.id.devicename_tv);
			devicename_tv.setText(devicename[position]);
			return convertView;
		}
		
	};
	EditText input_et ;
	Button add_bt;
	ListView listdb_lv;
	String flag[]=null,devicename[]=null,fullpath[]=null,dir[]=null;
	int _id[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setView();
	}
	
	private void setView(){
		input_et = (EditText)findViewById(R.id.input_et);
		add_bt = (Button)findViewById(R.id.add_bt);
		add_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String input = input_et.getText().toString();
				if(input.equals("")){
					return;
				}
				
				if(checkItemExist("DEVICE_NAME",input)){
					input_et.setText("");
					return;
				}
				addDB("fav","fav00","DEVICE_NAME",input);
				initData();
				
				input_et.setText("");
				listdb.notifyDataSetChanged();
			}
		});
		listdb_lv = (ListView)findViewById(R.id.listdb_lv);
		initData();
		listdb_lv.setAdapter(listdb);
		listdb_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				deleteDBByID("fav", "fav00", _id[position]);
				initData();
				listdb.notifyDataSetChanged();
			}
		});
		
	}
	
// Function //////////////////////////////////////////////////////////////////////////////////////////////////////
	private void initData(){
		_id = getDBID("fav", "fav00", "_ID");
		flag = getDB("fav", "fav00", "FLAG");
		devicename = getDB("fav", "fav00", "DEVICE_NAME");
		fullpath = getDB("fav", "fav00", "FULL_PATH");
		dir = getDB("fav", "fav00", "DIR");
	}
	
	private boolean checkItemExist(String item,String value){
		initData();
		if(item.equals("DEVICE_NAME")){
			int dbcnt = getDBCount("fav", "fav00");
			for(int i=0;i<dbcnt;i++){
				if(value.equals(devicename[i])){
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void addDB(String dbname,String tbname,String item,String value){
		DBHelp mydb = new DBHelp(getApplicationContext(), dbname, null, 1);
		SQLiteDatabase db = mydb.getWritableDatabase();
		Cursor cursor = db.rawQuery("select FLAG from "+tbname+" ORDER BY _ID DESC", null);
		if(cursor.getCount() >= 5){
			cursor.close();
			db.close();
			return ;
		}
		ContentValues cv = new ContentValues();
		
		cv.put(item,value);
		cv.put("TIME",getCurrentTime());
		db.insert(tbname, "", cv);
		
		cursor.close();
		db.close();
	}
	
	private void deleteDBByID(String dbname,String tbname,int _ID){
		DBHelp mydb = new DBHelp(getApplicationContext(), dbname, null, 1);
		SQLiteDatabase db = mydb.getWritableDatabase();
		Cursor cursor = db.rawQuery("select FLAG from "+tbname+" ORDER BY _ID DESC", null);
		cursor.moveToFirst();
		db.delete(tbname, "_ID=" + _ID, null);
		cursor.close();
		db.close();
	}
	
	private void deleteDBByDeviceName(String dbname,String tbname,String devicename){
		DBHelp mydb = new DBHelp(getApplicationContext(), dbname, null, 1);
		SQLiteDatabase db = mydb.getWritableDatabase();
		Cursor cursor = db.rawQuery("select FLAG from "+tbname+" ORDER BY _ID DESC", null);
		cursor.moveToFirst();
		db.delete(tbname, "DEVICE_NAME=" + devicename, null);
		cursor.close();
		db.close();
	}
	
	private int getDBCount(String dbname,String tbname){
		DBHelp mydb = new DBHelp(getApplicationContext(), dbname, null, 1);
		SQLiteDatabase db = mydb.getWritableDatabase();
		Cursor cursor = db.rawQuery("select FLAG from "+tbname+" ORDER BY _ID DESC", null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}
	
	private String[] getDB(String dbname,String tbname,String item){
		DBHelp mydb = new DBHelp(getApplicationContext(), dbname, null, 1);
		SQLiteDatabase db = mydb.getWritableDatabase();
		Cursor cursor = db.rawQuery("select "+item+" from "+tbname+" ORDER BY _ID DESC", null);
		String[] sNote = new String[cursor.getCount()];
		int rows_num = cursor.getCount();//取得資料表列數
		for(int i=0;i<rows_num;i++){
			sNote[i] = "";
		}
		if(rows_num != 0) {
			cursor.moveToFirst();
			for(int i=0; i<rows_num; i++) {
				String strCr = cursor.getString(0);
				sNote[i]=strCr;
				cursor.moveToNext();
			}
		}
		
		cursor.close();
		db.close();
		return sNote;
	}
	
	private int[] getDBID(String dbname,String tbname,String item){
		DBHelp mydb = new DBHelp(getApplicationContext(), dbname, null, 1);
		SQLiteDatabase db = mydb.getWritableDatabase();
		Cursor cursor = db.rawQuery("select "+item+" from "+tbname+" ORDER BY _ID DESC", null);
		int[] sNote = new int[cursor.getCount()];
		int rows_num = cursor.getCount();//取得資料表列數
		for(int i=0;i<rows_num;i++){
			sNote[i] = 0;
		}
		if(rows_num != 0) {
			cursor.moveToFirst();
			for(int i=0; i<rows_num; i++) {
				int strCr = cursor.getInt(0);
				sNote[i]=strCr;
				cursor.moveToNext();
			}
		}
		
		cursor.close();
		db.close();
		return sNote;
	}
	
	@SuppressLint("DefaultLocale")
	private String getCurrentTime(){
		Calendar c = Calendar.getInstance(); 
		int seconds = c.get(Calendar.SECOND);
		int minutes = c.get(Calendar.MINUTE);
		int hours = c.get(Calendar.HOUR)+12;
		int dates = c.get(Calendar.DATE);
		int months = c.get(Calendar.MONTH)+1;
		int years = c.get(Calendar.YEAR);
		String result = String.format("%04d%02d%02d%02d%02d%02d", years,months,dates,hours,minutes,seconds);
		return result;
	}
}
