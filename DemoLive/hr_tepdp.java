/*
 * Created on Jun 23, 2007
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
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;




public class hr_tepdp extends cl_pbase
{

    
    
    
    
    
    
    private TxtDate txtFMDT;
    private TxtDate txtTODT;
    private TxtLimit txtDPTCD;
    private TxtLimit txtEMPCT;
    private TxtLimit txtFULNM;
    private cl_JTable tblPUNTBL;
    private JButton btnDISP;
    private TxtLimit txtEMPNO;
    private String strFULNM;
    private String strEMPNO;
    private String strPNCDT;
    private int intINOCD;
    private String strSHFCD;
    private static String[] arrDAYS = {"31","28","31","30","31","30","31","31","30","31","30","31"};	
    
    private String strPNCTM;
    private String strWRKDT;
    private String strSCHTM;
    private int intTB_CHKFL=0;
    private int intTB_EMPNO=1;
    private int intTB_FULNM=2;
    private int intTB_WRKDT=3;
    private int intTB_SHFCD=4;
    private int intTB_INOCD=5;
    private int intTB_SCHTM=6;
    private int intTB_PNCTM=7;
    private int intTB_LCOTM=8;
    private int intTB_EGOTM=9;
    private String strLCOTM="";
    private String strEGOTM="";
    

    hr_tepdp()
    {
        super(2);
		try
		{
             setMatrix(20,16);
             add( new JLabel ("From Date"),1,9,1,1.5,this,'L');
		     add(txtFMDT=new TxtDate(),1,11,1,2,this,'L');
		    
		     add(new JLabel("To Date") ,1,13,1,1.3	,this,'L');
		     add(txtTODT=new TxtDate(),1,15,1,2,this,'L');
		     
		     add(new JLabel("Department"),3,1,1,2,this,'L');
		     add(txtDPTCD=new TxtLimit(3),3,3,1,2, this,'L');
		     
		     add(new JLabel("Emp.No") ,3,5,1,1.3,this,'L');
		     add(txtEMPNO=new TxtLimit(4),3,6,1,2, this,'L');
		     
		     add(new JLabel("Name"), 3,8,1,1 ,this,'L');
		     add(txtFULNM =new TxtLimit(50),3,9,1,4 ,this,'L');
		     
		     add(new JLabel(" Category"), 3,13,1,1.5 ,this,'L');
		     add(txtEMPCT =new TxtLimit(10),3,15,1,2 ,this,'L');
		     
		     add(btnDISP=new JButton("Display"),5,12,1,1.8,this,'L');
		     
		     String[] L_staPUNHD = {"Status ","Emp.No","Name","Wrk.Date", "Shift","In/Out","Sch.Time" , "Pch.Time " ,"L/Coming" ,"E/Going"};/**This is for create the coloums */
		     int[] L_intCOLSZ = {30,50,200,75,35,35,100,100,75,75	};
		     tblPUNTBL = crtTBLPNL1(this,L_staPUNHD,5000, 6,1,13,16 ,L_intCOLSZ,new int[]{0});
               
		     
		     txtFMDT.setEnabled( false);
		     txtTODT.setEnabled( false);
		     txtDPTCD.setEnabled( false);
		     txtEMPNO.setEnabled( false);
		     txtFULNM.setEnabled( false);
		     txtEMPCT.setEnabled( false);
		     btnDISP.setEnabled( false);
		     tblPUNTBL.setEnabled( false);
		     
             
		     
             
             
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"Constructor");
		}
    }
    void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		 txtFMDT.setEnabled( false);
	     txtTODT.setEnabled( false);
	     txtDPTCD.setEnabled( false);
	     txtEMPNO.setEnabled( false);
	     txtFULNM.setEnabled( false);
	     txtEMPCT.setEnabled( false);
	     btnDISP.setEnabled( false);
	     tblPUNTBL.setEnabled( false);
	     if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst )) )
		   {
	         txtFMDT.setEnabled( true);
		     txtFMDT.requestFocus();
		     txtDPTCD.setEnabled( true);
		     txtEMPNO.setEnabled( true);
		     txtEMPCT.setEnabled( true);
	         
		   }
	     if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst )) )
		   {
	         txtFMDT.setEnabled( true);
		     txtFMDT.requestFocus();
	         
		   }
	}
    public void actionPerformed(ActionEvent L_AE)
	{
	     super.actionPerformed(L_AE);
		 try
		 {
		      
		     if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst )) 
				{
		         
		         if(M_objSOURC==txtFMDT)
		         {
		             if(txtFMDT.getText() .length() >0)
				       {
		                 txtTODT.setEnabled(true);
		                 txtTODT.requestFocus() ;
				       }
		             else
		             {
		                 setMSG("Please Enter From Date" ,'E');
		                 txtFMDT.requestFocus();
		             }
		         }
		         if(M_objSOURC==txtTODT)
 		         {
 		             if(txtTODT.getText().trim().length() == 0)
 		 			{
 		 				setMSG("Enter To Date..",'E');
 		 				txtTODT.requestFocus();
 		 			}
 		             if(M_fmtLCDAT.parse(txtTODT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDT.getText().trim()))>0)
 		 			 {
     		                 txtDPTCD.setEnabled( true);
     		                txtDPTCD.requestFocus();
 		 			 }
 		             else
 		             {
 		                 setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
 		                 txtTODT.requestFocus() ;
 		             }
 		         }
		             if(M_objSOURC==txtDPTCD)
		             {
		                 if(vldDPTCD())
    		             {   
	    		             txtEMPNO.setEnabled(true);
	    		             txtEMPNO.requestFocus() ;
    		             }
    		             else
    		             {
    		                 setMSG("Please Press F1 .....",'E');
    		                 txtDPTCD.requestFocus() ;
    		             }
		             }
		             if(M_objSOURC==txtEMPNO)
		             {
		                 if(vldEMPNO())
		                 {
		                    txtEMPCT.setEnabled(true);
		                    txtEMPCT.requestFocus();
		                 }
		                 else
		                 {
		                     setMSG("Please Press F1.....",'E');
		                     txtEMPNO .requestFocus() ; 
		                 }
		             }
		            
		             if(M_objSOURC==txtEMPCT)
		             {
		                 if(vldEMPCT())
		                 {
		                     btnDISP.setEnabled(true);
		                     btnDISP.requestFocus();
		                 }
		                 else
		                 {
		                     setMSG("Please Press F1.....",'E');
		                     txtEMPCT .requestFocus() ; 
		                 }
		                 
		             }
		             if(M_objSOURC==btnDISP)
		             {
		                 getTBLREC();
		             }
				}
		 }catch(Exception L_EX)
		 {
		     setMSG(L_EX,"Action Performed");
		 }
	}
    
    
   
    /**
     *  // TODO Auto-generated method stub
     */
    private void getTBLREC() 
    {
       try
       {
           Timestamp L_tmpPNCTM;
           Timestamp L_tmpSCHTM;
           int L_intLCOTM=0;
           String L_strPNCTM="";
           String L_strSCHTM="";
           String L_strPNCTM1="";
           String L_strSCHTM1="";
           String L_strPNCTM2="";
           String L_strSCHTM2="";
           
           M_strSQLQRY="select ep_empno,ep_fulnm,sw_wrkdt,sw_shfcd,'0' sw_inocd,"; 
           M_strSQLQRY+=" sw_sintm sw_schtm,epa_pnctm ";
           M_strSQLQRY+=" from hr_epmst,hr_swtrn left outer join hr_epalg on ";
           M_strSQLQRY+=" ep_empno=epa_empno EP_CMPCD=EPA_CMPCD and SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sw_wrkdt = date(epa_pnctm) and EPA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND epa_inocd='0' where EP_CMPCD=SW_CMPCD and ep_empno=sw_empno ";
           M_strSQLQRY+=" and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_dptcd='"+txtDPTCD.getText() .toString() .trim() +"' and ep_empno = '" +txtEMPNO.getText() .toString() .trim() + "' ";
           M_strSQLQRY+=" and ep_desgn = '" +txtEMPCT.getText() .toString() .trim() + "' and sw_wrkdt between '" +M_fmtDBDAT .format(M_fmtLCDAT.parse(txtFMDT.getText() .toString().trim()))+"' and '"+M_fmtDBDAT .format(M_fmtLCDAT.parse(txtTODT.getText().toString().trim()))+"' union all ";
           
           M_strSQLQRY+=" select ep_empno,ep_fulnm,sw_wrkdt,sw_shfcd,'1' sw_inocd,";
           M_strSQLQRY+=" sw_sottm sw_schtm ,epa_pnctm ";
           M_strSQLQRY+=" from hr_epmst,hr_swtrn left outer join hr_epalg on ";
           M_strSQLQRY+=" ep_empno=epa_empno and sw_wrkdt = date(epa_pnctm) and epa_inocd='1' where ";
           M_strSQLQRY+=" ep_empno=sw_empno EP_CMPCD=SW_CMPCD and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_dptcd='"+txtDPTCD.getText() .toString() .trim() +"' and ep_empno = '" +txtEMPNO.getText() .toString() .trim() + "' ";
           M_strSQLQRY+=" and ep_desgn = '" +txtEMPCT.getText() .toString() .trim() + "' and SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sw_wrkdt between '" +M_fmtDBDAT .format(M_fmtLCDAT.parse(txtFMDT.getText() .toString().trim()))+"' and '"+M_fmtDBDAT .format(M_fmtLCDAT.parse(txtTODT.getText().toString().trim()))+"' ";
           M_strSQLQRY+=" order by ep_empno,sw_wrkdt,sw_inocd ";
           M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
           System.out.println(M_strSQLQRY);
           if(!M_rstRSSET.next() || M_rstRSSET==null)
					return;
           int i=0;
           boolean L_flgEOF = false;
           
           tblPUNTBL.setValueAt(M_rstRSSET.getString("ep_empno"), i, intTB_EMPNO);
           tblPUNTBL.setValueAt(M_rstRSSET.getString("ep_fulnm"), i, intTB_FULNM);
           while(true)
           {
               tblPUNTBL.setValueAt(M_rstRSSET.getString("sw_wrkdt"), i, intTB_WRKDT);
               tblPUNTBL.setValueAt((M_rstRSSET.getString("sw_shfcd").equals("X") ? "" : M_rstRSSET.getString("sw_shfcd")),i,intTB_SHFCD);
               intINOCD =M_rstRSSET.getInt("sw_inocd");
               tblPUNTBL.setValueAt(String.valueOf(intINOCD), i, intTB_INOCD);
               L_tmpSCHTM=M_rstRSSET.getTimestamp("sw_schtm");
               L_tmpPNCTM=M_rstRSSET.getTimestamp("epa_pnctm");

               L_strSCHTM="";
               L_strSCHTM1="";
               if (L_tmpSCHTM != null )
				{
					L_strSCHTM = M_fmtLCDTM.format(L_tmpSCHTM).substring(11);
					L_strSCHTM1= M_fmtLCDTM.format(L_tmpSCHTM).substring(0,10);
					tblPUNTBL.setValueAt(L_strSCHTM1 +" "+L_strSCHTM, i, intTB_SCHTM);
				}
               if(L_tmpPNCTM != null)
               {
					L_strPNCTM = M_fmtLCDTM.format(L_tmpPNCTM).substring(11);
					L_strPNCTM1= M_fmtLCDTM.format(L_tmpPNCTM).substring(0,10);
			    	tblPUNTBL.setValueAt(L_strPNCTM1 +" "+L_strPNCTM , i, intTB_PNCTM);
				}
               if (L_tmpSCHTM != null )
		       {
                   L_strSCHTM2 = M_fmtLCDTM.format(L_tmpSCHTM);
				}
               if(L_tmpPNCTM != null)
               {
                   L_strPNCTM2 = M_fmtLCDTM.format(L_tmpPNCTM);
               }
			   String L_strLCOTM="",L_strEGOTM="";
               if(intINOCD == 0)
               {
                  
                 L_strLCOTM = addTIME("00:00",calTIME(L_strSCHTM2,L_strPNCTM2));
                 tblPUNTBL.setValueAt(L_strLCOTM , i,intTB_LCOTM);
               }
               System.out.println( "0 ... " +strLCOTM);
               if(intINOCD==1)
               {
                  L_strEGOTM = addTIME("00:00",calTIME(L_strSCHTM2,L_strPNCTM2));
                   tblPUNTBL.setValueAt(L_strEGOTM , i,intTB_EGOTM);
               }
               
               
               System.out.println("1....." +strEGOTM);
               
               
         //      System.out.println(L_strPNCTM.substring(0,10));
               
               
               /*Calendar c1 = Calendar.getInstance(); 	//new GregorianCalendar();	
               Calendar c2 = Calendar.getInstance();
               System.out.println(L_strSCHTM1.substring( 6,10));
               System.out.println(L_strPNCTM1.substring( 6,8));
             //  System.out.println(L_strPNCTM1.substring(3,5));
               
               //System.out.println(Integer.parseInt(L_strPNCTM1.substring(6,10))+" " +Integer.parseInt(L_strPNCTM1.substring(3,5))+" " +Integer.parseInt(L_strPNCTM1.substring(0,3)));
              //c1.set(Integer.parseInt(L_strPNCTM1.substring(6,10)),Integer.parseInt(L_strPNCTM1.substring(3,5)),Integer.parseInt(L_strPNCTM1.substring(0,2)) );
              //c2.set(Integer.parseInt(L_strSCHTM1.substring(6,10)),Integer.parseInt(L_strSCHTM1.substring(3,5)),Integer.parseInt(L_strSCHTM1.substring(0,2)) );
               
              System.out.println((c2.getTime().getTime() - c1.getTime().getTime())/(24*3600*1000));
            //   System.out.println("L_strPNCTM + L_strSCHTM...  " +L_strPNCTM +"/" +L_strSCHTM);
               //M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				//M_calLOCAL.add(Calendar.DATE,-7);
				//String L_strDTCHK = M_fmtLCDAT.format(M_calLOCAL.getTime());
              //  L_intLCOTM=Integer.parseInt(L_strPNCTM) - Integer.parseInt(L_strSCHTM);
               //System.out.println("L_intLCOTM  " +L_intLCOTM);*/
               i++;
               if(!M_rstRSSET.next())
				 break;	
           }
           
       }catch(Exception L_EX)
       {
           setMSG(L_EX,"......GeTBLREC......");
       }
        
    }
    /**
     * @return  // TODO Auto-generated method stub
     */
    private boolean vldEMPNO() 
    {
        try{
			M_strSQLQRY = "Select EP_FULNM, EP_EMPNO from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO = '"+txtEMPNO.getText() .toString() .trim() +"'  and EP_DPTCD= '" + txtDPTCD.getText() .toString().trim() +"' ";
			if(txtDPTCD.getText() .length() ==0)
		    M_strSQLQRY = "Select distinct EP_EMPNO from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO = '"+txtEMPNO.getText() .toString() .trim() +"'  ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				strFULNM=M_rstRSSET.getString("ep_fulnm");
				txtFULNM.setText( strFULNM);
			    return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldEMPNO ....."+L_EX);							   
		}
       
        return false;
    }
    /**
     * @return // TODO Auto-generated method stub
     */
    private boolean vldEMPCT()
	{
	    try{
			M_strSQLQRY = "Select distinct EP_DESGN from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_DPTCD = '"+txtDPTCD.getText() .toString() .trim() +"' and  EP_DESGN= '"+txtEMPCT.getText() .toString() .trim() +"' and  EP_EMPNO= '"+txtEMPNO.getText() .toString() .trim() +"' ";
			if(txtEMPNO.getText() .length() >0)
			M_strSQLQRY = "Select distinct EP_DESGN from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  EP_DESGN= '"+txtEMPCT.getText() .toString() .trim() +"' and  EP_EMPNO= '"+txtEMPNO.getText() .toString() .trim() +"' ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldEMPCT ....."+L_EX);							   
		}
	    return false;
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
			    if(M_objSOURC==txtDPTCD)
			    {
			        cl_dat.M_flgHELPFL_pbst = true;
			        M_strHLPFLD = "txtDPTCD";
					String L_ARRHDR[] = {"Department Code","Department Description"};
			        M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			    }
			    if(M_objSOURC==txtEMPNO)
			    {
			        cl_dat.M_flgHELPFL_pbst = true;
			        M_strHLPFLD = "txtEMPNO";
					String L_ARRHDR[] = {"Employee No","Employee Name"};
			        M_strSQLQRY = "Select distinct EP_EMPNO,EP_FULNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_LFTDT is null and EP_DPTCD= '" + txtDPTCD.getText() .toString().trim() +"'";
					if(txtDPTCD.getText() .length() ==0)
				    M_strSQLQRY = "Select distinct EP_EMPNO,EP_FULNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_LFTDT is null" ;
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			    }
			    if(M_objSOURC==txtEMPCT)
			    {
			        cl_dat.M_flgHELPFL_pbst = true;
			        M_strHLPFLD = "txtEMPCT";
					String L_ARRHDR[] = {"Employee Category"};
			        M_strSQLQRY = "Select distinct EP_DESGN from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_DPTCD= '" + txtDPTCD.getText() .toString().trim() +"' and  EP_EMPNO = '" + txtEMPNO.getText() .toString().trim() +"'";
			        if(txtEMPNO.getText() .length() >0)
			        M_strSQLQRY = "Select distinct EP_DESGN from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO = '" + txtEMPNO.getText() .toString().trim() +"'";
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,1,"CT");
			    }
			}
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"Key Pressed");
		}
	}
    void exeHLPOK()
    {
        super.exeHLPOK();
	    if(M_strHLPFLD.equals("txtDPTCD"))
		{
	        txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
		}
	    if(M_strHLPFLD.equals("txtEMPCT"))
		{
	        txtEMPCT.setText(cl_dat.M_strHLPSTR_pbst);
		}
	    if(M_strHLPFLD.equals("txtEMPNO"))
		{
	        txtEMPNO.setText(cl_dat.M_strHLPSTR_pbst);
	        strFULNM=String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim() ;
	        txtFULNM.setText(strFULNM);
		}
    }
    private boolean vldDPTCD()
	{
	    try{
			M_strSQLQRY = "Select CMT_CODCD,EP_DPTCD from CO_CDTRN,HR_EPMST  WHERE CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'  and EP_DPTCD='"+txtDPTCD.getText().toString().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
			   
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldDPTCD ....."+L_EX);							   
		}
	    return false;
	}
    /**
    Method to add time given in second parameter to the time in first parameter 
    and returns the result in HH:MM format
	@param P_strSTRTM String argument to pass Starting Time.
	@param P_strNEWTM String argument to pass New Time.
    */
    private String addTIME(String P_strSTRTM,String P_strNEWTM)
    {
	    String L_strRETSTR = "";
		try
        {
            if (P_strSTRTM.equals(""))  P_strSTRTM = "00:00";
            if (P_strNEWTM.equals(""))  P_strNEWTM = "00:00";            
            int  L_intSTRLN = P_strSTRTM.trim().length();
            int  L_intNEWLN = P_strNEWTM.trim().length();
            int  L_intSTRCL = P_strSTRTM.indexOf(":");
            int  L_intNEWCL = P_strNEWTM.indexOf(":");                
            int  L_intSTRMN = Integer.parseInt(P_strSTRTM.substring(0,L_intSTRCL))*60+Integer.parseInt(P_strSTRTM.substring(L_intSTRCL+1,L_intSTRLN));
            int  L_intNEWMN = Integer.parseInt(P_strNEWTM.substring(0,L_intNEWCL))*60+Integer.parseInt(P_strNEWTM.substring(L_intNEWCL+1,L_intNEWLN));
            int  L_intTOTHR = (L_intSTRMN+L_intNEWMN) / 60;
            int  L_intTOTMN = (L_intSTRMN+L_intNEWMN)- ((L_intSTRMN+L_intNEWMN)/60)*60;
            String L_strTOTHR1 = "0000"+String.valueOf(L_intTOTHR).trim();
            String L_strTOTMN1 = "0000"+String.valueOf(L_intTOTMN).trim();                
            int L_intLENHR = L_strTOTHR1.length();
            int L_intLENMN = L_strTOTMN1.length();
            int L_intTOTCL;
            if (L_intTOTHR < 100)  
                L_intTOTCL = 2;
             else if (L_intTOTHR < 1000)
                    L_intTOTCL = 3;
                else 
                    L_intTOTCL = 4;
           L_strRETSTR = L_strTOTHR1.substring(L_intLENHR-L_intTOTCL,L_intLENHR)+":"+L_strTOTMN1.substring(L_intLENMN-2,L_intLENMN);                
           return  L_strRETSTR;
		}
        catch(Exception L_EX)
        {
		    setMSG(L_EX, "addTIME");
    		return "";
		}            
    }
    /**
    Method to Calculate the differance two Date & Time.
	@param P_strFINTM String argument to Final Time.
	@param P_strINITM String argument to Initial Time.
    */
	public String calTIME(String P_strFINTM,String P_strINITM)
	{
		String L_strDIFTM = "";
		try
		{
			int L_intYRS,L_intMTH,L_intDAY,L_intHRS,L_intMIN;
			int L_intYRS1,L_intMTH1,L_intDAY1,L_intHRS1,L_intMIN1;
			int L_intYRS2,L_intMTH2,L_intDAY2,L_intHRS2,L_intMIN2;
			String L_strHOUR,L_strMINT;			
			if(P_strFINTM.equals("") || P_strINITM.equals(""))
				return L_strDIFTM;			
			// Seperating year,month,day,hour & minute from Final time
			L_intYRS1 = Integer.parseInt(P_strFINTM.substring(6,10));
			L_intMTH1 = Integer.parseInt(P_strFINTM.substring(3,5));
			L_intDAY1 = Integer.parseInt(P_strFINTM.substring(0,2));
			L_intHRS1 = Integer.parseInt(P_strFINTM.substring(11,13));
			L_intMIN1 = Integer.parseInt(P_strFINTM.substring(14));			
			// Seperating year,month,day,hour & minute from Initial time
			L_intYRS2 = Integer.parseInt(P_strINITM.substring(6,10));
			L_intMTH2 = Integer.parseInt(P_strINITM.substring(3,5));
			L_intDAY2 = Integer.parseInt(P_strINITM.substring(0,2));
			L_intHRS2 = Integer.parseInt(P_strINITM.substring(11,13));
			L_intMIN2 = Integer.parseInt(P_strINITM.substring(14));			
			L_intMIN = L_intMIN1 - L_intMIN2;
			L_intHRS = L_intHRS1 - L_intHRS2;			
			// Checking for leap year
			if(L_intYRS1%4 == 0)
				arrDAYS[1] = "29";
			else
				arrDAYS[1] = "28";			
			// Final date is of next month
			if(L_intMTH1 > L_intMTH2)
			{
				for(int i = L_intMTH2;i < L_intMTH1;i++)
					L_intDAY1 += Integer.parseInt(arrDAYS[i-1]);
			}			
			L_intDAY = L_intDAY1 - L_intDAY2;			
			if(L_intMIN < 0)
			{
				L_intMIN += 60;
				L_intHRS--;
			}
			if(L_intHRS < 0)
			{
				L_intHRS += 24;
				L_intDAY--;
			}
			if(L_intDAY > 0)
				L_intHRS += L_intDAY*24;			
			L_strHOUR = String.valueOf(L_intHRS);
			L_strMINT = String.valueOf(L_intMIN);
			if(L_strHOUR.length() < 2)
				L_strHOUR = "0" + L_strHOUR;
			if(L_strMINT.length() < 2)
				L_strMINT = "0" + L_strMINT;
			L_strDIFTM = L_strHOUR + ":" + L_strMINT;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calTIME");
		}
		return L_strDIFTM;
	}	
}
