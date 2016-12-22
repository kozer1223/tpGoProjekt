/**
 * 
 */
package goclient.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel contentPanel = new JPanel(new BorderLayout(5,5));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		frame.setContentPane(contentPanel);
		
		JLabel label = new JLabel("Pick your board size.");
		label.setHorizontalAlignment(JLabel.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout());
		button19 = new JButton("19");
		button13 = new JButton("13");
		button9 = new JButton("9");
		buttonPanel.add(button19);
		buttonPanel.add(button13);
		buttonPanel.add(button9);
		button19.addActionListener(this);
		button13.addActionListener(this);
		button9.addActionListener(this);
		contentPanel.add(label, BorderLayout.NORTH);
		contentPanel.add(buttonPanel, BorderLayout.CENTER);
		this.opponentPick = opponentPick;
		frame.pack();
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
