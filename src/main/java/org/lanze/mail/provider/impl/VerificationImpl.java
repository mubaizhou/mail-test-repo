package org.lanze.mail.provider.impl;

import org.lanze.mail.provider.Verification;
import org.lanze.mail.receive.LMailReceiver;

public class VerificationImpl implements Verification{

	public String getVerificationCode(String mailAddress, String password) {
		
		return LMailReceiver.fetchVerificationCode(mailAddress, password);
	}

}
