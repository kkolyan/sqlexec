package net.kkolyan.sqlexec;

import net.kkolyan.sqlexec.ui.ConnectionPane;

import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

public class Main {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		final JFrame frame = new JFrame();
		ConnectionPane connectionPane = new ConnectionPane(frame.getContentPane(), null);
//		frame.setSize(800,600);
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		{
			Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation((int)(ss.getWidth() - frame.getWidth())/2,(int)(ss.getHeight() - frame.getHeight())/2);
		}
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
}
