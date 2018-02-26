import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Random;
import java.util.Iterator;
import java.util.Map.Entry;
/**
 * Class Player - the virus which is the user
 *
 * This class creates the player object and returns attributes of the player 
 * which includes the total weight the player is carrying and the limit that the 
 * player can carry. You can add to and get items from the player's inventory.
 * 
 * Also, you access methods that allow the player to commit actions in this
 * game, for example to drop an item or to go to a room.
 * 
 * @author  Michael Kölling and David J. Barnes and Kim-Anh Vu 
 * @version 2017.12.08
 */
public class Player
{
    // instance variables 
    private int weightCarried;
    private int weightLimit;
    private HashMap<String, Item> inventory;
    private Stack<Room> lastRoom;
    public Room currentRoom;
    private Game game;
 
    /**
     * Constructor for objects of class Player
     */
    public Player(Room currentRoom, Game game) //sets room and which game player is in
    {
        weightCarried = 0;
        weightLimit = 12;
        inventory = new HashMap<>();
        lastRoom = new Stack<Room>();
        this.game = game;
        this.currentRoom = currentRoom;
    }
    
    /**
     * returns the weight carried by the user(virus).
     */
    public int getWeightCarried()
    {
        return weightCarried;
    }
    
    /**
     * returns the weight limit that can be carried by the user(virus).
     */
    public int getWeightLimit()
    {
        return weightLimit;
    }
    
    /**
     * Adds to their user's inventory whenever they pick up item.
     */
    public void addItem(String name, Item item)
    {
        inventory.put(name, item);
    }
    
    /**
     * Returns name of item in the inventory.
     */
    public Item getItem(String string)
    { 
        return inventory.get(string); 
    }
    
    /**
     * Given a command that consists of 'pick' and 'up' in order to pick up items,
     * it is processed.
     * Result is dependent on third word of command.
     */
    public void pickUpItems(Command command) 
    // pick up different items from different rooms
    {
        String commandWord = command.getCommandWord();
        String secondWord = command.getSecondWord();
        String thirdWord = command.getThirdWord();
        Item item = currentRoom.getItem(thirdWord);
        if(secondWord.equals("up") && !command.hasThirdWord()) {
            //if there isn't a third word, we don't know what the virus is picking up
            System.out.println("What are we picking up?");
        } 
        
        else if(secondWord.equals("up")) {
            if(item == null) {
                System.out.println("item does not exist in this room");
            } 
            else if (!item.getCanPickUp()) {
                System.out.println("You cannot pick this item up");
            }
            
            else if(weightCarried < getWeightLimit()){
                if (item.getWeight() + weightCarried <= getWeightLimit()) {
                  weightCarried += item.getWeight();
                  System.out.println(thirdWord + " has been picked up.");
                  // if you pick up pillA or pillB, you die
                    if (item.getName().equals("pillA") || item.getName().equals("pillB")){
                      System.out.println("You've just killed yourself. Game Over.");
                      try{
                          Thread.sleep(3000); // suspends execution of thread for 3000ms
                        }
                        catch(InterruptedException e) {
                        }
                        System.exit(0); //ends program
                    }
                  else{
                  addItem(thirdWord, item);
                  currentRoom.removeItem(thirdWord);
                  currentRoom.printRoomInventory();
                }
              }
            }
            else{
               System.out.println("you're too weak to carry this many items. Unable to pick " + item.getName() + " up");
                }
        }
    }
    
