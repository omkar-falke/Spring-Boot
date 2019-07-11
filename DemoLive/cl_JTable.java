import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;import javax.swing.JOptionPane;
import javax.swing.JTable;import javax.swing.JTextField;import javax.swing.JComboBox;import javax.swing.JViewport;
import javax.swing.JCheckBox;import javax.swing.JComponent;import javax.swing.DefaultCellEditor;
import javax.swing.ListSelectionModel;import javax.swing.KeyStroke;
import java.awt.Color;import java.awt.KeyboardFocusManager;import java.awt.Component;
import java.awt.event.MouseListener;import java.awt.event.FocusListener;import java.awt.event.KeyListener;import java.awt.event.MouseEvent;import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import javax.swing.event.ChangeEvent;import javax.swing.event.TableColumnModelEvent;
import java.util.Hashtable;

/**
 * This class provides addtional functionality to JTable class in java. The table module is modified to accoplish general requirement in our application<br>
 * Table module used is cl_tab2 which is inherited from Default Table Module. This help in validating of data, focus nevigation and customised look & feel<br>
 * <p>Following features are available with this class :<br>
 * <FONT color=purple><STRONG><BLOCKQUOTE style="MARGIN-RIGHT: 0px">  <P align=left><FONT face="LotusWP Icon">/&nbsp;&nbsp;&nbsp;</FONT>   </STRONG></FONT>Focus   Nevigation on enter from cell to cell. Focus nevigation by TAB, which is   default with java, is disabled. Focus nevigation takes care of jumping through   the deisabled columns</P>  <P align=left><FONT face="LotusWP Icon"><FONT face="LotusWP Icon"><FONT   color=purple><STRONG>/</STRONG></FONT></FONT>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Defaul<FONT face="LotusWP Icon"><FONT   color=purple><FONT color=#000000 face="Times New Roman">t alignment of   data is set i.e. right aligned for numeric and left aligned for alphanumeric.   Also, number foramtting is done automatically</FONT></FONT></FONT></P>  <P align=left><FONT face="LotusWP Icon"><FONT color=purple><STRONG><FONT   face="LotusWP Icon"><FONT face="LotusWP Icon"><FONT   color=purple>/</FONT></FONT>&nbsp;&nbsp;&nbsp;</FONT></STRONG><FONT   face="LotusWP Icon"><FONT color=purple><FONT color=#000000   face="Times New Roman">For&nbsp;better L &amp; F, user can specify foreground   color for a cell, row, column or entire   table</FONT></FONT></FONT></FONT></FONT></P>  <P align=left><FONT face="LotusWP Icon"><FONT   color=purple><STRONG>/</STRONG></FONT>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Cell   Editor starts automatically when user starts entering data or nevigates   throught the cell. This facilitates use of customised text fields as cell   editor component </P>  <P align=left><FONT face="LotusWP Icon"><FONT   color=purple><STRONG>/</STRONG></FONT>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;User can   easily change editor components for columns. To give additional functionality,   key and focus listeners need to be registered. For default editor components,   table creation methods in cl_pbase register the listeners.</P>  <P align=left><FONT face="LotusWP Icon"><STRONG><FONT   color=purple>/</FONT></STRONG>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Easy   disabling/Enabling of table and<FONT face="LotusWP Icon"><FONT   color=purple><FONT   color=#000000 face="Times New Roman"> columns</FONT></FONT></FONT></P>  <P align=left><FONT face="LotusWP Icon"><FONT   color=purple><STRONG>/</STRONG></FONT>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Provision   Input Verifier class for additional validations. User&nbsp;need to define his   own customised class which&nbsp;is child of TableInputVerifier and has   to&nbsp;register the class as InputVerifier for the table object</P>  <P align=left><FONT face="LotusWP Icon"><STRONG><FONT   color=purple>/</FONT></STRONG>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;User is adviced   not to change the focus order or start editing of cell in InputVerifier class   as this will result into endless loop.</P>  <P align=left><FONT face="LotusWP Icon"><STRONG><FONT   color=purple>/</FONT></STRONG>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Before putting   the data into database, it is mandatory to call InputVerifier programatically   and then stop cell editing if Data validation is through. This is required   because, if focus is transfer outside of table during editing of cell, call to   InputVerifier is bypassed.</P>
 */
