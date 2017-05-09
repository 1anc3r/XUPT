package me.lancer.xupt.util;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by HuangFangzhi on 2016/12/14.
 */

public class ContentGetterSetter {

    String key, number;

    public ContentGetterSetter() {
    }

    public ContentGetterSetter(String key, String number) {
        this.key = key;
        this.number = number;
    }

    public String getContentFromHtml(String url, String cookie) {
        StringBuilder content = new StringBuilder();
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        client.setFollowSslRedirects(false);
        Request request = new Request.Builder().url(url).addHeader("Cookie", cookie).addHeader("Referer", url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                if (response.header("Content-Length").equals("276")) {
                    Log.e("gettersetter.fromHtml", "加载文件失败!未评价");
                    return "加载文件失败!未评价";
                }
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                Log.e("gettersetter.fromHtml", "加载文件成功!");
                return content.toString();
            } else {
                Log.e("gettersetter.fromHtml", "加载文件失败!状态码:" + response.code());
                return "加载文件失败!状态码:" + response.code();
            }
        } catch (IOException e) {
            Log.e("gettersetter.fromHtml", "加载文件失败!捕获异常:" + e.toString());
            return "加载文件失败!捕获异常:" + e.toString();
        }
    }

    public String getContentFromFile(String path) {
        try {
            File dir = new File(path + "/me.lancer.xupt");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir.getPath() + "/" + key + number);
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            String content = new String(bytes);
            if (content == null) {
                Log.e("gettersetter.fromFile", "加载文件失败!空文件");
                return "加载文件失败!空文件";
            }
            if (fis != null) {
                fis.close();
            }
            Log.e("gettersetter.fromFile", "加载文件成功!");
            return content;
        } catch (IOException e) {
            Log.e("gettersetter.fromFile", "加载文件失败!捕获异常:" + e.toString());
            return "加载文件失败!捕获异常:" + e.toString();
        }
    }

    public void setContentToFile(String path, String content) {
        try {
            File dir = new File(path + "/me.lancer.xupt");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir.getPath() + "/" + key + number);
            if (file.exists() || !content.contains(number)) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            if (fos != null) {
                fos.close();
            }
            Log.e("gettersetter.toFile", "配置文件成功!");
        } catch (Exception e) {
            Log.e("gettersetter.toFile", "配置文件失败!捕获异常:" + e.toString());
        }
    }

    public String getContentFromHtm1(String log, String url) {
        StringBuilder content = new StringBuilder();
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                Log.e(log, "获取成功!");
                return content.toString();
            } else {
                Log.e(log, "获取失败!状态码:" + response.code());
                return "获取失败!状态码:" + response.code();
            }
        } catch (IOException e) {
            Log.e(log, "获取失败!捕获异常:" + e.toString());
            return "获取失败!捕获异常:" + e.toString();
        }
    }
}
