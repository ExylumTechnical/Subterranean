package subter;

public interface iCharacter {
	public void move();
	public int[][] attack();
	public void changeDirection(int direction);
	public int[] getLocation();
	public void hit(int damage);
	public boolean isAlive();
	public boolean actionRoll(double chance);

	public int getDirection();
	public String getName();
	public char getSymbol();
	public void printStats();

	public void setName(String newName);




}
