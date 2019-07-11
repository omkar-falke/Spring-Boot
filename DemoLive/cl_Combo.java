import javax.swing.JComboBox;
import java.awt.event.*;
import java.util.Vector;

/**
 * This class is designed to give additional functionalities to JComboBox provided by JDK. The L&F will be as same as JDK. Additional functionalities provided are :
 * <p>
 *   <FONT color=purple><STRONG><BLOCKQUOTE style="MARGIN-RIGHT: 0px">  <P align=left><FONT face="LotusWP Icon">/&nbsp;&nbsp;&nbsp;</FONT>   </STRONG></FONT>Disable   selection change with mouse when ComboBox is not having focus </P>  <P align=left><FONT face="LotusWP Icon"><FONT   color=purple><STRONG>/</STRONG></FONT>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Facilitates   storage of codes against description as item in combo</P>  <P align=left><FONT face="LotusWP Icon"><FONT   color=purple><STRONG>/</STRONG></FONT>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Set   Tool-tip text to item selected<FONT face="LotusWP Icon"><FONT   color=purple><STRONG></STRONG></FONT></FONT></P>  <P align=left><FONT face="LotusWP Icon"><FONT   color=purple><STRONG>/</STRONG></FONT>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Pop-up the combo on Focus gain<BR></P></BLOCKQUOTE><FONT   face="LotusWP Icon"><STRONG></STRONG></FONT> 
 * <p>
 * Only default constructor is overridden as other constructors doesnt confirm response to all events and methods in the class. e.g. addItem() etc Please refer to JavaDoc for datails.
 * 
 */
class cl_Combo extends JComboBox implements ActionListener, ItemListener, FocusListener
{
	/**Referance to old selected value	 */
	private Object objOLDSL;/**Vector to store item codes*/
	Vector<Object> vtrITMCD;
	
	/**
	 * Default construtor
	 * 
	 * Default Constructor
	 * 
	 * <p>After call to super, initialises vector for item code and registers eventlistners
	 */
	cl_Combo()
	{
		super();
		vtrITMCD=new Vector<Object>(10,2);
		addItemListener(this);addActionListener(this);addFocusListener(this);
	}
	
	/**
	 * Adds an item to the item list. 
	 * 
	 * Adds an item to the item list. This method will work alsays as  the JComboBox uses a mutable data model. 
	 * 
	 * <p>Adds item to the list and stores corresponding code to the vector of Codes
	 * 
	 * @param P_objITMDS Object to be added to the list
	 * @param P_objITMCD Object to be stored as code against the object added to the list<br>
	 * 
	 * <b>USAGE : </B><br>
	 * addItem("Object","Code");<br>
	 * Adds "object" to the item list of Combo and Stores "Code" in the vector for codes
	 */
	public void addItem(Object P_objITMDS,Object P_objITMCD)
	{
		super.addItem(P_objITMDS);
		vtrITMCD.addElement(P_objITMCD);
	}
	
	/**
	 * Inserts an item into the item list at a given index. 
	 * 
	 * Inserts an item into the item list at a given index. This method will work always as the JComboBox uses a mutable data model. 
	 * 
	 * <p>Inserts item to the list at specified index and stores corresponding code to the vector of Codes at the same index
	 * 
	 * @param P_objITMDS the Object to add to the list
	 * @param P_objITMCD Object to be stored as code against the object added to the list
	 * @param P_intINDEX an integer specifying the position at which to add the item<br>
	 * 
	 * <b>USAGE : </B><br>
	 * insertItemAt("Object","Code",5);<br>
	 * Inserts "object" to the item list of Combo at position 5 and Stores "Code" in the vector for codes at position 5
	 */
	public void insertItemAt(Object P_objITMDS,Object P_objITMCD,int P_intINDEX)
	{
		super.insertItemAt(P_objITMDS,P_intINDEX);
		vtrITMCD.insertElementAt(P_objITMCD,P_intINDEX);
	}
	
