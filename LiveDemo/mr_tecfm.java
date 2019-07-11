/*
 * Created on Jun 7, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author SSR
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.awt.*;
import java.sql.*;
import java.sql.Date;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import java.io.*; 
import java.lang.*;

class mr_tecfm extends cl_pbase 
{
   
    private TxtLimit txtCFMNO,txtPRTNM,txtPRTCD;
    private JComboBox cmbMKTTP;
    private TxtDate txtVLDFR;
    private TxtDate txtVLDTO;
    private TxtNumLimit txtCFMVL;
    private TxtNumLimit txtACPVL;
    private TxtDate txtCFMDT;
    private String strPRTNM;
    
    mr_tecfm()
	{
	    super(2);
		try
		{
		    setMatrix(20,12);
		    add(new JLabel("Market Type"),4,2,1,1.5,this,'L');
			
			add(cmbMKTTP=new JComboBox(),4,4,1,2.3,this,'L');
					
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP = 'COXXMKT'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
				String	L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
				 	L_strCODCD =L_strCODCD+" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					cmbMKTTP.addItem(L_strCODCD);
				}
			}
		 	if(M_rstRSSET != null)
				M_rstRSSET.close();
		    
		    
		    add(new JLabel("C-Form No"),6,2,1,1.5,this,'L');
		    add(txtCFMNO=new TxtLimit(25),6,4,1,2,this,'L');
		    
		    add(new JLabel ("Date"),6,7,1,1.5,this,'L');
		    add(txtCFMDT=new TxtDate(),6,9,1,2,this,'L');
		    
		    
		    
		    add(new JLabel ("Buyer Code"),8,2,1,1.5,this,'L');
		    add(txtPRTCD=new TxtLimit(5),8,4,1,2,this,'L');
		    
		    add(new JLabel("Buyer Name"),8,7,1,1.5,this,'L');
		    add(txtPRTNM=new TxtLimit(40),8,9,1,4,this,'L');
		    
		    
		    
		    add(new JLabel("Valid From Date") ,10,2,1,1.5,this,'L');
		    add(txtVLDFR=new TxtDate(),10,4,1,2,this,'L');
		    
		    add(new JLabel("Valid To Date") ,10,7,1,1.5,this,'L');
		    add(txtVLDTO=new TxtDate(),10,9,1,2,this,'L');
		    
		    
		    add(new JLabel("C-Form Value"), 12,2,1,1.5,this,'L');
		    add(txtCFMVL=new TxtNumLimit(10.2),12,4,1,2,this,'L');
		     add(new JLabel("Accepted Value"),12,7,1,1.5,this,'L');
		    add(txtACPVL =new TxtNumLimit(10.2),12,9,1,2,this,'L');
		    
		    
		    setENBL(false);
		    
		    
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is constructor");
		}
	}
    
    void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		
		txtCFMNO.setEnabled(false);
		txtCFMDT.setEnabled(false);
		txtCFMVL.setEnabled(false);
		txtACPVL.setEnabled(false);
		txtPRTCD.setEnabled(false);
		txtPRTNM.setEnabled(false);
		txtPRTCD.setEnabled(false);
		cmbMKTTP.setEnabled(false);
		txtVLDFR.setEnabled(false);
		txtVLDTO.setEnabled(false);
		
	}
 
	public void actionPerformed(ActionEvent L_AE)
	{
	     super.actionPerformed(L_AE);
		 try
		 {
		     if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst )) )
			 {
		         cmbMKTTP.setEnabled(true);
		         cmbMKTTP.requestFocus() ;
		        
		         if(M_objSOURC==cmbMKTTP)
		         {
		               txtCFMNO.setEnabled(true);
		               txtCFMNO.requestFocus() ;
		         }
		         if(M_objSOURC==txtCFMNO)
		         {
		             if(txtCFMNO.getText() .length()  >0)
		             {   
		                 getDATA();
		             }    
		             else
		                 setMSG("Enter the C-Form No",'E');
		         }
		         if(M_objSOURC==txtCFMDT)
		         {
		             if(txtCFMDT.getText() .length()  >0)
		             {   
		                 txtPRTCD.setEnabled(true); 
		                 txtPRTCD.requestFocus() ;
		                 
		             }    
		             else
		             {  setMSG("Enter The Date ",'E');txtCFMDT.requestFocus() ;}
		             
		         }
		         if(M_objSOURC==txtPRTCD)
		         {
		             if(txtPRTCD.getText() .length()  >0)
		             {   
		                 if(vldPRTCD())
		                 {    
		                  txtVLDFR.setEnabled(true);
		                  txtVLDFR.requestFocus() ;
		                 } 
		                 else
		                 {
		                     setMSG("Enter the Valid Product Code or F1 ",'E');
			                 txtPRTCD.requestFocus() ;
		                 }
		                 
		             }    
		             else
		             { 
		                 setMSG("Enter the Valid Product Code or F1 ",'E');
		                 txtPRTCD.requestFocus() ;
		             }   
		             
		         }
		         if(M_objSOURC==txtVLDFR)
		         {
		             if(txtVLDFR.getText() .length()  >0)
		             {   
		                 txtVLDTO.setEnabled( true); 
		                 txtVLDTO.requestFocus() ;
		             }    
		             else
		             { 
		                 setMSG("Enter The Valid From Date ",'E');
		                 txtVLDFR.requestFocus() ;
		             }   
		             
		         }
		  
		         if(M_objSOURC==txtVLDTO)
		         {
		             if(txtVLDTO.getText() .length()  >0)
		             {   
		                txtCFMVL.setEnabled( true);
		                 txtCFMVL.requestFocus() ;
		             }   
		             else
		             { 
		                 setMSG("Enter the Valid To Date ",'E');
		                 txtVLDTO.requestFocus() ;
		             }   
		             
		         }
		         if(M_objSOURC==txtCFMVL)
		         {
		             if(txtCFMVL.getText() .length()  >0)
		             {   
		                 txtACPVL.setEnabled(false); 
		                 
		               //  txtACPVL.requestFocus() ;
		                 cl_dat.M_btnSAVE_pbst .requestFocus() ;
		             }    
		             else
		             { 
		                 setMSG("Enter C-Form Value  ",'E');
		                 txtCFMVL.requestFocus() ;
		             }   
		             
		         }
		         if(M_objSOURC==txtACPVL)
		         {
		             if(txtACPVL.getText() .length()  >0)
		                  cl_dat.M_btnSAVE_pbst .requestFocus() ;
		             else
		             { 
		                 setMSG("Enter Accepted Value ",'E');
		                 txtVLDTO.requestFocus() ;
		             }   
		         }
			 }
		     if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				        || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst )))
			    {
		             
		             
		             cmbMKTTP.setEnabled(true);
			         cmbMKTTP.requestFocus() ;
			         if(M_objSOURC==cmbMKTTP)
			         {
			               txtCFMNO.setEnabled(true);
			               txtCFMNO.requestFocus() ;
			              
			         }
			         if(M_objSOURC==txtCFMNO)
			         {
			             getDATA();
			         }
		             
		             
		             
				}
		         
		 }catch(Exception L_EX)
		 {
		     setMSG(L_EX,"This is action Performed");
		 }
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			if(L_KE.getKeyCode() == L_KE.VK_ENTER )
			{
			}
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{
			    if(M_objSOURC== txtPRTCD)
			    {
			        cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPRTCD";
					String L_ARRHDR[] = {"Party Code","Party Name"};
			        
			        M_strSQLQRY ="select PT_PRTCD,PT_PRTNM FROM CO_PTMST WHERE PT_PRTTP='C' order by PT_PRTNM";
			       	cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			    }
			    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			            || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst ))	           
			            || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst )))
				 {
			        if(M_objSOURC== txtCFMNO)
				    {
			            cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtCFMNO";
						String L_ARRHDR[] = {"C-Form No"};
						M_strSQLQRY ="select CF_CFMNO FROM MR_CFMST WHERE CF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CF_PRTTP='C' and CF_MKTTP= '" +cmbMKTTP.getSelectedItem().toString().substring(0,2)+"'";
				      	cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,1,"CT");
							            
				    }
		
				 }
			}
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is key Pressed");
		}
	}	
		    
	void exeHLPOK()
	{
	    super.exeHLPOK();
	    if(M_strHLPFLD.equals("txtPRTCD"))
		{
			txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
			strPRTNM =String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim() ;
	        
	        txtPRTNM.setText(strPRTNM);
		}
	    if(M_strHLPFLD.equals("txtCFMNO"))
		{
	        txtCFMNO.setText(cl_dat.M_strHLPSTR_pbst);
		}
		
	    
	}
	

	public void exeSAVE()
	{
	    try
	    {
	        if(vldCFORM())
	        {    
		        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
				{
				    cl_dat.M_flgLCUPD_pbst = true;
				    this.setCursor(cl_dat.M_curWTSTS_pbst);
				    
				    M_strSQLQRY ="INSERT INTO MR_CFMST(CF_CMPCD,CF_MKTTP,CF_CFMNO, CF_CFMDT,CF_PRTCD,CF_VLDFR,CF_VLDTO,CF_PRTTP,CF_CFMVL,CF_LUSBY,CF_LUPDT,CF_SBSCD,CF_SBSCD1)";
					M_strSQLQRY +="Values(";
					M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY += "'" +cmbMKTTP.getSelectedItem().toString().substring(0,2)+"',";
					M_strSQLQRY += "'" +txtCFMNO.getText() .toString() .trim() +"',";
					M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCFMDT.getText() .toString() .trim() ))+"',";
					M_strSQLQRY += "'" +txtPRTCD.getText() .toString() .trim() +"',";
					M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(txtVLDFR.getText() .toString() .trim() ))+"',";
					M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(txtVLDTO.getText() .toString() .trim())) +"',";
					M_strSQLQRY +=  " 'C',";
					M_strSQLQRY += "'" +txtCFMVL.getText() .toString() .trim() +"',";
					M_strSQLQRY += "'" + cl_dat.M_strUSRCD_pbst +"',";
					M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"',";
					M_strSQLQRY += "'"+M_strSBSCD+"',";
					M_strSQLQRY += "'"+M_strSBSCD.substring(0,2)+"XX00"+"')";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
				}
		        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst )) 
				{
				    cl_dat.M_flgLCUPD_pbst = true;
				    this.setCursor(cl_dat.M_curWTSTS_pbst);
				    if(txtACPVL.getText() .toString().length() >0)
				    {   
				        M_strSQLQRY ="UPDATE MR_CFMST SET ";
				        M_strSQLQRY +="CF_MKTTP=" +"'" +cmbMKTTP.getSelectedItem().toString().substring(0,2)+"',";
				        
				        M_strSQLQRY +="CF_CFMDT =" + "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCFMDT.getText() .toString() .trim() ))+ "',";
				        
				        M_strSQLQRY +="CF_VLDFR =" + "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtVLDFR.getText() .toString() .trim())) + "',";
				        M_strSQLQRY +="CF_VLDTO =" + "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtVLDTO.getText() .toString() .trim() ))+ "',";
				    	
				    	M_strSQLQRY +="CF_CFMVL =" + "'"+txtCFMVL.getText() .toString() .trim() + "',";
				    	M_strSQLQRY += "CF_LUSBY =" +"'" + cl_dat.M_strUSRCD_pbst +"',";
						M_strSQLQRY += "CF_LUPDT=" +"'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"'";
						M_strSQLQRY += " WHERE CF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  CF_CFMNO =" + "'"+txtCFMNO.getText() .toString() .trim() + "'";
						M_strSQLQRY += " and CF_PRTTP=  'C' ";
						M_strSQLQRY += " and CF_PRTCD= "   + "'"+txtPRTCD.getText() .toString() .trim() + "'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						
				    }
				    else
				        setMSG("Modification Not Allowed",'E');
						
			    	    				    
				}   
		        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst  )) 
				{
				    cl_dat.M_flgLCUPD_pbst = true;
				    this.setCursor(cl_dat.M_curWTSTS_pbst);
				    M_strSQLQRY ="UPDATE MR_CFMST SET ";
				    M_strSQLQRY += "CF_STSFL ='X',";
				    M_strSQLQRY += "CF_LUSBY =" +"'" + cl_dat.M_strUSRCD_pbst +"',";
				    M_strSQLQRY += "CF_LUPDT=" +"'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"'";
				 		M_strSQLQRY += " WHERE CF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CF_CFMNO =" + "'"+txtCFMNO.getText() .toString() .trim() + "'";
						M_strSQLQRY += " and CF_PRTTP=  'C' ";
						M_strSQLQRY += " and CF_PRTCD= "   + "'"+txtPRTCD.getText() .toString() .trim() + "'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}    
			        if(cl_dat.exeDBCMT("exeSAVE"))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{   
					   
					    clrCOMP();
					    setMSG(" Data Saved Successfully..",'N');
					}	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{  
					    clrCOMP();
						setMSG(" Data Modified Successfully..",'N');
						    
					}	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst ))
					{    
					    clrCOMP();
					    setMSG("Data Deletion Sucessfully...",'N');
					} 
				}
				else	
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						setMSG("Error in saving details..",'E'); 
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					   setMSG("Error in Modified Data details..",'E'); 
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst ))
						 setMSG("Error in Deletion ...",'N');  
				}	
		   
	        }        
	        
	    }catch(Exception L_EX)
	    {
	        setMSG(L_EX,"This is Save");
	    }
	} 
	private boolean vldPRTCD()
	{
	    try{
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST  WHERE PT_PRTTP='C' and PT_PRTCD='"+txtPRTCD.getText().toString().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				txtPRTNM.setText(M_rstRSSET.getString("PT_PRTNM"));
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldPRDCD ....."+L_EX);							   
		}
	    return false;
	}
	
	
	private void getDATA()
	{
	   try
	   {
	       String L_strTEMP = "";
	         Date  L_tmpFMDT, L_tmpVLDFR, L_tmpVLDTO;
	        M_strSQLQRY = "Select * from co_ptmst,MR_CFMST  ";  
		    M_strSQLQRY += " WHERE CF_PRTCD=PT_PRTCD ";
			M_strSQLQRY += "  AND CF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CF_MKTTP='"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"'";
			M_strSQLQRY += " AND CF_PRTTP='C' and CF_CFMNO= '"+txtCFMNO.getText() .toString().trim() +"'";
			M_strSQLQRY+= " order by CF_CFMNO ";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next() )
			{
	   		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			    {
			 		setMSG("C-Form no already present..",'E');
					return ;
			    }
			    else if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
			    {
	    				if(M_rstRSSET.getFloat("cf_acpvl") >0)
			    		{
				 		setMSG("C-Form Value has been considered.. cannot modify or delete..",'E');
						return ;
				      }		
			    }	
	                 txtCFMDT.setEnabled(true);
	                 txtCFMDT.requestFocus() ;
	                 txtCFMDT.setText(cl_dat.M_strLOGDT_pbst );
			    txtPRTCD.setText(nvlSTRVL(M_rstRSSET.getString("cf_prtcd"),""));
			    txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("pt_prtnm"),""));
            	    L_tmpFMDT = M_rstRSSET.getDate("cf_cfmdt");
			    L_tmpVLDFR = M_rstRSSET.getDate("cf_vldfr");
			    L_tmpVLDTO = M_rstRSSET.getDate("cf_vldto");
			    if (L_tmpFMDT != null)
			    {    
					L_strTEMP = M_fmtLCDAT.format(L_tmpFMDT);
					txtCFMDT.setText(L_strTEMP);
			    }		
			    if (L_tmpVLDFR != null)
			    {    
					L_strTEMP = M_fmtLCDAT.format(L_tmpVLDFR);
					txtVLDFR.setText(L_strTEMP);
			    }		
			    if (L_tmpVLDTO != null)
			    {   
					L_strTEMP = M_fmtLCDAT.format(L_tmpVLDTO);
					txtVLDTO.setText(L_strTEMP);
			    }		
				txtCFMVL.setText(nvlSTRVL(M_rstRSSET.getString("cf_cfmvl"),""));
	     		      txtACPVL.setText(nvlSTRVL(M_rstRSSET.getString("cf_acpvl"),"0"));
			}
			
			
			
	   }catch(Exception L_EX)
	   {
	       setMSG(L_EX,"This is getDATA");
	       
	   }
	    
	   
	} 
	   
	 private boolean vldCFORM()
		{
			try
			{
			    if(txtCFMDT.getText().trim().length() == 0)
				{
					setMSG("Enter  Date..",'E');
					txtVLDFR.requestFocus();
					return false;
				}
				if(M_fmtLCDAT.parse(txtCFMDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("Date Should Not Be Greater Than Today..",'E');
					txtVLDFR.requestFocus();
					return false;
				}
			    if(txtVLDFR.getText().trim().length() == 0)
				{
					setMSG("Enter From Date..",'E');
					txtVLDFR.requestFocus();
					return false;
				}
				
				if(txtVLDTO.getText().trim().length() == 0)
				{
					setMSG("Enter To Date..",'E');
					txtVLDTO.requestFocus();
					return false;
				}
				
				 if(M_fmtLCDAT.parse(txtVLDTO.getText().trim()).compareTo(M_fmtLCDAT.parse(txtVLDFR.getText().trim()))<0)
				{
					setMSG("To Date Should Be Greater Than Or Equal To From Date..",'E');
					txtVLDTO.requestFocus();
					return false;
				}
		    
			}catch(Exception L_EX)
			{
			    setMSG(L_EX,"This is VldCFORM");
			}
			return true;
			
	     }
}

