package subter;

import java.lang.reflect.Array;
import java.util.Scanner;

import javax.swing.JFrame;

import Misc.Misc;

public class Game {
	// instantiate the board, scanner, game cognition variables board parameters, and characters
	private Scanner scan; 
	private Board board; 
	private boolean keepPlaying = true, gameOver = false;
	int turn, maxRows, maxColumns;

	// basic character initialization variubles
	int health = 20;
	int xStart = 0;
	int yStart = 0;
	double baseChance=0.7;
	int maxDamage =5;
	int minDamage =0;
	int directionStart =2;
	// set default statistics for a defender or attacker
	double chanceToCounterVal=.3;
	int minCounterVal=0;
	int maxCounterVal=2;
	// set default statistics for the ranged character
	double rangedAttackChance=0.5;
	int range=3;
	int minRangedDamage=0;
	int maxRangedDamage=1;
	
	public static JFrame frame;
	Character c1,c2;
	
	// initialize the game
	public Game(Scanner scan, int maxRows, int maxColumns) {
		this.scan = scan;
		this.maxRows= maxRows;
		this.maxColumns = maxColumns;
	}
	
	/*****************************************************************************************************
	 * Initialization Functions
	 */
	
	private void initializeHumanCharacter(Scanner userInput) {
		String name=Misc.getUserInputReturnString("What is your name? ", userInput);
		// ask the player what character class they want to play as
		String prompt="What character would you like to play as?\n"
				+ "1 Brute who has a chance to power hit and push the enemy back one square\n"
				+ "2 Hacker who can activate traps on the board but has lower attack\n"
				+ "3 Ranged who can shoot an arrow\n"
				+ "4 Medic who when their health is low if they have a potion they will heal but has a lower attack\n";
		int choice=Misc.getUserInputInt(prompt, userInput, 1, 4);
		// assign c1 as the value of the function that returns a character.
		this.c1 = returnSelectedCharacter(choice, name);
		this.c1.setPlayerType(true); // sets the player to be a human or intractable type of player
		this.board.markBoard(c1.getLocation()[0],c1.getLocation()[1], c1.getSymbol());
		c1.botLastLocation=c1.getLocation();
		}
	
	private void initializeComputerCharacter() {
		// set the standard character 
		int health = 20;
		int xStart =this.maxColumns-1;
		int yStart= this.maxRows-1;
		double baseChance=0.6;
		int maxDamage =5;
		int minDamage =0;
		int directionStart =1;
		// initialize the computer character with a random name
		String [] randomName= {"Shift","Block","Pitch","Eternity","Ego","Blunder","Bank","Law","Gear","Beast","Ward","Block",
				"Dare","Math","Stretch","Spark","Chain","Books","Bandage"};
		this.c2 = new Brute(this.board,randomName[Misc.generateRandomInt(0, 17)],
				health,xStart,yStart,baseChance,maxDamage,minDamage,directionStart);		
		this.c2.setPlayerType(false); // sets character to be independent/non human/interaction not required
		this.board.markBoard(c2.getLocation()[0], c2.getLocation()[1],c2.getSymbol());
		c2.botLastLocation=c2.getLocation(); // sets the bot inactivity counter
		}
	
	

	private void initializeCharacters() {
		// initialize both the characters
		initializeComputerCharacter();
		initializeHumanCharacter(scan);
		}
	
	private void initializeGame() {
		// Initialize the game by setting the max rows, max columns and initializing the board
		int rows = this.maxRows;
		int columns = this.maxColumns;
		this.board = new Board(rows,columns);
		this.board.initializeBoard();
		initializeCharacters();
			
	}
	

	
	private void printRules() {
		System.out.println(""
				+ "------------------------Subterranean------------------------\n"
				+ ""
				+ "You find yourself a survivor of an unamed war hiding out\n"
				+ "a bunker deep underground, its builders abandoned it long\n"
				+ "ago. Their rivals seem to not have gotten the message that\n"
				+ "you are not their enemy but instead took it upon themselves\n"
				+ "to find and irradicate you from a nearby bunker. After\n"
				+ "blasting their way through one of the gates one of them\n"
				+ "managed to get into the underground complex. Thankfully\n"
				+ "the bunker builders designed rooms to close after a breach.\n"
				+ "They also built in a set of randomizing traps for the\n"
				+ "defenders to use which can be activated by a key only you\n"
				+ "have. Now you must choose who must defend the bunker and \n"
				+ "face the attacker.\n"
				+ "\n"
				+ "The Brute can do extra damage and on a successful hit has\n"
				+ "the chance to knock back the enemy.\n"
				+ "\n"
				+ "The Hacker has a bag of traps that can be thrown anywhere\n"
				+ "on the board.\n"
				+ "\n"
				+ "The Archer is a ranged character who can shoot arrows three\n"
				+ "spaces ahead.\n"
				+ "\n"
				+ "The Medic carries a number of medkits they can use to heal\n"
				+ "themselves after a certian amount of damage has been taken.\n");
	}
	
