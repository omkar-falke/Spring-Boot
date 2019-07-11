import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
//import spl.*;


public class cl_JTBL extends JTable implements KeyListener, FocusListener, CellEditorListener, ActionListener
{
	DefaultCellEditor[] dceEDITR;//Array to store editors in serial of col index
	JComponent[] cmpEDITR;//Array to store editor components in serial of col index
	int int_ACPSRC=-1,col=0,row=0;
	String LM_DRCTN="";
	MouseEvent LM_MSEVNT=new MouseEvent(this, 500, 123456l, 4, 0,0,1, false);
	boolean fl_COLMV;
	private Hashtable<String,Color> hstROWCL,hstCOLCL,hstCELCL;
	private Color clrTBLFG;

//	public cl_JTBL(cl_tab2 L_TBLOBJ1)
	public cl_JTBL(Object[][]L_TBLDT,Object[]LP_COLHD)
	{
//		super(L_TBLOBJ1); 
		super(new cl_tab2(L_TBLDT,LP_COLHD)); 
		this.addKeyListener(this);		
		setBackground(new Color(213,213,255));
		cmpEDITR=new JComponent[this.getColumnCount()];
		dceEDITR=new DefaultCellEditor[this.getColumnCount()];	
		for( int i=0;i<this.getColumnCount();i++)
		{
			if(getValueAt(0,i).equals(((Object)new Boolean(false))))
			{
				cmpEDITR[i]=new JCheckBox();
				((JCheckBox)cmpEDITR[i]).addActionListener(this);
				((JCheckBox)cmpEDITR[i]).addKeyListener(this);
				dceEDITR[i]=new DefaultCellEditor((JCheckBox)cmpEDITR[i]);
			}
			else
			{
				cmpEDITR[i]=new JTextField();
				((JTextField)cmpEDITR[i]).addActionListener(this);
				((JTextField)cmpEDITR[i]).addKeyListener(this);
				dceEDITR[i]=new DefaultCellEditor((JTextField)cmpEDITR[i]);
			}
			cmpEDITR[i].addFocusListener(this);
			dceEDITR[i].setClickCountToStart(1);
			dceEDITR[i].addCellEditorListener(this);
			this.getColumn(this.getColumnName(i)).setCellEditor(dceEDITR[i]);
		}
		hstROWCL=new Hashtable<String,Color>(5,0.2f);hstCOLCL=new Hashtable<String,Color>(5,0.2f);hstCELCL=new Hashtable<String,Color>(5,0.2f);
		clrTBLFG=Color.black;
		MyTableCellRenderer tcrCUST=new MyTableCellRenderer();
		for(int i=0;i<this.getColumnCount();i++)
			if(!(cmpEDITR[i] instanceof JCheckBox))
			this.getColumn(this.getColumnName(i)).setCellRenderer(tcrCUST);
	}
	
	public void setEnabled(boolean L_STAT)
	{
		setRowSelectionInterval(0,0);
		setColumnSelectionInterval(0,0);
		for(int i=0;i<cmpEDITR.length;i++)
		{
			cmpEDITR[i].setEnabled(L_STAT);
		}
	}
	public void setCellColor(int P_intROWINDEX,int P_intCOLINDEX,Color P_clrFORCL)
	{
		hstCELCL.put((String.valueOf(P_intROWINDEX)+"|"+String.valueOf(P_intCOLINDEX)),P_clrFORCL);
	}
	public void setColumnColor(int P_intCOLINDEX,Color P_clrFORCL)
	{
		hstCOLCL.put(String.valueOf(P_intCOLINDEX),P_clrFORCL);
	}
	public void setRowColor(int P_intROWINDEX,Color P_clrFORCL)
	{
		hstROWCL.put(String.valueOf(P_intROWINDEX),P_clrFORCL);
	}
	public void setTableColor(Color P_clrFORCL)
	{
		clrTBLFG=P_clrFORCL;
	}
	

