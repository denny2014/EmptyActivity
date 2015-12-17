package com.zet.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bdkj.bdlibrary.utils.DialogUtils;
import com.bdkj.bdlibrary.utils.FileUtils;
import com.bdkj.bdlibrary.utils.HttpUtils;
import com.bdkj.bdlibrary.utils.KeyBoardUtils;
import com.bdkj.bdlibrary.utils.LConfigUtils;
import com.bdkj.bdlibrary.utils.SerializeUtils;
import com.bdkj.bdlibrary.utils.StorageUtils;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.jsong.android.library.api.NetParameters;
import com.jsong.android.library.api.NetRequestInterface;
import com.jsong.android.library.api.NetRequestInterfaceImp;
import com.jsong.android.library.api.NetResponseListener;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.lidroid.xutils.event.OnClick;
import com.lidroid.xutils.utils.LogUtils;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.zet.ApplicationContext;
import com.zet.BaseActivity;
import com.zet.Constants;
import com.zet.R;
import com.zet.ZetParams;
import com.zet.adapter.SearchResultAdapter;
import com.zet.asyncHandler.DocHandler;
import com.zet.db.SQLiteUtils;
import com.zet.email.ReciveOneMail;
import com.zet.model.DocInfo;
import com.zet.model.NewsInBrief;
import com.zet.model.ReciveOneMailDB;
import com.zet.model.UserInfo;
import com.zet.net.BaseNetHandler;
import com.zet.net.HandlerFactory;
import com.zet.net.INetProxy;
import com.zet.pull.PullToRefreshLayout;
import com.zet.pull.PullableListView;
import com.zet.utils.InterfaceConst;
import com.zet.utils.ParseJsonUtil;
import com.zet.utils.PdfUtils;

/**
 * 搜索列表界面 Created by macchen on 15/4/3.
 */
