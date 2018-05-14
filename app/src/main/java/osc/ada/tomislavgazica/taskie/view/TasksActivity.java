package osc.ada.tomislavgazica.taskie.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.model.Category;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskGenerator;
import osc.ada.tomislavgazica.taskie.util.AddCategoryListener;
import osc.ada.tomislavgazica.taskie.util.EditTaskClickListener;
import osc.ada.tomislavgazica.taskie.util.TaskClickListener;

public class TasksActivity extends AppCompatActivity implements TaskClickListener, EditTaskClickListener {

    private static final String SHOW_TASKS = "show_tasks";
    private static final String SHOW_TASKS_BY_PRIORITY = "show_tasks_by_priority";
    private static final String SHOW_TASKS_BY_STATUS = "show_tasks_by_status";
    public static final String SHARED_PREFS_PRIORITY = "Priority";

    private String show;
    private boolean showAll;
    private Realm realm;


    TaskAdapter taskAdapter;

    @BindView(R.id.recycler_tasks)
    RecyclerView taskRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        realm = Realm.getDefaultInstance();

        ButterKnife.bind(this);
        setupRecyclerView();
        showTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        callLastShowTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.taskie_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupRecyclerView() {
        int orientation = LinearLayoutManager.VERTICAL;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, orientation, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, orientation);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        taskAdapter = new TaskAdapter(this);

        taskRecycler.setLayoutManager(layoutManager);
        taskRecycler.setItemAnimator(itemAnimator);
        taskRecycler.addItemDecoration(decoration);
        taskRecycler.setAdapter(taskAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sortByPriority:
                showTasksByPriority();
                return true;
            case R.id.menu_showCompleted:
                if (showAll) {
                    showTasksByStatus();
                    showAll = false;
                } else {
                    showTasks();
                    showAll = true;
                }
                return true;
            case R.id.menu_addCategory:
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                AddCategoryFragment dialogFragment = new AddCategoryFragment();
                dialogFragment.show(fm, "dialog");
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void callLastShowTask() {
        switch (show) {
            case SHOW_TASKS:
                showTasks();
                break;
            case SHOW_TASKS_BY_PRIORITY:
                showTasksByPriority();
                break;
            case SHOW_TASKS_BY_STATUS:
                showTasksByStatus();
                break;
            default:
                showTasks();
        }
    }

    private void showTasks() {
        showAll = true;
        show = SHOW_TASKS;

        RealmResults<Category> categories = realm.where(Category.class).findAll();
        if (categories.isEmpty()){
            new TaskGenerator().generateCategories();
        }

        RealmResults<Task> tasks = realm.where(Task.class).findAll();
        if(tasks.isEmpty()){
            new TaskGenerator().generate(10);
        }

        taskAdapter.updateTasks(tasks);
    }

    private void showTasksByPriority() {
        show = SHOW_TASKS_BY_PRIORITY;
        RealmResults<Task> tasks = realm.where(Task.class).findAll();
        List<Task> tasksByPriority = new ArrayList<>();
        tasksByPriority.addAll(tasks);
        Collections.sort(tasksByPriority, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o2.getTaskPriorityEnum().compareTo(o1.getTaskPriorityEnum());
            }
        });
        taskAdapter.updateTasks(tasksByPriority);
    }

    private void showTasksByStatus() {
        show = SHOW_TASKS_BY_STATUS;
        RealmResults<Task> tasks = realm.where(Task.class).findAll();
        List<Task> ongoingTasks = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (!tasks.get(i).isFinished()) {
                ongoingTasks.add(tasks.get(i));
            }
        }
        taskAdapter.updateTasks(ongoingTasks);
    }


    @OnClick(R.id.fab_tasks_addNew)
    public void startNewTaskActivity() {
        Intent intent = new Intent(this, NewTaskActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStatusClick(Task task) {
        callLastShowTask();
    }

    @Override
    public void onPriorityClick(Task task) {
        callLastShowTask();
    }

    @Override
    public void onLongClick(Task task) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        MyAlertDialog dialogFragment = new MyAlertDialog();
        dialogFragment.setListener(this);
        dialogFragment.setTask(task);
        dialogFragment.show(fm, "dialog");
    }

    @Override
    public void onEditClick(Task task) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra(EditItemActivity.EDIT_TASK, task.getID());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Task task) {
        realm.beginTransaction();
        RealmResults<Task> rows = realm.where(Task.class).equalTo("ID", task.getID()).findAll();
        rows.deleteAllFromRealm();
        realm.commitTransaction();
        callLastShowTask();
    }

}
