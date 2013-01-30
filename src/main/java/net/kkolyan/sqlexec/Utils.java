package net.kkolyan.sqlexec;

import org.springframework.jdbc.core.RowMapper;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author n.plekhanov
 */
public final class Utils {
	public static final Font FONT_COMMON = Font.decode("Verdana 12");
	public static final Font FONT_BOLD = Font.decode("Verdana BOLD 12");

	private Utils() {
    }

    public static <T> List<T> getData(ResultSet rs, RowMapper<T> mapper) throws SQLException {
        List<T> data = new ArrayList<T>();
        while (rs.next()) {
            data.add(mapper.mapRow(rs, rs.getRow()));
        }
        return data;
    }

	public static void centerComponent(Component target) {
		Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		target.setLocation(
				(int) (ss.getWidth() - target.getWidth()) / 2,
				(int) (ss.getHeight() - target.getHeight()) / 2
		);
	}

	public static void setFonts(Object o) {
		if (o instanceof JComponent) {
			for (Component component: ((JComponent) o).getComponents()) {
				setFonts(component);
			}
			setFonts(((JComponent) o).getBorder());
		}
		if (o instanceof JTable) {
			setFonts(((JTable) o).getTableHeader());
		}
		if (o instanceof TitledBorder) {
			((TitledBorder) o).setTitleFont(FONT_BOLD);
		}
		if (o instanceof Component) {
			((Component) o).setFont(FONT_COMMON);
		}
	}
}
