import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.text.SimpleDateFormat;

public class cl_eml {

    String strFRUSR="";  
    protected SimpleDateFormat fmtLCDAT=new SimpleDateFormat("dd/MM/yyyy");
    // Method overloaded on 17/06/2006 for sending internet messages on  "192.168.10.195" 
public void sendfile(String LP_MSGTP,String LP_TOADDR,String LP_FILNM,String LP_SUBJECT,String LP_BDTEXT) 
    {
	if((!cl_dat.M_flgTSTDT_pbst)||(LP_TOADDR.equalsIgnoreCase("systems@spl.co.in")))
	{
		String L_FRADDR = cl_dat.M_strSYSEML_pbst;
        String L_TOHOST = "192.168.10.195";
	//	String L_TOHOST = "11.0.0.2";
	    String L_MSGTXT = LP_BDTEXT ;                   
		Properties props = System.getProperties();
		props.put("mail.smtp.host", L_TOHOST);
		Session L_SESSION = Session.getDefaultInstance(props);
		
		try 
        {
	        //	L_SESSION.setDebug(true);
		    MimeMessage L_REPMSG = new MimeMessage(L_SESSION);
			L_REPMSG.setFrom(new InternetAddress(L_FRADDR));
			InternetAddress[] address = {new InternetAddress(LP_TOADDR)};
            L_REPMSG.setRecipients(Message.RecipientType.TO, address);
           // L_REPMSG.setRecipients(Message.RecipientType.CC, address);
            L_REPMSG.setSubject(LP_SUBJECT);
            MimeBodyPart L_MSGBDT = new MimeBodyPart();
			int L_intIDX =-1;   
			if(LP_FILNM!=null)
			{
				if((L_intIDX = LP_FILNM.indexOf(".")) > 0)
				{
				   // if((LP_FILNM.substring(L_intIDX).equals("html"))||(LP_FILNM.substring(L_intIDX).equals("htm")))
				   // L_MSGTXT   = "  ";  
				}
				else
				    L_MSGTXT += ".\n"+ "For Printing Please detach the file and give the command From MS DOS Prompt AS :"+"\n" + "copy "+ LP_FILNM.substring(11) + " prn" + "\n\n";
			}
		//	System.out.println(L_MSGTXT);
			L_MSGBDT.setText(L_MSGTXT);
			MimeBodyPart L_MSGBDA = new MimeBodyPart();
			FileDataSource fds=null;
			if(LP_FILNM!=null)
			{
				fds = new FileDataSource(LP_FILNM);
				L_MSGBDA.setDataHandler(new DataHandler(fds));
				L_MSGBDA.setFileName(fds.getName());
			}
            Multipart mp = new MimeMultipart();
            if(LP_FILNM!=null)
				mp.addBodyPart(L_MSGBDA);
			mp.addBodyPart(L_MSGBDT);
            L_REPMSG.setContent(mp);
            L_REPMSG.setSentDate(fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));
            Transport.send(L_REPMSG);
		}
         catch (MessagingException mex) 
        {
             System.out.println("Messageing Exception "+mex.getMessage());
			 System.out.println("Messageing Exception "+mex.toString());
			//mex.printStackTrace();
		    Exception ex = null;
		   /* if ((ex = mex.getNextException()) != null) {
			ex.printStackTrace();
		    }*/
		}
		 catch (NullPointerException mex) {
		    System.out.println("Null Pointer Exception ");
		    }
		 catch (Exception mex) {
		    System.out.println("Exception "+mex.toString());
		    }
		
	    }
    }

