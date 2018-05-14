package osc.ada.tomislavgazica.taskie.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

import io.realm.Realm;
import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.model.Category;

public class AddCategoryFragment extends android.support.v4.app.DialogFragment {

    Realm realm = Realm.getDefaultInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_category_fragment, container, false);

        Button save = v.findViewById(R.id.button_addCategory_save);
        final EditText categoryName = v.findViewById(R.id.edittext_addCategory_name);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                Category category = realm.createObject(Category.class, UUID.randomUUID().toString());
                category.setCategoryName(categoryName.getText().toString());
                realm.commitTransaction();
                dismiss();
            }
        });

        return v;
    }

}

