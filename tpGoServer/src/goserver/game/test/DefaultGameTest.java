package goserver.game.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import goserver.game.DefaultGoGame;
import goserver.game.GoGame;
import goserver.game.GoGroupType;
import goserver.game.GoMoveType;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;
import goserver.game.rules.GoRuleset;
import goserver.util.MatrixUtil;

public class DefaultGameTest {

	@Test
	public void testCreateEmptyBoard() {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		assertTrue(MatrixUtil.compareMatrix(game.getBoard().getBoard(), new int[size][size]));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongBoardSize() {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = -1;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
	}

	@Test
	public void testRightPlayers() {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		assertEquals(player1, game.getPlayer1());
		assertEquals(player2, game.getPlayer2());
	}

	@Test
	public void testMakeMoves() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		game.makeMove(player1, 0, 0);
		assertEquals(game.getBoard().getBlackColor(), game.getBoard().getBoard()[0][0]);
		game.makeMove(player2, 0, 1);
		assertEquals(game.getBoard().getWhiteColor(), game.getBoard().getBoard()[0][1]);
	}

	@Test(expected = InvalidMoveException.class)
	public void testConflictingMoves() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		game.makeMove(player1, 0, 0);
		game.makeMove(player2, 0, 0);
	}
	
	@Test
	public void testPassTurn() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		assertTrue(game.isPlayersTurn(player1));
		game.passTurn(player1);
		assertTrue(game.isPlayersTurn(player2));
	}
	
	@Test
	public void testConsecutivePassEndsGame() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		game.passTurn(player1);
		game.passTurn(player2);
		assertTrue(game.isGroupMarkingPhase());
		game.applyGroupTypeChanges(player1, new HashMap<Integer, GoGroupType>());
		assertTrue(game.isGameEnd());
	}
	
	@Test
	public void testOneStoneWinsGame() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		game.makeMove(player1, 0 ,0);
		game.passTurn(player2);
		game.passTurn(player1);
		assertTrue(game.isGroupMarkingPhase());
		game.applyGroupTypeChanges(player2, new HashMap<Integer, GoGroupType>());
		game.applyGroupTypeChanges(player1, new HashMap<Integer, GoGroupType>());
		assertTrue(game.isGameEnd());
		assertTrue(game.getPlayersScore(player1) > game.getPlayersScore(player2));
	}
	
	@Test
	public void testNeutralTerritoryEqualsZeroPoints() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		game.makeMove(player1, 0, 0);
		game.makeMove(player2, 1, 1);
		game.passTurn(player1);
		game.passTurn(player2);
		assertTrue(game.isGroupMarkingPhase());
		game.applyGroupTypeChanges(player1, new HashMap<Integer, GoGroupType>());
		game.applyGroupTypeChanges(player2, new HashMap<Integer, GoGroupType>());
		assertTrue(game.isGameEnd());
		assertTrue(game.getPlayersScore(player1) == 0); // cale terytorium niczyje
		assertTrue(game.getPlayersScore(player2) == 0);
	}
	
	@Test
	public void testDeadStoneRemoval() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		game.makeMove(player1, 0, 0);
		game.makeMove(player2, 1, 1);
		game.passTurn(player1);
		game.passTurn(player2);
		assertTrue(game.isGroupMarkingPhase());
		Map<Integer, GoGroupType> changes = new HashMap<Integer, GoGroupType>();
		changes.put(game.getBoard().getBoardWithLabeledGroups()[1][1], GoGroupType.DEAD);
		game.applyGroupTypeChanges(player1, changes);
		game.applyGroupTypeChanges(player2, new HashMap<Integer, GoGroupType>());
		assertTrue(game.isGameEnd());
		assertTrue(game.getPlayersScore(player1) > game.getPlayersScore(player2)); // cale terytorium nalezy do gracza 1
	}
	
	@Test
	public void testResumeGameAfterGroupMarkingDisagreement() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		game.makeMove(player1, 0, 0);
		game.makeMove(player2, 1, 1);
		game.passTurn(player1);
		game.passTurn(player2);
		assertTrue(game.isGroupMarkingPhase());
		
		game.getLabelsMap();
		Map<Integer, GoGroupType> changes1 = new HashMap<Integer, GoGroupType>();
		changes1.put(game.getBoard().getBoardWithLabeledGroups()[1][1], GoGroupType.DEAD);
		Map<Integer, GoGroupType> changes2 = new HashMap<Integer, GoGroupType>();
		changes2.put(game.getBoard().getBoardWithLabeledGroups()[1][1], GoGroupType.ALIVE);
		
		GoPlayer curPlayer = player1;
		Map<Integer, GoGroupType> curChanges = changes1;
		
		for(int i=0; i < DefaultGoGame.MAX_GROUP_MARKING_PHASE_LENGTH; i++){
			game.applyGroupTypeChanges(curPlayer, curChanges); // zmiana typu jednej grupy na przemian
			
			curPlayer = (curPlayer == player1 ? player2 : player1);
			curChanges = (curChanges == changes1 ? changes2 : changes1);
		}
		assertTrue(game.isStonePlacingPhase());
	}

}
