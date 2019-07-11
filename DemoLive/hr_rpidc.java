import java.awt.event.*;
import java.util.Date;import java.util.StringTokenizer;
import java.sql.SQLException;import java.sql.ResultSet;
import javax.swing.JTextField;import javax.swing.InputVerifier;import javax.swing.JComponent;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import javax.swing.InputVerifier;
class hr_rpidc extends cl_rbase 
{
	
	private FileOutputStream F_OUT ;   
    private DataOutputStream O_DOUT ;
    private JComboBox cmbRPTOP;
    private JTextField txtEMPFR,txtEMPTO;
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpidc.html";
	private INPVR objINPVF = new INPVR(); 
	int intLINNO=0;
	int intPAGNO=0;
	int intRECCT=0;
			
	hr_rpidc()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			setMatrix(20,8);
			add(new JLabel("Report Option "),3,3,1,1,this,'L');
			add(cmbRPTOP = new JComboBox(), 3,4,1,2,this,'L');
			cmbRPTOP.addItem("All ICARDS");
			cmbRPTOP.addItem("Specific Range");
			add(new JLabel("Emp.ID Range "),4,3,1,1,this,'L');
			add(txtEMPFR = new TxtLimit(4),4,4,1,1,this,'L');
			add(txtEMPTO = new TxtLimit(4),4,5,1,1,this,'L');
			setENBL(false);
			M_pnlRPFMT.setVisible(true);
			txtEMPFR.setInputVerifier(objINPVF);
			txtEMPTO.setInputVerifier(objINPVF);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	void setENBL(boolean L_flgSTAT)
	{
        super.setENBL(L_flgSTAT);
        /*if(cmbRPTOP.getItemCount() >0)
        if(cmbRPTOP.getSelectedIndex() ==0)
        {
            txtEMPFR.setVisible(false);
            txtEMPFR.setVisible(false);
        }
        else
        {
            txtEMPFR.setVisible(true);
            txtEMPFR.setVisible(true);
        }*/
        
   	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
				setENBL(true);
			else
				setENBL(false);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				M_cmbDESTN.requestFocus();
		}
		/*else if(M_objSOURC == cmbRPTOP)
		{
	        setENBL(true);	
	        clrCOMP();	    
		}*/
		setMSG("",'N');
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
		    if(M_objSOURC == cmbRPTOP)
		        cmbRPTOP.transferFocus();
		    else if(M_objSOURC == txtEMPFR)
		        txtEMPTO.requestFocus();
            else if(M_objSOURC == txtEMPTO)
		        cl_dat.M_btnSAVE_pbst.requestFocus();
		        
		}
		else if(L_KE.getKeyCode()==L_KE.VK_F1)
		{
			if(M_objSOURC == txtEMPFR)
			{
    			M_strHLPFLD = "txtEMPFR";
    			M_strSQLQRY = "SELECT EP_EMPNO,EP_SHRNM,EP_FULNM FROM HR_EPMST "
    					 +"WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EP_STSFL,'') <>'X' and EP_LFTDT is null ";
    			if(txtEMPFR.getText().length() >0)
    					 M_strSQLQRY += " AND EP_EMPNO like '"+txtEMPFR.getText().trim() +"%'";
    			if(txtEMPTO.getText().length() >0)		 
                    M_strSQLQRY += " AND EP_EMPNO < '"+txtEMPTO.getText().trim() +"'";    			
                M_strSQLQRY += "Order by EP_EMPNO";    
                System.out.println("To "+M_strSQLQRY);
    			cl_hlp(M_strSQLQRY,2,1,new String[]{"Emp. Number","Initial","Full Name"},3,"CT",new int[]{60,60,400});		
			}
			else if(M_objSOURC == txtEMPTO)
			{
    			M_strHLPFLD = "txtEMPTO";
    			M_strSQLQRY = "SELECT EP_EMPNO,EP_SHRNM,EP_FULNM FROM HR_EPMST "
    					 +"WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EP_STSFL,'') <>'X' and EP_LFTDT is null ";
    			if(txtEMPTO.getText().length() >0)
    				 M_strSQLQRY += " AND EP_EMPNO like '"+txtEMPTO.getText().trim() +"%'";
    			if(txtEMPFR.getText().length() >0)		 
                    M_strSQLQRY += " AND EP_EMPNO > '"+txtEMPFR.getText().trim() +"'";    			
                M_strSQLQRY += "Order by EP_EMPNO";
                System.out.println("FRom "+M_strSQLQRY);
    			cl_hlp(M_strSQLQRY,2,1,new String[]{"Emp. Number","Initial","Full Name"},3,"CT",new int[]{60,60,400});		
			}
		}
	}
	
	void exeHLPOK()
	{
		cl_dat.M_flgHELPFL_pbst = false;
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtEMPFR"))
		{
			txtEMPFR.setText(cl_dat.M_strHLPSTR_pbst);
		}
		if(M_strHLPFLD.equals("txtEMPTO"))
		{
			txtEMPTO.setText(cl_dat.M_strHLPSTR_pbst);
		}
	}
	boolean vldDATA()
	{
		try
		{
		    if(cmbRPTOP.getSelectedIndex() ==1)
		    {
		        if(txtEMPFR.getText().trim().length() ==0)
		        {
		            setMSG("Enter The From and To Employee Number..",'E'); 
		            return false;
		        }
		        else if(txtEMPTO.getText().trim().length() ==0)
		        {
    		         setMSG("Enter The From and To Employee Number..",'E'); 
    		         return false;
		        }
		        
		    }
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if(M_cmbDESTN.getSelectedIndex() ==0)
				{
					setMSG("Please select Printer ..",'E');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if(M_cmbDESTN.getItemCount() ==0)
				{
					setMSG("Please select E-mail Id by using the F1 list ..",'E');
					return false;
				}
			}
			
			return true;
		}
		catch(Exception L_VLD)
		{
			return false;
		}
	}
	void exePRINT()
	{
		try
		{
			if(vldDATA())
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				F_OUT = new FileOutputStream(strRPFNM);
				O_DOUT = new DataOutputStream(F_OUT);
				intLINNO = 0;	
				intPAGNO = 1;
				O_DOUT.writeBytes("<HTML><HEAD><Title>Identity Card </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \"></pre>");
				O_DOUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				O_DOUT.writeBytes("</STYLE>"); 
				genICARD();
				O_DOUT.writeBytes("</fontsize></BODY></HTML>");
				Runtime r = Runtime.getRuntime();
				Process p = null;					
				p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
				setMSG("For Printing Select File Menu, then Print  ..",'N');
				if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
				{
			    	cl_eml ocl_eml = new cl_eml();				    
				    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				    {
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"List Of Issues"," ");
					    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
					}				    	    	
			    }
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	
	private void genICARD()
	{
	  	try
		{
            M_strSQLQRY = "SELECT EP_FULNM,EP_EMPNO,EP_BLDGP,EP_SEXFL,DLURLPATH(IM_FILRF) IM_FILRF  FROM HR_EPMST,CO_IMMST WHERE IM_SYSCD ='HR' AND IM_DOCNO = EP_EMPNO AND IM_CMPCD=EP_CMPCD AND EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EP_STSFL,'') <> 'X' AND EP_LFTDT IS NULL";
            if(cmbRPTOP.getSelectedIndex() !=0)
            {
                M_strSQLQRY +=" AND EP_EMPNO between '"+txtEMPFR.getText().trim() +"'";
                 M_strSQLQRY +=" AND '"+txtEMPTO.getText().trim() +"'";
            }
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
            String L_strUSRNM =" ";
		    String L_strEMPNO =" ";
		    String L_strBLDGP =" ";
		    String L_strTEMP =" ";
		    String L_strFILRF =" ";
		    String L_strGENDER =" ";
		    java.util.StringTokenizer L_stnTOKEN; 
		    int L_intCOUNT =0;       
            int L_intRECCT =0;
            if(M_rstRSSET !=null)
            while(M_rstRSSET.next())
            {
		  		L_intRECCT++;
		  		O_DOUT.writeBytes("<TABLE border=0 cellPadding=3 cellSpacing=0  width=\"753\" height=\"1\">");
			   	O_DOUT.writeBytes("<TBODY>");
				
        		  L_strUSRNM = M_rstRSSET.getString("EP_FULNM");
        		  L_strFILRF = M_rstRSSET.getString("IM_FILRF");
        		  L_stnTOKEN = new StringTokenizer(L_strUSRNM,"|");
        		  while(L_stnTOKEN.hasMoreTokens())
        		  {
        		    L_strTEMP = L_stnTOKEN.nextToken();
        		    L_intCOUNT++;
        		    if(L_intCOUNT ==1)
        		        L_strUSRNM =  L_strTEMP.substring(0,1) +". ";
        		    else if(L_intCOUNT ==2)
        		        L_strUSRNM +=  L_strTEMP.substring(0,1) +". ";
        		    else
        		    {
        		        L_strUSRNM +=  L_strTEMP;
        		        L_intCOUNT =0;
        		     }
        		  }
        		 
        		  L_strEMPNO = M_rstRSSET.getString("EP_EMPNO");
        		  L_strGENDER = M_rstRSSET.getString("EP_SEXFL");
        		  if(L_strGENDER.equals("1"))
        		    L_strGENDER ="Mr. ";
        		  else if(L_strGENDER.equals("2"))
        		    L_strGENDER ="Ms. "; 
        		    L_strUSRNM = L_strGENDER + L_strUSRNM; 
        		  L_strBLDGP = nvlSTRVL(M_rstRSSET.getString("EP_BLDGP"),"-");
        		  O_DOUT.writeBytes("<TR><TD width=\"109\" height=\"1\">");
                  O_DOUT.writeBytes("<p align=\"center\"><img border=\"0\" src=\"file:///G:/exec/image/spllogo_old.gif\" width=\"70\" height=\"72\"></p>");
                  O_DOUT.writeBytes("</TD>");
                  O_DOUT.writeBytes("<TD style=\"LINE-HEIGHT: 1\" width=\"400\" height=\"1\">");
                  O_DOUT.writeBytes("<P align=left style=\"LINE-HEIGHT: 0.1\"><font face=\"Arial\" size=\"1\">&nbsp;IDENTITY CARD</font></P>") ;      
                  O_DOUT.writeBytes("<P align=left style=\"LINE-HEIGHT: 0.1\"><STRONG><font color=\"black\" face=\"Arial\" size=\"2\">SUPREME PETROCHEM LTD</font></STRONG>  </P>");       
                  O_DOUT.writeBytes("<P align=left style=\"LINE-HEIGHT: 0.1\"><font face=\"Arial\" size=\"1\">Village Amdoshi, Wakan Roha Road.</font></P>");       
                  O_DOUT.writeBytes("<P align=left style=\"LINE-HEIGHT: 0.1\"><font face=\"Arial\" size=\"1\">&nbsp;Taluka -Roha Dist- Raigad (Maharashtra) Pin Code-402106</font>&nbsp;</P>");       
                  O_DOUT.writeBytes("<P align=left style=\"LINE-HEIGHT: 0.1\"><font face=\"Arial\" size=\"1\">&nbsp;Phone :02194 - 222540 to 43 </font></P>");
                  O_DOUT.writeBytes("</TD>");
                  O_DOUT.writeBytes("<TD style=\"LINE-HEIGHT: 0.5\" width=\"351\" height=\"0.5\" nowrap>"); 
                  O_DOUT.writeBytes("<p align=\"center\"><b><u>RULES</u></b></p>");
                  O_DOUT.writeBytes("<ul>");
                  O_DOUT.writeBytes("<li>");
                  O_DOUT.writeBytes("<p align=\"left\"><font size=\"2\">For Security and Identification purpose this card &nbsp;</font>");
                  O_DOUT.writeBytes("</li>");
                  O_DOUT.writeBytes("</ul>");
                  O_DOUT.writeBytes("<p align=\"left\"><font size=\"2\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                  O_DOUT.writeBytes("must be carried at all times by the holder.</font></p>");
                  O_DOUT.writeBytes("<ul>");
                  O_DOUT.writeBytes("<li>");
                  O_DOUT.writeBytes("<p align=\"left\"><font size=\"2\">This card must be presented to the security or &nbsp;</font>");
                  O_DOUT.writeBytes("</li>");
                  O_DOUT.writeBytes("</ul>");
                  O_DOUT.writeBytes("<p align=\"left\"><font size=\"2\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                  O_DOUT.writeBytes(" other authorities on demand.</font>");     
                  O_DOUT.writeBytes("</TD></TR>");
                  O_DOUT.writeBytes("<TR><TD width=\"109\" height=\"101\"><img border=\"0\" src=\"file:///"+L_strFILRF+"\" width=\"89\" height=\"77\">");
                  O_DOUT.writeBytes("<p><font size=\"1\">Authorised Signatory</font></p>");
                  O_DOUT.writeBytes("</TD>");
                  O_DOUT.writeBytes("<TD style=\"LINE-HEIGHT: 1\" width=\"400\" height=\"101\">Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   : ");
                  O_DOUT.writeBytes(L_strUSRNM);
                  O_DOUT.writeBytes("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"); 
                  O_DOUT.writeBytes("<p>ID No&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; :  "+L_strEMPNO+"</p>");
                 O_DOUT.writeBytes("<p>Blood Group&nbsp; :&nbsp; "+L_strBLDGP+"&nbsp;</p>");
                 O_DOUT.writeBytes("</TD>");
                 O_DOUT.writeBytes("<TD style=\"LINE-HEIGHT: 0.5\" width=\"351\" height=\"0.5\">");
                 O_DOUT.writeBytes("<ul>");
                 O_DOUT.writeBytes("<li>");
                 O_DOUT.writeBytes("<p align=\"left\"><font size=\"2\">New Card can only be obtained on payment  </font></li>");
                 O_DOUT.writeBytes("</ul>");
                 O_DOUT.writeBytes("<p align=\"left\"><font size=\"2\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                 O_DOUT.writeBytes("of Rs. 100/-  in case of loss.</font></p>");
                 O_DOUT.writeBytes("<p align=\"left\"><font size=\"2\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                 O_DOUT.writeBytes("If found please return to the address on the reverse. </font>");
                 O_DOUT.writeBytes("</TD></TR>");
                 O_DOUT.writeBytes("</TBODY></TABLE><HR>");
                 if(L_intRECCT == 4)
                 {
                    O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
                 }
            }
	    }
		catch(Exception L_E)
		{
			setMSG(L_E,"genICARD");
		}
	}
	class INPVR extends InputVerifier
	{
	    public boolean verify(JComponent input)
	    {
	        try
	        {
	            if(((JTextField)input).getText().length() ==0)
	                return true;
	            if((input == txtEMPFR)||(input == txtEMPFR))
	            {
	                if(((JTextField)input).getText().length() !=4)
	                {
	                    setMSG("Invalid Employee ID..",'E');    
	                }
	                else if((txtEMPFR.getText().length() >0)&&(txtEMPTO.getText().length() >0))
	                {
	                   if((Integer.parseInt(txtEMPFR.getText().trim())) < (Integer.parseInt(txtEMPTO.getText().trim())))
	                   {
	                        setMSG(" From Employee number can not be greater than To Emp. No..",'E');
	                        return false; 
	                   }
	                    
	                }
	                else
	                {
	                    M_strSQLQRY = "Select EP_EMPNO from HR_EPMST WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO ='"+((JTextField)input).getText().trim() +"'"+
	                                    " and isnull(EP_STSFL,'') <>'X' and EP_LFTDT is null";
	                    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	                    System.out.println(M_strSQLQRY +M_rstRSSET);
	                    if(M_rstRSSET !=null)                
	                    {
	                        if(M_rstRSSET.next())
	                        {
	                            return true;       
	                        }
	                        else
	                        {
	                             setMSG("Invalid Employee Number..",'E');
	                            return false;
	                        }
	                    }
	                }
	            }
	        }
	        catch(Exception L_E)
	        {
	            setMSG(L_E,"veriify input");
	            return false;    
	        }
	        return true;    
	    }    
	}
}



