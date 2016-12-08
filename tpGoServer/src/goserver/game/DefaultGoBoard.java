/**
 * 
 */
package goserver.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import goserver.game.rules.SuicideRule;
import goserver.util.IntPair;

/**
 * @author Kacper
 *
 */
public class DefaultGoBoard implements GoBoard {

	// oznaczenia kamieni na planszy
	public static final int EMPTY = 0;
	public static final int BLACK = 1;
	public static final int WHITE = 2;

	private final int size;
	protected int board[][];
	protected int previousBoard[][]; // ko rule
	private int labeledBoard[][]; // oznaczone grupy kamieni
	private Map<Integer, GoGroupType> groups; // oznaczenie grup kamieni

	private boolean suicideCheckEnabled = false;

	/**
	 * Tworzy plansze o rozmiarze (size x size).
	 * 
	 * @param size
	 */
	public DefaultGoBoard(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException();
		}
		this.size = size;
		board = new int[size][size];
		previousBoard = new int[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = EMPTY;
				previousBoard[i][j] = EMPTY;
			}
		}
		
		labeledBoard = null;
	}

	/**
	 * Kładzie kamień na polu (x,y) i dusi kamienie przeciwnika, jeśli możliwe.
	 * Zwraca liczbę uduszonych kamieni przeciwnika.
	 * 
	 * @param color
	 * @param x
	 * @param y
	 * @return (liczba zabranych jeńców, liczba jeńców zabranych przez
	 *         przeciwnika w przypadku ruchu samobójczego)
	 * @throws IllegalArgumentException
	 * @throws InvalidMoveException
	 */
	public IntPair placeStone(int color, int x, int y) throws InvalidMoveException {
		if (x < 0 || x >= size || y < 0 || y >= size) {
			throw new IllegalArgumentException();
		}
		if (board[x][y] != EMPTY) {
			throw new IllegalArgumentException();
		}
		if (color != WHITE && color != BLACK) {
			throw new IllegalArgumentException();
		}

		// sprawdzanie poprawnosci ruchow przez Game.ruleset.validateMove() przy
		// wywolaniu Game.makeMove()

		// board -> previousBoard
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				previousBoard[i][j] = board[i][j];
			}
		}

		board[x][y] = color;

		// zabierz jencow
		int captured = 0;
		if (x + 1 < size && board[x + 1][y] == getOpposingColor(color))
			captured += captureStones(x + 1, y);
		if (y + 1 < size && board[x][y + 1] == getOpposingColor(color))
			captured += captureStones(x, y + 1);
		if (x - 1 >= 0 && board[x - 1][y] == getOpposingColor(color))
			captured += captureStones(x - 1, y);
		if (y - 1 >= 0 && board[x][y - 1] == getOpposingColor(color))
			captured += captureStones(x, y - 1);

		if (suicideCheckEnabled && captured <= 0) {
			// sprawdz czy ruch jest samobojczy
			if (mockCaptureStones(x, y) != 0) {
				board[x][y] = EMPTY;
				throw new InvalidMoveException(SuicideRule.invalidMoveMessage);
			}
		}
		labeledBoard = null;

		return new IntPair(captured, captureStones(x, y));
	}

	/**
	 * Zabierz kamienie z grupy, do której należy kamień, jeśli jest udoszona.
	 * Zwraca liczbę jeńców.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int captureStones(int x, int y) {
		List<IntPair> stoneGroup = getConnectedStones(x, y);

		int liberties = 0;

		for (int i = 0; i < stoneGroup.size(); i++) {
			liberties += getLiberties(stoneGroup.get(i).x, stoneGroup.get(i).y);
		}
		if (liberties == 0) { // uduszone
			int captured = stoneGroup.size();
			for (int i = 0; i < stoneGroup.size(); i++) {
				board[stoneGroup.get(i).x][stoneGroup.get(i).y] = EMPTY;
			}
			return captured;
		} else {
			return 0;
		}
	}

	/**
	 * Sprawdz, czy grupa kamieni jest uduszona.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int mockCaptureStones(int x, int y) {
		List<IntPair> stoneGroup = getConnectedStones(x, y);

		int liberties = 0;

		for (int i = 0; i < stoneGroup.size(); i++) {
			liberties += getLiberties(stoneGroup.get(i).x, stoneGroup.get(i).y);
		}
		if (liberties == 0) { // uduszone
			return stoneGroup.size();
		} else {
			return 0;
		}
	}

	/**
	 * Zwraca liczbę oddechów kamienia na polu (x,y). (Oddech kamienia definiuje
	 * jako puste pola tuż obok kamienia).
	 * 
	 * Zwraca -1, jeśli pole (x,y) jest puste.
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @throws IllegalArgumentException
	 */
	public int getLiberties(int x, int y) throws IllegalArgumentException {
		if (x < 0 || x >= size || y < 0 || y >= size) {
			throw new IllegalArgumentException();
		}
		if (board[x][y] == EMPTY) {
			return -1;
		}
		int liberties = 0;
		if (x + 1 < size && board[x + 1][y] == EMPTY)
			liberties++;
		if (y + 1 < size && board[x][y + 1] == EMPTY)
			liberties++;
		if (x - 1 >= 0 && board[x - 1][y] == EMPTY)
			liberties++;
		if (y - 1 >= 0 && board[x][y - 1] == EMPTY)
			liberties++;
		return liberties;
	}

	public int getOpposingColor(int color) {
		switch (color) {
		case BLACK:
			return WHITE;
		case WHITE:
			return BLACK;
		default:
			return EMPTY;
		}
	}

	public List<IntPair> getConnectedStones(int x, int y) {
		// algorytm floodfill
		final int width = getSize();
		final int height = getSize();

		Set<IntPair> result = new HashSet<IntPair>();
		List<IntPair> queue = new ArrayList<IntPair>();

		final int color = board[x][y];

		queue.add(new IntPair(x, y));

		int i, cur_x, cur_y;

		while (!queue.isEmpty()) {
			cur_x = queue.get(0).x;
			cur_y = queue.get(0).y;

			// obecne pole
			result.add(new IntPair(cur_x, cur_y));
			if (cur_y - 1 >= 0 && board[cur_x][cur_y - 1] == color) {
				if (result.add(new IntPair(cur_x, cur_y - 1)))
					queue.add(new IntPair(cur_x, cur_y - 1));
			}
			if (cur_y + 1 < height && board[cur_x][cur_y + 1] == color) {
				if (result.add(new IntPair(cur_x, cur_y + 1)))
					queue.add(new IntPair(cur_x, cur_y + 1));
			}
			// w lewo
			i = 1;
			while (cur_x - i >= 0) {
				if (board[cur_x - i][cur_y] == color) {
					result.add(new IntPair(cur_x - i, cur_y));
					if (cur_y - 1 >= 0 && board[cur_x - i][cur_y - 1] == color) {
						if (result.add(new IntPair(cur_x - i, cur_y - 1)))
							queue.add(new IntPair(cur_x - i, cur_y - 1));
					}
					if (cur_y + 1 < height && board[cur_x - i][cur_y + 1] == color) {
						if (result.add(new IntPair(cur_x - i, cur_y + 1)))
							queue.add(new IntPair(cur_x - i, cur_y + 1));
					}
					i++;
				} else {
					break;
				}
			}
			// w prawo
			i = 1;
			while (cur_x + i < width) {
				if (board[cur_x + i][cur_y] == color) {
					result.add(new IntPair(cur_x + i, cur_y));
					if (cur_y - 1 >= 0 && board[cur_x + i][cur_y - 1] == color) {
						if (result.add(new IntPair(cur_x + i, cur_y - 1)))
							queue.add(new IntPair(cur_x + i, cur_y - 1));
					}
					if (cur_y + 1 < height && board[cur_x + i][cur_y + 1] == color) {
						if (result.add(new IntPair(cur_x + i, cur_y + 1)))
							queue.add(new IntPair(cur_x + i, cur_y + 1));
					}
					i++;
				} else {
					break;
				}
			}
			queue.remove(0);
		}

		return new ArrayList<IntPair>(result);
	}

	//@Override
	private void createLabels() {
		// algorytm two-pass
		labeledBoard = new int[size][size];
		Map<Integer, Set<Integer>> labels = new HashMap<Integer, Set<Integer>>();
		int lowestLabel = 0;
		
		int[] neighbors = new int[2];
		
		// 1st pass
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				if(board[i][j] != EMPTY){
					neighbors[0]=0;
					neighbors[1]=0;
					// 0th neighbor
					if(i>0 && board[i-1][j] == board[i][j]){
						neighbors[0] = labeledBoard[i-1][j];
					}
					// 1st neighbor
					if(j>0 && board[i][j-1] == board[i][j]){
						neighbors[1] = labeledBoard[i][j-1];
					}
					
					if(neighbors[0] > 0 && neighbors[1] > 0){
						// two neighbors
						if (neighbors[0] != neighbors[1]){
							// different labels
							labeledBoard[i][j] = Math.min(neighbors[0], neighbors[1]);
							// store label equivalence
							labels.get(neighbors[0]).add(neighbors[1]);
							labels.get(neighbors[1]).add(neighbors[0]);
						} else {
							// same labels
							labeledBoard[i][j] = neighbors[0];
						}
					} else if(neighbors[0] > 0 && neighbors[1] == 0){
						// one neighbor
						labeledBoard[i][j] = neighbors[0];
					} else if(neighbors[0] == 0 && neighbors[1] > 0){
						// one neighbor
						labeledBoard[i][j] = neighbors[1];
					} else {
						// no neighbor -> new label
						lowestLabel++;
						labeledBoard[i][j] = lowestLabel;
						labels.put(lowestLabel, new HashSet<Integer>());
						labels.get(lowestLabel).add(lowestLabel);
					}
					
				} else {
					labeledBoard[i][j] = 0;
				}
			}
		}
		
		//2nd pass
		for(int i=0; i<labeledBoard.length; i++){
			for(int j=0; j<labeledBoard[i].length; j++){
				if(labeledBoard[i][j] != 0){
					// find lowest equivalent label
					labeledBoard[i][j] = Collections.min(labels.get(labeledBoard[i][j]));
				}
			}
		}
		
		//3rd pass (etykiety kolejnymi liczbami naturalnymi od 1)
		Map<Integer, Integer> newLabels = new HashMap<Integer, Integer>();
		groups = new HashMap<Integer, GoGroupType>();
		lowestLabel = 0;
		
		for(int i=0; i<labeledBoard.length; i++){
			for(int j=0; j<labeledBoard[i].length; j++){
				if(labeledBoard[i][j] != 0){
					if (newLabels.containsKey(labeledBoard[i][j])){
						labeledBoard[i][j] = newLabels.get(labeledBoard[i][j]);
					} else {
						lowestLabel++;
						newLabels.put(labeledBoard[i][j], lowestLabel);
						labeledBoard[i][j] = lowestLabel;
						groups.put(lowestLabel, GoGroupType.ALIVE);
					}
				}
			}
		}
		
		//TODO jakies madrzejsze ustalanie czy grupa jest zywa/martwa
		
	}
	
	@Override
	public int[][] getBoardWithLabeledGroups() {
		if (labeledBoard == null){
			createLabels();
		}
		return labeledBoard;
	}
	
	@Override
	public int[] getAllGroupLabels() {
		if (labeledBoard == null){
			createLabels();
		}
		
		int labels[] = new int[groups.keySet().size()];
		int i = 0;
		for(int label: groups.keySet()){
			labels[i] = label;
			i++;
		}
		
		return labels;
	}

	@Override
	public GoGroupType getGroupType(int label) {
		if (labeledBoard == null){
			createLabels();
		}
		
		return groups.get(label);
	}

	@Override
	public void setGroupType(int label, GoGroupType type) {
		if (labeledBoard == null){
			createLabels();
		}
		
		groups.put(label, type);
		
	}

	public int getSize() {
		return size;
	}

	public int[][] getBoard() {
		return board;
	}

	public int[][] getPreviousBoard() {
		return previousBoard;
	}

	@Override
	public int getBlackColor() {
		return BLACK;
	}

	@Override
	public int getWhiteColor() {
		return WHITE;
	}

	@Override
	public int getEmptyColor() {
		return EMPTY;
	}

	@Override
	public void setSuicideCheckEnabled(boolean checkEnabled) {
		suicideCheckEnabled = checkEnabled;
	}

}
