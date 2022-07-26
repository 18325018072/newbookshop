package vo.statistics;

import utils.CommonUtils;

import javax.swing.*;

public class SalesRecordOption implements Option{
    @Override
    public void check(JTabbedPane sellTabbedPane) {
        JTable totalTable = CommonUtils.getJTable("select * from sells",
                new String[]{"sellId", "bookId", "sellNum", "sellDate", "realPrice", "userId", "employeeId"},
                new String[]{"记录号","书号","数量","日期","实际售价","用户号","员工号"});
        //装配表
        JScrollPane totalScrollPane = new JScrollPane(totalTable);
        sellTabbedPane.addTab("销售记录", totalScrollPane);
    }
}
