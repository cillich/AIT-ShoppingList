package hu.ait.android.shoppinglists.data;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Represents a single item in a shopping list
 */
public class ShoppingItem extends SugarRecord implements Serializable {

    // ShoppingItem category values
    public static final short FOOD = 0;
    public static final short CLOTHING = 1;
    public static final short TECH = 2;
    public static final short TOYS = 3;
    public static final short BEAUTY = 4;

    // ShoppingItem category string names
    // Constants used when access to R.strings is not possible
    public static final String FOOD_NAME = "Food";
    public static final String CLOTHING_NAME = "Clothing";
    public static final String TECH_NAME = "Tech";
    public static final String TOYS_NAME = "Toys";
    public static final String BEAUTY_NAME = "Beauty";


    /**
     * Name of the shopping item
     */
    private String name = "";

    /**
     * Category of the shopping item. Can be one of:
     *  - ShoppingItem.FOOD
     *  - ShoppingItem.CLOTHING
     *  - ShoppingItem.TECH
     *  - ShoppingItem.TOYS
     *  - ShoppingItem.BEAUTY
     */
    private short category = 0;
    /**
     * Estimated price of the item
     */
    private double price = 0.0;
    /**
     * Description of the item
     */
    private String description = "";
    /**
     * Whether or not the item has been purchased
     */
    private boolean purchased = false;

    /**
     * The name of the shopping list this item is associated with.
     * The shopping list name is unique so the item is associated with only one shopping list.
     */
    private String list;

    public ShoppingItem() {

    }

    public ShoppingItem (String name, short category, double price, String description, boolean purchased) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.purchased = purchased;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getCategory() {
        return category;
    }

    public void setCategory(short category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public void setShoppingList(ShoppingList list) {
        this.list = list.getListName();
    }


}
