package osc.ada.tomislavgazica.taskie.util;

import osc.ada.tomislavgazica.taskie.model.Task;

public interface EditTaskClickListener {
    void onEditClick(Task task);
    void onDeleteClick(Task task);
}