public void sendfile(String LP_TOADDR,String LP_FILNM,String LP_SUBJECT,String LP_BDTEXT) 
    {
	
	//  System.out.println(cl_dat.M_flgTSTDT_pbst);
	if((!cl_dat.M_flgTSTDT_pbst)||(LP_TOADDR.equalsIgnoreCase("systems@spl.co.in")))
	{
		String L_FRADDR = cl_dat.M_strSYSEML_pbst;
        String L_TOHOST = "192.168.10.131";
	//	String L_TOHOST = "11.0.0.2";
	    String L_MSGTXT = LP_BDTEXT ;                   
		Properties props = System.getProperties();
		props.put("mail.smtp.host", L_TOHOST);
		Session L_SESSION = Session.getDefaultInstance(props);
		
		try 
        {
	        //	L_SESSION.setDebug(true);
		    MimeMessage L_REPMSG = new MimeMessage(L_SESSION);
			L_REPMSG.setFrom(new InternetAddress(L_FRADDR));
			InternetAddress[] address = {new InternetAddress(LP_TOADDR)};
            L_REPMSG.setRecipients(Message.RecipientType.TO, address);
           // L_REPMSG.setRecipients(Message.RecipientType.CC, address);
            L_REPMSG.setSubject(LP_SUBJECT);
            MimeBodyPart L_MSGBDT = new MimeBodyPart();
			int L_intIDX =-1;   
			if(LP_FILNM!=null)
			{
				if((L_intIDX = LP_FILNM.indexOf(".")) > 0)
				{
				    //if((LP_FILNM.substring(L_intIDX).equals("html"))||(LP_FILNM.substring(L_intIDX).equals("htm")))
				    //  L_MSGTXT   = "  ";  
				}
				else
				    L_MSGTXT += ".\n"+ "For Printing Please detach the file and give the command From MS DOS Prompt AS :"+"\n" + "copy "+ LP_FILNM.substring(11) + " prn" + "\n\n";
			}
		//	System.out.println(L_MSGTXT);
			L_MSGBDT.setText(L_MSGTXT);
			MimeBodyPart L_MSGBDA = new MimeBodyPart();
			FileDataSource fds=null;
			if(LP_FILNM!=null)
			{
				fds = new FileDataSource(LP_FILNM);
				L_MSGBDA.setDataHandler(new DataHandler(fds));
				L_MSGBDA.setFileName(fds.getName());
			}
            Multipart mp = new MimeMultipart();
            if(LP_FILNM!=null)
				mp.addBodyPart(L_MSGBDA);
			mp.addBodyPart(L_MSGBDT);
            L_REPMSG.setContent(mp);
            L_REPMSG.setSentDate(fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));
            Transport.send(L_REPMSG);
		}
         catch (MessagingException mex) 
        {
             System.out.println("Messageing Exception "+mex.getMessage());
			 System.out.println("Messageing Exception "+mex.toString());
			//mex.printStackTrace();
		    Exception ex = null;
		   /* if ((ex = mex.getNextException()) != null) {
			ex.printStackTrace();
		    }*/
		}
		 catch (NullPointerException mex) {
		    System.out.println("Null Pointer Exception ");
		    }
		 catch (Exception mex) {
		    System.out.println("Exception "+mex.toString());
		    }
		
	    }
    }

//Method without cl_dat variable (Running Externally)
public void sendfile_ext(String LP_TOADDR,String LP_FILNM,String LP_SUBJECT,String LP_BDTEXT) 
    {
	
	//  System.out.println(cl_dat.M_flgTSTDT_pbst);
	
		String L_FRADDR = "systems_works@spl.co.in";
        String L_TOHOST = "192.168.10.131";
	//	String L_TOHOST = "11.0.0.2";
	    String L_MSGTXT = LP_BDTEXT ;                   
		Properties props = System.getProperties();
		props.put("mail.smtp.host", L_TOHOST);
		Session L_SESSION = Session.getDefaultInstance(props);
		
		try 
        {
	        //	L_SESSION.setDebug(true);
		    MimeMessage L_REPMSG = new MimeMessage(L_SESSION);
			L_REPMSG.setFrom(new InternetAddress(L_FRADDR));
			InternetAddress[] address = {new InternetAddress(LP_TOADDR)};
            L_REPMSG.setRecipients(Message.RecipientType.TO, address);
           // L_REPMSG.setRecipients(Message.RecipientType.CC, address);
            L_REPMSG.setSubject(LP_SUBJECT);
            MimeBodyPart L_MSGBDT = new MimeBodyPart();
			int L_intIDX =-1;   
		//	System.out.println(L_MSGTXT);
			L_MSGBDT.setText(L_MSGTXT);
			MimeBodyPart L_MSGBDA = new MimeBodyPart();
			FileDataSource fds=null;
            Multipart mp = new MimeMultipart();
			mp.addBodyPart(L_MSGBDT);
            L_REPMSG.setContent(mp);
            L_REPMSG.setSentDate(fmtLCDAT.parse(fmtLCDAT.format(new  Date())));
            Transport.send(L_REPMSG);
		}
         catch (MessagingException mex) 
        {
             System.out.println("Messageing Exception "+mex.getMessage());
			 System.out.println("Messageing Exception "+mex.toString());
			//mex.printStackTrace();
		    Exception ex = null;
		   /* if ((ex = mex.getNextException()) != null) {
			ex.printStackTrace();
		    }*/
		}
		 catch (NullPointerException mex) {
		    System.out.println("Null Pointer Exception ");
		    }
		 catch (Exception mex) {
		    System.out.println("Exception "+mex.toString());
		    }
		
	    
    }


