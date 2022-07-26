package vo.store;

import LoginSet.LoginPerson;
import dao.SqlUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.swing.*;

@AllArgsConstructor
@NoArgsConstructor
public class AddOrderHandler extends Handler{
    String bookId;
    String num;
    String orderPrice;
    @Override
    public void handleRequest() {
        //订单表添加一条记录
        String sql = "insert into orders " +
                "values(?,?,?,CURDATE(),?, (SELECT employeeId " +
                "FROM employee,users WHERE users.userName=employee.Employee AND UserId= ?),NULL)";
        boolean success = SqlUtils.update(sql, getNextOrderId(), bookId, num, orderPrice, LoginPerson.getInstance().getUserId());
        if (success) {
            JOptionPane.showMessageDialog(null, "进货成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String getNextOrderId() {
        String sql = "SELECT MAX(orderId) FROM orders";
        long count = Long.parseLong((String)SqlUtils.selectForOneResult(sql));
        return String.valueOf((count + 1));
    }

}
