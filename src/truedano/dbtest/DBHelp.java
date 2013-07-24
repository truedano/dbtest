package truedano.dbtest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelp extends SQLiteOpenHelper {
	String DB_ITEMS = 
	"_ID INTEGER PRIMARY KEY" +
	",FLAG varchar" +
	",DEVICE_NAME varchar" +
	",FULL_PATH varchar" +
	",DIR varchar" +
	",TIME varchar";
	
	public DBHelp(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists fav00("+DB_ITEMS+")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}


}
