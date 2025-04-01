package subter;

import Misc.Misc;

public class Ranged extends Attacker {
	private double chanceToFire;
	private int range;
	private int maxRangedDamage;
	private int minRangedDamage;
	private boolean rangedAttack=false;
	
	public Ranged(Board board, String name, int max_hp, int x_coord, int y_coord, double base_chance, int do_damage_max,
			int do_damage_min, int direction, double chanceToRegen, int minRegen, 
			int maxRegen, double chanceToFire, int range, int minRangedDamage, int maxRangedDamage) {
		super(board, name, max_hp, x_coord, y_coord,  base_chance, do_damage_max, do_damage_min, direction, 
				chanceToFire, minRangedDamage, maxRangedDamage);
		this.chanceToFire=chanceToFire;
		this.charicterClass="Ranged";
		this.range=range;
		this.maxRangedDamage=maxRangedDamage;
		this.minRangedDamage=minRangedDamage;
	};
	
	public Ranged() {
		super();

		
	}
	
	public void setRangedAttack() {
		// sets the flag for the next attack to return a ranged attack
		this.rangedAttack=true;
	}
	
	@Override
	public int[][] attack() {
		if(this.rangedAttack=true) {
			// set ranged attack flag to normal condition of false
			this.rangedAttack=false;
			// preform the ranged attack
			return rangedAttack();
		} else {
			// do a normal attack
			return meleeAttack();
		}
	}
	
	public int[][] meleeAttack() {
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
	
	public int[][] rangedAttack(){
		// gets the direction the ranged character is facing
		int facing = this.getDirection();
		int [] currentLocation = this.getLocation();
		int [][] newSolution = new int [this.range][3];
		int damage = Misc.generateRandomInt(this.minRangedDamage, this.maxRangedDamage);
		if(Misc.generatePercent()<this.chanceToFire) {
			// if the shot was unsuccessfull reset the damage to zero
			damage=0;
		}
		switch(facing){
			case 1:
				// facing north should increment spaces on the y axis 
				for(int i=0; i<this.range; i++) {
					newSolution[i][0]=currentLocation[0];
					// increment in the north direction without exceeding it
					newSolution[i][1]=Misc.setVars(currentLocation[1]+1, this.board.getMaxRow(), currentLocation[1]+i+1);
					newSolution[i][2]=Misc.setVars(0, this.maxRangedDamage, damage);
				}
				break;
			case 2:
				// facing east should increment spaces on the x axis
				for(int i=0; i<this.range; i++) {
					newSolution[i][0]=Misc.setVars(currentLocation[0]+1, board.getMaxColumn(), currentLocation[0]+i);
					newSolution[i][1]=currentLocation[1];
					// increment in the eastern direction without exceeding it
					newSolution[i][2]=Misc.setVars(0, this.maxRangedDamage, damage);
				}
				break;
			case 3:
				// facing south should decrement along the y axis
				for(int i=0; i<this.range; i++) {
					newSolution[i][0]=currentLocation[0];
					newSolution[i][1]=Misc.setVars(0, board.getMaxColumn(), currentLocation[1]-i-1);
					// decrement in the southern direction without exceeding it
					newSolution[i][2]=Misc.setVars(0, this.maxRangedDamage, damage);
				}
				break;
			case 4:
				// facing west should decrement along the x axis
				for(int i=0; i<this.range; i++) {
					newSolution[i][0]=Misc.setVars(0, board.getMaxRow(), currentLocation[0]-i-1);
					// increment in the western direction without exceeding it
					newSolution[i][1]=currentLocation[1];
					newSolution[i][2]=Misc.setVars(0, this.maxRangedDamage, damage);
				}
				break;
		}
		// debugging output ranged attack solution
		//*/
		for(int i = 0; i < newSolution.length; i++) {
			System.out.println("Arrow hits: "+newSolution[i][0] +","+newSolution[i][1] +","+newSolution[i][2]);
		}
		//*
		return newSolution;
	}
	
}
