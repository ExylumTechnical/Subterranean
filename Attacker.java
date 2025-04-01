package subter;

import Misc.Misc;

public abstract class Attacker extends Character{
	private double chanceToRegen;
	private int minRegen;
	private int maxRegen;

	public Attacker(Board board, String name, int max_hp, int x_coord, int y_coord, double base_chance, int do_damage_max,
		int do_damage_min, int direction, double chanceToRegen, int minRegen, int maxRegen) {
		super(board, name, max_hp, x_coord, y_coord,  base_chance, do_damage_max, do_damage_min, direction);
		this.chanceToRegen = chanceToRegen;
		this.minRegen = minRegen;
		this.maxRegen = maxRegen;
		this.charicterType="Attacker";
	};

// computer generated character
	public Attacker() {
		super();
		this.chanceToRegen=.4;
		this.minRegen=1;
		this.maxRegen=4;
		this.charicterType="Attacker";
	}
	

	
	public void hit(int damage) {
		super.hit(damage);
		int regenAmount=0;
		if(super.isAlive()) {
			if(super.actionRoll(this.chanceToRegen) && damage> 0) {
				regenAmount=Misc.generateRandomInt(this.minRegen, this.maxRegen);
				if(regenAmount>0) {
					System.out.println(this.getName()+" successfully regenerated "+ regenAmount);
				}
			}
			super.hit(-1*regenAmount);
		}
	}
	
	
	// push function available to all attackers
	public void push(Character c) {
		// try to push the character if a roll is good
		if(Misc.generatePercent()>=this.baseChance) {
			// gets the target characters current direction
			c.changeDirection(this.getDirection()); // change to the direction the attacking character is facing
			c.move(); // move once valid moves are checked in the move function in the game.java
			System.out.println(this.name + " pushed "+c.name + " " + c.compass() + " one square" );
			c.turnAround();
		}
	}
	
}
