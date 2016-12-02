/**
 * 
 */
package goclient;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 * @author Maciek
 *
 */
public class BoardFrame implements ActionListener {

	JFrame frame;
	public BoardFrame() {
		frame=new JFrame("Go");
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);
		frame.setBounds(100, 0, 1750, 1000);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
