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
	private BoardCanvas canvas;
	private ClientRequestSender sender = ClientRequestSender.getInstance();
	private int phase = 0;
	private JButton proposeChangesButton;
	private JButton passButton;
	private JFrame waitingFrame;
	private boolean waitingForRematch = false;
	private boolean isDisabled = false;
	
	public int getPhase() {
		return phase;
	}
	
	public boolean isWaitingForRematch() {
		return waitingForRematch;
	}

	public void setPhase(int phase) {
		this.phase = phase;
		System.out.println("PHASE:" + phase);
		if (phase == 1){
			if(!isDisabled){
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
	
	public void setColor(String color) {
		this.color = color;
		System.out.println(color);
	}

	private TextArea captured;

	public void setCapturedStones(IntPair pair) {
		captured.setText("Black:" + pair.x + " White: " + pair.y);
	}

	Ellipse2D[][] stones;
	Color[][] stoneColors;
	int[][] groupLabels;
	Map<Integer, GoGroupType> groupStates; 
	List<Integer> lockedGroups;
	
	private TextArea messageArea;
	
	public void setMessage(String message){
		messageArea.setText(message);
	}

	public BoardFrame(int size) {
		communication = GUI.getCommunication();
		this.size = size;
		frame = new JFrame("Go");
		frame.setLayout(new GridLayout(1,1));
		frame.setVisible(false);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ?
		
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		frame.add(mainPanel);

		stones = new Ellipse2D[size][size];
		stoneColors = new Color[size][size];
		canvas = new BoardCanvas(size);
		
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(canvas, c);
		canvas.setLayout(null);
		
		captured = new TextArea("Black: 0 White: 0", 1, 15, TextArea.SCROLLBARS_NONE);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(captured, c);
		captured.setEditable(false);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		JPanel buttonPanel = new JPanel(new GridLayout(1,2));
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
		
		messageArea = new TextArea("", 1, 15, TextArea.SCROLLBARS_NONE);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		mainPanel.add(messageArea, c);
		messageArea.setEditable(false);

		ReadingThread thread = new ReadingThread(size, this);
		thread.start();
		
		groupLabels = null;
		groupStates = null;
		lockedGroups = null;
		
		waitingFrame = new JFrame("Please Wait");
		waitingFrame.setVisible(true);
		waitingFrame.setBounds(800, 300, 300, 120);
		JLabel waitLabel = new JLabel("Waiting for a player...");
		waitLabel.setHorizontalAlignment(JLabel.CENTER);
		waitingFrame.getContentPane().add(waitLabel);
		waitingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		frame.pack();
		if (color.equals(ServerClientProtocol.getInstance().BLACK)){
			enableInput();
		} else {
			disableInput();
		}
		// wait for game start info
	}

	private class BoardCanvas extends JPanel implements ActionListener {

		BufferedImage img;
		int size;

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
					MyButton button = new MyButton();
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
						if (getPhase() == 1 && groupLabels != null && groupStates != null){
							Color borderColor = (groupStates.get(groupLabels[i][j]) == GoGroupType.ALIVE ? Color.GREEN : Color.RED);
							if (isGroupLocked(groupLabels[i][j])){
								// ciemniejszy kolor dla zablokowanych
								borderColor = (borderColor == Color.GREEN ? new Color(0, 80, 0) : new Color (80, 0, 0));
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
			if (!isWaitingForRematch() & !isDisabled){
				MyButton button = (MyButton) e.getSource();
				int x = button.getX();
				int y = button.getY();
				
				if (phase == 0){
					disableInput();
					ClientRequestSender.getInstance().sendMove(x, y, communication);
					setMessage("Waiting...");
				} else if (phase == 1 && groupLabels != null){
					int label = groupLabels[x][y];
					if (!isGroupLocked(label)){
						toggleGroupState(label);
					}
					
				}
			}
		}

		private class MyButton extends JButton {
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
	
	public void setGroupLabels(int[][] labeledBoard){
		this.groupLabels = labeledBoard;
		canvas.repaint();
	}
	
	public void setGroupStates(Map<Integer, GoGroupType> groupStates){
		this.groupStates = groupStates;
		canvas.repaint();
	}
	
	public void setLockedGroups(List<Integer> lockedGroups){
		this.lockedGroups = lockedGroups;
		canvas.repaint();
	}
	
	public GoGroupType getGroupState(int label){
		if (groupStates != null){
			return groupStates.get(label);
		}
		return null;
	}
	
	public void setGroupState(int label, GoGroupType state){
		if (groupStates != null){
			groupStates.put(label, state);
		}
		canvas.repaint();
	}
	
	public boolean isGroupLocked(int label){
		if (lockedGroups != null){
			return lockedGroups.contains(label);
		}
		return false;
	}
	
	public void toggleGroupState(int label){
		GoGroupType state;
		if ((state = getGroupState(label)) != null){
			state = (state == GoGroupType.ALIVE ? GoGroupType.DEAD : GoGroupType.ALIVE);
			setGroupState(label, state);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isWaitingForRematch() && !isDisabled) {
			if (((JButton) e.getSource()).equals(passButton)) {
				disableInput();
				sender.passTurn(communication);
				setMessage("Waiting...");
			} else if (((JButton) e.getSource()).equals(proposeChangesButton)) {
				disableInput();
				sender.sendGroupChanges(groupStates, communication);
				setMessage("Waiting...");
			}
		}
	}

	public void beginGame() {
		frame.setVisible(true);
		if (waitingFrame != null){
			waitingFrame.setVisible(false);
			waitingFrame = null;
		}
	}

	public void showScore(DoublePair score) {
		boolean victory;
		double yourScore;
		double opponentsScore;
		if (color.equals(ServerClientProtocol.getInstance().BLACK)){
			yourScore = score.x;
			opponentsScore = score.y;
		} else {
			yourScore = score.y;
			opponentsScore = score.x;
		}
		System.out.println(color);
		System.out.println(yourScore + " " + opponentsScore);
		victory = (yourScore > opponentsScore);
		boolean tie = (yourScore == opponentsScore);
		
		StringBuilder message = new StringBuilder();
		if (tie){
			message.append("You tied.\n");
		} else if (victory){
			message.append("You won.\n");
		} else {
			message.append("You lost.\n");
		}
		message.append("You: "+ yourScore + " Opponent: " + opponentsScore + "\n");
		message.append("Do you want a rematch?");
		int choice = JOptionPane.showConfirmDialog(frame, message, "Rematch", JOptionPane.YES_NO_OPTION);
		
		if (choice == JOptionPane.YES_OPTION){
			//request rematch
			sender.requestRematch(communication);
			waitingForRematch = true;
			setMessage("Waiting for rematch!");
			passButton.setEnabled(false);
			proposeChangesButton.setEnabled(false);
		} else {
			frame.setVisible(false);
			frame.dispose();
			System.exit(0); //powrot do menu?
		}
	}

	public void rematchDenied() {
		JOptionPane.showMessageDialog(frame, "Rematch denied.");
		frame.setVisible(false);
		frame.dispose();
		System.exit(0); //powrot do menu?
	}

	public void rematchAccepted() {
		waitingForRematch = false;
		JOptionPane.showMessageDialog(frame, "Rematch accepted.");
		resetGame();
	}

	private void resetGame() {
		stones = new Ellipse2D[size][size];
		stoneColors = new Color[size][size];
		groupLabels = null;
		groupStates = null;
		lockedGroups = null;
		setMessage("");
		setPhase(0);
		canvas.repaint();
		if (color.equals(ServerClientProtocol.getInstance().BLACK)){
			enableInput();
		} else {
			disableInput();
		}
	}
	
	public void disableInput() {
		isDisabled = true;
		passButton.setEnabled(false);
		proposeChangesButton.setEnabled(false);
	}
	
	public void enableInput() {
		isDisabled = false;
		passButton.setEnabled(true);
		if (getPhase() == 1){
			proposeChangesButton.setEnabled(true);
		}
		
	}

}
