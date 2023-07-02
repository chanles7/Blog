package com.java.common;

/**
 * description 功能描述
 *
 * @author ch
 * create 2023/5/31 23:40
 */
public interface RedisConstants {


    /**
     * 验证码
     */
    String USER_TOKEN = "user:token:";


    /**
     * 验证码
     */
    String CAPTCHA_CODE = "CAPTCHA_CODE:";

    /**
     * 验证码有效期（分钟）
     */
    Integer CAPTCHA_EXPIRATION = 5;

    /**
     * 标签点击量key
     */
    String TAG_CLICK_VOLUME = "TAG_CLICK_VOLUME";

    /**
     * 文章阅读key
     */
    String ARTICLE_READING = "ARTICLE_READING";

    /**
     * 存储所有图片路径集合
     */
    String ALL_IMG = "all_img";

    /**
     * 存储实际使用图片路径集合
     */
    String APPLY_IMG = "apply_img";

    /**
     * 邮箱验证码
     */
    String EMAIL_CODE = "email_code_";

    /**
     * 公众号获取得验证码
     */
    String WECHAT_CODE = "wechat_code_";

    /**
     * 文章验证码校验通过的ip
     */
    String CHECK_CODE_IP = "check_code_ip";

    /**
     * 访客
     */
    String UNIQUE_VISITOR = "unique_visitor";

    /**
     * 访客地区
     */
    String VISITOR_AREA = "visitor_area";

    /**
     * 网站浏览量
     */
    String WEBSITE_BROWSE_COUNT = "website:count:browse";


    /**
     * 网站用户量
     */
    String WEBSITE_USER_COUNT = "website:count:user";

    /**
     * 用户点赞文章
     */
    String ARTICLE_USER_LIKE = "article_user_like:";


    /**
     * 文章点赞量
     */
    String ARTICLE_LIKE_COUNT = "article_like_count";
}
