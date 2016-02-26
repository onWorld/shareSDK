package cn.eol.wen.activity;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.eol.wen.R;
import cn.eol.wen.commons.Constants;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.GooglePlusShareContent;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.TwitterShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


/**
 * @description 实现友盟第三方登录注销、获取用户信息
 */
public class LoginActivity extends Activity implements OnClickListener {
	// 整个平台的Controller,负责管理整个SDK的配置、操作等处理
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(Constants.DESCRIPTOR);

	private Button sinaLoginButton;
	private Button sinaLogoutButton;
	private Button qqLoginButton;
	private Button qqLogoutButton;
	private Button wechatLoginButton;
	private Button wechatLogoutButton;
	private Button shareButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sinaLoginButton = (Button) this.findViewById(R.id.btn_sina_login);
		sinaLogoutButton = (Button) this.findViewById(R.id.btn_sina_logout);
		qqLoginButton = (Button) this.findViewById(R.id.btn_qq_login);
		qqLogoutButton = (Button) this.findViewById(R.id.btn_qq_logout);
		shareButton = (Button) this.findViewById(R.id.btn_share);
		wechatLoginButton = (Button) this.findViewById(R.id.btn_wechat_login);
		wechatLogoutButton = (Button) this.findViewById(R.id.btn_wechat_logout);
		
		sinaLoginButton.setOnClickListener(this);
		sinaLogoutButton.setOnClickListener(this);
		qqLoginButton.setOnClickListener(this);
		qqLogoutButton.setOnClickListener(this);
		shareButton.setOnClickListener(this);
		wechatLoginButton.setOnClickListener(this);
		wechatLogoutButton.setOnClickListener(this);