	/*****************************************************************************************************
	 * Main Function
	 */
	
	public void playGame() {
		int choice = 0; // the player has not made a choice therefore the choice is zero
		// while our global variable is true keep playing
		while(this.keepPlaying){
			// setOptions(); // this could be a function that the user changes settings before starting the match
			this.printRules();
			// initialize the game
			this.initializeGame();
			// while both players are alive keep taking turns
			while(!this.gameOver){
				this.takeTurns();
				}
			// ask the player if they want to continue
			choice = Misc.getUserInputInt("Enter 1 if you wish to play again. 2 if you don't", this.scan, 1, 2);
			if(choice != 1){
				keepPlaying = false;
			} else {
				keepPlaying = true;
				this.gameOver = false;
			}
		}
	}
	
	/*****************************************************************************************************
	 * Check functions
	 */
	private void checkGameOver() {
		if(!c1.isAlive() || !c2.isAlive()) {
			this.gameOver = true;
			if(c1.isAlive()) {
				System.out.println(c1.getName() + " wins");
			} else {
				System.out.println(c2.getName() + " wins");
			}
		} else {
		}
	}


	/*****************************************************************************************************
	 * Selection Functions
	 */
	private Character returnSelectedCharacter(int choice, String name) {
		Character c = null;
		
		// user choice determines what character returned becomes.
		if(choice == 1) {
	 		c = new Brute(this.board,name,health,xStart,yStart,baseChance,maxDamage,minDamage,directionStart); 
	 		// play as standard Brute		
		} else if (choice ==2 ) {
			// hacker has reduced damage potential
			minDamage=0;
			maxDamage=3;
			// use the Hacker character
			c = new Hacker(this.board,name,health,xStart,yStart,baseChance,maxDamage,minDamage,directionStart, 
					chanceToCounterVal, minCounterVal, maxCounterVal);
		} else if (choice ==3 ) {
			c = new Ranged(this.board,name,health,xStart,yStart,baseChance,maxDamage,minDamage,directionStart, 
					chanceToCounterVal, minCounterVal, maxCounterVal, rangedAttackChance, range, minRangedDamage, maxRangedDamage); // play as a ranged character
			
		} else if (choice ==4 ) {
			c = new Medic(this.board,name,health,xStart,yStart,baseChance,maxDamage,minDamage,directionStart); // play as a ranged character
			
		} 
		
		
		return c;
	}
	
	public static String optionSelect(Scanner scan, String message, String [] stringArray) {
		String entered="None";
		while(entered.equals("None")) {
			System.out.println(message);
			String userInput = scan.nextLine();
			// keep iterating throught the possibel 
			for(int i=0; i < stringArray.length; i++) {
				if(userInput.equals(stringArray[i])) {
					entered=userInput ;
				}
			}
			if(entered.equals("None")) {
				System.out.println("Please enter a valid option");
			}
		}
		// if none is returned then the supplied option was not valid
		return entered;
	}

