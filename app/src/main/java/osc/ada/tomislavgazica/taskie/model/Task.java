package osc.ada.tomislavgazica.taskie.model;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Task extends RealmObject implements Serializable {

    public static int sID = 0;

    @Required
    @PrimaryKey
    private String ID;
    private String priority;
    private String title;
    private String description;
    private int endDateDay;
    private int endDateMonth;
    private int endDateYear;
    private String category;
    private boolean isFinished = false;

    public Task(){}

    public Task(String priority, String title, String description, int endDateDay, int endDateMonth, int endDateYear) {
        ID = UUID.randomUUID().toString();
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.endDateDay = endDateDay;
        this.endDateMonth = endDateMonth;
        this.endDateYear = endDateYear;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEndDateDay() {
        return endDateDay;
    }

    public void setEndDateDay(int endDateDay) {
        this.endDateDay = endDateDay;
    }

    public int getEndDateMonth() {
        return endDateMonth;
    }

    public void setEndDateMonth(int endDateMonth) {
        this.endDateMonth = endDateMonth;
    }

    public int getEndDateYear() {
        return endDateYear;
    }

    public void setEndDateYear(int endDateYear) {
        this.endDateYear = endDateYear;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void setTaskPriorityEnum(TaskPriority taskPriority) {
        this.priority = taskPriority.toString();
    }

    public TaskPriority getTaskPriorityEnum() {
        return TaskPriority.valueOf(priority);
    }

    public String convertTaskPriorityEnumToString(TaskPriority taskPriority) {
        return String.valueOf(taskPriority.toString());
    }
}
