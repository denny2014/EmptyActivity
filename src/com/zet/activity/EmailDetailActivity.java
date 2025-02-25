package com.zet.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.other.apache.commons.codec.binary.Base64;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bdkj.bdlibrary.utils.DialogUtils;
import com.bdkj.bdlibrary.utils.KeyBoardUtils;
import com.bdkj.bdlibrary.utils.StorageUtils;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.lidroid.xutils.event.OnClick;
import com.lidroid.xutils.utils.LogUtils;
import com.zet.BaseActivity;
import com.zet.Constants;
import com.zet.R;
import com.zet.adapter.EmailDetailAdapter;
import com.zet.db.SQLiteUtils;
import com.zet.email.CallOtherOpeanFile;
import com.zet.model.ReciveOneMailDB;
import com.zet.parser.ATTACH;
import com.zet.parser.DETAIL;
import com.zet.parser.RSSFeed;
import com.zet.pull.PullableListView;
import com.zet.utils.Utility;

@ContentView(R.layout.activity_email_detail)
public class EmailDetailActivity extends BaseActivity {
	@ViewInject(R.id.tv_title)
	private TextView tv_title;// 标题
//	@ViewInject(R.id.sjx_yj_fjr)
//	private TextView sjx_yj_fjr;// 发件人
//	@ViewInject(R.id.sjx_yj_sjr)
//	private TextView sjx_yj_sjr;// 收件人
//	@ViewInject(R.id.sjx_yj_cs)
//	private TextView sjx_yj_cs;// 抄送
//	@ViewInject(R.id.sjx_yj_fssj)
//	private TextView sjx_yj_fssj;// 发送时间
	@ViewInject(R.id.tv_title_base)
	private TextView tv_title_base;
	@ViewInject(R.id.lv_content_view)
	private LinearLayout mLinearLayout;// 附件内容
	@ViewInject(R.id.email_detail_mattachs)
	private LinearLayout email_detail_mattachs;// 附件中的附件
	@ViewInject(R.id.sjx_layout_006)
	private LinearLayout sjx_layout_006;

	private ReciveOneMailDB mReciveOneMailDB;
	public RSSFeed feed;

	private List<DETAIL> mDetails = new ArrayList<DETAIL>();
	private List<ATTACH> mAttachs = new ArrayList<ATTACH>();

	private String path;

