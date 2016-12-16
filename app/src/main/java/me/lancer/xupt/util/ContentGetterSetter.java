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
                    Log.e("gettersetter.fromHtml", "!error!----no evaluate");
                    return "!error!";
                }
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                Log.e("gettersetter.fromHtml", "getContentFromHtml.done");
                return content.toString();
            } else {
                Log.e("gettersetter.fromHtml", "!error!----status code:" + response.code());
                return "!error!";
            }
        } catch (IOException e) {
            Log.e("gettersetter.fromHtml", "!error!----exception:" + e.toString());
            return "!error!";
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
                Log.e("gettersetter.fromFile", "!error!----empty file");
                return "!error!";
            }
            if (fis != null) {
                fis.close();
            }
            Log.e("gettersetter.fromFile", "getContentFromFile.done");
            return content;
        } catch (IOException e) {
            Log.e("gettersetter.fromFile", "!error!----exception:" + e.toString());
            return "!error!";
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
            Log.e("gettersetter.toFile", "setContentToFile.done");
        } catch (Exception e) {
            Log.e("gettersetter.toFile", "!error!----exception:" + e.toString());
        }
    }
}
