package vo.statistics;

import javax.swing.*;
import java.awt.*;

public class StatisticsJp extends JPanel {
    //统计中的选项
    private String choice;
    //使用状态设计模式，将本面板和具体展示内容进行解耦，提高了扩展性
    private Option optionState;
    JTabbedPane sellTabbedPane;

    public StatisticsJp() {
        setLayout(new BorderLayout());
        sellTabbedPane = new JTabbedPane(SwingConstants.LEFT, JTabbedPane.WRAP_TAB_LAYOUT);

        //创建下拉框
        String[] option = {"2020", "2021", "销售记录", "销量排行"};
        JComboBox<String> optionBox = new JComboBox<>(option);
        optionBox.setSize(100, 40);
        optionBox.setSelectedItem(null);
        optionBox.addItemListener(e -> {
            //准备工作
            sellTabbedPane.removeAll();
            choice = (String) optionBox.getSelectedItem();
            //制作表格JTable
            if (choice == "销量排行") {
                setOption(new SalesRankingOption());
            } else if (choice == "销售记录") {
                setOption(new SalesRecordOption());
            } else {
                setOption(new YearSummaryOption(choice));
            }
        });

        Box select = Box.createHorizontalBox();
        select.add(new JLabel("要查询的内容："));
        select.add(optionBox);
        add(select, BorderLayout.NORTH);
        add(sellTabbedPane);
    }

    //修改选项
    private void setOption(Option option) {
        this.optionState = option;
        optionState.check(sellTabbedPane);
    }
}
