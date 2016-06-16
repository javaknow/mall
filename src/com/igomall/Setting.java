
package com.igomall;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class Setting implements Serializable {

	private static final long serialVersionUID = -1478999889661796840L;
	public static final String CACHE_NAME = "setting";

	public static final Integer CACHE_KEY = 0;

	private static final String SEPARATOR = ",";

	private static final String SEPARATOR1 = ";";
	
	private static final String SEPARATOR2 = ":";


	public enum SignInType {
		point,
		balance,
	}
	
	public enum WatermarkPosition {

		no,

		topLeft,

		topRight,

		center,

		bottomLeft,

		bottomRight
	}

	public enum RoundType {

		roundHalfUp,

		roundUp,

		roundDown
	}

	public enum CaptchaType {

		memberLogin,

		memberRegister,

		adminLogin,

		review,

		consultation,

		findPassword,

		resetPassword,
		
		rechargeCard,//充值卡充值
		
		voucher,//代金券的领取
		
		redPacket,//领取红包
		
		modifyPhone,//修改手机号码

		other, 
	}

	public enum AccountLockType {

		member,

		admin
	}

	public enum StockAllocationTime {

		order,

		payment,

		ship
	}

	public enum ReviewAuthority {

		anyone,

		member,

		purchased
	}

	public enum ConsultationAuthority {

		anyone,

		member
	}

	private String siteName;

	private String siteUrl;

	private String logo;
	
	private String kefuQQ;
	
	private String kefuWangWang;

	private String hotSearch;

	private String address;

	private String phone;

	private String zipCode;

	private String email;

	private String certtext;

	private Boolean isSiteEnabled;

	private String siteCloseMessage;

	private Integer largeProductImageWidth;

	private Integer largeProductImageHeight;

	private Integer mediumProductImageWidth;

	private Integer mediumProductImageHeight;

	private Integer thumbnailProductImageWidth;

	private Integer thumbnailProductImageHeight;

	private String defaultLargeProductImage;

	private String defaultMediumProductImage;

	private String defaultThumbnailProductImage;

	private Integer watermarkAlpha;

	private String watermarkImage;

	private WatermarkPosition watermarkPosition;

	private Integer priceScale;

	private RoundType priceRoundType;

	private Boolean isShowMarketPrice;

	private Double defaultMarketPriceScale;

	private Boolean isRegisterEnabled;

	private Boolean isDuplicateEmail;

	private String disabledUsername;

	private Integer usernameMinLength;

	private Integer usernameMaxLength;

	private Integer passwordMinLength;

	private Integer passwordMaxLength;

	private Long registerPoint;

	private String registerAgreement;

	private Boolean isEmailLogin;

	private CaptchaType[] captchaTypes;

	private AccountLockType[] accountLockTypes;

	private Integer accountLockCount;

	private Integer accountLockTime;

	private Integer safeKeyExpiryTime;

	private Integer uploadMaxSize;

	private String uploadImageExtension;

	private String uploadFlashExtension;

	private String uploadMediaExtension;

	private String uploadFileExtension;

	private String imageUploadPath;

	private String flashUploadPath;

	private String mediaUploadPath;

	private String fileUploadPath;

	private String smtpFromMail;

	private String smtpHost;

	private Integer smtpPort;

	private String smtpUsername;

	private String smtpPassword;

	private String currencySign;

	private String currencyUnit;

	private Integer stockAlertCount;

	private StockAllocationTime stockAllocationTime;

	private Double defaultPointScale;

	private Boolean isDevelopmentEnabled;

	private Boolean isReviewEnabled;

	private Boolean isReviewCheck;

	private ReviewAuthority reviewAuthority;

	private Boolean isConsultationEnabled;

	private Boolean isConsultationCheck;

	private ConsultationAuthority consultationAuthority;

	private Boolean isInvoiceEnabled;

	private Boolean isTaxPriceEnabled;

	private Double taxRate;

	private String cookiePath;

	private String cookieDomain;

	private String kuaidi100Key;

	private Boolean isCnzzEnabled;

	private String cnzzSiteId;

	private String cnzzPassword;
	
	private String url;
	
	private Boolean isSignIn;// 是否开启签到
	
	private BigDecimal signInMoney;//签到金额
	
	private BigDecimal signInMaxMoney;//签到最高金额
	
	private BigDecimal signInEveryMoney;//连续签到增加的金额
	
	private SignInType signInType;//签到红利类型
	
	private BigDecimal zero = new BigDecimal(0);
	
	private BigDecimal percent = new BigDecimal(100);
	
	private BigDecimal one = new BigDecimal(1);
	
	private BigDecimal feeRate;
	
	private String memberNumberPrefix;// 会员编号的前缀

	private Integer memberNumberLength;// 会员编号的长度
	
	private String keyword;//网站关键字
	
	private String description;//网站描述
	
	private String defaultUserPassword="111111";//会员的默认密码
	
	private String access_token;
	
	private String jsapi_ticket;//
	
	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getDefaultUserPassword() {
		return defaultUserPassword;
	}

	public void setDefaultUserPassword(String defaultUserPassword) {
		this.defaultUserPassword = defaultUserPassword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMemberNumberPrefix() {
		return memberNumberPrefix;
	}

	public void setMemberNumberPrefix(String memberNumberPrefix) {
		this.memberNumberPrefix = memberNumberPrefix;
	}

	@NotNull
	@Min(1)
	public Integer getMemberNumberLength() {
		return memberNumberLength;
	}

	public void setMemberNumberLength(Integer memberNumberLength) {
		this.memberNumberLength = memberNumberLength;
	}
	
	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	public BigDecimal getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}

	@Length(max = 200)
	public String getKefuWangWang() {
		return kefuWangWang;
	}

	public void setKefuWangWang(String kefuWangWang) {
		this.kefuWangWang = kefuWangWang;
	}

	@Length(max = 200)
	public String getKefuQQ() {
		return kefuQQ;
	}

	public void setKefuQQ(String kefuQQ) {
		
		this.kefuQQ = kefuQQ;
	}

	public BigDecimal getOne() {
		return one;
	}

	public void setOne(BigDecimal one) {
		this.one = one;
	}

	public BigDecimal getZero() {
		return zero;
	}

	public void setZero(BigDecimal zero) {
		this.zero = zero;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public String getUrl() {
	    return url;
	}

	public void setUrl(String url) {
	    this.url = url;
	}

	@NotEmpty
	@Length(max = 200)
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@NotEmpty
	@Length(max = 200)
	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = StringUtils.removeEnd(siteUrl, "/");
	}

	@NotEmpty
	@Length(max = 200)
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Length(max = 200)
	public String getHotSearch() {
		return hotSearch;
	}

	public void setHotSearch(String hotSearch) {
		if (hotSearch != null) {
			hotSearch = hotSearch.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.hotSearch = hotSearch;
	}

	@Length(max = 200)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(max = 200)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(max = 200)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Email
	@Length(max = 200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(max = 200)
	public String getCerttext() {
		return certtext;
	}

	public void setCerttext(String certtext) {
		this.certtext = certtext;
	}

	@NotNull
	public Boolean getIsSiteEnabled() {
		return isSiteEnabled;
	}

	public void setIsSiteEnabled(Boolean isSiteEnabled) {
		this.isSiteEnabled = isSiteEnabled;
	}

	@NotEmpty
	public String getSiteCloseMessage() {
		return siteCloseMessage;
	}

	public void setSiteCloseMessage(String siteCloseMessage) {
		this.siteCloseMessage = siteCloseMessage;
	}

	@NotNull
	@Min(1)
	public Integer getLargeProductImageWidth() {
		return largeProductImageWidth;
	}

	public void setLargeProductImageWidth(Integer largeProductImageWidth) {
		this.largeProductImageWidth = largeProductImageWidth;
	}

	@NotNull
	@Min(1)
	public Integer getLargeProductImageHeight() {
		return largeProductImageHeight;
	}

	public void setLargeProductImageHeight(Integer largeProductImageHeight) {
		this.largeProductImageHeight = largeProductImageHeight;
	}

	@NotNull
	@Min(1)
	public Integer getMediumProductImageWidth() {
		return mediumProductImageWidth;
	}

	public void setMediumProductImageWidth(Integer mediumProductImageWidth) {
		this.mediumProductImageWidth = mediumProductImageWidth;
	}

	@NotNull
	@Min(1)
	public Integer getMediumProductImageHeight() {
		return mediumProductImageHeight;
	}

	public void setMediumProductImageHeight(Integer mediumProductImageHeight) {
		this.mediumProductImageHeight = mediumProductImageHeight;
	}

	@NotNull
	@Min(1)
	public Integer getThumbnailProductImageWidth() {
		return thumbnailProductImageWidth;
	}

	public void setThumbnailProductImageWidth(Integer thumbnailProductImageWidth) {
		this.thumbnailProductImageWidth = thumbnailProductImageWidth;
	}

	@NotNull
	@Min(1)
	public Integer getThumbnailProductImageHeight() {
		return thumbnailProductImageHeight;
	}

	public void setThumbnailProductImageHeight(Integer thumbnailProductImageHeight) {
		this.thumbnailProductImageHeight = thumbnailProductImageHeight;
	}

	@NotEmpty
	@Length(max = 200)
	public String getDefaultLargeProductImage() {
		return defaultLargeProductImage;
	}

	public void setDefaultLargeProductImage(String defaultLargeProductImage) {
		this.defaultLargeProductImage = defaultLargeProductImage;
	}

	@NotEmpty
	@Length(max = 200)
	public String getDefaultMediumProductImage() {
		return defaultMediumProductImage;
	}

	public void setDefaultMediumProductImage(String defaultMediumProductImage) {
		this.defaultMediumProductImage = defaultMediumProductImage;
	}

	@NotEmpty
	@Length(max = 200)
	public String getDefaultThumbnailProductImage() {
		return defaultThumbnailProductImage;
	}

	public void setDefaultThumbnailProductImage(String defaultThumbnailProductImage) {
		this.defaultThumbnailProductImage = defaultThumbnailProductImage;
	}

	@NotNull
	@Min(0)
	@Max(100)
	public Integer getWatermarkAlpha() {
		return watermarkAlpha;
	}

	public void setWatermarkAlpha(Integer watermarkAlpha) {
		this.watermarkAlpha = watermarkAlpha;
	}

	public String getWatermarkImage() {
		return watermarkImage;
	}

	public void setWatermarkImage(String watermarkImage) {
		this.watermarkImage = watermarkImage;
	}

	@NotNull
	public WatermarkPosition getWatermarkPosition() {
		return watermarkPosition;
	}

	public void setWatermarkPosition(WatermarkPosition watermarkPosition) {
		this.watermarkPosition = watermarkPosition;
	}

	@NotNull
	@Min(0)
	@Max(3)
	public Integer getPriceScale() {
		return priceScale;
	}

	public void setPriceScale(Integer priceScale) {
		this.priceScale = priceScale;
	}

	@NotNull
	public RoundType getPriceRoundType() {
		return priceRoundType;
	}

	public void setPriceRoundType(RoundType priceRoundType) {
		this.priceRoundType = priceRoundType;
	}

	@NotNull
	public Boolean getIsShowMarketPrice() {
		return isShowMarketPrice;
	}

	public void setIsShowMarketPrice(Boolean isShowMarketPrice) {
		this.isShowMarketPrice = isShowMarketPrice;
	}

	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	public Double getDefaultMarketPriceScale() {
		return defaultMarketPriceScale;
	}

	public void setDefaultMarketPriceScale(Double defaultMarketPriceScale) {
		this.defaultMarketPriceScale = defaultMarketPriceScale;
	}

	@NotNull
	public Boolean getIsRegisterEnabled() {
		return isRegisterEnabled;
	}

	public void setIsRegisterEnabled(Boolean isRegisterEnabled) {
		this.isRegisterEnabled = isRegisterEnabled;
	}

	@NotNull
	public Boolean getIsDuplicateEmail() {
		return isDuplicateEmail;
	}

	public void setIsDuplicateEmail(Boolean isDuplicateEmail) {
		this.isDuplicateEmail = isDuplicateEmail;
	}

	@Length(max = 200)
	public String getDisabledUsername() {
		return disabledUsername;
	}

	public void setDisabledUsername(String disabledUsername) {
		if (disabledUsername != null) {
			disabledUsername = disabledUsername.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.disabledUsername = disabledUsername;
	}

	@NotNull
	@Min(1)
	@Max(117)
	public Integer getUsernameMinLength() {
		return usernameMinLength;
	}

	public void setUsernameMinLength(Integer usernameMinLength) {
		this.usernameMinLength = usernameMinLength;
	}

	@NotNull
	@Min(1)
	@Max(117)
	public Integer getUsernameMaxLength() {
		return usernameMaxLength;
	}

	public void setUsernameMaxLength(Integer usernameMaxLength) {
		this.usernameMaxLength = usernameMaxLength;
	}

	@NotNull
	@Min(1)
	@Max(117)
	public Integer getPasswordMinLength() {
		return passwordMinLength;
	}

	public void setPasswordMinLength(Integer passwordMinLength) {
		this.passwordMinLength = passwordMinLength;
	}

	@NotNull
	@Min(1)
	@Max(117)
	public Integer getPasswordMaxLength() {
		return passwordMaxLength;
	}

	public void setPasswordMaxLength(Integer passwordMaxLength) {
		this.passwordMaxLength = passwordMaxLength;
	}

	@NotNull
	@Min(0)
	public Long getRegisterPoint() {
		return registerPoint;
	}

	public void setRegisterPoint(Long registerPoint) {
		this.registerPoint = registerPoint;
	}

	@NotEmpty
	public String getRegisterAgreement() {
		return registerAgreement;
	}

	public void setRegisterAgreement(String registerAgreement) {
		this.registerAgreement = registerAgreement;
	}

	@NotNull
	public Boolean getIsEmailLogin() {
		return isEmailLogin;
	}

	public void setIsEmailLogin(Boolean isEmailLogin) {
		this.isEmailLogin = isEmailLogin;
	}

	public CaptchaType[] getCaptchaTypes() {
		return captchaTypes;
	}

	public void setCaptchaTypes(CaptchaType[] captchaTypes) {
		this.captchaTypes = captchaTypes;
	}

	public AccountLockType[] getAccountLockTypes() {
		return accountLockTypes;
	}

	public void setAccountLockTypes(AccountLockType[] accountLockTypes) {
		this.accountLockTypes = accountLockTypes;
	}

	@NotNull
	@Min(1)
	public Integer getAccountLockCount() {
		return accountLockCount;
	}

	public void setAccountLockCount(Integer accountLockCount) {
		this.accountLockCount = accountLockCount;
	}

	@NotNull
	@Min(0)
	public Integer getAccountLockTime() {
		return accountLockTime;
	}

	public void setAccountLockTime(Integer accountLockTime) {
		this.accountLockTime = accountLockTime;
	}

	@NotNull
	@Min(0)
	public Integer getSafeKeyExpiryTime() {
		return safeKeyExpiryTime;
	}

	public void setSafeKeyExpiryTime(Integer safeKeyExpiryTime) {
		this.safeKeyExpiryTime = safeKeyExpiryTime;
	}

	@NotNull
	@Min(0)
	public Integer getUploadMaxSize() {
		return uploadMaxSize;
	}

	public void setUploadMaxSize(Integer uploadMaxSize) {
		this.uploadMaxSize = uploadMaxSize;
	}

	@Length(max = 200)
	public String getUploadImageExtension() {
		return uploadImageExtension;
	}

	public void setUploadImageExtension(String uploadImageExtension) {
		if (uploadImageExtension != null) {
			uploadImageExtension = uploadImageExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadImageExtension = uploadImageExtension;
	}

	@Length(max = 200)
	public String getUploadFlashExtension() {
		return uploadFlashExtension;
	}

	public void setUploadFlashExtension(String uploadFlashExtension) {
		if (uploadFlashExtension != null) {
			uploadFlashExtension = uploadFlashExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadFlashExtension = uploadFlashExtension;
	}

	@Length(max = 200)
	public String getUploadMediaExtension() {
		return uploadMediaExtension;
	}

	public void setUploadMediaExtension(String uploadMediaExtension) {
		if (uploadMediaExtension != null) {
			uploadMediaExtension = uploadMediaExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadMediaExtension = uploadMediaExtension;
	}

	@Length(max = 200)
	public String getUploadFileExtension() {
		return uploadFileExtension;
	}

	public void setUploadFileExtension(String uploadFileExtension) {
		if (uploadFileExtension != null) {
			uploadFileExtension = uploadFileExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadFileExtension = uploadFileExtension;
	}

	@NotEmpty
	@Length(max = 200)
	public String getImageUploadPath() {
		return imageUploadPath;
	}

	public void setImageUploadPath(String imageUploadPath) {
		if (imageUploadPath != null) {
			if (!imageUploadPath.startsWith("/")) {
				imageUploadPath = "/" + imageUploadPath;
			}
			if (!imageUploadPath.endsWith("/")) {
				imageUploadPath += "/";
			}
		}
		this.imageUploadPath = imageUploadPath;
	}

	@NotEmpty
	@Length(max = 200)
	public String getFlashUploadPath() {
		return flashUploadPath;
	}

	public void setFlashUploadPath(String flashUploadPath) {
		if (flashUploadPath != null) {
			if (!flashUploadPath.startsWith("/")) {
				flashUploadPath = "/" + flashUploadPath;
			}
			if (!flashUploadPath.endsWith("/")) {
				flashUploadPath += "/";
			}
		}
		this.flashUploadPath = flashUploadPath;
	}

	@NotEmpty
	@Length(max = 200)
	public String getMediaUploadPath() {
		return mediaUploadPath;
	}

	public void setMediaUploadPath(String mediaUploadPath) {
		if (mediaUploadPath != null) {
			if (!mediaUploadPath.startsWith("/")) {
				mediaUploadPath = "/" + mediaUploadPath;
			}
			if (!mediaUploadPath.endsWith("/")) {
				mediaUploadPath += "/";
			}
		}
		this.mediaUploadPath = mediaUploadPath;
	}

	@NotEmpty
	@Length(max = 200)
	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		if (fileUploadPath != null) {
			if (!fileUploadPath.startsWith("/")) {
				fileUploadPath = "/" + fileUploadPath;
			}
			if (!fileUploadPath.endsWith("/")) {
				fileUploadPath += "/";
			}
		}
		this.fileUploadPath = fileUploadPath;
	}

	@NotEmpty
	@Email
	@Length(max = 200)
	public String getSmtpFromMail() {
		return smtpFromMail;
	}

	public void setSmtpFromMail(String smtpFromMail) {
		this.smtpFromMail = smtpFromMail;
	}

	@NotEmpty
	@Length(max = 200)
	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	@NotNull
	@Min(0)
	public Integer getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}

	@NotEmpty
	@Length(max = 200)
	public String getSmtpUsername() {
		return smtpUsername;
	}

	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	@Length(max = 200)
	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	@NotEmpty
	@Length(max = 200)
	public String getCurrencySign() {
		return currencySign;
	}

	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}

	@NotEmpty
	@Length(max = 200)
	public String getCurrencyUnit() {
		return currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	@NotNull
	@Min(0)
	public Integer getStockAlertCount() {
		return stockAlertCount;
	}

	public void setStockAlertCount(Integer stockAlertCount) {
		this.stockAlertCount = stockAlertCount;
	}

	@NotNull
	public StockAllocationTime getStockAllocationTime() {
		return stockAllocationTime;
	}

	public void setStockAllocationTime(StockAllocationTime stockAllocationTime) {
		this.stockAllocationTime = stockAllocationTime;
	}

	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	public Double getDefaultPointScale() {
		return defaultPointScale;
	}

	public void setDefaultPointScale(Double defaultPointScale) {
		this.defaultPointScale = defaultPointScale;
	}

	@NotNull
	public Boolean getIsDevelopmentEnabled() {
		return isDevelopmentEnabled;
	}

	public void setIsDevelopmentEnabled(Boolean isDevelopmentEnabled) {
		this.isDevelopmentEnabled = isDevelopmentEnabled;
	}

	@NotNull
	public Boolean getIsReviewEnabled() {
		return isReviewEnabled;
	}

	public void setIsReviewEnabled(Boolean isReviewEnabled) {
		this.isReviewEnabled = isReviewEnabled;
	}

	@NotNull
	public Boolean getIsReviewCheck() {
		return isReviewCheck;
	}

	public void setIsReviewCheck(Boolean isReviewCheck) {
		this.isReviewCheck = isReviewCheck;
	}

	@NotNull
	public ReviewAuthority getReviewAuthority() {
		return reviewAuthority;
	}

	public void setReviewAuthority(ReviewAuthority reviewAuthority) {
		this.reviewAuthority = reviewAuthority;
	}

	@NotNull
	public Boolean getIsConsultationEnabled() {
		return isConsultationEnabled;
	}

	public void setIsConsultationEnabled(Boolean isConsultationEnabled) {
		this.isConsultationEnabled = isConsultationEnabled;
	}

	@NotNull
	public Boolean getIsConsultationCheck() {
		return isConsultationCheck;
	}

	public void setIsConsultationCheck(Boolean isConsultationCheck) {
		this.isConsultationCheck = isConsultationCheck;
	}

	@NotNull
	public ConsultationAuthority getConsultationAuthority() {
		return consultationAuthority;
	}

	public void setConsultationAuthority(ConsultationAuthority consultationAuthority) {
		this.consultationAuthority = consultationAuthority;
	}

	@NotNull
	public Boolean getIsInvoiceEnabled() {
		return isInvoiceEnabled;
	}

	public void setIsInvoiceEnabled(Boolean isInvoiceEnabled) {
		this.isInvoiceEnabled = isInvoiceEnabled;
	}

	@NotNull
	public Boolean getIsTaxPriceEnabled() {
		return isTaxPriceEnabled;
	}

	public void setIsTaxPriceEnabled(Boolean isTaxPriceEnabled) {
		this.isTaxPriceEnabled = isTaxPriceEnabled;
	}

	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	@NotEmpty
	@Length(max = 200)
	public String getCookiePath() {
		return cookiePath;
	}

	public void setCookiePath(String cookiePath) {
		if (cookiePath != null && !cookiePath.endsWith("/")) {
			cookiePath += "/";
		}
		this.cookiePath = cookiePath;
	}

	@Length(max = 200)
	public String getCookieDomain() {
		return cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	@Length(max = 200)
	public String getKuaidi100Key() {
		return kuaidi100Key;
	}

	public void setKuaidi100Key(String kuaidi100Key) {
		this.kuaidi100Key = kuaidi100Key;
	}

	public Boolean getIsCnzzEnabled() {
		return isCnzzEnabled;
	}

	public void setIsCnzzEnabled(Boolean isCnzzEnabled) {
		this.isCnzzEnabled = isCnzzEnabled;
	}

	public String getCnzzSiteId() {
		return cnzzSiteId;
	}

	public void setCnzzSiteId(String cnzzSiteId) {
		this.cnzzSiteId = cnzzSiteId;
	}

	public String getCnzzPassword() {
		return cnzzPassword;
	}

	public void setCnzzPassword(String cnzzPassword) {
		this.cnzzPassword = cnzzPassword;
	}

	public String[] getHotSearches() {
		return StringUtils.split(hotSearch, SEPARATOR);
	}

	public String[] getDisabledUsernames() {
		return StringUtils.split(disabledUsername, SEPARATOR);
	}

	public String[] getUploadImageExtensions() {
		return StringUtils.split(uploadImageExtension, SEPARATOR);
	}

	public String[] getUploadFlashExtensions() {
		return StringUtils.split(uploadFlashExtension, SEPARATOR);
	}

	public String[] getUploadMediaExtensions() {
		return StringUtils.split(uploadMediaExtension, SEPARATOR);
	}

	public String[] getUploadFileExtensions() {
		return StringUtils.split(uploadFileExtension, SEPARATOR);
	}

	public BigDecimal setScale(BigDecimal amount) {
		if (amount == null) {
			return null;
		}
		int roundingMode;
		if (getPriceRoundType() == RoundType.roundUp) {
			roundingMode = BigDecimal.ROUND_UP;
		} else if (getPriceRoundType() == RoundType.roundDown) {
			roundingMode = BigDecimal.ROUND_DOWN;
		} else {
			roundingMode = BigDecimal.ROUND_HALF_UP;
		}
		return amount.setScale(getPriceScale(), roundingMode);
	}
	
	@NotNull
	public Boolean getIsSignIn() {
		return isSignIn;
	}

	public void setIsSignIn(Boolean isSignIn) {
		this.isSignIn = isSignIn;
	}
	
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getSignInMoney() {
		return signInMoney;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public void setSignInMoney(BigDecimal signInMoney) {
		this.signInMoney = signInMoney;
	}

	public SignInType getSignInType() {
		return signInType;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getSignInMaxMoney() {
		return signInMaxMoney;
	}

	public void setSignInMaxMoney(BigDecimal signInMaxMoney) {
		this.signInMaxMoney = signInMaxMoney;
	}
	
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getSignInEveryMoney() {
		return signInEveryMoney;
	}

	public void setSignInEveryMoney(BigDecimal signInEveryMoney) {
		this.signInEveryMoney = signInEveryMoney;
	}

	public void setSignInType(SignInType signInType) {
		this.signInType = signInType;
	}
	
	public Map<String, String> getKefuQQs() {
		Map<String, String> map = new HashMap<String, String>();
		String[] qqWebNames = StringUtils.split(kefuQQ, SEPARATOR1);
		for (String qqWebName : qqWebNames) {
			String[] qqs = StringUtils.split(qqWebName, SEPARATOR2);
			if (qqs != null && qqs.length == 2) {
				map.put(qqs[0], qqs[1]);
			}

		}

		return map;
	}
	
	public Map<String, String> getKefuWangWangs() {
		
		Map<String, String> map = new HashMap<String, String>();
		String[] qqWebNames = StringUtils.split(kefuWangWang, SEPARATOR1);
		for (String qqWebName : qqWebNames) {
			String[] qqs = StringUtils.split(qqWebName, SEPARATOR2);
			if (qqs != null && qqs.length == 2) {
				map.put(qqs[0], qqs[1]);
			}

		}

		return map;
	}

}