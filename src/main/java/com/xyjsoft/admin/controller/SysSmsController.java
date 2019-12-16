package com.xyjsoft.admin.controller;


import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 短息接口
 * @author Louis
 * @date Oct 29, 2018
 */
public class SysSmsController {
	
	//产品名称:云通信短信API产品,开发者无需替换
	private  static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
	private static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
//    static final String accessKeyId = "LTAIlYUClklG7Ews";
//    static final String accessKeySecret = "TRAUzlIpodYWLGqqkeYCRsQEevprtT";
	private  static  String accessKeyId = "LTAI4AzTVoB6D1tv";
	private static  String accessKeySecret = "meqjoj5mqhE8KDgR0mn5KaULtr1lwK";
    
    
	public static  String signName  = "洛阳宏兴石化销售有限公司";
	public static  String[] templatecode  = {
		"SMS_158490242",//0支付密码更改验证码 验证码${code}，您正在尝试修改支付密码，请妥善保管账户信息。
		"SMS_152446860",//1身份验证验证码     验证码${code}，您正在进行身份验证，打死不要告诉别人哦！
		"SMS_152446859",//2登录确认验证码     验证码${code}，您正在登录，若非本人操作，请勿泄露。
		"SMS_152446858",//3登录异常验证码     验证码${code}，您正尝试异地登录，若非本人操作，请勿泄露。
		"SMS_152446857",//4用户注册验证码     验证码${code}，您正在注册成为新用户，感谢您的支持！
		"SMS_152446856",//5修改密码验证码     验证码${code}，您正在尝试修改登录密码，请妥善保管账户信息。
		"SMS_152446855"//6信息变更验证码     验证码${code}，您正在尝试变更重要信息，请妥善保管账户信息。
	};
	public static  String[] templateParam  = {
		"支付密码更改验证码 验证码${code},您正在尝试修改支付密码,请妥善保管账户信息。",
		"身份验证验证码 验证码${code},您正在进行身份验证,打死不要告诉别人哦!",
		"登录确认验证码 验证码${code},您正在登录，若非本人操作,请勿泄露。",
		"登录异常验证码 验证码${code},您正尝试异地登录,若非本人操作,请勿泄露。",
		"用户注册验证码 验证码${code},您正在注册成为新用户,感谢您的支持!",
		"修改密码验证码 验证码${code},您正在尝试修改登录密码,请妥善保管账户信息。",
		"信息变更验证码 验证码${code},您正在尝试变更重要信息,请妥善保管账户信息。"
	};	 
	 public static SendSmsResponse sendSms(String phone,int type,String JsonMsg,String extend) throws ClientException {
	    	
	    	if(phone.length() == 0){
	    		return null;
	    	}
	        //可自助调整超时时间
	        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
	        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

	        //初始化acsClient,暂不支持region化
	        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
	        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
	        IAcsClient acsClient = new DefaultAcsClient(profile);

	        //组装请求对象-具体描述见控制台-文档部分内容
	        SendSmsRequest request = new SendSmsRequest();
	        
	      //使用post提交
	        request.setMethod(MethodType.POST);
	        //必填:待发送手机号
	        request.setPhoneNumbers(phone);
	        //必填:短信签名-可在短信控制台中找到
	        request.setSignName(signName);
	        //必填:短信模板-可在短信控制台中找到
	        request.setTemplateCode(templatecode[type]);
	        //可选:模板中的变量替换JSON串,如模板内容为"验证码${code}，您正在注册成为新用户，感谢您的支持！"时,此处的值为
	        request.setTemplateParam(JsonMsg);
	        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
	        //request.setSmsUpExtendCode("90997");
	        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
	        if(extend != null){
	        	request.setOutId("yourOutId");
	        }  
	        SendSmsResponse sendSmsResponse = null;
	        //hint 此处可能会抛出异常，注意catch
	        try{
	        	sendSmsResponse = acsClient.getAcsResponse(request);
	        }catch(Exception e){
	        	return null;
	        }
	     
	        return sendSmsResponse;
	    }
	 public static void main(String[] args) throws ClientException, InterruptedException {

		    Map mapdata = new HashMap();
			String yzm = SysSmsController.getNumYZM(6);
	        //发短信
	    	Map map = new HashMap();
	    	map.put("code", yzm);
	        SendSmsResponse response = sendSms("15864500644",4,JSON.toJSONString(map),"xyjsoft");
	        System.out.println("短信接口返回的数据----------------");
	        System.out.println("Code=" + response.getCode());
	        System.out.println("Message=" + response.getMessage());
	        System.out.println("RequestId=" + response.getRequestId());
	        System.out.println("BizId=" + response.getBizId());
	        System.out.println("BizId=" + response.getBizId());
	    }
	 public static String getNumYZM(int t){
			// 定义验证码的字符表
			String chars = "0123456789";
			char[] rands = new char[t];
	        for (int i = 0; i < t; i++) {
	            int rand = (int) (Math.random() * 10);
	            rands[i] = chars.charAt(rand);
	        }
	        return String.valueOf(rands);
	}
	 
}