		// 配置需要分享的相关平台
		configPlatforms();
		// 设置分享的内容
		setShareContent();
	}
	

	/**
	 * 配置分享平台参数
	 */
	private void configPlatforms() {
		// 添加新浪sso授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		// 添加QQ、QZone平台
		addQQQZonePlatform();

		// 添加微信、微信朋友圈平台
		addWXPlatform();

	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent() {

		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				LoginActivity.this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();
		mController.setShareContent("测试掌上高考APP分享");
		

		UMImage localImage = new UMImage(LoginActivity.this, R.drawable.device);
		UMImage urlImage = new UMImage(LoginActivity.this,
				"http://d.3987.com/gkzmbz_140608/004.jpg");
		// UMImage resImage = new UMImage(LoginActivity.this, R.drawable.icon);

		// 视频分享
		UMVideo video = new UMVideo(
				"http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
		// vedio.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
		video.setTitle("友盟社会化组件视频");
		video.setThumb(urlImage);

		UMusic uMusic = new UMusic(
				"http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
		uMusic.setAuthor("umeng");
		uMusic.setTitle("天籁之音");
		uMusic.setThumb(urlImage);
		// uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，微信");
		weixinContent.setTitle("友盟社会化分享组件-微信");
		weixinContent.setTargetUrl("http://www.umeng.com");
		weixinContent.setShareMedia(urlImage);
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，朋友圈");
		circleMedia.setTitle("友盟社会化分享组件-朋友圈");
		circleMedia.setShareImage(urlImage);
		// circleMedia.setShareMedia(uMusic);
		// circleMedia.setShareMedia(video);
		circleMedia.setTargetUrl("http://www.umeng.com");
		mController.setShareMedia(circleMedia);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent("来自 掌上高考 分享功能 -- QZone");
		qzone.setTargetUrl("http://i.wen.106.dev.eol.com.cn/mob/share/article?id=1");
		qzone.setTitle("掌上高考");
		qzone.setShareImage(urlImage);
		mController.setShareMedia(qzone);

		video.setThumb(new UMImage(LoginActivity.this, BitmapFactory
				.decodeResource(getResources(), R.drawable.device)));

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
		qqShareContent.setTitle("hello, title");
		qqShareContent.setShareImage(urlImage);
		// qqShareContent.setShareMusic(uMusic);
		// qqShareContent.setShareVideo(video);
		qqShareContent.setTargetUrl("http://www.umeng.com/social");
		mController.setShareMedia(qqShareContent);

		// 视频分享
		UMVideo umVideo = new UMVideo(
				"http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
		umVideo.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
		umVideo.setTitle("友盟社会化组件视频");

		TencentWbShareContent tencent = new TencentWbShareContent();
		tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，腾讯微博");
		// 设置tencent分享内容
		mController.setShareMedia(tencent);

		// 设置邮件分享内容， 如果需要分享图片则只支持本地图片
		MailShareContent mail = new MailShareContent(localImage);
		mail.setTitle("share form umeng social sdk");
		mail.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，email");
		// 设置tencent分享内容
		mController.setShareMedia(mail);

		// 设置短信分享内容
		SmsShareContent sms = new SmsShareContent();
		sms.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，短信");
		sms.setShareImage(urlImage);
		mController.setShareMedia(sms);

		SinaShareContent sinaContent = new SinaShareContent(urlImage);
		sinaContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，新浪微博");
		mController.setShareMedia(sinaContent);

		TwitterShareContent twitterShareContent = new TwitterShareContent();
		twitterShareContent
				.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，TWITTER");
		twitterShareContent.setShareMedia(localImage);
		mController.setShareMedia(twitterShareContent);

		GooglePlusShareContent googlePlusShareContent = new GooglePlusShareContent();
		googlePlusShareContent
				.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，G+");
		googlePlusShareContent.setShareMedia(localImage);
		mController.setShareMedia(googlePlusShareContent);
	}
	/**
	 * 添加所有的平台</br>
	 */
	private void addCustomPlatforms() {
		// 添加微信平台
		addWXPlatform();
		// 添加QQ平台
		addQQQZonePlatform();
		
		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
		mController.openShare(LoginActivity.this, false);
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx5a5ea8cd383f8f36";
		String appSecret = "8ef24d7a77b7c6a3910c04c13a079b04";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(LoginActivity.this, appId,
				appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(LoginActivity.this,
				appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "1105199062";
		String appKey = "j2vvOOwE08zcWrBa";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(LoginActivity.this,
				appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				LoginActivity.this, appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	@Override
	public void onClick(View v) {
		int nid = v.getId();
		if (nid == R.id.btn_sina_login) {
			login(SHARE_MEDIA.SINA);
			return;
		}else if (nid == R.id.btn_qq_login) {
			login(SHARE_MEDIA.QQ);
			return;
		}else if (nid == R.id.btn_wechat_login) {
			login(SHARE_MEDIA.WEIXIN);
			return;
		}else if (nid == R.id.btn_sina_logout) {
			logout(SHARE_MEDIA.SINA);
			return;
		}else if (nid == R.id.btn_qq_logout) {
			logout(SHARE_MEDIA.QQ);
			return;
		}else if (nid == R.id.btn_wechat_logout) {
			logout(SHARE_MEDIA.WEIXIN);
			return;
		}else if(nid== R.id.btn_share){
			addCustomPlatforms();
			return;
		}
	}

	/**
	 * 授权。如果授权成功，则获取用户信息
	 * 
	 * @param platform
	 */
	private void login(final SHARE_MEDIA platform) {
		mController.doOauthVerify(LoginActivity.this, platform,
				new UMAuthListener() {

					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权开始",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权完成",
								Toast.LENGTH_LONG).show();
						// 获取uid
						String uid = value.getString("uid");
						if (!TextUtils.isEmpty(uid)) {
							// uid不为空，获取用户信息
							getUserInfo(platform);
						} else {
							Toast.makeText(LoginActivity.this, "授权失败...",
									Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权取消",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	/**
	 * 获取用户信息
	 * 
	 * @param platform
	 */
	private void getUserInfo(SHARE_MEDIA platform) {
		mController.getPlatformInfo(LoginActivity.this, platform,
				new UMDataListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {
						//获取第三方信息
						String showText = "";
						 if (status == StatusCode.ST_CODE_SUCCESSED) {
						 showText = "用户名：" +
						 info.get("screen_name").toString();
						 Log.d("#########", "##########" + info.toString());
						 } else {
						 showText = "获取用户信息失败";
						 }

						if (info != null) {
							Toast.makeText(LoginActivity.this, info.toString(),Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
	
	/**
	 * 注销本次登陆
	 * @param platform
	 */
	private void logout(final SHARE_MEDIA platform) {
		mController.deleteOauth(LoginActivity.this, platform, new SocializeClientListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onComplete(int status, SocializeEntity entity) {
				String showText = "解除" + platform.toString() + "平台授权成功";
				if (status != StatusCode.ST_CODE_SUCCESSED) {
					showText = "解除" + platform.toString() + "平台授权失败[" + status + "]";
				}
				Toast.makeText(LoginActivity.this, showText, Toast.LENGTH_SHORT).show();
			}
		});
	}
	

	// 如果有使用任一平台的SSO授权, 则必须在对应的activity中实现onActivityResult方法, 并添加如下代码
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 根据requestCode获取对应的SsoHandler，返回一定是requestCode
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}