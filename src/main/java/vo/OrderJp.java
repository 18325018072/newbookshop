package vo;

import dao.SqlUtils;
import utils.CommonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OrderJp extends JPanel {
    JTable ordersTable;
    EmployeeSoftware employeeSoftware;

    public OrderJp(EmployeeSoftware employeeSoftware) {
        this.employeeSoftware = employeeSoftware;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //建表
        ordersTable = CommonUtils.getJTable("select * from orders",
                new String[]{"OrderId", "BookId", "OrderNum", "OrderDate", "OrderPrice", "EmployeeId", "Note"},
                new String[]{"订单号", "书号", "进货量", "进货日期", "进价", "员工号", "备注"});
        JScrollPane jScrollPane = new JScrollPane(ordersTable);

        //备注栏
        JTextArea noteText = new JTextArea(2, 60);
        noteText.setFont(new Font("宋体", Font.PLAIN, 16));
        JScrollPane noteScrollPane = new JScrollPane(noteText);
        Box noteBox = Box.createHorizontalBox();
        noteBox.add(new JLabel("备  注："));
        noteBox.add(noteScrollPane);

        //订单号栏
        Box orderBox = Box.createHorizontalBox();
        JTextField idText = new JTextField();
        idText.setSize(5, 5);
        orderBox.add(new JLabel("订单号： "));
        orderBox.add(idText);

        //按钮
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(new JButton(new AbstractAction("修改备注") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isOrderExist(idText.getText())) {
                    String sql = "UPDATE orders SET Note= ? WHERE OrderId = ?";
                    boolean success = SqlUtils.update(sql, noteText.getText(), idText.getText());
                    if (success) {
                        employeeSoftware.flush();
                        JOptionPane.showMessageDialog(null, "修改成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "订单号不存在", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }));
        buttonBox.add(new JButton(new AbstractAction("删除") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isOrderExist(idText.getText())) {
                    String sql = "delete from orders where orderId=?";
                    boolean success = SqlUtils.update(sql, idText.getText());
                    if (success) {
                        employeeSoftware.flush();
                        JOptionPane.showMessageDialog(null, "删除成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "订单号不存在", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }));

        //组装修改栏
        Box totalBox = Box.createVerticalBox();
        totalBox.add(orderBox);
        totalBox.add(noteBox);
        totalBox.add(buttonBox);

        //选中监听器
        ordersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = ordersTable.getSelectedRow();
                Object a = ordersTable.getValueAt(selectedRow, 0);
                Object g = ordersTable.getValueAt(selectedRow, 6);
                idText.setText(a.toString());
                if (g != null) {
                    noteText.setText(g.toString());
                } else {
                    noteText.setText(null);
                }
            }
        });

        add(jScrollPane);
        add(totalBox);
    }

    //判断某书是否已写入档案
    private boolean isOrderExist(String orderId) {
        String sql = "select bookId from orders where orderId=?";
        String s = (String) SqlUtils.selectForOneResult(sql, orderId);
        if (s == null) {
            return false;
        } else {
            return true;
        }
    }
}
