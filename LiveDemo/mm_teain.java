import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
class mm_teain extends cl_pbase
{
	final String strINDTR_fn ="10";  
	private CallableStatement cstATIND;
	final int TBL_CHKFL =0;
	final int TBL_MATCD =1;
	final int TBL_UOMCD =2;
	final int TBL_MATDS =3;
	final int TBL_STKQT =4;
	final int TBL_RORLV =5;
	final int TBL_CALQT =6;
	private cl_JTable tblITMDT ;
	private JButton btnAUTIN = new JButton(); ;
	
	mm_teain()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			cstATIND = cl_dat.M_conSPDBA_pbst.prepareCall("{call mm_stprc(?,?,?,?,?,?)}");
			String[] L_COLHD = {"","Item Code","UOM","Item Description ","Stock ","ROR level","Cal.Qty"};
			int[] L_COLSZ = {20,80,40,360,80,80,85};
			int L_intRECCT =0; 
			add(new JLabel("Items for Auto Indent Generation"),1,1,1,8,this,'L');
			tblITMDT = crtTBLPNL1(this,L_COLHD,500,3,1,8,8,L_COLSZ,new int[]{0});	
			add(btnAUTIN = new JButton("Generate Auto Indent"),12,7,1,2,this,'L');
			btnAUTIN.setEnabled(false);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"const");			
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			try
			{
				tblITMDT.setEnabled(false);
				btnAUTIN.setEnabled(false);
				clrCOMP();
				int L_intRECCT =0;
				if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						String L_strSQLQRY = "SELECT ST_MMSBS,ST_STRTP,ST_MATCD,ST_UOMCD,ST_MATDS,ST_STKQT,ST_STKIN,ST_STKIP,ST_STKOR,"+
							 "ST_RORLV,ST_RORQT,isnull(ST_STKQT,0 ) + isnull (ST_STKIN,0 ) + isnull (ST_STKOR,0)L_CALQT FROM MM_STMST WHERE "+
							 " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') not in('X','9') AND isnull(ST_STKQT,0 ) + isnull (ST_STKIN,0 ) + isnull (ST_STKOR,0) < "+
							 "isnull (ST_RORLV,0 ) AND ST_STKFL = 'Y' and ST_STRTP ='"+M_strSBSCD.substring(2,4) +"'"+
							 " ORDER BY ST_STRTP , ST_MATCD";
						M_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
						if(M_rstRSSET !=null)
						{
							while(M_rstRSSET.next())
							{
								
								tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),L_intRECCT,TBL_MATCD);
								tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),L_intRECCT,TBL_UOMCD);
								tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),L_intRECCT,TBL_MATDS);
								tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),""),L_intRECCT,TBL_STKQT);
								tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_RORLV"),""),L_intRECCT,TBL_RORLV);
								tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_CALQT"),""),L_intRECCT,TBL_CALQT);
								L_intRECCT++;					
							}
						}
						if(L_intRECCT >0)
							btnAUTIN.setEnabled(true);
						else
						{
							setMSG("No item is available for generation of Auto Indent..",'N');
							btnAUTIN.setEnabled(false);
						}
					}
			}
			catch(Exception L_E)
			{
				setMSG("Error in displaying data ..",'E');
			}
				
		}
		if(M_objSOURC == btnAUTIN)
		{
			String L_strINDNO  = "",  L_strCODCD = "", L_strCCSVL = "",L_strCHP01="";
			try
			{
				cl_dat.M_flgLCUPD_pbst = true;
				M_strSQLQRY = "Select CMT_CHP01,CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(0,2)+"IND' and ";
				M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strINDTR_fn +"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						 L_strCHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
						 L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
						 L_strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					}
					M_rstRSSET.close();
				}
				if(L_strCHP01.trim().length()== 3)
				{
					setMSG("In use for generating auto indent..",'E');
					if(M_rstRSSET !=null)
						M_rstRSSET.close();
				}
				else
				{
					M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CHP01 = '"+cl_dat.M_strUSRCD_pbst +"'";
					M_strSQLQRY +=" WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='MM"+M_strSBSCD.substring(0,2)+"IND' ";
					M_strSQLQRY += " AND CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strINDTR_fn + "'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(!cl_dat.exeDBCMT("genAUTIND"))
						setMSG("Could not lock the record..",'E');
					else
						setMSG(" ",'N');
				}
				L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL.trim()) + 1);
				int strTEMP = 5-L_strCCSVL.length();
				for(int i=0;i<strTEMP;i++)
					L_strCCSVL = "0"+L_strCCSVL;
				L_strINDNO = L_strCODCD + L_strCCSVL;
				cstATIND.setString(1,cl_dat.M_strCMPCD_pbst);
				cstATIND.setString(1,M_strSBSCD.substring(2,4));
				cstATIND.setString(2,L_strINDNO.trim());
				cstATIND.setString(3,cl_dat.M_strUSRCD_pbst);
				cstATIND.setString(4,"KVM");
				cstATIND.setString(5,"302");
				cstATIND.executeUpdate();
				cl_dat.exeDBCMT("after proceduue");
				M_strSQLQRY ="SELECT COUNT(*) L_CNT from mm_inmst where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND in_strtp ='"+M_strSBSCD.substring(2,4)+"'";
				M_strSQLQRY +=" AND IN_INDNO ='"+L_strINDNO+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
					if(M_rstRSSET.next())
						if(M_rstRSSET.getInt("L_CNT") >0)
						{
							M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CHP01 =null,CMT_CCSVL = '"+L_strCCSVL +"'";
							M_strSQLQRY +=" WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='MM"+M_strSBSCD.substring(0,2) +"IND' ";
							M_strSQLQRY += " AND CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strINDTR_fn + "'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.exeDBCMT("genAUTIND"))
							{
								String L_strEML ="";
								setMSG("Generated an Auto Indent no..."+L_strINDNO,'N');
								JOptionPane.showMessageDialog(this,"Generated an auto Indent no." + L_strINDNO,"Indent No.",JOptionPane.INFORMATION_MESSAGE);		
								btnAUTIN.setEnabled(false);
								cl_eml ocl_eml = new cl_eml();
								M_strSQLQRY = "Select CMT_CODds from CO_CDTRN ";
								M_strSQLQRY += " where CMT_CGMTP = 'EML' and CMT_CGSTP = 'MMXXATI'";
								M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
								if(M_rstRSSET != null)
								{
									while(M_rstRSSET.next())
									{
										L_strEML = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
										ocl_eml.sendfile(L_strEML,null,"Intimation of Auto Indent ","Auto Indent No."+L_strINDNO+" has been generated");
									}
									M_rstRSSET.close();
								}
							}
						}
						else
						{
							M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CHP01 =null";
							M_strSQLQRY +=" WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='MM"+M_strSBSCD.substring(0,2)+"IND' ";
							M_strSQLQRY += " AND CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strINDTR_fn + "'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.exeDBCMT("genAUTIND"))
							{
								setMSG("Auto Indent is not generated.. ",'N');
								//setMSG("Issue has been Authorised,Auto Indent is not Required.. ",'N');
							}
						}
					clrCOMP();
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"btnAUTH ");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
	}
}