package subter;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner userInput = new Scanner(System.in);
		Game newGame = new Game(userInput, 5, 5);
		newGame.playGame();
	}


}
