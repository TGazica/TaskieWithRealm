package osc.ada.tomislavgazica.taskie.view;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.realm.Realm;
import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.model.Category;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskPriority;
import osc.ada.tomislavgazica.taskie.util.TaskClickListener;

class TaskViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textview_item_title)
    TextView title;

    @BindView(R.id.textview_item_description)
    TextView description;

    @BindView(R.id.textview_item_date)
    TextView date;

    @BindView(R.id.togglebutton_item_status)
    ToggleButton status;

    @BindView(R.id.imagebutton_item_priority)
    ImageButton priority;

    @BindView(R.id.textview_item_category)
    TextView category;

    private Task item;
    private TaskClickListener listener;

    private Realm realm = Realm.getDefaultInstance();

    public TaskViewHolder(View itemView) {
        super(itemView);
        realm.beginTransaction();
        realm.commitTransaction();
        ButterKnife.bind(this, itemView);
    }

    public void setListener(TaskClickListener listener) {
        this.listener = listener;
    }

    public void setItem(Task current) {
        this.item = realm.where(Task.class).equalTo("ID", current.getID()).findFirst();

        title.setText(item.getTitle());
        description.setText(item.getDescription());
        date.setText(new StringBuilder().append(item.getEndDateDay()).append(".").append(item.getEndDateMonth()).append(".").append(item.getEndDateYear()));

        category.setText(item.getCategory());

        int color = R.color.taskPriority_unknown;
        switch (current.getTaskPriorityEnum()) {
            case LOW:
                color = R.color.taskpriority_low;
                break;
            case MEDIUM:
                color = R.color.taskpriority_medium;
                break;
            case HIGH:
                color = R.color.taskpriority_high;
                break;
        }
        priority.setImageResource(color);
    }

    @OnClick(R.id.imagebutton_item_priority)
    public void onPriorityClick() {
        realm.beginTransaction();
        TaskPriority priority = item.getTaskPriorityEnum();
        if (priority == TaskPriority.LOW) {
            item.setPriority(TaskPriority.MEDIUM.toString());
            this.priority.setImageResource(R.color.taskpriority_medium);
        } else if (priority == TaskPriority.MEDIUM) {
            item.setPriority(TaskPriority.HIGH.toString());
            this.priority.setImageResource(R.color.taskpriority_high);
        } else if (priority == TaskPriority.HIGH) {
            item.setPriority(TaskPriority.LOW.toString());
            this.priority.setImageResource(R.color.taskpriority_low);
        }
        if (listener != null) {
            listener.onPriorityClick(item);
        }
        realm.commitTransaction();
    }

    @OnClick(R.id.togglebutton_item_status)
    public void onStatusClick() {
        realm.beginTransaction();
        item.setFinished(!item.isFinished());
        realm.commitTransaction();
        if (listener != null) {
            listener.onStatusClick(item);
        }
    }

    @OnLongClick
    public boolean onTaskLongClick() {
        if (listener != null) {
            listener.onLongClick(item);
        }
        return true;
    }
}