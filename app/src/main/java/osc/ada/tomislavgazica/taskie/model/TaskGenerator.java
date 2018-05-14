package osc.ada.tomislavgazica.taskie.model;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.realm.Realm;

public class TaskGenerator {

    private static final Random generator = new Random();
    Realm realm = Realm.getDefaultInstance();

    private static String[] titles = {
            "Osc", "Potato", "Homework", "Shopping", "Gaming", "Reading",
            "Android", "Studying", "Question", "Ideas", "Party", "Nothing",
    };

    private static String[] descriptions = {
            "Do it.", "Play it", "Drink it", "Answer it", "Shut it down",
            "Try it.", "Don't do it.", "Ignore it.",
    };

    private static int[] day = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31
    };

    private static int[] month = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
    };

    private static int[] year = {
            2018, 2019, 2020, 2021, 2022
    };

    private static String categories[] = {
            "School", "Job","Games"
    };

    public void generate(int taskCount) {

        for (int i = 0; i < taskCount; i++) {
            String title = titles[generator.nextInt(titles.length)];
            String description = descriptions[generator.nextInt(descriptions.length)];
            int setDay = day[generator.nextInt(day.length)];
            int setMonth = month[generator.nextInt(month.length)];
            int setYear = year[generator.nextInt(year.length)];
            int prioritySelector = generator.nextInt(TaskPriority.values().length);
            TaskPriority priority = TaskPriority.values()[prioritySelector];
            List<Category> dbCategories = realm.where(Category.class).findAll();
            Category category = dbCategories.get(generator.nextInt(dbCategories.size()));

            realm.beginTransaction();
            Task task = realm.createObject(Task.class, UUID.randomUUID().toString());
            task.setCategory(category.getCategoryName());
            task.setTitle(title);
            task.setDescription(description);
            task.setTaskPriorityEnum(priority);
            task.setEndDateDay(setDay);
            task.setEndDateMonth(setMonth);
            task.setEndDateYear(setYear);
            realm.commitTransaction();

        }
    }

    public void generateCategories() {
        for (String category : categories) {
            realm.beginTransaction();
            Category newCategory = realm.createObject(Category.class, UUID.randomUUID().toString());
            newCategory.setCategoryName(category);
            realm.commitTransaction();
        }
    }
}
