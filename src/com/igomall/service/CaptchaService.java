/*
 * 
 * 
 * 
 */
package com.igomall.service;

import java.awt.image.BufferedImage;

import com.igomall.Setting.CaptchaType;

public interface CaptchaService {

	BufferedImage buildImage(String captchaId);

	boolean isValid(CaptchaType captchaType, String captchaId, String captcha);

}