/*
public void sendfile(String LP_TOADDR,String[] LP_FILNM,String LP_SUBJECT,String LP_BDTEXT) 
{

//  System.out.println(cl_dat.M_flgTSTDT_pbst);
if((!cl_dat.M_flgTSTDT_pbst)||(LP_TOADDR.equalsIgnoreCase("systems@spl.co.in")))
{
	String L_FRADDR = cl_dat.M_strSYSEML_pbst;
    String L_TOHOST = "192.168.10.202";
//	String L_TOHOST = "11.0.0.2";
    String L_MSGTXT = LP_BDTEXT ;                   
	Properties props = System.getProperties();
	props.put("mail.smtp.host", L_TOHOST);
	Session L_SESSION = Session.getDefaultInstance(props);
	try 
    {
        //	L_SESSION.setDebug(true);
	    MimeMessage L_REPMSG = new MimeMessage(L_SESSION);
		L_REPMSG.setFrom(new InternetAddress(L_FRADDR));
		InternetAddress[] address = {new InternetAddress(LP_TOADDR)};
        L_REPMSG.setRecipients(Message.RecipientType.TO, address);
       // L_REPMSG.setRecipients(Message.RecipientType.CC, address);
        L_REPMSG.setSubject(LP_SUBJECT);
        MimeBodyPart L_MSGBDT = new MimeBodyPart();
		int L_intIDX =-1;   
		if(LP_FILNM!=null)
		{
			if((L_intIDX = LP_FILNM.indexOf(".")) > 0)
			{
			    if((LP_FILNM.substring(L_intIDX).equals("html"))||(LP_FILNM.substring(L_intIDX).equals("htm")))
			    L_MSGTXT   = "  ";  
			}
			else
			    L_MSGTXT += ".\n"+ "For Printing Please detach the file and give the command From MS DOS Prompt AS :"+"\n" + "copy "+ LP_FILNM.substring(11) + " prn" + "\n\n";
		}
	//	System.out.println(L_MSGTXT);
		L_MSGBDT.setText(L_MSGTXT);
		MimeBodyPart L_MSGBDA = new MimeBodyPart();
		FileDataSource fds=null;
		if(LP_FILNM!=null)
		{
			 for (int i = 0; i <LP_FILNM.length; i++) 
	         {
				fds = new FileDataSource(LP_FILNM[i]);
				L_MSGBDA.setDataHandler(new DataHandler(fds));
				L_MSGBDA.setFileName(fds.getName());
	         }
		}
        Multipart mp = new MimeMultipart();
        if(LP_FILNM!=null)
			mp.addBodyPart(L_MSGBDA);
		mp.addBodyPart(L_MSGBDT);
        L_REPMSG.setContent(mp);
        L_REPMSG.setSentDate(fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));
        Transport.send(L_REPMSG);
	}
     catch (MessagingException mex) 
    {
         System.out.println("Messageing Exception "+mex.getMessage());
		 System.out.println("Messageing Exception "+mex.toString());
		//mex.printStackTrace();
	    Exception ex = null;
	   // if ((ex = mex.getNextException()) != null) {
	   //	ex.printStackTrace();
	   // }
	}
	 catch (NullPointerException mex) {
	    System.out.println("Null Pointer Exception ");
	    }
	 catch (Exception mex) {
	    System.out.println("Exception "+mex.toString());
	    }
	
    }
}*/

    public void setFRADR(String P_strFRADR)
    {
        strFRUSR = cl_dat.M_strSYSEML_pbst;
        cl_dat.M_strSYSEML_pbst = P_strFRADR;
    }
    public void setORADR()
    {
        cl_dat.M_strSYSEML_pbst = strFRUSR;
       // System.out.println("Original "+cl_dat.M_strSYSEML_pbst);
    }
}
