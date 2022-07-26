package vo;

import LoginSet.LoginJf;
import LoginSet.LoginPerson;
import dao.SqlUtils;
import dao.UserDao;
import po.User;
import vo.statistics.StatisticsJp;
import vo.store.StoreJp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

//员工端界面
public class EmployeeSoftware {
    private JFrame jfManager;
    JTabbedPane tabbedPane;

    public EmployeeSoftware() {
        JButton flushButton;
        JButton changeButton;

        //创建店员端界面
        jfManager = new JFrame("书店店员端");
        jfManager.setLocation(400, 200);
        jfManager.setFont(new Font("宋体", Font.ITALIC, 30));
        //设置背景
        jfManager.setContentPane(new CInstead());
        //设置图标
        URL imgURL = LoginJf.class.getResource("/images/dog.jpg");
        ImageIcon imgIcon = new ImageIcon(imgURL);
        Image img = imgIcon.getImage();
        jfManager.setIconImage(img);
        //关闭时登出
        jfManager.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {logout();}

            @Override
            public void windowClosed(WindowEvent e) {
                logout();
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        //弹窗：使用说明+版权声明
        URL imgURL2 = LoginJf.class.getResource("/images/cat.png");
        ImageIcon imgIcon2 = new ImageIcon(imgURL2);
        String id=LoginPerson.getInstance().getUserId();
        String job="用户";
        if ("0".equals(id)){
            job="店长";
        }else if (Integer.parseInt(id)<0){
            job="员工";
        }
        JOptionPane.showMessageDialog(jfManager, "尊敬的" + job + ":" +
                LoginPerson.getInstance().getUserName() +
                "，欢迎您的使用！\n本软件实现了书店销售业务中的各方面数据的管理，使用方法如下：" +
                "\n1、通过在页面中的数据文本框输入信息，在点击相应操作按钮，即可完成对数据的改动。" +
                "\n2、本软件支持对任何表格的所有字段进行排序，单击表头即可切换排序的顺序。" +
                "\n3、鼠标单击数据项即可自动将选中项的数据完整录入文本框。" +
                "\n4、更多细节，敬请探索！" +
                "\n联系我们：1420790977@qq.com" +
                "\n最后，祝您使用愉快！", "使用手册", JOptionPane.INFORMATION_MESSAGE, imgIcon2);

        //添加容器的内容1：主操作页面
        tabbedPane = new JTabbedPane(SwingConstants.LEFT, JTabbedPane.WRAP_TAB_LAYOUT);
        //设置透明度
        tabbedPane.setBackground(new Color(255, 255, 255, 100));
        addAllTabs(tabbedPane);
        Box totalBox = Box.createVerticalBox();
        totalBox.add(Box.createVerticalStrut(16));
        totalBox.add(new JButton(new AbstractAction("共享讯息") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MessageJp();
            }
        }));
        totalBox.add(tabbedPane);

        //添加容器的内容2：刷新按钮+切换账户按钮
        Box downBox = Box.createHorizontalBox();
        flushButton = new JButton(new AbstractAction("刷新") {
            @Override
            public void actionPerformed(ActionEvent e) {
                flush();
            }
        });
        //稳定按键颜色
        flushButton.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                flushButton.repaint();
                flushButton.setBackground(new Color(215, 175, 142, 200));
            }
        });
        changeButton = new JButton(new AbstractAction("切换账户") {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
                jfManager.setVisible(false);
                new LoginJf();
            }
        });
        //稳定按键颜色
        changeButton.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                changeButton.repaint();
                changeButton.setBackground(new Color(215, 175, 142, 200));
            }
        });
        flushButton.setBackground(new Color(215, 175, 142, 200));
        changeButton.setBackground(new Color(215, 175, 142, 200));
        //时间显示标签
        JLabel time = new JLabel();
        time.setFont(new Font("幼圆", Font.PLAIN, 18));
        time.setForeground(new Color(255, 255, 255, 250));
        setClock(time);
        //装填
        downBox.add(flushButton);
        downBox.add(Box.createHorizontalStrut(30));
        downBox.add(changeButton);
        downBox.add(Box.createHorizontalStrut(300));
        downBox.add(time);
        totalBox.add(downBox);

        jfManager.add(totalBox);
        jfManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfManager.pack();
        jfManager.setVisible(true);
    }

    //登出操作
    private void logout() {
        User user=LoginPerson.getInstance();
        UserDao userDao = new UserDao();
        user.setLoginState("0");
        userDao.updateUser(user);
    }

    //刷新数据
    public void flush() {
        int nowPageIndex = tabbedPane.getSelectedIndex();
        try {
            tabbedPane.removeAll();
            addAllTabs(tabbedPane);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        tabbedPane.setSelectedIndex(nowPageIndex);
    }

    //窗口的背景类
    static class CInstead extends JPanel {
        Image img;

        public CInstead() {
            img = new ImageIcon(LoginJf.class.getResource("/images/kiki.jpg")).getImage();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, null);
        }
    }

    //向操作页面填入所有主标签内容（每个标签由内容JPanel和标签名组成）
    public void addAllTabs(JTabbedPane tabbedPane) {
        //查询库存表
        JPanel storeJp = new StoreJp(this);
        tabbedPane.addTab("仓库库存", storeJp);

        //订单查询
        JPanel orderJp = new OrderJp(this);
        tabbedPane.addTab("订货查询", orderJp);

        //修改图书
        JPanel bookJp = new BookJp(this);
        tabbedPane.addTab("图书管理", bookJp);

        //销售查询
        JPanel statisticsJp = new StatisticsJp();
        tabbedPane.addTab("销售查询", statisticsJp);

        //出售书籍
        JPanel sellJp = new SellJp();
        tabbedPane.addTab("图书销售", sellJp);

        //会员管理
        JPanel userJp = new UserJp(this);
        tabbedPane.addTab("会员中心", userJp);

        //员工中心
        JPanel employeeJp = new EmployeeJp(this);
        tabbedPane.addTab("员工中心", employeeJp);

        //设置只有老板BIG·MOM才能管理员工
        if (!"0".equals(LoginPerson.getInstance().getUserId())) {
            tabbedPane.setEnabledAt(6, false);
        }
    }

    //设置时间显示
    private void setClock(JLabel time) {
        Timer timeAction = new Timer(100, e -> {
            long timeNum = System.currentTimeMillis();
            // 转换日期显示格式
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time.setText("当前时间：" + df.format(new Date(timeNum)));
        });
        timeAction.start();
    }
}
