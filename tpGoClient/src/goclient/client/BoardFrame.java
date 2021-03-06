/**
 * 
 */
package goclient.client;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import goclient.util.DoublePair;
import goclient.util.IntPair;

/**
 * @author Maciek
 *
 */
public class BoardFrame implements ActionListener {

	private JFrame frame;
	private int size;
	private String color = "";
	private ReaderWriter communication;
	private ClientRequestSender sender = ClientRequestSender.getInstance();
	private int phase = 0;
	private boolean waitingForRematch = false;
	private boolean isDisabled = false;

	// board data
	Ellipse2D[][] stones;
	Color[][] stoneColors;
	int[][] groupLabels;
	Map<Integer, GoGroupType> groupStates;
	List<Integer> lockedGroups;

	// GUI elements
	private BoardCanvas canvas;
	private JButton proposeChangesButton;
	private JButton passButton;
	private JFrame waitingFrame;
	private StatusJPanel statusPanel;
	private TextArea messageArea;

	public BoardFrame(int size) {
		communication = GUI.getCommunication();
		this.size = size;
		frame = new JFrame("Go");
		frame.setLayout(new GridLayout(1, 1));
		frame.setVisible(false);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		frame.add(mainPanel);

		stones = new Ellipse2D[size][size];
		stoneColors = new Color[size][size];
		canvas = new BoardCanvas(size);

		// canvas
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(canvas, c);
		canvas.setLayout(null);

		// game status panel
		statusPanel = new StatusJPanel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(statusPanel, c);

		// pass and propose changes button
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(buttonPanel, c);

		passButton = new JButton("PASS");
		buttonPanel.add(passButton);
		passButton.setOpaque(true);
		passButton.setContentAreaFilled(true);
		passButton.setBorderPainted(true);

		passButton.addActionListener(this);

		proposeChangesButton = new JButton("Propose Changes");
		buttonPanel.add(proposeChangesButton);
		proposeChangesButton.setEnabled(false);
		proposeChangesButton.addActionListener(this);

		// invalid move message box
		messageArea = new TextArea("", 1, 15, TextArea.SCROLLBARS_NONE);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		mainPanel.add(messageArea, c);
		messageArea.setEditable(false);

		ReadingThread thread = new ReadingThread(size, this);
		thread.setCommunication(GUI.getCommunication());
		thread.start();

		groupLabels = null;
		groupStates = null;
		lockedGroups = null;

		// waiting for a player window
		waitingFrame = new JFrame("Please Wait");
		waitingFrame.setVisible(true);
		waitingFrame.setBounds(800, 300, 300, 120);
		JLabel waitLabel = new JLabel("Waiting for a player...");
		waitLabel.setHorizontalAlignment(JLabel.CENTER);
		waitingFrame.getContentPane().add(waitLabel);
		waitingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();
		if (color.equals(ServerClientProtocol.getInstance().BLACK)) {
			// black starts
			enableInput();
		} else {
			disableInput();
		}
	}

	/**
	 * @return Current game phase.
	 */
	public int getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 *            Phase (0 - stone placing, 1 - group marking)
	 */
	public void setPhase(int phase) {
		this.phase = phase;
		if (phase == 1) {
			if (!isDisabled) {
				proposeChangesButton.setEnabled(true);
			}
		} else {
			proposeChangesButton.setEnabled(false);
			groupLabels = null;
			groupStates = null;
			lockedGroups = null;
		}
		canvas.repaint();
	}

	/**
	 * @param color
	 *            Player's color as String.
	 */
	public void setColor(String color) {
		this.color = color;
		statusPanel.setColor(color);
	}

	/**
	 * @param pair
	 *            (Stones captured by Black, stones captured by White)
	 */
	public void setCapturedStones(IntPair pair) {
		statusPanel.setCaptured(pair.x, pair.y);
	}

	/**
	 * @param message
	 *            Invalid move message.
	 */
	public void setMessage(String message) {
		messageArea.setText(message);
	}

