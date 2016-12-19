/**
 * 
 */
package goclient.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Maciek
 *
 */
public class BoardFrame implements ActionListener {

	private JFrame frame;
	private int size;
	private String colour="no colour";
	private ReaderWriter communication;
	public BoardFrame(int size) {
		communication = GUI.getCommunication();
		this.size=size;
		frame=new JFrame("Go");
		frame.setLayout(new BorderLayout());
		frame.setVisible(false);
		frame.setResizable(false);
		JLabel boardImage=null;
		if(size==19) {
			frame.setBounds(100, 0, 570, 600);
			boardImage = new JLabel(new ImageIcon("resources\\go.jpg"));
		}
		else if(size==13) {
			frame.setBounds(100, 0, 395, 430);
			boardImage = new JLabel(new ImageIcon("resources\\go13.png"));
		}
		else if(size==9) {
			frame.setBounds(100, 0, 270, 310);
			boardImage = new JLabel(new ImageIcon("resources\\go9.png"));
		}
		frame.add(boardImage);
		boardImage.setLayout(null);
		for (int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				JButton button = new JButton();
				button.setBounds(29*(i+1)-29, 29*(j+1)-24, 29, 29);
				button.addActionListener(this);
				button.setOpaque(false);
				button.setContentAreaFilled(false);
				button.setBorderPainted(false);
				boardImage.add(button);
				button.setName(i + " " + j);
			}
		}
		JFrame waitingFrame =new JFrame("Please Wait");
		waitingFrame.setVisible(true);
		waitingFrame.setBounds(800,300,200, 20);
		System.out.println("waiting for input");
		colour = null;
		while(colour == null){
			colour=communication.read();
		}
		System.out.println(colour);
		frame.setVisible(true);
		waitingFrame.setVisible(false);
		waitingFrame=null;
		ReadingThread thread = new ReadingThread(size,this);
		thread.start();
		
		//wait for game start info
	}
	
	Ellipse2D[][] stones = new Ellipse2D[size][size];
	Graphics2D g;
	public void drawBoard(int board[][]) {
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				if(board[i][j]==0) {
					if(stones[i][j]==null) continue;
					//TODO else
				}
				else {
					if(stones[i][j]==null) {
						stones[i][j]=new Ellipse2D.Double(29*i,29*j+5,29,29);
					}
					if(board[i][j]==1) {
						g.setColor(Color.black);
						g.draw(stones[i][j]);
					}
					else if(board[i][j]==2) {
						g.setColor(Color.white);
						g.draw(stones[i][j]);
					}
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String placeClicked = ((JButton) e.getSource()).getName();
		System.out.println(placeClicked);
		//communication.write(placeClicked);
		//frame.setEnabled(false);
		//String msg = communication.read();
		//frame.setEnabled(true);
	}

}
