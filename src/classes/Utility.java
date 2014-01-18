package classes;

import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.widget.Toast;

public class Utility {
	
	public static final int MENU_DELETE_ITEM = 1;
	public static final int MENU_EDIT_ITEM = 2;
	
 	public static String GetColumnValue(Cursor cur, String ColumnName) {
		try {
			return cur.getString(cur.getColumnIndex(ColumnName));
		} catch (Exception ex) {
			return "";
		}
	}
 	
//	public static String GetString(EditText source) {
//		try {
//			return GetString(source.getText().toString());
//		} catch (Exception ex) {
//			return "";
//		}
//	}
//
//	public static String GetString(TextView source) {
//		try {
//			return GetString(source.getText().toString());
//		} catch (Exception ex) {
//			return "";
//		}
//	}
//
//	public static String GetString(Object source) {
//		try {
//			return GetString(source.toString());
//		} catch (Exception ex) {
//			return "";
//		}
//	}
	
	public static void ShowMessageBox(Context cont, String msg) {
		Toast toast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 0, 0);
		toast.show();
	}
}
