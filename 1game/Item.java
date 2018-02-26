/**
 * This class creates item objects and returns attributes of items 
 * including weight, name, description and whether or it can be picked
 * up.
 *
 * @ Kim-Anh Vu
 * @ 2017.12.08
 */
public class Item
{
    // instance variables 
    private int weight;
    private String description;
    private boolean canPickUp;
    private String name;
        
    /**
     * Constructor for objects of class Item
     */
    public Item(String name, int weight, boolean canPickUp)
    {
        this.name = name;
        this.weight = weight;
        this.canPickUp = canPickUp;
    }
    
    /**
     * returns weight of item object
     */
    public int getWeight()
    {
        return weight;
    }
    
    /**
     * returns name of item object
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * returns whether or not item can be picked up or not
     */
    public boolean getCanPickUp()
    {
        return this.canPickUp;
    }
    
}

