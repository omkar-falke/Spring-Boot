import java.awt.*;
				txtPRVDT.setEnabled(false);
				txtPRVSS.setEnabled(false);
//				setENBL1(false);
	 */
		/**  Picking up details for previous updated session
		 */
		/** Setting / displaying details for Cuurent Session, to be updated
		 */
                        strCURDT = txtCURDT.getText().trim();
		 */
		 */
				{
					return false;
				}
		 */
	 */
			strCURDT = txtCURDT.getText();
			if (!chkVLDSSN())
            if(!chkDTFILE())
				return;
	

		/** Validating the current session
		*/
		{
			boolean strLCHKFL = true;
			try
			{
				int intTMPCUR = Integer.parseInt(strCURSS);
				int intTMPPRV = Integer.parseInt(strPRVSS);
				System.out.println("Date Comparison "+M_fmtLCDAT.parse(strCURDT).compareTo(M_fmtLCDAT.parse(strPRVDT)));
				if(M_fmtLCDAT.parse(strCURDT).compareTo(M_fmtLCDAT.parse(strPRVDT))<0)
				{
					oppOPTNPN.showMessageDialog(this,"Data upto "+strPRVDT+" is already updated","Error Message",JOptionPane.INFORMATION_MESSAGE);
					strLCHKFL = false;
				}
				else if((M_fmtLCDAT.parse(strCURDT).compareTo(M_fmtLCDAT.parse(strPRVDT))>0) && (!strPRVSS.equals("99")))
				{
					oppOPTNPN.showMessageDialog(this,"99 session of "+strPRVDT+" is not updated","Error Message",JOptionPane.INFORMATION_MESSAGE);
					strLCHKFL = false;
				}
				else if((M_fmtLCDAT.parse(strCURDT).compareTo(M_fmtLCDAT.parse(strPRVDT))==0) && intTMPCUR < intTMPPRV)
				{
					oppOPTNPN.showMessageDialog(this,"Data upto session "+strPRVSS+" is already updated","Error Message",JOptionPane.INFORMATION_MESSAGE);
					strLCHKFL = false;
				}
				if((M_fmtLCDAT.parse(strCURDT).compareTo(M_fmtLCDAT.parse(strPRVDT))==0) && intTMPCUR == intTMPPRV)
				{
		            int intSELOPT = oppOPTNPN.showConfirmDialog(null," Session "+strPRVSS+" already updated .... Reupdate ?","Warning",JOptionPane.YES_NO_OPTION);
					if(intSELOPT != 0)
					{
						strLCHKFL = false;
					}
				}
				if(intTMPCUR-intTMPPRV>1)
				{
		            int intSELOPT = oppOPTNPN.showConfirmDialog(null," Previous session Updated : "+strPRVSS+" .... Continue ?","Warning",JOptionPane.YES_NO_OPTION);
					if(intSELOPT != 0)
					{
						strLCHKFL = false;
					}
				}
				if((M_fmtLCDAT.parse(strCURDT).compareTo(M_fmtLCDAT.parse(strPRVDT))==1) && (strPRVSS.equals("99")) && (!strCURSS.equals("01")))
				{
		            int intSELOPT = oppOPTNPN.showConfirmDialog(null," Previous session Updated : "+strPRVSS+" .... Continue ?","Warning",JOptionPane.YES_NO_OPTION);
					if(intSELOPT != 0)
					{
						strLCHKFL = false;
					}
				}
			}
			catch (Exception L_EX)
			{
			}
		}
	
	
		/** Data file updation, Main Method
		 */
		 * @param LP_FLPRFX  Data file prefix (IM for Indent Master, IT for Indent Transaction etc.)
		 * @param LP_TRFNO   Data transfer serial number (predefined in MR_DTREF table) of the corresponding data file
		 * @param LP_CURFILE	Corresponding main data file getting updated on Server
		 * @param LP_ORDFL		Order (of columns) according to which data will be selected (picked-up) for updation
		 * @param LP_CHKFL		Filtering condition to be applied while selecting data for updation
		 * 
		 */
		/** Garbage collection
		 */
		 * for reference, during data updation
		 */
		 * into the Hash Table
		 */
		 */
		 */
		 */
		 */
		 */
		 * @param LP_FLDNM Input field Name A method with same name (condition) is defined for 
		 * <br> processing corresponding source code.
         * <br> with respective verifications against various data types
         * @param	LP_RSLSET		Result set name
         * @param	LP_FLDNM		Name of the field for which data is to be extracted
         * @param	LP_FLDTP		Data Type of the field
         */
		private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) {
		 * through getFLDVL
		 */
		 * @param	LP_PRMST_KEY	Product Description
		 */
		 * <B> for Specified Indent Master key
		 * @param LP_INMST_KEY	Indent master key
		 */
		 * <B> for Specified D.O. Master key
		 * @param LP_DOMST_KEY	D.O. master key
		 */
		 * <B> for Specified D.O.Transaction key
		 * @param LP_DOTRN_KEY	D.O.Transaction key
		 */
		 * <B> for Specified Code Transaction key
		 * @param LP_CDTRN_KEY	Code Transaction key
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
		 * <B> for Specified Indent Transaction key
		 * @param LP_INTRN_KEY	Indent Transaction key
		 */
         * @param	Resultset Name
         * @param	Field name
         * @param	Dtata type of the field
		 */
		
		private void insREFTBL()
	 */
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */
/** Generating string for Updation Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 * @param	LP_UPDTP	Type of updation,  For where condition / for Upsdation string
 */
 * @param	LP_SQLVAL	Query to be executed
 */
 * @param	LP_SQLVAL	Query to be executed
 */
 * @param	LP_PTHWD	Path for foxpro table
 */
 * @param	LP_CONVAL	Connection Object
 */
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 * @param	LP_UPDTP	Type of updation,  For where condition / for Upsdation string
 */