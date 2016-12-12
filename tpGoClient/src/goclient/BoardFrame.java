/**
 * 
 */
package goclient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private Comunication comunication;
	public BoardFrame(int size) {
		comunication = GUI.getComunication();
		this.size=size;
		frame=new JFrame("Go");
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
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
		for (int i=1;i<=size;i++) {
			for(int j=1;j<=size;j++) {
				JButton button = new JButton();
				button.setBounds(29*i-29, 29*j-24, 29, 29);
				button.addActionListener(this);
				button.setOpaque(false);
				button.setContentAreaFilled(false);
				button.setBorderPainted(false);
				boardImage.add(button);
				if(j==1) button.setName("a"+i);
				else if(j==2) button.setName("b"+i);
				else if(j==3) button.setName("c"+i);
				else if(j==4) button.setName("d"+i);
				else if(j==5) button.setName("e"+i);
				else if(j==6) button.setName("f"+i);
				else if(j==7) button.setName("g"+i);
				else if(j==8) button.setName("h"+i);
				else if(j==9) button.setName("i"+i);
				else if(j==10) button.setName("j"+i);
				else if(j==11) button.setName("k"+i);
				else if(j==12) button.setName("l"+i);
				else if(j==13) button.setName("m"+i);
				else if(j==14) button.setName("n"+i);
				else if(j==15) button.setName("o"+i);
				else if(j==16) button.setName("p"+i);
				else if(j==17) button.setName("q"+i);
				else if(j==18) button.setName("r"+i);
				else if(j==19) button.setName("s"+i);
			}
		}
		colour=comunication.read();
		System.out.println(colour);
		if(colour=="white") {
			frame.setEnabled(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String placeClicked = ((JButton) e.getSource()).getName();
		System.out.println(placeClicked);
		comunication.write(placeClicked);
		frame.setEnabled(false);
		String msg = comunication.read();
		frame.setEnabled(true);
	}

}
