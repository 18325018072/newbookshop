package vo.store;

import dao.SqlUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import utils.CommonUtils;

import javax.swing.*;

@AllArgsConstructor
@NoArgsConstructor
public class InspectorHandler extends Handler {
    String orderPrice;
    String bookId;

    @Override
    public void handleRequest() {
        if (!CommonUtils.isNumeric(orderPrice)) {
            JOptionPane.showMessageDialog(null, "进货请输入正确的进价,它至少得算个数", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
            return;
        } else if (!isExistInBook(bookId)) {
            JOptionPane.showMessageDialog(null, "请先在图书中心登记此书", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
            return;
        } else {
            this.successor.handleRequest();
        }
    }

    //判断某书是否已写入档案
    private boolean isExistInBook(String bookId) {
        String sql = "select publisher from books where bookId=?";
        String s = (String) SqlUtils.selectForOneResult(sql, bookId);
        if (s == null) {
            return false;
        } else {
            return true;
        }
    }

}