import spl.*;
import javax.swing.*;
import javax.swing.event.*;
public class mr_tedot extends cl_pbase{
	JTextField  txtINDNO = new TxtLimit(9), /**@Indent Number*/
				txtBYRCD = new TxtLimit(5), /**@Buyer Code*/
				txtCNSCD = new TxtLimit(5), /**@Consignee Code*/
				txtTRPCD = new TxtLimit(5), /**@Transporter Code*/
				txtSALTP = new TxtLimit(1), /**@Sale Type*/
				txtSALDS = new JTextField(), /**@Sale Type Description*/
				txtINV = new JTextField(), /**@*/
				txtACC = new JTextField(), /**@*/
				txtSTXRT = new TxtNumLimit(6.2), /**@Sales Tax Rate*/
				txtSTXCD = new TxtLimit(2), /**@Sales Tax Code*/
				txtMSTS = new JTextField(), /**@*/
				txtDORNO = new TxtNumLimit(8), /**@Delivery Order No.*/
				txtDORDT = new TxtDate(), /**@Delivery Order Date*/
				txtPMTCD = new TxtLimit(2), /**@Payment Term*/
				txtDSRCD = new TxtLimit(5), /**@Distributor Code*/
				txtDSRDS = new JTextField(), /**@Distributor Description*/
				txtBYRDS = new JTextField(), /**@Buyer Description*/
				txtDTPCD = new TxtLimit(2), /**@Delivery Type Code*/
				txtDTPDS = new JTextField(), /**@Delivery Type Description*/
				txtDLCCD = new TxtLimit(2), /**@Despatch from location*/
				txtDLCDS = new JTextField(), /**@Despatch location*/
				txtTMOCD = new TxtLimit(2), /**@Mode of Transport*/
				txtTMODS = new JTextField(), /**@Transport Description*/
				txtLRYNO = new TxtLimit(15), /**@Lorry No*/
				txtFRTRT = new TxtNumLimit(10.3), /**@Freight Rate*/
				txtCNSDS = new JTextField(), /**@Consignee Description*/
				txtTRPDS = new JTextField(), /**@Transporter Description*/
				txtSPLIN = new TxtLimit(50), /**@Special Instruction*/
				txtPRDGR = new TxtLimit(2), /**@Product Group*/
				txtPRDDS = new TxtLimit(15), /**@Product Description*/
				txtPKGTP = new TxtLimit(2), /**@Package Type*/
				txtPKGWT = new TxtNumLimit(7.3), /**@Package Weight*/
				txtBASRT = new TxtNumLimit(10.2), /**@Basic Rate*/
				txtLOTRF = new TxtLimit(40), /**@Lot Reference*/
				txtEXCRT = new TxtNumLimit(5.2), /**@Excise Rate*/
				txtDELTP = new TxtLimit(1), /**@Delivery Type*/
				txtDSPTP = new TxtLimit(1), /**@Despatch Type*/
				txtDELDS = new JTextField(), /**@Delivery Description*/
				txtSRLNO = new TxtLimit(2), /**@Serial Number*/
				txtDORQT = new TxtNumLimit(10.3), /**@Delivery Order Qty.*/
				txtDSPDT = new TxtDate(), /**@Despatch Date*/
				txtTRSTS = new JTextField(); /**@Third Party Name*/
	JTable tblPRDDS; /**Grade Entry JTable*/
	JScrollPane srpPRDDS; /**Scroll Panel for JTable tblPRDDS*/
	  mr_tedot(){
		//LM_VSTRT = 10;  
		setMatrix(22,6);
		
		add(new JLabel("Indent Number :"),1,1,1,1,this,'L');
		add(txtINDNO,1,2,1,1,this,'L');
		add(new JLabel("D.O. Number"),1,3,1,1,this,'L');
		add(txtDORNO,1,4,1,1,this,'L');
		add(new JLabel("D.O. Date"),1,5,1,1,this,'L');
		add(txtDORDT,1,6,1,1,this,'L');
		add(new JLabel("Sale Type"),2,1,1,1,this,'L');
		add(new JLabel("Description"),2,2,1,1,this,'L');
		add(new JLabel("Payment Terms"),2,3,1,1,this,'L');
		add(new JLabel("Sales Tax Code"),2,4,1,1,this,'L');
		add(new JLabel("Sales Tax Rate"),2,5,1,1,this,'L');
		add(txtSALTP,3,1,1,1,this,'L');
		add(txtSALDS,3,2,1,1,this,'L');
		add(txtINV,3,3,1,0.50,this,'L');
		add(txtACC,3,3,1,0.50,this,'R');
		add(txtSTXCD,3,4,1,1,this,'L');
		add(txtSTXRT,3,5,1,1,this,'L');
		add(new JLabel("Buyer Code/Description       :"),4,1,1,2,this,'L');
		add(txtBYRCD,4,3,1,1,this,'L');
		add(txtBYRDS,4,4,1,3,this,'L');
		add(new JLabel("Distributor Code/Description :"),5,1,1,2,this,'L');
		add(txtDSRCD,5,3,1,1,this,'L');
		add(txtDSRDS,5,4,1,3,this,'L');
		add(new JLabel("Consignee Code/Description   :"),6,1,1,2,this,'L');
		add(txtCNSCD,6,3,1,1,this,'L');
		add(txtCNSDS,6,4,1,3,this,'L');
		add(new JLabel("Transporter Code/Description :"),7,1,1,2,this,'L');
		add(txtTRPCD,7,3,1,1,this,'L');
		add(txtTRPDS,7,4,1,3,this,'L');
		add(new JLabel("Despatch From"),8,1,1,2,this,'L');
		add(new JLabel("Transport Mode"),8,2,1,1,this,'L');
		add(new JLabel("Lorry No."),8,3,1,1,this,'L');
		add(new JLabel("Delivery Type"),8,4,1,1,this,'L');
		add(new JLabel("Freight Rs./MT"),8,5,1,1,this,'L');
		add(txtDLCCD,9,1,1,0.25,this,'L');
		add(txtDLCDS,9,1,1,0.75,this,'R');
		add(txtTMOCD,9,2,1,0.25,this,'L');
		add(txtTMODS,9,2,1,0.75,this,'R');
		add(txtLRYNO,9,3,1,1,this,'L');
		add(txtDELTP,9,4,1,1,this,'L');
		add(txtFRTRT,9,5,1,1,this,'L');
		add(new JLabel("Special Instructions :"),10,1,1,1,this,'L');
		add(txtSPLIN,10,2,1,5,this,'L');
		cl_dat.M_CHKTBL = false;
		String[] L_TBLHDR=new String[]{"Product Type","Grade","D.O. Qty.","Available Qty.","Pkg. Type","Packages","Pkg. Weight","Basic Rate","Lot Reference","Delivery Schedule"};
		int[] L_COLSZ=new int[]{75,100,75,75,75,75,75,75,100,75};
		int h1=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
		int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
		tblPRDDS=crtTBLPNL(new JPanel(null),L_TBLHDR,10,0,25,LM_COLWID*2,320,L_COLSZ,0) ;
		tblPRDDS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		srpPRDDS=new JScrollPane(tblPRDDS,v1,h1);
		add(srpPRDDS,11,1,5,10,this,'L');
		add(new JLabel("Excise Duty"),16,1,1,1,this,'L');
		add(new JLabel("Serial No."),16,2,1,1,this,'L');
		add(new JLabel("Quantity"),16,3,1,1,this,'L');
		add(new JLabel("Date"),16,4,1,1,this,'L');
		add(new JLabel("Tr.Status"),16,5,1,1,this,'L');
		add(txtEXCRT,17,1,1,1,this,'L');
		add(txtSRLNO,17,2,1,1,this,'L');
		add(txtDORQT,17,3,1,1,this,'L');
		add(txtDSPDT,17,4,1,1,this,'L');
		add(txtTRSTS,17,5,1,1,this,'L');
		
	  }
}
