package com.lomoye.common.util;

import com.google.common.base.Strings;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * httpclient的通用封装，httpClientUtil是一个单例，在这个单例中控制超时时间以及网络调用的线程池
 * <p>
 * example:
 * get:
 * HttpGet httpGet = HttpClientUtil.getGetRequest();
 * httpGet.setURI(new URI(""));
 * httpGet.setHeader("user_id", "");
 * httpGet.setHeader("user_key", "");
 * httpGet.setHeader("baseid", "");
 * httpGet.setHeader("client_sys_name", "");
 * String codeStr = HttpClientUtil.executeAndGet(httpGet);
 * <p>
 * post:
 * HttpPost httpPost = HttpClientUtil.getPostRequest();
 * httpPost.setURI(new URI(""));
 * httpPost.setHeader("Accept", "application/json");
 * httpPost.setHeader("user_id", "");
 * httpPost.setHeader("user_key", "");
 * httpPost.setHeader("baseid", "");
 * httpPost.setHeader("client_sys_name", "");
 * String json = SerializationUtil.gson2String(null);
 * httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
 * String resJsonStr = HttpClientUtil.executeAndGet(httpPost);
 * Type type = new TypeToken<GeneralModel<Integer>>() {}.getType();
 * GeneralModel<Integer> domain = SerializationUtil.gson2Object(resJsonStr, type);
 * <p>
 * <p>
 * upload
 * HttpPost httppost = new HttpPost(url);
 * FileBody bin = new FileBody(new File(filepath + File.separator + filename1));
 * comment = new StringBody(filename1);
 * MultipartEntity reqEntity = new MultipartEntity();
 * reqEntity.addPart("file1", bin);//file1为请求后台的File upload;属性
 * reqEntity.addPart("filename1", comment);//filename1为请求后台的普通参数;属性
 * httppost.setEntity(reqEntity);
 * HttpClientUtil.executeAndGet(httppost);
 *
 * @version 1.0
 * @since 2014年9月16日
 */

public class HttpClientUtil {
    private static final HttpClientUtil instance = new HttpClientUtil();

    private CloseableHttpClient httpClient;

    //这两个参数可以放到配置文件，构建httpclient的时候一定要设置这两个参数。很多故障都由此引起
    private final int TIMEOUT_SECONDS = 10 * 1000;   //10s

    private final int POOL_SIZE = 60;

    private static Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    // 创建包含connection pool与超时设置的client
    private HttpClientUtil() {
        //setCircularRedirectsAllowed有风险.访问Tmall有域名店铺的时候， 会有circle redirect, 但是参数不同. setCircularRedirectsAllowed必须设置为true
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT_SECONDS)
                .setConnectTimeout(TIMEOUT_SECONDS)
                .setConnectionRequestTimeout(TIMEOUT_SECONDS).setCircularRedirectsAllowed(true).build();