	private String emailUsername;
	private String emailUid;
	protected ProgressDialog dialog;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mReciveOneMailDB = (ReciveOneMailDB) (savedInstanceState != null ? savedInstanceState
				.getSerializable("mReciveOneMail") : getIntent()
				.getSerializableExtra("mReciveOneMail"));
		emailUsername = (savedInstanceState != null ? savedInstanceState
				.getString("emailUsername") : getIntent().getStringExtra(
				"emailUsername"));
		if (mReciveOneMailDB == null) {
			finish();
		}
		dialog = DialogUtils.showLoading(mContext,
				mContext.getString(R.string.dialog_loading), true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		emailUid = mReciveOneMailDB.getUserid();
		sjx_layout_006.setVisibility(View.GONE);
		String mSubject = TextUtils.isEmpty(mReciveOneMailDB.getSubject()) ? "无标题文档"
				: mReciveOneMailDB.getSubject();
//		String mFrom = TextUtils.isEmpty(mReciveOneMailDB.getForm()) ? ""
//				: mReciveOneMailDB.getForm();
//		String mTo = TextUtils.isEmpty(mReciveOneMailDB.getTopeople()) ? ""
//				: mReciveOneMailDB.getTopeople();
//		String mCC = TextUtils.isEmpty(mReciveOneMailDB.getCcpeople()) ? ""
//				: mReciveOneMailDB.getCcpeople();
//		String mTime = TextUtils.isEmpty(mReciveOneMailDB.getSentDate()) ? ""
//				: mReciveOneMailDB.getSentDate();
		tv_title.setText(mSubject);
//		sjx_yj_fjr.setText(mFrom);
//		sjx_yj_sjr.setText(mTo);
//		if (TextUtils.isEmpty(mCC)) {
//			findViewById(R.id.lyout_caosong).setVisibility(View.GONE);
//		} else
//			sjx_yj_cs.setText(mCC);
//		sjx_yj_fssj.setText(mTime);

		path = StorageUtils.getStorageFile().toString()
				+ Constants.ROOT_DIRECTION + "/tmp";
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// String AppfileName = mReciveOneMailDB.getAppfileName();
				List<ATTACH> mAttachs = SQLiteUtils.getInstance(mContext)
						.getEmailAttachsFromDb(emailUsername, emailUid);
				// feed = readFile(path + "/" + AppfileName);
				mDetails = SQLiteUtils.getInstance(mContext)
						.getEmailDetailFromDb(emailUsername, emailUid);
				android.os.Message msg = mHandler.obtainMessage();
				msg.what = 1;
				msg.obj = mAttachs;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				mAttachs = (List<ATTACH>) msg.obj;
				LogUtils.e("mAttachs : " + mAttachs.toString());
				int mAttachsItemCount = mAttachs.size();
				if (mAttachsItemCount > 0) {
					sjx_layout_006.setVisibility(View.VISIBLE);
					addmAttachs(mAttachs);
				}
				if (mDetails != null && mDetails.size() > 0) {
					tv_title_base.setVisibility(View.VISIBLE);
					LayoutParams layoutParams = new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					layoutParams.setMargins(20, 15, 15, 15);
					for (int i = 0; i < mDetails.size(); i++) {
						DETAIL mDetail = mDetails.get(i);
						LayoutInflater mLayoutInflater = LayoutInflater
								.from(mContext);
						View mView = mLayoutInflater.inflate(
								R.layout.email_detail_item, null);
						TextView mTextView1 = (TextView) mView
								.findViewById(R.id.exp_email_detail_1);
						TextView mTextView3 = (TextView) mView
								.findViewById(R.id.exp_email_detail_3);
						String mName = TextUtils.isEmpty(mDetail
								.getELEMENT_NAME()) ? "" : mDetail
								.getELEMENT_NAME();
						String mContent = TextUtils.isEmpty(mDetail
								.getPROC_CONTENT()) ? "" : mDetail
								.getPROC_CONTENT();
						if(mName.contains("政���")){
							mName=mName.replace("���", "策");
						}
						if (!TextUtils.isEmpty(mName)
								&& !TextUtils.isEmpty(mContent)) {
							mTextView1.setText(mName + " : ");
							mTextView3.setText(mContent);
						}
						mLinearLayout.addView(mView, layoutParams);
					}
				}
			}
		}

	};

	private boolean decodingAttachs(List<ATTACH> mAttachs, int location) {
		String mFILE_BIN = mAttachs.get(location).getFILE_BIN();
		String fileName = mAttachs.get(location).getATTACH_ID();
		if (TextUtils.isEmpty(fileName)) {
			fileName = "" + System.currentTimeMillis();
			mAttachs.get(location).setATTACH_ID(fileName);
		}
		String fileType = mAttachs.get(location).getFILE_TYPE();
		if (!TextUtils.isEmpty(mFILE_BIN) && !TextUtils.isEmpty(fileType)) {

			// BASE64Decoder decoder = new BASE64Decoder();
			FileOutputStream write;
			try {
				File storefile = new File(path + "/" + fileName + "."
						+ fileType);
				if (storefile.exists()) {
					storefile.delete();
				}
				new File(path).mkdirs();// 新建文件夹
				storefile.createNewFile();// 新建文件
				byte[] decodeBytes = Base64.decodeBase64(mFILE_BIN);
				bytesToFile(storefile, decodeBytes);
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public static void bytesToFile(File file, byte[] bytes) throws Exception {
		BufferedOutputStream stream = null;
		try {
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(bytes);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	private void addmAttachs(List<ATTACH> mAttachs) {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(20, 15, 15, 15);
		for (int i = 0; i < mAttachs.size(); i++) {
			String name = mAttachs.get(i).getFILE_NAME();
			if(name.contains("政���急")){
				name=name.replace("���", "应");
			}
			if(name.contains("预���草案")){
				name=name.replace("���", "算");
			}
			if(name.contains("交���")){
				name=name.replace("���", "流");
			}
			final String fileType = mAttachs.get(i).getFILE_TYPE();
			final String fileName = mAttachs.get(i).getATTACH_ID();
			boolean haveAttatch = decodingAttachs(mAttachs, i);
			if (haveAttatch) {
				TextView textView = new TextView(EmailDetailActivity.this);
				textView.setTextSize(16);
				textView.setTextColor(getResources().getColor(
						android.R.color.holo_red_light));
				textView.setText(TextUtils.isEmpty(name) ? "" : name);
				textView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							File myFile = new File(path + "/" + fileName + "."
									+ fileType);
							String string = myFile.getAbsolutePath();
							if (string.endsWith(".rar")) {
							} else if (string.endsWith(".apk")) {
								return;
							} else {
								CallOtherOpeanFile.openFile(mContext, myFile);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				});
				email_detail_mattachs.addView(textView, layoutParams);
			}
		}
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@OnClick({ R.id.tv_back, R.id.tv_delete })
	public void onClick(View v) {
		KeyBoardUtils.hideKeyBoard(this);
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_delete:
			DialogUtils.showAlertDialog(mContext, "删除邮件", "确定要删除该邮件吗？", "确定",
					"取消", new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ToastUtils.show(mContext, "已删除该邮件");
							int index = SQLiteUtils.getInstance(mContext)
									.emailUpdate(mReciveOneMailDB.getUserid(),
											"emailVisual");
							LogUtils.e("index = " + index);
							if (index > 0) {
								setResult(RESULT_OK);
								finish();
							}
						}
					}, null, Gravity.LEFT | Gravity.CENTER_VERTICAL);
			break;
		}
	}
}
