package vo;

import LoginSet.LoginJf;

import javax.swing.*;


public class ClientSoftware {

    public ClientSoftware() {
        JFrame jf_Client = new JFrame("书店客户端");
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.LEFT, JTabbedPane.WRAP_TAB_LAYOUT);
        jf_Client.setLocationRelativeTo(null);

        ImageIcon imgIcon2 = new ImageIcon(LoginJf.class.getResource("/images/cat.png"));
        JOptionPane.showMessageDialog(jf_Client, "欢迎使用！\n本软件实现了书店销售业务中的各方面数据的管理，" +
                "目前暂未开放客户端，仅支持店员端。\n本客户端仅做测试使用，敬请谅解！",
                "使用手册", JOptionPane.INFORMATION_MESSAGE, imgIcon2);
        //添加标签
        tabbedPane.addTab("商品查询", new JList<>(new String[]{"商品一", "商品二", "商品三"}));
        tabbedPane.addTab("订单查询", new JList<>(new String[]{"订单一", "订单二", "订单三"}));

        //设置第二个标签默认选中
        tabbedPane.setSelectedIndex(1);
        //设置第一个标签不能用
        tabbedPane.setEnabledAt(0, false);

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            JOptionPane.showMessageDialog(jf_Client, "选中了第" + (selectedIndex + 1) + "个标签");
        });
        jf_Client.add(tabbedPane);
        jf_Client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf_Client.pack();
        jf_Client.setVisible(true);

    }
}
