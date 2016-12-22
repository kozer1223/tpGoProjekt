/**
 * 
 */
package goclient.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		frame.setVisible(true);
		frame.setBounds(800, 300, 300, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel contentPanel = new JPanel(new BorderLayout(5,5));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		frame.setContentPane(contentPanel);
		
		JLabel label = new JLabel("Welcome to OnlineGo 2016! Pick your opponent.");
		label.setHorizontalAlignment(JLabel.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPlayer = new JButton("Player");
		buttonBot = new JButton("Bot");
		buttonPanel.add(buttonPlayer);
		buttonPanel.add(buttonBot);
		buttonPlayer.addActionListener(this);
		buttonBot.addActionListener(this);
		contentPanel.add(label, BorderLayout.NORTH);
		contentPanel.add(buttonPanel, BorderLayout.CENTER);
		frame.pack();
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
