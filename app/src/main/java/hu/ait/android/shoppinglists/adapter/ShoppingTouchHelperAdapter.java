package hu.ait.android.shoppinglists.adapter;

public interface ShoppingTouchHelperAdapter {

    void onItemDismiss(int position);

    void onItemMove(int fromPosition, int toPosition);


}