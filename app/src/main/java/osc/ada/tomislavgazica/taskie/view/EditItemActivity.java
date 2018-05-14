package osc.ada.tomislavgazica.taskie.view;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.model.Category;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskPriority;


public class EditItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EDIT_TASK = "edit_task";

    @BindView(R.id.edittext_newtask_enterTitle)
    EditText titleEntry;
    @BindView(R.id.edittext_newtask_enterDescription)
    EditText descriptionEntry;
    @BindView(R.id.spinner_newtask_category)
    Spinner categoryEntry;
    @BindView(R.id.spinner_newtask_priority)
    Spinner priorityEntry;
    @BindView(R.id.button_newtask_setDate)
    Button saveDateEntry;
    @BindView(R.id.textview_newtask_dueDate)
    TextView dueDate;
    private int year, month, day;

    Task task;
    String ID;

    List<Category> categories;

    SharedPreferences pref;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        realm = Realm.getDefaultInstance();
        pref = getApplicationContext().getSharedPreferences(TasksActivity.SHARED_PREFS_PRIORITY, MODE_PRIVATE);

        ButterKnife.bind(this);
        setUpSpinnerSource();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EDIT_TASK)) {
            ID = intent.getStringExtra(EDIT_TASK);
        }

        task = realm.where(Task.class).equalTo("ID", ID).findFirst();

        if (task != null) {
            titleEntry.setText(task.getTitle());

            descriptionEntry.setText(task.getDescription());

            year = task.getEndDateYear();
            month = task.getEndDateMonth();
            day = task.getEndDateDay();
        }
        showDate(year, month, day);
    }

    private void showDate(int year, int month, int day) {
        dueDate.setText(new StringBuilder().append(day).append(".").append(month).append(".").append(year));
    }

    private void setUpSpinnerSource() {
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TaskPriority.values());
        priorityEntry.setAdapter(adapter);
        priorityEntry.setSelection(0);

        realm.beginTransaction();
        List<String> spinnerCategories = new ArrayList<>();
        categories = realm.where(Category.class).findAll();
        realm.commitTransaction();
        for (int i = 0; i < categories.size(); i++) {
            spinnerCategories.add(categories.get(i).getCategoryName());
        }
        ArrayAdapter categoryArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spinnerCategories);
        categoryEntry.setAdapter(categoryArrayAdapter);
        categoryEntry.setSelection(0);
    }

    @OnClick(R.id.button_newtask_setDate)
    public void setDate(View view) {
        showDatePickerDialog(view);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        showDate(year, month + 1, day);
    }

    public void showDatePickerDialog(View v) {
        android.support.v4.app.FragmentManager newFragment = getSupportFragmentManager();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnClickListener(this);
        datePickerFragment.show(newFragment, "datePicker");
    }


    @OnClick(R.id.imagebutton_newtask_saveTask)
    public void saveTask() {
        String title = titleEntry.getText().toString();
        String description = descriptionEntry.getText().toString();
        TaskPriority priority = (TaskPriority) priorityEntry.getSelectedItem();
        String category = categoryEntry.getSelectedItem().toString();
        SharedPreferences.Editor editor = pref.edit();
        Task newTask = realm.where(Task.class).equalTo("ID", task.getID()).findFirst();
        realm.beginTransaction();

        newTask.setCategory(category);
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setTaskPriorityEnum(priority);
        newTask.setEndDateYear(year);
        newTask.setEndDateMonth(month + 1);
        newTask.setEndDateDay(day);

        editor.putInt(TasksActivity.SHARED_PREFS_PRIORITY, priority.ordinal());
        editor.apply();
        realm.commitTransaction();
        finish();
    }
}
