package clientmock;

import static org.junit.Assert.assertTrue;

import java.util.InputMismatchException;
import java.util.Scanner;

import goserver.game.DefaultGoGame;
import goserver.game.DefaultGoRuleset;
import goserver.game.GoGame;
import goserver.game.GoPlayer;
import goserver.game.rules.GoRuleset;
import goserver.game.rules.KoRule;
import goserver.game.rules.SuicideRule;
import goserver.randombot.RandomGoBot;

public class TextGo {
	
	public static void playWithPlayer(int size, TextGoPlayer player1, Scanner in){
		TextGoPlayer player2 = new TextGoPlayer(2);
		GoGame game = new DefaultGoGame(player1, player2, size, DefaultGoRuleset.getDefaultRuleset());
		TextGoPlayer curPlayer;
		
		while (true) {
			if (game.isPlayersTurn(player1)) {
				curPlayer = player1;
			} else {
				curPlayer = player2;
			}
			curPlayer.drawBoard();
			System.out
					.println("Player 1: " + player1.getCapturedStones() + " Player 2: " + player2.getCapturedStones());
			System.out.println("Player " + curPlayer.playerId + "'s Turn");
			try {
				try {
					int x = in.nextInt();
					int y = in.nextInt();
					in.nextLine();
					curPlayer.makeMove(x, y);
					System.out.println("correct move");
				} catch (InputMismatchException ime) {
					String line = in.next();
					in.nextLine();
					if (line.contains("pass")) {
						curPlayer.passTurn();
					}
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
	}
	
	public static void playWithBot(int size, TextGoPlayer player1, Scanner in){
		RandomGoBot player2 = new RandomGoBot();
		GoGame game = new DefaultGoGame(player1, player2, size, DefaultGoRuleset.getDefaultRuleset());
		
		while (true) {
			if (game.isPlayersTurn(player1)) {
				player1.drawBoard();
				System.out
						.println("Player 1: " + player1.getCapturedStones() + " Player 2: " + game.getPlayersCapturedStones(player2));
				System.out.println("Player 1's Turn");
				try {
					try {
						int x = in.nextInt();
						int y = in.nextInt();
						in.nextLine();
						player1.makeMove(x, y);
						System.out.println("correct move");
					} catch (InputMismatchException ime) {
						String line = in.next();
						in.nextLine();
						if (line.contains("pass")) {
							player1.passTurn();
						}
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}

		}
	}

	public static void main(String[] args) {
		TextGoPlayer player1 = new TextGoPlayer(1);
		int size = 9;
		
		Scanner in = new Scanner(System.in);
		
		int choice = 0;
		while(choice == 0){
			String line = in.next();
			in.nextLine();
			if (line.contains("player")) {
				choice = 1;
			} else if (line.contains("bot")) {
				choice = 2;
			}
		}

		if (choice == 1){
			playWithPlayer(size, player1, in);
		} else if (choice == 2){
			playWithBot(size, player1, in);
		}


	}

}
