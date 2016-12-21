/**
 * 
 */
package goclient.client;

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
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

	public void setPhase(int phase) {
		this.phase = phase;
		System.out.println("PHASE:" + phase);
	}
	
	public void setColor(String color) {
		this.color = color;
	}

	private TextArea captured;

	public void setCapturedStones(IntPair pair) {
		captured.setText("Black:" + pair.x + " White: " + pair.y);
	}

	Ellipse2D[][] stones;
	Color[][] stoneColors;

	public BoardFrame(int size) {
		communication = GUI.getCommunication();
		this.size = size;
		frame = new JFrame("Go");
		frame.setLayout(new GridLayout(1,1));
		// frame.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		frame.setVisible(false);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ?
		
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		frame.add(mainPanel);
		// JLabel boardImage = null;
		BufferedImage img = null;
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
			e.printStackTrace();
		}
		stones = new Ellipse2D[size][size];
		stoneColors = new Color[size][size];
		canvas = new BoardCanvas(size, img);
		
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(canvas, c);
		canvas.setLayout(null);
		JFrame waitingFrame = new JFrame("Please Wait");
		waitingFrame.setVisible(true);
		waitingFrame.setBounds(800, 300, 200, 120);
		System.out.println("waiting for input");
		
		frame.setVisible(true);
		waitingFrame.setVisible(false);
		waitingFrame = null;
		
		captured = new TextArea("Black:0 White:0", 1, 15, TextArea.SCROLLBARS_NONE);
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

		JButton passbutton = new JButton("PASS");
		buttonPanel.add(passbutton);
		passbutton.setOpaque(true);
		passbutton.setContentAreaFilled(true);
		passbutton.setBorderPainted(true);

		passbutton.addActionListener(this);

		JButton proposeChangesButton = new JButton("Propose Changes");
		buttonPanel.add(proposeChangesButton);
		proposeChangesButton.setEnabled(false);
		proposeChangesButton.addActionListener(this);

		ReadingThread thread = new ReadingThread(size, this);
		thread.start();

		frame.pack();
		// wait for game start info
	}

	private class BoardCanvas extends JPanel implements ActionListener {

		BufferedImage img;
		int size;

		public BoardCanvas(int size, BufferedImage img) {
			super();
			this.size = size;
			this.img = img;
			System.out.println(img.getWidth() + "  " + img.getHeight());
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
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (stones[i][j] != null) {
						g2.setColor(stoneColors[i][j]);
						g2.fill(stones[i][j]);
					}
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			MyButton button = (MyButton) e.getSource();
			int x = button.getX();
			int y = button.getY();
			System.out.println(x + " " + y);
			if (phase == 0)
				ClientRequestSender.getInstance().sendMove(x, y, communication);
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
		System.out.println("DRAWING BOARD " + size + " " + board.length);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board[i][j] == 0) {
					if (stones[i][j] != null) {
						stones[i][j] = null;
						stoneColors[i][j] = null;
					}
				} else {
					if (stones[i][j] == null) {
						System.out.println(29 * i + ":x y:" + 29 * j);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (((JButton) e.getSource()).getText() == "PASS") {
			sender.passTurn(communication);
		} else if (((JButton) e.getSource()).getText() == "Propose Changes") {
			// TODO sender.sendGroupChanges(, communication);
		}
	}

}
