package me.lancer.xupt.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.course.CourseBean;
import me.lancer.xupt.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2017/3/3.
 */

public class CourseService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CourseWidgetFactory(getApplicationContext(), intent);
    }

    public static class CourseWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private List<CourseBean> mCourseList;

        public CourseWidgetFactory(Context context, Intent intent) {
            mContext = context;
            if ((mCourseList = getCourse(context)) != null) {
                Log.e("remoteView", "加载课表成功!");
                Collections.sort(mCourseList, new Comparator<CourseBean>() {

                    public int compare(CourseBean cb1, CourseBean cb2) {
                        if (cb1.getCourseDay() > cb2.getCourseDay()) {
                            return 2;
                        } else if (cb1.getCourseDay() == cb2.getCourseDay()) {
                            if (cb1.getCourseTime() > cb2.getCourseTime()) {
                                return 1;
                            } else if (cb1.getCourseTime() == cb2.getCourseTime()) {
                                return 0;
                            } else {
                                return -1;
                            }
                        } else {
                            return -2;
                        }
                    }
                });
            }
        }

        @Override
        public int getCount() {
            return mCourseList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position < 0 || position >= getCount()) {
                return null;
            }
            String weekday = "", time = "", today = "";
            Calendar c = Calendar.getInstance();
//            int minuteman = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
//            Log.e("minuteman", minuteman+"");
            switch (c.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    today = "周日";
                    break;
                case 2:
                    today = "周一";
                    break;
                case 3:
                    today = "周二";
                    break;
                case 4:
                    today = "周三";
                    break;
                case 5:
                    today = "周四";
                    break;
                case 6:
                    today = "周五";
                    break;
                case 7:
                    today = "周六";
                    break;
            }
            switch (mCourseList.get(position).getCourseDay()) {
                case 1:
                    weekday = "周一";
                    break;
                case 2:
                    weekday = "周二";
                    break;
                case 3:
                    weekday = "周三";
                    break;
                case 4:
                    weekday = "周四";
                    break;
                case 5:
                    weekday = "周五";
                    break;
                case 6:
                    weekday = "周六";
                    break;
                case 7:
                    weekday = "周七";
                    break;
            }
            if (position > 0 && (mCourseList.get(position).getCourseDay() == mCourseList.get(position - 1).getCourseDay())) {
                weekday = "";
            }
            switch (mCourseList.get(position).getCourseTime()) {
                case 1:
                    time = "08:00 - 09:45";
                    break;
                case 3:
                    time = "10:15 - 12:00";
                    break;
                case 5:
                    time = "14:30 - 16:15";
                    break;
                case 7:
                    time = "16:35 - 18:20";
                    break;
            }

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.course_item);
            if (today.equals(weekday)) {
                views.setTextColor(R.id.tv_weekday, mContext.getResources().getColor(R.color.blue));
//                if ((minuteman>8*60 && minuteman<9*60+45) || (minuteman>10*60+15 && minuteman<12*60)
//                        || (minuteman>14*60+30 && minuteman<16*60+15) || (minuteman>16*60+35 && minuteman<18*60+20)){
//                    views.setTextColor(R.id.tv_name, mContext.getResources().getColor(R.color.black));
//                    views.setTextColor(R.id.tv_time_place, mContext.getResources().getColor(R.color.black));
//                } else {
//                    views.setTextColor(R.id.tv_name, mContext.getResources().getColor(R.color.white));
//                    views.setTextColor(R.id.tv_time_place, mContext.getResources().getColor(R.color.white));
//                }
            } else {
                views.setTextColor(R.id.tv_weekday, mContext.getResources().getColor(R.color.black));
            }
            if (position == 0) {
                views.setViewVisibility(R.id.tv_top, View.VISIBLE);
            } else if (position == mCourseList.size() - 1) {
                views.setViewVisibility(R.id.tv_bottom, View.VISIBLE);
            } else {
                views.setViewVisibility(R.id.tv_top, View.GONE);
                views.setViewVisibility(R.id.tv_bottom, View.GONE);
            }
            views.setTextViewText(R.id.tv_weekday, weekday);
            views.setTextViewText(R.id.tv_name, mCourseList.get(position).getCourseName());
            views.setTextViewText(R.id.tv_time_place, time + "，" + mCourseList.get(position).getCourseClassroom());
            return views;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
        }

        public List<CourseBean> getCourse(Context context) {
            SharedPreferences sharedPreferences;
            sharedPreferences = context.getSharedPreferences(context.getString(R.string.spf_user), Context.MODE_PRIVATE);
            String number = sharedPreferences.getString(context.getString(R.string.spf_number), "");
            ContentGetterSetter contentGetterSetter = new ContentGetterSetter("course_", number);
            String path = Environment.getExternalStorageDirectory().toString();
            String content;
            List<CourseBean> list;
            if (!(content = contentGetterSetter.getContentFromFile(path)).contains("失败!")) {
                list = getCourseFromJson(content);
                return list;
            }
            return null;
        }

        public List<CourseBean> getCourseFromJson(String json) {
            try {
                JSONObject jbCourse = new JSONObject(json);
                JSONArray jaCourse = (JSONArray) jbCourse.get("course");
                List<CourseBean> list = new ArrayList<>();
                for (int i = 0; i < jaCourse.length(); i++) {
                    CourseBean cbItem = new CourseBean();
                    JSONObject jbItem = (JSONObject) jaCourse.get(i);
                    cbItem.setCourseTime((Integer) jbItem.get("time"));
                    cbItem.setCourseDay((Integer) jbItem.get("day"));
                    cbItem.setCourseColor((Integer) jbItem.get("color"));
                    cbItem.setCourseName((String) jbItem.get("name"));
                    cbItem.setCourseTeacher((String) jbItem.get("teacher"));
                    cbItem.setCourseClassroom((String) jbItem.get("classroom"));
                    list.add(cbItem);
                }
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}