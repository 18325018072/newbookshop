package vo.statistics;

import utils.CommonUtils;

import javax.swing.*;

public class SalesRankingOption implements Option {
    @Override
    public void check(JTabbedPane sellTabbedPane) {
        JTable totalTable = CommonUtils.getJTable("SELECT 书名,销量,总收益,剩余库存 FROM view_Champion",
                new String[]{"书名", "销量", "总收益", "剩余库存"},
                new String[]{"书名", "销量", "总收益", "剩余库存"});
        //装配表
        JScrollPane totalScrollPane = new JScrollPane(totalTable);
        sellTabbedPane.addTab("销量排行", totalScrollPane);
    }
}
