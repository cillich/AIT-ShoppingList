package hu.ait.android.shoppinglists.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.ait.android.shoppinglists.ListActivity;
import hu.ait.android.shoppinglists.R;
import hu.ait.android.shoppinglists.data.ShoppingItem;
import hu.ait.android.shoppinglists.data.ShoppingList;

public class ShoppingItemsAdapter
        extends RecyclerView.Adapter<ShoppingItemsAdapter.ViewHolder> implements ShoppingTouchHelperAdapter{

    private Context context;
    private List<ShoppingItem> items = new ArrayList<ShoppingItem>();

    public ShoppingItemsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Create our view holder object
        //Create view holder and inflate first item
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_item_row, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemIcon.setImageBitmap(
                ((ListActivity) context).iconChooser(items.get(position).getCategory()));
        holder.itemName.setText(items.get(position).getName());
        holder.itemPrice.setText(String.valueOf(items.get(position).getPrice()));
        holder.checkBox.setChecked(items.get(position).isPurchased());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingItem item = items.get(holder.getAdapterPosition());
                item.setPurchased(holder.checkBox.isChecked());

                item.save();
            }
        });

        holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListActivity)context).showDeleteAlert("Delete Item",
                        holder.itemName.getText().toString(),
                        holder.getAdapterPosition());
            }
        });

        holder.btnEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListActivity)context).editItem(items.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Adds a new item to the ShoppingItemsAdapter list and the database
     * @param item New item
     */
    public void addItem(ShoppingItem item) {

        items.add(0, item);
        item.save();
        notifyItemInserted(0);

    }

    /**
     * Removes an old item from the ShoppingItemsAdapter and the database
     * @param position position of the item in ShoppingItemsAdapter to be removed
     */
    public void removeItem(int position) {
        //Removes from table
        items.get(position).delete();

        //Removes from list
        items.remove(position);

        notifyItemRemoved(position);
    }

    /**
     * Replaces the preexisting ShoppingItem with the new given item in the ShoppingItemsAdapter and database
     * @param item new ShoppingItem
     * @param position position in ShoppingItemsAdapter to put the new ShoppingItem
     */
    public void replaceItemAtIndex(ShoppingItem item, int position) {
        // Delete the old item
        items.get(position).delete();

        // Save the new item in the database
        item.save();
        
        //Put the item in the ShoppingListAdapter at the given position
        items.set(position, item);

        notifyItemChanged(position);
    }

    @Override
    public void onItemDismiss(int position) {
        removeItem(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public List<ShoppingItem> getItems() {
        return items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIcon;
        LinearLayout contentLayout;
        LinearLayout textContentLayout;
        CheckBox checkBox;
        TextView itemName;
        TextView itemPrice;
        LinearLayout buttonLayout;
        Button btnEditItem;
        Button btnDeleteItem;

        public ViewHolder(View itemView) {
            super(itemView);

            itemIcon = (ImageView) itemView.findViewById(R.id.itemIcon);
            contentLayout = (LinearLayout) itemView.findViewById(R.id.contentLayout);
            textContentLayout = (LinearLayout) contentLayout.findViewById(R.id.textContentLayout);
            buttonLayout = (LinearLayout) contentLayout.findViewById(R.id.buttonLayout);
            checkBox = (CheckBox) textContentLayout.findViewById(R.id.checkbox);
            itemName = (TextView) textContentLayout.findViewById(R.id.itemName);
            itemPrice = (TextView) textContentLayout.findViewById(R.id.itemPrice);
            btnEditItem = (Button) buttonLayout.findViewById(R.id.btnEditItem);
            btnDeleteItem = (Button) buttonLayout.findViewById(R.id.btnDeleteItem);
        }

    }

    /**
     * Clears all items in the ShoppingItemsAdapter and database as well as updates the UI
     */
    public void clearItems() {
        for(int i = 0; i < items.size(); i++) {
            items.get(i).delete();
        }
        items.clear();
        notifyDataSetChanged();
    }

    /**
     * Shows items that are of the given category in the given list
     * @param list the current ShoppingList to be filtered
     * @param category category of items the user wants to see
     * @return The ShoppingItems with the given category
     */
    public void addFilter(ShoppingList list, short category) {
        List<ShoppingItem> allItems = list.getShoppingItems();
        for (ShoppingItem item : allItems) {
            if (item.getCategory() == category) {
                items.add(item);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Removes items of the given category from the given ShoppingItemsAdapter
     * @param category category to remove from the ShoppingItemsAdapter
     */
    public void removeFilter(short category) {
        List<ShoppingItem> filteredList = new ArrayList<ShoppingItem>();

        for (ShoppingItem item : items) {
            if (item.getCategory() != category) {
                filteredList.add(item);
            }
        }
        items = filteredList;
        notifyDataSetChanged();

    }
}
