/**
 * @package com.bluevia.payment.parser.url This package contains the classes in order to serialize and parse URL encoded data for Payment API.
 */
package com.bluevia.payment.parser.url;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import com.bluevia.commons.Entity;
import com.bluevia.commons.parser.ISerializer;
import com.bluevia.commons.parser.SerializeException;
import com.bluevia.payment.data.PaymentInfo;
import com.bluevia.payment.data.PaymentRequestTokenParams;

public class UrlEncodedPaymentReqTokenSerializer implements ISerializer {

	private static final String PAYMENT_INFO = "paymentInfo.";
	private static final String PINFO_AMOUNT = PAYMENT_INFO + "amount";
	private static final String PINFO_CURRENCY = PAYMENT_INFO + "currency";

	private static final String SERVICE_INFO = "serviceInfo.";
	private static final String SINFO_SERVICE_ID = SERVICE_INFO + "serviceID";
	private static final String SINFO_NAME = SERVICE_INFO + "name";
	private static final String SINFO_DESCRIPTION = SERVICE_INFO + "description";


	@Override
	public ByteArrayOutputStream serialize(Entity entity) throws SerializeException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		if (entity == null)
			throw new SerializeException("Can not serialize null entity ");

		if (! (entity instanceof PaymentRequestTokenParams))
			throw new SerializeException("Entity class does not support serializing this entity class");

		PaymentRequestTokenParams params = (PaymentRequestTokenParams) entity;
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(6);

		//ServiceInfo
		nameValuePairs.add(new BasicNameValuePair(SINFO_SERVICE_ID, params.getServiceInfo().getServiceId()));
		nameValuePairs.add(new BasicNameValuePair(SINFO_NAME, params.getServiceInfo().getServiceName()));
		if (params.getServiceInfo().getServiceDescription() != null)
			nameValuePairs.add(new BasicNameValuePair(SINFO_DESCRIPTION, 
					params.getServiceInfo().getServiceDescription()));

		//PaymentInfo
		PaymentInfo pInfo = params.getPaymentInfo();

		//Mandatory params
		nameValuePairs.add(new BasicNameValuePair(PINFO_AMOUNT, String.valueOf(pInfo.getAmount())));
		nameValuePairs.add(new BasicNameValuePair(PINFO_CURRENCY, pInfo.getCurrency()));

		try {

			UrlEncodedFormEntity formEncodedEntity = new UrlEncodedFormEntity(nameValuePairs);
			formEncodedEntity.writeTo(out);

		} catch (IOException e) {
			throw new SerializeException(e);
		}

		return out;
	}

}
