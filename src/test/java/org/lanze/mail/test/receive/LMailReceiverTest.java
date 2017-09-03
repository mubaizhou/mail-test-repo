package org.lanze.mail.test.receive;

import org.junit.Test;
import org.lanze.mail.provider.Verification;
import org.lanze.mail.provider.impl.VerificationImpl;


public class LMailReceiverTest {
	
	@Test
	public void testFetch(){
		Verification verification = new VerificationImpl();
		String verificationCode = verification.getVerificationCode("mffaau9@mail.ru", "VfV0nmn");
		System.out.println("验证码是："+verificationCode);
		
	}
}
