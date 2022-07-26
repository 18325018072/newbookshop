package utils;

import dao.SqlUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class CommonUtils {
    //判断字符串是否是数字
    public static boolean isNumeric(String str){
        return str.matches("[0-9][0-9\\.]*");
    }

    //通过传入的sql语句、数据库中表格标题（用于从数据库返回的Map对象中取出数据）和本表格标题，获取相应表格对象
    public static JTable getJTable(String sqlSentence, String[] title, String[] tableTitle)  {
        //查询sql的表格数据
        List<Map<String, Object>> mapList;
        mapList = SqlUtils.checkTable(sqlSentence);

        //创建数据的二维数组
        int rowNum = mapList.size();
        int columnNum = title.length;
        Object[][] tableData = new Object[rowNum][columnNum];
        int editRow = 0;
        for (Map<String, Object> map : mapList) {
            for (int j = 0; j < columnNum; j++) {
                tableData[editRow][j] = map.get(title[j]);
            }
            editRow++;
        }

        //创建JTable对象
        DefaultTableModel tableModel = new DefaultTableModel(tableData, tableTitle);
        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//表格不允许被编辑
            }
        };
        table.setRowSorter(new TableRowSorter<>(tableModel));
        //只能选择单行
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        // 创建表格标题对象，并设置表头的字体
        JTableHeader head = table.getTableHeader();
        head.setFont(new Font("黑体", Font.PLAIN, 16));
        return table;
    }

}
