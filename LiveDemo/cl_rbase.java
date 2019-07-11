import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.io.*;
import javax.print.attribute.*;import javax.print.*;import javax.print.event.*;import javax.print.attribute.standard.*;
import javax.swing.JOptionPane;
/**This class is a base class for all reports. <br>In this class, First row of the screen components is reserved for Available pronter list, report from date and to date. <br>Printer list is derived dynamically by JVM. <br>Respective methods for printing the report send the said file (as aurgument) to the said or selected printer. <br>Also, in E-mail option, on pressing F1 on the E-mail ID combo, user gets ID's available in HRMS from which user can select required ID. <br>Developer has to populate this Combo for default receipents of this report.<br>All members are available for child classes.
 */
class cl_rbase extends cl_pbase
{
	protected int intCOLCT;
	protected JLabel M_lblDESTN,M_lblFMDAT,M_lblTODAT;
	protected JTextField M_txtDESTN;
	protected JPanel M_pnlRPFMT;
	JComboBox M_cmbDESTN;
	protected TxtDate M_txtFMDAT;
	protected TxtDate M_txtTODAT;
	protected Vector<String> M_vtrEMAIL;
	//protected Vector M_vtrPRNTR = new Vector();
	protected cl_JTBL M_tblEMAIL;
	//protected DocFlavor flavor;
	//protected PrintRequestAttributeSet aset;
	//protected PrintService[] pservices;	
	protected int M_intPAGNO;
	protected int M_intLINNO;
	protected JRadioButton M_rdbTEXT;
	protected JRadioButton M_rdbHTML;
	/**Constructor for the class.<br>Takes SBS Level as aurgument and passes it to cl_pbase for SBS Combo generation. <br> Lays first row of default components.
	 */
	cl_rbase(int P_intSBSLV)
	{
		super(P_intSBSLV);
		try{
			setMatrix(20,8);
			add(M_lblDESTN=new JLabel("File : "),1,1,1,0.75,this,'L');
			add(M_txtDESTN=new JTextField(),1,2,1,3,this,'L');
			add(M_cmbDESTN=new JComboBox(),1,2,1,3,this,'L');
			add(M_lblFMDAT=new JLabel("From Date : "),1,4,1,1,this,'R');
			add(M_txtFMDAT=new TxtDate(),1,5,1,1,this,'L');
			add(M_lblTODAT=new JLabel("To Date : "),1,6,1,1,this,'L');
			add(M_txtTODAT=new TxtDate(),1,7,1,1,this,'L');
			M_pnlRPFMT=new JPanel(null);
			add(M_rdbTEXT=new JRadioButton("Text"),1,1,1,0.9,M_pnlRPFMT,'L');
			add(M_rdbHTML=new JRadioButton("HTML"),2,1,1,0.9,M_pnlRPFMT,'L');
			ButtonGroup btg=new ButtonGroup();
			M_rdbTEXT.setSelected(true);
			btg.add(M_rdbTEXT);btg.add(M_rdbHTML);
			M_pnlRPFMT.setBorder(BorderFactory.createTitledBorder("Report Format"));
			add(M_pnlRPFMT,1,8,3,1.1,this,'L');
			M_pnlRPFMT.setVisible(false);
			M_lblDESTN.setVisible(false);M_lblFMDAT.setVisible(false);M_lblTODAT.setVisible(false);
			M_txtDESTN.setVisible(false);M_txtFMDAT.setVisible(false);M_txtTODAT.setVisible(false);
			M_cmbDESTN.setVisible(false);
		/*	flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//					flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_US_ASCII;
			aset = new HashPrintRequestAttributeSet();
			pservices =PrintServiceLookup.lookupPrintServices(flavor, aset);
			System.out.println("pservices.length "+pservices.length);
			for(int i=0;i<pservices.length;i++)
			{
				//M_cmbDESTN.addItem(pservices[i].getName());
				M_vtrPRNTR.addElement(pservices[i].getName());
			}*/
		}catch(Exception e)
		{setMSG(e,"cl_rbase constructor");}
	}
	cl_rbase()
	{
		try{
			setMatrix(20,6);
			add(M_lblDESTN=new JLabel("File : "),1,1,1,0.75,this,'L');
			//add(M_txtDESTN=new JTextField(),1,2,1,1,this,'L');
			//add(M_cmbDESTN=new JComboBox(),1,2,1,1.25,this,'R');
			add(M_txtDESTN=new JTextField(),1,2,1,2,this,'L');
			add(M_cmbDESTN=new JComboBox(),1,2,1,2,this,'R');
			add(M_lblFMDAT=new JLabel("From Date : "),1,3,1,0.95,this,'R');
			add(M_txtFMDAT=new TxtDate(),1,4,1,1,this,'L');
			add(M_lblTODAT=new JLabel("To Date : "),1,5,1,1,this,'L');
			add(M_txtTODAT=new TxtDate(),1,6,1,1,this,'L');
			M_lblDESTN.setVisible(false);M_lblFMDAT.setVisible(false);M_lblTODAT.setVisible(false);
			M_txtDESTN.setVisible(false);M_txtFMDAT.setVisible(false);M_txtTODAT.setVisible(false);
			M_cmbDESTN.setVisible(false);
			M_pnlRPFMT=new JPanel(null);
			add(M_rdbTEXT=new JRadioButton("Text"),1,1,1,0.9,M_pnlRPFMT,'L');
			add(M_rdbHTML=new JRadioButton("HTML"),2,1,1,0.9,M_pnlRPFMT,'L');
			ButtonGroup btg=new ButtonGroup();
			btg.add(M_rdbTEXT);btg.add(M_rdbHTML);
			M_pnlRPFMT.setBorder(BorderFactory.createTitledBorder("Report Format"));
			add(M_pnlRPFMT,1,8,3,1.1,this,'L');
			M_pnlRPFMT.setVisible(false);
			M_lblDESTN.setVisible(false);M_lblFMDAT.setVisible(false);M_lblTODAT.setVisible(false);
			M_txtDESTN.setVisible(false);M_txtFMDAT.setVisible(false);M_txtTODAT.setVisible(false);
			M_cmbDESTN.setVisible(false);
/*			flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//					flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_US_ASCII;
			aset = new HashPrintRequestAttributeSet();
			pservices =PrintServiceLookup.lookupPrintServices(flavor, aset);
			System.out.println("pservices.length "+pservices.length);
			for(int i=0;i<pservices.length;i++)
			{
				//M_cmbDESTN.addItem(pservices[i].getName());
				M_vtrPRNTR.addElement(pservices[i].getName());
			}*/
		//	System.out.println("Vector");
				
		}catch(Exception e)
		{setMSG(e,"cl_rbase constructor");}
	}
	/**Method to transfer data to specified printer.
	 * @param LP_strFILNM : Fully qualified name of the file to be printed 
	 * @param LP_intPRNNO : Index of the printer to which the file is to be printed. Normally, same as index of the printer in the Printer selection combo.
	 */
	protected void doPRINT(String LP_strFILNM,int LP_intPRNNO)
	{
		try
		{
			if(cl_dat.M_thrPRNLS_pbst !=null)
			{
				//JOptionPane.showMessageDialog(this,"Fetching Printer list .. Please wait " ," ",JOptionPane.INFORMATION_MESSAGE);
				cl_dat.M_thrPRNLS_pbst.join();
			}
			if(LP_intPRNNO>M_cmbDESTN.getItemCount()-1)
				throw( new Exception("Illegal Printer Index"));
			DocPrintJob job=cl_dat.pservices[LP_intPRNNO].createPrintJob();
			PrintJobWatcher pjDone = new PrintJobWatcher(job);
			InputStream is = new BufferedInputStream(new FileInputStream(LP_strFILNM));
			DocAttributeSet daset = new HashDocAttributeSet();
			Doc d=new SimpleDoc(is,cl_dat.flavor,daset);
			InputStream inputStream = d.getStreamForBytes();
			job.print(d,null);
			pjDone.waitForDone();
			inputStream.close();
		}catch(Exception e)
		{setMSG(e,"doPRINT");}
	}
	/**Method to transfer the file to printer selected by user.
	 * @param LP_FILNM : Fully quallified name of the file 
	 * @exception : Illegal printer selection - If selected index of the Combo is 0.
	 */
	@SuppressWarnings("unchecked") protected void doPRINT(String LP_strFILNM)
	{
		try
		{
			if(cl_dat.M_thrPRNLS_pbst !=null)
			{
				//JOptionPane.showMessageDialog(this,"Fetching Printer list .. Please wait " ," ",JOptionPane.INFORMATION_MESSAGE);
				cl_dat.M_thrPRNLS_pbst.join();
			}
			
			if(M_cmbDESTN.getSelectedIndex()==0)
				throw( new Exception("Illegal printer selection"));
			
			DocPrintJob job=cl_dat.pservices[M_cmbDESTN.getSelectedIndex()-1].createPrintJob();
			//job.printDialog();
			Class[] cl=cl_dat.pservices[M_cmbDESTN.getSelectedIndex()-1].getSupportedAttributeCategories();
			
//getDefaultAttributeValue(java.lang.Class<? extends javax.print.attribute.Attribute>) in 
//javax.print.PrintService cannot be applied to (java.lang.Class<?>)


			for (int i=0;i<cl.length;i++)
			{	if(cl[i].toString().equals("class <? extends javax.print.attribute.standard.Media>"))
					System.out.println(cl[i].toString()+cl_dat.pservices[M_cmbDESTN.getSelectedIndex()-1].getDefaultAttributeValue(cl[i]));}
//			 getAttribute
/*class javax.print.attribute.standard.JobName  Java Printing
class javax.print.attribute.standard.RequestingUserName  aap
class javax.print.attribute.standard.Copies  1
class javax.print.attribute.standard.Destination  file:/C:/splerp/out.prn
class javax.print.attribute.standard.OrientationRequested  portrait
class javax.print.attribute.standard.PageRanges  1-2147483647
class javax.print.attribute.standard.Media  na-letter
class javax.print.attribute.standard.MediaPrintableArea  (0.0,0.0)->(215.9,279.4
)mm
			
			class javax.print.attribute.standard.JobName
class javax.print.attribute.standard.RequestingUserName
class javax.print.attribute.standard.Copies
class javax.print.attribute.standard.Destination
class javax.print.attribute.standard.OrientationRequeste
class javax.print.attribute.standard.PageRanges
class javax.print.attribute.standard.Media
class javax.print.attribute.standard.MediaPrintableArea
class javax.print.attribute.standard.Fidelity
class sun.print.SunAlternateMedia
class javax.print.attribute.standard.Chromaticity
class javax.print.attribute.standard.SheetCollate
class javax.print.attribute.standard.PrinterResolution


			*/
			PrintJobWatcher pjDone = new PrintJobWatcher(job);
			InputStream is = new BufferedInputStream(new FileInputStream(LP_strFILNM));
			DocAttributeSet daset = new HashDocAttributeSet();
			Doc d=new SimpleDoc(is,cl_dat.flavor,daset);
			InputStream inputStream = d.getStreamForBytes();
			job.print(d,null);
			pjDone.waitForDone();
			inputStream.close();
		}catch(Exception e)
		{setMSG(e,"doPRINT");}
	}
	public JComboBox getPRNLS()
	{
		JComboBox L_cmbTEMP ;
		try
		{
		//	String [] L_staTEMP =getPRINTERS();
			if(cl_dat.M_thrPRNLS_pbst !=null)
			{
				//JOptionPane.showMessageDialog(this,"Fetching Printer list .. Please wait " ," ",JOptionPane.INFORMATION_MESSAGE);
			//	System.out.println("inside join");						
				cl_dat.M_thrPRNLS_pbst.join();
			}
			//	System.out.println("out of join");						
		    String [] L_staTEMP=new String[cl_dat.M_vtrPRNTR_pbst.size()];
			for(int i=0;i<cl_dat.M_vtrPRNTR_pbst.size();i++)
			{
				L_staTEMP[i]=cl_dat.M_vtrPRNTR_pbst.elementAt(i).toString();
			}
			//System.out.println("after getprinters");
			L_cmbTEMP=new JComboBox(L_staTEMP);
			cl_dat.M_intPRIND_pbst = JOptionPane.showConfirmDialog( this,L_cmbTEMP,"Select Printer",JOptionPane.OK_CANCEL_OPTION);
			//System.out.println("from class "+cl_dat.M_intPRIND_pbst);
			M_cmbDESTN=L_cmbTEMP;
			return L_cmbTEMP;
		}
		catch(Exception L_E)
		{
			System.out.println(L_E.toString());
		}
		return null;
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
			{
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				String L_strTEMP=cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString();
				if(L_strTEMP.equals(cl_dat.M_OPRSL_pbst))
				{
					if(M_vtrSCCOMP.contains(M_lblDESTN)) M_lblDESTN.setVisible(false);
					if(M_vtrSCCOMP.contains(M_lblFMDAT)) M_lblFMDAT.setVisible(false);
					if(M_vtrSCCOMP.contains(M_lblTODAT)) M_lblTODAT.setVisible(false);
					if(M_vtrSCCOMP.contains(M_txtDESTN)) M_txtDESTN.setVisible(false);
					if(M_vtrSCCOMP.contains(M_txtFMDAT)) M_txtFMDAT.setVisible(false);
					if(M_vtrSCCOMP.contains(M_txtTODAT)) M_txtTODAT.setVisible(false);
					if(M_vtrSCCOMP.contains(M_cmbDESTN)) M_cmbDESTN.setVisible(false);
				}
				else if(L_strTEMP.equals(cl_dat.M_OPFIL_pbst))
				{
					if(M_vtrSCCOMP.contains(M_lblDESTN)) M_lblDESTN.setText("File : ");
					if(M_vtrSCCOMP.contains(M_lblDESTN)) M_lblDESTN.setVisible(true);
					if(M_vtrSCCOMP.contains(M_lblFMDAT)) M_lblFMDAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_lblTODAT)) M_lblTODAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtDESTN)) M_txtDESTN.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtFMDAT)) M_txtFMDAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtTODAT)) M_txtTODAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_cmbDESTN)) M_cmbDESTN.setVisible(false);
				}
				else if(L_strTEMP.equals(cl_dat.M_OPFAX_pbst))
				{
					if(M_vtrSCCOMP.contains(M_lblDESTN)) M_lblDESTN.setText("E-mail ID: ");
					if(M_vtrSCCOMP.contains(M_lblDESTN)) M_lblDESTN.setVisible(true);
					if(M_vtrSCCOMP.contains(M_lblFMDAT)) M_lblFMDAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_lblTODAT)) M_lblTODAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtDESTN)) M_txtDESTN.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtFMDAT)) M_txtFMDAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtTODAT)) M_txtTODAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_cmbDESTN)) M_cmbDESTN.setVisible(false);
				}
				else if(L_strTEMP.equals(cl_dat.M_OPPRN_pbst))
				{
					if(cl_dat.M_thrPRNLS_pbst !=null)
					{
						//JOptionPane.showMessageDialog(this,"Fetching Printer list .. Please wait " ," ",JOptionPane.INFORMATION_MESSAGE);
						cl_dat.M_thrPRNLS_pbst.join();
					}
					//System.out.println("Printer Selected");
					if(M_vtrSCCOMP.contains(M_lblDESTN)) M_lblDESTN.setText("Printer : ");
					if(M_vtrSCCOMP.contains(M_lblDESTN)) M_lblDESTN.setVisible(true);
					if(M_vtrSCCOMP.contains(M_lblFMDAT)) M_lblFMDAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_lblTODAT)) M_lblTODAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtDESTN)) M_txtDESTN.setVisible(false);
					if(M_vtrSCCOMP.contains(M_txtFMDAT)) M_txtFMDAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtTODAT)) M_txtTODAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_cmbDESTN)) M_cmbDESTN.setVisible(true);
					if(M_vtrSCCOMP.contains(M_cmbDESTN)) M_cmbDESTN.removeAllItems();
					if(M_vtrSCCOMP.contains(M_cmbDESTN)) M_cmbDESTN.addItem("Select Printer");
					