	/*****************************************************************************************************
	 * Turn Functions
	 */
	private void takeHumanTurn(Character human) {
		// check for trap damage first thing
		checkForTrapDamage(human);
		int choice = 1;
		String message = "You have three options: \n" +
		"Move (Enter 1)\n" +
		"Combat (Enter 2)\n" +
		"Change direction (Enter 3)";
		if(human.charicterClass== "Hacker" || human.charicterClass== "Healer" || human.charicterType== "Defender" ) {
			message = message + "\nTake an action (Enter 4)";
			choice = Misc.getUserInputInt(message,this.scan, 1, 4);
		} else {
			choice = Misc.getUserInputInt(message,this.scan, 1, 3);
		}
		// use a switch to determine what the user chose
		switch(choice){
		// move choice logic
			case 1:
				human.move();
				break;
		// attack choice logic
			case 2:
				if(human.charicterClass=="Ranged") {
					message=("Would you like to do a melee(1) or ranged attack(2) ?");
					choice = Misc.getUserInputInt(message,this.scan, 1, 2);
					if(choice==1) {
						// set the ranged attack to be true and do a ranged attack
						human.setRangedAttack();
						this.combat(human,c2);
					} else {
						this.combat(human,c2);
					}
				}
					this.combat(human,c2); 
				break;
			// change direction logic
			case 3:
				message ="Which direction do you wish to face?\n1 for north\n2 for east\n3 for south\n4 for west";
				int moveChoice = Misc.getUserInputInt(message,this.scan, 1, 4);
				// change the direction
				human.changeDirection(moveChoice);
				break;
				// character takes an action
			// special action logic
			case 4:
				// the character can activate a trap if they are on the lever and are a defender
				String actionChoiceMessage = "Action choices are:\n";
				int maxChoices=3;
				String choiceStrings[]= new String [maxChoices];
				String actionChoice;
				int choices=0;
				if(human.charicterType.equals("Defender") && board.checkForLever(human.getLocation()[0], human.getLocation()[1], board.numberOfLevers)) {
					choiceStrings[choices]="Activate";
					choices++;
					actionChoiceMessage = actionChoiceMessage+ " activate traps on the board ( Enter Activate)\n";

				}

				// character can only be a single class so new if statement begun
				if( human.charicterClass.equals("Hacker") ) {
					// dynamically change the choice number
					choiceStrings[choices]="Trap";
					choices++;
					actionChoiceMessage = actionChoiceMessage+ "Try to throw a trap one square ahead ( Enter Trap )\n";

				} else if(human.charicterClass.equals("Medic")) {
					// dynamically add the choice either place 0 or place 1 on the array
					choiceStrings[choices]="Medkit";
					choices++;
					actionChoiceMessage = actionChoiceMessage+ "Try to use a medkit ( Enter Medkit )\n";
				}
				// add in nothing at the end of the array
				for(int i =choices+1; i < maxChoices;i++) {
					choiceStrings[i]=null;
				}
				
				
				 actionChoice = optionSelect(scan,actionChoiceMessage,choiceStrings);
				 if(actionChoice.equals("Activate")) {
						board.activateTraps(human);					 
				 } else if(actionChoice.equals("Trap")) {
						if(human.getTrapsLeft() > 0 ) {
						// throw a trap on the board
							System.out.println(human.getName()+" grabs a handfull of spikes from their bag and throws them on the ground.");
							human.throwTrap();
						} else {
							System.out.println("Reaches for a trap but there are no more left");
						}
				 } else if(actionChoice.equals("Medkit")) {
						human.useMedkit(human);					 
				 }
			}
	}
	

