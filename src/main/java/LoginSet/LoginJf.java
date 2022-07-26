package LoginSet;

import dao.SqlUtils;
import po.User;
import vo.ClientSoftware;
import vo.EmployeeSoftware;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

public class LoginJf {
    SqlUtils sqlUtils = new SqlUtils();

    public LoginJf() {
        JFrame loginJf = new JFrame("登录");
        //窗口在屏幕中间显示
        loginJf.setLocation(800, 400);

        //设置窗口图标
        URL imgURL = LoginJf.class.getResource("/images/dog.jpg");
        ImageIcon imgIcon = new ImageIcon(imgURL);
        Image img = imgIcon.getImage();
        loginJf.setIconImage(img);

        Box vBox = Box.createVerticalBox();

        //设置字体
        Font youYuanFont = new Font("幼圆", Font.PLAIN, 18);
        UIManager.put("Button.font", youYuanFont);
        UIManager.put("Label.font", youYuanFont);
        UIManager.put("OptionPane.font", youYuanFont);
        UIManager.put("Panel.font", youYuanFont);
        UIManager.put("ScrollPane.font", youYuanFont);
        UIManager.put("TabbedPane.font", youYuanFont);
        UIManager.put("Table.font", youYuanFont);
        UIManager.put("TextField.font", youYuanFont);

        //组装用户名
        Box uBox = Box.createHorizontalBox();
        JLabel uLabel = new JLabel("用户名：");
        JTextField uField = new JTextField(15);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uLabel);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uField);
        uBox.add(Box.createHorizontalStrut(20));
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(uBox);

        //组装密码
        Box pBox = Box.createHorizontalBox();
        JLabel pLabel = new JLabel("密  码：");
        JTextField pField = new JTextField(15);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(pLabel);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(pField);
        pBox.add(Box.createHorizontalStrut(20));
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pBox);

        //添加按钮
        Box hBox3 = Box.createHorizontalBox();
        hBox3.add(Box.createHorizontalStrut(20));
        hBox3.add(new JButton(new AbstractAction("登录") {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = LoginPerson.getInstance(uField.getText(), pField.getText());
                if (user != null) {
                    //登录界面消失
                    loginJf.dispose();
                    //id<=0，用户为员工，启动员工端；id>0，普通用户，启动客户端
                    if (Integer.parseInt(user.getUserId()) > 0) {
                        new ClientSoftware();
                    } else {
                        new EmployeeSoftware();
                    }
                }
            }
        }));
        hBox3.add(Box.createHorizontalStrut(10));
        hBox3.add(new JButton(new AbstractAction("注册") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Register();
            }
        }));
        hBox3.add(Box.createHorizontalStrut(10));
        hBox3.add(new JButton(new AbstractAction("修改密码") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChangePassword();
            }
        }));
        hBox3.add(Box.createHorizontalStrut(20));
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(hBox3);
        vBox.add(Box.createVerticalStrut(30));

        loginJf.add(vBox);
        loginJf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginJf.setVisible(true);
        loginJf.pack();
    }

}