package subter;

import Misc.Misc;

public class Board {
	private char[][] board;
	char blankSpace= " ".charAt(0);
	char trapSpace= "_".charAt(0); // charAt is not entirely necessary but it is good to know the method exists
	char leverSpace= "/".charAt(0);
	char coumnSpace = "O".charAt(0); // also a fun text emoji
	int rows, columns;

	int setTraps;
	int trapDamage; // added trap damage
	int numberOfTraps;
	int numberOfLevers;
	int turnsTrapsActive= 0;
	int [][] trapCoords;
	int [][] leverCoords;

	public Board(int columns, int rows) {
		this.rows = rows;
		this.columns = columns;
		this.board = new char [columns][rows];
		this.trapDamage=1;
		this.numberOfTraps=4;
		this.numberOfLevers=1;
		this.trapCoords = new int [numberOfTraps][2];
		this.leverCoords = new int [numberOfLevers][2];
		
	}
	/*****************************************************************************************************
	 * initialization functions
	 */

	public void initializeBoard() {
		// creates the board by defining the multi dimensional array with blank spaces
		for( int row = 0; row< this.rows; row++) {
			for( int column = 0; column < this.columns; column++) {
				this.board[column][row]= blankSpace;
			}
		}
		// set trap and lever locations and populate them on the board
		this.generateTraps();
		this.generateLevers();
		for(int i =0; i <this.numberOfTraps; i++) {
			board[trapCoords[i][0]][trapCoords[i][1]]=trapSpace;
		}
		// generate a leaver 

	}

	/*****************************************************************************************************
	 * getter functions
	 */

	public int getMaxColumn() {
		// gets the current minimum rows
		return this.columns;
	}		
	public int getMaxRow() {
		// gets the current maximum rows
		return this.rows;
	}		

	public char getSquare(int column, int row) {
		// returns the char value at any particular square
		return board[column][row];
	}
	/*****************************************************************************************************
	 * setter functions
	 */
	
	public void markBoard(int column, int row, char symbol) {
		// marks the board
		this.board [column][row] = symbol;
	}
	
	/*****************************************************************************************************
	 * environment trap functions
	 */
	
	public void generateLevers() {
		// generate a new space for the lever

		int [] newLeverCoords = new int [2];
		// generate traps 
		for(int i =0; i <this.numberOfLevers; i++) { // iterate through the number of levers and add them to the lever array
			// generate some new random coordinates for the new lever
			newLeverCoords [0] = Misc.generateRandomInt(0,this.columns-1);
			newLeverCoords [1] = Misc.generateRandomInt(0,this.rows-1);
			// check to see if there is aready a trap or a lever at that space
			while(this.checkForTrap(newLeverCoords[0], newLeverCoords[1], this.numberOfTraps) == true ) {
				// if there is a trap already at that space then generate some new coordinates until a 
				// unique space is found to place the trap.
				newLeverCoords [0] = Misc.generateRandomInt(0,this.columns-1);
				newLeverCoords [1] = Misc.generateRandomInt(0,this.rows-1);
			}
			this.leverCoords[i][0]=newLeverCoords [0];
			this.leverCoords[i][1]=newLeverCoords [1]; 
		}
	
	}
	

	
	public void generateTraps() {
		int [] newTrapCoords = new int [2];
		// generate traps 
		for(int i =0; i <this.numberOfTraps; i++) { // iterate through the number of traps to add them to the trap array
			// generate some new random coordinates for the new traps
			newTrapCoords [0] = Misc.generateRandomInt(0,this.columns-1);
			newTrapCoords [1] = Misc.generateRandomInt(0,this.rows-1);
			// check to see if there is already a trap or a lever at that space

			while(this.checkForTrap(newTrapCoords[0], newTrapCoords[1], i) == true ) {
				// if there is a trap or lever already at that space then generate some new coordinates until a 
				// unique space is found to place the trap.
				newTrapCoords [0] = Misc.generateRandomInt(0,this.columns-1);
				newTrapCoords [1] = Misc.generateRandomInt(0,this.rows-1);
			}
			this.trapCoords[i][0]=newTrapCoords [0];
			this.trapCoords[i][1]=newTrapCoords [1];
		}
	}
	
