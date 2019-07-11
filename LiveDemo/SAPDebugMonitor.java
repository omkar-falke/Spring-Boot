
//-Begin----------------------------------------------------------------
//
// @(#)SAPDebugMonitor.java
//
// SAPDebugMonitor application
//
// @author Stefan Schnell
// @version 0.01 2009/8/31
//
//----------------------------------------------------------------------
/*
    This Class is used for RFC connectivity to SAP through java.
    
	put sapjco3.dll,librfc32.dll,sapjcorfc.dll in to the folder windows\system32
	set path to the jdk bin and lib folder.
	set classpath to SAPDebugMonitor.jar and sapjco3.jar
	
    create class object and then use method as follows.
    SAPDebugMonitor obj = new SAPDebugMonitor(strASHost,strSysNo,strClient,strUser,strLanguage,strPassword);	
	obj.getDATA(table name,vector of column names)
	
	e.g.
	
	////calling MARA
		strQUERY_TABLE="MARA";
		vtrCOLDATA.clear();
		vtrCOLDATA.add("MATNR");
		vtrCOLDATA.add("ERNAM");
		vtrDBDATA.clear();
		vtrDBDATA = frame.getDATA(strQUERY_TABLE,vtrCOLDATA);
		for(int i=0;i<vtrDBDATA.size();i++)
		{
			String[] staDT_MARA = vtrDBDATA.get(i).split("~");
			for(int j=0;j<staDT_MARA.length;j++)	
				System.out.println(j+" : "+staDT_MARA[j]);
		}
		//////////////////////////////
		
		
		
	for windows vista and server 2003 install "vcredist_x64" windows utility
	for any query reffer
	http://www.jabaco.org/board/185-how-to-connect-sap-with-jabaco-via-jco-java-connector.html
	http://www.stschnell.de/debmon.htm
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;
import java.util.Vector;
public class SAPDebugMonitor extends JFrame{

  //-Class constants----------------------------------------------------
    private static final int WinSizeX = 600;

  //-Class variables----------------------------------------------------
    private myDestinationDataProvider myDestDataProv;
	String strASHost="",strSysNo="",strClient="",strUser="",strLanguage="",strPassword="";
	String strQUERY_TABLE="";
	//String[] strFIELDNAME;
	//String staDBDATA[] = new String[200];  
	Vector<String> vtrFIELDNAME = new Vector<String>();          
	Vector<String> vtrDBDATA = new Vector<String>();          
  //-myDestinationDataProvider------------------------------------------
  //
  // Standard from a SAP example
  //
  //--------------------------------------------------------------------
    static class myDestinationDataProvider
      implements DestinationDataProvider {

      private DestinationDataEventListener eL;
      private Properties ABAP_AS_Prop;

      public Properties getDestinationProperties(String destinationName)
        {
          if(destinationName.equals("ABAP_AS") && ABAP_AS_Prop!=null)
            return ABAP_AS_Prop;
          return null;
        }

        public void setDestinationDataEventListener(
          DestinationDataEventListener eventListener)
          {
            this.eL = eventListener;
          }

        public boolean supportsEvents()
          {
            return true;
          }

        void changePropertiesForABAP_AS(Properties properties)
          {
            if(properties==null)
              {
                eL.deleted("ABAP_AS");
                ABAP_AS_Prop = null;
              }
            else
              {
                if (ABAP_AS_Prop!=null  &&
                  !ABAP_AS_Prop.equals(properties))
                  eL.updated("ABAP_AS");
                ABAP_AS_Prop = properties;
              }
          }
    }



  //-getDATA----------------------------------------------
      public Vector<String> getDATA(String LP_QUERY_TABLE,Vector<String> LP_FIELDNAME) 
      {
        JCoTable fields;
        try
        {
            JCoDestination dest = CallSAP();
			strQUERY_TABLE = LP_QUERY_TABLE;      
      		//strFIELDNAME = LP_FIELDNAME;
            vtrFIELDNAME = LP_FIELDNAME;
            JCoRepository repos = dest.getRepository();
            JCoFunction func = repos.getFunction("RFC_READ_TABLE");
            func.getImportParameterList().setValue("DELIMITER", "~");
            JCoContext.begin(dest);
        
            fields=func.getTableParameterList().getTable("FIELDS");
            for(int i=0;i<vtrFIELDNAME.size();i++)
	    	{
				fields.appendRow();
            	fields.setValue("FIELDNAME", vtrFIELDNAME.get(i));
		  	}
          	func.getImportParameterList().setValue("QUERY_TABLE",strQUERY_TABLE);
          	func.execute(dest);
            outGrid(func);
            JCoContext.end(dest);
            Environment.unregisterDestinationDataProvider(myDestDataProv);
		    return vtrDBDATA;
          }
          catch (Exception ex)
          {
			ex.printStackTrace();
          }
            return vtrDBDATA;
        }
    
    
  //-outGrid------------------------------------------------------------
  public void outGrid(JCoFunction JFunc) 
  {
      JCoTable JFields;
      JCoTable JTable;
      int fcnt, rcnt;
      int i;
      JFields = JFunc.getTableParameterList().getTable("FIELDS");
      //-Get table content----------------------------------------------
      JTable = JFunc.getTableParameterList().getTable("DATA");
      rcnt = JTable.getNumRows();
      vtrDBDATA.clear();
      //-Set table in the grid------------------------------------------
	  for (i = 0; i <= rcnt - 1; i++) 
	  {
	      JTable.setRow(i);
	      String felder[] = JTable.getString("WA").split("~");
	      vtrDBDATA.add(JTable.getString("WA"));
      }
      JTable.clear();
      JFields.clear();
  }


  //-CallSAP------------------------------------------------------------
    public JCoDestination CallSAP() throws Exception {
        Properties propConn = new Properties();

        propConn.setProperty(DestinationDataProvider.JCO_ASHOST,
         strASHost);
        propConn.setProperty(DestinationDataProvider.JCO_SYSNR,
         strSysNo);
        propConn.setProperty(DestinationDataProvider.JCO_CLIENT,
         strClient);
	    propConn.setProperty(DestinationDataProvider.JCO_USER,
         strUser);
        propConn.setProperty(DestinationDataProvider.JCO_PASSWD,
         strPassword);
        propConn.setProperty(DestinationDataProvider.JCO_LANG,
         strLanguage);

        myDestDataProv = new myDestinationDataProvider();
        Environment.registerDestinationDataProvider(myDestDataProv);
        myDestDataProv.changePropertiesForABAP_AS(propConn);

        return JCoDestinationManager.getDestination("ABAP_AS");
      }

  //-SAPDebugMonitor----------------------------------------------------
    public SAPDebugMonitor(String LP_strASHost,String LP_strSysNo,String LP_strClient,String LP_strUser,String LP_strLanguage,String LP_strPassword) 
    {
      strASHost = LP_strASHost;
      strSysNo = LP_strSysNo;
      strClient = LP_strClient;
      strUser = LP_strUser;
      strLanguage = LP_strLanguage;
      strPassword = LP_strPassword;
    }
}

//-End------------------------------------------------------------------
