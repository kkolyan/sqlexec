package net.kkolyan.sqlexec.ui;

import net.kkolyan.sqlexec.Utils;
import net.kkolyan.sqlexec.api.Connect;
import net.kkolyan.sqlexec.api.Vendor;
import net.kkolyan.sqlexec.impl.NewConnectionData;

import javax.swing.*;
import java.awt.Window;
import java.awt.event.*;

public class NewConnectionDialog extends JDialog implements NewConnectionData {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField host_JTextField;
    private JTextField userName_JTextField;
    private JPasswordField password_JPasswordField;
    private JComboBox vendor_JComboBox;
    private JTextField port_JTextField;
    private JTextField database_JTextField;
	private JTextField connectionName_JTextField;

	private NewConnectionDialog(String title) {
		super((Window) null, title);
        setContentPane(contentPane);
		IntegerDocument.apply(port_JTextField);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
		initListeners();
		pack();
		Utils.centerComponent(this);
		initModel();
    }

	//========================

	private static NewConnectionDialog singleton = new NewConnectionDialog("New Connection");
	private static Handler handler;

	public synchronized static void showDialog(Handler handler) {
		NewConnectionDialog.handler = handler;
		singleton.setVisible(true);
	}

	//========================

    private void onOK() {
		try {
			handler.handle(this);
			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(rootPane, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
    }

    private void onCancel() {
        dispose();
    }

	//=====================================

	private void initModel() {
		vendor_JComboBox.setModel(new DefaultComboBoxModel(Vendor.values()));
		vendor_JComboBox.setSelectedIndex(0);

		host_JTextField.setText("localhost");
	}

	private void initListeners() {

		vendor_JComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				port_JTextField.setText(getVendor().getDefaultPort()+"");
				userName_JTextField.setText(getVendor().getDefaultUsername());
			}
		});

		// default

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	//===================================

	@Override
	public String getConnectionName() {
		return connectionName_JTextField.getText();
	}

	@Override
	public String getDatabase() {
		return database_JTextField.getText();
	}

	@Override
	public String getHost() {
		return host_JTextField.getText();
	}

	@Override
	public char[] getPassword() {
		return password_JPasswordField.getPassword();
	}

	@Override
	public int getPort() {
		return Integer.parseInt(port_JTextField.getText());
	}

	@Override
	public String getUsername() {
		return userName_JTextField.getText();
	}

	@Override
	public Vendor getVendor() {
		return (Vendor) vendor_JComboBox.getSelectedItem();
	}
}
