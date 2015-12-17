package com.zet.utils;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.text.TextUtils;

import com.bdkj.bdlibrary.utils.LConfigUtils;
import com.lidroid.xutils.utils.LogUtils;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.zet.R;
import com.zet.activity.EmptyActivity;
import com.zet.db.SQLiteUtils;
import com.zet.email.ReciveOneMail;

public class RefreshEmailList extends Service {
	private Context mContext;
	SharedPreferences emailSetting;
	static String emailUsername;
	static String emailPssword;
	private Timer mTimer;
	private MyTimerTask mTimerTask;
	private boolean reload = true;
	private Message message[] = null;
	private ReciveOneMail pmm = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		getEmailConfig();
		mTimer = new Timer();
		mContext = getApplicationContext();
	}

	@SuppressWarnings("deprecation")
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		// handler.postDelayed(task, 60 * 1000);// 延迟调用
		if (mTimer != null) {
			if (mTimerTask != null) {
				mTimerTask.cancel(); // 将原任务从队列中移除
			}
			mTimerTask = new MyTimerTask();
			mTimer.schedule(mTimerTask, 30 * 1000, 5 * 60 * 1000);
		}
		;

	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 需要执行的代码
			try {
				if (!TextUtils.isEmpty(emailUsername)
						&& !TextUtils.isEmpty(emailPssword)
						&& checkNetwork(mContext)) {
					do {
						receiverEmail();
					} while (reload);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void receiverEmail() {
		String[] emailUsernames = emailUsername.split("@");
		Properties props = System.getProperties();
		Session session = null;
		URLName urln = null;
		Store store = null;
		try {
//			if (emailUsername.contains("@126")) {
//				props.put("mail.smtp.host", "smtp.126.com");
//				props.put("mail.smtp.post", 25);
//				props.put("mail.smtp.auth", true);
//				session = Session.getDefaultInstance(props, null);
//				urln = new URLName("pop3", "pop3.126.com", 110, null,
//						emailUsernames[0], emailPssword);
//				store = session.getStore(urln);
//
//				store.connect();
//			} else 
			if (emailUsername.contains("@qq")
					|| emailUsername.contains("@QQ")) {
				String host = "imap.qq.com";
				String port = "143";
				props.setProperty("mail.imap.socketFactory.port", port);
				props.setProperty("mail.imap.partialfetch", "false");
				props.setProperty("mail.store.protocol", "imap");
				props.setProperty("mail.imap.host", host);
				props.setProperty("mail.imap.port", port);
				props.setProperty("mail.imap.auth.login.disable", "true");
				props.setProperty("mail.mime.base64.ignoreerrors", "true");
				session = Session.getDefaultInstance(props, null);
				session.setDebug(false);
				store = session.getStore("imap");
				store.connect(host, emailUsernames[0], emailPssword);
			}else {
				return;
			}
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			// List<ReciveOneMailDB> list = new ArrayList<ReciveOneMailDB>();
			reload = false;
			if (folder instanceof IMAPFolder) {
				IMAPFolder inbox = (IMAPFolder) folder;
				message = inbox.getMessages();
				for (int i = 0; i < message.length; i++) {
					MimeMessage mimeMessage = (MimeMessage) message[i];
					pmm = new ReciveOneMail((MimeMessage) message[i]);
					String uid = Long.toString(inbox.getUID(mimeMessage));
					if (!SQLiteUtils.getInstance(mContext).emailIsExist(uid,
							emailUsername)) {
						// 获得邮件内容
						IMAPMessage msg = (IMAPMessage) message[i];
						String contentype = pmm.getFrom();
						LogUtils.e("contentype : " + contentype);
						if (!TextUtils.isEmpty(contentype)
								&& (contentype.contains("PostMaster") || contentype
										.contains("postmaster"))) {
							MimeMessage cmsg = new MimeMessage(
									(MimeMessage) msg);
							LogUtils.e("QQ_MAIL_RETURN=======cmsg.getContent() : "
									+ cmsg.getContent().toString());
						} else {
							Object object = msg.getContent();
							if (object instanceof String) {
								LogUtils.e("=======object.toString() : "
										+ object.toString());
							} else {
								if (pmm.isContainAttach((Part) message[i])) {
									LogUtils.e("IMAPFolder fileName "
											+ pmm.getappfileName1(uid));
									if (pmm.getappfileName1(uid).contains(
											".xml")) {

											String subject = pmm.getSubject();
											if(subject.contains("工作汇报_")){
												addNotifications("工作汇报");
											}else if(subject.contains("部门预算_")){
												addNotifications("部门预算");
											}else if(subject.contains("预算执行_")){
												addNotifications("预算执行");
											}else if(subject.contains("工作报告_")){
												addNotifications("工作报告");
											}else if(subject.contains("依法行政_")){
												addNotifications("依法行政");
											}
									}
								}
							}
						}
					}
				}
			}
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	private void addNotifications(String str) {
		boolean addNotifications = emailSetting.getBoolean("addNotifications", false);
		if(addNotifications){
			return;
		}
		// 获取状态通知栏管理
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// 实例化通知栏构造器NotificationCompat.Builder
		Notification.Builder mBuilder = new Notification.Builder(this);
		// 对Builder进行配置
		mBuilder.setContentTitle(str+"有新的内容，请查看")
				// 设置通知栏标题
				.setContentText("收到新的邮件")
				// 设置通知栏显示内容
				.setContentIntent(
						getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) // 设置通知栏点击意图
				// .setNumber(number) //设置通知集合的数量
				.setTicker("您有新的邮件") // 通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
				.setPriority(Notification.PRIORITY_DEFAULT) // 设置该通知优先级
				.setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
				.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
				// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
				// requires VIBRATE permission
				.setSmallIcon(R.drawable.ic_launcher);// 设置通知小ICON
		mNotificationManager.notify(R.id.exp_email_detail_1, mBuilder.build());
		SharedPreferences.Editor editor = emailSetting.edit();
		editor.putBoolean("addNotifications", true);
		editor.commit();
//		mContext.stopService(new Intent(mContext, RefreshEmailList.class));
	}

	public PendingIntent getDefalutIntent(int flags) {
		Intent intent = new Intent(mContext, EmptyActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
				intent, flags);
		SharedPreferences.Editor editor = emailSetting.edit();
		editor.putBoolean("addNotifications", false);
		editor.commit();
		return pendingIntent;
	}

	private void getEmailConfig() {
		emailSetting = LConfigUtils.getPreferences(this,
				InterfaceConst.emailSetting);
		emailUsername = emailSetting.getString(InterfaceConst.emailname, "");
		emailPssword = emailSetting.getString(InterfaceConst.emailpassword, "");
	}

	/*** 判断网络连接 */
	public static boolean checkNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifi.isConnected()) {
			return true;
		}
		NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobile != null && mobile.isConnected()) {
			return true;
		}
		return false;
	}
}
