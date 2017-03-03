package me.lancer.xupt.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.course.CourseBean;
import me.lancer.xupt.ui.application.ApplicationInstance;

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
            mCourseList = ((ApplicationInstance) mContext).getCourseList();
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
            String day = "", time = "", today = "";
            Calendar c = Calendar.getInstance();
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
                    day = "周一";
                    break;
                case 2:
                    day = "周二";
                    break;
                case 3:
                    day = "周三";
                    break;
                case 4:
                    day = "周四";
                    break;
                case 5:
                    day = "周五";
                    break;
                case 6:
                    day = "周六";
                    break;
                case 7:
                    day = "周七";
                    break;
            }
            if (position > 0 && (mCourseList.get(position).getCourseDay() == mCourseList.get(position - 1).getCourseDay())) {
                day = "";
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
            if (today.equals(day)) {
                views.setTextColor(R.id.tv_weekday, mContext.getResources().getColor(R.color.blue));
            } else {
                views.setTextColor(R.id.tv_weekday, mContext.getResources().getColor(R.color.black));
            }
            views.setTextViewText(R.id.tv_weekday, day);
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
    }
}