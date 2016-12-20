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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //?
		// JLabel boardImage = null;
		BufferedImage img = null;
		try {
			if (size == 19) {
				frame.setBounds(100, 0, 570, 600);
				img = ImageIO.read(new File("resources/go.jpg"));
			} else if (size == 13) {
				frame.setBounds(100, 0, 395, 430);
				img = ImageIO.read(new File("resources/go13.png"));
			} else if (size == 9) {
				frame.setBounds(100, 0, 270, 310);
				img = ImageIO.read(new File("resources/go9.png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		stones = new Ellipse2D[size][size];
		stoneColors = new Color[size][size];
		// frame.add(boardImage);
		canvas = new BoardCanvas(size, img);
		// canvas.add(boardImage);
		frame.add(canvas);
		canvas.setLayout(null);
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

	private class BoardCanvas extends JPanel implements ActionListener{

		BufferedImage img;
		int size;

		public BoardCanvas(int size, BufferedImage img) {
			super();
			this.size = size;
			this.img = img;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					MyButton button = new MyButton();
					button.setBounds(29 * i , 29 * j, 29, 29);
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
			int x=button.getX();
			int y=button.getY();
			System.out.println(x+" "+y);
			ClientRequestSender.getInstance().sendMove(x, y, communication);
		}
		private class MyButton extends JButton {
			private int x;
			private int y;
			public void setX(int x){
				this.x=x;
			}
			public void setY(int y){
				this.y=y;
			}
			public int getY(){
				return y;
			}
			public int getX(){
				return x;
			}
		}

	}

	public void drawBoard(int board[][]) {
		System.out.println("DRAWING BOARD "+size + " " + board.length);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board[i][j] == 0) {
					if (stones[i][j] != null) {
						stones[i][j] = null;
						stoneColors[i][j] = null;
					}
				} else {
					if (stones[i][j] == null) {
						System.out.println(29*i + ":x y:" + 29*j);
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
		
	}

}
