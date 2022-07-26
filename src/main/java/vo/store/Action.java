package vo.store;

//配合动态代理模式需要，将对表格操作从StoreJp中解耦出来
public interface Action {
    void addBooks(String orderPriceText, String num);

    void deleteSomeBooks(String num);

    void deleteAllBooks();

}