/*EXMAPLE IN PrintService Documentation
   DocFlavor flavor = DocFlavor.INPUT_STREAM.POSTSCRIPT;
   PrintRequestAttributeSet aset = new HashPrintRequestHashAttributeSet();
   aset.add(MediaSizeName.ISO_A4);
   PrintService[] pservices =
                 PrintServiceLookup.lookupPrintServices(flavor, aset);
					if (pservices.length > 0) {
       DocPrintJob pj = pservices[0].createPrintJob();
       // InputStreamDoc is an implementation of the Doc interface //
       Doc doc = new InputStreamDoc("test.ps", flavor);
       try {
             pj.print(doc, aset);
        } catch (PrintException e) { 
        }
   }
   
   
*/
				/*	flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//					flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_US_ASCII;
					aset = new HashPrintRequestAttributeSet();
					pservices =PrintServiceLookup.lookupPrintServices(flavor, aset);
					for(int i=0;i<pservices.length;i++)
					{
						//M_cmbDESTN.addItem(pservices[i].getName());
						M_vtrPRNTR.addElement(pservices[i].getName());
					}
					System.out.println("Vector");*/
					for(int i=0;i<cl_dat.M_vtrPRNTR_pbst.size();i++)
					{
						//System.out.println(cl_dat.M_vtrPRNTR_pbst.elementAt(i));
						M_cmbDESTN.addItem(cl_dat.M_vtrPRNTR_pbst.elementAt(i));
						
					}
					//System.out.println("Added from Vector");
					if(M_cmbDESTN.getItemCount()==2)
					{
						M_cmbDESTN.setSelectedIndex(1);
						M_cmbDESTN.setEnabled(false);
					}
				}
				else if(L_strTEMP.equals(cl_dat.M_OPSCN_pbst))
				{
					if(M_vtrSCCOMP.contains(M_lblDESTN)) M_lblDESTN.setVisible(false);
					if(M_vtrSCCOMP.contains(M_lblFMDAT)) M_lblFMDAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_lblTODAT)) M_lblTODAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtDESTN)) M_txtDESTN.setVisible(false);
					if(M_vtrSCCOMP.contains(M_txtFMDAT)) M_txtFMDAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtTODAT)) M_txtTODAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_cmbDESTN)) M_cmbDESTN.setVisible(false);
				}
				else if(L_strTEMP.equals(cl_dat.M_OPEML_pbst))
				{
					M_lblDESTN.setText("Mail To : ");
					if(M_vtrSCCOMP.contains(M_lblDESTN)) M_lblDESTN.setVisible(true);
					if(M_vtrSCCOMP.contains(M_lblFMDAT)) M_lblFMDAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_lblTODAT)) M_lblTODAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtDESTN)) M_txtDESTN.setVisible(false);
					if(M_vtrSCCOMP.contains(M_txtFMDAT)) M_txtFMDAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_txtTODAT)) M_txtTODAT.setVisible(true);
					if(M_vtrSCCOMP.contains(M_cmbDESTN)) M_cmbDESTN.setVisible(true);
					if(M_vtrSCCOMP.contains(M_cmbDESTN)) M_cmbDESTN.removeAllItems();
				}
			}
		}catch(Exception e)
		{setMSG(e,"cl_rbase.actionperformed");}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(M_objSOURC==M_cmbDESTN&&L_KE.getKeyCode()==L_KE.VK_F1)
			{
				JPanel L_pnlTEMP=new JPanel(new BorderLayout());
				StringTokenizer L_stkTEMP=new StringTokenizer("","|");
				if(M_vtrEMAIL==null)//FETCHING EMAIL ID LIST FROM HRMS
				{
					M_vtrEMAIL=new Vector<String>(50,5);
					String L_strTEMP="";
					M_strSQLQRY="SELECT EP_FULNM,EP_ADRPR FROM HR_EPMST ORDER BY EP_FULNM";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					L_strTEMP="";
					if(M_rstRSSET!=null)
					{
						while (M_rstRSSET.next())
						{
							L_strTEMP=M_rstRSSET.getString("EP_ADRPR");
							L_stkTEMP=new StringTokenizer(L_strTEMP,"|");
							while(L_stkTEMP.hasMoreTokens())
								L_strTEMP=L_stkTEMP.nextToken();
							for(int i=0;i<L_strTEMP.length();i++)
								if(L_strTEMP.charAt(i)=='@')
								{
									M_vtrEMAIL.addElement(M_rstRSSET.getString("EP_FULNM").replace('|',' ')+"|"+L_strTEMP);
									break;
								}
						}
					}
				}					
				M_tblEMAIL=crtTBLPNL(L_pnlTEMP,new String[]{"FL","Name","E-mail ID"},M_vtrEMAIL.size(),1,1,10,6,new int[]{20,100,100},new int[]{0});
				L_pnlTEMP.add(new JLabel("Select Recepents and click OK ..\nTo select, check on left; to deselect, uncheck"),BorderLayout.NORTH);
				for(int i=0;i<M_vtrEMAIL.size();i++)
				{
					L_stkTEMP=new StringTokenizer((String)M_vtrEMAIL.elementAt(i),"|");
					M_tblEMAIL.setValueAt(L_stkTEMP.nextToken(),i,1);
					M_tblEMAIL.setValueAt(L_stkTEMP.nextToken(),i,2);
					for(int j=0;j<M_cmbDESTN.getItemCount();j++)
						if(M_tblEMAIL.getValueAt(i,2).equals(M_cmbDESTN.getItemAt(j)))
						   M_tblEMAIL.setValueAt(new Boolean(true),i,0);
				}
				int L_intRPLY=JOptionPane.showConfirmDialog(this,L_pnlTEMP,"Select Recipents",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
				if(L_intRPLY==0)
				{
					Boolean TRUE=new Boolean(true);
					M_cmbDESTN.removeAllItems();
					for(int i=0;i<M_tblEMAIL.getRowCount();i++)
					{
						if(M_tblEMAIL.getValueAt(i,0).equals((Object)TRUE))
							M_cmbDESTN.addItem(M_tblEMAIL.getValueAt(i,2));
					}
				}
			}
		}catch(Exception e)
		{setMSG(e,"cl_rbase.keypressed");}
	}
	
	void exePRINT()
	{
		doPRINT("c:\\try.lwp");
	}
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC==M_txtFMDAT)
			setMSG("Enter Report From date..",'N');
		else if(M_objSOURC==M_txtTODAT)
			setMSG("Enter Report To date..",'N');
		else if(M_objSOURC==M_cmbDESTN)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Print"))
			   setMSG("Select Printer..",'N');
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("E-mail"))
			   setMSG("To edit receipent list, Press 'F1'..",'N');
		}
		else if(M_objSOURC==M_txtDESTN)
			setMSG("Enter File Name..",'N');
	}
	String[] getPRINTERS() 
	{
		String [] L_staTEMP;
		try
		{
			/*flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//			flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_US_ASCII;
			aset = new HashPrintRequestAttributeSet();
			pservices =PrintServiceLookup.lookupPrintServices(flavor, aset);
			for(int i=0;i<pservices.length;i++)
			M_cmbDESTN.addItem(pservices[i].getName());
			if(M_cmbDESTN.getItemCount()==2)
			{
				M_cmbDESTN.setSelectedIndex(1);
				M_cmbDESTN.setEnabled(false);
			}*/
		M_cmbDESTN.removeAllItems();
		M_cmbDESTN.insertItemAt("Select",0);
		if(cl_dat.M_thrPRNLS_pbst !=null)
		{
			//JOptionPane.showMessageDialog(this,"Fetching Printer list .. Please wait " ," ",JOptionPane.INFORMATION_MESSAGE);
			//System.out.println("inside join");						
			cl_dat.M_thrPRNLS_pbst.join();
		}
			//System.out.println("out of join");						
	    L_staTEMP=new String[cl_dat.M_vtrPRNTR_pbst.size()];
		for(int i=0;i<cl_dat.M_vtrPRNTR_pbst.size();i++)
		{
			L_staTEMP[i]=cl_dat.M_vtrPRNTR_pbst.elementAt(i).toString();
			M_cmbDESTN.addItem(cl_dat.M_vtrPRNTR_pbst.elementAt(i).toString());
		}
		M_cmbDESTN.setSelectedIndex(1);
		if(M_cmbDESTN.getItemCount()==2)
		{
			M_cmbDESTN.setSelectedIndex(1);
			M_cmbDESTN.setEnabled(false);
		}
		return L_staTEMP;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getPRINTERS");
			return null;
		}
	}
	
	private class PrintJobWatcher 
	{
      // true if it is safe to close the print job's input stream
      boolean done = false;

      PrintJobWatcher(DocPrintJob job) 
	  {
          // Add a listener to the print job
          job.addPrintJobListener(new PrintJobAdapter() 
			{
				public void printJobCanceled(PrintJobEvent pje) 
				{
				  System.out.println("printJobCanceled");
				    allDone();
				}
				public void printJobCompleted(PrintJobEvent pje) 
				{
				    System.out.println("printJobCompleted");
				    allDone();
				}
		        public void printJobFailed(PrintJobEvent pje) 
				{
				    System.out.println("printJobFailed");
					allDone();
				}
			    public void printJobNoMoreEvents(PrintJobEvent pje) 
				{
			        System.out.println("printJobNoMoreEvents");
			        allDone();
			    }
			    public void printDataTransferCompleted(PrintJobEvent pje) 
				{
			      System.out.println("printDataTransferCompleted");
			      allDone();
			    }
			    public void printJobRequiresAttention(PrintJobEvent pje) 
				{
			      System.out.println("printJobRequiresAttention");
			      allDone();
			    }
			    void allDone() 
				{
			        synchronized (PrintJobWatcher.this) 
					{
			            done = true;
			            PrintJobWatcher.this.notify();
			        }
			    }
			});
		}
		public synchronized void waitForDone() 
		{
			try 
			{
				while (!done) 
				{
					wait();
				}
			} catch (InterruptedException e) 
			{  System.out.println(e);      }
		}
	}  
	protected  void prnFMTCHR(DataOutputStream L_DOUT,String L_FMTSTR){		try{			if(L_FMTSTR.equals(M_strCPI10))
			{
				intCOLCT=78;
				if(M_rdbHTML.isSelected())
				{
					L_DOUT.writeBytes("<FONT FACE=\"Arial\" Size = \"3\">");				}
				else
				{					L_DOUT.writeChar(27);					L_DOUT.writeBytes("P");
				}			}			if(L_FMTSTR.equals(M_strCPI12))
			{
				intCOLCT=90;
				if(M_rdbHTML.isSelected())
				{
					L_DOUT.writeBytes("<FONT FACE=\"Arial\" Size = \"2\">");				}
				else
				{					L_DOUT.writeChar(27);					L_DOUT.writeBytes("M");
				}			}			if(L_FMTSTR.equals(M_strCPI17))			{				intCOLCT=145;
				if(M_rdbHTML.isSelected())
				{
					L_DOUT.writeBytes("<FONT FACE=\"Arial\" Size = \"1\">");				}
				else
					L_DOUT.writeChar(15);
			}			if(L_FMTSTR.equals(M_strNOCPI17))			{
				if(M_rdbHTML.isSelected())
				{
					L_DOUT.writeBytes("<FONT FACE=\"Arial\" Size = \"3\">");				}
				else
					L_DOUT.writeChar(18);
			}			if(L_FMTSTR.equals(M_strBOLD))
			{
				if(M_rdbHTML.isSelected())
				{
					L_DOUT.writeBytes("<B>");				}
				else
				{					L_DOUT.writeChar(27);					L_DOUT.writeBytes("G");
				}			}			if(L_FMTSTR.equals(M_strNOBOLD))
			{
				if(M_rdbHTML.isSelected())
				{
					L_DOUT.writeBytes("</B>");				}
				else
				{					L_DOUT.writeChar(27);					L_DOUT.writeBytes("H");
				}			}			if(L_FMTSTR.equals(M_strENH))
			{
				if(M_rdbHTML.isSelected())
				{
									}
				else
				{					L_DOUT.writeChar(27);					L_DOUT.writeBytes("W1");
				}			}			if(L_FMTSTR.equals(M_strNOENH))
			{
				if(M_rdbHTML.isSelected())
				{
									}
				else
				{					L_DOUT.writeChar(27);					L_DOUT.writeBytes("W0");					L_DOUT.writeChar(27);					L_DOUT.writeBytes("F");
				}			}			if(L_FMTSTR.equals(M_strEJT))			{
				if(M_rdbHTML.isSelected())
				{
									}
				else
					L_DOUT.writeChar(12);
			}		}catch(IOException L_EX){			setMSG(L_EX,"prnFMTCHR");		}	}
	protected void prnHRLIN(DataOutputStream L_DOUT,String P_strLINCH)
	{
		try
		{
			
			if(P_strLINCH.length()>1)
				throw new Exception("Illegal string length");
			if(M_rdbTEXT.isSelected())
			{
				for(byte b=0;b<intCOLCT;b++)
					L_DOUT.writeBytes(P_strLINCH);
			}
			else
			{
				L_DOUT.writeBytes("<hr>");
			}
		}catch(Exception e){setMSG(e,"in prnHRLIN");}
	}
	/*protected  String padSTRING(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)	{		String P_strTRNVL = "";		try		{			boolean flgTEXT=false;
			if(M_rdbTEXT==null)				flgTEXT=true;
			else if(M_rdbTEXT.isSelected())
				flgTEXT=true;			String L_STRSP = " ";			P_strSTRVL = P_strSTRVL.trim();			int L_STRLN = P_strSTRVL.length();			if(P_intPADLN <= L_STRLN)			{				P_strSTRVL = P_strSTRVL.substring(0,P_intPADLN).trim();				L_STRLN = P_strSTRVL.length();				P_strTRNVL = P_strSTRVL;			}			int L_STRDF = P_intPADLN - L_STRLN;			StringBuffer L_STRBUF;
			int L_intTABSZ=0;
			if(intCOLCT==145)
				L_intTABSZ=10;
			else if(intCOLCT==90)
				L_intTABSZ=7;
			else if(intCOLCT==75)
				L_intTABSZ=5	;			switch(P_chrPADTP)			{				case 'C':					L_STRDF = L_STRDF / 2;					L_STRBUF = new StringBuffer(L_STRDF);
					if(flgTEXT)
					{						for(int j = 0;j < L_STRBUF.capacity();j++)							L_STRBUF.insert(j,' ');						P_strTRNVL =  L_STRBUF+P_strSTRVL+L_STRBUF ;
					}
					else
					{						L_STRDF=L_STRDF/L_intTABSZ+1;						for(int j = 0;j < L_STRBUF.capacity();j++)							L_STRBUF.insert(j,"\t");						P_strTRNVL =  L_STRBUF+P_strSTRVL+L_STRBUF ;
					}					break;				case 'R':					if(flgTEXT)
					{
						L_STRBUF = new StringBuffer(L_STRDF);						for(int j = 0;j < L_STRBUF.capacity();j++)							L_STRBUF.insert(j,' ');						P_strTRNVL =  P_strSTRVL+L_STRBUF ;
					}
					else
					{						L_STRDF=L_STRDF/L_intTABSZ+1;						L_STRBUF = new StringBuffer(L_STRDF);						for(int j = 0;j < L_STRBUF.capacity();j++)							L_STRBUF.insert(j,"\t");						P_strTRNVL =  P_strSTRVL+L_STRBUF ;
					}					break;				case 'L':					L_STRBUF = new StringBuffer(L_STRDF);
					if(flgTEXT)
					{						for(int j = 0;j < L_STRBUF.capacity();j++)							L_STRBUF.insert(j,' ');						P_strTRNVL =  L_STRBUF+P_strSTRVL ;
					}
					else
					{						for(int j = 0;j < L_STRBUF.capacity();j++)							L_STRBUF.insert(j,"\t");					}					break;			}		}catch(Exception L_EX){			setMSG(L_EX,"padSTRING");		}		return P_strTRNVL;	}*/
