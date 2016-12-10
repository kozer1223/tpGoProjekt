package clientmock;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import goserver.game.DefaultGoGame;
import goserver.game.DefaultGoRuleset;
import goserver.game.GoGame;
import goserver.game.GoGroupType;
import goserver.game.GoMoveType;
import goserver.game.GoPlayer;
import goserver.game.rules.GoRuleset;
import goserver.game.rules.KoRule;
import goserver.game.rules.SuicideRule;
import goserver.randombot.RandomGoBot;

public class TextGo {
	
	public static void printMap(Map<Integer, GoGroupType> map) {
		for(Integer key : map.keySet()){
			System.out.println(key + " -> " + map.get(key).toString());
		}
	}
	
	public static void printMap(Map<Integer, GoGroupType> map, GoGame game) {
		for(Integer key : map.keySet()){
			System.out.print(key + " -> " + map.get(key).toString());
			if (game.getBoard().checkIfGroupIsLocked(key)){
				System.out.print(" LK");
			}
			System.out.println();
		}
	}
	
	public static void playWithPlayer(int size, TextGoPlayer player1, Scanner in){
		TextGoPlayer player2 = new TextGoPlayer(2);
		GoGame game = new DefaultGoGame(player1, player2, size, DefaultGoRuleset.getDefaultRuleset());
		TextGoPlayer curPlayer;
		Map<Integer, GoGroupType> groupTypeChanges = new HashMap<Integer, GoGroupType>();
		
		while (true) {
			if (game.isPlayersTurn(player1)) {
				curPlayer = player1;
			} else {
				curPlayer = player2;
			}
			curPlayer.drawBoard();
			System.out
					.println("Player 1: " + player1.getCapturedStones() + " Player 2: " + player2.getCapturedStones());
			System.out.println("Player " + curPlayer.playerId + "'s Turn, Phase " + curPlayer.getGamePhase());
			
			try {
				if (curPlayer.game.isStonePlacingPhase()){
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
				} else {
					printMap(game.getLabelsMap(), game);
					printMap(groupTypeChanges);
					try {
						int x = in.nextInt();
						String y = in.next();
						if (y.contains("a")) {
							groupTypeChanges.put(x, GoGroupType.ALIVE);
							System.out.println("added");
						} else if (y.contains("d")) {
							groupTypeChanges.put(x, GoGroupType.DEAD);
							System.out.println("added");
						}
						in.nextLine();

					} catch (InputMismatchException ime) {
						String line = in.next();
						in.nextLine();
						if (line.contains("apply")) {
							curPlayer.applyGroupTypeChanges(groupTypeChanges);
							groupTypeChanges.clear();
						} else if (line.contains("reset")) {
							groupTypeChanges.clear();
						} else if (line.contains("pass")) {
							groupTypeChanges.clear();
							curPlayer.applyGroupTypeChanges(groupTypeChanges);
						}
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
