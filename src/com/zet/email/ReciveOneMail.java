package com.zet.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.text.TextUtils;

import com.lidroid.xutils.utils.LogUtils;
import com.zet.db.SQLiteUtils;
import com.zet.parser.RSSFeed;
import com.zet.parser.RSSHandler2;

/**
 * 有一封邮件就需要建立一个ReciveMail对象
 */
public class ReciveOneMail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MimeMessage mimeMessage = null;
	private String saveAttachPath = ""; // 附件下载后的存放目录
	private StringBuffer bodytext = new StringBuffer();// 存放邮件内容
	private String dateformat = "yy-MM-dd HH:mm"; // 默认的日前显示格式
	private String appfileName = "";
	private String appfileNameReal = "";

	public ReciveOneMail() {
	}

	public ReciveOneMail(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	/**
	 * 获得发件人的地址和姓名
	 */
	public String getFrom() throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		String from = address[0].getAddress();
		if (from == null)
			from = "";
		String personal = address[0].getPersonal();
		if (personal == null)
			personal = "";
		String fromaddr = personal + "<" + from + ">";
		return fromaddr;
	}

	/**
	 * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址
	 */
	public String getMailAddress(String type) throws Exception {
		String mailaddr = "";
		String addtype = type.toUpperCase();
		InternetAddress[] address = null;
		if (addtype.equals("TO") || addtype.equals("CC")
				|| addtype.equals("BCC")) {
			if (addtype.equals("TO")) {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.TO);
			} else if (addtype.equals("CC")) {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.CC);
			} else {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.BCC);
			}
			if (address != null) {
				for (int i = 0; i < address.length; i++) {
					String email = address[i].getAddress();
					if (email == null)
						email = "";
					else {
						email = MimeUtility.decodeText(email);
					}
					String personal = address[i].getPersonal();
					if (personal == null)
						personal = "";
					else {
						personal = MimeUtility.decodeText(personal);
					}
					String compositeto = personal + "<" + email + ">";
					mailaddr += "," + compositeto;
				}
				mailaddr = mailaddr.substring(1);
			}
		} else {
			throw new Exception("Error emailaddr type!");
		}
		return mailaddr;
	}

	/**
	 * 获得邮件主题
	 */
	public String getSubject() throws MessagingException {
		String subject = "";
		try {
			subject = MimeUtility.decodeText(mimeMessage.getSubject());
			if (subject == null)
				subject = "";
		} catch (Exception exce) {
		}
		return subject;
	}

	/**
	 * 获得邮件发送日期
	 */
	public String getSentDate() throws Exception {
		Date sentdate = mimeMessage.getSentDate();
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		return format.format(sentdate);
	}

	/**
	 * 获得邮件正文内容
	 */
	public String getBodyText() {
		return bodytext.toString();
	}

	/**
	 * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
	 */
	public void getMailContent(Part part) throws Exception {
		String contenttype = part.getContentType();
		// LogUtils.d("contenttype : " + contenttype);
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if (nameindex != -1)
			conname = true;
		// LogUtils.e("CONTENTTYPE: " + contenttype);
		if (part.isMimeType("text/plain") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("text/html") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i));
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent());
		} else {
		}
	}

	/**
	 * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
	 */
	public boolean getReplySign() throws MessagingException {
		boolean replysign = false;
		String needreply[] = mimeMessage
				.getHeader("Disposition-Notification-To");
		if (needreply != null) {
			replysign = true;
		}
		return replysign;
	}

	/**
	 * 获得此邮件的Message-ID
	 */
	public String getMessageId() throws MessagingException {
		return mimeMessage.getMessageID();
	}

	/**
	 * 【判断此邮件是否已读，如果未读返回返回false,反之返回true】
	 */
	public boolean isNew() throws MessagingException {
		boolean isnew = false;
		Flags flags = ((Message) mimeMessage).getFlags();
		Flags.Flag[] flag = flags.getSystemFlags();
		// System.out.println("flags's length: " + flag.length);
		for (int i = 0; i < flag.length; i++) {
			if (flag[i] == Flags.Flag.SEEN) {
				isnew = true;
				break;
			}
		}
		return isnew;
	}

	/**
	 * 判断此邮件是否包含附件
	 */
	public boolean isContainAttach(Part part) throws Exception {
		boolean attachflag = false;
		String contentType = part.getContentType();
		// LogUtils.e(contentType);
		String fileName;
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
								.equals(Part.INLINE)))) {
					attachflag = true;
					fileName = mpart.getFileName();
					if (fileName != null) {
						fileName = MimeUtility.decodeText(fileName);
						appfileName += fileName;
						// LogUtils.d("appfileName ---1 " + appfileName);
					}
				} else if (mpart.isMimeType("multipart/*")) {
					attachflag = isContainAttach((Part) mpart);
				} else {
					fileName = mpart.getFileName();
					if (fileName != null) {
						fileName = MimeUtility.decodeText(fileName);
						appfileName += fileName;
						// LogUtils.d("appfileName ---2 " + appfileName);
					}
					String contype = mpart.getContentType();
					if (contype.toLowerCase().indexOf("application") != -1)
						attachflag = true;
					if (contype.toLowerCase().indexOf("name") != -1)
						attachflag = true;
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			attachflag = isContainAttach((Part) part.getContent());
		}
		return attachflag;
	}

	/**
	 * 【保存附件】
	 */
	public void saveAttachMent(Part part, String uid, Context mContext,
			String emailAddress) throws Exception {
		String fileName = "";
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
								.equals(Part.INLINE)))) {
					fileName = mpart.getFileName();
					if (fileName != null) {
						fileName = MimeUtility.decodeText(fileName);
						saveFile(fileName, mpart.getInputStream(), uid,
								mContext, emailAddress);
					}

				} else if (mpart.isMimeType("multipart/*")) {
					saveAttachMent(mpart, uid, mContext, emailAddress);
				} else {
					fileName = mpart.getFileName();
					if (fileName != null) {
						fileName = MimeUtility.decodeText(fileName);
						saveFile(fileName, mpart.getInputStream(), uid,
								mContext, emailAddress);
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachMent((Part) part.getContent(), uid, mContext,
					emailAddress);
		}
	}

	/**
	 * 【设置附件存放路径】
	 */

	public void setAttachPath(String attachpath) {
		this.saveAttachPath = attachpath;
	}

	/**
	 * 【设置日期显示格式】
	 */
	public void setDateFormat(String format) throws Exception {
		this.dateformat = format;
	}

	/**
	 * 【获得附件存放路径】
	 */
	public String getAttachPath() {
		return saveAttachPath;
	}

	/**
	 * 【获得附件名字】
	 */
	public String getappfileName(String uid) {
		return (uid + "@" + appfileNameReal);
	}

	public String getappfileName1(String uid) {
		// LogUtils.e("appfileName ---3 " + uid + "@" + appfileName);
		return (uid + "@" + appfileName);
	}

	/**
	 * 【真正的保存附件到指定目录里】
	 */
	@SuppressWarnings("resource")
	public synchronized void saveFile(String fileName, InputStream in, String uid,
			Context mContext, String emailAddress) throws Exception {
		// LogUtils.e("fileName : " + fileName);
		appfileNameReal = fileName;
		// String storedir = getAttachPath() + "/tmp";
		// String pathName = storedir + "/" + uid + "@" + fileName;
		if (fileName.endsWith("xml")) {
			readconfigOn(in, mContext, uid, emailAddress);
		}
	}

	private void readconfigOn(InputStream in, Context mContext, String userid,
			String emailAddress) {
		byte[] resultByte = new byte[512];
		int length = 0;
		StringBuffer mBuffer = new StringBuffer();
		try {
	        BufferedReader br = new BufferedReader(  
	                new InputStreamReader(in,"UTF-8"));
	        String data = "";
	        while ((data = br.readLine()) != null) {  
	            mBuffer.append(data);  
            }
	        /*
			while ((length = in.read(resultByte)) != -1) {
				String resultStr = new String(resultByte, 0, length); // 根据从XML文件读取byte的个数进行截取
				mBuffer.append(resultStr);
			}
			*/
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			RSSHandler2 handler = new RSSHandler2();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(new StringReader(mBuffer.toString())));
			RSSFeed mRssFeed = handler.getFeed();
			// 在Isemaillist为false的情况下uid当作FILE_ID
			if (TextUtils.isEmpty(userid)) {
				userid = mRssFeed.getFILE_ID();
				SQLiteUtils.getInstance(mContext).addLocalEmailToDB(userid, emailAddress);
			}
			long index = SQLiteUtils.getInstance(mContext).addEmailWorksToDB(
					mRssFeed, userid, emailAddress);
			if (index > 0) {
				SQLiteUtils.getInstance(mContext)
						.addEmailDetailsToDB(
								mRssFeed.getAllDETAILItems(),
								userid,
								emailAddress,
								userid + UUID.randomUUID()
										+ System.currentTimeMillis());
				SQLiteUtils.getInstance(mContext).addEmailAttachsToDB(
						mRssFeed.getAllATTACHItems(), userid, emailAddress);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					LogUtils.e("Exception occur when try to close inputStream!");
					e.printStackTrace();
				}
			}
		}
	}
}