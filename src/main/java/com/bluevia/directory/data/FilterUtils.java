package com.bluevia.directory.data;

import java.util.HashMap;

import com.bluevia.directory.data.UserInfo.DataSet;

public class FilterUtils {

	private static final HashMap<UserInfo.DataSet, String> sUserInfoMatcher;
	private static final HashMap<PersonalInfo.Fields, String> sPersonalInfoMatcher;
	private static final HashMap<Profile.Fields, String> sProfileMatcher;
	private static final HashMap<AccessInfo.Fields, String> sAccessInfoMatcher;
	private static final HashMap<TerminalInfo.Fields, String> sTerminalInfoMatcher;

	static {

		//User Info
		sUserInfoMatcher = new HashMap<DataSet, String>();
		sUserInfoMatcher.put(DataSet.ACCESS_INFO, FilterConstants.DATASET_ACCESSINFO);
		sUserInfoMatcher.put(DataSet.PERSONAL_INFO, FilterConstants.DATASET_PERSONALINFO);
		sUserInfoMatcher.put(DataSet.PROFILE, FilterConstants.DATASET_PROFILE);
		sUserInfoMatcher.put(DataSet.TERMINAL_INFO, FilterConstants.DATASET_TERMINALINFO);

		//Personal Info
		sPersonalInfoMatcher = new HashMap<PersonalInfo.Fields, String>();
		sPersonalInfoMatcher.put(PersonalInfo.Fields.GENDER, FilterConstants.PERSONALINFO_GENDER);

		//User Profile
		sProfileMatcher = new HashMap<Profile.Fields, String>();
		sProfileMatcher.put(Profile.Fields.USER_TYPE, FilterConstants.PROFILE_USERTYPE);
		sProfileMatcher.put(Profile.Fields.ICB, FilterConstants.PROFILE_ICB);
		sProfileMatcher.put(Profile.Fields.OCB, FilterConstants.PROFILE_OCB);
		sProfileMatcher.put(Profile.Fields.LANGUAGE, FilterConstants.PROFILE_LANGUAGE);
		sProfileMatcher.put(Profile.Fields.PARENTAL_CONTROL, FilterConstants.PROFILE_PARENTALCONTROL);
		sProfileMatcher.put(Profile.Fields.OPERATOR_ID, FilterConstants.PROFILE_OPERATORID);
		sProfileMatcher.put(Profile.Fields.MMS_STATUS, FilterConstants.PROFILE_MMSSTATUS);
		sProfileMatcher.put(Profile.Fields.SEGMENT, FilterConstants.PROFILE_SEGMENT);

		//Access Info
		sAccessInfoMatcher = new HashMap<AccessInfo.Fields, String>();
		sAccessInfoMatcher.put(AccessInfo.Fields.ACCESS_TYPE, FilterConstants.ACCESSINFO_ACCESSTYPE);
		sAccessInfoMatcher.put(AccessInfo.Fields.APN, FilterConstants.ACCESSINFO_APN);
		sAccessInfoMatcher.put(AccessInfo.Fields.ROAMING, FilterConstants.ACCESSINFO_ROAMING);

		//Terminal Info
		sTerminalInfoMatcher = new HashMap<TerminalInfo.Fields, String>();
		sTerminalInfoMatcher.put(TerminalInfo.Fields.BRAND, FilterConstants.TERMINALINFO_BRAND);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.MODEL, FilterConstants.TERMINALINFO_MODEL);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.VERSION, FilterConstants.TERMINALINFO_VERSION);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.MMS, FilterConstants.TERMINALINFO_MMS);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.EMS, FilterConstants.TERMINALINFO_EMS);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.SMART_MESSAGING, FilterConstants.TERMINALINFO_SMARTMESSAGING);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.WAP, FilterConstants.TERMINALINFO_WAP);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.USSD_PHASE, FilterConstants.TERMINALINFO_USSDPHASE);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.EMS_MAX_NUMBER, FilterConstants.TERMINALINFO_EMSMAXNUMBER);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.WAP_PUSH, FilterConstants.TERMINALINFO_WAP_PUSH);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.MMS_VIDEO, FilterConstants.TERMINALINFO_MMS_VIDEO);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.VIDEO_STREAMING, FilterConstants.TERMINALINFO_VIDEO_STREAMING);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.SCREEN_RESOLUTION, FilterConstants.TERMINALINFO_SCREENRESOLUTION);
	}

	public static String buildUserInfoFilter(UserInfo.DataSet[] dataSet){
		StringBuilder sb = new StringBuilder();

		for (DataSet data : dataSet){
			sb.append(sUserInfoMatcher.get(data));
			sb.append(",");
		}

		String res = sb.toString();

		if (res.endsWith(",")){
			res = res.substring(0,res.length()-1);
		}

		return res;
	}	

	public static String buildPersonalInfoFilter(PersonalInfo.Fields[] fields){
		StringBuilder sb = new StringBuilder();

		for (PersonalInfo.Fields field : fields){
			sb.append(sPersonalInfoMatcher.get(field));
			sb.append(",");
		}

		return deleteComma(sb.toString());
	}

	public static String buildProfileFilter(Profile.Fields[] fields){
		StringBuilder sb = new StringBuilder();

		for (Profile.Fields field : fields){
			sb.append(sProfileMatcher.get(field));
			sb.append(",");
		}

		return deleteComma(sb.toString());
	}

	public static String buildAccessInfoFilter(AccessInfo.Fields[] fields){

		StringBuilder sb = new StringBuilder();

		for (AccessInfo.Fields field : fields){
			sb.append(sAccessInfoMatcher.get(field));
			sb.append(",");
		}

		return deleteComma(sb.toString());
	}

	public static String buildTerminalInfoFilter(TerminalInfo.Fields[] fields){
		StringBuilder sb = new StringBuilder();

		for (TerminalInfo.Fields field : fields){
			sb.append(sTerminalInfoMatcher.get(field));
			sb.append(",");
		}

		return deleteComma(sb.toString());
	}

	protected static String deleteComma(String in){
		if (in.endsWith(",")){
			in = in.substring(0,in.length()-1);
		}

		return in;
	}
}
