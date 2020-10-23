package com.kgc.kmall.kmallpaymentweb.AlipayConfig;

/**
 * @author shkstart
 * @create 2020-10-23 14:59
 */
public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号，此处是沙箱环境的appid
    public static String app_id = "2016102300746116";

    // 商户私钥，您的PKCS8格式RSA2私钥，需要使用支付宝平台开发助手生成私钥和公钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCHSyP22/3itIibhQ92dE99NpSBQmQ8qTpguvagaWKAg72nCMOSl2MeS8lPV13SmKQAKO7Y1ZeNWjZyi/zwHDNXIYoHc7t2EdBDuE8aHmieN6h964q48Dm7MiaNfNai5n197k/L2/oZjTryQStQa59LQDD4vHUwWt1oMN8B7S/Sqom5j8Y3mHXUr1u7h6/OvHPAzFpHP9Lm2pQ58KjA3pozLHO7pq08l2yxAWqpw7z1dsVoHCtrOHYo/HSzxxg4L4St0HCxAmFsBC6JXQ35BxVVXIBVW6rI0D1g8bQFLxRvlc9Nk0jYOCiyC4FTepr1tNkWOAnLh2LDcqm8sUouLZODAgMBAAECggEAKVj4g9BV9jwQZ98xi5UeWfkdR6e/tJ5PfhKFnx62AOrCQlM2Wg20OCUXQA1GWj9WF8pqvJACgiuo6jcINWWY9y8D3SUsjHgDOz8Pxb63YGtILjuXKNAaBLDeAW2+twhUH/F+J6Jx++8irbjnAOvzQe87SDloYz/R3K7a6N7RN8N7mdoC+6AE06g5NnZ9pNr6Xm//Pd838z5m9GQr/cABTmS3eiH80RQ8UK7voz+TJFvF2Kmo/hsUoOJNqAzS8xX+x+MiSkLH0271Katqg893RhO8QLCKsI+wc0mnepy2C7jFz2/IIO7VsvL3D5iRoOA8ZnEBjyhrFECiq/hTXwnsMQKBgQDuL0i3suQW165M/0OIx+0zC67C/YHwphJVGZQy6btJWoVIQCOhvCC85w2Ext8FbPKKVemg2jw1cMviNw0gOHwupT2boGXDDETrWNm0pKjnYMpJ/p3gvkOo09pTEBel6NuBRmwUjmMkV9t0A6luNP1d4hMYaC9bHi/u9OvadvFpOwKBgQCRabcgcVCoNGX7T8SkprXnfM79MkTiSRXuB9g+ck4U9cTWWNe1w0wYAo8CgGwbI90SdXOq0SW7aMQ+DF8HUzCVV3L3QBGHlPjPTgFb69qk9OdBhGY+fejp15fZJqFMCU43yRdv/ol1Kg6XCabNJ47iQIL2oFmf4ITpu1J2dbAaWQKBgG8kZ0U415AFIpNRRb0stXVtTSY1zw1xIPrOzcWYJUmMUMcvDrForMoY8sjzV5wbg22juznQu+mf8bNkRt6zHNdTSdU12mZSclJzilrtvtcA5kPy6M63WbEM8kJyLua/KrY/Gy3YZ1DNIl0XXFdNqLDHTmvEKdzV0D4r0WaGvA2DAoGAYcVj5QNryP0aSOS0gkS5cEFB54qCOGgNv7M0maA/keZCCCFKQKNkqYwm3fa9OW5euYeRUxw3M5BYyqOEzacItpFK8qX4Y2It/S0kysqFg35Lc4jzOjdsgLZ+GfrYXwiut+GUya+qesXhc3qA1dYwQz4/EFZ2jIHovENASfgNhVkCgYEAoTGMLyUOGOsl79/X6cp5DGsVXn8r00VkGW3ay9ONSJYQq3IyZdqNBoXkjwvebY//4BcqxPeFEolbYm2qRtpa4J9zHuPyhQlct0DfJF4spkZan/0Lr9UwqChsbdKaDkJ/mMWEf+fsilVfis/I5AaM4X0X5XAkig3b+Nsa5ErK5+M=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoUSTdVW+/7rCZjQ/F/uJFOY5pdZq76X27yZ1w360f6GhYIf5hz1YIHVTBBgc9y4sBogXURcSIEqwnDNyJgRzAHM/9cXwFrno+0AfnDtbMC/6XY1QEKL2Nh4+vroOzzSpKRfPo08ZF5UNvvvCa37qId48D5JyB6aharee4rZnLnH+QZgFg7fhANyvpUYYuLfV71RAbSehvmxNlPIoW4V8x4xJ5xXchzwo7y2l6i0K3WUXDmkcsdsOxbpZdjLrP2WB3TFW8d+b239FbxCSEMU8lyhyckYjNLQz6S4yFlCfR7Q3EIThpxYm7cjZs4PUFLlR4mPmKlqHvLCm9zJTZ0cKXwIDAQAB";

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
