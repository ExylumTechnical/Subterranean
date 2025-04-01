package subter;

public class Brute extends Attacker {
	private double chanceToPowerHit;
	private double damageModifier;
	public Brute(Board board,String name, int max_hp, int x_coord, int y_coord, double base_chance, int do_damage_max,
			int do_damage_min, int direction){
		// warrior does not regenerate
		super(board, name, max_hp, x_coord, y_coord,  base_chance, do_damage_max, do_damage_min, direction,0,0,0); // does not regenerate because they can power atttack
		this.charicterClass="Warrior";

		this.maxHp=20;
		this.maxDamage=5;
		this.minDamage=0;
		this.chanceToPowerHit=.6;
		this.damageModifier=1.1;
	
	}
	
	public Brute() {
		super();

		this.chanceToPowerHit=.20;

	}
	
	
	
	@Override
	public int[][] attack(){
		int[][] solution= super.attack();
		int damage = solution[0][2];
		// we check to see if this is a power hit or not.
		if(super.actionRoll(this.chanceToPowerHit) && damage>0) {
			damage= (int) (solution[0][2]*this.damageModifier);
			solution[0][2]=damage;
		}
		else {
			solution=super.attack();
		}
		return solution;
	}



}






