package LoginSet;

import dao.SqlUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChangePassword  {
    public  ChangePassword(){
        JFrame jf_Register = new JFrame("修改密码");
        Box vBox = Box.createVerticalBox();
        //窗口在屏幕中间显示
        jf_Register.setLocationRelativeTo(null);

        //用户名
        Box uBox = Box.createHorizontalBox();
        JLabel uLabel = new JLabel("用户名：");
        JTextField uField = new JTextField(15);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uLabel);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uField);
        uBox.add(Box.createHorizontalStrut(20));
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(uBox);

        //旧密码
        Box oBox = Box.createHorizontalBox();
        JLabel oLabel = new JLabel("旧密码：");
        JTextField oField = new JTextField(15);
        oBox.add(Box.createHorizontalStrut(20));
        oBox.add(oLabel);
        oBox.add(Box.createHorizontalStrut(20));
        oBox.add(oField);
        oBox.add(Box.createHorizontalStrut(20));
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(oBox);

        //新密码
        Box pBox = Box.createHorizontalBox();
        JLabel pLabel = new JLabel("新密码：");
        JTextField pField = new JTextField(15);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(pLabel);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(pField);
        pBox.add(Box.createHorizontalStrut(20));
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pBox);

        //按钮
        Box btnBox = Box.createHorizontalBox();
        btnBox.add(Box.createHorizontalStrut(20));
        btnBox.add(new JButton(new AbstractAction("确认修改") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "UPDATE users SET password =?  WHERE userName =? AND  password =?";
                Boolean success = SqlUtils.update(sql, pField.getText(),uField.getText(), oField.getText());
                if (success) {
                    JOptionPane.showMessageDialog(jf_Register, "修改成功，即将返回登录界面",
                            "成功", JOptionPane.INFORMATION_MESSAGE);
                    jf_Register.dispose();
                } else {
                    JOptionPane.showMessageDialog(jf_Register, "修改失败，请检查信息",
                            "失败", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }));

        btnBox.add(Box.createHorizontalStrut(80));
        btnBox.add(new JButton(new AbstractAction("返回登录页面") {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf_Register.dispose();
            }
        }));
        vBox.add(Box.createVerticalStrut(20));
        btnBox.add(Box.createHorizontalStrut(20));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(20));

        jf_Register.add(vBox);

        jf_Register.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf_Register.setVisible(true);
        jf_Register.pack();
    }

}
