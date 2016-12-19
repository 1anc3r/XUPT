package me.lancer.xupt.mvp.book;

import java.util.List;

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
        view.showLoad();
        model.search(keyword);
    }

    @Override
    public void searchSuccess(List<BookBean> list) {
        view.search(list);
        view.hideLoad();
    }

    @Override
    public void searchFailure(String log) {
        view.showMsg(log);
        view.hideLoad();
    }

    public void rank(String type, String size) {
        view.showLoad();
        model.rank(type, size);
    }

    @Override
    public void rankSuccess(List<BookBean> list) {
        view.rank(list);
        view.hideLoad();
    }

    @Override
    public void rankFailure(String log) {
        view.showMsg(log);
        view.hideLoad();
    }
}
