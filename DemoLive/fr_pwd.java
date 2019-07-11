import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class fr_pwd extends JInternalFrame implements ActionListener,FocusListener,KeyListener{
	JPanel pnlSETP;
	Font f = new Font("Times New Roman",Font.BOLD,13);
	Font f1 = new Font("Times New Roman",Font.BOLD,11);
	JButton btnSETP,btnEXT;
	JPasswordField txtOPWD,txtNPWD,txtCONF;
	String L_ACTCMD,LM_OPWD,LM_NPWD,LM_CONF;
	char[] LM_OPWDCH,LM_NPWDCH,LM_CONFCH;
	
	fr_pwd(){
		getContentPane().setLayout(new BorderLayout());
		pnlSETP = new JPanel();
		pnlSETP.setLayout(null);
		getContentPane().add(pnlSETP,BorderLayout.CENTER);
		setLOUT();	
		setSTAINT();
		setSize(700,500);
	}
	
	public void setLOUT(){
		crtLBL("Change Password ",337,105,new Font("Arial",Font.BOLD,20));
		Image imgSPL = this.getToolkit().createImage("C:\\splerp\\spllogo.gif");
		ImageIcon imgico=new ImageIcon(imgSPL);
		JLabel lblIMGIC = new JLabel(imgico);
		lblIMGIC.setBounds(365,132,100,100);
        pnlSETP.add(lblIMGIC);
		crtLBL("Old Password ",320,235,f1);
		crtLBL("New Password ",320,265,f1);
		crtLBL("ReConfirm    ",320,295,f1);
		txtOPWD = crtPASS(405,235,120,20);
		txtOPWD.setActionCommand("txtOPWD");
		txtNPWD = crtPASS(405,265,120,20);
		txtNPWD.setActionCommand("txtNPWD");
		txtCONF = crtPASS(405,295,120,20);
		txtCONF.setActionCommand("txtCONF");
		txtOPWD.setEchoChar('*');
		txtNPWD.setEchoChar('*');
		txtCONF.setEchoChar('*');
		btnSETP = crtBTN("Set",320,320,85,25);
		btnEXT = crtBTN("Exit",440,320,85,25);
    }
	
	public void actionPerformed(ActionEvent L_AE){
		if(L_AE.getSource().equals(btnEXT))
		{	if(chkPASS())
			{
				cl_dat.M_txtCURDT_pbst.requestFocus();
				dispose();
				cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"login");
			}
		}
		else if(L_AE.getSource().equals(btnSETP))
		{
			if(chkPASS())
				exeSETPASS();			   
		}
		
		L_ACTCMD = L_AE.getActionCommand();
		if(L_ACTCMD.equals("txtOPWD")){
			LM_OPWDCH = txtOPWD.getPassword();
			LM_OPWD = "";
			for(int i=0;i<LM_OPWDCH.length;i++){
				LM_OPWD += LM_OPWDCH[i];
			}
			
			LM_OPWD = LM_OPWD.trim();
			if(LM_OPWD.length()>0){
				if(cl_ProcessPassword.encode(LM_OPWD.toUpperCase()).equals(cl_dat.M_strUSRPW_pbst.toUpperCase())){
					txtNPWD.setEnabled(true);
					txtOPWD.setEnabled(false);	
					txtNPWD.requestFocus();
					if(LM_OPWD.equalsIgnoreCase(cl_dat.M_strUSRCD_pbst))
					   btnEXT.setEnabled(false);
				}
				else
					// wrong password message
					;
			}	
		}
		
		else if(L_ACTCMD.equals("txtNPWD")){
			LM_NPWDCH = txtNPWD.getPassword();
			LM_NPWD = "";
			for(int i=0;i<LM_NPWDCH.length;i++){
				LM_NPWD += LM_NPWDCH[i];
			}
			LM_NPWD = LM_NPWD.trim();	
			if(LM_NPWD.length()>0){
				txtCONF.setEnabled(true);
				txtNPWD.setEnabled(false);
				txtCONF.requestFocus();
			}	
		}
		
		else if(L_ACTCMD.equals("txtCONF")){
			LM_CONFCH = txtCONF.getPassword();
			LM_CONF = "";
			for(int i=0;i<LM_CONFCH.length;i++){
				LM_CONF += LM_CONFCH[i];
			}
			LM_CONF = LM_CONF.trim();	
			if(LM_CONF.length()>0){
				if(LM_CONF.equalsIgnoreCase(LM_NPWD)){
					txtCONF.setEnabled(false);
					btnSETP.setEnabled(true);
				}
				else{
					txtCONF.requestFocus();
					txtCONF.setText("");
				}
			}	
		}
	}
	
	public JButton crtBTN(String btnText,int x,int y,int nwidth,int nheight){
		JButton btn = new JButton(btnText); 
		btn.setSize(nwidth,nheight);
		btn.setLocation(x,y);
        pnlSETP.add(btn);
		btn.addActionListener(this);
		btn.addFocusListener(this);
		btn.addKeyListener(this);
		return btn; 
	}
	
	public JPasswordField crtPASS(int x,int y,int nwidth,int nHeight){
    	JPasswordField pass = new JPasswordField();
		pass.setLocation(x,y);
		pass.addActionListener(this);
		pass.setSize(nwidth,nHeight);
        pnlSETP.add(pass); 
		return pass; 
	}
	
    public JTextField crtTXT(int x,int y,int nwidth,int nHeight){
		JTextField txt = new JTextField();
		txt.setLocation(x,y);
		txt.setSize(nwidth,nHeight);
        pnlSETP.add(txt);
		txt.addActionListener(this);
		txt.addFocusListener(this);
		txt.addKeyListener(this);
		return txt; 
	}	
	
    public void crtLBL(String strText,int x,int y,Font f){
		JLabel lbl = new JLabel(strText);
		if(strText.equalsIgnoreCase("change password "))
		{
			lbl.setForeground(Color.blue);
			lbl.setSize(200,30);
			lbl.setFont(new Font("Arial",Font.BOLD,20));
		}
		else
			lbl.setSize((int) strText.length()*strText.length()/2+15,15);
		lbl.setLocation(x,y);
        pnlSETP.add(lbl);
	}
	
	private void setSTAINT(){
		txtNPWD.setEnabled(false);
		txtCONF.setEnabled(false);
		btnSETP.setEnabled(false);
	}
	
	public void keyPressed(KeyEvent L_KE){
		if (L_KE.getKeyCode()== L_KE.VK_ENTER){
		}
	}
	
	public void keyTyped(KeyEvent L_KE){
	}
	
	public void keyReleased(KeyEvent L_KE){
	}
	private boolean chkPASS()
	{
		boolean flag=true;
		if(txtNPWD.getPassword().length==0)
		{
			if(cl_ProcessPassword.encode(cl_dat.M_strUSRCD_pbst).equalsIgnoreCase(cl_dat.M_strUSRPW_pbst))
			   flag= false;
		}
		if(new String(txtNPWD.getPassword()).equalsIgnoreCase(cl_dat.M_strUSRCD_pbst))
		{
			flag= false;
		}
		if(!flag)
		{
			JOptionPane.showInternalMessageDialog(getContentPane(),"Password cannot be same as user name","",JOptionPane.ERROR_MESSAGE);
			txtNPWD.setEnabled(true);txtNPWD.setText("");
			txtNPWD.requestFocus();
			return flag;
			
		}
		return flag;
	}
	private void exeSETPASS(){
		try{
		if(LM_NPWD.equals(LM_CONF)){	
			try{
				cl_dat.M_flgLCUPD_pbst = true;
				String LM_STRSQL = "UPDATE SA_USMST SET US_USRPW = ";
				LM_STRSQL += "'" + cl_ProcessPassword.encode(LM_NPWD.toUpperCase()) + "'" ;
				LM_STRSQL += " where US_USRCD =" + "'" + cl_dat.M_strUSRCD_pbst +"'";
                cl_dat.exeSQLUPD(LM_STRSQL ,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst){
					cl_dat.M_conSPDBA_pbst.commit();
					cl_dat.M_strUSRPW_pbst =cl_ProcessPassword.encode(LM_NPWD.toUpperCase());
				}else{
					cl_dat.M_conSPDBA_pbst.rollback();
				}
		    }catch(Exception L_SE){
				System.out.println("Error " + L_SE.toString());
			}
			}
		else{
				//error message
		}
		cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"login");
		}catch(Exception e)
		{System.out.println("Error in setPASS");}
	}
	
	public void focusLost(FocusEvent L_FE){
	}
	
	public void focusGained(FocusEvent L_FE){
	}
}
