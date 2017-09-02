package org.lanze.mail.receive;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import jdk.nashorn.internal.runtime.regexp.RegExp;
import jdk.nashorn.internal.runtime.regexp.RegExpMatcher;

public class LMailReceiver {
	public static void fetch() {
	      try {
	    	  String pop3Host = "pop3.mail.ru";
	    	  String user = "mffaau9@mail.ru";
	    	  String password = "VfV0nmn";
	    	  
	    	  
	         // create properties field
	         Properties properties = new Properties();
	         properties.put("mail.store.protocol", "pop3");
	         properties.put("mail.pop3.host", pop3Host);
	         properties.put("mail.pop3.port", "110");
	         properties.put("mail.pop3.starttls.enable", "true");
	         Session emailSession = Session.getDefaultInstance(properties);
	         // emailSession.setDebug(true);

	         // create the POP3 store object and connect with the pop server
	         Store store = emailSession.getStore("pop3");

	         store.connect(pop3Host, user, password);

	         // create the folder object and open it
	         Folder emailFolder = store.getFolder("INBOX");
	         emailFolder.open(Folder.READ_ONLY);


	         // retrieve the messages from the folder in an array and print it
	         Message[] messages = emailFolder.getMessages();
	         System.out.println("messages.length---" + messages.length);

	         for (int i = 0; i < messages.length; i++) {
	            Message message = messages[i];
	            //Message message = messages[messages.length-1];
	            Address[] froms = message.getFrom();
	            if("Apple".equals(((InternetAddress)froms[0]).getPersonal())){
	            	System.out.println("邮件主题:" + message.getSubject());  
		            System.out.println("发件人地址:" + ((InternetAddress)froms[0]).getAddress());
		            System.out.println("发件人姓名:" + ((InternetAddress)froms[0]).getPersonal());
		            
		         // getContent() 是获取包裹内容, Part相当于外包装  
		            Object o = message.getContent();  
		            if(o instanceof Multipart) {  
		                Multipart multipart = (Multipart) o ;  
		                reMultipart(multipart);  
		            } else if (o instanceof Part){  
		                Part part = (Part) o;   
		                rePart(part);  
		            } else {  
		                System.out.println("类型" + message.getContentType());  
		                System.out.println("内容" + message.getContent());  
		            } 
	            }
	             
	            
	         }

	         // close the store and folder objects
	         emailFolder.close(false);
	         store.close();

	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	    }
		
	/** 
     * @param part 解析内容 
     * @throws Exception 
     */  
    private static void rePart(Part part) throws MessagingException,UnsupportedEncodingException, IOException, FileNotFoundException {  
        /*if (part.getDisposition() != null) {  
          
            String strFileNmae = MimeUtility.decodeText(part.getFileName()); //MimeUtility.decodeText解决附件名乱码问题  
            System.out.println("发现附件: " +  MimeUtility.decodeText(part.getFileName()));  
            System.out.println("内容类型: " + MimeUtility.decodeText(part.getContentType()));  
            System.out.println("附件内容:" + part.getContent());  
            InputStream in = part.getInputStream();// 打开附件的输入流  
            // 读取附件字节并存储到文件中  
            java.io.FileOutputStream out = new FileOutputStream(strFileNmae);  
            int data;  
            while((data = in.read()) != -1) {  
                out.write(data);  
            }  
            in.close();  
            out.close();  
        } else { */ 
            if(part.getContentType().startsWith("text/plain")) {  
                System.out.println("文本内容：" + part.getContent());  
                //String verificationCode = Pattern.compile(regex).matcher(content).find();
                String REGEX = "\\r\\n\\r\\n\\d+\\r\\n\\r\\n";
    			String CONTENT = part.getContent().toString();
    			Pattern p = Pattern.compile(REGEX);
    		    // 获取 matcher 对象
    		    Matcher m = p.matcher(CONTENT);
    		    while(m.find()){
    		    	System.out.println("验证码***************是："+m.group().replaceAll("\\r", "").replaceAll("\\n", ""));
    		    }
                
            } /*else {  
                //System.out.println("HTML内容：" + part.getContent());  
            }  */
        /*} */ 
    }  
      
    /** 
     * @param multipart // 接卸包裹（含所有邮件内容(包裹+正文+附件)） 
     * @throws Exception 
     */  
    private static void reMultipart(Multipart multipart) throws Exception {  
        System.out.println("邮件共有" + multipart.getCount() + "部分组成");  
        // 依次处理各个部分  
        for (int j = 0, n = multipart.getCount(); j < n; j++) {  
            System.out.println("处理第" + j + "部分");  
            Part part = multipart.getBodyPart(j);//解包, 取出 MultiPart的各个部分, 每部分可能是邮件内容,  
            // 也可能是另一个小包裹(MultipPart)  
            // 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative  
            if (part.getContent() instanceof Multipart) {  
                Multipart p = (Multipart) part.getContent();// 转成小包裹  
                //递归迭代  
                reMultipart(p);  
            } else {  
                rePart(part);  
            }  
         }  
    }  
	
	
	
	
		public static void main(String[] args) {
			fetch();
			/*String REGEX = "a*b";
			String INPUT = "aabfooaabfooabfoob";
			String REPLACE = "";
			Pattern p = Pattern.compile(REGEX);
		    // 获取 matcher 对象
		    Matcher m = p.matcher(INPUT);
		    StringBuffer sb = new StringBuffer();
		    while(m.find()){
		       m.appendReplacement(sb,REPLACE);
		    }
		    m.appendTail(sb);
		    System.out.println(sb.toString());*/
		}
}
