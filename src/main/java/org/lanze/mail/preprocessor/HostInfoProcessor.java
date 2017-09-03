package org.lanze.mail.preprocessor;

public class HostInfoProcessor {
	
	public String getPop3Host(String mailAddress) {
		return (new StringBuffer().append("pop.").append(mailAddress.substring(mailAddress.indexOf("@")+1)).toString());
	}
	
}
