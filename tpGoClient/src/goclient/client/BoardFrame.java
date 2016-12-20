/**
 * 
 */
package goclient.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

/**
 * @author Maciek
 *
 */
public class BoardFrame implements ActionListener {

	private JFrame frame;
	private int size;
	private String colour = "no colour";
	private ReaderWriter communication;
	private BoardCanvas canvas;

	Ellipse2D[][] stones;
	Color[][] stoneColors;

	public BoardFrame(int size) {
		communication = GUI.getCommunication();
		this.size = size;
		frame = new JFrame("Go");
		frame.setLayout(new BorderLayout());
		frame.setVisible(false);
		frame.setResizable(false);
		// JLabel boardImage = null;
		BufferedImage img = null;
		try {
			if (size == 19) {
				frame.setBounds(100, 0, 570, 600);
				img = ImageIO.read(new File("resources\\go.jpg"));
			} else if (size == 13) {
				frame.setBounds(100, 0, 395, 430);
				img = ImageIO.read(new File("resources\\go13.jpg"));
			} else if (size == 9) {
				frame.setBounds(100, 0, 270, 310);
				img = ImageIO.read(new File("resources\\go9.jpg"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		stones = new Ellipse2D[size][size];
		stoneColors = new Color[size][size];
		// frame.add(boardImage);
		canvas = new BoardCanvas(img);
		// canvas.add(boardImage);
		frame.add(canvas);
		canvas.setLayout(null);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				JButton button = new JButton();
				button.setBounds(29 * (i + 1) - 29, 29 * (j + 1) - 24, 29, 29);
				button.addActionListener(this);
				button.setOpaque(false);
				button.setContentAreaFilled(false);
				button.setBorderPainted(false);
				canvas.add(button);
				button.setName(i + " " + j);
			}
		}
		JFrame waitingFrame = new JFrame("Please Wait");
		waitingFrame.setVisible(true);
		waitingFrame.setBounds(800, 300, 200, 20);
		System.out.println("waiting for input");
		colour = null;
		while (colour == null) {
			colour = communication.read();
		}
		System.out.println(colour);
		frame.setVisible(true);
		waitingFrame.setVisible(false);
		waitingFrame = null;

		ReadingThread thread = new ReadingThread(size, this);
		thread.start();

		// wait for game start info
	}

	private class BoardCanvas extends JPanel {

		BufferedImage img;

		public BoardCanvas(BufferedImage img) {
			super();
			this.img = img;
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

	}

	public void drawBoard(int board[][]) {
		System.out.println("DRAWING BOARD");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board[i][j] == 0) {
					if (stones[i][j] != null) {
						stones[i][j] = null;
						stoneColors[i][j] = null;
					}
				} else {
					if (stones[i][j] == null) {
						stones[i][j] = new Ellipse2D.Double(29 * i, 29 * j + 5, 29, 29);
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
		// to trzeba zrobic bardziej ogarniecie
		String placeClicked = ((JButton) e.getSource()).getName();
		System.out.println(placeClicked);
		Scanner scanner = new Scanner(placeClicked);
		int x = scanner.nextInt();
		int y = scanner.nextInt();
		ClientRequestSender.getInstance().sendMove(x, y, communication);
		// communication.write(placeClicked);
		// frame.setEnabled(false);
		// String msg = communication.read();
		// frame.setEnabled(true);
	}

}
