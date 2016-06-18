package hu.ait.android.shoppinglists;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import hu.ait.android.shoppinglists.data.ShoppingItem;
import hu.ait.android.shoppinglists.data.ShoppingList;

public class ItemActivity extends AppCompatActivity {

    public static final String NEW_ITEM = "NEW_ITEM";
    public static final String EDITED_ITEM = "EDITED_ITEM";
    public static final CharSequence[] categoryList = new CharSequence[] {ShoppingItem.FOOD_NAME,
            ShoppingItem.CLOTHING_NAME, ShoppingItem.TECH_NAME,
            ShoppingItem.TOYS_NAME, ShoppingItem.BEAUTY_NAME};


    private LinearLayout newItemLayout;
    private EditText etItemName;
    private EditText etItemPrice;
    private TextView tvItemCategory;
    private Button btnChooseCategory;
    private TextView etItemDescription;
    private LinearLayout isPurchasedLayout;
    private CheckBox cbIsPurchased;
    private Button btnAddItem;

    private short category;
    private int position;
    private ShoppingList shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        newItemLayout = (LinearLayout) findViewById(R.id.newItemLayout);
        etItemName = (EditText) findViewById(R.id.etItemName);
        etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        tvItemCategory = (TextView) findViewById(R.id.tvItemCategory);
        btnChooseCategory = (Button) findViewById(R.id.btnChooseCategory);
        etItemDescription = (EditText) findViewById(R.id.etItemDescription);
        isPurchasedLayout = (LinearLayout) findViewById(R.id.purchasedLayout);
        cbIsPurchased = (CheckBox) isPurchasedLayout.findViewById(R.id.cbIsPurchased);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);

        btnChooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etItemName.getText().toString().trim().length() == 0) {
                    // If there is no name for the item, give user an alert
                    showSnackbarWarning(getString(R.string.txtName));
                } else if (etItemPrice.getText().toString().trim().length() == 0) {
                    // If there is no price for the item, give user an alert
                    showSnackbarWarning(getString(R.string.txtPrice));
                } else {
                    ShoppingItem item = new ShoppingItem(etItemName.getText().toString(),
                            category,
                            Double.parseDouble(etItemPrice.getText().toString()),
                            etItemDescription.getText().toString(),
                            cbIsPurchased.isChecked());
                    Intent intentResult = new Intent();

                    // If there are no extras, you can assume this item is new and will be added
                    // to the list. Otherwise, this item is being replaced in the list and the
                    // item position is required.
                    if (getIntent().getExtras() == null) {
                        intentResult.putExtra(NEW_ITEM, item);
                    } else {
                        intentResult.putExtra(EDITED_ITEM, item);
                        intentResult.putExtra(ListActivity.ITEM_POSITION, position);

                    }

                    setResult(RESULT_OK, intentResult);
                    finish();
                }
            }
        });

        // If the intent has no extras, you are creating a new item. Otherwise an older item is
        // being edited.
        if(getIntent().getExtras()==null) {
            // Set activity title
            setTitle(R.string.labelCreateItem);
            // Default category is food
            category = ShoppingItem.FOOD;
        } else {
            // Set activity title
            setTitle(R.string.labelEditItem);

            // Obtain extras
            position = (int) getIntent().getIntExtra(ListActivity.ITEM_POSITION, 0);
            ShoppingItem item = (ShoppingItem) getIntent().getSerializableExtra(ListActivity.SHOPPING_ITEM);

            // Set values in the activity based on the old item
            etItemName.setText(item.getName());
            etItemPrice.setText(String.valueOf(item.getPrice()));
            changeCategory(item.getCategory());
            etItemDescription.setText(item.getDescription());
            cbIsPurchased.setChecked(item.isPurchased());
        }

        setCategoryText();
    }

    /**
     * Display a Snackbar warning to the user that a field in the activity needs to have a value
     *
     * @param field Activity field
     */
    public void showSnackbarWarning(String field) {
        Snackbar.make(newItemLayout, String.format(getString(R.string.messageItemWarning), field), Snackbar.LENGTH_LONG).show();
    }

    /**
     * For the current item category, set the text on the screen to indicate the category to the user
     */
    public void setCategoryText() {
        switch(category) {
            case ShoppingItem.FOOD:
                tvItemCategory.setText(R.string.catFood);
                break;
            case ShoppingItem.CLOTHING:
                tvItemCategory.setText(R.string.catClothing);
                break;
            case ShoppingItem.TECH:
                tvItemCategory.setText(R.string.catTech);
                break;
            case ShoppingItem.TOYS:
                tvItemCategory.setText(R.string.catToys);
                break;
            case ShoppingItem.BEAUTY:
                tvItemCategory.setText(R.string.catBeauty);
                break;
        }
    }

    /**
     * Change the current category and update the text on the screen
     *
     * @param categoryNum Category to update the current category to
     */
    public void changeCategory(short categoryNum) {
        category = categoryNum;
        setCategoryText();
    }

    /**
     * Displays the dialog to the user so the user can update the current item category
     */
    public void showCategoryDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Choose Item Category");
        alertDialogBuilder.setItems(categoryList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Change the current category based on the user pressing the button
                switch (which) {
                    case 0:
                        changeCategory(ShoppingItem.FOOD);
                        break;
                    case 1:
                        changeCategory(ShoppingItem.CLOTHING);
                        break;
                    case 2:
                        changeCategory(ShoppingItem.TECH);
                        break;
                    case 3:
                        changeCategory(ShoppingItem.TOYS);
                        break;
                    case 4:
                        changeCategory(ShoppingItem.BEAUTY);
                        break;

                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
