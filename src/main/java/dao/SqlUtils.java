package dao;

import lombok.NoArgsConstructor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.C3P0Util;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class SqlUtils {
    static QueryRunner queryRunner = new QueryRunner(C3P0Util.getDataSource());

    //查询多条记录:可变参数
    public static List<Map<String, Object>> checkTable(String sqlSentence,String ... arguments)  {
        try {
            return queryRunner.query(sqlSentence, new MapListHandler(),arguments);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "数据库操作异常,请检查数据", "消息对话框", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    //查询一个数据
    public static Object selectForOneResult(String sql,String ... arguments){
        try {
            return  queryRunner.query(sql, new ScalarHandler(),arguments);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "数据库操作异常,请检查数据", "消息对话框", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    //增加、修改、删除操作
    public static boolean update(String sql,String ... arguments){
        try {
            int resultNum = queryRunner.update(sql,arguments);
            if (resultNum!=0) {
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "数据库操作异常,请检查数据", "消息对话框", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

}