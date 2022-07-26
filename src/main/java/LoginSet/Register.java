package LoginSet;

import dao.SqlUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

public class Register {
    public Register(){
        JFrame jf_Register = new JFrame("注册");
        //窗口在屏幕中间显示
        jf_Register.setLocationRelativeTo(null);
        Box vBox = Box.createVerticalBox();

        //组装用户名
        Box uBox = Box.createHorizontalBox();
        uBox.add(Box.createHorizontalStrut(20));
        JLabel uLabel = new JLabel("用户名：");
        JTextField uField = new JTextField(15);
        uBox.add(uLabel);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uField);
        uBox.add(Box.createHorizontalStrut(20));
        vBox.add(Box.createVerticalStrut(50));
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

        //按钮
        Box btnBox = Box.createHorizontalBox();
        btnBox.add(Box.createHorizontalStrut(20));
        btnBox.add(new JButton(new AbstractAction("注册") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "insert into users values(?,?,?,?,?,?,?)";
                boolean success = SqlUtils.update(sql,getNextUserId(), uField.getText(), pField.getText(), "0", null, "1","0");
                //如果注册成功则返回
                if (success) {
                    JOptionPane.showMessageDialog(jf_Register, "注册成功，即将返回登录界面",
                            "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                    jf_Register.dispose();
                } else {
                    JOptionPane.showMessageDialog(jf_Register, "注册异常",
                            "消息对话框", JOptionPane.ERROR_MESSAGE);
                }
            }
        }));
        btnBox.add(Box.createHorizontalStrut(50));
        btnBox.add(new JButton(new AbstractAction("返回登录页面") {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf_Register.dispose();
            }
        }));
        btnBox.add(Box.createHorizontalStrut(20));
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(20));

        jf_Register.add(vBox);
        jf_Register.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf_Register.setVisible(true);
        jf_Register.pack();
    }

    //用户注册时生成新用户号
    private String getNextUserId() {
        //查询所有的Id号
        String sql = "select userId from users";
        List<Map<String, Object>> mapList = SqlUtils.checkTable(sql);
        //在所有Id中选出最大的
        int maxId = 0;
        for (Map<String, Object> map : mapList) {
            int thisId=Integer.parseInt((String)map.get("userId"));
            if (thisId >maxId) {
                maxId=thisId;
            }
        }
        //返回Id号+1，即可用的新用户号
        return String.valueOf(maxId + 1);
    }
}
