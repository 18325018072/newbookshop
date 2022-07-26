package vo;

import dao.SqlUtils;
import utils.CommonUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class EmployeeJp extends JPanel {
    EmployeeSoftware employeeSoftware;
    JTable employeeTable;

    public EmployeeJp(EmployeeSoftware employeeSoftware) {
        this.employeeSoftware = employeeSoftware;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //制作表格JTable
        employeeTable = CommonUtils.getJTable("select * from employee",
                new String[]{"employeeId", "employee", "job"},
                new String[]{"员工号", "员工名", "岗位"});
        JScrollPane jScrollPane = new JScrollPane(employeeTable);

        //修改栏
        Box box = Box.createHorizontalBox();
        JTextField idText = new JTextField(10);
        JTextField nameText = new JTextField(10);
        JTextField jobText = new JTextField(10);
        box.add(new JLabel("员工号："));
        box.add(idText);
        box.add(new JLabel("员工名："));
        box.add(nameText);
        box.add(new JLabel("岗位："));
        box.add(jobText);
        //操作按钮
        box.add(new JButton(new AbstractAction("雇佣") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isExistInEmployee(idText.getText())) {
                    JOptionPane.showMessageDialog(null, "该id已存在", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String sql = "insert into employee values(?,?,?)";
                    boolean success = SqlUtils.update(sql,idText.getText(), nameText.getText(), jobText.getText());
                    if (success) {
                        employeeSoftware.flush();
                        JOptionPane.showMessageDialog(null, "添加成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }));
        box.add(new JButton(new AbstractAction("修改") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "UPDATE employee SET employee=?,job=? WHERE employeeId=?";
                boolean success = SqlUtils.update(sql,nameText.getText(), jobText.getText(), idText.getText());
                if (success) {
                    employeeSoftware.flush();
                    JOptionPane.showMessageDialog(null, "修改成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }));
        box.add(new JButton(new AbstractAction("解雇") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "delete from employee where employeeId=?";
                boolean success = SqlUtils.update(sql,idText.getText());
                if (success) {
                    employeeSoftware.flush();
                    JOptionPane.showMessageDialog(null, "删除成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }));

        //选中监听器
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = employeeTable.getSelectedRow();
                Object a = employeeTable.getValueAt(selectedRow, 0);
                Object b = employeeTable.getValueAt(selectedRow, 1);
                Object c = employeeTable.getValueAt(selectedRow, 2);
                idText.setText(a.toString());
                nameText.setText(b.toString());
                jobText.setText(c.toString());
            }
        });

        add(jScrollPane);
        add(box);
    }

    //判断是否存在此员工号
    private boolean isExistInEmployee(String employeeId) {
        String sql = "select job from employee where employeeId=?";
        String s = (String) SqlUtils.selectForOneResult(sql,employeeId);
        if (s==null) {
            return false;
        }else {
            return true;
        }
    }
}
