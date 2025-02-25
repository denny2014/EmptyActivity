package com.zet.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bdkj.bdlibrary.utils.DialogUtils;
import com.bdkj.bdlibrary.utils.FileUtils;
import com.bdkj.bdlibrary.utils.HttpUtils;
import com.bdkj.bdlibrary.utils.IntentUtils;
import com.bdkj.bdlibrary.utils.LConfigUtils;
import com.bdkj.bdlibrary.utils.PackageUtils;
import com.bdkj.bdlibrary.utils.SerializeUtils;
import com.bdkj.bdlibrary.utils.StorageUtils;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.bdkj.bdlibrary.utils.WindowUtils;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.lidroid.xutils.event.OnClick;
import com.lidroid.xutils.utils.LogUtils;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.zet.BaseActivity;
import com.zet.Constants;
import com.zet.R;
import com.zet.ZetParams;
import com.zet.asyncHandler.DetailHandler;
import com.zet.db.SQLiteUtils;
import com.zet.model.DetailInfo;
import com.zet.model.DocInfo;
import com.zet.model.FileInfo;
import com.zet.model.TiYaoInfo;
import com.zet.model.UserInfo;
import com.zet.net.BaseNetHandler;
import com.zet.net.HandlerFactory;
import com.zet.net.INetProxy;
import com.zet.utils.PdfUtils;
import com.zet.view.EnhanceWordWrapTextView;

/**
 * 信息详情界面 Created by macchen on 15/4/3.
 */
@ContentView(R.layout.activity_info_detail)
public class InfoDetailActivity extends BaseActivity implements DialogInterface.OnCancelListener {

	@ViewInject(R.id.iv_collect)
	ImageView ivCollect;
	@ViewInject(R.id.tv_doc_title)
	TextView tvDocTitlte;
	@ViewInject(R.id.llt_tiyao_content)
	LinearLayout lltTiYao;
	@ViewInject(R.id.llt_fujian_content)
	LinearLayout lltFujian;
	@ViewInject(R.id.llt_relative_content)
	LinearLayout lltRelative;
	@ViewInject(R.id.web_view)
	private WebView web_view;
	@ViewInject(R.id.layout_zhengwen)
	LinearLayout layout_zhengwen;

	private DocInfo info = null;
	private boolean isCancel;
	private String newFilePath;
	/**
	 * 下载对话框
	 */
	private Dialog downloadDialog;

	/**
	 * 下载进度条
	 */
	private ProgressBar progressBar;

	private TextView tvTitle;

	/**
	 * 进度
	 */
	private int progress;

	private int userId;

	private DetailInfo detailInfo = null;
	private String docType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		docType = getIntent().getStringExtra("docType");
		info = (DocInfo) (savedInstanceState != null ? savedInstanceState.getSerializable("docInfo") : getIntent().getSerializableExtra("docInfo"));
		if (info == null)
			finish();
		if (docType.equals("11")) {
			layout_zhengwen.setVisibility(View.GONE);
		}
		findViewById(R.id.iv_help).setVisibility(View.GONE);
		// findViewById(R.id.iv_collect).setVisibility(View.GONE);
		// findViewById(R.id.tv_collect).setVisibility(View.VISIBLE);
		boolean isCollect = SQLiteUtils.getInstance(mContext).isCollect(info.getFileId());
		// tvCollect.setText(isCollect ? R.string.activity_detail_cancel_collect
		// : R.string.activity_detail_collect);
		ivCollect.setTag(isCollect);
		Object object = SerializeUtils.readObject(new File(getCacheDir(), "user.info"));
		if (object != null) {
			UserInfo userInfo = (UserInfo) object;
			userId = userInfo.getUserId();
			// String url = Constants.SERVER_URL + "?" +
			// ZetParams.getDetail(info.getFileId(), info.getFileNo(),
			// info.getSetYear(), userId).toString();
			// LogUtils.e("----" + url);
			HttpUtils.get(mContext, Constants.SERVER_URL, ZetParams.getDetail(info.getFileId(), info.getFileNo(), info.getSetYear(), userId), HandlerFactory.getHandler(DetailHandler.class, new BaseNetHandler(new INetProxy(mContext, this, true), Constants.DETAIL_ACTION)));
			initWebView();
		} else {
			finish();
		}