	public boolean checkIfTrapsAreActive() {
		if(this.turnsTrapsActive>0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void activateTraps(Character c) {
		// if the character is a defender type and on a lever square then activate traps
		if(c.charicterType=="Defender" && this.checkForLever(c.getLocation()[0], c.getLocation()[1], this.numberOfLevers)) {
			this.turnsTrapsActive=(columns*rows)/2;
			System.out.println(c.getName()+" activated traps!");
		} else {
			System.out.println(c.getName()+" pulled a lever but nothing happened");			
		}
		// activate the traps for the time it takes to move across the board
	}
	
	public void markTraps() {
		for(int i = 0; i < this.numberOfTraps; i++) {
			// if both coordinates are the same as one of the values in the trap cords multi-diminsional array
			// then set the trapExists variable to be true
			if(this.getSquare(this.trapCoords[i][0], this.trapCoords[i][1])==this.blankSpace) {
				this.markBoard(this.trapCoords[i][0], this.trapCoords[i][1], this.trapSpace);
			} else {
				// do nothing
			}
			
		}
	}
	
	
	public boolean checkForTrap(int column, int row, int trapCount) {
		// checks to see if there are traps at the given coordinates
		boolean trapExists=false;
		for(int i = 0; i < trapCount; i++) {
			if(column == this.trapCoords[i][0] && row == this.trapCoords[i][1]) {
				// if there is a trap from the list of static traps then a trap does exist at that square
				// this makes sure that if a character is standing on a trap when traps are activated it still
				// does damage
				trapExists=true;
			} else if (this.getSquare(column, row) == this.trapSpace) {
				// we also check to make sure there is not a temporary trap in the space
				trapExists=true;
			} else {
				// do not mark as false because it will clear the mark that a trap does exist on the square
			}
		}
		return trapExists;
	}
	
	public boolean checkForLever(int column, int row, int leverCount) {
		// checks to see if there are traps at the given coordinates
		boolean leverExists=false;
		for(int i = 0; i < leverCount; i++) {
			// if both coordinates are the same as one of the values in the trap coords multi diminsional array
			// then set the trapExists variable to be true
			if(column == this.leverCoords[i][0] && row == this.leverCoords[i][1]) {
				leverExists=true;
			} else {
				leverExists = false;
			}
		}
		return leverExists;
		
	}
	/*****************************************************************************************************
	 * display functions
	 */
	
	public void displayBoard() {
		// redraw traps on the board
		 markTraps();
		// draws the board using | as the separators 
		String vertical_bar = "|";
		// display the beginning of the line
		String main_line = "|";
		for(int i = 0; i < this.columns - 1; i++) {
			// display the middle row values
			main_line += "---+";
		}
		// display the end of the line
		main_line += "---|";
		if(this.rows > 0 && this.columns > 0) {
			System.out.println(main_line);
		}
		// display the main rows of the board
		for(int i = this.rows -1 ; i >= 0;i--) { // move along the y axis
			for(int j = 0; j < this.columns;j++) { // move along the x axis
				// display the char value at each space
				char spaceValue=board[j][i];
				if(this.checkForTrap(j, i, this.numberOfTraps) == true && spaceValue==this.blankSpace ) {
					spaceValue=trapSpace;
				}
				if(this.checkForLever(j, i, this.numberOfLevers) == true && spaceValue==this.blankSpace ) {
					spaceValue=leverSpace;
				}
				System.out.print(vertical_bar + " " + spaceValue + " ");
			}
			if(this.columns>0) {
				System.out.print(vertical_bar);
				System.out.println();
				System.out.println(main_line);
			}
		}
			System.out.println();
/*/ code block to print out information about the traps
			for(int i =0; i < this.numberOfTraps; i++) {
				System.out.println("Trap at: "+trapCoords[i][0]+","+trapCoords[i][1]);
			}
			for(int i =0; i < this.numberOfLevers; i++) {
				System.out.println("Lever exists at : "+leverCoords[i][0]+","+leverCoords[i][1]);
			}
//*/
			if(this.turnsTrapsActive>0) {
				System.out.println("Traps will be active for "+this.turnsTrapsActive+" turns");
				this.turnsTrapsActive=this.turnsTrapsActive-1;
			}
		}




	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}