        HttpClientBuilder builder = HttpClientBuilder.create().setMaxConnTotal(10 * POOL_SIZE).setMaxConnPerRoute(3 * POOL_SIZE)
                .setDefaultRequestConfig(requestConfig);
        builder.setRedirectStrategy(new DefaultRedirectStrategy() {
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
                boolean isRedirect = false;
                try {
                    isRedirect = super.isRedirected(request, response, context);
                } catch (ProtocolException e) {
                    LOGGER.error("redirect error ", e);
                }
                if (!isRedirect) {
                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == 301 || responseCode == 302) {
                        return true;
                    }
                }
                return isRedirect;
            }
        });

        // setup a Trust Strategy that allows all certificates.
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (Exception e) {
            LOGGER.error("SSLContext setting error ...", e);
        }
        builder.setSslcontext(sslContext);

        // don't check Hostname, either.
        //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        //allows multi-threaded use
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        //connMgr.closeIdleConnections(3, TimeUnit.SECONDS);
        builder.setConnectionManager(connMgr);

        httpClient = builder.build();
    }

    private void destroyApacheHttpClient() {
        try {
            if (httpClient != null) {
                httpClient.close();
                httpClient = null;
            }
        } catch (IOException e) {
            LOGGER.error("httpclient close fail", e);
        }
    }

    public static HttpGet getGetRequest() {
        return new HttpGet();
    }

    public static HttpPost getPostRequest() {
        return new HttpPost();
    }

    public static HttpPut getPutRequest() {
        return new HttpPut();
    }

    public static HttpDelete getDeleteRequest() {
        return new HttpDelete();
    }

    public static HttpClientUtil getInstance() {
        return instance;
    }

    public String executeAndGet(HttpRequestBase httpRequestBase, HttpContext httpContext, String encode) throws Exception {
        CloseableHttpResponse response = null;

        String entityStr = "";
        Long start = System.currentTimeMillis();

        try {
            response = httpClient.execute(httpRequestBase, httpContext);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                LOGGER.error("请求地址：" + httpRequestBase.getURI() + ", 请求方法：" + httpRequestBase.getMethod()
                        + ",STATUS CODE = " + response.getStatusLine().getStatusCode());

                throw new Exception("Response Status Code : " + response.getStatusLine().getStatusCode());
            } else {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String actualEncode = getEncode(response, encode);
                    entityStr = EntityUtils.toString(entity, actualEncode);
                } else {
                    throw new Exception("Response Entity Is Null");
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpRequestBase != null) {
                httpRequestBase.releaseConnection();
            }
            Long end = System.currentTimeMillis();
            LOGGER.info("url:" + httpRequestBase.getURI().toString() + ",cost time:" + (end - start) / 1000 + "s");
        }
        return entityStr;
    }

    public byte[] executeAndGet(HttpRequestBase httpRequestBase, HttpContext httpContext) throws Exception {
        CloseableHttpResponse response = null;

        byte[] entityBytes = null;
        Long start = System.currentTimeMillis();

        try {
            response = httpClient.execute(httpRequestBase, httpContext);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                LOGGER.error("请求地址：" + httpRequestBase.getURI() + ", 请求方法：" + httpRequestBase.getMethod()
                        + ",STATUS CODE = " + response.getStatusLine().getStatusCode());

                throw new Exception("Response Status Code : " + response.getStatusLine().getStatusCode());
            } else {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    entityBytes = EntityUtils.toByteArray(entity);
                } else {
                    throw new Exception("Response Entity Is Null");
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpRequestBase != null) {
                httpRequestBase.releaseConnection();
            }
            Long end = System.currentTimeMillis();
            LOGGER.info("url:" + httpRequestBase.getURI().toString() + ",cost time:" + (end - start) / 1000 + "s");
        }
        return entityBytes;
    }

    private String getEncode(CloseableHttpResponse response, String encode) {
        if (Strings.isNullOrEmpty(encode)) {
            encode = "UTF-8";
        }
        Header[] hs = response.getHeaders("Content-Type");
        if (hs == null || hs.length == 0) {
            return encode;
        }
        for (int i = 0; i < hs.length; i++) {
            String value = hs[i].getValue();
            if (value != null && value.toUpperCase().contains("GBK")) {
                encode = "GBK";
                break;
            } else if (value != null && value.toUpperCase().contains("UTF-8")) {
                encode = "UTF-8";
                break;
            }
        }
        return encode;
    }
//
//    public static void main(String[] args) throws Exception {
//        String req = "http://shop117316962.taobao.com";
//        HttpGet httpGet = HttpClientUtil.getGetRequest();
//        httpGet.setURI(new URI(req));
//        String res = HttpClientUtil.getInstance().executeAndGet(httpGet, null, "GBK");
//        int a = res.indexOf("rate.taobao.com/user-rate-");
//        if (a == -1) {
//            LOGGER.error("抓取用户uid,url=" + "http://shop117316962.taobao.com/");
//        } else {
//            int b = res.indexOf(".htm", a);
//            System.out.println("uid=" + res.substring(a + 26, b));
//        }
//    }
}