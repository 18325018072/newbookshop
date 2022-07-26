package vo.store;

import dao.SqlUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.swing.*;

@AllArgsConstructor
@NoArgsConstructor
public class AddStoreHandler extends Handler {
    String bookId;
    String num;

    @Override
    public void handleRequest() {
        boolean success;
        //添加书到仓库
        if (isExistInStore(bookId)) {
            //仓库有本书的仓位,则直接添加书
            String sql = "UPDATE store SET bookNum = ? WHERE bookId = ?";
            success = SqlUtils.update(sql, String.valueOf(getStoreNum(bookId) + Integer.parseInt(num)), bookId);
        } else {
            //如果仓库没有这本书，创建仓位,并添加书
            String sql = "insert into store values(?,?)";
            success = SqlUtils.update(sql, num, bookId);
        }
        //添加成功跳转
        if (success) {
            this.successor.handleRequest();
        }else{
            JOptionPane.showMessageDialog(null, "仓库操作错误", "消息对话框", JOptionPane.ERROR_MESSAGE);
        }
    }

    //判断某书是否库存
    private boolean isExistInStore(String bookId) {
        String sql = "select bookNum from store where bookId=?";
        Integer s = (Integer) SqlUtils.selectForOneResult(sql, bookId);
        if (s == null) {
            return false;
        } else {
            return true;
        }
    }

    //查询书的库存
    private int getStoreNum(String bookId) {
        String sql = "SELECT bookNum FROM store WHERE bookId = ?";
        return (int) SqlUtils.selectForOneResult(sql, bookId);
    }
}
