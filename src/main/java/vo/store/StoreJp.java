package vo.store;

import dao.SqlUtils;
import vo.EmployeeSoftware;
import utils.CommonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.*;

/**
 * @author 20349
 */
public class StoreJp extends JPanel implements Action{
    private String bookId;
    EmployeeSoftware employeeSoftware;
    JTable storeTable;

    public StoreJp( EmployeeSoftware employeeSoftware) {
        this.employeeSoftware = employeeSoftware;
        setLayout(new BorderLayout());

        //制作表格JTable
        storeTable = CommonUtils.getJTable("select * from store", new String[]{"BookId", "BookNum"}, new String[]{"书号", "库存"});
        JScrollPane jScrollPane = new JScrollPane(storeTable);

        //修改栏
        Box box = Box.createHorizontalBox();
        JTextField numText = new JTextField(10);
        JTextField orderPriceText = new JTextField(10);
        box.add(new JLabel("书号："));
        //下拉框
        JComboBox<String> bookIdBox = new JComboBox<>(checkAllBookId());
        bookIdBox.setSize(10, 10);
        bookIdBox.setSelectedItem(null);
        bookIdBox.addItemListener(e -> {
            bookId = (String) bookIdBox.getSelectedItem();
            numText.setText(String.valueOf(getStoreNum(bookId)));
        });
        box.add(bookIdBox);
        //文本框
        box.add(new JLabel("数量："));
        box.add(numText);
        box.add(new JLabel("进价："));
        box.add(orderPriceText);

        //按钮
        Action proxy = proxyAct(employeeSoftware,this);
        box.add(new JButton(new AbstractAction("进货") {
            @Override
            public void actionPerformed(ActionEvent e) {
                proxy.addBooks(orderPriceText.getText(), numText.getText());
            }
        }));
        box.add(new JButton(new AbstractAction("出库") {
            @Override
            public void actionPerformed(ActionEvent e) {
                proxy.deleteSomeBooks(numText.getText());
            }
        }));
        box.add(new JButton(new AbstractAction("删除") {
            @Override
            public void actionPerformed(ActionEvent e) {
                proxy.deleteAllBooks();
            }
        }));

        //选中监听器
        storeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = storeTable.getSelectedRow();
                Object a = storeTable.getValueAt(selectedRow, 0);
                Object b = storeTable.getValueAt(selectedRow, 1);
                bookIdBox.setSelectedItem(a.toString());
                numText.setText(b.toString());
                bookId = a.toString();
            }
        });
        add(jScrollPane, BorderLayout.CENTER);
        add(box, BorderLayout.SOUTH);
    }

    //使用了（动态）代理模式，让所有操作结束后，刷新表格
    private Action proxyAct(EmployeeSoftware employeeSoftware,StoreJp a) {
        Action proxy = (Action) Proxy.newProxyInstance(a.getClass().getClassLoader(),a.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                method.invoke(a, args);
                employeeSoftware.flush();
                return null;
            }  });
        return proxy;
    }

    //查询所有书号
    private Vector checkAllBookId() {
        Vector bookIds = new Vector();
        String sql = "select bookId from books order by bookId desc ";
        List<Map<String, Object>> mapList = SqlUtils.checkTable(sql);
        for (Map<String, Object> map : mapList) {
            bookIds.add(map.get("bookId"));
        }
        return bookIds;
    }

    //出库一些书
    @Override
    public void deleteSomeBooks(String num) {
        if (getStoreNum(bookId) < Integer.parseInt(num)) {
            JOptionPane.showMessageDialog(null, "库存不足，出库数量不能大于库存数量", "消息对话框", JOptionPane.WARNING_MESSAGE);
        } else {
            //数据库中删除书
            String sql = "UPDATE store SET bookNum = ? WHERE bookId = ?";
            boolean successDeleteSomeBooks = SqlUtils.update(sql, String.valueOf(getStoreNum(bookId) - Integer.parseInt(num)), bookId);
            //成功，则修改表
            if (successDeleteSomeBooks) {
                //employeeSoftware.flush();
                JOptionPane.showMessageDialog(null, "出库成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    //删除此仓位
    @Override
    public void deleteAllBooks() {
        //数据库中删除仓位
        String sql = "delete from store where bookId = ?";
        boolean successDeleteBookCell = SqlUtils.update(sql, bookId);
        //成功，则更新表格
        if (successDeleteBookCell) {
            //employeeSoftware.flush();
            JOptionPane.showMessageDialog(null, "删除成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**    进货-职责链设计模式
    新书入库前，判断是否满足前置条件：1、进价输入正确。2、图书管理认证存在此书。3、有本书的仓位
    这里采用了职责链设计模式，处理者有三：检查录入数据者、添加仓库者、添加库存记录者*/
    @Override
    public void addBooks(String orderPrice, String num) {
        //配置处理者
        Handler inspectorHandler =new InspectorHandler(orderPrice,bookId);
        Handler addStoreHandler =new AddStoreHandler(bookId,num);
        Handler addOrderHandler =new AddOrderHandler(bookId,num,orderPrice);
        inspectorHandler.setSuccessor(addStoreHandler);
        addStoreHandler.setSuccessor(addOrderHandler);
        //处理者开始执行工作
        inspectorHandler.handleRequest();
    }

    //查询书的库存
    private int getStoreNum(String bookId) {
        String sql = "SELECT bookNum FROM store WHERE bookId = ?";
        return (int) SqlUtils.selectForOneResult(sql,bookId);
    }

    public void  aa(){
        ArrayList<String> list = new ArrayList<>();// 被代理类
        list.add("a"); list.add("b"); list.add("a");

        List<String> proxy = (List<String>)Proxy.newProxyInstance(list.getClass().getClassLoader(),ArrayList.class.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object res = method.invoke(list, args);
                if (method.getName().equals("remove")){
                    Iterator<String> it = list.iterator();
                    while (it.hasNext()) {
                        String e = it.next();
                        if (e.equals(args[0])){// false
                            it.remove();
                        } } }
                return res;
            }  });
        boolean res = proxy.remove("a");
    }
}