@ContentView(R.layout.activity_search_list)
public class SearchListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
	private Message message[] = null;
	private ReciveOneMail pmm = null;
	private String docType = null;
	private String workType = null;
	@ViewInject(R.id.lv_content_view)
	PullableListView listView;
	@ViewInject(R.id.tv_department)
	TextView tvDepartment;
	@ViewInject(R.id.tv_serarch_year)
	TextView tvYear;
	@ViewInject(R.id.iv_collect)
	ImageView iv_collect;
	@ViewInject(R.id.tv_refuse)
	private TextView tv_refuse;
	@ViewInject(R.id.et_search_number)
	EditText etNumber;
	@ViewInject(R.id.refresh_view)
	PullToRefreshLayout layout;
	@ViewInject(R.id.etKey)
	EditText etKey;
	@ViewInject(R.id.etKey_news)
	EditText etKey_news;
	@ViewInject(R.id.etKey_news_time)
	TextView etKey_new_times;
	@ViewInject(R.id.list_search_layout)
	RelativeLayout searchLayout;;
	@ViewInject(R.id.list_title_layout)
	LinearLayout titleLayout;

	@ViewInject(R.id.list_search_layout_cjjx)
	RelativeLayout list_search_layout_cjjx;
	@ViewInject(R.id.tv_serarch_year_cjxj)
	TextView tv_serarch_year_cjxj;
	@ViewInject(R.id.tv_serarch_month_cjxj)
	TextView tv_serarch_month_cjxj;
	@ViewInject(R.id.tv_serarch_day_cjxj)
	TextView tv_serarch_day_cjxj;

	@ViewInject(R.id.search_list_editview)
	LinearLayout otherLayout;
	@ViewInject(R.id.search_list_editview_news)
	LinearLayout timesLayout;

	private static final int GETEMAILRROMDB = 0x01;
	private static final int GETEMAILRROMWEB = 0x02;
	private static final int DISMISS = 0x03;
	private static final int SHOWEMAILLIST = 0x04;
	private static final int GETLOCALDATA = 0x05;

	/**
	 * 部门请求
	 */
	private final int DEPART_REQUEST = 1;

	private final int EMAIL_DELETE = 3;
	/**
	 * 年代请求
	 */
	private final int YEAR_REQUEST = 2;

	private final int MONTH_REQUEST = 7;

	private final int DAY_REQUEST = 8;

	/**
	 * 每页的条数
	 */
	private int pagesize = 50;
	private int pagesizeNew = 20;
	/**
	 * 页码
	 */
	private int page = 1;
	private boolean reload = true;
	private String departCode = "";
	private String mDepartName = "";
	private String year = "";
	private String month = "";
	private String day = "";
	private boolean isShow = false;
	/**
	 * 搜索的关键词
	 */
	private String keyWord = "";
	private String keyWord_News = "";
	private String keyWord_NewsTimes = "";

	/**
	 * 文号
	 */
	private String number = "";

	/**
	 * 是否是线上数据（表示线上,0表示本地)
	 */
	private int online = 1;// 线上

	private SearchResultAdapter adapter;

	private UserInfo userInfo;

	/**
	 * 当前是否显示的是收藏的数据
	 */
	private boolean isCollect = false;

	SharedPreferences emailSetting;
	static String emailUsername;
	static String emailPssword;

	/**
	 * 收藏切换时缓存下来的数据
	 */
	private List<Object> cacheData = new ArrayList<Object>();
	protected ProgressDialog dialog;
	private Folder folder;

	private int mTimeYear;
	private int mTimeMonth;
	private int mTimeDay;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			docType = savedInstanceState.getString("docType");
		} else {
			docType = getIntent().getStringExtra("docType");
		}
		if (docType == null)
			finish();
		layout.setOnRefreshListener(new MyListener());
		Object object = SerializeUtils.readObject(new File(getCacheDir(), "user.info"));
		if (docType.equals("11")) {
			otherLayout.setVisibility(View.GONE);
			// tvDepartment.setVisibility(View.GONE);
			searchLayout.setVisibility(View.GONE);
			timesLayout.setVisibility(View.VISIBLE);
			iv_collect.setVisibility(View.GONE);
			// 初始化Calendar日历对象
			Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
			Date mydate = new Date(); // 获取当前日期Date对象
			mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
			mTimeYear = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
			mTimeMonth = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
			mTimeDay = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天
		} else {
			list_search_layout_cjjx.setVisibility(View.GONE);
		}
		if (docType.equals("06") || docType == "06") {
			if (savedInstanceState != null) {
				workType = savedInstanceState.getString("workType");
			} else {
				workType = getIntent().getStringExtra("workType");
			}
			// 邮件系统
			getEmailConfig();
			// searchLayout.setVisibility(View.GONE);
			iv_collect.setVisibility(View.GONE);
			tv_refuse.setVisibility(View.VISIBLE);
			if (workType.equals("2") || workType.equals("3") || workType.equals("4") || workType.equals("5")) {
				etNumber.setVisibility(View.GONE);
			}
			/*
			try {
				mHandler.sendEmptyMessage(GETEMAILRROMDB);
				// refuseGetNewEmail();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			// 预先加载本地数据
			try {
				mHandler.sendEmptyMessage(GETLOCALDATA);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if (docType.equals("05") || docType == "05") {
			// 财经政策简讯
			dialog = DialogUtils.showLoading(mContext, mContext.getString(R.string.dialog_loading), true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			if (object != null) {
				AskTaskLoad(12);
			} else {
				listView.setCanPullUp(false);
				listView.setCanPullDown(false);
			}
		} else {
			if (object != null) {
				userInfo = (UserInfo) object;
				if (docType.equals("11")) {
					HttpUtils.get(mContext, Constants.SERVER_URL,
							ZetParams.getSearchList(keyWord_News, userInfo.getAdmdivCode(), userInfo.getDivCode(),
									departCode, number, year, userInfo.getUserId(), docType, pagesize + "",
									((page - 1) * pagesize) + "", online + "", docType, month, day),
							HandlerFactory.getHandler(DocHandler.class,
									new BaseNetHandler(new INetProxy(mContext, this), Constants.SEARCH_ACTION)));
				} else {
					HttpUtils.get(mContext, Constants.SERVER_URL,
							ZetParams.getSearchList(keyWord, userInfo.getAdmdivCode(), userInfo.getDivCode(),
									departCode, number, year, userInfo.getUserId(), docType, pagesize + "",
									((page - 1) * pagesize) + "", online + "", docType, "", ""),
							HandlerFactory.getHandler(DocHandler.class,
									new BaseNetHandler(new INetProxy(mContext, this), Constants.SEARCH_ACTION)));
				}
				LogUtils.e("---" + Constants.SERVER_URL
						+ ZetParams.getSearchList(keyWord, userInfo.getAdmdivCode(), userInfo.getDivCode(), departCode,
								number, year, userInfo.getUserId(), docType, pagesize + "",
								((page - 1) * pagesize) + "", online + "", docType, month, day).toString());
			} else {
				listView.setCanPullUp(false);
				listView.setCanPullDown(false);
			}
		}
		listView.setOnItemClickListener(this);
		adapter = new SearchResultAdapter(new ArrayList<DocInfo>());
		listView.setAdapter(adapter);
		if (!docType.equals("06")) {

			etNumber.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					filterDate(s.toString(), etKey.getText().toString());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			etKey.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					filterDate(etNumber.getText().toString(), s.toString());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});
		}
	}

	private void filterDate(String snumber, String skeyword) {
		// KeyBoardUtils.hideKeyBoard(SearchListActivity.this);
		keyWord = skeyword;
		number = snumber;
		if (docType.equals("05") || docType == "05") {
			ToastUtils.show(mContext, "简讯搜索");
			NewsInBriefdoRefresh(true);
		} else {
			if (userInfo != null) {
				reload();
				isShow = true;
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (dialog != null && dialog.isShowing()) {

		}
	}

	private void refuseGetNewEmail() {
		if (dialog == null) {
			dialog = DialogUtils.showLoading(mContext, mContext.getString(R.string.dialog_loading), true);
		}
		dialog.setCanceledOnTouchOutside(false);
		if (dialog != null) {
			dialog.show();
		} else {
			dialog = DialogUtils.showLoading(mContext, mContext.getString(R.string.dialog_loading), true);
			dialog.show();
		}
		page = 1;
		final boolean isFirstLogin = emailSetting.getBoolean(InterfaceConst.isFirstLogin, false);
		saveEmailMessage();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					do {
						receiverEmail(true);
					} while (reload);
					if (isFirstLogin) {
						mHandler.sendEmptyMessage(DISMISS);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mHandler.sendEmptyMessage(DISMISS);
				}
			}
		}).start();
	}

	private void AskTaskLoad(int tag) {
		// TODO Auto-generated method stub
		NetParameters parameters = new com.jsong.android.library.api.NetParameters();
		parameters.addParam(NetRequestInterface.REQUESTYPE, NetRequestInterface.REQUESTBYGET);
		parameters.addParam("url", Constants.SERVER_URL);
		parameters.addParam("action", "getmess");
		String dpartName = TextUtils.isEmpty(mDepartName) ? "" : mDepartName;
		if (!TextUtils.isEmpty(dpartName) && dpartName.equals("所有部门")) {
			dpartName = "";
		}
		parameters.addParam("depname", dpartName);
		parameters.addParam("fileno", TextUtils.isEmpty(number) ? "" : number);// fileNO1
		String mYear = TextUtils.isEmpty(year) ? "" : year;
		if (!TextUtils.isEmpty(mYear) && mYear.equals("所有年度")) {
			mYear = "";
		}
		parameters.addParam("setyear", TextUtils.isEmpty(mYear) ? "" : mYear);// sdocyear1
		parameters.addParam("eyword",
				TextUtils.isEmpty(etKey.getText().toString()) ? "" : (etKey.getText().toString()));
		parameters.addParam("rows", pagesizeNew + "");//
		parameters.addParam("debug", Constants.PARAMS_DEBUG_VALUE);
		parameters.addParam("start", ((page - 1) * pagesizeNew) + "");

		new NetRequestInterfaceImp().dorequest(parameters, new NetResponseListener() {
			@Override
			public void onException(Exception e, int tag) {
				ToastUtils.show(mContext, "网络不佳");
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				ParseJsonUtil.getInstance().showFailDialog(mContext);
			}

			@Override
			public void onComplete(Object object, int tag) {
				if (ParseJsonUtil.getInstance().parseIsSuccess(object.toString())) {
					List<NewsInBrief> list = ParseJsonUtil.getInstance().parseNewsInBriefMessage(object.toString());
					LogUtils.e(list.toString());
					if (page == 1) {
						layout.refreshFinish(PullToRefreshLayout.SUCCEED);
						adapter.getList().clear();
					} else {
						layout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					}
					adapter.getList().addAll(list);
					adapter.notifyDataSetChanged();
					listView.setSelection(pagesize * (page - 1));
					listView.setCanPullUp(list.size() >= pagesize);
					// if (list.size() < pagesize) {
					// ToastUtils.show(mContext,
					// R.string.activity_search_not_data);
					// }
					isCollect = false;
				} else {
					String errorMessage = ParseJsonUtil.getInstance().getErrorMessage(object.toString());
					if (TextUtils.isEmpty(errorMessage)) {
						ParseJsonUtil.getInstance().showFailDialog(mContext);
					} else {
						ParseJsonUtil.getInstance().createErrorDialog(mContext, errorMessage);
					}
				}
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}, mContext, tag);
	}

	private void getEmailConfig() {
		emailSetting = LConfigUtils.getPreferences(this, InterfaceConst.emailSetting);
		emailUsername = emailSetting.getString(InterfaceConst.emailname, "");
		emailPssword = emailSetting.getString(InterfaceConst.emailpassword, "");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("docType", docType);
		outState.putString("workType", workType);

	}

	public void receiverEmail(boolean isLists) {
		String[] emailUsernames = emailUsername.split("@");
		Properties props = System.getProperties();
		Session session = null;
		URLName urln = null;
		Store store = null;
		try {
			if (emailUsername.contains("@qq") || emailUsername.contains("@QQ")) {
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
				LogUtils.e("emailUsernames=" + emailUsernames[0] + "  emailPssword=" + emailPssword);
			} else {
				ToastUtils.show(mContext, "暂不支持QQ以外邮箱登陆");
				return;
			}
			folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			// List<ReciveOneMailDB> list = new ArrayList<ReciveOneMailDB>();
			reload = false;
			if (folder instanceof IMAPFolder) {
				searchUserInfoEmails(isLists);
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

	private void searchUserInfoEmails(boolean isLists) throws MessagingException, Exception, IOException {
		if (isLists) {
			emailUidLists = new ArrayList<String>();
			emailUidArrays = null;
			selected = null;
		}
		if (folder == null) {
			// ToastUtils.show(mContext, "folder==null");
			// receiverEmail(isLists);
			return;
		}
		IMAPFolder inbox = (IMAPFolder) folder;
		message = inbox.getMessages();
		for (int i = 0; i < message.length; i++) {
			MimeMessage mimeMessage = (MimeMessage) message[i];
			pmm = new ReciveOneMail((MimeMessage) message[i]);
			String uid = Long.toString(inbox.getUID(mimeMessage));
			if (!SQLiteUtils.getInstance(mContext).emailIsExist(uid, emailUsername)) {
				// 获得邮件内容
				IMAPMessage msg = (IMAPMessage) message[i];
				String contentype = pmm.getFrom();
				// LogUtils.e("contentype : " + contentype);
				if (!TextUtils.isEmpty(contentype)
						&& (contentype.contains("PostMaster") || contentype.contains("postmaster"))) {
					// MimeMessage cmsg = new MimeMessage((MimeMessage) msg);
					// LogUtils.e("QQ_MAIL_RETURN=======cmsg.getContent() : "
					// + cmsg.getContent().toString());
				} else {
					Object object = msg.getContent();
					if (object instanceof String) {
						// LogUtils.e("=======object.toString() : "
						// + object.toString());
					} else {
						if (pmm.isContainAttach((Part) message[i])) {
							LogUtils.e("IMAPFolder fileName " + pmm.getappfileName1(uid));
							if (pmm.getappfileName1(uid).contains(".xml")) {
								if (isLists) {
									String subject = pmm.getSubject();
									if (workType.equals("1") && subject.contains("工作汇报_")) {
										emailUidLists.add(uid + "@" + subject);
									} else if (workType.equals("2") && subject.contains("部门预算_")) {
										emailUidLists.add(uid + "@" + subject);
									} else if (workType.equals("3") && subject.contains("预算执行_")) {
										emailUidLists.add(uid + "@" + subject);
									} else if (workType.equals("4") && subject.contains("工作报告_")) {
										emailUidLists.add(uid + "@" + subject);
									} else if (workType.equals("5") && subject.contains("依法行政_")) {
										emailUidLists.add(uid + "@" + subject);
									}
								} else {
									if (emailUidLists != null && selected != null) {
										for (int j = 0; j < emailUidLists.size(); j++) {
											// LogUtils.d("uid : " + uid
											// + "pmm.getSubject() : "
											// + pmm.getSubject()
											// + "emailUidLists.get(j) : "
											// + emailUidLists.get(j)
											// + " selected[j] :"
											// + selected[j]);
											if (emailUidLists.get(j).contains(uid)
													&& emailUidLists.get(j).contains(pmm.getSubject())
													&& selected[j] == true) {
												// LogUtils.d("getSubject"
												// + pmm.getSubject());
												new SavaAttachAndDBAsync().execute(pmm, uid, message[i]);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (isLists && emailUidLists.size() > 0) {
			selected = new boolean[emailUidLists.size()];
			emailUidArrays = new String[emailUidLists.size()];
			for (int i = 0; i < emailUidLists.size(); i++) {
				emailUidArrays[i] = emailUidLists.get(i).split("@")[1];
				selected[i] = false;
			}
			mHandler.sendEmptyMessage(SHOWEMAILLIST);
		} else {
			if (isLists)
				mHandler.sendEmptyMessage(DISMISS);
		}
	}

	private ArrayList<String> emailUidLists = new ArrayList<String>();
	private String[] emailUidArrays;
	private boolean[] selected;
	DialogInterface.OnMultiChoiceClickListener mutiListener = new DialogInterface.OnMultiChoiceClickListener() {

		@Override
		public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
			selected[which] = isChecked;
		}
	};
	DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialogInterface, int which) {
			String selectedStr = "";
			for (int i = 0; i < selected.length; i++) {
				if (selected[i] == true) {
					selectedStr = selectedStr + " " + emailUidArrays[i];
				}
			}
			if (dialog == null) {
				dialog = DialogUtils.showLoading(mContext, mContext.getString(R.string.dialog_loading), true);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			} else {
				if (dialog != null && !dialog.isShowing()) {
					dialog.show();
				}
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						searchUserInfoEmails(false);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
	};

	private void saveToDataBaseAndList(ReciveOneMail pmm, String uid, Message message) throws Exception {
		pmm.getMailContent((Part) message);
		pmm.setDateFormat("yyyy年MM月dd日 HH:mm");
		SQLiteUtils.getInstance(mContext).addEmailToDB(pmm, pmm.isContainAttach((Part) message), uid, emailUsername,
				pmm.getappfileName(uid));
		pmm.setAttachPath(StorageUtils.getStorageFile().toString() + Constants.ROOT_DIRECTION);
		pmm.saveAttachMent(message, uid, mContext, emailUsername);
		// respondToEmail(pmm.getFrom(), pmm.getSubject());
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOWEMAILLIST:
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (!SearchListActivity.this.isFinishing())
					new AlertDialog.Builder(SearchListActivity.this).setTitle("请选择需要下载邮件")
							.setIcon(R.drawable.ic_launcher).setCancelable(false)
							.setMultiChoiceItems(emailUidArrays, selected, mutiListener)
							.setPositiveButton("确定", btnListener).setNegativeButton("取消", null).show();
				break;
			case DISMISS:
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				layout.refreshFinish(PullToRefreshLayout.SUCCEED);
				break;
			case GETEMAILRROMDB:
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				List<ReciveOneMailDB> listDB = SQLiteUtils.getInstance(mContext).getEmailFromDb(
						((page - 1) * pagesizeNew), (page * pagesizeNew) - page + 1, emailUsername,
						etKey.getText().toString(), emailUsername, workType, false);
				Log.i("DreamlistDB", "" + listDB.size());
				adapter.getList().addAll(listDB);
				adapter.notifyDataSetChanged();
				isCollect = false;
				break;
			case GETEMAILRROMWEB:
				List<ReciveOneMailDB> list = SQLiteUtils.getInstance(mContext).getEmailFromDb(
						((page - 1) * pagesizeNew), (page * pagesizeNew) - page + 1, emailUsername,
						etKey.getText().toString(), emailUsername, workType, false);
				Log.i("Dream", "" + list.size());
				layout.refreshFinish(PullToRefreshLayout.SUCCEED);
				adapter.getList().clear();
				adapter.addList(list);
				adapter.notifyDataSetChanged();
				listView.setSelection(pagesize * (page - 1));
				listView.setCanPullUp(list.size() >= pagesizeNew);
				isCollect = false;
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				break;
			case GETLOCALDATA:
				dialog = DialogUtils.showLoading(mContext, mContext.getString(R.string.dialog_loading), true);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				LoadLocalDataTask dataTask = new LoadLocalDataTask();
				dataTask.execute(0);
				break;
			}
		};
	};

	@Override
	public boolean start(String type, Object objects) {
		if (isShow) {
			isShow = false;
			return true;
		}
		return false;
	};

	/**
	 * 重新加载
	 */
	public void reload() {
		page = 1;
		adapter.getList().clear();
		adapter.notifyDataSetChanged();
		listView.setCanPullDown(true);
		if (docType.equals("11")) {
			HttpUtils.get(mContext, Constants.SERVER_URL,
					ZetParams.getSearchList(keyWord_News, userInfo.getAdmdivCode(), userInfo.getDivCode(), departCode,
							number, year, userInfo.getUserId(), docType, pagesize + "", ((page - 1) * pagesize) + "",
							online + "", docType, month, day),
					HandlerFactory.getHandler(DocHandler.class, new BaseNetHandler(
							new INetProxy(mContext, SearchListActivity.this), Constants.SEARCH_ACTION)));
		} else {
			HttpUtils.get(mContext, Constants.SERVER_URL,
					ZetParams.getSearchList(keyWord, userInfo.getAdmdivCode(), userInfo.getDivCode(), departCode,
							number, year, userInfo.getUserId(), docType, pagesize + "", ((page - 1) * pagesize) + "",
							online + "", docType, "", ""),
					HandlerFactory.getHandler(DocHandler.class, new BaseNetHandler(
							new INetProxy(mContext, SearchListActivity.this), Constants.SEARCH_ACTION)));
		}
	}

	@OnClick({ R.id.iv_collect, R.id.iv_help, R.id.tv_back, R.id.tv_department, R.id.tv_serarch_year, R.id.iv_goto_top,
			R.id.tv_refuse, R.id.btn_search, R.id.etKey_news_time, R.id.btn_search_news, R.id.tv_serarch_year_cjxj,
			R.id.tv_serarch_month_cjxj, R.id.tv_serarch_day_cjxj })
	public void onClick(View v) {
		KeyBoardUtils.hideKeyBoard(this);
		switch (v.getId()) {
		case R.id.tv_refuse:// 更新邮件
			if (docType.equals("06") || docType == "06") {
				// 邮件处理
				reload = true;
				refuseGetNewEmail();
			}
			break;
		case R.id.iv_collect:
			if (isCollect)
				return;
			if (docType.equals("06") || docType == "06") {
				// 邮件处理
				ToastUtils.show(mContext, "邮件收藏");
			} else if (docType.equals("05") || docType == "05") {
				Toast.makeText(mContext, " 简讯收藏", Toast.LENGTH_LONG).show();
			} else {
				List<DocInfo> list = SQLiteUtils.getInstance(mContext).getCollect();
				findViewById(R.id.title_bar_img).setVisibility(View.GONE);
				findViewById(R.id.search_list_editview).setVisibility(View.GONE);
				findViewById(R.id.list_search_layout).setVisibility(View.GONE);
				findViewById(R.id.iv_collect).setVisibility(View.GONE);
				findViewById(R.id.iv_help).setVisibility(View.GONE);
				findViewById(R.id.list_search_layout_cjjx).setVisibility(View.GONE);
				findViewById(R.id.search_list_editview_news).setVisibility(View.GONE);
				((TextView) findViewById(R.id.title_bar_text)).setText("收藏夹");
				cacheData = adapter.getList();
				adapter.setList(list);
				listView.setCanPullUp(false);
				listView.setCanPullDown(false);
				adapter.notifyDataSetChanged();
				isCollect = true;
			}
			break;
		case R.id.iv_help:
			if (!StorageUtils.isMount()) {
				ToastUtils.showWarn(mContext, R.string.activity_sdcard_not_mount);
			} else {
				File file = new File(StorageUtils.getStorageFile(), Constants.ROOT_DIRECTION + "/userGuide.pdf");
				if (!file.exists()) {
					boolean success = false;
					FileUtils.createFolder(file.getParentFile());
					FileUtils.createFile(file);
					InputStream stream = null;
					try {
						stream = mContext.getAssets().open("userGuide.pdf");
						FileUtils.writeFile(file, stream);
						success = true;
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (stream != null) {
							try {
								stream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					if (success) {
						PdfUtils.openPDF(mContext, file.getAbsolutePath(), false);
					} else {
						ToastUtils.showError(mContext, R.string.open_pdf_fail);
						FileUtils.deleteFile(file);
					}
				} else {
					PdfUtils.openPDF(mContext, file.getAbsolutePath(), false);
				}
			}
			break;
		case R.id.tv_back:
			if (isCollect) {
				findViewById(R.id.title_bar_img).setVisibility(View.VISIBLE);
				findViewById(R.id.search_list_editview).setVisibility(View.VISIBLE);
				findViewById(R.id.list_search_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.iv_collect).setVisibility(View.VISIBLE);
				findViewById(R.id.iv_help).setVisibility(View.VISIBLE);
				findViewById(R.id.title_bar_text).setVisibility(View.GONE);
				adapter.setList(cacheData);
				adapter.notifyDataSetChanged();
				listView.setCanPullDown(true);
				listView.setCanPullUp(true);
				isCollect = false;
			} else {
				finish();
			}
			break;
		case R.id.tv_department:
			Bundle departBunld = new Bundle();
			departBunld.putInt("selectItem", 0);
			if (docType.equals("06")) {
				departBunld.putBoolean("IsFromEmail", true);
			} else {
				departBunld.putBoolean("IsFromEmail", false);
			}
			departBunld.putString("workType", workType);

			departBunld.putString("docType", docType);
			ApplicationContext.showSelect(mContext, departBunld, DEPART_REQUEST);
			break;
		case R.id.tv_serarch_year_cjxj:
		case R.id.tv_serarch_year:
			Bundle yearBunld = new Bundle();
			yearBunld.putInt("selectItem", 1);
			if (docType.equals("06")) {
				yearBunld.putBoolean("IsFromEmail", true);
			} else {
				yearBunld.putBoolean("IsFromEmail", false);
			}
			yearBunld.putString("docType", docType);
			yearBunld.putString("workType", workType);
			ApplicationContext.showSelect(mContext, yearBunld, YEAR_REQUEST);
			break;
		case R.id.tv_serarch_month_cjxj:
			Bundle monthBunld = new Bundle();
			monthBunld.putInt("select_type", 0);
			ApplicationContext.showSelectMonthAndDay(mContext, monthBunld, MONTH_REQUEST);
			break;
		case R.id.tv_serarch_day_cjxj:
			Bundle dayBunld = new Bundle();
			dayBunld.putInt("select_type", 1);
			ApplicationContext.showSelectMonthAndDay(mContext, dayBunld, DAY_REQUEST);
			break;
		case R.id.iv_goto_top:
			listView.setSelection(0);
			break;
		case R.id.etKey_news_time:
			// 创建DatePickerDialog对象

			DatePickerDialog dpd = new DatePickerDialog(SearchListActivity.this, Datelistener, mTimeYear, mTimeMonth,
					mTimeDay);
			dpd.show();// 显示DatePickerDialog组件
			break;
		case R.id.btn_search_news:
			keyWord_News = etKey_news.getText().toString();
			// keyWord_NewsTimes =
			// etKey_new_times.getText().toString().replace("-", "");
			KeyBoardUtils.hideKeyBoard(SearchListActivity.this);
			if (userInfo != null) {
				reload();
			}
			break;
		case R.id.btn_search:
			String snumber = etNumber.getText().toString();
			String skeyword = etKey.getText().toString();
			KeyBoardUtils.hideKeyBoard(SearchListActivity.this);
			keyWord = skeyword;
			number = snumber;
			if (docType.equals("06") || docType == "06") {
				// 邮件处理
				if (!TextUtils.isEmpty(keyWord) && TextUtils.isEmpty(number)) {
					EmaildoRefresh(true, keyWord, false, false);
				} else if (!TextUtils.isEmpty(number) && TextUtils.isEmpty(keyWord)) {
					EmaildoRefresh(true, number, false, false);
				} else if (!TextUtils.isEmpty(keyWord) && !TextUtils.isEmpty(number)) {
					EmaildoRefresh(true, keyWord + "@&%" + number, false, false);
				} else {
					EmaildoRefresh(true, "", false, false);
				}
			}
			break;
		}
	}

	private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
		/**
		 * params：view：该事件关联的组件 params：myyear：当前选择的年 params：monthOfYear：当前选择的月
		 * params：dayOfMonth：当前选择的日
		 */

		@Override
		public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
			// 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
			mTimeYear = myyear;
			mTimeMonth = monthOfYear;
			mTimeDay = dayOfMonth;
			// 更新日期
			updateDate();
		}

		// 当DatePickerDialog关闭时，更新日期显示
		private void updateDate() {
			// 在TextView上显示日期
			String mTimeMonthStr = "" + (mTimeMonth + 1);
			String mTimeDayStr = "" + mTimeDay;
			if ((mTimeMonth + 1) < 10) {
				mTimeMonthStr = "0" + (mTimeMonth + 1);
			}
			if (mTimeDay < 10) {
				mTimeDayStr = "0" + mTimeDay;
			}
			etKey_new_times.setText(mTimeYear + "-" + mTimeMonthStr + "-" + mTimeDayStr);
		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isCollect) {
				findViewById(R.id.title_bar_img).setVisibility(View.VISIBLE);
				if (!docType.equals("11")) {
					findViewById(R.id.search_list_editview).setVisibility(View.VISIBLE);
					findViewById(R.id.list_search_layout).setVisibility(View.VISIBLE);
				} else {
					findViewById(R.id.list_search_layout_cjjx).setVisibility(View.VISIBLE);
					findViewById(R.id.search_list_editview_news).setVisibility(View.VISIBLE);
				}
				findViewById(R.id.iv_collect).setVisibility(View.VISIBLE);
				findViewById(R.id.iv_help).setVisibility(View.VISIBLE);
				findViewById(R.id.title_bar_text).setVisibility(View.GONE);

				adapter.setList(cacheData);
				adapter.notifyDataSetChanged();
				listView.setCanPullDown(true);
				listView.setCanPullUp(true);
				isCollect = false;
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case EMAIL_DELETE:
				LogUtils.e("EMAIL_DELETE");
				// reload = true;
				// refuseGetNewEmail();
				page = 1;
				mHandler.sendEmptyMessage(GETEMAILRROMWEB);
				break;
			case DEPART_REQUEST:
				String departName = data.getStringExtra("depart_name");
				String redepartCode = data.getStringExtra("depart_code");
				tvDepartment.setText(departName);
				mDepartName = departName;
				departCode = redepartCode;
				if (docType.equals("06") || docType == "06") {
					if (mDepartName.equals("所有部门")) {
						// Add by tanghf
						tvYear.setText("所有年度");
						year = "所有年度";
						EmaildoRefresh(true, "所有部门年度", false, true);
					} else {
						EmaildoRefresh(true, mDepartName, false, true);
					}
					// NewsInBriefdoRefresh(true);
				} else {
					if (userInfo != null) {
						reload();
					}
				}
				break;
			case YEAR_REQUEST:
				String mYear = data.getStringExtra("year");
				if (getString(R.string.activity_search_all_year).equals(mYear)
						|| getString(R.string.activity_search_all_year_xwjx).equals(mYear)) {
					year = "";
					if (docType.equals("11")) {
						tv_serarch_year_cjxj.setText("年度");
					} else {
						tv_serarch_year_cjxj.setText(mYear);
					}
				} else {
					year = mYear;
					tv_serarch_year_cjxj.setText(mYear);
				}
				tvYear.setText(mYear);

				if (docType.equals("06") || docType == "06") {
					if (mYear.equals("所有年度")) {
						tvDepartment.setText("所有部门");
						mDepartName = "所有部门";
						EmaildoRefresh(true, "所有部门年度", false, true);
					} else {
						EmaildoRefresh(true, mYear, false, true);
					}
					// NewsInBriefdoRefresh(true);
				} else {
					if (userInfo != null) {
						reload();
					}
				}
				break;
			case MONTH_REQUEST:
				String value = data.getStringExtra("value");
				if (getString(R.string.activity_search_all_month).equals(value)) {
					month = "";
					tv_serarch_month_cjxj.setText("月份");
				} else {
					month = value;
					tv_serarch_month_cjxj.setText(value);
				}

				if (userInfo != null) {
					reload();
				}
				break;
			case DAY_REQUEST:
				String value2 = data.getStringExtra("value");
				if (getString(R.string.activity_search_all_day).equals(value2)) {
					day = "";
					tv_serarch_day_cjxj.setText("日期");
				} else {
					day = value2;
					tv_serarch_day_cjxj.setText(value2);
				}

				if (userInfo != null) {
					reload();
				}
				break;
			}
		}
	}

	@Override
	public boolean success(String type, Object objects) {
		Object[] data = (Object[]) objects;
		System.out.println(data[1]);
		if (type.equals(Constants.SEARCH_ACTION)) {
			List<DocInfo> list = (List<DocInfo>) data[1];
			if (page == 1) {
				layout.refreshFinish(PullToRefreshLayout.SUCCEED);
				adapter.getList().clear();
			} else {
				layout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
			adapter.getList().addAll(list);
			adapter.notifyDataSetChanged();
			listView.setSelection(pagesize * (page - 1));
			listView.setCanPullUp(list.size() >= pagesize);
			isCollect = false;
		}
		return super.success(type, objects);
	}

	@Override
	public boolean failure(String type, Object objects) {
		if (page == 1) {
			layout.refreshFinish(PullToRefreshLayout.FAIL);
		} else {
			layout.loadmoreFinish(PullToRefreshLayout.FAIL);
		}
		return super.failure(type, objects);
	}

	@Override
	public boolean dataFailure(String type, Object objects) {
		if (type.equals(Constants.SEARCH_ACTION)) {
			if (page == 1) {
				layout.refreshFinish(PullToRefreshLayout.FAIL);
			} else {
				layout.loadmoreFinish(PullToRefreshLayout.FAIL);
			}

		}
		return super.dataFailure(type, objects);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (docType.equals("06") || docType == "06") {
			// 邮件处理
			ReciveOneMailDB mReciveOneMail = (ReciveOneMailDB) adapter.getItem(position);
			Bundle mBundle = new Bundle();
			mBundle.putString("emailUsername", emailUsername);
			mBundle.putSerializable("mReciveOneMail", mReciveOneMail);
			ApplicationContext.showEmailDetail(this, mBundle, EMAIL_DELETE);
			SQLiteUtils.getInstance(mContext).emailUpdate(mReciveOneMail.getUserid(), "hasRead");
		} else if (docType.equals("05") || docType == "05") {
			ToastUtils.show(mContext, "简讯详情");
			NewsInBrief mNewsInBrief = (NewsInBrief) adapter.getItem(position);
			Bundle mBundle = new Bundle();
			mBundle.putString("docType", docType);
			mBundle.putSerializable("docInfo", mNewsInBrief);
			ApplicationContext.showDetail(this, mBundle);
			mNewsInBrief.setIsLook(true);
		} else {
			DocInfo info = (DocInfo) adapter.getItem(position);
			Bundle bundle = new Bundle();
			bundle.putString("docType", docType);
			bundle.putSerializable("docInfo", info);
			ApplicationContext.showDetail(this, bundle);
			info.setIsLook(true);
			// adapter.notifyDataSetChanged();
		}
		SearchResultAdapter.ViewHolder holder = (SearchResultAdapter.ViewHolder) view.getTag();
		holder.tvName.setTextColor(Color.GRAY);

	}

	class MyListener implements PullToRefreshLayout.OnRefreshListener {

		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
			// 下拉刷新操作
			if (docType.equals("06") || docType == "06") {
				// 邮件处理
				reload = true;
				refuseGetNewEmail();
			} else if (docType.equals("05") || docType == "05") {
				Toast.makeText(mContext, " 简讯下拉刷新操作", Toast.LENGTH_LONG).show();
				NewsInBriefdoRefresh(true);
			} else {
				doRefresh(true);
			}
		}

		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
			// 加载操作
			if (docType.equals("06") || docType == "06") {
				// 邮件处理
				Toast.makeText(mContext, "邮件处理加载操作", Toast.LENGTH_LONG).show();
				EmaildoRefresh(false, "", true, false);
			} else if (docType.equals("05") || docType == "05") {
				Toast.makeText(mContext, " 简讯下拉刷新操作", Toast.LENGTH_LONG).show();
				NewsInBriefdoRefresh(false);
			} else {
				doRefresh(false);
			}
		}

	}

	private void NewsInBriefdoRefresh(boolean isRefresh) {
		// TODO Auto-generated method stub
		if (isRefresh)
			page = 1;
		else
			page++;
		if (dialog != null) {
			dialog.show();
		}
		AskTaskLoad(12);
	}

	private void EmaildoRefresh(boolean isRefresh, String keyWords, boolean isShow, boolean isYear) {
		// TODO Auto-generated method stub
		if (isRefresh)
			page = 1;
		else
			page++;
		if (dialog != null && isShow) {
			dialog.show();
		}
		
		// Add by tanghf
		if (isYear) {
			LogUtils.d("EmaildoRefresh called: mDepartName="+mDepartName+" year="+year);
			keyWords = mDepartName + "@&%" + year;
		}
		
		List<ReciveOneMailDB> list;
		list = SQLiteUtils.getInstance(mContext).getEmailFromDb(((page - 1) * pagesizeNew),
				(page * pagesizeNew) - page + 1, emailUsername, keyWords, emailUsername, workType, isYear);
		layout.refreshFinish(PullToRefreshLayout.SUCCEED);
		if (!TextUtils.isEmpty(keyWords)) {
			adapter.getList().clear();
			if (list.size() == 0) {
				ToastUtils.show(mContext, mContext.getString(R.string.serach_email_list));
			} else {
				adapter.setList(list);
			}
		} else {
			adapter.setList(list);
		}
		adapter.notifyDataSetChanged();
		listView.setSelection(pagesize * (page - 1));
		listView.setCanPullUp(list.size() >= pagesizeNew);
		isCollect = false;
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public void doRefresh(boolean isRefresh) {
		if (isRefresh)
			page = 1;
		else
			page++;
		if (userInfo != null) {
			HttpUtils.get(mContext, Constants.SERVER_URL,
					ZetParams.getSearchList(keyWord, userInfo.getAdmdivCode(), userInfo.getDivCode(), departCode,
							number, year, userInfo.getUserId(), docType, pagesize + "", ((page - 1) * pagesize) + "",
							online + "", docType, month, day),
					HandlerFactory.getHandler(DocHandler.class, new BaseNetHandler(this, Constants.SEARCH_ACTION)));
		}
	}

	// private void respondToEmail(final String toAddress, final String
	// mSubject) {
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// try {
	// String newAddress = toAddress.substring(
	// toAddress.indexOf("<") + 1,
	// toAddress.lastIndexOf(">"));
	// EmailSender sender = new EmailSender();
	// // 设置服务器地址和端口
	// sender.setProperties("smtp.qq.com", "25");
	// // 分别设置发件人，邮件标题和文本内容
	// sender.setMessage(emailUsername, "回复:" + mSubject, "邮件已收到~");
	// // 设置收件人
	// sender.setReceiver(new String[] { newAddress });
	// // 添加附件
	// // sender.addAttachment("/default.prop");
	// // 发送邮件
	// sender.sendEmail("smtp.qq.com", emailUsername, emailPssword);
	// } catch (AddressException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (MessagingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }).start();
	// }

	class SavaAttachAndDBAsync extends AsyncTask<Object, Object, Object> {
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try {
				saveToDataBaseAndList((ReciveOneMail) params[0], (String) params[1], (Message) params[2]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Object result) {
			mHandler.sendEmptyMessage(GETEMAILRROMWEB);
		};
	};

	private void saveEmailMessage() {
		SharedPreferences.Editor editor = emailSetting.edit();
		editor.putBoolean(InterfaceConst.isFirstLogin, true);
		editor.commit();
	}
	
	/**
	 * 加载本地数据(xml)
	 * @author tanghf
	 *
	 */
	class LoadLocalDataTask extends AsyncTask<Integer, Integer, Integer> {

		private ArrayList<String> pathList = null;
		
		public LoadLocalDataTask() {
			super();
			pathList = new ArrayList<String>();
			loadXmlFile();
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {

			for (int i = 0; i < pathList.size(); i++) {
				String path = pathList.get(i);
				File xmlFile = new File(path);
				try {
					FileInputStream fio = new FileInputStream(xmlFile);
					// 为了访问saveFile
					ReciveOneMail rom = new ReciveOneMail();
					// uid由inbox.getUID(mimeMessage)获得，本地文件不存在
					// 将有联网获取的时候更新
					rom.saveFile(path, fio, "", mContext, emailUsername);
				} catch (Exception e) {
					LogUtils.e("LoadLocalDataTask occur exception");
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			try {
				// 刷新UI
				mHandler.sendEmptyMessage(GETEMAILRROMDB);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void loadXmlFile() {
			File file = new File(StorageUtils.getStorageFile() + Constants.ROOT_DIRECTION);
			if (file.exists()) {
				File[] files = file.listFiles();
				for (File f : files) {
					if (f.isFile() && f.canRead()) {
						String path  = f.getAbsolutePath();
						if (!path.endsWith(".xml"))
							continue;
						pathList.add(path);
					}
				}
			}
		}
	}
}