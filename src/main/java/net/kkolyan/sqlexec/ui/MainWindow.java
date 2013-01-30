package net.kkolyan.sqlexec.ui;

import net.kkolyan.sqlexec.Utils;
import net.kkolyan.sqlexec.api.Connect;
import net.kkolyan.sqlexec.impl.NewConnectionData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author n.plekhanov
 */
public class MainWindow implements ActionListener {
    private JTabbedPane tabbedPane1;
    private JPanel rootPane;
    private JButton newConnectionButton;
    private JButton closeButton;

    public MainWindow(Container container) throws Exception {
		tabbedPane1.setVisible(false);
		tabbedPane1.setBackground(Color.BLACK);
        container.add(rootPane);
		Utils.setFonts(rootPane);
        newConnectionButton.addActionListener(this);
    }

	private void newConnectTab(Connect connect) throws Exception {
		if (!tabbedPane1.isVisible()) {
			tabbedPane1.setVisible(true);
		}
		JPanel panel = new JPanel(new BorderLayout());
		new ConnectionPane(panel, connect);
		tabbedPane1.insertTab(connect.getName(), null, panel, "tip", 0);
	}

    @Override
    public void actionPerformed(ActionEvent event) {
		if (event.getSource() == newConnectionButton) {
			NewConnectionDialog.showDialog(new NewConnectionData.Handler() {
				@Override
				public void handle(NewConnectionData data) throws Exception {
					Connect connect = data.getVendor().newConnect(data);
					newConnectTab(connect);
				}
			});
		}
	}

    public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		final JFrame frame = new JFrame();
        new MainWindow(frame);
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