    /** 
     * Try to go in to one direction. If there is an exit, enter the new
     * body part, otherwise print an error message. If land in the
     * stomach, then the game is over.
     */
    public void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no entrance!");
        }
        else if (nextRoom.getDestroyed() == true) {
            System.out.println("You've destroyed this location. There is nowhere to go");
        }
        else {
            lastRoom.push(currentRoom); //add currentRoom to lastRoom stack
            currentRoom = nextRoom;     
            if (currentRoom.getName().equals("stomach1") || currentRoom.getName().equals("stomach2") || currentRoom.getName().equals("stomach3")){
                System.out.println("You land in the stomach and the hydrochloric acid kills you. Game over.");
                try{
                    Thread.sleep(2000);
                }
                catch(InterruptedException e) {
                }
                System.exit(0);
            } 
            else if(currentRoom.getName().equals ("teleport")){
                System.out.println(currentRoom.getShortDescription());
                toTeleport();
            }
            else {
                System.out.println(currentRoom.getLongDescription());
                System.out.println(currentRoom.printRoomInventory());
            }
            
        }
    }
    
    /** 
     * Allows user to travel to previous sections of the body that 
     * they've been in.
     */
    public void goBack(){
        if(lastRoom.empty()) {  //if stack is empty, then cannot go back
            System.out.println("You cannot go back any further");
        }
        else {
            toWin();
            currentRoom = lastRoom.peek(); //returns object on top of lastRoom stack
            lastRoom.pop(); //removes object at top of stack
            System.out.println(currentRoom.getLongDescription());
            currentRoom.printRoomInventory();
        }
    }
    
    /**
     * When the user lands in body part called 'teleport', it is randomly
     * transported to another place in the body.
     */
    private void toTeleport()
    {
        int index = new Random().nextInt(game.rooms.size()-1); //randomly generates index number from 0 to (rooms arraylist size - 1)
        currentRoom = game.rooms.get(index);
        if (currentRoom.getDestroyed() == true) { // if room transported to is destroyed, teleport again
            toTeleport();
        }
        else{
        System.out.println(currentRoom.getLongDescription());
        System.out.println(currentRoom.printRoomInventory());
       }
    }
    
    /**
     * If user inputs 'inventory', list of all the items that have been
     * picked up is printed out.
     */
    public void getInventory() {
      String print = "";
       if (inventory.isEmpty()) {
          System.out.println(print);
       }
       else 
       {
        // Looping through inventory and printing out all items in the player's inventory
        // while loop is used as number of iterations are unknown
        System.out.println("You currently are carrying: ");
        Iterator<Entry<String, Item>> it = inventory.entrySet().iterator();
           while (it.hasNext())
           {
               Item item = (Item)it.next().getValue();
               System.out.println(item.getName());
            }
      }
    }
    
    /**
     * If user inputs 'drop', item is removed from inventory and is 
     * placed in current room. If second word is not recognised, error
     * statement is printed.
     */
    public void dropItem(Command command) {
        String commandWord = command.getCommandWord();
        String secondWord = command.getSecondWord();
        Item item = getItem(secondWord);
        if(commandWord.equals("drop") && !command.hasSecondWord()) {
            //if there isn't a second word, we don't know what the virus is dropping
            System.out.println("What are we dropping?");
       } 
        
        if(item != null) {
            weightCarried = weightCarried - item.getWeight(); 
            currentRoom.setItem(secondWord, item); //item is now placed in current room
            System.out.println(secondWord + " has been dropped.");
            inventory.remove(secondWord); 
            currentRoom.printRoomInventory();
          }
            
         else{
            System.out.println("the item is not in your inventory, so cannot be dropped.");
          }
    }
    
     /**
      * If user picks up correct items, then are able to attack cells.
      * Otherwise, error message is printed.
      */
     public void toAttack(Command command)
     {
       String secondWord = command.getSecondWord();
       HashMap<String, Item> items = inventory; 
       //if necessary items are in inventory, then user can attack
       if (secondWord.equals(currentRoom.getName())) {
          if (currentRoom.getName().equals("cell1") || currentRoom.getName().equals("cell2") || currentRoom.getName().equals("cell3") || currentRoom.getName().equals("cell4") || currentRoom.getName().equals("cell5")){
                   if (items.get("nutrientsX") != null){
                        if (items.get("nutrientsY") != null) {
                            if(items.get("water") != null) { 
                                if(items.get("glucoseA") != null) { 
                                   System.out.println("You have attacked " + currentRoom.getName() + " successfully.");
                                   currentRoom.setDestroyed(true);
                                   goBack();
                                 }
                                else if (items.get("glucoseB") != null) {
                                    System.out.println("Beta-glucose cannot be used to make energy, therefore you do not have sufficient energy to attack."); 
                                }
                                else {
                        System.out.println("You do not have the necessary items in order to attack cells.");
                        System.out.println("You need nutrientsX, nutrientsY, glucoseA and water. Good luck.");
                       }   
                             } 
                             else {
                        System.out.println("You do not have the necessary items in order to attack cells.");
                        System.out.println("You need nutrientsX, nutrientsY, glucoseA and water. Good luck.");
                       }   
                    }
                    else {
                        System.out.println("You do not have the necessary items in order to attack cells.");
                        System.out.println("You need nutrientsX, nutrientsY, glucoseA and water. Good luck.");
                    }
                }
                else {
                        System.out.println("You do not have the necessary items in order to attack cells.");
                        System.out.println("You need nutrientsX, nutrientsY, glucoseA and water. Good luck.");
                    }
                }
          else {
              System.out.println("You can only attack cells");
          }
                 }
          else {
           System.out.println("Location unidentified. REMINDER: You can only attack the location only if you're present in it");
        }
        }
    
       /**
         * After every time goBack method is initialised, program checks
         * if all cells have been destroyed. If they are, user wins.
         */
    public void toWin()
    {
      // if all cells have a value of true for the instance destroyed, then 
      //user wins and game ends.
         if (game.rooms.get(6).getDestroyed() == true) {
           if (game.rooms.get(7).getDestroyed() == true) {
               if (game.rooms.get(8).getDestroyed() == true) {
                  if (game.rooms.get(9).getDestroyed() == true) {
                       if (game.rooms.get(10).getDestroyed() == true) {
                           System.out.println("You've successfully attacked all cells. CONGRATULATIONS!");
                           try{
                         Thread.sleep(2000);
                        }
                        catch(InterruptedException e) {
                        }
                           System.exit(0);
                        }
                    }
                }
            }
        }
      }
    }

