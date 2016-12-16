package me.lancer.xupt.mvp.course;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.lancer.xupt.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class CourseModel {

    ICoursePresenter presenter;

    String number, name, cookie;

    public CourseModel(ICoursePresenter presenter) {
        this.presenter = presenter;
    }

    public void loadCourse(String number, String name, String cookie, boolean refresh) {
        this.number = number;
        this.name = name;
        this.cookie = cookie;
        ContentGetterSetter contentGetterSetter = new ContentGetterSetter("course_", number);
        String url = "http://222.24.19.201/xskbcx.aspx?xh=" + number + "&xm=" + name + "&gnmkdm=N121603";
        String path = Environment.getExternalStorageDirectory().toString();
        String content;
        List<CourseBean> list;
        if (!(content = contentGetterSetter.getContentFromFile(path)).contains("!error!") && !refresh) {
//            list = getCourseFromContent(content);
            list = getCourseFromJson(content);
            presenter.loadCourseSuccess(list);
            Log.e("loadCourse", "loadCourseSuccess.done");
        } else if (!(content = contentGetterSetter.getContentFromHtml(url, cookie)).contains("!error!") && refresh) {
            list = getCourseFromContent(content);
            content = setCourseToJson(list);
            contentGetterSetter.setContentToFile(path, content);
            presenter.loadCourseSuccess(list);
            Log.e("loadCourse", "loadCourseSuccess.done");
        } else {
            presenter.loadCourseFailure("loadCourseFailure.done");
            Log.e("loadCourse", "loadCourseFailure.done");
        }
    }

    public List<CourseBean> getCourseFromContent(String content) {
        List<CourseBean> courseList = new ArrayList<>();
        Document document = Jsoup.parse(content);
        Element element = document.getElementById("Table1");
        Elements elements = element.getElementsByTag("tr");
        HashMap<String, Integer> colorMap = new HashMap<>();
        int color = 0;
        for (int i = 2; i < elements.size(); i += 2) {
            Elements elements1 = elements.get(i).getElementsByAttributeValue("align", "Center");
            for (int j = 0; j < 7; j++) {
                String str = elements1.get(j).text();
                if (str.length() > 1) {
                    CourseBean item = new CourseBean();
                    item.setCourseTime(i - 1);
                    item.setCourseDay(j + 1);
                    String[] strs = str.split(" ");
                    String name = strs[0];
                    String teacher = strs[2];
                    String classroom = "";
                    if (strs.length > 3) {
                        classroom = strs[3];
                    }
                    item.setCourseName(name);
                    item.setCourseTeacher(teacher);
                    if (strs.length > 3) {
                        item.setCourseClassroom(classroom);
                    }
                    if (colorMap.get(name) != null) {
                        item.setCourseColor(colorMap.get(name));
                    } else {
                        item.setCourseColor(color);
                        colorMap.put(name, color);
                        if ((++color) == 7) {
                            color = 0;
                        }
                    }
                    courseList.add(item);
                }
            }
        }
        return courseList;
    }

    public List<CourseBean> getCourseFromJson(String json) {
        try {
            JSONObject jbCourse = new JSONObject(json);
            JSONArray jaCourse = (JSONArray) jbCourse.get("course");
            List<CourseBean> list = new ArrayList<>();
            for (int i = 0; i < jaCourse.length(); i++) {
                CourseBean item = new CourseBean();
                JSONObject jbItem = (JSONObject) jaCourse.get(i);
                item.setCourseTime((Integer) jbItem.get("time"));
                item.setCourseDay((Integer) jbItem.get("day"));
                item.setCourseColor((Integer) jbItem.get("color"));
                item.setCourseName((String) jbItem.get("name"));
                item.setCourseTeacher((String) jbItem.get("teacher"));
                item.setCourseClassroom((String) jbItem.get("classroom"));
                list.add(item);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String setCourseToJson(List<CourseBean> list) {
        try {
            JSONObject jbCourse = new JSONObject();
            JSONArray jaCourse = new JSONArray();
            for (CourseBean item : list) {
                JSONObject jbItem = new JSONObject();
                jbItem.put("time", item.getCourseTime());
                jbItem.put("day", item.getCourseDay());
                jbItem.put("color", item.getCourseColor());
                jbItem.put("name", item.getCourseName());
                jbItem.put("teacher", item.getCourseTeacher());
                jbItem.put("classroom", item.getCourseClassroom());
                jaCourse.put(jbItem);
            }
            jbCourse.put("course", jaCourse);
            return jbCourse.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "!error!----" + e.toString();
        }
    }
}
