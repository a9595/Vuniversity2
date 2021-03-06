package com.example.vuniversity;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import classes.Student;
import classes.TestAdapter;
import classes.Utility;
import com.tieorange.vuniversity.R;

public class StudentsActivity extends MainActivity {
	private ArrayList<Student> listItems;
	private ListView listView;
	private String groupId, ORDER_BY, ASC_DESC;
	private EditText searchField;
	private ArrayAdapter<Student> adapter;

	private Adapter getListAdapter() {
		return new ArrayAdapter<Student>(this,
				android.R.layout.simple_list_item_1, listItems);
	}

	public void loadList() {

		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		listItems = mDbHelper.getAllStudents(groupId, ORDER_BY, ASC_DESC);
		adapter = new ArrayAdapter<Student>(this,
				android.R.layout.simple_list_item_1, listItems);
		listView.setAdapter(adapter);

		mDbHelper.close();
	}

	public void onClickAddNew(View view) {
		Intent intent = new Intent(view.getContext(), AddStudentActivity.class);
		intent.putExtra("id", groupId);
		startActivity(intent);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		Student Item = (Student) getListAdapter().getItem(info.position);
		switch (item.getItemId()) {
		case R.id.contextMenuDeleteItem: {
			TestAdapter mDbHelper = new TestAdapter(this);
			mDbHelper.createDatabase();
			mDbHelper.open();
			mDbHelper.RemoveStudentById(Item.getId());
			mDbHelper.close();
			loadList();

			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("lastStudentId", 0);
			editor.commit();
			break;
		}
		case R.id.contextMenuEditItem: {
			Intent intent = new Intent(this, EditStudentActivity.class);
			intent.putExtra("studentId", Item.getId());
			intent.putExtra("groupId", Item.getGroupId());
			startActivity(intent);
			break;
		}
		case R.id.contextMenuPutMarkForStudent: {
			Intent intent = new Intent(this, PutMarkForStudent.class);
			intent.putExtra("studentId", Item.getId());
			intent.putExtra("groupId", Item.getGroupId());
			startActivity(intent);
			break;
		}

		}
		return true;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				groupId = null;
			} else {
				groupId = extras.getString("groupId");
			}
		} else {
			groupId = (String) savedInstanceState.getSerializable("groupId");
		}

		setContentView(R.layout.list);
		listView = (ListView) findViewById(R.id.list);

		registerForContextMenu(listView);
		loadList();

		listView.setClickable(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg) {
				Student selectedItem = (Student) adapter.getAdapter().getItem(
						position);
				Intent intent = new Intent(view.getContext(),
						ShowDetailsStudent.class);
				intent.putExtra("groupId", groupId);
				intent.putExtra("studentId", selectedItem.getId());
				startActivity(intent);

			}
		});
		searchField = (EditText) findViewById(R.id.search_field);
		searchField.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				adapter.getFilter().filter(cs);
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		String header = listItems.get(info.position).toString();
		menu.setHeaderTitle(header);
		if (v.getId() == R.id.list) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.student_put_mark_edit, menu);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.students, menu);
		return true;// return true so to menu pop up is opens
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menuBySurnameASC:
			ORDER_BY = Utility.SURNAME;
			ASC_DESC = Utility.ASC;
			loadList();
			return true;
		case R.id.menuBySurnameDESC:
			ORDER_BY = Utility.SURNAME;
			ASC_DESC = Utility.DESC;
			loadList();
			return true;
		case R.id.menuByEskaASC:
			ORDER_BY = Utility.ESKA;
			ASC_DESC = Utility.ASC;
			loadList();
			return true;
		case R.id.menuByEskaDESC:
			ORDER_BY = Utility.ESKA;
			ASC_DESC = Utility.DESC;
			loadList();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadList();
	}

}
