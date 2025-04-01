package subter;

import Misc.Misc;

public class Medic extends Defender {

		private double chanceToHeal;
		private double chanceToDamageMedkit;
		private int minHeal;
		private int maxHeal;
		private int threshold;
		private int medkits;
		
		public Medic(Board board,String name, int max_hp, int x_coord, int y_coord, double base_chance, int do_damage_max,
				int do_damage_min, int direction){
			// warrior does not regenerate
			super(board, name, max_hp, x_coord, y_coord,  base_chance, do_damage_max, do_damage_min, direction,0,0,0); // does not regenerate because they can power atttack
			this.charicterClass="Medic";
			this.minHeal=1;
			this.maxHeal=10;
			this.threshold=5;
			this.chanceToHeal=.6;
			this.baseChance = base_chance;
			this.chanceToDamageMedkit=.20;
			this.medkits=3;
			this.maxDamage=3; // does less damage to nerf the heal power
		}
		
		public Medic() {
			super();
			this.charicterClass="Medic";
			this.minHeal=1;
			this.maxHeal=10;
			this.threshold=5;
			this.chanceToHeal=.6;
			this.chanceToDamageMedkit=.20;
			this.medkits=3;
			this.maxDamage=3; // does less damage to nerf the heal power
		}
		
		
		private boolean healDecision(Character c) {
			// determines if the players hp is below the threashold or not and returns a boolean expression
			double healthPercent = c.getHealth() / (double)c.getMaxHealth();
			return healthPercent <= this.threshold;
		}
		
		private void medkitDamaged() {
			if(super.actionRoll(chanceToDamageMedkit)) {
				System.out.println(this.getName()+" failed to effectively use the medkit, but the medkit was not ruined.");
			} else {
				System.out.println(this.getName()+" failed to effectively use the medkit and the medkit was ruined.");
				this.medkits--;
			}
		}
		
		public void useMedkit(Character c) {
			// if this character has a medkit left and the character is below the medicare threshold
			if(healDecision(c) && this.medkits>0) {
				// if they successfully heal the character
				if(super.actionRoll(chanceToHeal)) {
					int heal = Misc.generateRandomInt(this.minHeal,this.maxHeal);
					System.out.println(this.getName()+" was patched up for "+ heal + " hp");
					// preform healing action and reduce the number of medkits
					super.hit((-heal));
					this.medkits--;
				} else {
					medkitDamaged();
				}
			} else if (this.medkits<1) {
				System.out.println(this.getName()+" does not have any more medkits left");				
			} else {
				System.out.println(c.getName()+" is not close enough to death to use a medkit on");
			}
		
			
		}
		@Override
		public int [][] attack() {
			// adds the option to heal instead of attack
			int [][] solution = super.attack();
			return solution;
		}
		@Override
		public void printStats() {
			System.out.println(this.name+" has "+this.medkits + " medkits left.");
			System.out.println(this.name+" has "+this.currentHp + " hp left and is facing "+this.compass());
		}
		
		
	
}
