import java.util.ArrayList;
/**
 *  This class is the main class of the "Cell Attack" application. 
 *  "Cell Attack" is a text based adventure game.  
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes and Kim-Anh Vu 
 * @version 2017.12.08
 */

public class Game 
{
    private Parser parser;
    private Player player;
    private Room currentRoom;
    private Item item;
    boolean wantToQuit;
    private int weightCarried; 
    ArrayList<Room> rooms = new ArrayList<Room>();
    Room lIntestine, sIntestine1, sIntestine2, stomach1, stomach2, stomach3, cell1, cell2, cell3, cell4, cell5, teleport;
    Item nutrientsX, nutrientsY, water, pillA, pillB, glucoseA, glucoseB, fat;
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        wantToQuit = false;
        player = new Player(lIntestine, this); //'this' is used to signify this game 
        weightCarried = player.getWeightCarried();
    }

    /**
     * Create all the rooms, links their exits together and states whether or not they have been destroyed.
     * Also, creates all items and assigns whether or not can be picked up and weight to corresponding item. 
     */
    private void createRooms()
    {
        // create the rooms
        lIntestine = new Room("lIntestine", "You're in the large intestine. To attack, use code: lIntestine.", false);
        sIntestine1 = new Room("sIntestine1", "You're at the entrance of the small intestine. To attack, use code: sIntestine1.", false);
        sIntestine2 = new Room("sIntestine2", "You're at the exit of the small intestine. To attack, use code: sIntestine2.", false);
        stomach1 = new Room("stomach1", "", false);
        stomach2 = new Room("stomach2", "", false);
        stomach3 = new Room("stomach3", "", false);
        cell1 = new Room("cell1", "You're in cell 1. To attack, use code: cell1.", false);
        cell2 = new Room("cell2", "You're in cell2. To attack, use code: cell2.", false);
        cell3 = new Room("cell3", "You're in cell3. To attack, use code: cell3.", false);
        cell4 = new Room("cell4", "You're in cell4. To attack, use code: cell4.", false);
        cell5 = new Room("cell5", "You're in cell5. To attack, use code: cell5.", false);
        teleport = new Room("teleport", "You're in a special place of the body and you're currently in the process of teleporting.", false);
        
        //add rooms to arrayList called rooms.
        rooms.add(lIntestine);
        rooms.add(sIntestine1);
        rooms.add(sIntestine2);
        rooms.add(stomach1);
        rooms.add(stomach2);
        rooms.add(stomach3);
        rooms.add(cell1);
        rooms.add(cell2);
        rooms.add(cell3);
        rooms.add(cell4);
        rooms.add(cell5);
        rooms.add(teleport);
        
        //create the items
        nutrientsX = new Item("nutrientsX", 3, true);
        nutrientsY = new Item("nutrientsY",3, true);
        water = new Item("water", 5, true);
        pillA = new Item("pillA", 2, true);
        pillB = new Item("pillB", 2, true);
        glucoseA = new Item("glucoseA",1, true);
        glucoseB = new Item("glucoseB",1,true);
        fat = new Item("fat", 10, false);
        
        // initialise room exits
        lIntestine.setExit("east", cell1);
        lIntestine.setExit("south", stomach1);
        lIntestine.setExit("west", cell2);
        lIntestine.setExit("north", sIntestine1);

        sIntestine1.setExit("east", cell3);
        sIntestine1.setExit("south", lIntestine);
        sIntestine1.setExit("west", cell4);
        sIntestine1.setExit("north", sIntestine2);

        sIntestine2.setExit("east", cell5);
        sIntestine2.setExit("south", sIntestine1);
        
        cell1.setExit("west", lIntestine);
        cell1.setExit("north", cell3);
        
        cell2.setExit("south", stomach2);
        cell2.setExit("west", stomach3);
        cell2.setExit("east", lIntestine);
        cell2.setExit("north", cell4);

        cell3.setExit("south", cell1);
        cell3.setExit("west", sIntestine1);
        cell3.setExit("north", cell5);
        
        cell4.setExit("south", cell2);
        cell4.setExit("east", sIntestine1);
        cell4.setExit("west", teleport);
        
        cell5.setExit("south", cell3);
        cell5.setExit("west", sIntestine2);

        currentRoom = lIntestine;  // start game in large intestine
        
        //initialise item names
        sIntestine1.setItem("nutrientsX", nutrientsX);
        sIntestine1.setItem("water", water);
        sIntestine2.setItem("nutrientsY", nutrientsY);
        cell1.setItem("glucoseA", glucoseA);
        cell5.setItem("glucoseB", glucoseB);
        cell4.setItem("pillA", pillA);
        cell2.setItem("pillB", pillB);
        lIntestine.setItem("fat", fat);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to a section of your body!");
        System.out.println("You are a virus and you need to invade and conquer the cells. Beware of the stomach!");
        System.out.println("Collect the necessary items in order to gain the ability to attack ");
        System.out.println("To win, successfully attack all cells. Good luck!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println(currentRoom.printRoomInventory()); //prints items that are currently in the room the user is in
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        String commandWord = command.getCommandWord();
        String secondWord = command.getSecondWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            player.goRoom(command);
        }
        else if (commandWord.equals("pick")) {
            player.pickUpItems(command);
        }
        else if (commandWord.equals("back")) {
            player.goBack();    //go to previous room that user was in
        }
        else if (commandWord.equals("inventory")){
            player.getInventory(); //shows list of what user has picked up
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("drop")){
            player.dropItem(command);
        }
        else if (commandWord.equals("attack")){
            player.toAttack(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("So you don't know what to do? Pretty useless aren't you?");
        System.out.println("Your command words are:");
        System.out.println("To pick items up, type 'pick up' then your item");
        parser.showCommands();
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}


