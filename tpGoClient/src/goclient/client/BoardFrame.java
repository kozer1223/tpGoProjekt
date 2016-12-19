/**
 * 
 */
package goclient.client;

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
	private ReaderWriter communication;
	public BoardFrame(int size) {
		communication = GUI.getCommunication();
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
				/*if(j==1) button.setName("a"+i);
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
				else if(j==19) button.setName("s"+i);*/
			}
		}
		System.out.println("waiting for input");
		colour = null;
		while(colour == null){
			colour=communication.read();
		}
		System.out.println(colour);
		if(ClientProtocolParser.getInstance().parseColor(colour)==ServerClientProtocol.getInstance().WHITE) {
			frame.setEnabled(false);
		}
		
		//wait for game start info
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String placeClicked = ((JButton) e.getSource()).getName();
		System.out.println(placeClicked);
		communication.write(placeClicked);
		frame.setEnabled(false);
		String msg = communication.read();
		frame.setEnabled(true);
	}

}