	/**
	 * Removes all items from the item list
	 * 
	 * Removes all items from the item list
	 */
	public void removeAllItems() 
	{
		super.removeAllItems();
		vtrITMCD.removeAllElements();
	}
	
	/**
	 * Removes an item from the item list. 
	 * 
	 * Removes an item from the item list. This method works always as the JComboBox uses a mutable data model
	 * 
	 * <p>Removes the item from the list as well as corresponding code from the vector
	 * 
	 * @param anObject the object to remove from the item list
	 */
	public void removeItem(Object anObject) 
	{
		int L_intINDEX=-1;
		for(L_intINDEX=0;L_intINDEX<getItemCount();L_intINDEX++)
			if(getItemAt(L_intINDEX).equals(anObject))
				break;
		if(L_intINDEX == getItemCount()-1 && ! getItemAt(getItemCount()-1).equals(anObject))
			L_intINDEX=-1;
		super.removeItem(anObject);
		if(L_intINDEX != -1 && vtrITMCD.size()>=L_intINDEX)
			vtrITMCD.removeElementAt(L_intINDEX);
	}
	/**
	 * Removes the item at anIndex 
	 * 
	 * Removes the item at anIndex This method works always as the JComboBox uses a mutable data model
	 * 
	 * <p> Removes the item and corresponding code from vector
	 * 
	 * @param anIndex an int specifying the index of the item to remove, where 0 indicates the first item in the list
	 */
	public void removeItemAt(int anIndex)
	{
		super.removeItemAt(anIndex);
		if(vtrITMCD.size()>=anIndex)
			vtrITMCD.removeElementAt(anIndex);
	}
	
	/**
	 * Returns the item code at the selected index. If index is out of range (less than zero or greater than or equal to size of code vector) it will return null
	 */
	public Object getITMCD()
	{
		if(getSelectedIndex()<vtrITMCD.size())
			return vtrITMCD.elementAt(getSelectedIndex());
		else 
			return null;
	}
	
	/**
	 * Returns the item code at the spcified index. If index is out of range (less than zero or greater than or equal to size of code vector) it will return null
	 */
	public Object getITMCDAt(int P_intINDEX)
	{
		if(P_intINDEX<vtrITMCD.size())
			return vtrITMCD.elementAt(P_intINDEX);
		else 
			return null;
	}
	
	/**
	 * To disable selection change by mouse when combo is not having focus on it
	 * 
	 * <p>If combo is not having focus (i.e. objOLDSL != null), set ComboBox selection to original and registers the ItemListener which was un-registered in ItemStateChanged() to avoid recurssive loop.
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		if(objOLDSL!=null)
		{
			setSelectedItem(objOLDSL);
			objOLDSL=null;
			addItemListener(this);
		}
	}
	/**
	 * To disable selection change by mouse when Combo is not having focus on it
	 * 
	 * <p>Sets tool-tip text to selected Item<br>
	 * If ComboBox dosent have focus on it and state change is 2 (i.e. prior to new item selection procedure), copies refernce of selected item to objOLDSL and unregisters ItemListener.<br>
	 * Item listener is unregistered to avoid recurssive loop during selection change in actionPerformed()<br>
	 * If ComboBox is having focus, sets objOLDSL to null
	 */
	public void itemStateChanged(ItemEvent L_IE)
	{
		setToolTipText(L_IE.getItem().toString());		
		if(!hasFocus()&&L_IE.getStateChange()==2)
		{
			objOLDSL=L_IE.getItem();
			removeItemListener(this);
		}
		else
			objOLDSL=null;
	}
	/** To Pop-up the contents automatically when focus is gained	 */
	public void focusGained(FocusEvent L_FE)
	{
		try
		{
			showPopup();
		}catch(Exception e){System.out.println(e);}
	}
	/**Emplty definition of interface method	 */
	public void focusLost(FocusEvent L_FE)
	{}
}