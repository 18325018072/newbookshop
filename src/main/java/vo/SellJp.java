package vo;

import LoginSet.LoginJf;
import LoginSet.LoginPerson;
import dao.SqlUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SellJp extends JPanel {
    boolean isCheckable=false;
    public SellJp() {
        setLayout(new BorderLayout());

        //工作栏
        //书Box
        Box bookTipBox = Box.createVerticalBox();
        JLabel bookTipLabel = new JLabel(" ");
        bookTipBox.add(bookTipLabel);
        JTextField bIdText = new JTextField(20);
        bookTipBox.add(bIdText);
        bookTipBox.add(Box.createVerticalStrut(15));
        //数量Box
        Box numTipBox = Box.createVerticalBox();
        JLabel numTipLabel = new JLabel(" ");
        numTipBox.add(numTipLabel);
        JTextField numText = new JTextField(20);
        numTipBox.add(numText);
        numTipBox.add(Box.createVerticalStrut(15));
        //用户Box
        Box userTipBox = Box.createVerticalBox();
        JLabel userTipLabel = new JLabel(" ");
        userTipBox.add(userTipLabel);
        JTextField uIdText = new JTextField(20);
        userTipBox.add(uIdText);
        userTipBox.add(Box.createVerticalStrut(15));
        //绑定监听器：辅助员工判断
        bIdText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}
            @Override
            public void focusLost(FocusEvent e) {
                String sql = "select bookName from books where bookId=?";
                String s = (String) SqlUtils.selectForOneResult(sql, bIdText.getText());
                if (s != null) {
                    bookTipLabel.setText(s+"√");
                    isCheckable=true;
                }else{
                    isCheckable=false;
                }
            }
        });
        numText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}
            @Override
            public void focusLost(FocusEvent e) {
                if (isCheckable) {
                    if (isEnoughInStore(bIdText.getText(), numText.getText())) {
                        numTipLabel.setText("库存充足"+"√");
                    }else {
                        numTipLabel.setText("库存不足"+"×");
                    }
                }
            }
        });
        uIdText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}
            @Override
            public void focusLost(FocusEvent e) {
                String sql = "select userName from users where userId=?";
                String s = (String) SqlUtils.selectForOneResult(sql, uIdText.getText());
                if (s != null) {
                    userTipLabel.setText(s+"√");
                    userTipLabel.setVisible(true);
                }
            }
        });

        Box box = Box.createHorizontalBox();
        box.add(new JLabel("书号："));
        box.add(bookTipBox);
        box.add(new JLabel("数量："));
        box.add(numTipBox);
        box.add(new JLabel("用户号："));
        box.add(userTipBox);
        box.add(new JButton(new AbstractAction("确认购买") {
            @Override
            public void actionPerformed(ActionEvent e) {
                sellBook(bIdText, numText, uIdText);
            }
        }));
        JLabel pic = new JLabel(new ImageIcon(LoginJf.class.getResource("/images/trainWithLog6.jpg")));
        add(pic);
        add(box, BorderLayout.SOUTH);
    }

    private void sellBook(JTextField bIdText, JTextField numText, JTextField uIdText) {
        if (isEnoughInStore(bIdText.getText(), numText.getText())) {
            //1st:生成销售记录
            String sql1 = "INSERT INTO sells VALUES(?,?,?, CURDATE(), \n" +
                    "(SELECT nowPrice*discount FROM books,users,vips WHERE users.vipGrade=vips.vipGrade AND bookId=? AND userId=?) \n" +
                    ", ?, (SELECT employeeId FROM employee,users WHERE users.userName=employee.employee AND userId= ?));";
            //2ed:减少库存
            String sql2 = "UPDATE store SET BookNum = (SELECT bookNum FROM(SELECT bookNum FROM store  WHERE bookId=?) b )-? WHERE bookId=?;";
            //3rd:更新用户累计消费
            String sql3 = "UPDATE users SET consumption=(SELECT consumption FROM(SELECT consumption FROM users WHERE userId=?) b )+\n" +
                    "(SELECT c FROM(SELECT nowPrice*discount c FROM books,users,vips WHERE users.vipGrade=vips.vipGrade AND bookId=? AND userId=?) a ) *1 WHERE userId=?;";
            boolean success1 = SqlUtils.update(sql1, getNextSellId(), bIdText.getText(), numText.getText(), bIdText.getText(), uIdText.getText(), uIdText.getText(), LoginPerson.getInstance().getUserId());
            boolean success2 = SqlUtils.update(sql2, bIdText.getText(), numText.getText(), bIdText.getText());
            boolean success3 = SqlUtils.update(sql3, uIdText.getText(), bIdText.getText(), uIdText.getText(), uIdText.getText());
            if (success1 && success2 && success3) {
                JOptionPane.showMessageDialog(null, "购买成功！", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    //用户购买时判断是否有足够的库存
    private boolean isEnoughInStore(String bookId, String sellNum) {
        //查询有多少库存
        String sql = "select bookNum from store where bookId=?";
        int count = 0;
        try {
            count = (int) SqlUtils.selectForOneResult(sql, bookId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "该书无库存", "提示", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        //比一下，库存够不够
        if (count < Integer.parseInt(sellNum)) {
            JOptionPane.showMessageDialog(null, "该书库存不够", "提示", JOptionPane.INFORMATION_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    //新建销售记录时生成新销售记录号
    private String getNextSellId() {
        String sql = "SELECT MAX(sellId) FROM sells";
        long count = Long.parseLong((String) SqlUtils.selectForOneResult(sql));
        return String.valueOf((count + 1));
    }
}
