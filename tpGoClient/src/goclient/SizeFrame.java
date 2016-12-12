/**
 * 
 */
package goclient;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author Maciek
 *
 */
public class SizeFrame implements ActionListener {
	
	private JFrame frame;
	private JButton button19;
	private JButton button13;
	private JButton button9;
	
	public SizeFrame() {
		frame = new JFrame("GO");
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);
		frame.setBounds(800, 300, 300, 100);
		button19 = new JButton("19");
		button13 = new JButton("13");
		button9 = new JButton("9");
		frame.add(button19);
		frame.add(button13);
		frame.add(button9);
		button19.addActionListener(this);
		button13.addActionListener(this);
		button9.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.setVisible(false);
		frame.setEnabled(false);
		frame=null;
		if(e.getSource()==button19){
			GUI.getComunication().write("19");
			BoardFrame nextFrame = new BoardFrame(19);
		}
		else if(e.getSource()==button13){
			GUI.getComunication().write("13");
			BoardFrame nextFrame = new BoardFrame(13);
		}
		else if(e.getSource()==button9){
			GUI.getComunication().write("9");
			BoardFrame nextFrame = new BoardFrame(9);
		}
	}

}
