package me.lancer.xupt.mvp.book;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class BookBean {

    private String bookId;// 图书馆内控制号
    private String bookISBN;//标准号
    private String bookMainTitle;// 标题
    private String bookSubTitle;// 副标题
    private String bookPublish;// 出版社
    private String bookAuthor;// 责任者，作者
    private String bookSort;// 图书馆索书号
    private String bookSubject; // 主题
    private String bookTotal;// 图书馆藏书数量
    private String bookAvaliable;// 可借阅数量
    private String bookBarCode;// 条形码
    private String bookDepartment;// 所在分馆
    private String bookState;// 当前状态
    private String bookDate;// 应还日期
    private boolean bookCanRenew;// 是否可续借
    private String bookDepartmentId;// 书库ID号，用于续借
    private String bookLibraryId;// 分馆ID号，用于续借
    private String bookImage;
    private String bookRank;
    private String bookBorNum;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public String getBookMainTitle() {
        return bookMainTitle;
    }

    public void setBookMainTitle(String bookMainTitle) {
        this.bookMainTitle = bookMainTitle;
    }

    public String getBookSubTitle() {
        return bookSubTitle;
    }

    public void setBookSubTitle(String bookSubTitle) {
        this.bookSubTitle = bookSubTitle;
    }

    public String getBookPublish() {
        return bookPublish;
    }

    public void setBookPublish(String bookPublish) {
        this.bookPublish = bookPublish;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookSort() {
        return bookSort;
    }

    public void setBookSort(String bookSort) {
        this.bookSort = bookSort;
    }

    public String getBookSubject() {
        return bookSubject;
    }

    public void setBookSubject(String bookSubject) {
        this.bookSubject = bookSubject;
    }

    public String getBookTotal() {
        return bookTotal;
    }

    public void setBookTotal(String bookTotal) {
        this.bookTotal = bookTotal;
    }

    public String getBookAvaliable() {
        return bookAvaliable;
    }

    public void setBookAvaliable(String bookAvaliable) {
        this.bookAvaliable = bookAvaliable;
    }

    public String getBookBarCode() {
        return bookBarCode;
    }

    public void setBookBarCode(String bookBarCode) {
        this.bookBarCode = bookBarCode;
    }

    public String getBookDepartment() {
        return bookDepartment;
    }

    public void setBookDepartment(String bookDepartment) {
        this.bookDepartment = bookDepartment;
    }

    public String getBookState() {
        return bookState;
    }

    public void setBookState(String bookState) {
        this.bookState = bookState;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public boolean isBookCanRenew() {
        return bookCanRenew;
    }

    public void setBookCanRenew(boolean bookCanRenew) {
        this.bookCanRenew = bookCanRenew;
    }

    public String getBookDepartmentId() {
        return bookDepartmentId;
    }

    public void setBookDepartmentId(String bookDepartmentId) {
        this.bookDepartmentId = bookDepartmentId;
    }

    public String getBookLibraryId() {
        return bookLibraryId;
    }

    public void setBookLibraryId(String bookLibraryId) {
        this.bookLibraryId = bookLibraryId;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookRank() {
        return bookRank;
    }

    public void setBookRank(String bookRank) {
        this.bookRank = bookRank;
    }

    public String getBookBorNum() {
        return bookBorNum;
    }

    public void setBookBorNum(String bookBorNum) {
        this.bookBorNum = bookBorNum;
    }
}
