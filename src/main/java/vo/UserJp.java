package vo;

import dao.SqlUtils;
import dao.UserDao;
import po.User;
import utils.CommonUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserJp extends JPanel {
    private final UserDao userDao;
    EmployeeSoftware employeeSoftware;
    JTable userTable;

    public UserJp(EmployeeSoftware employeeSoftware) {
        userDao = new UserDao();
        this.employeeSoftware = employeeSoftware;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //制作表格JTable
        userTable = CommonUtils.getJTable("SELECT * FROM users",
                new String[]{"UserId", "UserName", "PassWord", "Consumption", "Note", "VipGrade"},
                new String[]{"用户号", "用户名", "密码", "累计消费", "备注", "会员等级"});
        JScrollPane jScrollPane = new JScrollPane(userTable);

        //修改栏
        Box box = Box.createVerticalBox();
        JTextField idText = new JTextField(10);
        JTextField nameText = new JTextField(10);
        JTextField passText = new JTextField(10);
        JTextField consumText = new JTextField(10);
        JTextField noteText = new JTextField(10);
        Box upLine = Box.createHorizontalBox();
        upLine.add(new JLabel("用户号:"));
        upLine.add(idText);
        upLine.add(new JLabel("用户名:"));
        upLine.add(nameText);
        upLine.add(new JLabel("密码:"));
        upLine.add(passText);

        Box downLine = Box.createHorizontalBox();
        downLine.add(new JLabel("累计消费:"));
        downLine.add(consumText);
        downLine.add(new JLabel("备注:"));
        downLine.add(noteText);
        //操作按钮
        downLine.add(new JButton(new AbstractAction("添加") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isExistInUsers(idText.getText())) {
                    JOptionPane.showMessageDialog(null, "该用户号已存在。您可以选择置空", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                }
                if (isExistInUsersName(nameText.getText())) {
                    JOptionPane.showMessageDialog(null, "该用户名已存在", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String level = getLevelByConsumption(Float.parseFloat(consumText.getText()));
                    userDao.addUser(new User(null, nameText.getText(), passText.getText(), Double.parseDouble(consumText.getText()), noteText.getText(), level, "0"));
                    employeeSoftware.flush();
                    JOptionPane.showMessageDialog(null, "添加成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }));
        downLine.add(new JButton(new AbstractAction("修改") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "UPDATE users SET userName=?,password=?,consumption=?,note=?,vipGrade=? WHERE userId=?";
                boolean success = SqlUtils.update(sql, nameText.getText(), passText.getText(), consumText.getText(), noteText.getText(), getLevelByConsumption(Float.parseFloat(consumText.getText())), idText.getText());
                if (success) {
                    employeeSoftware.flush();
                    JOptionPane.showMessageDialog(null, "修改成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }));
        downLine.add(new JButton(new AbstractAction("删除") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "delete from users where userId=?";
                boolean success = SqlUtils.update(sql, idText.getText());
                if (success) {
                    employeeSoftware.flush();
                    JOptionPane.showMessageDialog(null, "删除成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }));

        //选中监听器
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = userTable.getSelectedRow();
                Object a = userTable.getValueAt(selectedRow, 0);
                Object b = userTable.getValueAt(selectedRow, 1);
                Object c = userTable.getValueAt(selectedRow, 2);
                Object d = userTable.getValueAt(selectedRow, 3);
                Object ee = userTable.getValueAt(selectedRow, 4);
                idText.setText(a.toString());
                nameText.setText(b.toString());
                passText.setText(c.toString());
                consumText.setText(d.toString());
                if (ee != null) {
                    noteText.setText(ee.toString());
                } else {
                    noteText.setText(null);
                }
            }
        });

        add(jScrollPane);
        box.add(upLine);
        box.add(downLine);
        add(box);
    }

    //根据累计消费计算VIP等级
    private String getLevelByConsumption(float consumption) {
        String level = "1";
        if (consumption > 600) {
            level = "4";
        } else if (consumption > 400) {
            level = "3";
        } else if (consumption > 200) {
            level = "2";
        }
        return level;
    }

    //判断是否存在此用户号
    private boolean isExistInUsers(String userId) {
        return userDao.findUserById(userId) != null;
    }

    //判断是否存在此用户名
    private boolean isExistInUsersName(String userName) {
        return userDao.findUserByName(userName) != null;
    }
}
