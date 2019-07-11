import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;

public class cl_tab2 extends DefaultTableModel
{
	public cl_tab2(Object[][] data,Object[] LP_COLHDG){
		super(data,LP_COLHDG);
	}
	public Class getColumnClass(int col){
		Vector v = (Vector)dataVector.elementAt(0);
		return v.elementAt(col).getClass();
	}
	public boolean isCellEditable(int row,int col){
		Class columnClass = getColumnClass(col);
		return columnClass!=ImageIcon.class && columnClass!=Date.class;
	}
}
