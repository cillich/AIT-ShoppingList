package hu.ait.android.shoppinglists;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import hu.ait.android.shoppinglists.adapter.ShoppingListAdapter;
import hu.ait.android.shoppinglists.adapter.ShoppingTouchHelperCallback;
import hu.ait.android.shoppinglists.data.ShoppingList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NEW_LIST = 101;
    public static final String SHOPPING_LIST = "SHOPPING_LIST";
    public static final int REQUEST_CODE_VIEW_LIST = 102;
    public static final String LIST_POSITION = "LIST_POSITION";

    final ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Sets up RecyclerView
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(shoppingListAdapter);

        // Sets up TouchHelper
        ItemTouchHelper.Callback callback = new ShoppingTouchHelperCallback(shoppingListAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_newList:
                startActivityForResult(new Intent(
                        MainActivity.this,
                        NewListActivity.class), REQUEST_CODE_NEW_LIST);
                break;
            case R.id.action_deleteAll:
                showDeleteAllAlert();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_NEW_LIST) {
            if (resultCode == RESULT_OK) {
                // Adds the new list to the adapter
                ShoppingList list = (ShoppingList) data.getSerializableExtra(NewListActivity.NEW_LIST);
                shoppingListAdapter.addList(list);
            }
        } else if(requestCode == REQUEST_CODE_VIEW_LIST) {
            if (resultCode == RESULT_OK) {
                // Updates the edited list at the position from the extra
                int position = (int) data.getIntExtra(LIST_POSITION, 0);
                shoppingListAdapter.updateListAtPosition(position);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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
                shoppingListAdapter.onItemDismiss(position);
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
     * SHows this alert dialog before deleting all lists and associated items
     */
    public void showDeleteAllAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.labelDeleteAll);
        alertDialogBuilder.setMessage(R.string.messageDeleteAll);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.labelDelete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shoppingListAdapter.clearList();
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
     * Opens the ListActivity with the items associated with the given ShoppingList
     *
     * @param list ShoppingList
     * @param position position of the ShoppingList in the adapter
     */
    public void openList(ShoppingList list, int position) {
        Intent intentViewList = new Intent(MainActivity.this, ListActivity.class);
        intentViewList.putExtra(LIST_POSITION, position);
        intentViewList.putExtra(SHOPPING_LIST, list);
        startActivityForResult(intentViewList, REQUEST_CODE_VIEW_LIST);

    }
}
