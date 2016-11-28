package clientmock;

import static org.junit.Assert.assertTrue;

import java.util.Scanner;

import goserver.game.DefaultGoGame;
import goserver.game.GoGame;
import goserver.game.GoPlayer;
import goserver.game.GoRuleset;

public class TextGo {

	public static void main(String[] args) {
		TextGoPlayer player1 = new TextGoPlayer(1);
		TextGoPlayer player2 = new TextGoPlayer(2);
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		TextGoPlayer curPlayer;
		
		Scanner in = new Scanner(System.in);
		
		while(true){
			if(game.isPlayersTurn(player1)){
				curPlayer = player1;
			} else {
				curPlayer = player2;
			}
			curPlayer.drawBoard();
			System.out.println("Player 1: " + player1.getCapturedStones() + " Player 2: " + player2.getCapturedStones());
			System.out.println("Player " + curPlayer.playerId + "'s Turn");
			try{
				int x = in.nextInt();
				int y = in.nextInt();
				curPlayer.makeMove(x, y);
				System.out.println("correct move");

			} catch (Exception e){
				System.out.println(e.getMessage());
			}

		}
	}

}
