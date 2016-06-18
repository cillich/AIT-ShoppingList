package hu.ait.android.shoppinglists.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import hu.ait.android.shoppinglists.adapter.ShoppingTouchHelperAdapter;

public class ShoppingTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ShoppingTouchHelperAdapter mAdapter;

    public ShoppingTouchHelperCallback(ShoppingTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    //How we can tell this handler can handle these different gestures
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //Only certain movements are allowed for drag/swipe
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //Get the index of the todo row
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
