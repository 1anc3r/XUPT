package me.lancer.xupt.mvp.book;

import java.util.List;
import java.util.Map;

import me.lancer.xupt.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class BookPresenter implements IBasePresenter<IBookView>, IBookPresenter {

    private IBookView view;
    private BookModel model;

    public BookPresenter(IBookView view) {
        attachView(view);
        model = new BookModel(this);
    }

    @Override
    public void attachView(IBookView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void search(String keyword) {
        if (view != null) {
            view.showLoad();
            model.search(keyword);
        }
    }

    @Override
    public void searchSuccess(List<BookBean> list) {
        if (view != null) {
            view.search(list);
            view.hideLoad();
        }
    }

    @Override
    public void searchFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void reviewer(int type, int pager) {
        if (view != null) {
            model.reviewer(type, pager);
        }
    }

    @Override
    public void reviewerSuccess(List<BookReviewer> list) {
        if (view != null) {
            view.reviewer(list);
            view.hideLoad();
        }
    }

    @Override
    public void reviewerFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void view(String url) {
        if (view != null) {
            model.view(url);
        }
    }

    @Override
    public void viewSuccess(BookReviewer item) {
        if (view != null) {
            view.view(item);
            view.hideLoad();
        }
    }

    @Override
    public void viewFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void rank(String type, String size) {
        if (view != null) {
            view.showLoad();
            model.rank(type, size);
        }
    }

    @Override
    public void rankSuccess(List<BookBean> list) {
        if (view != null) {
            view.rank(list);
            view.hideLoad();
        }
    }

    @Override
    public void rankFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void detail(int key, String value) {
        if (view != null) {
            view.showLoad();
            model.detail(key, value);
        }
    }

    @Override
    public void detailSuccess(Map<String, List<BookBean>> map) {
        if (view != null) {
            view.detail(map);
            view.hideLoad();
        }
    }

    @Override
    public void detailFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void addFavorite(String id, String cookie) {
        if (view != null) {
            view.showLoad();
            model.addFavorite(id, cookie);
        }
    }

    @Override
    public void addFavoriteSuccess(String log) {
        if (log != null && log.length() > 0) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    @Override
    public void addFavoriteFailure(String log) {
        if (log != null && log.length() > 0) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void delFavorite(String id, String number, String cookie) {
        if (view != null) {
            view.showLoad();
            model.delFavorite(id, number, cookie);
        }
    }

    @Override
    public void delFavoriteSuccess(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    @Override
    public void delFavoriteFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }
}