public class cl_JTable extends JTable implements MouseListener,KeyListener, FocusListener
{
	/**	Array to store editors in serial of col index */
	private DefaultCellEditor[] dceEDITR;/**	Array to store editor components in serial of col index */
	JComponent[] cmpEDITR;/** Hashtable to store Row Colors set by the user	 */
	private Hashtable<String,Color> hstROWCL;/** Hashtable to store Column Colors set by the user	 */
	private Hashtable<String,Color> hstCOLCL;/**	Hashtable to store Cell Colors set by the user */
	private Hashtable<String,Color> hstCELCL;/**	Table ForeGound Color set by the user */
	private Color clrTBLFG;/**	Flag To check if column is being moved */
	private boolean flgCOLMV;/**	Flag for error, To avoid re-execution of code in processKeyBinding on KeyReleaseEvent */
	private boolean flgERROR;/**	Copy of old value in the cell  */
	private Object objOLDVL;/**	Cell indexes of the cell under editing */
	private int intROWID,intCOLID;/**	Default TableInputVerifier for this table */
	private TableInputVerifier tivINPVF;/**	Flag to indicate error in Date Format */
	private boolean flgDTERR=false;/**	Flag for focus state, used for focus nevigation with 'Enter' */
	private boolean flgFGAIN=false;	
	
