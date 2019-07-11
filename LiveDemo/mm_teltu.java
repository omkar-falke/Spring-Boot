import java.awt.event.*;import javax.swing.BorderFactory;import javax.swing.border.TitledBorder;
import javax.swing.JTextField; import javax.swing.JLabel;import javax.swing.JPanel;

class mm_teltu extends cl_pbase
{
	private JTextField txtSSGRP,	   /** Sub sub group Code  		                */
					   txtSSDSC,	   /** Sub sub group Description				*/
					   txtILDTM,       /** Internal Lead time for Indigenious codes */
					   txtELDTM,	   /** Enternal Lead time for Indigenious codes */
					   txtIILTM,	   /** Internal Lead time for Imported codes	*/
					   txtIELTM;	   /** Enternal Lead time for Imported codes	*/
	private JPanel	   pnlLDTM1,	   /** Panel for Imported Lead times	*/
					   pnlLDTM2;	   /** Panel for Indigeneous Lead times	*/
	mm_teltu()
	{
		super(2);
		setMatrix(20,8);
		pnlLDTM1 = new JPanel();
		pnlLDTM1.setLayout(null);
		pnlLDTM2 = new JPanel();
		pnlLDTM2.setLayout(null);
		add(new JLabel("Sub Sub Group"),2,1,1,1,this,'L');
		add(txtSSGRP=new TxtLimit(10),2,2,1,1,this,'L');
		add(txtSSDSC=new TxtLimit(60),2,3,1,6,this,'L');
		setMatrix(20,8);
		add(new JLabel("Internal "),1,3,1,1,pnlLDTM1,'L');
		add(txtIILTM=new TxtNumLimit(3),1,4,1,1,pnlLDTM1,'L');
		add(new JLabel("External "),1,5,1,1,pnlLDTM1,'L');
		add(txtIELTM=new TxtNumLimit(3),1,6,1,1,pnlLDTM1,'L');
		
		pnlLDTM1.setBorder(BorderFactory.createTitledBorder(null,"Lead Time for Imported codes",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman"),java.awt.Color.blue));
		
		add(pnlLDTM1,4,1,3,8,this,'L');
		
		add(new JLabel("Internal "),1,3,1,1,pnlLDTM2,'L');
		add(txtILDTM=new TxtNumLimit(3),1,4,1,1,pnlLDTM2,'L');
		add(new JLabel("External "),1,5,1,1,pnlLDTM2,'L');
		add(txtELDTM=new TxtNumLimit(3),1,6,1,1,pnlLDTM2,'L');
	    pnlLDTM2.setBorder(BorderFactory.createTitledBorder(null,"Lead Time for Other codes",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman"),java.awt.Color.blue));
		
		add(pnlLDTM2,8,1,3,8,this,'L');
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == txtSSGRP)
		{
			try
			{
				String L_strSSGRP = txtSSGRP.getText().trim();
				clrCOMP();
				setENBL(false);
				txtSSGRP.setEnabled(true);
				txtSSGRP.setText(L_strSSGRP);
				M_strSQLQRY = "SELECT CT_MATDS,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM from CO_CTMST "+
							  " WHERE CT_CODTP = 'SS' and CT_MATCD ='"+txtSSGRP.getText().trim()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						setENBL(true);
						txtSSDSC.setText(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));
						txtIILTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_IILTM"),"0"));
						txtIELTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_IELTM"),"0"));
						txtILDTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),"0"));
						txtELDTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),"0"));
						txtIILTM.requestFocus();
						setMSG("Enter Internal lead time for imported code..",'N');
					}
					else
					{
						setENBL(false);
						setMSG("Invalid Sub sub group..",'E');
					}
					M_rstRSSET.close();
				}
				
				
			}
			catch(Exception L_E)
			{
				//setMSG(L_E,'E');
				setMSG("Error in getting data ..",'E');
				setENBL(false);
				txtSSGRP.setEnabled(true);
			}
		}
		else if(M_objSOURC == txtIILTM)
		{
			txtIELTM.requestFocus();
			setMSG("Enter External lead time for imported code..",'N');
		}
		else if(M_objSOURC == txtIELTM)
		{
			txtILDTM.requestFocus();
			setMSG("Enter Internal lead time for indigenious codes..",'N');
		}
		else if(M_objSOURC == txtILDTM)
		{
			txtELDTM.requestFocus();
			setMSG("Enter External lead time for indigenious codes..",'N');
		}
		else if(M_objSOURC == txtELDTM)
		{
			cl_dat.M_btnSAVE_pbst.requestFocus();	
		}

	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtSSGRP)
			{
				int []L_inaCOLSZ = new int[] {100,400};
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtSSGRP";
				String L_ARRHDR[] = {"Item Code","Description"};
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
				M_strSQLQRY += " where CT_CODTP ='SS' and isnull(CT_STSFL,'') <>'X'";
				if(txtSSGRP.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '" + txtSSGRP.getText().trim() + "%'";
				M_strSQLQRY += " Order by ct_matcd ";
				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtSSGRP"))
		{
			java.util.StringTokenizer L_STRTKN =new java.util.StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			L_STRTKN.nextToken();
			txtSSGRP.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
			txtSSDSC.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
	}
	boolean vldDATA()
	{
		if(txtSSGRP.getText().trim().length() ==0)
		{
			setMSG("Enter Sub sub group code..",'E');
			return false;
		}
		else if(txtILDTM.getText().trim().length() ==0)
		{
			setMSG("Enter Internal Lead time for Indigenious codes..",'E');
			return false;
		}
		else if(txtELDTM.getText().trim().length() ==0)
		{
			setMSG("Enter External Lead time for Indigenious codes..",'E');
			return false;
		}
		else if(txtIILTM.getText().trim().length() ==0)
		{
			setMSG("Enter Internal Lead time for Imported codes..",'E');
			return false;
		}
		else if(txtIELTM.getText().trim().length() ==0)
		{
			setMSG("Enter External Lead time for Imported codes..",'E');
			return false;
		}
		return true;	
	}
	void exeSAVE()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY = "Update CO_CTMST SET CT_ILDTM = "+txtILDTM.getText().trim()+
							  ",CT_ELDTM = "+txtELDTM.getText().trim() +
							  ",CT_IILTM = "+txtIILTM.getText().trim() +
							  ",CT_IELTM = "+txtIELTM.getText().trim() +
							  ","+getUSGDTL("CT",'U',null)+
							  " WHERE CT_MATCD = '"+txtSSGRP.getText().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				M_strSQLQRY = "Update CO_CTMST SET CT_IILTM = "+txtIILTM.getText().trim()+
							  ",CT_IELTM = "+txtIELTM.getText().trim() +
							  ","+getUSGDTL("CT",'U',null)+
							  " WHERE CT_MATCD like '"+txtSSGRP.getText().trim().substring(0,6)+"%'"+
							  " AND SUBSTRING(CT_MATCD,10,1) in ('2','6')";
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				M_strSQLQRY = "Update CO_CTMST SET CT_ILDTM = "+txtILDTM.getText().trim()+
							  ",CT_ELDTM = "+txtELDTM.getText().trim() +
							  ","+getUSGDTL("CT",'U',null)+
							  " WHERE CT_MATCD like '"+txtSSGRP.getText().trim().substring(0,6)+"%'"+
							  " AND SUBSTRING(CT_MATCD,10,1) not in ('2','6')";
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						setMSG("Data Updated Successfully..",'N');
						clrCOMP();
					}
					else
						setMSG("Error in Updating Data ..",'E');
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG("Error in updating ,,",'E');
			
		}
		finally
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
}