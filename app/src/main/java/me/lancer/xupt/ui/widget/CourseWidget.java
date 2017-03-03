package me.lancer.xupt.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.RemoteViews;

import java.util.Calendar;

import me.lancer.xupt.R;
import me.lancer.xupt.ui.activity.LoginEduActivity;

/**
 * Created by HuangFangzhi on 2017/3/3.
 */

public class CourseWidget extends AppWidgetProvider {

    private static HandlerThread workerThread;
    private static Handler workerQueue;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        workerThread = new HandlerThread("CourseWidget-worker");
        workerThread.start();
        workerQueue = new Handler(workerThread.getLooper());
        performUpdate(context, appWidgetManager, appWidgetIds, null);
    }

    private void performUpdate(Context context, final AppWidgetManager awm, final int[] appWidgetIds, long[] changedEvents) {

        for (final int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, CourseService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            Calendar c = Calendar.getInstance();
            String month = String.valueOf(c.get(Calendar.MONTH) + 1);
            String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            String week = "";
            switch (c.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    week = "周日";
                    break;
                case 2:
                    week = "周一";
                    break;
                case 3:
                    week = "周二";
                    break;
                case 4:
                    week = "周三";
                    break;
                case 5:
                    week = "周四";
                    break;
                case 6:
                    week = "周五";
                    break;
                case 7:
                    week = "周六";
                    break;
            }

            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.course_widget);
            views.setOnClickPendingIntent(R.id.ll_widget, PendingIntent.getActivity(context, 0, new Intent(context, LoginEduActivity.class), 0));
            views.setTextViewText(R.id.tv_weekday, week);
            views.setTextViewText(R.id.tv_date, month + "月" + day + "日");
            views.setRemoteAdapter(R.id.lv_course, intent);
//            workerQueue.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    views.setScrollPosition(R.id.lv_course, 0);
//                    awm.partiallyUpdateAppWidget(appWidgetId, views);
//                }
//
//            }, 1000);
            awm.updateAppWidget(appWidgetId, views);
            awm.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_course);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }
}
