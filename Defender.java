package subter;

import Misc.Misc;

public abstract class Defender extends Character{
	
	protected double chanceToBlock;
	protected int minBlock;
	protected int maxBlock;

	public Defender(Board board, String name, int max_hp, int x_coord, int y_coord, double base_chance, int do_damage_max,
		int do_damage_min, int direction, double chanceToBlockVal, int minBlockVal, int maxBlockVal) {
		super(board, name, max_hp, x_coord, y_coord,  base_chance, do_damage_max, do_damage_min, direction);
		this.chanceToBlock = chanceToBlockVal;
		this.maxBlock = maxBlockVal;
		this.minBlock = minBlockVal;
		this.charicterType="Defender";
	};

	// Character is computer generated
	public Defender() {
		super();
		this.chanceToBlock =.4;
		this.maxBlock=1;
		this.minBlock=4;		
		this.charicterType="Defender";
	}
	

 /*****************************************************************************************************
 * Class specific over ride functions
 */
	
	public void hit(int damage) {
		// calls the hit function from the character class
		super.hit(damage);
		int blockAmount=0;
		// if the character is alive then do stuff
		if(super.isAlive()) {
			// stuff being rolling to see if they block any damage
			if(super.actionRoll(this.chanceToBlock) && damage > 0 ) {
				blockAmount=Misc.generateRandomInt(this.minBlock, this.maxBlock);
				// we only print out an info blip if the block was successful
				if(blockAmount>0) {
					System.out.println(this.getName()+" successfully blocked for "+ blockAmount);
				}
				super.hit(-1*blockAmount);
			} 
		}
	}
	

}
