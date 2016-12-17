/**
 * 
 */
package goclient.client;

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

	private int opponentPick; // 0 - player, 1 - bot

	public SizeFrame(int opponentPick) {
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
		this.opponentPick = opponentPick;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int size = -1;
		if (e.getSource() == button19) {
			size = 19;
		} else if (e.getSource() == button13) {
			size = 13;
		} else if (e.getSource() == button9) {
			size = 9;
		}
		if (size != -1) {
			frame.setVisible(false);
			frame.setEnabled(false);
			frame = null;
			if (opponentPick == 0) {
				ClientRequestSender.getInstance().requestGameWithPlayer(size, GUI.getCommunication());
			} else {
				ClientRequestSender.getInstance().requestGameWithBot(size, GUI.getCommunication());
			}
			BoardFrame nextFrame = new BoardFrame(size);
		}
	}

}