/*	protected  String padSTRING(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)	{		String P_strTRNVL = "";		try		{			boolean flgTEXT=false;
			if(M_rdbTEXT==null)				flgTEXT=true;
			else if(M_rdbTEXT.isSelected())
				flgTEXT=true;			
			String L_STRSP = " ";			P_strSTRVL = P_strSTRVL.trim();			int L_STRLN = P_strSTRVL.length();			if(P_intPADLN <= L_STRLN)			{				P_strSTRVL = P_strSTRVL.substring(0,P_intPADLN).trim();				L_STRLN = P_strSTRVL.length();				P_strTRNVL = P_strSTRVL;			}			int L_STRDF = P_intPADLN - L_STRLN;
			if(!flgTEXT)			{				//	L_STRDF=new Float(L_STRDF*2.6f).intValue();
				System.out.println(L_STRDF/5.0f+" "+new Float(L_STRDF/5.0f).intValue());				if(L_STRDF/5.0f>new Float(L_STRDF/5.0f).intValue())					L_STRDF=new Float(L_STRDF/5.0f+1).intValue();				else
				L_STRDF=new Float(L_STRDF/5.0f).intValue();
							}
						StringBuffer L_STRBUF;			switch(P_chrPADTP)			{				case 'C':					L_STRDF = L_STRDF / 2;					L_STRBUF = new StringBuffer(L_STRDF);					for(int j = 0;j < L_STRBUF.capacity();j++)						L_STRBUF.insert(j,' ');					P_strTRNVL =  L_STRBUF+P_strSTRVL+L_STRBUF ;					break;				case 'R':					L_STRBUF = new StringBuffer(L_STRDF);					for(int j = 0;j < L_STRBUF.capacity();j++)					{
						if(M_rdbTEXT.isSelected())
						L_STRBUF.insert(j,' ');
						else
							L_STRBUF.insert(j,"\t");
					}					P_strTRNVL =  P_strSTRVL+L_STRBUF ;					break;				case 'L':					L_STRBUF = new StringBuffer(L_STRDF);					for(int j = 0;j < L_STRBUF.capacity();j++)						L_STRBUF.insert(j,' ');					P_strTRNVL =  L_STRBUF+P_strSTRVL ;					break;			}		}catch(Exception L_EX){			setMSG(L_EX,"padSTRING");		}		return P_strTRNVL;	}*/
}