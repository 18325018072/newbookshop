package vo;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MessageJp  {
    public MessageJp() {
        JFrame messageJf = new JFrame("共享讯息");
        JTextArea note = new JTextArea(10,60);
        note.setFont(new Font("宋体", Font.PLAIN, 16));
        JScrollPane jScrollPane = new JScrollPane(note);
        //窗口在屏幕中间显示
        messageJf.setLocationRelativeTo(null);

        //设置窗口图标
        URL imgURL = MessageJp.class.getResource("/images/dog.jpg");
        ImageIcon imgIcon = new ImageIcon(imgURL);
        Image img = imgIcon.getImage();
        messageJf.setIconImage(img);

        //开启时读取备忘录文件，关闭时保存
        messageJf.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                //初始文本框内容，从文件中读取
                try {
                    String oldNote = FileUtils.readFileToString(new File("BookShopSystem/Resources/note/note.txt"), "utf-8");
                    note.setText(oldNote);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void windowClosing(WindowEvent e) {
                //读取文本框内容，保存到文件中
                try {
                    FileUtils.writeStringToFile(new File("BookShopSystem/Resources/note/note.txt"),note.getText(),"utf-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        messageJf.add(jScrollPane);
        messageJf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messageJf.setVisible(true);
        messageJf.pack();
    }
}