	public void setCellEditor(int LP_COLID,JComponent LP_EDCMP)
	{
		String L_STRTMP=LP_EDCMP.getClass().toString();
		if(L_STRTMP.equals("class javax.swing.JTextField")
		   ||L_STRTMP.equals("class TxtNumLimit")
		   ||L_STRTMP.equals("class TxtLimit")
		   ||L_STRTMP.equals("class TxtNonNumLimit")
		   ||L_STRTMP.equals("class TxtDate"))
		{
			dceEDITR[LP_COLID]=new DefaultCellEditor((JTextField)LP_EDCMP);
			((JTextField)LP_EDCMP).addActionListener(this);
			((JTextField)LP_EDCMP).addKeyListener(this);
			LP_EDCMP.addFocusListener(this);
		}
		else if(L_STRTMP.equals("class javax.swing.JComboBox"))
		{
			dceEDITR[LP_COLID]=new DefaultCellEditor((JComboBox)LP_EDCMP);
			((JComboBox)LP_EDCMP).addActionListener(this);
			((JComboBox)LP_EDCMP).addKeyListener(this);
		}
		else if(L_STRTMP.equals("class javax.swing.JCheckBox"))
		{
			dceEDITR[LP_COLID]=new DefaultCellEditor((JCheckBox)LP_EDCMP);
			((JCheckBox)LP_EDCMP).addActionListener(this);
			((JCheckBox)LP_EDCMP).addKeyListener(this);
			LP_EDCMP.addFocusListener(this);
		}
		cmpEDITR[LP_COLID]=LP_EDCMP;
		dceEDITR[LP_COLID].addCellEditorListener(this);
		dceEDITR[LP_COLID].setClickCountToStart(1);
		this.getColumn(this.getColumnName(LP_COLID)).setCellEditor(dceEDITR[LP_COLID]);
	}

