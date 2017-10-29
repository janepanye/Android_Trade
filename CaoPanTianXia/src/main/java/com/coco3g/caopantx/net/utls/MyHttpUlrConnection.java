package com.coco3g.caopantx.net.utls;

import android.util.Log;
import android.webkit.CookieManager;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lisen on 15/12/22.
 */
public class MyHttpUlrConnection {
    private int mTimeout = 10000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    public static String COOKIE = "";

    /**
     * get访问
     *
     * @param url
     * @return
     */
    public String[] requestJson(String url) {
//        JSONObject ret = null;
//        try {
//            String[] str = request(url);
//            if (str != null && !"".equals(str)) {
//                ret = new JSONObject(str);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return request(url);
    }

    /**
     * post访问
     *
     * @param connurl
     * @param params
     * @return
     */
    public String[] requestJson(String connurl, List<NameValuePair> params) {
//        JSONObject ret = null;
//        try {
//            String str = request(connurl, params);
//            if (str != null && !"".equals(str)) {
//                ret = new JSONObject(str);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return request(connurl, params);
    }

    /**
     * 上传
     *
     * @param connurl
     * @param params
     * @param filename
     * @param filepath
     * @return
     */
    public String[] requestJson(String connurl, List<NameValuePair> params, String filename, String filepath) {
//        JSONObject ret = null;
//        try {
//            String str = request(connurl, params, filename, filepath);
//            if (str != null && !"".equals(str)) {
//                ret = new JSONObject(str);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return request(connurl, params, filename, filepath);
    }

    /**
     * 存储cookie
     *
     * @param headerFields
     */
    protected void setCookies(Map<String, List<String>> headerFields) {
        if (null == headerFields) {
            return;
        }
        List<String> cookies = headerFields.get("Set-Cookie");
        if (null == cookies) {
            return;
        }
        for (String cookie : cookies) {
            setCookie(cookie);
        }
    }

    protected String getCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        COOKIE = cookieManager.getCookie("cookie");
        if (COOKIE != null) {
            return COOKIE;
        } else {
            return "";
        }
    }

    protected void setCookie(String cookie) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie("cookie", cookie);
        COOKIE = cookie;
    }


    /**
     * get访问方式
     *
     * @param connurl
     */
    private String[] request(String connurl) {
        String[] resultStr = new String[]{"", ""};
        StringBuilder resultData = new StringBuilder("");
        HttpURLConnection conn = null;
        try {
            URL url = new URL(connurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setConnectTimeout(mTimeout);
            conn.setRequestProperty("cookie", getCookie());
            conn.connect();
            setCookies(conn.getHeaderFields());
            int resultCode = conn.getResponseCode();
            InputStreamReader in = null;
            if (resultCode == 200) {
                in = new InputStreamReader(conn.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                String inputLine;
                while ((inputLine = buffer.readLine()) != null) {
                    resultData.append(inputLine);
                    resultData.append("\n");
                }
                buffer.close();
                in.close();
            } else if (resultCode == 400) {
                in = new InputStreamReader(conn.getErrorStream());
                BufferedReader buffer = new BufferedReader(in);
                String inputLine;
                while ((inputLine = buffer.readLine()) != null) {
                    resultData.append(inputLine);
                    resultData.append("\n");
                }
                buffer.close();
                in.close();
            }
            resultStr[0] = resultData.toString();
            resultStr[1] = resultCode + "";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return resultStr;
    }

    /**
     * post访问方式
     *
     * @param connurl
     * @param params
     * @return
     */
    private String[] request(String connurl, List<NameValuePair> params) {

        Log.e("信息url", connurl.toString());
//            Log.e("信息", params.get(0).getValue() + "&&" + params.get(1).getValue());

        String[] resultStr = new String[]{"", ""};
        String resultData = "";
        HttpURLConnection conn;
        try {
            URL url = new URL(connurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setReadTimeout(mTimeout);
            conn.setConnectTimeout(mTimeout);
            String data = "";
            if (params != null && params.size() > 0) {
                for (NameValuePair nvp : params) {
                    if (nvp != null && nvp.getName() != null && nvp.getValue() != null) {
                        data = data + nvp.getName() + "=" + URLEncoder.encode(nvp.getValue(), "UTF-8") + "&";
                    }
                }
            }
            // 设置请求的头
            conn.setRequestProperty("Connection", "keep-alive");
            // 设置请求的头
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置请求的头
            conn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            // 设置请求的头
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            conn.setRequestProperty("cookie", getCookie());
            //
            conn.setDoOutput(true); // 发送POST请求必须设置允许输出
            conn.setDoInput(true); // 发送POST请求必须设置允许输入
            //获取输出流
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            setCookies(conn.getHeaderFields());
            int resultCode = conn.getResponseCode();

            Log.e("响应码", resultCode + "");

            InputStream is = null;
            if (resultCode == 200) {
                // 获取响应的输入流对象
                is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    baos.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                baos.close();
                // 返回字符串
                resultData = new String(baos.toByteArray());
            } else if (resultCode == 400) {
                // 获取响应的输入流对象
                is = conn.getErrorStream();
                // 创建字节输出流对象
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    baos.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                baos.close();
                // 返回字符串
                resultData = new String(baos.toByteArray());
            }
            resultStr[0] = resultData;
            resultStr[1] = resultCode + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }


    /**
     * 上传文件
     *
     * @param connurl
     * @param filepath
     * @param params
     * @return
     */
    private String[] request(String connurl, List<NameValuePair> params, String filename, String filepath) {
        String[] resultStr = new String[]{"", ""};
        int resultCode = 0;
        String result = "";
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        File file = new File(filepath);
        URL url;
        try {
            url = new URL(connurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(mTimeout);
            conn.setConnectTimeout(mTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.setRequestProperty("cookie", getCookie());
            if (file != null) { // 当文件不为空时执行上传
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuilder textEntity = new StringBuilder();
                if (params != null) {
                    for (NameValuePair nvp : params) {// 构造文本类型参数的实体数据
                        textEntity.append("--");
                        textEntity.append(BOUNDARY);
                        textEntity.append("\r\n");
                        textEntity.append("Content-Disposition: form-data; name=\"" + nvp.getName() + "\"\r\n\r\n");
                        textEntity.append(nvp.getValue());
                        textEntity.append("\r\n");
                    }
                }
                dos.write(textEntity.toString().getBytes());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                // 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                //* filename是文件的名字，包含后缀名
                sb.append("Content-Disposition: form-data; name=\"" + filename + "\"; filename=\"" + file.getName() + "\"" +
                        LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                setCookies(conn.getHeaderFields());
                resultCode = conn.getResponseCode();
                InputStream input = null;
                if (resultCode == 200) {
                    input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    // 释放资源
                    is.close();
                    result = sb1.toString();
                } else if (resultCode == 400) {
                    input = conn.getErrorStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    // 释放资源
                    is.close();
                    result = sb1.toString();
                }
                resultStr[0] = result;
                resultStr[1] = resultCode + "";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultStr;

    }
}
