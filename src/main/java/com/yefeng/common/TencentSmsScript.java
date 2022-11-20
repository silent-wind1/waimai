package com.yefeng.common;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author: 短信发送
 * @date: 2022/8/2 15:29
 * @修改备注:
 * @修改记录:
 */
@Service
@Slf4j
public class TencentSmsScript {

    public static void main(String[] args) {
//        new TencentSmsScript().sendMsg(new String[]{"+8617507012997", "+8613875946834"}, new String[]{"1314"});
        new TencentSmsScript().sendMsg(new String[]{"+8619189502685"}, new String[]{"1314"});
//        new TencentSmsScript().copyPhone("17507012997");

    }

    /**
     * 发送短信
     * @param phone
     * @param code
     */
    public void sendMsg(String[] phone, String[] code) {
        try {
            // 注意点：填写自己的secreteid和secretekey，密钥在腾讯云访问密钥获取
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential("公钥账号", "公钥密码");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            //注意点：+86必须要，为国内号码，不然会识别不了国内号码
            req.setPhoneNumberSet(phone);

            //设置自己的SdkAppID
            req.setSmsSdkAppid("SDKappID");
            //设置自己短信签名名称，不是签名id
            req.setSign("冷影默枫小程序");
            //设置自己短信模板id
            req.setTemplateID("1585487");

            //设置验证码
            req.setTemplateParamSet(code);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            log.info("验证码是否发送成功={}", SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

    }

    /**
     *
     * @param verfSize
     * @return
     */
    public String[] achieveCode(int verfSize) {
        // 验证码长度不为4和6那么抛出异常
        if(verfSize != 4 && verfSize != 6) {
            throw new CustomException("验证码长度有误，必须是4位或者6位");
        }
        String[] beforeShuffle = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        List<String> list = Arrays.asList(beforeShuffle);//将数组转换为集合
        Collections.shuffle(list);  //打乱集合顺序
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s); //将集合转化为字符串
        }
        return new String[]{sb.substring(0, verfSize)};
    }

    /**
     * 手机号添加国家+86
     * @param phone
     * @return
     */
    public String[] copyPhone(String phone) {
        String newPhone = "+86" + phone;
        String[] phoneArray = null;
        if(phone != null && phone.length() > 0) {
            phoneArray = newPhone.split(",");
        }
        return phoneArray;
    }

}