	public void clrTABLE()
	{
		String L_STRTMP="";
		setRowSelectionInterval(0,0);
		setColumnSelectionInterval(0,0);
		scrollRectToVisible(getCellRect(0,0,false));
		for(int i=0;i<cmpEDITR.length;i++)
		{
			L_STRTMP=cmpEDITR[i].getClass().toString();
			if(L_STRTMP.equals("class javax.swing.JTextField")
			   ||L_STRTMP.equals("class TxtNumLimit")
			   ||L_STRTMP.equals("class TxtNonNumLimit")
			   ||L_STRTMP.equals("class TxtLimit")
			   ||L_STRTMP.equals("class TxtDate"))
			{
				((JTextField)cmpEDITR[i]).setText("");
				for(int j=0;j<getRowCount();j++)
				{
					setValueAt("",j,i);
				}
			}
			else if(L_STRTMP.equals("class javax.swing.JComboBox"))
			{
				String L_STRTMP1=((JComboBox)cmpEDITR[i]).getItemAt(0).toString();
				for(int j=0;j<getRowCount();j++)
				{
			//		setValueAt(L_STRTMP1,j,i);
					setValueAt(" ",j,i);
				}
			}
			else if(L_STRTMP.equals("class javax.swing.JCheckBox"))
			{
				Boolean b=new Boolean(false);
				for(int j=0;j<getRowCount();j++)
				{
					setValueAt(b,j,i);
				}
			}
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		{
			Object L_SRC=L_AE.getSource();
			for(int i=0;i<cmpEDITR.length;i++)
			{
				if(L_SRC==cmpEDITR[i])
				{
					int_ACPSRC=i;
					break;
				}
			}
		}
	}

	public void keyPressed(KeyEvent L_KE)
	{
		int key=L_KE.getKeyCode ();
		if(key==L_KE.VK_ENTER)
		{
			col=getSelectedColumn();
			row=getSelectedRow();
			if(getSelectedColumn()==cmpEDITR.length-1)
			{
				int i=0;
				for(i=0;i<cmpEDITR.length;i++)
				{
					if(cmpEDITR[i].isEnabled())
						break;
				}
				setRowSelectionInterval(getSelectedRow()+1,getSelectedRow()+1);
				setColumnSelectionInterval(i,i);
			}
			else
			{
				int i=0;
				for(i=getSelectedColumn()+1;i<cmpEDITR.length;i++)
				{
					if(cmpEDITR[i].isEnabled())
						break;
				}
				if(i==cmpEDITR.length)
				{
					for(i=0;i<cmpEDITR.length;i++)
					{
						if(cmpEDITR[i].isEnabled())
							break;
					}
					setRowSelectionInterval(getSelectedRow()+1,getSelectedRow()+1);
					setColumnSelectionInterval(i,i);
				}
				else
				{
					setRowSelectionInterval(getSelectedRow(),getSelectedRow());
					setColumnSelectionInterval(i,i);
				}
			}
		}
		if(key==L_KE.VK_ENTER&&L_KE.getSource()==this)
			L_KE.setKeyCode(L_KE.VK_TAB);
		else if(key==L_KE.VK_UP)
			LM_DRCTN="UP";
		else if(key==L_KE.VK_DOWN)
			LM_DRCTN="DOWN";
		else if(L_KE.getSource()!=this&&getSelectedColumn()!=0&&key!=L_KE.VK_TAB
				&&key!=L_KE.VK_ENTER&&key!=L_KE.VK_SHIFT&&key!=L_KE.VK_LEFT
				&&key!=L_KE.VK_RIGHT)
		{
			setValueAt(new Boolean(true),getSelectedRow(),0);
		}
	}
	
	public void keyReleased(KeyEvent L_KE)
	{
		int key=L_KE.getKeyCode ();
		if(L_KE.getSource()==this){
			if(getSelectedRow()!=-1&&getSelectedColumn()!=-1)
			{
				if(!cmpEDITR[getSelectedColumn()].getClass().toString().equals("class javax.swing.JCheckBox"))
				{
					editCellAt(getSelectedRow(),getSelectedColumn(),LM_MSEVNT);	
					((JComponent)cmpEDITR[getSelectedColumn()]).requestFocus();
				}
			}
		}
	}
	public void keyTyped(KeyEvent L_KE){}
	public void focusLost(FocusEvent L_FE)
	{
		String L_STRTMP=cmpEDITR[col].getClass().toString();
		if(LM_DRCTN.equals("UP")
		   &&(!L_STRTMP.equals("class javax.swing.JCheckBox"))
		   &&(!L_STRTMP.equals("class javax.swing.JComboBox")))
		{
			setValueAt(((JTextField)cmpEDITR[getSelectedColumn()]).getText(),getSelectedRow()+1,getSelectedColumn());
			LM_DRCTN="";
		}
		else if (LM_DRCTN.equals("DOWN")
				 &&getSelectedRow()!=(getRowCount()-1)
				 &&(!L_STRTMP.equals("class javax.swing.JCheckBox"))
				 &&(!L_STRTMP.equals("class javax.swing.JComboBox")))
		{
			setValueAt(((JTextField)cmpEDITR[getSelectedColumn()]).getText(),getSelectedRow()-1,getSelectedColumn());
			LM_DRCTN="";
		}
		if (int_ACPSRC!=-1)
		{
			if(!L_STRTMP.equals("class javax.swing.JCheckBox")
			   &&(!L_STRTMP.equals("class javax.swing.JComboBox")))
			{
				if((!L_STRTMP.equals("class javax.swing.JComboBox"))&&(!L_STRTMP.equals("class javax.swing.JCheckBox")))
					setValueAt(((JTextField)cmpEDITR[col]).getText(),row,col);
				if(int_ACPSRC==cmpEDITR.length-1)
				{
//					setRowSelectionInterval(row+1,row+1);
//					setColumnSelectionInterval(0,0);
				}
				else
				{
//					setRowSelectionInterval(getSelectedRow(),getSelectedRow());
//					setColumnSelectionInterval(getSelectedColumn(),getSelectedColumn());
				}
			}
			int_ACPSRC=-1;
		}			
	}
	
	public void focusGained(FocusEvent L_FE)
	{
		if(L_FE.getSource()!=this)
		{
			String L_STRTMP=cmpEDITR[getSelectedColumn()].getClass().toString();
			if(L_STRTMP.equals("class TxtNumLimit"))
			{
				((TxtNumLimit)cmpEDITR[this.getSelectedColumn()]).focusLost(L_FE);
			}
			else if(L_STRTMP.equals("class TxtLimit"))
			{
				((TxtLimit)cmpEDITR[this.getSelectedColumn()]).focusLost(L_FE);
			}
			else if(L_STRTMP.equals("class TxtNonNumLimit"))
			{
				((TxtNonNumLimit)cmpEDITR[this.getSelectedColumn()]).focusLost(L_FE);
			}
		}
	}
	public void columnMoved(TableColumnModelEvent e)
	{
		//disables column moving
		fl_COLMV=!fl_COLMV;
		if(fl_COLMV)
			moveColumn(e.getToIndex(),e.getFromIndex());
	}
	private class MyTableCellRenderer extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col)
		{
				if(hstCELCL.containsKey(String.valueOf(row)+"|"+String.valueOf(col)))
					setForeground(((Color)hstCELCL.get(String.valueOf(row)+"|"+String.valueOf(col))));
				else if(hstROWCL.containsKey(String.valueOf(row)))
					setForeground((Color)hstROWCL.get(String.valueOf(row)));
				else if(hstCOLCL.containsKey(String.valueOf(col)))
					setForeground((Color)hstCOLCL.get(String.valueOf(col)));
				else
					setForeground(clrTBLFG);
				try
				{
					Double.parseDouble(value.toString());
					setHorizontalAlignment(JTextField.RIGHT);
				}catch(Exception e){setHorizontalAlignment(JTextField.LEFT);}
						
						// setHorizontalAlignment(((JTextField)cmpEDITR[col]).getHorizontalAlignment());
				if(table.getValueAt(row,col) instanceof Number)
					setHorizontalAlignment(JTextField.RIGHT);
			return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
		}
	}
}
	class cl_tab2 extends DefaultTableModel
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
