package net.kkolyan.sqlexec.ui;

import net.kkolyan.sqlexec.InputDataException;
import net.kkolyan.sqlexec.Utils;
import net.kkolyan.sqlexec.api.Connect;
import net.kkolyan.sqlexec.api.Result;
import net.kkolyan.sqlexec.api.Table;
import net.kkolyan.sqlexec.api.TableSpace;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionPane {
    private Model model = new Model();
    
	private JTable tables_JTable;
	private JPanel rootPanel;
	private JTable columns_JTable;
	private JTextArea queryEditor_JTextArea;
	private JTable result_JTable;
	private JTabbedPane queryEditor_JTabbedPane;
	private JButton executeQuery_JButton;
	private JTextField queryOffset_JTextField;
	private JTextField queryLimit_JTextField;
	private JComboBox tableSpaces_JComboBox;

	private TablesModel tables_Model = new TablesModel();
	private ColumnsModel columns_Model = new ColumnsModel();
	private MapListModel results_Model = new MapListModel();
	private TableSpaceComboBoxModel tableSpaces_Model = new TableSpaceComboBoxModel();

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private Connect connect;


	public ConnectionPane(Container container, Connect connect) throws IOException, InputDataException, SQLException {
		container.add(rootPanel);
		Utils.setFonts(rootPanel);

		this.connect = connect;

		tableSpaces_Model.setList(connect.getTableSpaces());
		tableSpaces_JComboBox.setModel(tableSpaces_Model);
		tables_JTable.setModel(tables_Model);
		columns_JTable.setModel(columns_Model);
		result_JTable.setModel(results_Model);
		if (tableSpaces_Model.getSize() > 0) {
			int index = tableSpaces_Model.getSize() - 1;
			tableSpaces_JComboBox.setSelectedIndex(index);
			refreshTables(index);
		}

		tables_JTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tables_JTable.setTableHeader(null);

        IntegerDocument.apply(queryOffset_JTextField);
        IntegerDocument.apply(queryLimit_JTextField);
		initListeners();
	}

	private TableSpace getSelectedTableSpace() {
		int selectedDatabaseIndex = tableSpaces_JComboBox.getSelectedIndex();
		if (selectedDatabaseIndex > 0) {
			return tableSpaces_Model.getList().get(selectedDatabaseIndex);
		}
		return null;
	}

	private void executeScript(String sql) {
		try {
			TableSpace tableSpace = getSelectedTableSpace();
			Result<Map<String, Object>> result = connect.execute(sql, tableSpace, new ColumnMapRowMapper());
			results_Model.setKeys(result.getColumnNames());
			results_Model.setList(result.getList());
			results_Model.fireTableStructureChanged();
		} catch (SQLException e) {
			handle(e);
		}
	}

	private void handle(Throwable e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(rootPanel, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.INFORMATION_MESSAGE);
	}

	private void addQueryEditor() {

	}

	private void refreshTables(int selectedTableSpaceIndex) {
		try {
			int row = selectedTableSpaceIndex;
			TableSpace tableSpace = tableSpaces_Model.getList().get(row);
			tables_Model.setList(tableSpace.getTables());
			tables_Model.fireTableDataChanged();
		} catch (SQLException e) {
			handle(e);
		}
	}

	private void initListeners() {
		FocusListener intFieldFocusListener = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				final JTextField tf = (JTextField) event.getSource();
				if (tf.getText().isEmpty()) {
					tf.setText("0");
				}
				tf.getText();
			}
		};
		queryOffset_JTextField.addFocusListener(intFieldFocusListener);
		queryLimit_JTextField.addFocusListener(intFieldFocusListener);

		Object executeAction = new Object();
		queryEditor_JTextArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Event.CTRL_MASK), executeAction);
		queryEditor_JTextArea.getActionMap().put(executeAction, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				executeScript(queryEditor_JTextArea.getText());
			}
		});

		tableSpaces_JComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshTables(tableSpaces_JComboBox.getSelectedIndex());
			}
		});

		executeQuery_JButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				executeScript(queryEditor_JTextArea.getText());
			}
		});

		queryEditor_JTabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1) {
					addQueryEditor();
				}
			}
		});


		tables_JTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				try {
					int row = tables_JTable.getSelectedRow();
					Table table = tables_Model.getList().get(row);
					columns_Model.setList(table.getColumns());
					columns_Model.fireTableDataChanged();

					if (event.getClickCount() > 0) {
						executeScript(String.format(
								"select * from %s limit %d offset %d",
								table.getName(),
								Integer.valueOf(queryLimit_JTextField.getText()),
								Integer.valueOf(queryOffset_JTextField.getText())
						));
					}
				} catch (Exception e) {
					handle(e);
				}
			}
		});
	}

}
