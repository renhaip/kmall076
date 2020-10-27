package com.kgc.kmall.kmallpaymentservice.config;

/**
 * @author shkstart
 * @create 2020-10-26 19:39
 */
public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016091000481487";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCNVj3VxIQjhklh4MG1RfsoxtiIAzCQhTwk++vbA+wNbn2nSSwH6vvO5PdtA0t/UnnpeHrbdug5TFg3DcvqOhh4qoyBiLvZwkOgkVnyDrEOxOob+qn9QcNtvyBfhjEWHVyEnYXR+D/L9tTmB1AnSPU3YTv+O3ZEKX3lgPDHch0gEqgoaF97P0mTT4t2aJP3JhZF6b+eNeVTIAZxxfo1WAxK4rNSQl1MKeG2Xxt2v1bQkRg53Dlph9EtplNs83hTRUmugHID1X3717fEJx+z6iELsUb6XaJDKgvpHeeJ1YgESK0NLQw5Sc+KH2F8MwG7GsYDrBItVPpxKdQ8ljs16ywlAgMBAAECggEAAoKhnoZbAnaEHyTqy9luWI8PkXrdBfQX2WBQFBfBvYtU1//ItGtMBaSzcFA6qww/9hGZvQQjo8DjYDbyhDp77Mi6riJLk9aLdPKf8liodGay8cRF+KWOXwih/pBXltj2p7RUiCIsn+bGaT+AAPKeyFkFw8Iym4tRHR5pKGGyertTPA2sOTARo/RG9qL5soFSUqUMQXm3X33krYmq8JQsDFH34aG/4FQpQBgDp0bHkt1H7niiFtPV2egKImEJp2VjfsAOrDq3jSsxF2czLU07erjxktsgAjkKFPUi8s2Mn41+YJJA25+EQM/CS5qQKRw9SNT7UwIRTXv79X4X147+kQKBgQDjPsQSPQYWRGU4hfZ4pJ9tyET44ERxgLeZsoVhyWQQT+28ZWghKjCKWPhQ0ZVpzlNGcn5L4fhjwYKcrN0uy2tZT8ayzPQSv1JRS+YB7OE6xtGANG9ac4ee1UOcL0nAbW+Y01W+UZ/qi2zKbxc+smDayi+SCx+2wNsrzkX1+OPSrwKBgQCfOJ4VMPaZWe4Ut7eIFSk5pyEPNEW4LL24h1BTTx4SvrEub5KlPh3xQrtNAB6+BZKJsiEag547xlXZaEvxdnenUoIC4+F3k+Pp25lmrzEx73KgG/18TFMvDr8LJactZR4nIDC40ZaRWU5/cFQdt7QdIh10sw58+ClkXgb1syrzawKBgQCtUdT32CVJXHcbSkl3MJAMil/YtixdqX/fMSK8N38jVeML+wgVq3kEO+GAuqlL3rrzEyYyuDHb297DorC8HRhOMSNqghlyJwwgx1OxijsqujNVpih8A3uf8Ca1+/czVgR0ulM+akEfAaJDL8G55xEqLmVj7SoOGD/RxGzlsyQILwKBgAm0rtZF3NJuS/gRVuvBtnigOoCtxA49E/easAfUC9ZCr5RQEtVl2vdpBPflqPoWZt9j72apXUzxgyw0kmSOvy6A9hHtCEJ8zmjkDQ7YYRxVJ1yhOCuKj4uPz4+DZRR8VIE4EiBCDnafxgw4sbf6Cc241ajWfWyayBMpNC1oFZcvAoGAXa8H6gb8nk19Apu89Ylt9UYgfmwQvMV1B96If1CaAL0a3omBf601ZQl3Mu1oIJIftvD6Snw8/FRqYLe/T3xjA7zOAs4X80+pQAdFtFQ88/cFaSKsshZM9VYeMJR6rlOI4uPkWUzfwuMW+02oAO5FVkZK6fUYkJy/YHqUGHT8yUs=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAouT2w8xlozFFqpCo2XGwCZVUHyWxiY+vRIpLZaUitX7PVBsDiE+sNNWnkoivMl/bfQPRcGApcAMshlBOwhVCSKmXzpE8a9j6G6UTEdGJm6BSikVCKDKpaJGb1y+0s5W5d6vogmNtMygVpRv2syqQUgAS8tg23hyNkNItNLjDjkPmMX3koG7zTRo5/WjsLwTren8lWYa1w/64zy2we6v7NUq442qWhrrky5taWtMoq57MkqHEt5Wbd4+/g0EY3dbb7RCq8zdHweqydID0fZMw3AjT34BhsAKnAcfX8Wc1uyZe6/G5P5KS2msjkVsU/C0SZOR4WAayvJDktAGXclyeqwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url ="http://60.205.215.91/alipay/callback/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://payment.kmall.com:8088/alipay/callback/return";


    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl ="https://openapi.alipaydev.com/gateway.do";
}
