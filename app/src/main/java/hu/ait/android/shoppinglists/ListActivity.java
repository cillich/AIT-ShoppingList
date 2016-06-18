package hu.ait.android.shoppinglists;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import hu.ait.android.shoppinglists.adapter.ShoppingItemsAdapter;
import hu.ait.android.shoppinglists.adapter.ShoppingTouchHelperCallback;
import hu.ait.android.shoppinglists.data.ShoppingItem;
import hu.ait.android.shoppinglists.data.ShoppingList;

public class ListActivity extends AppCompatActivity {


    public static final int REQUEST_CODE_NEW_ITEM = 103;
    public static final String ITEM_POSITION = "ITEM_POSITION";
    public static final String SHOPPING_ITEM = "SHOPPING_ITEM";
    public static final int REQUEST_CODE_EDIT_ITEM = 105;

    final ShoppingItemsAdapter shoppingItemsAdapter = new ShoppingItemsAdapter(this);
    ShoppingList shoppingList;
    String shoppingListName = "";
    int listPosition = 0;

    boolean[] filters = {true, true, true, true, true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Get the extras from the intent
        shoppingList = (ShoppingList) getIntent().getSerializableExtra(MainActivity.SHOPPING_LIST);
        listPosition = (int) getIntent().getIntExtra(MainActivity.LIST_POSITION, 0);
        setupItems(shoppingList);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(shoppingListName); // Sets the activity tile to the list name

        // Sets up the RecyclerView
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_items);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(shoppingItemsAdapter);

        // Sets up the TouchHelper
        ItemTouchHelper.Callback callback = new ShoppingTouchHelperCallback(shoppingItemsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_newItem:
                // Goes to ItemActivity to create a new item
                startActivityForResult(new Intent(
                        ListActivity.this,
                        ItemActivity.class), REQUEST_CODE_NEW_ITEM);
                break;
            case R.id.action_deleteAll:
                showDeleteAllAlert();
                break;
            case R.id.action_filter:
                showFilterDialog();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_NEW_ITEM) {
            if(resultCode == RESULT_OK) {
                // Gets the new item from the intent extras, sets the item's ShoppingList and adds
                // item to the adapter
                ShoppingItem item = (ShoppingItem) data.getSerializableExtra(ItemActivity.NEW_ITEM);
                item.setShoppingList(shoppingList);
                shoppingItemsAdapter.addItem(item);
            }
        } else if (requestCode == REQUEST_CODE_EDIT_ITEM) {
            if (resultCode == RESULT_OK) {
                // Gets the edited item and position from intent extras, sets the item's ShoppingList
                // and replaces item in the adapter at the position
                ShoppingItem item = (ShoppingItem) data.getSerializableExtra(ItemActivity.EDITED_ITEM);
                item.setShoppingList(shoppingList);
                int position = (int) data.getIntExtra(ITEM_POSITION, 0);
                shoppingItemsAdapter.replaceItemAtIndex(item, position);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     *
     * @param category Category of an item
     * @return Image associated to that category
     */
    public Bitmap iconChooser(short category) {
        switch(category) {
            case ShoppingItem.FOOD:
                return BitmapFactory.decodeResource(getResources(), R.drawable.food);
            case ShoppingItem.CLOTHING:
                return BitmapFactory.decodeResource(getResources(), R.drawable.clothing);
            case ShoppingItem.TECH:
                return BitmapFactory.decodeResource(getResources(), R.drawable.tech);
            case ShoppingItem.TOYS:
                return BitmapFactory.decodeResource(getResources(), R.drawable.toys);
            case ShoppingItem.BEAUTY:
                return BitmapFactory.decodeResource(getResources(), R.drawable.beauty);
            default:
                return BitmapFactory.decodeResource(getResources(), R.drawable.bag);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        // Sends the ShoppingList and the position of the list to the MainActivity to update the UI
        ShoppingList list = new ShoppingList(shoppingListName);
        Intent intentResult = new Intent();
        intentResult.putExtra(MainActivity.LIST_POSITION, listPosition);
        setResult(RESULT_OK, intentResult);

        finish();
    }

    /**
     * Adds ShoppingItems ListActivity based on the list the user is viewing
     *
     * @param list ShoppingList the user is viewing
     */
    public void setupItems(ShoppingList list) {
        shoppingListName = list.getListName();
        // Populates the adapter's list if the ShoppingList has any items
        if (list.getShoppingItems() != null) {
            for (ShoppingItem item : list.getShoppingItems()) {
                shoppingItemsAdapter.addItem(item);
            }
        }

    }


    /**
     * Shows this alert dialog before deleting an object
     *
     * @param title The title for the alert
     * @param name The name of the item to be deleted
     * @param position Position of the item the user is about to delete within the adapter
     */
    public void showDeleteAlert(String title, String name, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(String.format(getString(R.string.messageDeleteConfirmation), name));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.labelDelete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shoppingItemsAdapter.onItemDismiss(position);
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.labelCancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Shows an alert before deleting all items in the list
     */
    public void showDeleteAllAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.labelDeleteAll);
        alertDialogBuilder.setMessage(R.string.messageDeleteAll);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.labelDelete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Deletes all items from adapter and database
                shoppingItemsAdapter.clearItems();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.labelCancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancels the alert and does not delete anything
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Opens ItemActivity to edit a pre-existing item
     *
     * @param item the ShoppingItem the user wants to edit
     * @param position the position of the ShoppingItem in the ShoppingList
     */
    public void editItem(ShoppingItem item, int position) {
        Intent intentViewList = new Intent(ListActivity.this, ItemActivity.class);
        intentViewList.putExtra(ITEM_POSITION, position);
        intentViewList.putExtra(SHOPPING_ITEM, item);
        startActivityForResult(intentViewList, REQUEST_CODE_EDIT_ITEM);
    }

    public void showFilterDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.labelShowCategories);
        alertDialogBuilder.setMultiChoiceItems(ItemActivity.categoryList, filters, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    filters[which] = true;
                    switch (which) {
                        case 0:
                            shoppingItemsAdapter.addFilter(shoppingList, ShoppingItem.FOOD);
                            break;
                        case 1:
                            shoppingItemsAdapter.addFilter(shoppingList, ShoppingItem.CLOTHING);
                            break;
                        case 2:
                            shoppingItemsAdapter.addFilter(shoppingList, ShoppingItem.TECH);
                            break;
                        case 3:
                            shoppingItemsAdapter.addFilter(shoppingList, ShoppingItem.TOYS);
                            break;
                        case 4:
                            shoppingItemsAdapter.addFilter(shoppingList, ShoppingItem.BEAUTY);
                            break;

                    }
                } else {
                    filters[which] = false;
                    switch (which) {
                        case 0:
                            shoppingItemsAdapter.removeFilter(ShoppingItem.FOOD);
                            break;
                        case 1:
                            shoppingItemsAdapter.removeFilter(ShoppingItem.CLOTHING);
                            break;
                        case 2:
                            shoppingItemsAdapter.removeFilter(ShoppingItem.TECH);
                            break;
                        case 3:
                            shoppingItemsAdapter.removeFilter(ShoppingItem.TOYS);
                            break;
                        case 4:
                            shoppingItemsAdapter.removeFilter(ShoppingItem.BEAUTY);
                            break;

                    }

                }
            }
        });
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
