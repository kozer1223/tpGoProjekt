/**
 * 
 */
package goclient;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

/**
 * @author Maciek
 *
 */
public class GUI {
	
	private static Comunication comunication;
	
	public static void main(String[] args) {
		comunication = new Comunication();
		OpponentFrame frame = new OpponentFrame();
	}
		
	public static Comunication getComunication() {
		return comunication;
	}
	
}
