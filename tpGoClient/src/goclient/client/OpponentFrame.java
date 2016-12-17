/**
 * 
 */
package goclient.client;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author Maciek
 *
 */
public class OpponentFrame implements ActionListener {

	private JFrame frame;
	private JButton buttonPlayer;
	private JButton buttonBot;

	public OpponentFrame() {
		frame = new JFrame("GO");
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);
		frame.setBounds(800, 300, 300, 100);
		buttonPlayer = new JButton("Player");
		buttonBot = new JButton("Bot");
		frame.add(buttonPlayer);
		frame.add(buttonBot);
		buttonPlayer.addActionListener(this);
		buttonBot.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int pick = -1;
		if (e.getSource() == buttonPlayer) {
			pick = 0;
		} else if (e.getSource() == buttonBot) {
			pick = 1;
		}
		if (pick != -1) {
			frame.setVisible(false);
			frame.setEnabled(false);
			frame = null;
			SizeFrame nextFrame = new SizeFrame(pick);
		}
	}

}