	private void takeComputerTurn( Character bot, Character nonbot) {
		// the direction the bot will turn 
		int turnDirection=0;
		// set a boolean for if the computer will attack or move if the enemy is not in a diagnal square
		boolean enemyNearby=false;
		boolean willAttack=false;
		boolean willMove=false;
		boolean willTurn=false;
		boolean facingWall=false;
		boolean isStuck=false;
		boolean isReallyStuck=false;
		
		// check for trap damage first thing
		checkForTrapDamage(bot);

		// if the bot has not moved and the next turn is not an attack increase the bot has not moved counter
		if(bot.botLastLocation[0]==bot.getLocation()[0] && bot.botLastLocation[1]==bot.getLocation()[1]) {
			bot.botHasNotMovedTurnCount=bot.botHasNotMovedTurnCount+1;
			System.out.println(bot.getName()+" Has not moved for "+bot.botHasNotMovedTurnCount+" turns");
			if(bot.botHasNotMovedTurnCount>5) {
				// if the bot has not moved for more than five turns then initiate the second method of unsticking a bot
				isReallyStuck=true;
			}
		} else {
			bot.botHasNotMovedTurnCount=0;	
		}
		if(bot.botHasNotMovedTurnCount>4) {
			isStuck=true;
		}


		// check around the bot player to see if there is an enemy nearby
		int [] adjacentSquareValues=bot.checkAdjacentSquares(bot.getLocation()[0],bot.getLocation()[1]);

		/*/ used to print out the adjacent square values for troubleshooting
		for(int i =0;i<adjacentSquareValues.length;i++) {
			System.out.print(adjacentSquareValues[i]+" ");
		}
		System.out.print("\n");
		//*/
		// figure out if the bot is facing the wall
		if(bot.newIncrement()[0] == bot.getLocation()[0] 
				&& bot.newIncrement()[1] == bot.getLocation()[1]) {
			// if the new increment is pushed back to the current location then the out of bounds error checking will feed back the current
			// location
			facingWall=true;
			System.out.println(bot.getName()+" is facing a wall");
		}
		

		// set the bots last location before any new moves are made
		bot.botLastLocation=bot.getLocation();
		
		// make an intelligent decision where to move based off of the following statement
		// ordered in the following format so that places from 0-3 are direct hits and places 
		// 4-7 are turn or move indicators
		for(int i =0 ; i < adjacentSquareValues.length; i++) {
			// if an enemy exists in a nearby square mark the flag
			if(adjacentSquareValues[i]==1) {
				enemyNearby=true;
			}
			// if there is something directly next to the bot either turn to face it or if its already facing it attack
			if(enemyNearby==true && i<4){
				if(i+1==bot.getDirection()) {
					// if the enemy is in the square the bot is facing then set flag to attack
					willAttack=true;
				} else {
					// if the bot is not facing the enemy then it will turn in direction
					turnDirection=i+1;
					willTurn=true;
				}
			}
			if(adjacentSquareValues[i]==1 && i>3){
				// if the enemy is in a diagonal square then subtract 4 from i and turn in that direction
				willTurn=true;
				turnDirection=Misc.generateRandomInt(i-1, i-4);// System.out.println("Turning the value of "+turnDirection); // subtract 3 because option list begins at 1 instead of 0
			}
		}
		// basic flow is -> if stuck move -> if nothing interesting is going and Not facing a wall (bots really like walls for some reason) move randomly -> if an enemy is nearby attack/move/turn accordingly 
		// -> otherwise if facing a wall turn around
		
		// if the bot is stuck but not really stuck and it does not sense an enemy around then do a random move
		if(isStuck == true && isReallyStuck != true && willAttack != true) {
			System.out.println(bot.getName()+" made a choice and moves " + bot.compass());
			bot.move();
		} else if (isReallyStuck == true  && willAttack != true) {
			// if the bot is super stuck and not going to attack then do something completely random
			
			// so during testing there were some wierd edge cases where if two bots were diagonal to eachother they would get locked in place
			// this block depends on finding out if they have not moved in more than 5 turns and has not unlocked themselves.
			System.out.println(bot.getName()+" was confused and does something random");
			int randomChoice = Misc.generateRandomInt(1, 3);
			// chose a random action and does it
			switch(randomChoice) {
				case 1:
					bot.move();
					break;
				case 2:
					bot.turnAround();
					break;
				case 3:
					combat(bot,nonbot); // combat checks to see if the attack will succeed
					break;
			}
		} else if(board.checkForLever(bot.getLocation()[0], bot.getLocation()[0], board.numberOfLevers) && board.checkIfTrapsAreActive() == false) { // if they are on a lever pull it except if traps are active
			board.activateTraps(bot);
		} else if(enemyNearby==false && facingWall == false){ // if no enemy is nearby and the bot is not facing a wall then the character moves or turns randomly
			// determine if the computer will attack or take a move action randomly
			int choice = Misc.generateRandomInt(1,2);
			// if a move action is chosen then determine if the action will be a change direction or an actual move
			choice = Misc.generateRandomInt(1,3); // they should turn more often than they move to avoid getting stuck in corners
			if(choice ==1) {
				int direction = Misc.generateRandomInt(1, 4);
				
				bot.changeDirection(direction); // change direction
				System.out.println(bot.getName() + "is blissfully unaware and faces " 
				+ bot.compass());
			} else {
				System.out.println(bot.getName() + "is blissfully unaware and moves " 
			+ bot.compass() + " one square.");
				bot.move(); // else move in the current direction
			}
		} else if (enemyNearby == true){ // if there is a nearby enemy then
			
			// check if the enemy is directly ahead and attack
			if( willAttack == true ) {
				// if the computer is facing the opponent then it attacks
				System.out.println(bot.getName()+" sees something and attacks "+bot.compass());
				this.combat(bot,nonbot);	
				
			} else if(willMove == true ) {
				System.out.println(bot.getName()+" sees something and moves "+bot.compass());
				bot.move();
			}
			else if (willTurn == true) { // the 
				bot.changeDirection(turnDirection); // change direction
				System.out.println(bot.getName()+" heard something and faces "+bot.compass());
			} else  {
				// do nothing
			}
			
		} else if(facingWall == true ) {
			// if the computer hits a wall and there is no enemy nearby then turn around
			// the bot would also get stuck in a corner if a random turn choice was made so the easy path of turning them around entirely makes for an interesting game
			System.out.println(bot.getName()+" turned away from the wall and is now facing "+bot.compass());
			bot.turnAround();
		} else {
			
		}
	}


