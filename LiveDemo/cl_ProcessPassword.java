//TO DECODE A TEXT EXTERNALLY, RUN THE PROGRAM FROM DOS PROMT BY GIVING ENCODED TEXT 
//AS COMMANDLINE AURGUMENT.

public class cl_ProcessPassword
{
	//Character array to store set of coded and decoded characters
	private static final char []	chrDECODE=new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'},
		chrCODE=new char[]{'Z','Y','X','W','V','U','T','S','R','Q','P','O','N','M','L','K','J','I','H','G','F','E','D','C','B','A','9','8','7','6','5','4','3','2','1','0'};
	
	//Private constructor restrics user from creting object of the class.
	//Done to avoid usage of method decode() by creating an object.
	private cl_ProcessPassword()
	{}
	
	//Codifies the Password
	public static String encode(String LP_PSPWD)
	{
		char[] L_PWDCD=new char[LP_PSPWD.length()]; 
		for(int i=0;i<L_PWDCD.length;i++)
			L_PWDCD[i]='|';
		LP_PSPWD=LP_PSPWD.toUpperCase();
		for(int i=0;i<LP_PSPWD.length();i++)
		{
			for(int j=0;j<chrDECODE.length;j++)
				if(LP_PSPWD.charAt(i)==chrDECODE[j])
				{
					L_PWDCD[i]=chrCODE[j];
					break;
				}
			if(L_PWDCD[i]=='|')
				L_PWDCD[i]=LP_PSPWD.charAt(i);
		}
		return String.copyValueOf(L_PWDCD);
	}
	
	//Decodes the Password
	private static String decode(String LP_PWDCD)
	{
		char[] L_PSPWD=new char[LP_PWDCD.length()]; 
		for(int i=0;i<L_PSPWD.length;i++)
			L_PSPWD[i]='|';
		LP_PWDCD=LP_PWDCD.toUpperCase();
		for(int i=0;i<LP_PWDCD.length();i++)
		{
			for(int j=0;j<chrDECODE.length;j++)
				if(LP_PWDCD.charAt(i)==chrCODE[j])
				{
					L_PSPWD[i]=chrDECODE[j];
					break;
				}
			if(L_PSPWD[i]=='|')
				L_PSPWD[i]=LP_PWDCD.charAt(i);
		}
		return String.copyValueOf(L_PSPWD);
	}
	
/*	public static void main(String[] a)
	{
		if(a.length==0)
		{
			System.out.println("Illegal Aurgument ..");
			System.exit(0);
		}
		System.out.println("Password DeCoded :"+cl_ProcessPassword.decode(a[0]));
	}*/
}