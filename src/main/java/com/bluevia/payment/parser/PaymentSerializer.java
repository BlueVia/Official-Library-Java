/**
 * @package com.bluevia.payment.parser This package contains the classes in order to parse and serialize data for Payment API.
 */
package com.bluevia.payment.parser;

import java.io.ByteArrayOutputStream;

import com.bluevia.commons.Entity;
import com.bluevia.commons.parser.ISerializer;
import com.bluevia.commons.parser.SerializeException;
import com.bluevia.commons.parser.xml.XmlSerializer;
import com.bluevia.payment.data.PaymentRequestTokenParams;
import com.bluevia.payment.parser.url.UrlEncodedPaymentReqTokenSerializer;

public class PaymentSerializer implements ISerializer {

	@Override
	public ByteArrayOutputStream serialize(Entity entity) throws SerializeException {
		
		if (entity instanceof PaymentRequestTokenParams){
			return new UrlEncodedPaymentReqTokenSerializer().serialize(entity);
		} else {     
			return new XmlSerializer().serialize(entity);
		}
	}

}
