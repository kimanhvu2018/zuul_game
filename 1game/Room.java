import java.util.*;
import java.util.Set;
import java.util.HashMap;
import java.util.Map.Entry;
/**
 * Class Room - a location in the body.
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other location via exits.  For each existing exit, the location 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes and Kim-Anh Vu 
 * @version 2017.12.08
 */

public class Room 
{
    private String description;
    private String name;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private HashMap<String, Item> roomInventory; //stores items in inventory of player
    private Item item;
    private boolean destroyed;
    /**
     * Creates a 'room' described by "description". 
     * @param description The room's description.
     * @param name The room's name
     */
    public Room(String name, String description, boolean destroyed) 
    {
        this.description = description;
        this.name = name;
        exits = new HashMap<>();
        roomInventory = new HashMap<>();
        destroyed = false;
    }
    
    /**
     * sets destroyed to true or false - symbolising whether or not the 
     * cell has been attacked.
     */
    public void setDestroyed(boolean destroyed) {
      this.destroyed = destroyed; 
    }
    
    /**
     * returns name of Room
     */
    public String getName () {
        return name;
    }
    
    /**
     * returns the value of the variable destroyed of the particular room
     */
    public boolean getDestroyed() {
        return destroyed;
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * This method adds item to the room
     * @param name The name of the item
     * @param item  The object of the item class 
     */
    public void setItem(String name, Item item) 
    {
        roomInventory.put(name, item);
    }
    
    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit + " ";
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Return item in roomInventory that corresponds with the string name 
     * inputted.
     */
    public Item getItem(String name)
    { 
        return roomInventory.get(name);
    }
    
    /** 
     * Returns exits hashmap
     */
    public HashMap<String, Room> getExits()
    {
        return exits;
    }
    
    /**
     * Prints all items in room that user is currently in
     */
    public String printRoomInventory() {
       String print = "";
       System.out.println("Around your location, there is/are: ");
       
       if (roomInventory.isEmpty()) {
          System.out.println("nothing");
       }
       else 
       {
        //Looping through roomInventory and printing out all items in the room
        // while loop is used as number of iterations are unknown
           Iterator<Entry<String, Item>> it = roomInventory.entrySet().iterator();
           while (it.hasNext())
           {
               Item item = (Item)it.next().getValue();
               System.out.println(item.getName());
            }
        }
       return print;
    }
    
    /**
     * Removes item from the room when user picks up item.
     */
    public void removeItem(String name)
    {
        roomInventory.remove(name);
    }
}

