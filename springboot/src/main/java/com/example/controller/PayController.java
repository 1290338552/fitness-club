package com.example.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.example.common.Result;
import com.example.service.RechargeService;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Resource
    AlipayClient alipayClient;

    @Resource
    RechargeService rechargeService;

    @Value("${alipay.notify-url}")
    private String notifyUrl;

    @Value("${alipay.return-url}")
    private String returnUrl;

    @Value("${alipay.alipay-public-key}")
    private String alipayPublicKey;

    /**
     * 发起支付，返回支付宝表单 HTML
     */
    @GetMapping("/create")
    public Result create(@RequestParam BigDecimal amount,
                         @RequestParam Integer userId) throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        String outTradeNo = UUID.randomUUID().toString().replace("-", "");
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + outTradeNo + "\"," +
                "\"total_amount\":\"" + amount + "\"," +
                "\"subject\":\"健身房余额充值\"," +
                "\"body\":\"用户ID:" + userId + "\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"" +
                "}");

        String form = alipayClient.pageExecute(request).getBody();
        return Result.success(form);
    }

    /**
     * 支付宝异步回调（加余额）
     */
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        try {
            // 获取所有参数
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, String.join(",", requestParams.get(name)));
            }

            // 验签
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params, alipayPublicKey, "UTF-8", "RSA2"
            );

            if (!signVerified) {
                return "failure";
            }

            String tradeStatus = params.get("trade_status");
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                // 从 body 里取 userId
                String body = params.get("body");
                if (body != null && body.startsWith("用户ID:")) {
                    Integer userId = Integer.parseInt(body.replace("用户ID:", "").trim());
                    BigDecimal amount = new BigDecimal(params.get("total_amount"));
                    rechargeService.recharge(userId, amount);
                }
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }
}
