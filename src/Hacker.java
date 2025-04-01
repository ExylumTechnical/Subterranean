package subter;

import Misc.Misc;

public class Hacker extends Defender {
	private int trapsLeft;
	
	public Hacker(Board board, String name, int max_hp, int x_coord, int y_coord, double base_chance, int do_damage_max,
			int do_damage_min, int direction, double chanceToBlockVal, int minBlockVal, int maxBlockVal) {
			super(board, name, max_hp, x_coord, y_coord,  base_chance, do_damage_max, do_damage_min, direction,chanceToBlockVal,minBlockVal,maxBlockVal); 
			this.trapsLeft=3;
			this.minDamage=0;
			this.maxDamage=3;
			
			// default Stats for the Artifiacer
			this.maxHp=20;
			this.baseChance=.6;
			this.maxDamage=3;
			this.minDamage=0;
			this.chanceToBlock=.4;
			this.minBlock=0;
			this.maxBlock=3;
			this.charicterClass="Hacker";
		};
	public Hacker() {
		super();
		this.trapsLeft=3;
		this.minDamage=0;
		this.maxDamage=3;
		
		// default Stats for the Artifiacer
		this.maxHp=20;
		this.baseChance=.6;
		this.maxDamage=5;
		this.minDamage=0;
		this.chanceToBlock=.4;
		this.minBlock=0;
		this.maxBlock=3;
		this.charicterClass="Hacker";
		
	}
	/*****************************************************************************************************
	 * trap ability functions
	 */
	public void throwTrap() {
		// if the character has a trap left
		if(trapsLeft>0) {
			System.out.println(this.getName()+" laid a trap");
			// get one square ahead
			int [] trapLocation=this.newIncrement();
			// mark the board with a temporary trap ahead of them
			board.markBoard(trapLocation[0], trapLocation[1], board.trapSpace);
			this.trapsLeft=this.trapsLeft-1;
		} else {
			System.out.println(this.getName()+" has no more traps");			
		}
	}

	public int getTrapsLeft() {
		return this.trapsLeft;
	}
	
	/*****************************************************************************************************
	 * attack functions
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
	
	/*****************************************************************************************************
	 * print functions
	 */
	@Override
	public void printStats() {
		System.out.println(this.name+" has "+this.trapsLeft + " temporary traps left.");
		System.out.println(this.name+" has "+this.currentHp + " hp left and is facing "+this.compass());
	}

		
}
