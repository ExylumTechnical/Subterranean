package subter;

import java.util.Random;
import Misc.Misc;


public abstract class Character implements iCharacter{
	protected String name;
	protected int maxHp;
	protected int currentHp;
	protected int row;
	protected int column;
	protected double baseChance;
	protected int maxDamage, minDamage;
	protected int direction;
	protected int maxRow;
	protected int maxColumn;
	protected char standingOn;
	int [] botLastLocation;
	int botHasNotMovedTurnCount;
	int range;
	// added board class for some of the error checking functions
	protected Board board;
	// added charicterClass string to identify the type of charicter that was created
	protected String charicterClass;
	protected String charicterType;
	// added a isHuman boolean to define if the character will require human input or not.
	protected boolean isHuman; // true for interactable and false for non interactable
	
	public Character(Board board, String name, int max_hp, int x_coord, int y_coord, double base_chance, int do_damage_max,
			int do_damage_min, int direction) {
		this.name = name;
		this.maxHp = max_hp;
		this.currentHp = max_hp;
		this.column = x_coord;
		this.row = y_coord;
		this.baseChance = base_chance;
		this.maxDamage = do_damage_max;
		this.minDamage = do_damage_min;
		this.direction = direction;
		this.maxColumn = board.getMaxColumn();
		this.maxRow = board.getMaxRow();
		this.board = board;
	}
	
	
	public Character() { // default character stats
		this.range=1; // range of one by default
		this.currentHp = 20;
		this.baseChance=0.6;
		this.maxDamage =5;
		this.minDamage =0;
		this.direction = 2; // faces east by default
		
	}
	
	/***************************************************************************************************** 
	 * Getters and setters
	 */
	public void setPlayerType(boolean set){
		this.isHuman=set;
		
	}
	public boolean getPlayerType(){
		return this.isHuman;
		
	}
 	public int[] getLocation() {
		int [] currentLocation={this.column , this.row }; // gets the current location of the character
		return  currentLocation; // returns the array of the current location
	}
	@Override
	public void setName(String newName) {
		this.name=newName; // sets the name of the character
	}

	
	@Override
	public String getName() {
		return this.name; // gets the name of the character
	}
	
	@Override
	public int getDirection() { 
		// returns the int value of the direction the character is facing
		return this.direction;
	}
	
	public int getHealth() { // gets the current health of the character
		return this.currentHp;
	}

	public int getMaxHealth() { // gets the max health of the character
		return this.maxHp;
	}
	
	@Override
	public char getSymbol() { // gets the symbol of the character
		return this.name.charAt(0);
	}
	
	public String compass() {
		// returns the string value of the direction the character is facing
		String direction="";
		switch(this.direction){ // switch loop to set the string of the direction change
			case 1:
				direction = "North";
				break;
			case 2:
				direction = "East";
				break;
			case 3:
				direction = "South";
				break;
			case 4:
				direction = "West";
				break;
			default: // defaults to north
				direction = "North";
				this.direction = 1;
		}
		return direction; // returns the direction string like a compass
	}
	/***************************************************************************************************** 
	 * Environment checks
	 */
	
