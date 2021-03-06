package com.example.vuniversity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import classes.Teacher;
import classes.TestAdapter;
import classes.Utility;
import com.tieorange.vuniversity.R;

public class AddTeacherActivity extends MainActivity {
	Button buttonSubmitNew;
	EditText editTextName, editTextSurname;
	LinearLayout linearLayoutAddSubject;

	public void onClickAdd(View view) {
		if (editTextName.getText().length() <= 0
				|| editTextSurname.getText().length() <= 0) {
			Utility.ShowMessageBox(view.getContext(),
					"Fill all the fields please..");
			return;
		}
		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		String name = editTextName.getText().toString();
		String surname = editTextSurname.getText().toString();

		if (mDbHelper.isTeacherExist(new Teacher(name, surname))) {
			Utility.ShowMessageBox(view.getContext(),
					"Such teacher already exists");
			return;
		}

		if (mDbHelper.AddTeacher(name, surname)) {
			Utility.ShowMessageBox(this, "added");
			finish();
		} else {
			Utility.ShowMessageBox(this, "OOPS try again!");
		}

	}

	public void onClickAddNewSubject(View view) {
		Intent intent = new Intent(view.getContext(),
				AddSubjectForTeacher.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_teacher);

		buttonSubmitNew = (Button) findViewById(R.id.buttonSubmitNewStudent);
		editTextName = (EditText) findViewById(R.id.editTextStudentName);
		editTextSurname = (EditText) findViewById(R.id.editTextStudentSurname);
		linearLayoutAddSubject = (LinearLayout) findViewById(R.id.linearLayoutAddSubject);
		linearLayoutAddSubject.setVisibility(View.GONE);
	}
}
