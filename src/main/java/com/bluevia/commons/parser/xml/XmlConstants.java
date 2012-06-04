package com.bluevia.commons.parser.xml;

/**
 * Constants (like XML tags, etc) common to all APIs
 * @author Telefonica R&D
 * 
 */
public class XmlConstants {
	
	/*
	 * Query parameters
	 */
	public static final String PARAM_VERSION_KEY = "version";
	public static final String VERSION_1 = "v1";
	
    /*
     * XML Generic
     */
    public static final String XSD_CLIENT_EXCEPTION = "ClientException";
    public static final String XSD_XSD_CLIENT_EXCEPTION_ATTR_TEXT = "text";
    
    public static final String XSD_RECEIPTREQUEST = "receiptRequest";
    public static final String XSD_RECEIPTREQUEST_ENDPOINT = "endpoint";
    public static final String XSD_RECEIPTREQUEST_CORRELATOR = "correlator";

    
    /*
     * USERID XML tags
     */
    public static final String XSD_USERIDTYPE_PHONENUMBER = "phoneNumber";
    public static final String XSD_USERIDTYPE_ANYURI = "anyUri";
    public static final String XSD_USERIDTYPE_ALIAS = "alias";
    public static final String XSD_USERIDTYPE_IPADDRESS = "ipAddress";
    public static final String XSD_USERIDTYPE_IPADDRESS_IPV4 = "ipv4";
    public static final String XSD_USERIDTYPE_IPADDRESS_IPV6 = "ipv6";
    public static final String XSD_USERIDTYPE_OTHERTYPE = "otherId";
    public static final String XSD_USERIDTYPE_OTHERTYPE_TYPE = "type";
    public static final String XSD_USERIDTYPE_OTHERTYPE_VALUE = "value";


    /*
     * SMS XML tags
     */
    public static final String XSD_SMSTEXTTYPE_SMSTEXT = "smsText";
    public static final String XSD_SMSNOTIFICATION_SMSMESSAGE = "smsNotification";   
    public static final String XSD_SMS_API_INSTANCE = "com.telefonica.schemas.unica.rest.sms.v1";
    public static final String NS_SMS_API_URI = "http://www.telefonica.com/schemas/UNICA/REST/sms/v1/";
    

    /*
     * MMS XML tags
     */
    public static final String PARAM_USE_ATTACHMENT_URLS = "useAttachmentURLs";
    public static final String XSD_MMSTEXTTYPE_MMSMESSAGE = "message";
    public static final String XSD_MMSNOTIFICATION_MMSMESSAGE = "messageNotification";
    public static final String XSD_MMS_API_INSTANCE = "com.telefonica.schemas.unica.rest.mms.v1";
	public static final String NS_MMS_API_URI = "http://www.telefonica.com/schemas/UNICA/REST/mms/v1/";
  
	
    /* Advertisement XML Tags */
    public static final String XSD_AD_ATTRIBUTE_ATTR_TYPE_URL_VALUE = "URL";
    public static final String XSD_AD_ATTRIBUTE_ATTR_TYPE_ADTEXT_VALUE = "adtext";
    public static final String XSD_AD_ATTRIBUTE_ATTR_TYPE_LOCATOR_VALUE = "locator";
    public static final String XSD_AD_ATTRIBUTE_ATTR_TYPE_CODEC_VALUE = "codec";

    public static final String XSD_AD_INTERACTION_ATTR_TYPE_CLICK2WAP_VALUE = "click2wap";

    public static final String XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_IMAGE_VALUE="image";
    public static final String XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_TEXT_VALUE="text";
    public static final String XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_SOUND_VALUE="sound";
    public static final String XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_ZIP_VALUE="zip";
    public static final String XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_PAGE_VALUE="page";
    
    /* Location xml tags */	
	public static final String XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_RETRIEVED_VALUE = "Retrieved";
	public static final String XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_NONRETRIEVED_VALUE = "Non-Retrieved";
	public static final String XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_ERROR_VALUE = "Error";
	
    
	/* XML-RPC common tags */
	public static final String XSD_RPC_METHOD_CALL = "methodCall";
	
	/* Payment xml tags */
	public static final String XSD_PAYMENT_API_INSTANCE = "com.telefonica.schemas.unica.rpc.payment.v1";
	public static final String NS_PAYMENT_API_URI = "http://www.telefonica.com/schemas/UNICA/RPC/payment/v1";
	public static final String XSD_PAYMENT_ATTR_TRANSACTION_STATUS_SUCCESS_VALUE = "SUCCESS";
	public static final String XSD_PAYMENT_ATTR_TRANSACTION_STATUS_FAILURE_VALUE = "FAILURE";
	public static final String XSD_PAYMENT_ATTR_TRANSACTION_STATUS_PENDING_VALUE = "PENDING";
	
	
}