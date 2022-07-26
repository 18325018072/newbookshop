package vo;

import dao.SqlUtils;
import utils.CommonUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BookJp extends JPanel {
    JTable booksTable;
    EmployeeSoftware employeeSoftware;

    public BookJp(EmployeeSoftware employeeSoftware) {
        this.employeeSoftware = employeeSoftware;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //制作表格JTable
        booksTable = CommonUtils.getJTable("select * from books",
                new String[]{"BookId", "BookName", "Publisher", "BookType", "author", "NowPrice", "Note"},
                new String[]{"书号", "书名", "出版社", "类型", "作者", "当前售价", "备注"});
        JScrollPane jScrollPane = new JScrollPane(booksTable);

        //修改栏
        Box box1 = Box.createHorizontalBox();
        Box box2 = Box.createHorizontalBox();
        Box box3 = Box.createHorizontalBox();
        Box box = Box.createVerticalBox();
        JTextField idText = new JTextField(10);
        JTextField nameText = new JTextField(10);
        JTextField publisherText = new JTextField(10);
        JTextField typeText = new JTextField(10);
        JTextField authorText = new JTextField(10);
        JTextField nowPriceText = new JTextField(10);
        JTextField noteText = new JTextField(10);
        box1.add(new JLabel(" 书号：   "));
        box1.add(idText);
        box1.add(new JLabel(" 书名： "));
        box1.add(nameText);
        box1.add(new JLabel(" 出版社："));
        box1.add(publisherText);
        box2.add(new JLabel(" 类型:"));
        box2.add(typeText);
        box2.add(new JLabel(" 作者： "));
        box2.add(authorText);
        box2.add(new JLabel(" 当前售价："));
        box2.add(nowPriceText);
        box3.add(new JLabel(" 备注："));
        box3.add(noteText);
        box.add(box1);
        box.add(box2);
        box3.add(new JButton(new AbstractAction("添加") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isExistInBook(idText.getText())) {
                    JOptionPane.showMessageDialog(null, "该书号已存在", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String sql = "insert into books values(?,?,?,?,?,?,?)";
                    boolean success = SqlUtils.update(sql,idText.getText(), nameText.getText(), publisherText.getText(), typeText.getText(), authorText.getText(), nowPriceText.getText(), noteText.getText());
                    if (success) {
                        employeeSoftware.flush();
                        JOptionPane.showMessageDialog(null, "添加成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }));
        box3.add(new JButton(new AbstractAction("修改") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "UPDATE books SET BookName=?, Publisher=?, BookType=?, author=?, NowPrice=?, Note=? WHERE BookId=?";
                boolean success = SqlUtils.update(sql,nameText.getText(), publisherText.getText(), typeText.getText(), authorText.getText(), nowPriceText.getText(), noteText.getText(),idText.getText());
                if (success) {
                    employeeSoftware.flush();
                    JOptionPane.showMessageDialog(null, "修改成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }));
        box3.add(new JButton(new AbstractAction("删除") {
            @Override
            public void actionPerformed(ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(null, "删除图书将同时删除其仓库库存，确认操作吗？", "删除图书", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        String sql1 = "delete from store where BookId = ?";
                        String sql2 = "delete from books where BookId = ?";
                        boolean success1 = SqlUtils.update(sql1,idText.getText());
                        boolean success2 = SqlUtils.update(sql2,idText.getText());
                        if (success1||success2) {
                            employeeSoftware.flush();
                            JOptionPane.showMessageDialog(null, "删除成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
            }
        }));
        box.add(box3);

        //选中监听器
        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = booksTable.getSelectedRow();
                Object a = booksTable.getValueAt(selectedRow, 0);
                Object b = booksTable.getValueAt(selectedRow, 1);
                Object c = booksTable.getValueAt(selectedRow, 2);
                Object d = booksTable.getValueAt(selectedRow, 3);
                Object ee = booksTable.getValueAt(selectedRow, 4);
                Object f = booksTable.getValueAt(selectedRow, 5);
                Object g = booksTable.getValueAt(selectedRow, 6);
                idText.setText(a.toString());
                nameText.setText(b.toString());
                publisherText.setText(c.toString());
                typeText.setText(d.toString());
                authorText.setText(ee.toString());
                nowPriceText.setText(f.toString());
                if (g != null) {
                    noteText.setText(g.toString());
                } else {
                    noteText.setText(null);
                }
            }
        });
        add(jScrollPane);
        add(box);
    }

    //判断某书是否已写入档案
    private boolean isExistInBook(String bookId) {
        String sql = "select publisher from books where bookId=?";
        String s = (String) SqlUtils.selectForOneResult(sql,bookId);
        if (s==null) {
            return false;
        }else {
            return true;
        }
    }
}
