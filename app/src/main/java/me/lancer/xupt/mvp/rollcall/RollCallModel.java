package me.lancer.xupt.mvp.rollcall;

import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class RollCallModel {

    IRollCallPresenter presenter;

    public RollCallModel(IRollCallPresenter presenter) {
        this.presenter = presenter;
    }

    public void getDetail(String start, String end, String status, String flag, String page, String cookie0, String cookie1) {
        String url = "http://jwkq.xupt.edu.cn:8080/User/GetAttendList";
        OkHttpClient client = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("WaterDate", start + "a" + end);
        builder.add("Status", status);
        builder.add("Flag", flag);
        builder.add("page", page);
        builder.add("rows", "50");
        Request request = new Request.Builder().url(url)
                .addHeader("Cookie", cookie0 + "; " + cookie1).post(builder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            List<RollCallBean> list;
            if (response.isSuccessful()) {
                list = getDetailFromContent(response.body().string());
                if (list != null) {
                    presenter.getDetailSuccess(list);
                } else {
                    presenter.getDetailSuccess(null);
                }
            } else {
                Log.e("getDetail", "获取信息失败!");
            }
        } catch (Exception e) {
            presenter.getDetailFailure("获取信息失败!捕获异常:" + e.toString());
            Log.e("getDetail", "获取信息失败!捕获异常:" + e.toString());
        }
    }

    public void getStatistic(String cookie0, String cookie1) {
        String url = "http://jwkq.xupt.edu.cn:8080/User/GetAttendRepList";
        OkHttpClient client = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("json", "true");
        Request request = new Request.Builder().url(url)
                .addHeader("Cookie", cookie0 + "; " + cookie1).post(builder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            List<RollCallBean> list;
            if (response.isSuccessful()) {
                list = getStatisticFromContent(response.body().string());
                if (list != null) {
                    presenter.getStatisticSuccess(list);
                } else {
                    presenter.getStatisticSuccess(null);
                }
            } else {
                Log.e("getStatistic", "获取信息失败!");
            }
        } catch (Exception e) {
            presenter.getStatisticFailure("获取信息失败!捕获异常:" + e.toString());
            Log.e("getStatistic", "获取信息失败!捕获异常:" + e.toString());
        }
    }

    public void appeal(RollCallBean rollcall, String remark, String state, String cookie0, String cookie1){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://jwkq.xupt.edu.cn:8080/Apply/ApplyData");
            JSONObject jbAppeal = new JSONObject();
            jbAppeal.put("Class_no", rollcall.getRollCallClassNo());
            jbAppeal.put("S_Date", rollcall.getRollCallDate());
            jbAppeal.put("Jc", rollcall.getRollCallTime());
            jbAppeal.put("S_Code", rollcall.getRollCallSBH());
            jbAppeal.put("R_BH", rollcall.getRollCallRBH());
            jbAppeal.put("Term", rollcall.getRollCallTerm());
            jbAppeal.put("Remark", remark);
            jbAppeal.put("S_Status", rollcall.getRollCallState());
            jbAppeal.put("A_Status", state);
            Log.e("js", jbAppeal.toString());
            post.setEntity(new StringEntity(jbAppeal.toString()));
            post.setHeader("Cookie", cookie0 + "; " + cookie1);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(entity, "utf-8").toString();
                try {
                    jbAppeal = new JSONObject(content);
                    if (jbAppeal.getBoolean("IsSucceed")){
                        presenter.appealSuccess("申诉成功!");
                    }else {
                        presenter.appealFailure("申诉失败!错误信息:" + jbAppeal.getString("Msg"));
                        Log.e("login", "申诉失败!错误信息:" + jbAppeal.getString("Msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                presenter.appealFailure("申诉失败!状态码:" + response.getStatusLine().getStatusCode());
                Log.e("login", "申诉失败!状态码:" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            presenter.appealFailure("申诉失败!捕获异常:" + e.toString());
            Log.e("login", "申诉失败!捕获异常:" + e.toString());
        }
    }

    private List<RollCallBean> getDetailFromContent(String content) {
        try {
            JSONObject jbDetail = new JSONObject(content);
            int total = jbDetail.getInt("total");
            if (total > 0) {
                JSONArray jaRows = jbDetail.getJSONArray("rows");
                List<RollCallBean> list = new ArrayList<>();
                if (jaRows.length() == 0) {
                    return null;
                } else {
                    for (int i = 0; i < jaRows.length(); i++) {
                        RollCallBean bbItem = new RollCallBean();
                        JSONObject jbItem = jaRows.getJSONObject(i);
                        bbItem.setRollCallCourseName(jbItem.getString("S_Name"));
                        bbItem.setRollCallCourseCode(jbItem.getString("S_Code"));
                        bbItem.setRollCallDate(jbItem.getString("WaterDate"));
                        bbItem.setRollCallTerm(jbItem.getString("Term_No"));
                        bbItem.setRollCallTime(jbItem.getString("JT_No"));
                        bbItem.setRollCallRoom(jbItem.getString("RoomNum"));
                        bbItem.setRollCallBuild(jbItem.getString("BName"));
                        bbItem.setRollCallState(jbItem.getInt("Status"));
                        bbItem.setRollCallCardTime(jbItem.getString("WaterTime"));
                        bbItem.setRollCallClassNo(jbItem.getInt("Class_No"));
                        bbItem.setRollCallSBH(jbItem.getInt("SBH"));
                        bbItem.setRollCallRBH(jbItem.getInt("RBH"));
                        list.add(bbItem);
                    }
                    return list;
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<RollCallBean> getStatisticFromContent(String content) {
        try {
            JSONObject jbStatistic = new JSONObject(content);
            JSONArray jaObj = jbStatistic.getJSONArray("Obj");
            List<RollCallBean> list = new ArrayList<>();
            if (jaObj.length() == 0) {
                return null;
            } else {
                for (int i = 0; i < jaObj.length(); i++) {
                    RollCallBean bbItem = new RollCallBean();
                    JSONObject jbItem = jaObj.getJSONObject(i);
                    bbItem.setRollCallCourseName(jbItem.getString("CourseName"));
                    bbItem.setRollCallTotal(jbItem.getInt("Total"));
                    bbItem.setRollCallShouldAttend(jbItem.getInt("ShouldAttend"));
                    bbItem.setRollCallAttend(jbItem.getInt("Attend"));
                    bbItem.setRollCallLate(jbItem.getInt("Late"));
                    bbItem.setRollCallAbsence(jbItem.getInt("Absence"));
                    list.add(bbItem);
                }
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
