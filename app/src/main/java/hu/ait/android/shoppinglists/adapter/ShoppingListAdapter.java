package hu.ait.android.shoppinglists.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.ait.android.shoppinglists.MainActivity;
import hu.ait.android.shoppinglists.R;
import hu.ait.android.shoppinglists.data.ShoppingItem;
import hu.ait.android.shoppinglists.data.ShoppingList;

public class ShoppingListAdapter
        extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> implements ShoppingTouchHelperAdapter{

    private Context context;
    private List<ShoppingList> lists = new ArrayList<ShoppingList>();

    public ShoppingListAdapter(Context context) {
        this.context = context;

        // Loads all ShoppingLists in the database into the ShoppingListAdapter
        this.lists = ShoppingList.listAll(ShoppingList.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Create our view holder object
        //Create view holder and inflate first item
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_row, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.listName.setText(lists.get(position).getListName());
        int numberOfRemainingItems = lists.get(position).numberOfRemainingItems();

        if (numberOfRemainingItems == 1) {
            holder.remainingItems.setText(String.valueOf(numberOfRemainingItems) +
                    context.getString(R.string.textRemainingItem));
        } else {
            holder.remainingItems.setText(String.valueOf(numberOfRemainingItems) +
                    context.getString(R.string.textRemainingItems));
        }

        holder.btnDeleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showDeleteAlert(context.getString(R.string.titleDeleteList),
                        holder.listName.getText().toString(),
                        holder.getAdapterPosition());
            }
        });
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).openList(lists.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    /**
     * Adds a new ShoppingList to the ShoppingListAdapter and the database
     * @param list new ShoppingList
     */
    public void addList(ShoppingList list) {

        lists.add(0, list);
        list.save();
        notifyItemInserted(0);

    }

    /**
     * Removes ShoppingList at the given position
     * @param position position of the list in ShoppingListAdapter to remove
     */
    public void removeList(int position) {
        //Removes from table
        lists.get(position).delete();

        //Removes from list
        lists.remove(position);

        notifyItemRemoved(position);
    }

    @Override
    public void onItemDismiss(int position) {
        removeList(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(lists, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(lists, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * Clears all items in the ShoppingListAdapter and database as well as updates the UI
     */
    public void clearList() {
        ShoppingList.deleteAll(ShoppingList.class);
        ShoppingItem.deleteAll(ShoppingItem.class);
        lists.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView listName;
        private TextView remainingItems;
        private LinearLayout listButtonLayout;
        private Button btnView;
        private Button btnDeleteList;

        //Represents one line in the list of todos
        public ViewHolder(View itemView) {
            super(itemView);

            listName = (TextView) itemView.findViewById(R.id.listName);
            remainingItems = (TextView) itemView.findViewById(R.id.remainingItems);
            listButtonLayout = (LinearLayout) itemView.findViewById(R.id.listButtonLayout);
            btnView = (Button) listButtonLayout.findViewById(R.id.btnEditList);
            btnDeleteList = (Button) listButtonLayout.findViewById(R.id.btnDeleteList);


        }

    }

    /**
     * Updates the UI of the ShoppingList at the given position
     * @param position position of the ShoppingList in the ShoppingListAdapter
     */
    public void updateListAtPosition(int position) {
        notifyItemChanged(position);
    }
}