	/**
	 * @return true if player is currently waiting for a rematch.
	 */
	public boolean isWaitingForRematch() {
		return waitingForRematch;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isWaitingForRematch() && !isDisabled) {
			if (((JButton) e.getSource()).equals(passButton)) {
				// pass and wait for other player's turn
				disableInput();
				sender.passTurn(communication);
				setMessage("Waiting...");
			} else if (((JButton) e.getSource()).equals(proposeChangesButton)) {
				// propose changes and wait for other player's turn
				disableInput();
				sender.sendGroupChanges(groupStates, communication);
				setMessage("Waiting...");
			}
		}
	}

	/**
	 * Method called when there are two players and the game begins.
	 */
	public void beginGame() {
		frame.setVisible(true);
		if (waitingFrame != null) {
			waitingFrame.setVisible(false);
			waitingFrame = null;
		}
	}

	/**
	 * Shows a window with received score.
	 * 
	 * @param score
	 *            (Black's score, White's score)
	 */
	public void showScore(DoublePair score) {
		boolean victory;
		double yourScore;
		double opponentsScore;
		if (color.equals(ServerClientProtocol.getInstance().BLACK)) {
			yourScore = score.x;
			opponentsScore = score.y;
		} else {
			yourScore = score.y;
			opponentsScore = score.x;
		}

		victory = (yourScore > opponentsScore);
		boolean tie = (yourScore == opponentsScore);

		StringBuilder message = new StringBuilder();
		if (tie) {
			message.append("You tied.\n");
		} else if (victory) {
			message.append("You won.\n");
		} else {
			message.append("You lost.\n");
		}
		message.append("You: " + yourScore + " Opponent: " + opponentsScore + "\n");
		message.append("Do you want a rematch?");
		int choice = JOptionPane.showConfirmDialog(frame, message, "Rematch", JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION) {
			// request rematch
			sender.requestRematch(communication);
			waitingForRematch = true;
			setMessage("Waiting for rematch!");
			passButton.setEnabled(false);
			proposeChangesButton.setEnabled(false);
		} else {
			frame.setVisible(false);
			frame.dispose();
			System.exit(0); // quit
		}
	}

	/**
	 * Method called when opponent denies a rematch request.
	 */
	public void rematchDenied() {
		JOptionPane.showMessageDialog(frame, "Rematch denied.");
		frame.setVisible(false);
		frame.dispose();
		System.exit(0); // quit
	}

	/**
	 * Method caled when a rematch request is accepted.
	 */
	public void rematchAccepted() {
		waitingForRematch = false;
		JOptionPane.showMessageDialog(frame, "Rematch accepted.");
		resetGame();
	}

	/**
	 * Reset game data upon a start of a rematch.
	 */
	private void resetGame() {
		stones = new Ellipse2D[size][size];
		stoneColors = new Color[size][size];
		groupLabels = null;
		groupStates = null;
		lockedGroups = null;
		setMessage("");
		setPhase(0);
		canvas.repaint();
		if (color.equals(ServerClientProtocol.getInstance().BLACK)) {
			enableInput();
		} else {
			disableInput();
		}
	}

	/**
	 * Disable all buttons.
	 */
	public void disableInput() {
		isDisabled = true;
		passButton.setEnabled(false);
		proposeChangesButton.setEnabled(false);
	}

	/**
	 * Enable input buttons depending on the current phase.
	 */
	public void enableInput() {
		isDisabled = false;
		passButton.setEnabled(true);
		if (getPhase() == 1) {
			proposeChangesButton.setEnabled(true);
		}

	}

	/**
	 * Panel containing a graphical representation of the board with buttons for
	 * placing stones and marking groups.
	 * 
	 * @author Kacper
	 *
	 */
	private class BoardCanvas extends JPanel implements ActionListener {

		private static final long serialVersionUID = -3929397568035091522L;
		BufferedImage img;
		int size;

		/**
		 * Create canvas for a board of given size.
		 * 
		 * @param size
		 *            Board size.
		 */
		public BoardCanvas(int size) {
			super();
			this.size = size;
			img = null;
			try {
				if (size == 19) {
					frame.setBounds(100, 0, 570, 800);
					img = ImageIO.read(new File("resources/go.jpg"));
				} else if (size == 13) {
					frame.setBounds(100, 0, 395, 650);
					img = ImageIO.read(new File("resources/go13.png"));
				} else if (size == 9) {
					frame.setBounds(100, 0, 270, 550);
					img = ImageIO.read(new File("resources/go9.png"));
				}
			} catch (IOException e) {
				System.err.println("Error: Resources not found.");
				System.exit(1);
			}
			this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
			this.setMinimumSize(new Dimension(img.getWidth(), img.getHeight()));
			this.setMaximumSize(new Dimension(img.getWidth(), img.getHeight()));

			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					StonePlaceButton button = new StonePlaceButton();
					button.setBounds(29 * i, 29 * j, 29, 29);
					button.addActionListener(this);
					button.setOpaque(false);
					button.setContentAreaFilled(false);
					button.setBorderPainted(false);
					button.setX(i);
					button.setY(j);
					this.add(button);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(img, 0, 0, null);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (stones[i][j] != null) {
						g2.setColor(stoneColors[i][j]);
						g2.fill(stones[i][j]);
						if (getPhase() == 1 && groupLabels != null && groupStates != null) {
							Color borderColor = (groupStates.get(groupLabels[i][j]) == GoGroupType.ALIVE ? Color.GREEN
									: Color.RED);
							if (isGroupLocked(groupLabels[i][j])) {
								// ciemniejszy kolor dla zablokowanych
								borderColor = (borderColor == Color.GREEN ? new Color(0, 80, 0) : new Color(80, 0, 0));
							}

							g2.setColor(borderColor);
							g2.setStroke(new BasicStroke(2));
							g2.draw(stones[i][j]);
						}
					}
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isWaitingForRematch() & !isDisabled) {
				StonePlaceButton button = (StonePlaceButton) e.getSource();
				int x = button.getX();
				int y = button.getY();

				if (phase == 0) {
					// send move and wait for server reply
					disableInput();
					ClientRequestSender.getInstance().sendMove(x, y, communication);
					setMessage("Waiting...");
				} else if (phase == 1 && groupLabels != null) {
					// group marking phase, so toggle group state
					int label = groupLabels[x][y];
					if (!isGroupLocked(label)) {
						toggleGroupState(label);
					}

				}
			}
		}

		private class StonePlaceButton extends JButton {
			private static final long serialVersionUID = 1L;
			private int x;
			private int y;

			public void setX(int x) {
				this.x = x;
			}

			public void setY(int y) {
				this.y = y;
			}

			public int getY() {
				return y;
			}

			public int getX() {
				return x;
			}
		}

	}

	/**
	 * Update board graphic with given board data.
	 * 
	 * @param board
	 *            Board data.
	 */
	public void drawBoard(int board[][]) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board[i][j] == 0) {
					if (stones[i][j] != null) {
						stones[i][j] = null;
						stoneColors[i][j] = null;
					}
				} else {
					if (stones[i][j] == null) {
						stones[i][j] = new Ellipse2D.Double(29 * i, 29 * j, 29, 29);
					}
					if (board[i][j] == 1) {
						stoneColors[i][j] = Color.BLACK;
					} else if (board[i][j] == 2) {
						stoneColors[i][j] = Color.WHITE;
					}
				}
			}
		}
		canvas.repaint();
	}

	/**
	 * Set group labels.
	 * 
	 * @param labeledBoard
	 *            Labeled board data.
	 */
	public void setGroupLabels(int[][] labeledBoard) {
		this.groupLabels = labeledBoard;
		canvas.repaint();
	}

	/**
	 * Set group states.
	 * 
	 * @param groupStates
	 *            Group states map.
	 */
	public void setGroupStates(Map<Integer, GoGroupType> groupStates) {
		this.groupStates = groupStates;
		canvas.repaint();
	}

	/**
	 * Set locked groups.
	 * 
	 * @param lockedGroups
	 *            List of locked groups.
	 */
	public void setLockedGroups(List<Integer> lockedGroups) {
		this.lockedGroups = lockedGroups;
		canvas.repaint();
	}

	/**
	 * Get state of a group with a given label.
	 * 
	 * @param label
	 *            Group label.
	 * @return Group's state.
	 */
	public GoGroupType getGroupState(int label) {
		if (groupStates != null) {
			return groupStates.get(label);
		}
		return null;
	}

	/**
	 * Change group state and redraw the canvas.
	 * 
	 * @param label
	 *            Group label.
	 * @param state
	 *            New state.
	 */
	public void setGroupState(int label, GoGroupType state) {
		if (groupStates != null) {
			groupStates.put(label, state);
		}
		canvas.repaint();
	}

	/**
	 * Check if a group is locked.
	 * 
	 * @param label
	 *            Group label.
	 * @return true if group is locked.
	 */
	public boolean isGroupLocked(int label) {
		if (lockedGroups != null) {
			return lockedGroups.contains(label);
		}
		return false;
	}

	/**
	 * Toggle group state of a group with the given label.
	 * 
	 * @param label
	 *            Group label.
	 */
	public void toggleGroupState(int label) {
		GoGroupType state;
		if ((state = getGroupState(label)) != null) {
			state = (state == GoGroupType.ALIVE ? GoGroupType.DEAD : GoGroupType.ALIVE);
			setGroupState(label, state);
		}
	}

	/**
	 * Game status panel. Display information about each player's captured
	 * stones, as well as information about client's color.
	 * 
	 * @author Kacper
	 *
	 */
	/**
	 * @author Kacper
	 *
	 */
	private class StatusJPanel extends JPanel {
		// displays "You" or "Opponent" depending on player's color
		JLabel blackPlayer;
		JLabel whitePlayer;
		// displays each player's number of captured stones.
		JLabel blackCaptured;
		JLabel whiteCaptured;

		final String YOU = "You";
		final String OPPONENT = "Opponent";
		final String CAPTURED = "Captured: ";

		public StatusJPanel() {
			setLayout(new GridLayout(1, 2, 5, 5));

			JPanel blackPanel = new JPanel(new GridLayout(3, 1));
			JPanel whitePanel = new JPanel(new GridLayout(3, 1));
			add(blackPanel);
			add(whitePanel);

			JLabel blackName = new JLabel("Black");
			blackName.setHorizontalAlignment(JLabel.CENTER);
			JLabel whiteName = new JLabel("White");
			whiteName.setHorizontalAlignment(JLabel.CENTER);
			blackPlayer = new JLabel();
			blackPlayer.setHorizontalAlignment(JLabel.CENTER);
			whitePlayer = new JLabel();
			whitePlayer.setHorizontalAlignment(JLabel.CENTER);
			blackCaptured = new JLabel(CAPTURED + 0);
			blackCaptured.setHorizontalAlignment(JLabel.CENTER);
			whiteCaptured = new JLabel(CAPTURED + 0);
			whiteCaptured.setHorizontalAlignment(JLabel.CENTER);

			blackPanel.add(blackName);
			blackPanel.add(blackPlayer);
			blackPanel.add(blackCaptured);
			whitePanel.add(whiteName);
			whitePanel.add(whitePlayer);
			whitePanel.add(whiteCaptured);
		}

		/**
		 * Update information about which player is which by passing the
		 * client's color.
		 * 
		 * @param color
		 *            Client's color.
		 */
		public void setColor(String color) {
			if (color.equals(ServerClientProtocol.getInstance().BLACK)) {
				blackPlayer.setText(YOU);
				whitePlayer.setText(OPPONENT);
			} else if (color.equals(ServerClientProtocol.getInstance().WHITE)) {
				blackPlayer.setText(OPPONENT);
				whitePlayer.setText(YOU);
			} else {
				blackPlayer.setText("");
				whitePlayer.setText("");
			}
		}

		/**
		 * Update information about each player's captured stones.
		 * 
		 * @param black
		 *            Black's captured stones.
		 * @param white
		 *            White's captured stones.
		 */
		public void setCaptured(int black, int white) {
			blackCaptured.setText(CAPTURED + black);
			whiteCaptured.setText(CAPTURED + white);
		}
	}

}
