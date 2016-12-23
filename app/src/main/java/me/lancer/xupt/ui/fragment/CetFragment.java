package me.lancer.xupt.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;

import me.lancer.xupt.R;
import me.lancer.xupt.ui.activity.MainActivity;
import me.lancer.xupt.ui.view.ClearEditText;
import me.lancer.xupt.util.NetworkDiagnosis;

/**
 * Created by HuangFangzhi on 2016/12/16.
 */

public class CetFragment extends BaseFragment {

    LinearLayout llCet;
    Toolbar tCet;
    ClearEditText cetNumber, cetName;
    Button btnQuery;
    TextView tvName, tvSchool, tvType, tvNumber, tvSum, tvHear, tvRead, tvWrite;
    ProgressDialog pdQuery;

    String number, name, school, type, time, sum, hear, read, write;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                String result = (String) msg.obj;
                String[] results = result.split("!");
                tvName.setText(results[0]);
                tvSchool.setText(results[1]);
                tvType.setText(results[2]);
                tvNumber.setText(results[3]);
                tvSum.setText(results[4]);
                tvHear.setText(results[5]);
                tvRead.setText(results[6]);
                tvWrite.setText(results[7]);
                pdQuery.dismiss();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cet, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        llCet = (LinearLayout) view.findViewById(R.id.ll_cet);
        tCet = (Toolbar) view.findViewById(R.id.t_cet);
        tCet.setTitle(R.string.cet_title);
        ((MainActivity) getActivity()).initDrawer(tCet);
        cetNumber = (ClearEditText) view.findViewById(R.id.cet_number);
        cetName = (ClearEditText) view.findViewById(R.id.cet_name);
        btnQuery = (Button) view.findViewById(R.id.btn_query);
        btnQuery.setOnClickListener(vOnClickListener);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvSchool = (TextView) view.findViewById(R.id.tv_school);
        tvType = (TextView) view.findViewById(R.id.tv_type);
        tvNumber = (TextView) view.findViewById(R.id.tv_number);
        tvSum = (TextView) view.findViewById(R.id.tv_sum);
        tvHear = (TextView) view.findViewById(R.id.tv_hear);
        tvRead = (TextView) view.findViewById(R.id.tv_read);
        tvWrite = (TextView) view.findViewById(R.id.tv_write);
        pdQuery = new ProgressDialog(getActivity());
        pdQuery.setMessage(getString(R.string.cet_query));
        pdQuery.setCancelable(true);
    }

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnQuery) {
                NetworkDiagnosis networkDiagnosis = new NetworkDiagnosis();
                if (networkDiagnosis.checkNetwork(getActivity())) {
                    number = cetNumber.getText().toString();
                    name = cetName.getText().toString();
                    if (number.length() != 15) {
                        showSnackbar(llCet, getResources().getString(R.string.tips_1));
                    } else if (name.length() == 0) {
                        showSnackbar(llCet, getResources().getString(R.string.tips_2));
                    } else {
                        pdQuery.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getScore(number, name);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        }
    };

    private void getScore(String number, String name) throws IOException {
        StringBuilder content = new StringBuilder();
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        client.setFollowSslRedirects(false);
        Request request = new Request.Builder().url("http://www.chsi.com.cn/cet/query?zkzh=" + number + "&xm=" + name)
                .addHeader("Referer", "http://www.chsi.com.cn/cet/").build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                parseScore(content.toString());
                Log.e("getScore", "获取成绩成功!");
            } else {
                Log.e("getScore", "获取成绩失败!状态码:" + response.code());
            }
        } catch (IOException e) {
            Log.e("getScore", "获取成绩失败!捕获异常:" + e.toString());
        }
    }

    private void parseScore(String content) {
        Document document = Jsoup.parse(content);
        Element element = document.getElementsByClass("error").first();
        if ((element != null)) {
            Log.e("ele", element.text());
            if ((element.text().equals(getResources()
                    .getString(R.string.tips_4)))) {
                pdQuery.dismiss();
                Looper.prepare();
                showSnackbar(llCet, getResources().getString(R.string.tips_4));
                Looper.loop();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.tips_3));
                builder.setPositiveButton(getResources()
                        .getString(R.string.yes), null);
                pdQuery.dismiss();
                Looper.prepare();
                builder.show();
                Looper.loop();
            }
        } else {
            Elements elements = document.getElementsByTag("tr");
            for (Element item : elements) {
                if (item.getElementsByTag("th").text().equals(getResources().getString(R.string.name_3))) {
                    name = item.getElementsByTag("td").text();
                } else if (item.getElementsByTag("th").text().equals(getResources().getString(R.string.school))) {
                    school = item.getElementsByTag("td").text();
                } else if (item.getElementsByTag("th").text().equals(getResources().getString(R.string.type))) {
                    type = item.getElementsByTag("td").text();
                } else if (item.getElementsByTag("th").text().equals(getResources().getString(R.string.id_2))) {
                    number = item.getElementsByTag("td").text();
                } else if (item.getElementsByTag("th").text().equals(getResources().getString(R.string.time))) {
                    time = item.getElementsByTag("td").text();
                } else if (item.getElementsByTag("th").text().equals(getResources().getString(R.string.sum_2))) {
                    String result = item.getElementsByTag("td").text();
                    String[] score = result.split(" ");
                    sum = score[0];
                    hear = score[2];
                    read = score[4];
                    write = score[6];
                    Message msg = new Message();
                    msg.obj = name + "!" + school + "!" + type + "!" + number + "!" + sum + "!" + hear + "!" + read + "!" + write;
                    handler.sendMessage(msg);
                }
            }
        }
    }
}
