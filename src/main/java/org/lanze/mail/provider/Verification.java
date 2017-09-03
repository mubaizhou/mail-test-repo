package org.lanze.mail.provider;

public interface Verification {
	String getVerificationCode(String mailAddress,String password);
}
