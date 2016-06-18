package hu.ait.android.shoppinglists;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import hu.ait.android.shoppinglists.data.ShoppingList;

public class NewListActivity extends AppCompatActivity {

    public static final String NEW_LIST = "NEW_LIST";

    private EditText listName;
    private Button btnCreate;
    private LinearLayout newListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        listName = (EditText) findViewById(R.id.etListName);
        btnCreate = (Button) findViewById(R.id.btnCreateList);
        newListLayout = (LinearLayout) findViewById(R.id.newListLayout);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listName.getText().toString().trim().length() == 0) {
                    // Shows an alert that the list needs a name
                    Snackbar.make(newListLayout, getString(R.string.warningLabelName), Snackbar.LENGTH_LONG).show();
                } else if(nameDuplicate(listName.getText().toString())) {
                    // Shows an alert if that list name has already been used
                    // Items that are in a list will use the list name as an id. Because of this,
                    // the list names must be unique so items are not put in two lists unintentionally
                    Snackbar.make(newListLayout, R.string.warningDuplicateName, Snackbar.LENGTH_LONG).show();
                } else {
                    // Creates a new list
                    ShoppingList list = new ShoppingList(listName.getText().toString());
                    Intent intentResult = new Intent();
                    intentResult.putExtra(NEW_LIST, list);

                    setResult(RESULT_OK, intentResult);

                    finish();
                }
            }
        });
    }

    /**
     * Determines if the given name is the name of any other ShoppingList in the database
     *
     * @param name String name of the new list
     * @return true if the name is a duplicate, false otherwise
     */
    public boolean nameDuplicate(String name) {
        for(ShoppingList list : ShoppingList.listAll(ShoppingList.class)) {
            if (list.getListName().equals(name)) {
                return true;
            }
        }

        return false;
    }
}