	public int [] checkAdjacentSquares(int column, int row) { // returns an array of directions where enemy exist
		int [] direction = {0,0,0,0,0,0,0,0}; // if the value in the array remains zero then they there are no characters in that direction 1 is north .. 4 is south
		char squareValue; // Misc.setVars(0, , );
		int [][] checkSpaces= {
				// ordered in the following format so that places from 0-3 are direct hits and places 4-7 are turn or move indicators
				{Misc.setVars(0, this.maxColumn-1,column) , Misc.setVars(0,this.maxRow-1,row+1 ) }, // 0,+1 North
				{Misc.setVars(0, this.maxColumn-1,column+1), Misc.setVars(0,this.maxRow-1,row ) }, // +1,0 East
				{Misc.setVars(0, this.maxColumn-1,column), Misc.setVars(0,this.maxRow-1,row-1 ) }, // 0,-1 South
				{Misc.setVars(0, this.maxColumn-1,column-1), Misc.setVars(0,this.maxRow-1,row ) }, // -1,0 West
				{Misc.setVars(0, this.maxColumn-1,column+1), Misc.setVars(0,this.maxRow-1,row+1 ) }, // +1,+1 North East diagonal square
				{Misc.setVars(0, this.maxColumn-1,column+1), Misc.setVars(0,this.maxRow-1,row-1 ) }, // +1,-1 South East diagonal square
				{Misc.setVars(0, this.maxColumn-1,column-1), Misc.setVars(0,this.maxRow-1,row-1 ) }, // -1,-1 South West diagonal square
				{Misc.setVars(0, this.maxColumn-1,column-1), Misc.setVars(0,this.maxRow-1,row+1 ) }, // -1,+1 North West diagonal square
				};

		for(int i=0;i<8;i++) {
			squareValue = board.getSquare(checkSpaces[i][0],checkSpaces[i][1]);
			// System.out.println("value of check space action: "+squareValue);
			// if the adjacent square being queried is either a blank space or a trap space then the value in the direction array is changed.
			// if the value of the square is equal to the current characters symbol then it cannot attack ( the error correcting code in the check spaces array
			// can end up setting the current space as the space to query if the character is against a wall. ) 
			if ( (int) squareValue != (int) board.blankSpace && (int) squareValue != (int) board.trapSpace && (int) squareValue != (int) this.getSymbol() && squareValue != board.leverSpace) {
				// if the square is not blank, not a trap space (currently occupied), and not this character, and is not a lever it has a value of one.
				direction[i]=1;
			} else if (squareValue == board.leverSpace) {
				// if the square is a leaver then it has a value of 2
				direction[i]=2;
			}
			else {
				direction[i]=0;
			}
		}
		return direction;
	}
	

	
	/***************************************************************************************************** 
	 * Condition checks
	 */
	
	@Override
	public boolean isAlive() {
		// returns true or false if this character is dead or not.
		return (this.currentHp>0);
	}
	
	/***************************************************************************************************** 
	 * Printing functions
	 */
	
	@Override
	public String toString(){
		// override for the toString function
		String read_out = this.name + "\n";
		read_out += "Facing " + this.compass() + "\n";
		read_out += "Has " + this.currentHp + " hp\n";
		return read_out;
	}
	
	@Override
	public void printStats() {
		System.out.println(this.name+" has "+this.currentHp + " hp left and is facing "+this.compass());
	}
	
	
	
	/*****************************************************************************************************
	 * Action functions
	 */

	@Override
	public boolean actionRoll(double chance) {
		// create and generate a random double
		Random rand = new Random();
		double roll = rand.nextDouble();
		// print the result of the roll and return true or false if the result was above or below the chance we supplied
		return (roll >= chance);
	}


	
	/***************************************************************************************************** 
	 * Move Functions
	 */
	@Override
	public void move() {
		// set the current location of the specified character
		int [] currentLocation = this.getLocation();
		// predict the next move the character will take and store it
		int [] futureMove = this.newIncrement();
		// make sure its a legal move
		boolean validMove = this.checkMove(futureMove[0],futureMove[1]);
		
		if(validMove) { // if it is a valid move go ahead and let the character move to that space
//			System.out.println("Number of traps: "+board.numberOfTraps);
			board.markBoard(currentLocation[0], currentLocation[1], board.blankSpace);	
			this.column=futureMove[0]; // sets the character column to be that of the new increment
			this.row=futureMove[1]; // sets the character row to be the that of the new increment

			board.markBoard(this.column, this.row, this.getSymbol());
			// by default make sure the character takes trap damage but this can be overridden for a special class with trap sense/avoidance
			
		} else { // if its not a valid move then do nothing and let the character know they may have hurt themselves
			System.out.println("Something prevented "+this.getName()+" from moving.");
		}
		// bounce the character backwards one square it will appear they stay in place 
	
	}
	
	public boolean checkMove(int column, int row) {
		// returns true if square can be moved to 
		char squareValue = board.getSquare(column, row);
		// Default the solution to be false
		boolean solution = false;
		// check the value of the square
		if( (int) squareValue == (int) board.blankSpace) {
			// the player can move onto a blank space
			solution = true;
		} else if ( (int) squareValue == (int) board.trapSpace) {
			// the player can move onto a trap space
			solution = true;
		} else {
			// all other squares are invalid moves
			solution = false;
		}
		return solution;
	}
	
