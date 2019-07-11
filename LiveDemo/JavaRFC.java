import java.util.Vector;

public class JavaRFC
{
	public static void main(String args[])
	{
		String strASHost="192.168.10.134";
		String strSysNo="03";
		String strClient="250";
		String strUser="ajain";
		String strLanguage="EN";
		String strPassword="aawajjain";
		String strQUERY_TABLE = "";
		SAPDebugMonitor frame = new SAPDebugMonitor(strASHost,strSysNo,strClient,strUser,strLanguage,strPassword);	
		Vector<String> vtrDBDATA = new Vector<String>();
		Vector<String> vtrCOLDATA = new Vector<String>();

		////calling ZSPL_MM_WB
		strQUERY_TABLE="ZSPL_MM_WB";
		vtrCOLDATA.clear();
		vtrCOLDATA.add("MANDT");
		vtrCOLDATA.add("ZWERKS");
		vtrCOLDATA.add("ZWEIGHT");
		vtrCOLDATA.add("ZUNIT");
		vtrCOLDATA.add("ZWBNO");
		vtrDBDATA.clear();
		vtrDBDATA = frame.getDATA(strQUERY_TABLE,vtrCOLDATA);
		for(int i=0;i<vtrDBDATA.size();i++)
		{
			String[] staDT_ZSPL_MM_WB = vtrDBDATA.get(i).split("~");
			for(int j=0;j<staDT_ZSPL_MM_WB.length;j++)	
				System.out.println(j+" : "+staDT_ZSPL_MM_WB[j]);
		}
		//////////////////////////////

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
	}	
}