	/**
	 * Constructs a table with given table data and column headers
	 * 
	 * Constructs a table with given table data and column headers
	 * 
	 * <p>This constructor makes use of table model cl_tab2<br>
	 * Sets default background, Foreground, CellEditors and CellRenderer for this table<br>
	 * Registers listeners and set default input verifier
	 */
	public cl_JTable(Object[][]L_TBLDT,Object[]LP_COLHD)
	{
		super(new cl_tab2(L_TBLDT,LP_COLHD)); 
		setBackground(new Color(213,213,255));
		cmpEDITR=new JComponent[this.getColumnCount()];
		dceEDITR=new DefaultCellEditor[this.getColumnCount()];	
		for( int i=0;i<this.getColumnCount();i++)
		{
			if(getValueAt(0,i).equals(((Object)new Boolean(false))))
			{
				cmpEDITR[i]=new JCheckBox();
				dceEDITR[i]=new DefaultCellEditor((JCheckBox)cmpEDITR[i]);
			}
			else
			{
				cmpEDITR[i]=new JTextField();
				dceEDITR[i]=new DefaultCellEditor((JTextField)cmpEDITR[i]);
				
			}
			dceEDITR[i].setClickCountToStart(1);
			this.getColumn(this.getColumnName(i)).setCellEditor(dceEDITR[i]);
		}
		hstROWCL=new Hashtable<String,Color>(5,0.2f);hstCOLCL=new Hashtable<String,Color>(5,0.2f);hstCELCL=new Hashtable<String,Color>(5,0.2f);
		clrTBLFG=Color.black;
		MyTableCellRenderer tcrCUST=new MyTableCellRenderer();
		for(int i=0;i<this.getColumnCount();i++)
			if(!(cmpEDITR[i] instanceof JCheckBox))
			this.getColumn(this.getColumnName(i)).setCellRenderer(tcrCUST);
		addMouseListener(this);addKeyListener(this);
		for(int i=0;i<cmpEDITR.length;i++)
		{
			cmpEDITR[i].addFocusListener(this);
			cmpEDITR[i].addKeyListener(this);
		}
		setSurrendersFocusOnKeystroke(true);
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION );
		setInputVerifier(getDefaultTableInputVerifier());
		addFocusListener(this);
	}
	/**Sets flgGAIN to false if focus is not with Table	 */
	public void focusGained(FocusEvent L_FE)
	{
		if(!(L_FE.getSource() instanceof cl_JTable))
			flgFGAIN=false;
	}
	/**Empty definition of interface implementation	 */
	public void focusLost(FocusEvent L_FE)
	{}
	/**Returns instance of DefaultTableInputVerifier, which will have no validations defined in it	 */
	public DefaultTableInputVerifier getDefaultTableInputVerifier()
	{
		return new DefaultTableInputVerifier();
	}
	/**
	 * Method to set InputVerifier for the table<br>
	 * 
	 * @param P_tivTEMP Object of the class defined by user for validations. The class should be a valid child class of TableInputVerifier	 
	 */	
	public void setInputVerifier(TableInputVerifier P_tivTEMP)
	{
		tivINPVF=P_tivTEMP;
	}
	/**Set Cell Editor for a column
	 * @param P_intCOLID Zero base Column Index
	 * @param	PL_EDCMP Editor Coomponent	 */
	public void setCellEditor(int P_intCOLID,JComponent P_cmpEDITOR)
	{
		if(P_cmpEDITOR instanceof JTextField
		   ||P_cmpEDITOR instanceof  TxtNumLimit
		   ||P_cmpEDITOR instanceof TxtLimit
		   ||P_cmpEDITOR instanceof TxtNonNumLimit
		   ||P_cmpEDITOR instanceof TxtDate)
		{
			dceEDITR[P_intCOLID]=new DefaultCellEditor((JTextField)P_cmpEDITOR);
		}
		else if(P_cmpEDITOR instanceof JComboBox)
		{
			dceEDITR[P_intCOLID]=new DefaultCellEditor((JComboBox)P_cmpEDITOR);
		}
		else if(P_cmpEDITOR instanceof JCheckBox)
		{
			dceEDITR[P_intCOLID]=new DefaultCellEditor((JCheckBox)P_cmpEDITOR);
		}
		cmpEDITR[P_intCOLID]=P_cmpEDITOR;
		cmpEDITR[P_intCOLID].addKeyListener(this);
		dceEDITR[P_intCOLID].addCellEditorListener(this);
		dceEDITR[P_intCOLID].setClickCountToStart(1);
		this.getColumn(this.getColumnName(P_intCOLID)).setCellEditor(dceEDITR[P_intCOLID]);
	}
		/**
		 * To Enable/Dissable the table	 
		 * 
		 * @param P_flgSTATE ture : Enables table, false : disables table
		 */
	public void setEnabled(boolean P_flgSTATE)
	{
		setRowSelectionInterval(0,0);
		setColumnSelectionInterval(0,0);
		for(int i=0;i<cmpEDITR.length;i++)
		{
			cmpEDITR[i].setEnabled(P_flgSTATE);
		}
	}
	/**
	 * To set forecolor for a perticular cell
	 * 
	 * To set forecolor for a perticular cell
	 * 
	 * <p> Call to this method should be prior setValueAt()
	 * 
	 * @param	P_intROWINDEX : zero base Row Number
	 * @param	P_intCOLINDEX : Zero base Column Number
	 * @param	P_clrFORCL : Object of Color as forecolor	 
	 */
	public void setCellColor(int P_intROWINDEX,int P_intCOLINDEX,Color P_clrFORCL)
	{
		hstCELCL.put((String.valueOf(P_intROWINDEX)+"|"+String.valueOf(P_intCOLINDEX)),P_clrFORCL);
	}
	/**
	 *  To set forecolor for all cells in a column
	 * 
	 *  To set forecolor for all cells in a column 
	 * 
	 * <p> Call to this method should be prior to setValueAt(). However, repeated calls to setValueAt() do not need to setColor every time
	 * 
	 * @param	P_intROWINDEX Zero base row index
	 * @param	P_intCOLINDEX Zero base column index
	 * @param	P_clrFORCL : Object of Color as forecolor
	 */
	public void setColumnColor(int P_intCOLINDEX,Color P_clrFORCL)
	{
		hstCOLCL.put(String.valueOf(P_intCOLINDEX),P_clrFORCL);
	}
	/**
	 * To set Fore Color for a row
	 * 
	 * To set Fore Color for a row
	 * 
	 * <p>Call to this method should be prior to setValueAt(). However, repeated calls to setValueAt() do not need to setColor every time
	 * 
	 * @param	P_intROWINDEX Zero base row index
	 * @param	P_clrFORCL : Object of Color as forecolor
	 */
	public void setRowColor(int P_intROWINDEX,Color P_clrFORCL)
	{
		hstROWCL.put(String.valueOf(P_intROWINDEX),P_clrFORCL);
	}
	/**
	 * To set forecolor for all cells in a table
	 * 
	 * <p>Call to this method should be prior to setValueAt(). However, repeated calls to setValueAt() do not need to setColor every time
	 * 
	 * @param	P_clrFORCL : Object of Color as forecolor
	 */
	public void setTableColor(Color P_clrFORCL)
	{
		clrTBLFG=P_clrFORCL;
	}
	
	public Color getCellColor(int P_intROWID,int P_intCOLID)
	{
		if(hstCELCL.containsKey(String.valueOf(P_intROWID)+"|"+String.valueOf(P_intCOLID)))
			return (Color)hstCELCL.get(String.valueOf(P_intROWID)+"|"+String.valueOf(P_intCOLID));
		else if(hstCOLCL.containsKey(String.valueOf(P_intCOLID)))
			return (Color)hstCOLCL.get(String.valueOf(P_intCOLID));
		else if(hstROWCL.containsKey(String.valueOf(P_intROWID)))
			return (Color)hstROWCL.get(String.valueOf(P_intROWID));
		else 
			return clrTBLFG;
	}
	/**Clears contents of table	 */
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
			   ||L_STRTMP.equals("class TxtDate")
			   ||L_STRTMP.equals("class TxtTime"))
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
					setValueAt(" ",j,i);
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
	/**
	 * Programmatically starts editing the cell at row and column, if the cell is editable
	 * 
	 * <p>This method is over-ridden to remove descripancies in JDK provided method. <br>This method creates descripancy as it is not rooted thro' EventDelegation model. Please refer JavaDoc for details<br>
	 * 
	 */
	public boolean editCellAt(int P_intROWID,int P_intCOLID)
	{
		if(isEditing())
		getCellEditor().stopCellEditing();
		setRowSelectionInterval(P_intROWID,P_intROWID);
		setColumnSelectionInterval(P_intCOLID,P_intCOLID);
		setEditingRow(P_intROWID);
		setEditingColumn(P_intCOLID);
		boolean L_flgRTNVL=super.editCellAt(P_intROWID,P_intCOLID);
		intROWID=P_intROWID;
		intCOLID=P_intCOLID;
		cmpEDITR[P_intCOLID].requestFocus();
		doLayout();
		return L_flgRTNVL;
	}
	/**Specifies actions for certain key strokes if validation is through.<br>
	 * 'F2' : Nullify the default behavior of toggle between editing and stop editing of a cell
	 * Enter : Nevigate to next editable cell<br>
	 * UP : Nevigate to previous row, same column<br>
	 * DOWN : Nevigate to next row, same Column	 */
	public boolean processKeyBinding(KeyStroke ks,KeyEvent e,int condition,boolean pressed)
	{
		if(e.getKeyCode()==e.VK_F2)
		{
			return true;
		}
/*		if(e.getKeyCode()==e.VK_ESCAPE)
		{
			for(int i=0;i<getColumnCount();i++)
			{
				if(cmpEDITR[i] instanceof JTextField)
					setValueAt("",getSelectedRow(),i);
				else if(cmpEDITR[i] instanceof JComboBox)
					setValueAt(((JComboBox)cmpEDITR[i]).getItemAt(0),getSelectedRow(),i);
				else if(cmpEDITR[i] instanceof JCheckBox)
					setValueAt(Boolean.FALSE,getSelectedRow(),i);
			}
			editCellAt(getSelectedRow(),getSelectedColumn());
		}
		
*/		if(e.getKeyCode()==e.VK_ENTER&&flgFGAIN)
		{
			if(flgDTERR)//CHECKING ERROR IN DATE FORMAT
				return true;
			objOLDVL="";
			tivINPVF.setSource(this);
			if(!tivINPVF.verify(intROWID,getSelectedColumn()))
			{
				editCellAt(getSelectedRow(),getSelectedColumn());
				cmpEDITR[getSelectedColumn()].requestFocus();
				return false;
			}
			int row=0;
			int key=e.getKeyCode();
		//RETRIEVING NEXT FOCUSING CELL
			if(getSelectedColumn()==cmpEDITR.length-1)
			{
				int i=0;
				for(i=0;i<cmpEDITR.length;i++)
				{
					if(cmpEDITR[i].isEnabled())
						break;
				}
				if(!pressed)
				{
					if(getSelectedRow()<getRowCount()-1)
					{
						editCellAt(getSelectedRow()+1,i);
						java.awt.Point pt=((JViewport)getParent()).getViewPosition();
						pt.y=pt.y+getRowHeight();
						if(((JViewport)getParent()).getViewRect().height<(getSelectedRow()+1)*getRowHeight())
						((JViewport)getParent()).setViewPosition(pt);
					}
				}
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
					if(!pressed)
					{
						if(getSelectedRow()<getRowCount()-1)
						{
							editCellAt(getSelectedRow()+1,i);
//						unconfigureEnclosingScrollPane();
							java.awt.Point pt=((JViewport)getParent()).getViewPosition();
							pt.y=pt.y+getRowHeight();
							if(((JViewport)getParent()).getViewRect().height<(getSelectedRow()+1)*getRowHeight())
							((JViewport)getParent()).setViewPosition(pt);
//						super.processKeyBinding(ks,e,condition,pressed);
//						editCellAt(getSelectedRow(),i);
						}						
					}
				}
				else
				{
					if(!pressed)
					{
						editCellAt(getSelectedRow(),i);
//						unconfigureEnclosingScrollPane();

//						java.awt.Point pt=((JViewport)getParent()).getViewPosition();
//						pt.y=pt.y+getRowHeight();
//						((JViewport)getParent()).setViewPosition(pt);
//						super.processKeyBinding(ks,e,condition,pressed);
//						editCellAt(getSelectedRow(),i);
					}
				}
			}
//			super.processKeyBinding(ks,e,condition,pressed);
			return true;
		}
		else if(e.getKeyCode()==e.VK_DOWN)
		{
			flgERROR=!flgERROR;
			if(flgERROR)
			{
/*				if(getCellEditor()!=null)
					getCellEditor().stopCellEditing();
				if(!tivINPVF.verify(intROWID,intCOLID))
				{
					setRowSelectionInterval(intROWID,intROWID);
					setColumnSelectionInterval(intCOLID,intCOLID);
					setEditingRow(intROWID);
					setEditingColumn(intCOLID);
					editCellAt(intROWID,intCOLID);
					cmpEDITR[getSelectedColumn()].requestFocus();
				}
				else if(getSelectedRow()!=getRowCount()-1&&!(cmpEDITR[getSelectedColumn()] instanceof JComboBox))
				{
					if(getCellEditor()!=null)
					getCellEditor().stopCellEditing();
					intROWID=getSelectedRow();
					intCOLID=getSelectedColumn();
					editCellAt(intROWID+1,intCOLID);
				}
				else if(getSelectedRow()==getRowCount()-1)
				{
//					setColumnSelectionInterval(intCOLID,intCOLID);
//					setRowSelectionInterval(getSelectedRow(),getSelectedRow());
					editCellAt(getSelectedRow(),intCOLID);
				}
			}
			return true;
*/			
				
				if(getCellEditor()!=null)
					getCellEditor().stopCellEditing();
				if(!tivINPVF.verify(intROWID,intCOLID))
				{
					setRowSelectionInterval(intROWID,intROWID);
					setColumnSelectionInterval(intCOLID,intCOLID);
					setEditingRow(intROWID);
					setEditingColumn(intCOLID);
//					super.processKeyBinding(ks,e,condition,pressed);
					editCellAt(intROWID,intCOLID);
					cmpEDITR[getSelectedColumn()].requestFocus();
				}
				else if(getSelectedRow()!=getRowCount()-1&&!(cmpEDITR[getSelectedColumn()] instanceof JComboBox))
				{
					super.processKeyBinding(ks,e,condition,pressed);
//					if(getCellEditor()!=null)
//					getCellEditor().stopCellEditing();
					intROWID=getSelectedRow();
					intCOLID=getSelectedColumn();
					editCellAt(intROWID,intCOLID);
				}
				else if(getSelectedRow()==getRowCount()-1)
				{
					setColumnSelectionInterval(intCOLID,intCOLID);
					setRowSelectionInterval(getSelectedRow(),getSelectedRow());
					editCellAt(getSelectedRow(),intCOLID);
				}
			}
		//	return true;
			
			return true;
		}
		else if(e.getKeyCode()==e.VK_UP)
		{
			flgERROR=!flgERROR;
			if(flgERROR)
			{
				if(getCellEditor()!=null)
					getCellEditor().stopCellEditing();
				if(!tivINPVF.verify(intROWID,intCOLID))
				{
					intROWID=getSelectedRow();
					setRowSelectionInterval(intROWID,intROWID);
					setColumnSelectionInterval(intCOLID,intCOLID);
					setEditingRow(intROWID);
					setEditingColumn(intCOLID);
					editCellAt(intROWID,getSelectedColumn());
					cmpEDITR[getSelectedColumn()].requestFocus();
				}
				else if(getSelectedRow()!=0&&!(cmpEDITR[getSelectedColumn()] instanceof JComboBox))
				{
					super.processKeyBinding(ks,e,condition,pressed);
					editCellAt(getSelectedRow(),getSelectedColumn());
					cmpEDITR[getSelectedColumn()].requestFocus();
					
				}
				else if(getSelectedRow()==0)
				{
					editCellAt(0,intCOLID);
				}
			}
			return true;
		}
		else if(e.getKeyCode()==e.VK_TAB||e.getKeyCode()==37||e.getKeyCode()==38)
			return true;
		return true;//super.processKeyBinding(ks,e,condition,pressed);
	} 
	/**Remember Editing Row & column if editor compoenent is not JComboBox	 */
	public void editingStopped(ChangeEvent e)
	{
		if(isEditing())
		{
			if(cmpEDITR[intCOLID] instanceof TxtLimit)
				((TxtLimit)cmpEDITR[intCOLID]).focusLost(new FocusEvent(cmpEDITR[intCOLID],FocusEvent.FOCUS_LOST));
			if( cmpEDITR[getSelectedColumn()] instanceof TxtNumLimit )
				((TxtNumLimit)cmpEDITR[getSelectedColumn()]).focusLost(new FocusEvent(cmpEDITR[getSelectedColumn()],FocusEvent.FOCUS_LOST));
			if( cmpEDITR[intCOLID] instanceof TxtNonNumLimit )
			   ((TxtNonNumLimit)cmpEDITR[intCOLID]).focusLost(new FocusEvent(cmpEDITR[intCOLID],FocusEvent.FOCUS_LOST));
			if( cmpEDITR[intCOLID] instanceof TxtDate)
			   if(((JTextField)cmpEDITR[intCOLID]).getText().length()>0 && ((TxtDate)cmpEDITR[intCOLID]).vldDATE()!=null)
				   return;
		}
		if(!(cmpEDITR[getSelectedColumn()] instanceof JComboBox))
		{
			intROWID=getSelectedRow();
			intCOLID=getSelectedColumn();
			objOLDVL=getValueAt(intROWID,intCOLID);
		}
		super.editingStopped(e);
	}
	/**Disable nevigation by left or right arrow keys	 */
	public void processKeyEvent(KeyEvent L_KE)
	{
		if(L_KE.getKeyCode()==L_KE.VK_LEFT||L_KE.getKeyCode()==L_KE.VK_RIGHT)
			L_KE.consume();
		else
			super.processKeyEvent(L_KE);
	}
	/**Empty definition of implementation of interface	 */
	public void mouseClicked(MouseEvent L_ME)
	{}
	/**
	 * Checks for date format error and otherwise, remembers old value in the cell
	 */
	public void mousePressed(MouseEvent L_ME)
	{
		flgDTERR=false;
		if(cmpEDITR[intCOLID] instanceof TxtDate && ((JTextField)cmpEDITR[intCOLID]).getText().length()==0)
		{
			flgDTERR=false;
		}
		else
		{
			if(cmpEDITR[intCOLID] instanceof TxtDate)
				if(((TxtDate)cmpEDITR[intCOLID]).vldDATE()!=null)
					flgDTERR=true;
				else
					flgDTERR=false;
			else 
				flgDTERR=false;
		}
		if(flgDTERR)
		{
			Object L_objOLDVL=objOLDVL.toString();//Making Copy of old value
				if (objOLDVL.equals(Boolean.FALSE))
					L_objOLDVL=Boolean.FALSE;//Making Copy of old value
				else if (objOLDVL.equals(Boolean.TRUE))
					L_objOLDVL=Boolean.TRUE;//Making Copy of old value
			setValueAt(objOLDVL,intROWID,intCOLID);
			super.editCellAt(intROWID,intCOLID);
			cmpEDITR[intCOLID].requestFocus();
			if(cmpEDITR[intCOLID] instanceof JTextField)
				((JTextField)cmpEDITR[intCOLID]).setText(L_objOLDVL.toString());
			else if(cmpEDITR[intCOLID] instanceof JComboBox)
				((JComboBox)cmpEDITR[intCOLID]).setSelectedItem(L_objOLDVL);
			else if(cmpEDITR[intCOLID] instanceof JCheckBox)
				((JCheckBox)cmpEDITR[intCOLID]).setSelected(((Boolean)L_objOLDVL).booleanValue());
			}
	}
	/**Call validation and allow cell selection by mouse click if validation is true, else revert back to original cell	 */
	public void mouseReleased(MouseEvent L_ME)
	{
		if(flgDTERR)
			getCellEditor().cancelCellEditing();
		if(!flgDTERR)
		if(!tivINPVF.verify(intROWID,intCOLID))
		{
			Object L_objOLDVL=objOLDVL.toString();//Making Copy of old value
				if (objOLDVL.equals(Boolean.FALSE))
					L_objOLDVL=Boolean.FALSE;//Making Copy of old value
				else if (objOLDVL.equals(Boolean.TRUE))
					L_objOLDVL=Boolean.TRUE;//Making Copy of old value
			setValueAt(objOLDVL,intROWID,intCOLID);
			editCellAt(intROWID,intCOLID);
			cmpEDITR[intCOLID].requestFocus();
			if(cmpEDITR[intCOLID] instanceof JTextField)
				((JTextField)cmpEDITR[intCOLID]).setText(L_objOLDVL.toString());
			else if(cmpEDITR[intCOLID] instanceof JComboBox)
				((JComboBox)cmpEDITR[intCOLID]).setSelectedItem(L_objOLDVL);
			else if(cmpEDITR[intCOLID] instanceof JCheckBox)
				((JCheckBox)cmpEDITR[intCOLID]).setSelected(((Boolean)L_objOLDVL).booleanValue());
		}
	}
	/**Empty definition for interface implementation	 */
	public void mouseEntered(MouseEvent L_ME)
	{}
	/**Empty definition for interface implementation	 */
	public void mouseExited(MouseEvent L_ME)
	{}
	/** set flgFGAIN to true*/
	public void keyPressed(KeyEvent L_KE){flgFGAIN=true;}
	/**Empty definition for interface implementation	 */
	public void keyReleased(KeyEvent L_KE){}
	/** Set checkbox at index zero to checked */
	public void keyTyped(KeyEvent L_KE)
	{
		setValueAt(Boolean.TRUE,getSelectedRow(),0);
	}
	/**Disables Column dragging	 */
	public void columnMoved(TableColumnModelEvent e)
	{
		flgCOLMV=!flgCOLMV;
		if(flgCOLMV)
			moveColumn(e.getToIndex(),e.getFromIndex());
	}
	/** Class for default definition of input varifier for this table, with no effect	 */
	private class DefaultTableInputVerifier extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			if(cmpEDITR[P_intCOLID] instanceof TxtDate)
			{
				if(((JTextField)cmpEDITR[intCOLID]).getText().length()==0)
					return true;
				String L_STRMSG=((TxtDate)cmpEDITR[P_intCOLID]).vldDATE();
				if(L_STRMSG!=null)
					return false;
			}
			return true;
		}
	}
	/**
	 * This class implements cell redering for  cl_JTable class. <br>
	 * 
	 * Display of any value in the table will be routed thru this class.<br> This class will in turn set the forecolor for display as per color set by the user.<br>
	 * Foreground color change after display of the value (i.e. call to setValueAt()) will not have any effect<br>
	 * This will also ensure that, alignment of numeric data is right aligned and that for alphanumeric is keft aligned.<br>
	 * User can define his own renderer to override default rendering 
	 */
	private class MyTableCellRenderer extends DefaultTableCellRenderer
	{
		/**
		 * To set Foreground and alignment.
		 * 
		 * To set Foreground and alignment.
		 * 
		 * <p>This method is called by JVM before call to paint() method of Component class (which does the job of display of value being set at the cell).
		 * <br> The priority set for selection of foreground is as follows :<br>
		 * 1. Cell Color<br>
		 * 2. Row Color<br>
		 * 3. Column Color<br>
		 * 4. Table Color<br>
		 * This means, Cell Color is given the highest prority while, table color is the last one
		 */
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
			if(table.getValueAt(row,col) instanceof Number)
				setHorizontalAlignment(JTextField.RIGHT);
			return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
		}
	}
}

/**Abstract Class for validation of data entered by user in the table cell<br>
 * User is to extend this class for defining validations in the table. */
abstract class TableInputVerifier 
{
	private cl_JTable objSOURC;
	/**Method to verify the contents of the cell. This method is called before focus is transfered from a cell to another after editing.
	 * @param	P_intCOLID ColumnIndex for which validation is fired
	 * @return	true if validation is successful & false if validation failed.
	 */
	abstract public boolean verify(int P_intROWID,int P_intCOLID) ;
	/**
	 * Method to get table object generating the event
	 */
	public cl_JTable getSource()
	{
		return objSOURC;
	}
	/**
	 * Method to set table object generating the even
	 * 
	 * <p> This method is useful to call inputvarify programatically<br>Ideally, this call should be before stopping editing of a cell programatically.
	 */
	public void setSource(cl_JTable P_objSOURC)
	{
		objSOURC=P_objSOURC;
	}
}