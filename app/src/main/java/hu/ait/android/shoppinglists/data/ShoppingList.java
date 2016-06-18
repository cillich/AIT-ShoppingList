package hu.ait.android.shoppinglists.data;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single list of shopping items
 */
public class ShoppingList extends SugarRecord implements Serializable {

    /**
     * Name of the shopping list
     */
    private String listName;

    public ShoppingList() {
    }

    public ShoppingList (String listName) {
        this.listName = listName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }


    public List<ShoppingItem> getShoppingItems() {
        try {
            // Queries the database for all ShoppingItems associated with this list
            return ShoppingItem.find(ShoppingItem.class, "list = ?", this.getListName());

        } catch (NullPointerException e){
            //If there are no ShoppingItems associated with the list, returns an empty list.
            return new ArrayList<ShoppingItem>();
        }
    }


    /**
     *
     * @return Number of items in the list that have not been purchased
     */
    public int numberOfRemainingItems() {
        int numRemainingItems = 0;
        for (ShoppingItem item : getShoppingItems()) {
            if(!item.isPurchased()){
                numRemainingItems++;
            }
        }
        return numRemainingItems;
    }


}