		initView();
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		// WebView加载web资源
		String myUrl = getStrUrl();// "http://121.43.231.19/api/show.php?action=getdescbyfileid&doctype=1&fileid={20C181C3-EA21-4BD2-A9C9-2D5C34C4BB81}&fileno=%E8%B4%A2%E7%BB%BC[2011]43%E5%8F%B7&setyear=2014&userid=1";
		LogUtils.e("myUrl : " + myUrl);
		web_view.loadUrl(myUrl);
		WebSettings webSettings = web_view.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setDomStorageEnabled(true);
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		web_view.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
		web_view.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if (newProgress == 100) {
					// 网页加载完成

				} else {
					// 加载中

				}

			}
		});
	}

	private String getStrUrl() {
		// TODO Auto-generated method stub
		if (docType.equals("11")) {
			return info.getFileName();
//			return "http://economy.caixin.com/2015-10-13/100862435.html";
		}
		String url = "http://121.43.231.19/api/show.php?action=getdescbyfileid";
		if (!TextUtils.isEmpty(info.getFileId())) {
			url += "&fileid=" + info.getFileId();
		}
		if (!TextUtils.isEmpty(info.getFileNo())) {
			try {
				url += "&fileno=" + URLEncoder.encode(info.getFileNo(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!TextUtils.isEmpty(info.getSetYear())) {
			url += "&setyear=" + info.getSetYear();
		}
		url += "&userid=" + userId;
		return url;
	}

	private void initView() {
		boldText(tvDocTitlte);
		((TextView) findViewById(R.id.tv_body_content)).setText(info.getTitle());
	}

	private void boldText(TextView textView) {
		textView.getPaint().setFakeBoldText(true);
	}

	private void initDialog(String title) {
		if (downloadDialog == null) {
			View view = LayoutInflater.from(mContext).inflate(com.bdkj.library.R.layout.softupdate_progress, null);
			progressBar = (ProgressBar) view.findViewById(com.bdkj.library.R.id.update_progress);
			progressBar.setMax(100);
			downloadDialog = new Dialog(mContext, com.bdkj.library.R.style.my_dialog);
			downloadDialog.setContentView(view);
			tvTitle = (TextView) view.findViewById(com.bdkj.library.R.id.tv_dialog_title);
			downloadDialog.setCancelable(true);
			downloadDialog.setCanceledOnTouchOutside(false);
			downloadDialog.setOnCancelListener(this);
		}
		tvTitle.setText(title);
		progressBar.setProgress(0);
		downloadDialog.show();
	}

	@Override
	public void finish() {
		super.finish();
		isCancel = true;
	}

	@OnClick({ R.id.tv_back, R.id.iv_collect, R.id.tv_body_content })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.iv_collect:
			boolean isCollect = (Boolean) v.getTag();
			if (isCollect) {
				SQLiteUtils.getInstance(mContext).delCollect(info.getFileId());
				ToastUtils.show(mContext, "删除收藏成功");
			} else {
				SQLiteUtils.getInstance(mContext).addCollect(info);
				ToastUtils.show(mContext, "收藏成功");
			}
			isCollect = !isCollect;
			// tvCollect.setText(isCollect ?
			// R.string.activity_detail_cancel_collect :
			// R.string.activity_detail_collect);
			ivCollect.setTag(isCollect);
			break;
		case R.id.tv_body_content: // 点击正文
			lookFile(info.getFileName(), info.getFileSize());
			break;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("docInfo", info);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		HttpUtils.getClient().cancelRequests(mContext, true);
		if (progressBar != null) {
			File saveFile = (File) progressBar.getTag();
			FileUtils.deleteFile(saveFile);
		}
	}

	@Override
	public boolean success(String type, Object objects) {
		if (type.equals(Constants.DETAIL_ACTION)) {
			if (isCancel)
				return super.success(type, objects);
			Object[] data = (Object[]) objects;
			detailInfo = (DetailInfo) data[1];
			tvDocTitlte.setText(detailInfo.getTitle());
			LayoutInflater inflater = LayoutInflater.from(mContext);
			int paddingLeft = (int) getResources().getDimension(R.dimen.activity_detail_item_content_padding_left);
			Paint paint = new Paint();
			paint.setTextSize(getResources().getDimension(R.dimen.text_mediumn_small));
			float fontWidth = paint.measureText("中");
			int maxFontLength = (int) (WindowUtils.getWidth(mContext) / fontWidth);
			if (detailInfo.getTiyao() != null) {
				for (TiYaoInfo info : detailInfo.getTiyao()) {
					View view = inflater.inflate(R.layout.item_detail_tiyao, null);
					TextView tvName = (TextView) view.findViewById(R.id.tv_tiyao_name);
					EnhanceWordWrapTextView tvContent = (EnhanceWordWrapTextView) view.findViewById(R.id.tv_tiyao_content);
					tvName.setText(info.getName());
					tvContent.setText(info.getContent());
					boldText(tvName);
					lltTiYao.addView(view);
					if (info.getContent().length() > maxFontLength) {
						((LinearLayout) view).setOrientation(LinearLayout.VERTICAL);
						tvContent.setPadding(paddingLeft, 0, 0, 0);
					}
				}
			} else {
				lltTiYao.setVisibility(View.GONE);
			}
			if (detailInfo.getFujian() != null && detailInfo.getFujian().size() > 0) {
				int paddingTop = (int) getResources().getDimension(R.dimen.activity_detail_item_margin_top);
				int index = 0;
				for (FileInfo info : detailInfo.getFujian()) {
					index++;
					TextView textView = new TextView(mContext);
					textView.setText(index + "." + info.getTitle());
					textView.setTextColor(Color.RED);
					textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_mediumn_small));
					textView.setPadding(0, paddingTop, 0, 0);
					lltFujian.addView(textView);
					textView.setTag(info);
					textView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							FileInfo fileInfo = (FileInfo) v.getTag();
							lookFile(fileInfo.getFilename(), fileInfo.getFilesize());
						}
					});
				}
			} else {
				((View) lltFujian.getParent()).setVisibility(View.GONE);
			}
			if (detailInfo.getRelative() != null && detailInfo.getRelative().size() > 0) {
				int paddingTop = (int) getResources().getDimension(R.dimen.activity_detail_item_margin_top);
				int index = 0;
				for (DocInfo info : detailInfo.getRelative()) {
					index++;
					TextView textView = new TextView(mContext);
					textView.setText(index + "." + info.getFileTitle());
					textView.setTextColor(Color.RED);
					textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_mediumn_small));
					textView.setPadding(0, paddingTop, 0, 0);
					lltRelative.addView(textView);
					textView.setTag(info);
					textView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							DocInfo docInfo = (DocInfo) v.getTag();
							Bundle bundle = new Bundle();
							bundle.putSerializable("docInfo", docInfo);
							IntentUtils.launcher(mContext, InfoDetailActivity.class, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT, bundle);
						}
					});
				}
			} else {
				((View) lltRelative.getParent()).setVisibility(View.GONE);
			}
			findViewById(R.id.scrollview).setVisibility(View.VISIBLE);
		}
		return super.success(type, objects);
	}

	private void lookFile(String fileName, long filesize) {
		if (TextUtils.isEmpty(fileName) || "null".equals(fileName)) {
			ToastUtils.show(mContext, R.string.activity_detail_resource_not_exist);
		} else if (!StorageUtils.isMount()) {
			ToastUtils.showError(mContext, R.string.activity_sdcard_not_mount);
		} else if (StorageUtils.getUsableVolumn() < filesize) {
			ToastUtils.showError(mContext, R.string.activity_sdcard_usable_vol_unEnough);
		} else {
			File file = Constants.findFile(fileName);
			if (file != null) {
				if (file.length() != filesize) {
					FileUtils.deleteFile(file);
					File dir = Constants.findViableSaveDir();
					if (dir != null) {
						FileUtils.createFolder(dir);
						file = new File(dir, fileName);
						download(file, Constants.getDownloadUrl(fileName), filesize);
					} else {
						ToastUtils.showError(mContext, R.string.activity_sdcard_file_max);
					}
					return;
				}
				openPDF(mContext, file);
			} else {
				File dir = Constants.findViableSaveDir();
				if (dir != null) {
					FileUtils.createFolder(dir);
					file = new File(dir, fileName);
					download(file, Constants.getDownloadUrl(fileName), filesize);
				} else {
					ToastUtils.showError(mContext, R.string.activity_sdcard_file_max);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (!TextUtils.isEmpty(newFilePath)) {
			File deletenewFile = new File(newFilePath);
			if (deletenewFile.exists()) {
				deletenewFile.delete();
			}
		}
	}

	private void openPDF(Context context, File file) {
		String versionName = PdfUtils.isInstallWPS(context);
		String localVersion = LConfigUtils.getString(context, "appconfig.wpsVer", PdfUtils.WPS_DEFAULT_VERSION);
		boolean isDownload = false;
		File apkFile = new File(StorageUtils.getStorageFile(), Constants.ROOT_DIRECTION + "/" + localVersion + "_" + PdfUtils.ASSERT_WPS_NAME);
		if (versionName == null) {
			if (!apkFile.exists()) {
				isDownload = true;
			} else {
				PackageUtils.installApk(context, apkFile.getAbsolutePath());
			}
		} else {
			newFilePath = PdfUtils.openPDF(mContext, file.getAbsolutePath(), Constants.needDecrypt);
		}
		if (isDownload) {
			progress = 0;
			initDialog(getString(R.string.activity_detail_download_wps));
			progressBar.setProgress(0);
			File downloadFile = new File(apkFile.getAbsolutePath() + "_cache");
			progressBar.setTag(downloadFile);
			FileUtils.deleteFile(downloadFile);
			final File saveFile = apkFile;
			String address = Constants.SERVER_IP + LConfigUtils.getString(context, "appconfig.wpsUrl", PdfUtils.WPS_NETWORK_DOWNLOAD);
			LogUtils.e("address--" + address);
			HttpUtils.get(mContext, address, new FileAsyncHttpResponseHandler(downloadFile) {

				@Override
				public void onSuccess(int statusCode, Header[] headers, File file) {

					// TODO Auto-generated method stub
					file.renameTo(saveFile);
					PackageUtils.installApk(mContext, saveFile.getAbsolutePath());
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

					// TODO Auto-generated method stub
					ToastUtils.showError(mContext, "WPS下载错误!");
				}

				@Override
				public void onProgress(int bytesWritten, int totalSize) {

					// TODO Auto-generated method stub
					super.onProgress(bytesWritten, totalSize);
					progress = bytesWritten * 100 / totalSize;
					if (progressBar != null) {
						progressBar.setProgress(progress);
					}
				}

				@Override
				public void onFinish() {

					// TODO Auto-generated method stub
					super.onFinish();
					if (downloadDialog != null) {
						downloadDialog.dismiss();
						downloadDialog = null;
					}
				}
			});
		}
	}

	private void download(final File saveFile, final String downloadUrl, long fileSize) {
		initDialog(getString(R.string.activity_detail_downloading));
		File cacheFile = new File(saveFile.getAbsolutePath() + "_cache_");
		if (cacheFile.length() != fileSize) {
			FileUtils.deleteFile(cacheFile);
		} else {
			if (downloadDialog != null)
				downloadDialog.dismiss();
			openPDF(mContext, saveFile);
			return;
		}
		progressBar.setTag(cacheFile);
		HttpUtils.get(mContext, downloadUrl, new FileAsyncHttpResponseHandler(cacheFile) {
			@Override
			public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
				DialogUtils.showAlertNoTitle(mContext, getString(R.string.activity_download_file_fail));
				FileUtils.deleteFile(file);
				if (downloadDialog != null)
					downloadDialog.dismiss();
			}

			@Override
			public void onSuccess(int i, Header[] headers, File file) {
				file.renameTo(saveFile);
				if (downloadDialog != null)
					downloadDialog.dismiss();
				openPDF(mContext, saveFile);
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// super.onProgress(bytesWritten, totalSize);
				int progress = Integer.valueOf(totalSize > 0 ? (int) ((double) bytesWritten * 1.0D / (double) totalSize * 100.0f) : 0);
				if (progressBar != null) {
					progressBar.setProgress(progress);
				}
			}
		});

	}
}