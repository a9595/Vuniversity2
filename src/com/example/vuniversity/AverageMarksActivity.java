package com.example.vuniversity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import classes.AverageStudentMarkOfSubject;
import classes.TestAdapter;
import classes.Utility;
import com.tieorange.vuniversity.R;

public class AverageMarksActivity extends MainActivity {
	Button buttonAddNew;
	private ArrayList<AverageStudentMarkOfSubject> listItems;
	private ListView listView;
	private EditText searchField;
	private ArrayAdapter<AverageStudentMarkOfSubject> adapter;
	private String ORDER_BY, ASC_DESC, subjectId;

	public void loadList() {
		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		listItems = mDbHelper.getAverageMarksOfStudents(subjectId, ORDER_BY,
				ASC_DESC);
		adapter = new ArrayAdapter<AverageStudentMarkOfSubject>(this,
				android.R.layout.simple_list_item_1, listItems);
		listView.setAdapter(adapter);

		mDbHelper.close();
	}

	public void onClickAddNew(View view) {
		Intent intent = new Intent(view.getContext(), AddSubjectActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				subjectId = null;
			} else {
				subjectId = extras.getString("subjectId");
			}
		} else {
			subjectId = (String) savedInstanceState
					.getSerializable("subjectId");
		}
		listView = (ListView) findViewById(R.id.list);
		loadList();

		buttonAddNew = (Button) findViewById(R.id.buttonAddNew);
		buttonAddNew.setVisibility(View.GONE);
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
			inflater.inflate(R.menu.context_menu, menu);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.marks, menu);
		return true;// return true so to menu pop up is opens
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menuByMarksASC:
			ORDER_BY = Utility.AVERAGE_MARK;
			ASC_DESC = Utility.ASC;
			loadList();
			return true;
		case R.id.menuByMarksDESC:
			ORDER_BY = Utility.AVERAGE_MARK;
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
