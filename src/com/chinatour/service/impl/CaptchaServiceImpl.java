package com.chinatour.service.impl;

/**
 * Created by XuXuebin on 2014/7/9.
 */

import com.chinatour.Setting;
import com.chinatour.service.CaptchaService;
import com.chinatour.util.SettingUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;

/**
 * Service - 验证码
 *
 * @author SHOP++ Team
 * @version 3.0
 */
@Service("captchaServiceImpl")
public class CaptchaServiceImpl implements CaptchaService {

    @Resource(name = "imageCaptchaService")
    private com.octo.captcha.service.CaptchaService imageCaptchaService;

    public BufferedImage buildImage(String captchaId) {
        return (BufferedImage) imageCaptchaService.getChallengeForID(captchaId);
    }

    public boolean isValid(Setting.CaptchaType captchaType, String captchaId, String captcha) {
        Setting setting = SettingUtils.get();
        if (captchaType == null || ArrayUtils.contains(setting.getCaptchaTypes(), captchaType)) {
        	System.out.println("1>>>>>");
            if (StringUtils.isNotEmpty(captchaId) && StringUtils.isNotEmpty(captcha)) {
                try {
                	boolean a = imageCaptchaService.validateResponseForID(captchaId, captcha.toUpperCase());
                	System.out.println("11***"+a+"***"+"captchaId="+captchaId+"***captcha="+captcha);
                    //return imageCaptchaService.validateResponseForID(captchaId, captcha.toUpperCase());
                	return a;
                } catch (Exception e) {
                	System.out.println("11***False***");
                	System.out.println(e);
                    return false;
                }
            } else {
            	System.out.println("12***False***captchaId||captcha isEmpty");
                return false;
            }
        } else {
        	System.out.println("2>>>>>True");
            return true;
        }
    }

}