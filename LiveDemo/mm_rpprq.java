
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.sql.ResultSet;

class mm_rpprq extends cl_rbase
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"mm_rpprq.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
	private DataOutputStream D_OUT ;
	private JTextField txtFRMDT;	//txtDOCDT.setText(cl_dat.M_txtCLKDT_pbst.getText());		
	private JTextField txtTODT;
	private JTextField txtFRMNO;
	private static String strFRMNO="";
	private static String strTONO="";
	private String frmDT="",toDT="",strBILTP="",strSTRTP="NIL",	strPORRF="NIL",strPORDS="",strGRNNO="";
	private static String strFRMDT="";
	private static String strTODT="";

	private JTextField txtTONO;
	private JComboBox cmbPRQTP;
	private JRadioButton rdbDT,rdbNO;	
	private JLabel lblDATE,lblNO,lblFRMNO,lblTONO;
	private String strPRQTP,strDETL,strCMB,M_strTOTAL;;
	private Hashtable<Object,Object> hst;
	private Hashtable<String,String> hstBLMST;
	private int selINDEX;
    
	int flaghtml=0; //0 text selected, 1 html selected 
	Hashtable<String,String> hst1to100;
	Thread thrSTORE;
	mm_rpprq()		/*  Constructor   */
	{
		super(2);

		try
		{
			hst=new Hashtable<Object,Object>();
			hstBLMST=new Hashtable<String, String>();
			//System.out.println("hiiiiiiiiii");
			//thrSTORE=new Thread(this);
			//thrSTORE.start();
			setMatrix(20,20);
			add(new JLabel("PR TYPE :"),2,2,1,2,this,'L');
			cmbPRQTP=new JComboBox(new String[]{"ALL"});
			add(cmbPRQTP,2,4,1,4,this,'L');
			//add(cmbPRQTP=new JComboBox(new String[]{"Select option"}),2,4,1,3,this,'L');
			//cmbPRQTP.addActionListener(this);

			//add(lblDETAIL=new JLabel("Content"),2,7,1,5,this,'L');
			//lblDETAIL.setVisible(false);

			add(lblDATE=new JLabel("Date Selection"),4,2,1,2,this,'L');			
			add(rdbDT=new JRadioButton(),4,4,1,1,this,'L');

			add(lblNO=new JLabel("PR No Selection"),4,5,1,3,this,'L');			
			add(rdbNO=new JRadioButton(),4,7,1,1,this,'L');
			ButtonGroup bg=new ButtonGroup();
			bg.add(rdbDT);
			bg.add(rdbNO);
			rdbDT.setSelected(true);
			rdbDT.addActionListener(this);
			rdbNO.addActionListener(this);

			add(new JLabel("PR Date   From :"),5,2,1,3,this,'L');			
			add(txtFRMDT = new TxtDate(),5,4,1,2,this,'L');	
			add(new JLabel("   To :"),5,7,1,1,this,'L');			
			add(txtTODT = new TxtDate(),5,8,1,2,this,'L');


			add(lblFRMNO=new JLabel("PR  No  From :"),6,2,1,3,this,'L');			
			add(txtFRMNO = new TxtNum(),6,4,1,2,this,'L');			
			add(lblTONO=new JLabel("   To :"),6,7,1,1,this,'L');			
			add(txtTONO = new TxtNum(),6,8,1,2,this,'L');		
			setENBL(false);

			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_pnlRPFMT.setVisible(true);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			this.crtHST();	
			setENBL(false);
			M_strSQLQRY="select substring(CMT_CODCD,2,2) CMT_CODCD,CMT_CODDS FROM CO_CDTRN where CMT_CGSTP='MMXXBLP' and substring(CMT_CGMTP,2,2)='"+cl_dat.M_strCMPCD_pbst+"' order by CMT_CODCD";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				int i=0;
				//System.out.print(M_rstRSSET.getString(1));
				while(M_rstRSSET.next())
				{	   	   	 

					cmbPRQTP.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));


				}
				M_rstRSSET.close();
			}


		}
		catch(Exception L_SQLE)
		{
			
			setMSG(L_SQLE,"constructor");
		}

	}


	public void actionPerformed(ActionEvent L_AE)
	{	
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{

				clrCOMP();
				System.out.println("comb sel");

				//if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					//System.out.println("comb sel>0");			

					//setENBL(false);cmbPRQTP.setEnabled(true);
					rdbNO.setVisible(false);
					txtFRMNO.setVisible(false);
					txtTONO.setVisible(false);
					lblNO.setVisible(false);
					lblFRMNO.setVisible(false);
					lblTONO.setVisible(false);

				}
			}



			if(M_objSOURC==cmbPRQTP)
			{
				System.out.println("my comb selected");

				//if(cmbPRQTP.getSelectedIndex()>0)			
				{ 
					//clrCOMP();

					selINDEX=cmbPRQTP.getSelectedIndex();
					strPRQTP=cmbPRQTP.getSelectedItem().toString().trim().substring(0,2);					
					strDETL=cmbPRQTP.getSelectedItem().toString().trim().substring(2);
					System.out.println(" my comb sel>0");
					setENBL(true);
					txtFRMDT.setText(cl_dat.M_txtCLKDT_pbst.getText());
					txtTODT.setText(cl_dat.M_txtCLKDT_pbst.getText());					
					txtFRMNO.setEnabled(false);
					txtTONO.setEnabled(false);	
					txtFRMDT.requestFocus();
					rdbDT.setSelected(true);
					txtFRMNO.setText("");
					txtTONO.setText("");
					txtFRMDT.setText("");
					txtTODT.setText("");


				}
				//else
				{
					

					rdbDT.setSelected(true);

				}
			}

			if(M_objSOURC==rdbDT)
			{
				{
					clrCOMP();
					strFRMNO="";
					strTONO="";
					strFRMDT="";
					strTODT="";
					cmbPRQTP.setSelectedIndex(selINDEX);						
					txtFRMDT.setText(cl_dat.M_txtCLKDT_pbst.getText());						 
					txtTODT.setText(cl_dat.M_txtCLKDT_pbst.getText());								
					txtFRMNO.setEnabled(false);
					txtTONO.setEnabled(false);	
					txtFRMDT.setEnabled(true);
					txtTODT.setEnabled(true);
					txtFRMDT.requestFocus();
					rdbDT.setSelected(true);
				}

			}
			if(M_objSOURC==rdbNO)
			{

				{
					clrCOMP();
					strFRMNO="";
					strTONO="";
					strFRMDT="";
					strTODT="";


					cmbPRQTP.setSelectedIndex(selINDEX);
					txtFRMDT.setText("");
					txtTODT.setText("");
					txtFRMDT.setEnabled(false);
					txtTODT.setEnabled(false);	
					txtFRMNO.setEnabled(true);
					txtTONO.setEnabled(true);							 
					txtFRMNO.requestFocus();	
					rdbNO.setSelected(true);
				}
			}	
			if(M_objSOURC==cmbPRQTP)
			{
				System.out.print("cobo selected");
				if(cmbPRQTP.getSelectedItem().toString().trim().equalsIgnoreCase("ALL"))
				{
					rdbNO.setVisible(false);
					txtFRMNO.setVisible(false);
					txtTONO.setVisible(false);
					lblNO.setVisible(false);
					lblFRMNO.setVisible(false);
					lblTONO.setVisible(false);
					
					
					
				}
				else
					{rdbNO.setVisible(true);
				txtFRMNO.setVisible(true);
				txtTONO.setVisible(true);
				lblNO.setVisible(true);
				lblFRMNO.setVisible(true);
				lblTONO.setVisible(true);
				
					}
			}


		}
		catch(Exception L_EX)
		{
			
			setMSG(L_EX,"KeyPressed");
		}
	}

	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);

		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{		
			try{
				if(M_objSOURC==txtFRMNO)
				{
					M_strHLPFLD="txtFRMNO";
					//M_strSQLQRY="Select distinct PT_PRTCD,PT_SHRNM,PT_PRTNM from CO_PTMST where  PT_PRTTP='D' ";
					M_strSQLQRY="Select distinct BL_DOCNO,BL_BLPDT,BL_PRTNM";
					M_strSQLQRY+=" FROM MM_BLMST ";
					M_strSQLQRY+=" WHERE BL_BILTP='"+strPRQTP+"' ";		
					if(txtFRMNO.getText().length()>0)
						M_strSQLQRY+=" AND BL_DOCNO like '"+txtFRMNO.getText().trim()+"%'";                
					M_strSQLQRY+=" AND BL_STSFL!='X' order BY BL_DOCNO desc";

					cl_hlp(M_strSQLQRY,1,1,new String[]{"DOC. NO. ", "DOC. DATE","SUPPLIER"},3,"CT",new int[]{80,100,325});
				}

				if(M_objSOURC==txtTONO)
				{
					M_strHLPFLD="txtTONO";
					//M_strSQLQRY="Select distinct PT_PRTCD,PT_SHRNM,PT_PRTNM from CO_PTMST where  PT_PRTTP='D' ";
					M_strSQLQRY="Select distinct BL_DOCNO,BL_BLPDT,BL_PRTNM ";
					M_strSQLQRY+=" FROM MM_BLMST ";
					M_strSQLQRY+=" WHERE BL_BILTP='"+strPRQTP+"' ";		
					if(txtFRMNO.getText().length()>0)
						M_strSQLQRY+=" AND BL_DOCNO like '"+txtTONO.getText().trim()+"%'";                
					M_strSQLQRY+=" AND BL_STSFL!='X' order BY BL_DOCNO desc";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"DOC. NO. ", "DOC. DATE","SUPPLIER"},3,"CT",new int[]{80,100,325});
				}	
			}
			catch(Exception e)
			{
				
			}


		}		


		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			try
			{
				if(M_objSOURC == txtTODT)
				{
					//cl_dat.M_btnSAVE_pbst.setEnabled(true);
					if(txtTODT.getText().length()>0){strTODT=txtTODT.getText().trim();}

					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				if(M_objSOURC == txtFRMDT)
				{	
					if(txtFRMDT.getText().length()>0){strFRMDT=txtFRMDT.getText().trim();}

					txtTODT.requestFocus();

				}

				if(M_objSOURC == txtTONO)
				{
					//cl_dat.M_btnSAVE_pbst.setEnabled(true);
					if(txtTONO.getText().length()>0){strTONO=txtTONO.getText().trim();
					System.out.println("---"+strTONO);
					}

					cl_dat.M_btnSAVE_pbst.requestFocus();


				}
				if(M_objSOURC == txtFRMNO)
				{
					if(txtFRMNO.getText().length()>0)
					{strFRMNO=txtFRMNO.getText().trim();
					System.out.println("+++++"+strFRMNO);
					}

					txtTONO.requestFocus();
					//vldDATA();
					//if(!vldDATA())
					//setMSG("TO field should be greater",'E');

				}



			}
			catch(Exception L_EK)
			{
				
				setMSG(L_EK,"Key Evelnt");
			}
		}
	}


	void exeHLPOK()
	{
		super.exeHLPOK() ;
		try
		{			

			setMSG("",'N');
			if(M_strHLPFLD.equals("txtFRMNO"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtFRMNO.setText(L_STRTKN1.nextToken());

			}
			if(M_strHLPFLD.equals("txtTONO"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtTONO.setText(L_STRTKN1.nextToken());

			}

		}catch(Exception L_EX)
		{
			
			setMSG(L_EX,"exeHLPOK"); 
		}
	}

	public String getCURWRD(String LP_CURVL,String LP_DENOM){
		//String to be printed
		String L_PRNSTR = "";
		try{
			//Remainder of divison
			int L_REMVL = 0;
			//Quotient of divison
			int L_QUOVL = 0;
			//Getting rupees
			String L_RPSVL = LP_CURVL.substring(0,LP_CURVL.indexOf(".")).trim();
			//Converting rupees in string to integer
			int L_RUPVL = Integer.parseInt(L_RPSVL);
			//Getting paise
			String L_PAIVL = LP_CURVL.substring(LP_CURVL.lastIndexOf(".")+1,LP_CURVL.length()).trim();
			//Taking length of rupees value
			int L_ABSVL = L_RPSVL.length();
			if(L_ABSVL > 0){
				//Ones
				if(L_ABSVL == 1){
					L_PRNSTR += chkZERO(hst1to100.get(L_RPSVL).toString(),"",' ');
				}
				//Tens
				else if(L_ABSVL == 2){
					L_PRNSTR += chkZERO(hst1to100.get(L_RPSVL).toString(),"",' ');
				}
				//Hundreds
				else if(L_ABSVL == 3){
					L_QUOVL = L_RUPVL/100;
					L_REMVL = L_RUPVL%100;
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)),"Hundred",'B');
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)),"and",'F');
				}
				//Thousands
				else if(L_ABSVL == 4 || L_ABSVL == 5){
					L_QUOVL = L_RUPVL/1000;
					L_REMVL = L_RUPVL%1000;
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Thousand",'B');
					L_QUOVL = L_REMVL/100;
					L_REMVL = L_REMVL%100;
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');

				}
				//Lacs
				else if(L_ABSVL == 6 || L_ABSVL == 7){
					L_QUOVL = L_RUPVL/100000; //for getting lac value
					L_REMVL = L_RUPVL%100000;
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Lac",'B');
					L_QUOVL = L_REMVL/1000;  //for getting thousand value
					L_REMVL = L_REMVL%1000;
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Thousand",'B');
					L_QUOVL = L_REMVL/100;  //for getting hundred value
					L_REMVL = L_REMVL%100;
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
				}
				//Crores
				else if(L_ABSVL == 8 || L_ABSVL == 9){
					L_QUOVL = L_RUPVL/10000000; //for getting crore value
					L_REMVL = L_RUPVL%10000000;
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Crore",'B');
					L_QUOVL = L_REMVL/100000;  //for getting lac value
					L_REMVL = L_REMVL%100000;
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Lac",'B');
					L_QUOVL = L_REMVL/1000;  //for getting thousand value
					L_REMVL = L_REMVL%1000;
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Thousand",'B');
					L_QUOVL = L_REMVL/100;  //for getting hundred value
					L_REMVL = L_REMVL%100;
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
					L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
				}
			}
			if(L_PAIVL.length() > 0 && !L_PAIVL.equals("00")){
				if(L_PAIVL.substring(0,1).equals("0"))
					L_PAIVL =L_PAIVL.substring(1);
				L_PRNSTR += chkZERO(hst1to100.get(L_PAIVL),"and "+LP_DENOM,'F');
			}
			L_PRNSTR += "Only";
		}catch(Exception L_EX){
		setMSG(L_EX,"getCURWRD");
		}
		return L_PRNSTR;
	}


	private String chkZERO(String LP_STRVL,String LP_RPSVL,char LP_PLCSTR){
		String L_RTNSTR = "";
		try{
			System.out.println("LP_STRVL"+LP_STRVL);

			if(LP_STRVL==null || LP_STRVL.equalsIgnoreCase("zero")){
				System.out.println("reached here 5"+5);
				return "";
			}else{
				if(LP_PLCSTR == 'F'){System.out.print("reached here 6"+6);

				L_RTNSTR = LP_RPSVL + " " +  LP_STRVL + " ";
				}
				else if(LP_PLCSTR == 'B'){System.out.print("reached here 7"+7);

				L_RTNSTR = LP_STRVL + " " + LP_RPSVL + " ";
				}
			}
		}catch(Exception L_EX){
			
			setMSG(L_EX,"chkZERO");
		}
		return L_RTNSTR;
	}

	protected String setNumberFormat(double P_dblNUMBR,int P_intFRCTN)
	{
		NumberFormat L_fmtNUMFM=NumberFormat.getNumberInstance();
		L_fmtNUMFM.setMaximumFractionDigits(P_intFRCTN);
		L_fmtNUMFM.setMinimumFractionDigits(P_intFRCTN);
		String L_strTEMP=(L_fmtNUMFM.format(P_dblNUMBR));
		StringTokenizer L_stkSTTKN=new StringTokenizer(L_strTEMP,",");
		L_strTEMP="";
		while(L_stkSTTKN.hasMoreTokens())
		{
			L_strTEMP+=L_stkSTTKN.nextToken();
		}
		return L_strTEMP;
	}

	public void crtHST()
	{
		hst1to100 = new Hashtable<String,String>();
		hst1to100.put("0","zero");hst1to100.put("1","One");hst1to100.put("2","Two");hst1to100.put("3","Three");hst1to100.put("4","Four");
		hst1to100.put("5","Five");hst1to100.put("6","Six");hst1to100.put("7","Seven");hst1to100.put("8","Eight");
		hst1to100.put("9","Nine");hst1to100.put("10","Ten");hst1to100.put("11","Eleven");hst1to100.put("12","Twelve");
		hst1to100.put("13","Thirteen");hst1to100.put("14","Fourteen");hst1to100.put("15","Fifteen");hst1to100.put("16","Sixteen");
		hst1to100.put("17","Seventeen");hst1to100.put("18","Eighteen");hst1to100.put("19","Nineteen");hst1to100.put("20","Twenty");
		hst1to100.put("21","Twenty One");hst1to100.put("22","Twenty Two");hst1to100.put("23","Twenty Three");hst1to100.put("24","Twenty Four");
		hst1to100.put("25","Twenty Five");hst1to100.put("26","Twenty Six");hst1to100.put("27","Twenty Seven");hst1to100.put("28","Twenty Eight");
		hst1to100.put("29","Twenty Nine");hst1to100.put("30","Thirty");hst1to100.put("31","Thirty One");hst1to100.put("32","Thirty Two");
		hst1to100.put("33","Thirty Three");hst1to100.put("34","Thirty Four");hst1to100.put("35","Thirty Five");hst1to100.put("36","Thirty Six");
		hst1to100.put("37","Thirty Seven");hst1to100.put("38","Thirty Eight");hst1to100.put("39","Thirty Nine");hst1to100.put("40","Fourty");
		hst1to100.put("41","Fourty One");hst1to100.put("42","Fourty Two");hst1to100.put("43","Fourty Three");hst1to100.put("44","Fourty Four");
		hst1to100.put("45","Fourty Five");hst1to100.put("46","Fourty Six");hst1to100.put("47","Fourty Seven");hst1to100.put("48","Fourty Eight");
		hst1to100.put("49","Fourty Nine");hst1to100.put("50","Fifty");hst1to100.put("51","Fifty One");hst1to100.put("52","Fifty Two");
		hst1to100.put("53","Fifty Three");hst1to100.put("54","Fifty Four");hst1to100.put("55","Fifty Five");hst1to100.put("56","Fifty Six");
		hst1to100.put("57","Fifty Seven");hst1to100.put("58","Fifty Eight");hst1to100.put("59","Fifty Nine");hst1to100.put("60","Sixty");
		hst1to100.put("61","Sixty One");hst1to100.put("62","Sixty Two");hst1to100.put("63","Sixty Three");hst1to100.put("64","Sixty Four");
		hst1to100.put("65","Sixty Five");hst1to100.put("66","Sixty Six");hst1to100.put("67","Sixty Seven");hst1to100.put("68","Sixty Eight");
		hst1to100.put("69","Sixty Nine");hst1to100.put("70","Seventy");hst1to100.put("71","Seventy One");hst1to100.put("72","Seventy Two");hst1to100.put("73","Seventy Three");
		hst1to100.put("74","Seventy Four");hst1to100.put("75","Seventy Five");hst1to100.put("76","Seventy Six");hst1to100.put("77","Seventy Seven");
		hst1to100.put("78","Seventy Eight");hst1to100.put("79","Seventy Nine");hst1to100.put("80","Eighty");hst1to100.put("81","Eighty One");
		hst1to100.put("82","Eighty Two");hst1to100.put("83","Eighty Three");hst1to100.put("84","Eighty Four");hst1to100.put("85","Eighty Five");		
		hst1to100.put("86","Eighty Six");hst1to100.put("87","Eighty Seven");hst1to100.put("88","Eighty Eight");hst1to100.put("89","Eighty Nine");
		hst1to100.put("90","Ninety");hst1to100.put("91","Ninety One");hst1to100.put("92","Ninety Two");hst1to100.put("93","Ninety Three");
		hst1to100.put("94","Ninety Four");hst1to100.put("95","Ninety Five");hst1to100.put("96","Ninety Six");hst1to100.put("97","Ninety Seven");
		hst1to100.put("98","Ninety Eight");hst1to100.put("99","Ninety Nine");

	}

	String strLIN(int LP_intLINE)
	{
		String chrDASH="_";
		String strLIN="";
		try{
			for(int i=0;i<LP_intLINE;i++)
			{		
				strLIN+=chrDASH;
			}
		}
		catch(Exception e)
		{
			setMSG(e,"error in strLIN");
		}

		return strLIN;

	}

	String strPGBRK(int LP_intLINE)
	{
		String chrDASH="-";
		String strLIN="";
		try{
			for(int i=0;i<LP_intLINE;i++)
			{		
				strLIN+=chrDASH;
			}
		}
		catch(Exception e)
		{
			setMSG(e,"error in strLIN");
		}

		return strLIN;

	}


	String strBLKSPACE(int LP_intLINE)
	{
		String chrSPACE=" ";
		String strBLKSPACE="";
		try{
			for(int i=0;i<LP_intLINE;i++)
			{		
				strBLKSPACE+=chrSPACE;
			}
		}
		catch(Exception e)
		{
			setMSG(e,"error in strLIN");
		}

		return strBLKSPACE;

	}

	String strLSTLIN(int LP_intLINE)
	{
		String chrSPACE="_";
		String strLSTLIN="";
		try{
			for(int i=0;i<LP_intLINE;i++)
			{		
				strLSTLIN+=chrSPACE;
			}
		}
		catch(Exception e)
		{
			setMSG(e,"error in strLIN");
		}

		return "|"+strLSTLIN+"|";

	}

	void genRPTFL()  //generate reprt file
	{
		ResultSet L_rstRSSET1=null;

		try
		{
			//String str=getCURWRD("876.0","Rupees");
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			genRPHDR();
			
			if((strFRMDT.trim().length()>0)&&(strTODT.trim().length()>0))
			{
				frmDT=M_fmtDBDAT.format(M_fmtLCDAT.parse(strFRMDT.trim()));
				toDT=M_fmtDBDAT.format(M_fmtLCDAT.parse(strTODT.trim()));				
			}

			double dblBLAMT=0.0,dblEXGRT=0.0;
			////////////////for writing content for the file////////////////

			System.out.println("......."+strFRMNO.trim());
			System.out.println("......."+strTONO.trim());
			System.out.println("......."+strFRMDT.trim());
			System.out.println("......."+strTODT.trim());

	   M_strSQLQRY="SELECT DISTINCT BL_DOCNO,BIL_DOCRF FROM MM_BLMST,MM_BILTR ";
       M_strSQLQRY+=" WHERE BL_DOCNO=BIL_DOCNO ";
       M_strSQLQRY+=" AND BL_STSFL !='X' AND BL_BILTP !='03'"; 
       
       if(rdbDT.isSelected())
			M_strSQLQRY+=" AND BL_BLPDT BETWEEN '"+frmDT+"' AND '"+toDT+"'";
		
		else if(rdbNO.isSelected())
			M_strSQLQRY+=" AND BL_DOCNO BETWEEN '"+txtFRMNO.getText().trim()+"' AND '"+txtTONO.getText().trim()+"'"; 
		

			if(L_rstRSSET1!=null)
				L_rstRSSET1.close();
			
			
			L_rstRSSET1=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET1!=null)
			{		while(L_rstRSSET1.next())				
					{
					hstBLMST.put(L_rstRSSET1.getString("BL_DOCNO").trim(), L_rstRSSET1.getString("BIL_DOCRF").trim());
					}

			      
			}
			else
			{
				
				System.out.print("*******null in resultset");
			
			}
			if(L_rstRSSET1!=null)
				L_rstRSSET1.close();
			
			M_strSQLQRY="";
			
				System.out.println("***inside ALL");
				M_strSQLQRY = "Select DISTINCT BL_BILTP,BL_DOCNO,BL_BLPDT,BL_PRTNM,BL_PMDDT,BIL_STRTP,BL_BILNO,BIL_PORRF,BL_BILDT,";
				M_strSQLQRY+= "CMT_CHP01 CURFDS,CMT_CODDS BL_CURDS,BL_CALAM,BL_EXGRT,PO_PORNO,PO_SHRDS";
				M_strSQLQRY+=" FROM MM_BLMST,CO_CDTRN,MM_BILTR LEFT OUTER JOIN MM_POMST ON  PO_STRTP=BIL_STRTP and PO_PORNO=BIL_PORRF WHERE  BL_BILTP=BIL_BILTP AND BL_DOCNO=BIL_DOCNO  AND CMT_CGSTP='COXXCUR' AND CMT_CODCD=BL_CURCD ";
				
							
			
			if(!((String)cmbPRQTP.getSelectedItem()).trim().equalsIgnoreCase("ALL"))
			{
				M_strSQLQRY+=" AND BL_BILTP='"+cmbPRQTP.getSelectedItem().toString().substring(0,2)+"'";
			}
			
			if(rdbDT.isSelected())
				M_strSQLQRY+=" AND BL_BLPDT BETWEEN '"+frmDT+"' AND '"+toDT+"'";
			
			else if(rdbNO.isSelected())
				M_strSQLQRY+=" AND BL_DOCNO BETWEEN '"+txtFRMNO.getText().trim()+"' AND '"+txtTONO.getText().trim()+"'"; 
				
			
			M_strSQLQRY+=" order by BL_BILTP,BL_DOCNO desc";
			/*
			if(((String)cmbPRQTP.getSelectedItem()).substring(0, 2).trim().equals("03"))
			{
				System.out.println("***inside 03");
				M_strSQLQRY = "Select DISTINCT BL_BILTP,BL_DOCNO,BL_BLPDT,BL_PRTNM,BL_PMDDT,BIL_STRTP,'NIL' BIL_DOCRF,'NIL' BIL_PORRF,BL_BILNO,BL_BILDT,CMT_CHP01 CURFDS,";

				M_strSQLQRY+= "CMT_CODDS BL_CURDS,BL_BILAM,BL_EXGRT,'Transpotation Cost' PO_SHRDS FROM MM_BLMST,MM_BILTR,CO_CDTRN WHERE ";
				String var="";
				if((strFRMDT.trim().length()>0)&&(strTODT.trim().length()>0))
					var="BL_BLPDT BETWEEN '"+frmDT+"' AND '"+toDT+"' ";

				else if((strFRMNO.trim().length()>0)&&(strTONO.trim().length()>0))
					var="BL_DOCNO BETWEEN '"+strFRMNO+"' AND '"+strTONO+"'";

				M_strSQLQRY+= var;					
				M_strSQLQRY+= " AND BL_BILTP=BIL_BILTP AND BL_DOCNO=BIL_DOCNO ";
				M_strSQLQRY+= " AND CMT_CGSTP='COXXCUR' AND CMT_CODCD=BL_CURCD order by BL_BILTP,BL_DOCNO desc";
			}
			*/
			
				System.out.println("test qry: "+M_strSQLQRY);
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);          
				int P_intPADLN=100;
				if(M_rdbTEXT.isSelected())P_intPADLN=80;
				String strLFT="  ";
				int pgBRK=0;
				//prnFMTCHR(D_OUT,M_strNOCPI17);
				//prnFMTCHR(D_OUT,M_strCPI10);prnFMTCHR(D_OUT,M_strBOLD);
				if(M_rstRSSET!=null)				
				{
					while(M_rstRSSET.next())	
					{
						
						
						pgBRK++;
						System.out.println("1*******");
						strBILTP=M_rstRSSET.getString("BL_BILTP");
						System.out.println("***"+M_rstRSSET.getString("BL_BILTP"));
						
						dblBLAMT=Double.parseDouble(M_rstRSSET.getString("BL_CALAM"));

						dblEXGRT=Double.parseDouble(M_rstRSSET.getString("BL_EXGRT"));

						//testing---------
						//double dblEXGRT2=3.0;
						//System.out.println(" ==dblBLAMT===dblEXGRT==div"+dblBLAMT+"  "+dblEXGRT+"  "+nf.format(dblBLAMT/dblEXGRT2));
						//testing ends

						M_strTOTAL=M_rstRSSET.getString("BL_CURDS").trim().equalsIgnoreCase("Rupees")?""+this.setNumberFormat(dblBLAMT,2):""+this.setNumberFormat(dblBLAMT/dblEXGRT,2);//+Double.parseDouble(M_rstRSSET.getString("BL_BILAM")):""+setNumberFormat(Double.parseDouble(M_rstRSSET.getString("BL_BILAM"))/Double.parseDouble(M_rstRSSET.getString("BL_EXGRT")),2);
						//M_strTOTAL="";
						//System.out.print(M_rstRSSET.getString("CMT_CODDS"));			 
						System.out.println("********got rec");System.out.println(M_rstRSSET.getString("BL_DOCNO"));


						if(M_rdbTEXT.isSelected()||flaghtml==1)
						{
							if(M_rdbHTML.isSelected())
							{D_OUT.writeBytes(" "+strLFT+strLIN(P_intPADLN));D_OUT.writeBytes("\n");//crtNWLIN();
							D_OUT.writeBytes(strLFT+"|"+strBLKSPACE(P_intPADLN)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
							}
							//prnFMTCHR(D_OUT,M_strCPI10);prnFMTCHR(D_OUT,M_strBOLD);	
							if(M_rdbHTML.isSelected())
							{

								D_OUT.writeBytes("<font size=+2>");D_OUT.writeBytes(strLFT+"|");D_OUT.writeBytes(padSTRING('C',cl_dat.M_strCMPNM_pbst+"  MUMBAI",P_intPADLN-26));D_OUT.writeBytes("    |");D_OUT.writeBytes("</font>");D_OUT.writeBytes("\n");//crtNWLIN();
								//D_OUT.writeBytes(strLFT+"|"+padSTRING('C',"MUMBAI",P_intPADLN)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
								//D_OUT.writeBytes(strLFT+"|"+padSTRING('C',"PAYMENT REQUISITION",P_intPADLN)+" |");D_OUT.writeBytes("\n");//crtNWLIN();
								D_OUT.writeBytes("<font size=+2>");D_OUT.writeBytes(strLFT+"|");D_OUT.writeBytes(padSTRING('C',"PAYMENT REQUISITION",P_intPADLN-26));D_OUT.writeBytes("    |");D_OUT.writeBytes("</font>");D_OUT.writeBytes("\n");//crtNWLIN();

							}
							if(M_rdbTEXT.isSelected())
							{

								D_OUT.writeBytes(strLFT+" ");prnFMTCHR(D_OUT,M_strENH);D_OUT.writeBytes(padSTRING('C',cl_dat.M_strCMPNM_pbst+"  MUMBAI",(P_intPADLN/2)));prnFMTCHR(D_OUT,M_strNOENH);D_OUT.writeBytes("   ");D_OUT.writeBytes("\n");//crtNWLIN();

								//D_OUT.writeBytes(strLFT+"|"+padSTRING('C',"MUMBAI",P_intPADLN)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
								//D_OUT.writeBytes(strLFT+"|"+padSTRING('C',"PAYMENT REQUISITION",P_intPADLN)+" |");D_OUT.writeBytes("\n");//crtNWLIN();
								D_OUT.writeBytes(strLFT+" ");prnFMTCHR(D_OUT,M_strENH);D_OUT.writeBytes(padSTRING('C',"PAYMENT REQUISITION",(P_intPADLN/2)));prnFMTCHR(D_OUT,M_strNOENH);D_OUT.writeBytes("   ");D_OUT.writeBytes("\n");//crtNWLIN();
							}
							//prnFMTCHR(D_OUT,M_strNOCPI10);
							//prnFMTCHR(D_OUT,M_strNOENH);
							if(M_rdbHTML.isSelected()&&!M_rdbTEXT.isSelected())
								D_OUT.writeBytes(strLFT+"|"+strLIN(P_intPADLN)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
								if(M_rdbTEXT.isSelected()&&!M_rdbHTML.isSelected())
									D_OUT.writeBytes(strLFT+" "+strLIN(P_intPADLN)+" ");D_OUT.writeBytes("\n");//crtNWLIN();

									D_OUT.writeBytes(strLFT+"| "+padSTRING('R'," PR NO               : "+nvlSTRVL(M_rstRSSET.getString("BL_DOCNO"),""), (P_intPADLN-1+7)/2));
									if(M_rstRSSET.getDate("BL_BLPDT") == null)
									{
										D_OUT.writeBytes(padSTRING('R',"PR DATE   : "+nvlSTRVL(M_rstRSSET.getString("BL_BLPDT"),""),(P_intPADLN-3/2))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									}
									else
										D_OUT.writeBytes(padSTRING('R',"PR DATE   : "+nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_BLPDT")),""),(P_intPADLN-3/2))+"|");D_OUT.writeBytes("\n");//crtNWLIN();																													   
									//D_OUT.writeBytes(strLFT+"|"+strBLKSPACE(P_intPADLN)+"|");

									D_OUT.writeBytes(strLFT+"| "+padSTRING('R'," PAID TO             : "+nvlSTRVL(M_rstRSSET.getString("BL_PRTNM"),""), P_intPADLN-1)+"|");D_OUT.writeBytes("\n");//crtNWLIN();						
									//D_OUT.writeBytes(strLFT+"|"+strBLKSPACE(P_intPADLN)+"|");

									//D_OUT.writeBytes(strLFT+"| "+padSTRING('R'," DUE DATE            : "+nvlSTRVL(M_rstRSSET.getString("BL_PMDDT"),""), P_intPADLN-1)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									if(M_rstRSSET.getDate("BL_PMDDT") == null)
								    {   
										D_OUT.writeBytes(strLFT+"| "+padSTRING('R'," DUE DATE            : "+nvlSTRVL(M_rstRSSET.getString("BL_PMDDT"),""), (P_intPADLN-1+7)/2));
									}
									else
										D_OUT.writeBytes(strLFT+"| "+padSTRING('R'," DUE DATE            : "+nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_PMDDT")),""), (P_intPADLN-1+7)/2));

									if(strBILTP.equals("03") || strBILTP.equals("06")||strBILTP.equals("07"))
									{
									strGRNNO="NIL";
									
									}
								 else
									{
									 strGRNNO=hstBLMST.get(M_rstRSSET.getString("BL_DOCNO"));
									
									}
									D_OUT.writeBytes(padSTRING('R',"GRN No    : "+strGRNNO,((P_intPADLN-1-8)/2))+" |");D_OUT.writeBytes("\n");//crtNWLIN();

									//D_OUT.writeBytes(strLFT+"|"+strBLKSPACE(P_intPADLN)+"|");

									D_OUT.writeBytes(strLFT+"| "+padSTRING('R'," CHEQUE/DD PAYABLE AT: MUMBAI",(P_intPADLN-1+7)/2));
									
									//if(((String)cmbPRQTP.getSelectedItem()).substring(0, 2).trim().equals("01"))
									if(strBILTP.equals("03") || strBILTP.equals("07"))
										{
										strSTRTP="NIL";
										strPORRF="NIL";
										}
									else
										{
										strSTRTP=nvlSTRVL(M_rstRSSET.getString("BIL_STRTP"),"NIL");
										strPORRF=nvlSTRVL(M_rstRSSET.getString("BIL_PORRF"),"NIL");
										}
										
									//{
										//D_OUT.writeBytes(padSTRING('R',"  PO/WO  NO : "+nvlSTRVL(M_rstRSSET.getString("BIL_STRTP"),"")+"/"+nvlSTRVL(M_rstRSSET.getString("BIL_PORRF"),""), ((P_intPADLN)/2))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
										D_OUT.writeBytes(padSTRING('R',"  PO/WO  NO : "+strSTRTP+"/"+strPORRF, ((P_intPADLN-7)/2))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									//}	
								   //else if(((String)cmbPRQTP.getSelectedItem()).substring(0, 2).trim().equals("03"))
									//	{
									//   		D_OUT.writeBytes(padSTRING('R',"  PO/WO  NO : "+strBILTP.equalsIgnoreCase("03")?"NIL":"l", ((P_intPADLN)/2))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
										
									//	}
								 	
									
									
									//D_OUT.writeBytes(strLFT+"|"+strBLKSPACE(P_intPADLN)+"|");

									D_OUT.writeBytes(strLFT+"| "+padSTRING('R'," BILL NO             : "+nvlSTRVL(M_rstRSSET.getString("BL_BILNO"),"")+"  "+nvlSTRVL(M_rstRSSET.getString("BL_BILDT"),""), (P_intPADLN-1+7)/2));

									D_OUT.writeBytes(padSTRING('R',"CURRENCY  : "+nvlSTRVL(M_rstRSSET.getString("BL_CURDS"),""), ((P_intPADLN-1-8)/2))+" |");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strLIN((P_intPADLN/2)+9)+"_"+strLIN(P_intPADLN-((P_intPADLN/2)+10))+"|");D_OUT.writeBytes("\n");//crtNWLIN();

									D_OUT.writeBytes(strLFT+"|"+padSTRING('C',"ACCOUNT  HEAD",(P_intPADLN/2)+10)+"|"+padSTRING('C',"AMOUNT",P_intPADLN-((P_intPADLN/2)+10))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strLIN((P_intPADLN/2)+9)+"|"+strLIN(P_intPADLN-((P_intPADLN/2)+10))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"| "+padSTRING('R',"Sundry Creditors",(P_intPADLN/2)+8)+"|"+padSTRING('C',M_strTOTAL,P_intPADLN-((P_intPADLN/2)+8))+"");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strBLKSPACE((P_intPADLN/2)+9)+"|"+strBLKSPACE(P_intPADLN-((P_intPADLN/2)+10))+"|");D_OUT.writeBytes("\n");//crtNWLIN();

									
									if(strBILTP.equals("03"))
									{
										strPORDS="(Transportation Cost)";
									}
									else if(strBILTP.equals("07"))
									{									
									strPORDS="";
									}
								    else
									{
								    strPORDS=nvlSTRVL(M_rstRSSET.getString("PO_SHRDS"),"NIL");
									}									
									D_OUT.writeBytes(strLFT+"| "+padSTRING('R',strPORDS,(P_intPADLN/2)+8)+"|"+strBLKSPACE(P_intPADLN-((P_intPADLN/2)+10))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strBLKSPACE((P_intPADLN/2)+9)+"|"+strBLKSPACE(P_intPADLN-((P_intPADLN/2)+10))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strBLKSPACE((P_intPADLN/2)+9)+"|"+strBLKSPACE(P_intPADLN-((P_intPADLN/2)+10))+"|");D_OUT.writeBytes("\n");//crtNWLIN();

									D_OUT.writeBytes(strLFT+"|"+strBLKSPACE((P_intPADLN/2)+9)+"|"+strBLKSPACE(P_intPADLN-((P_intPADLN/2)+10))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strBLKSPACE((P_intPADLN/2)+9)+"|"+strBLKSPACE(P_intPADLN-((P_intPADLN/2)+10))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strBLKSPACE((P_intPADLN/2)+9)+"|"+strBLKSPACE(P_intPADLN-((P_intPADLN/2)+10))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strLIN((P_intPADLN/2)+9)+"|"+strLIN(P_intPADLN-((P_intPADLN/2)+10))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+padSTRING('L',"TOTAL | ",(P_intPADLN/2)+10)+" "+padSTRING('C',M_strTOTAL,P_intPADLN-((P_intPADLN/2)+10))+"");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strLIN((P_intPADLN/2)+9)+"|"+strLIN(P_intPADLN-((P_intPADLN/2)+11))+" |");D_OUT.writeBytes("\n");
									//D_OUT.writeBytes(strLFT+"|"+strBLKSPACE((P_intPADLN/2)+10)+" "+strBLKSPACE(P_intPADLN-((P_intPADLN/2)+11))+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"| "+padSTRING('R',M_rstRSSET.getString("BL_CURDS")+" "+getCURWRD(M_strTOTAL,M_rstRSSET.getString("CURFDS")), P_intPADLN-1)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strLIN(P_intPADLN)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"| "+padSTRING('R'," REMARK  :", P_intPADLN-1)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strBLKSPACE(P_intPADLN)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strLIN(P_intPADLN)+"|");D_OUT.writeBytes("\n");//crtNWLIN();

									D_OUT.writeBytes(strLFT+"|"+strBLKSPACE(P_intPADLN)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+strBLKSPACE(P_intPADLN)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+"|"+padSTRING('C',"PREPARED BY",P_intPADLN/4)+" "+padSTRING('C',"CHECKED BY",P_intPADLN/4)+" "+padSTRING('C',"APPROVED BY",P_intPADLN/4)+padSTRING('C',"ENTERED BY",P_intPADLN/4)+"|");D_OUT.writeBytes("\n");//crtNWLIN();
									D_OUT.writeBytes(strLFT+strLSTLIN(P_intPADLN));
									D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
									D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");


									//for marking to cut (2 block per page)
									/*if(!((pgBRK%2)==0))
						{
							D_OUT.writeBytes(strPGBRK(P_intPADLN+10));D_OUT.writeBytes("\n");
							D_OUT.writeBytes("\n");
						}


					}

					/*
					if(M_rdbTEXT.isSelected()||flaghtml==1)
					{
						crtNWLIN();
						D_OUT.writeBytes(" _____________________________________________________________________________________________________________________________________________ ");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();	
						prnFMTCHR(D_OUT,M_strCPI10);prnFMTCHR(D_OUT,M_strBOLD);
						D_OUT.writeBytes("|                                                         SUPREME PETROCHEM LTD                                                               |");crtNWLIN();			
						D_OUT.writeBytes("|                                                               MUMBAI                                                                        |");crtNWLIN();
						D_OUT.writeBytes("|                                                         PAYMENT REQUISITION                                                                 |");crtNWLIN();
						prnFMTCHR(D_OUT,M_strNOCPI10);
						D_OUT.writeBytes("|_____________________________________________________________________________________________________________________________________________|");crtNWLIN();
						prnFMTCHR(D_OUT,M_strCPI17);
						D_OUT.writeBytes("|  PR NO     :"+nvlSTRVL(M_rstRSSET.getString("BL_DOCNO"),"")+"   PR DATE :"+M_rstRSSET.getString("BL_BLPDT")+"                               |");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________________________________________________________________|");crtNWLIN();
						D_OUT.writeBytes("|  PAID TO   : "+nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"")+"                                                                              |");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________________________________________________________________|");crtNWLIN();
						D_OUT.writeBytes("|  DUE DATE  : "+nvlSTRVL(M_rstRSSET.getString("BL_PMDDT"),"")+"                                                                              |");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________________________________________________________________|");crtNWLIN();
						D_OUT.writeBytes("|  CHEQUE/DO PAYABLE AT :                                                        PO/WO  NO : "+M_rstRSSET.getString("BIL_STRTP")+"/"+M_rstRSSET.getString("BIL_PORRF")+"|");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________________________________________________________________|");crtNWLIN();
						D_OUT.writeBytes("|  BILL NO   : PIN "+M_rstRSSET.getString("BL_BILNO")+"  "+M_rstRSSET.getString("BL_BILDT")+"                                                                  CURRENCY  : "+M_rstRSSET.getString("BL_CURDS")+"                                     |");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________________________________________________________________|");crtNWLIN();
						D_OUT.writeBytes("|                                  ACCOUNT HEAD                                       |                  AMOUNT                               |");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________|_______________________________________________________|");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|               "+"Sundry Creditors"+"                                                              "+M_strTOTAL+"                            |");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________________________________________________________________|");crtNWLIN();
						D_OUT.writeBytes("|                                                                TOTAL  :             |         "+M_strTOTAL+"                                |");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________|_______________________________________________________|");crtNWLIN();
						D_OUT.writeBytes("|"+getCURWRD(M_strTOTAL,M_rstRSSET.getString("BL_CURDS"))+"                                                                                   |");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________________________________________________________________|");crtNWLIN();
						D_OUT.writeBytes("|    REM   :                                                                                                                                  |");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________________________________________________________________|");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________________________________________________________________|");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();				
						D_OUT.writeBytes("|                                                                                                                                             |");crtNWLIN();				
						D_OUT.writeBytes("|                PREPARED BY                   CHECKED BY                  APPROVED BY               ENTERED BY                               |");crtNWLIN();
						D_OUT.writeBytes("|_____________________________________________________________________________________________________________________________________________|");crtNWLIN();
						crtNWLIN();crtNWLIN();crtNWLIN();

					}
									 */
									if((pgBRK%2==0)&& M_rdbTEXT.isSelected())
									{prnFMTCHR(D_OUT,M_strEJT);pgBRK=0;
									//D_OUT.writeBytes("---break---");
									}
									else if((pgBRK%2==0)&& M_rdbHTML.isSelected())
									{D_OUT.writeBytes("<P CLASS = \"breakhere\">");pgBRK=0;
									//D_OUT.writeBytes("---break---");
									}

						}

					}
					M_rstRSSET.close();



					//for printing the inter office memo

					//eject
					//if(M_rdbHTML.isSelected())D_OUT.writeBytes("<P CLASS = \"breakhere\">");
					//else if(M_rdbTEXT.isSelected())prnFMTCHR(D_OUT,M_strEJT);

					prnFMTCHR(D_OUT,M_strNOCPI17);				
					D_OUT.writeBytes(strLFT+" ");prnFMTCHR(D_OUT,M_strENH);D_OUT.writeBytes(padSTRING('C',"INTER OFFFICE MEMO",(P_intPADLN-40)));
					prnFMTCHR(D_OUT,M_strNOENH);D_OUT.writeBytes("   ");D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");//crtNWLIN();
					D_OUT.writeBytes(strLFT+padSTRING('R'," FROM    : "+"D.P. SOMANI", (P_intPADLN-1)/2));
					D_OUT.writeBytes(padSTRING('L'," TO   : "+"MR.  A.R. DESAI",(P_intPADLN/2)));D_OUT.writeBytes("\n");//crtNWLIN();						
					D_OUT.writeBytes(strLFT+padSTRING('R'," DATE    : "+cl_dat.M_txtCLKDT_pbst.getText(), (P_intPADLN-1)/2));D_OUT.writeBytes("\n");

					D_OUT.writeBytes(strLFT+padSTRING('C'," THROUGH   : "+" MR. N. GOPAL", P_intPADLN-1));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");//crtNWLIN();						
					D_OUT.writeBytes(strLFT+padSTRING('R',"Pease find enclosed the following Bank Payment Requisitions. Kindly",(P_intPADLN-1)));D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFT+padSTRING('R',"acknowledge receipt of the same.",(P_intPADLN-1)));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFT+padSTRING('C',"SR.NO.",(P_intPADLN-70)));D_OUT.writeBytes(strLFT+padSTRING('C',"VENDOR NAME",(P_intPADLN-40)));D_OUT.writeBytes(strLFT+padSTRING('R',"AMOUNT(Rs.)",(P_intPADLN-60)));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");

					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY); 
					int L_CNT=0;
					while(M_rstRSSET.next())
					{
						L_CNT++;
						dblBLAMT=Double.parseDouble(M_rstRSSET.getString("BL_CALAM"));

						dblEXGRT=Double.parseDouble(M_rstRSSET.getString("BL_EXGRT"));

						M_strTOTAL=M_rstRSSET.getString("BL_CURDS").trim().equalsIgnoreCase("Rupees")?""+this.setNumberFormat(dblBLAMT,2):""+this.setNumberFormat(dblBLAMT/dblEXGRT,2);//+Double.parseDouble(M_rstRSSET.getString("BL_BILAM")):""+setNumberFormat(Double.parseDouble(M_rstRSSET.getString("BL_BILAM"))/Double.parseDouble(M_rstRSSET.getString("BL_EXGRT")),2);

						D_OUT.writeBytes(strLFT+padSTRING('C',L_CNT+"",(P_intPADLN-70)));
						D_OUT.writeBytes(strLFT+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("BL_PRTNM"),""),(P_intPADLN-40)));
						D_OUT.writeBytes(strLFT+padSTRING('R',M_strTOTAL,P_intPADLN-60)+"");D_OUT.writeBytes("\n");
					}

					D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFT+padSTRING('R'," REGARDS , ", (P_intPADLN-1)));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFT+padSTRING('R'," D.P.  SOMANI  ", (P_intPADLN-1)));D_OUT.writeBytes("\n");

					M_rstRSSET.close();  

					//for eject				
					if(M_rdbHTML.isSelected())D_OUT.writeBytes("<P CLASS = \"breakhere\">");
					if(M_rdbTEXT.isSelected())prnFMTCHR(D_OUT,M_strEJT);


				}
				else 
				{
					System.out.println("&&&&&&null obtain");
					//System.exit(1);
				}

				if(M_rdbHTML.isSelected())
				{
					D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
				}	
				///////////////////////////////////////////////////////////////	   
				crtNWLIN();crtNWLIN();
				genRPFTR();


				D_OUT.close();
				F_OUT.close();

				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			catch(Exception L_EX)
			{
				L_EX.printStackTrace();
				System.out.println("rep error");
				setMSG(L_EX,"Report");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		private void crtNWLIN() 
		{
			try
			{
				D_OUT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst++;
				if(M_rdbHTML.isSelected())
				{
					if(cl_dat.M_intLINNO_pbst >70)
					{
						genRPFTR();
						genRPHDR();
					}
				}	
				else if(cl_dat.M_intLINNO_pbst >60)
				{		
					genRPFTR();
					genRPHDR();			
				}			
			}
			catch(Exception e)
			{
				
				setMSG(e,"Chlid.crtNWLIN");
			}
		}

		void genRPHDR()
		{

			try
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{	
					prnFMTCHR(D_OUT,M_strNOCPI17);
					prnFMTCHR(D_OUT,M_strCPI10);
					//prnFMTCHR(D_OUT,M_strCPI17);				
					prnFMTCHR(D_OUT,M_strBOLD);
					prnFMTCHR(D_OUT,M_strNOENH);
				}	
				if(M_rdbHTML.isSelected())
				{
					flaghtml=1;
					D_OUT.writeBytes("<b>");
					D_OUT.writeBytes("<HTML><HEAD><Title>Attendance Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 14 pt \">");    
					D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				}	

				cl_dat.M_PAGENO +=1;

				//crtNWLIN();
				prnFMTCHR(D_OUT,M_strBOLD);
				//D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
				//D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
				//D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
				//crtNWLIN();

				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					prnFMTCHR(D_OUT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					D_OUT.writeBytes("</b>");		
			}catch(Exception L_IOE)
			{
				
				setMSG(L_IOE,"Report Header");
			}
		}

		void genRPFTR()
		{
			try
			{
				cl_dat.M_intLINNO_pbst = 0;	//new test
				D_OUT.writeBytes(padSTRING('L',"",65));
				crtNWLIN();
				//D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
				crtNWLIN();
				prnFMTCHR(D_OUT,M_strNOCPI17);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					prnFMTCHR(D_OUT,M_strEJT);			
				if(M_rdbHTML.isSelected())
					D_OUT.writeBytes("<P CLASS = \"breakhere\">");
			}
			catch(Exception L_IOE)
			{
				
				setMSG(L_IOE,"Report Footer");
			}
		}	
		boolean vldDATA()
		{
			try
			{	


				if(txtFRMDT.getText().trim().length()>0)
				{
					if(txtTODT.getText().trim().length()==0)
					{
						setMSG("TO Date can't be Blank",'E');
						txtTODT.requestFocus();
						return false;					
					}
					else if(M_fmtLCDAT.parse(txtTODT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFRMDT.getText().trim()))<0)
					{
						setMSG("TO Date can't be less than From date",'E');
						txtTODT.requestFocus();
						return false;	
					}
					else
					{
						strTODT=txtTODT.getText().trim();
						strFRMDT=txtFRMDT.getText().trim();
					}


				}			

				if(txtFRMNO.getText().trim().length()>0)
				{
					if(Integer.parseInt(txtTONO.getText().trim())<Integer.parseInt(txtFRMNO.getText().trim()))
					{
						setMSG("TO NO should be greater than FROM No",'E');
						txtTONO.requestFocus();
						return false;
					}
					else
					{
						strFRMNO=txtFRMNO.getText().trim();
						strTONO=txtTONO.getText().trim();
					}



				}




			}
			catch(Exception L_VLD)
			{
				
				setMSG(L_VLD,"vldDATA()");
			}
			return true;
		}

		void exePRINT()
		{
			try
			{	
				//System.out.println("report prog called");
				if(!vldDATA())
				{
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					return;
				}
				cl_dat.M_intLINNO_pbst=0;
				cl_dat.M_PAGENO = 0;

				if(M_rdbHTML.isSelected())
					strRPFNM = strRPLOC + "mm_rpprq.html";
				if(M_rdbTEXT.isSelected())
					strRPFNM = strRPLOC + "mm_rpprq.doc";

				genRPTFL();

				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if (M_rdbTEXT.isSelected())
					{
						doPRINT(strRPFNM);
						/*	Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
						 */	
					}	
					else 
					{    
						Runtime r = Runtime.getRuntime();
						Process p = null;					    
						p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
						setMSG("For Printing Select File Menu, then Print  ..",'N');
					}

				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
					Runtime r = Runtime.getRuntime();
					Process p = null;
					if(M_rdbHTML.isSelected())
						p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
					else
						p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 

				}
				/*else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Supplier Analysis Reort"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}*/

			}
			catch(Exception L_E)
			{
				
				setMSG(L_E,"exePRINT");
			}
		}

		void setENBL(boolean L_STAT)
		{
			super.setENBL(L_STAT);

		}



		class INPVF extends InputVerifier 
		{
			public boolean verify(JComponent input) 
			{
				try
				{

				}
				catch(Exception L_E)
				{
					
					setMSG(L_E,"INPVF");
				}
				return true;
			}
		}
	}