	private void takeTurns() {
		// take turns as long as the game is not over


		this.displayInfo(); // display the information about all the charicters
		board.displayBoard();
		// check if either charicter is on a trap space and apply damage

		// mark the board with the charicters 
		board.markBoard(c1.getLocation()[0],c1.getLocation()[1], c1.getSymbol());
		board.markBoard(c2.getLocation()[0],c2.getLocation()[1], c2.getSymbol());
		// 
			if(!this.gameOver) {
				if((this.turn % 2) == 0) { // if the current turn is even
//					this.takeComputerTurn(c1,c2);
					this.takeHumanTurn(c1);
				} else { // if the current turn is odd
					this.takeComputerTurn(c2,c1);
				}
				this.turn ++; // increment the turn forward one
				this.checkGameOver(); // see if the game is over.
			}
		}
	
	
	/*****************************************************************************************************
	 * Effect Functions
	 */
	
	// checks for perminant trap damage on board
	private void checkForTrapDamage(Character c) {
		// check if the character is on a trap square and apply damage
		int [] charLocation = c.getLocation();
		if(board.checkForTrap(charLocation[0], charLocation[1], board.numberOfTraps) && board.turnsTrapsActive>0) {
			System.out.println(c.getName()+" steped on a trap and lost "+board.trapDamage+" HP");
			c.hit(board.trapDamage);
		} 
	}
	/*****************************************************************************************************
	 * Action Functions
	 */

	private void combat(Character attacker, Character defender) {
		int[][] solution = attacker.attack();
		System.out.println(solution.length);
		int [] defendCoordinates = defender.getLocation();
		// if the solution is 1 in length, meaning there is only one array set
		if(solution.length<2) { // check if this is a ranged attack or not
			// attack one square
			int [] attackCoordinates = {solution[0][0],solution[0][1]};
			// if the defender is at the specified coordinates then damage is delt to the defender
			if(attackCoordinates[0] == defendCoordinates[0] && attackCoordinates[1] == defendCoordinates[1] ) {
			// check to see if the Brute did a power hit or not based on the maximum damage and the attacker is a Brute class
				if(attacker.charicterType == "Attacker") {
					System.out.println(attacker.name + " successfully preformed power hit for "
							+solution[0][2]+" damage and pushed them back one square.");
					defender.hit(solution[0][2]);
					attacker.push(defender);
				}
				// check to see if the attack is from behind and if the attacker is a Artifacer aka rouge type class
				else if(attacker.getDirection() == defender.getDirection() 
						&& attacker.charicterClass == "Artifacer") {
					solution[0][2]=attacker.sneakAttack(solution);
					System.out.println(attacker.getName()+ " successfully preformed sneak attack for "+solution[0][2]+" damage.");
					defender.hit(solution[0][2]);
					defender.turnAround();
					}
				// otherwise do the regular damage
				else if (solution[0][2]>0){
					System.out.println(attacker.name + " successfully hit "+defender.name+" for "+solution[0][2]+" damage.");	
					defender.hit(solution[0][2]);
					attacker.turnAround();
				}
			}

			// if it is a ranged attack because of the length of the solution then do that attack type instead
		} else { 
			for(int i=0; i< solution.length; i++) { // loop through all the arrays in the 2d array
				int [] attackCoordinates = {solution[i][0],solution[i][1]};
				// if the defender is at the specified coordinates then damage is delt to the defender
				if(attackCoordinates[0] == defendCoordinates[0] && attackCoordinates[1] == defendCoordinates[1] ) {
					defender.hit(solution[i][2]);
					// let the defender know they succeeded in hitting the opponent
					if(solution[i][2]>0) {
						System.out.println(attacker.getName()+" successfully preformed a ranged attack on "+defender.getName() +" for "+solution[i][2]+" damage.");
					}
					// break to stop double damage from ocurring.
					// can add if class=="wizard" or if damageType="bounce" to allow multiple hits to occur if desired
					break;
				}
			}
		}
	}
		

	/*****************************************************************************************************
	 * Print Functions
	 */
	private void displayInfo() {
		// displays all the information about each character in one kind function
		System.out.println(c1.toString());
		System.out.println(c2.toString());
	}
	
}