	public void changeDirection(int choice) {
		this.direction = choice; // sets the direction of the character
	}
	
	public int[] newIncrement() {
		int[] newLocation = {this.column,this.row};
		// 1 is north, 2 is east, 3 is south, 4 is west.
		
			switch(this.direction){ // switch loop to choose the direction the character moves based off of the direction
				case 1:
					if( this.row >= this.maxRow-1) {
					} else {
						newLocation=incrementNorth();
					}
					break;
				case 2:
					if(this.column >= this.maxColumn-1 ) {
					} else {
						newLocation=incrementEast();
					}
					break;
				case 3:
					if(this.row == 0 ) {
					} else {
						newLocation=incrementSouth();
					}
					break;
				case 4:
					if(this.column == 0) {
					} else {
						newLocation=incrementWest();
					}
					break;
				default: // defaults to north and we change the direction to north for consistency
					if( this.row != 0) {
						newLocation=incrementSouth();
						this.direction = 3;
					} 
					
			}
		return newLocation;
	}
	
	// all increment functions stop the character from moving off the boards maximum column or row.
	private int[] incrementNorth() {
		int new_row = Misc.setVars(0,this.maxRow-1,this.row+1); // sets min and max possible values and increments up one
		int[] coordinates =  { this.column, new_row}; // sets the coordinates
		return coordinates; // returns the coordinates
	};
	private int[] incrementEast() {
		int new_column = Misc.setVars(0,this.maxColumn-1,this.column+1); // sets min an max possible values and increments right one
		int[] coordinates =  { new_column, this.row };// sets the coordinates
		return coordinates;// returns the coordinates
	};
	private int[] incrementSouth() {
		int new_row = Misc.setVars(0,this.maxRow-1,this.row-1); // sets min an max possible values and increments down one
		int[] coordinates =  { this.column, new_row };// sets the coordinates
		return coordinates;// returns the coordinates
	};
	private int[] incrementWest() {
		int new_column = Misc.setVars(0,this.maxColumn-1,this.column-1); // sets min an max possible values and increments left one
		int[] coordinates =  { new_column, this.row};// sets the coordinates
		return coordinates;	// returns the coordinates
	};
	
	public void turnAround() {
		int currentDirection = this.getDirection();
		// switch case for the direction changes
		switch (currentDirection) {
		case 1: // if facing north 
			this.changeDirection(3); // north south
			break;
		case 2: // if facing east
			this.changeDirection(4); // face west
			break;
		case 3: // if facing south
			this.changeDirection(1);  // face north
			break;
		case 4: // if facing west.
			this.changeDirection(2);  // face east
			break;
		default:
			break;
		}
	}
	

	
	/***************************************************************************************************** 
	 * Combat functions
	 */

	@Override
	public int[][] attack() {
		int [][] fireSolution = new int [1][3]; // create a multidimensional array {x coordinate, y coordinate, damage}
		int [] solution = this.newIncrement();
		// set the coordinates
		fireSolution[0][0] = solution[0];
		fireSolution[0][1] = solution[1];
		// base damage off of roll 
		if(actionRoll(this.baseChance)) { // if the attack succeeds roll for damage
			fireSolution[0][2] = Misc.generateRandomInt(this.minDamage, this.maxDamage);		
		} else { // if the attack fails store 0 as the last int in the array
			System.out.println("The attack failed");
			fireSolution[0][2] = 0;
		}
		return fireSolution; // return the fire solution
	}

	@Override
	public void hit(int damage) {
		// apply damage with positive values and health with negative values
		this.currentHp = Misc.setVars(0,this.maxHp,
		(this.currentHp - damage));
	}



	public void push(Character defender) {
		// this is a function in the warrior class and it is used in the Game class only when the max damage is exceeded in a single square damage hit
	}
	public int sneakAttack(int [][] originalAttack) {
		return 0;
		// this function is for the rouge type defender class
	}

	public void throwTrap() {
		// function defined in Hacker class
	}

	public int getTrapsLeft() {
		// function defined in Hacker class
		return 0;
	}

	public void useMedkit(Character c) {
		// function defined in Medic class
		
	}

	public void setRangedAttack() {
		// function defined in Ranged class
		
	}




}
