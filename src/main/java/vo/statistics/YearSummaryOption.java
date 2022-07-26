package vo.statistics;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import utils.CommonUtils;

import javax.swing.*;

@NoArgsConstructor
@AllArgsConstructor
public class YearSummaryOption implements Option{
    private String choice;

    @Override
    public void check(JTabbedPane sellTabbedPane) {
        //按日统计的销售记录
        JTable dayTable = CommonUtils.getJTable("SELECT 日期,新闻销量,小说销量,教材销量,SUM(sells.sellNum*realPrice) 总销售额,SUM(sells.sellNum) 总销售量\n" +
                        "FROM ((( (select DISTINCT sellDate 日期 from sells) myday left outer join \n" +
                        "\t(SELECT sellDate bDAY,SUM(sells.sellNum) 新闻销量\n" +
                        "\tFROM sells,books\n" +
                        "\tWHERE sells.bookId=books.bookId \n" +
                        "\tAND YEAR(sellDate)=" + choice + "\n" +
                        "\t\tAND bookType='新闻'\n" +
                        "\tGROUP BY DAY(sellDate)) AS new on 日期=bDAY)\n" +
                        "\t\tleft outer join \n" +
                        "\t\t(SELECT sellDate aDAY,SUM(sells.sellNum) 小说销量\n" +
                        "\t\tFROM sells,books\n" +
                        "\t\tWHERE sells.bookId=books.bookId \n" +
                        "\t\tAND YEAR(sellDate)=" + choice + "\n" +
                        "\t\t\tAND bookType='小说'\n" +
                        "\t\tGROUP BY DAY(sellDate)) AS novel on 日期=aDAY)\n" +
                        "\t\t\t\tleft outer join\n" +
                        "\t\t\t\t\t(SELECT sellDate cDAY,SUM(sells.SellNum) 教材销量\n" +
                        "\t\t\t\t\tFROM sells,books\n" +
                        "\t\t\t\t\tWHERE sells.bookId=books.BookId \n" +
                        "\t\t\t\t\tAND YEAR(sellDate)=" + choice + "\n" +
                        "\t\t\t\t\t\tAND bookType='教育'\n" +
                        "\t\t\t\t\tGROUP BY sellDate) AS learn on 日期=cDAY)\n" +
                        "\t\t\t\t\t\tleft outer join sells ON 日期=sellDate\n" +
                        "WHERE YEAR(sellDate)=" + choice + "\n" +
                        "GROUP BY 日期\n" +
                        "ORDER BY 日期 DESC",
                new String[]{"日期", "新闻销量", "小说销量", "教材销量", "总销售额", "总销售量"},
                new String[]{"日期", "新闻销量", "小说销量", "教材销量", "总销售额", "总销售量"});

        //装配表
        JScrollPane dayScrollPane = new JScrollPane(dayTable);
        sellTabbedPane.addTab("按日统计销量", dayScrollPane);

        //按月统计的销售记录
        JTable monthTable = CommonUtils.getJTable("SELECT 月份,新闻销量,小说销量,教材销量,SUM(sellNum*realPrice) 总销售额,SUM(sellNum) 总销售量\n" +
                        "FROM ((( (select DISTINCT MONTH(sellDate) 月份 from sells) mymonth left outer join \n" +
                        "\t(SELECT MONTH(SellDate) bMONTH,SUM(sells.sellNum) 新闻销量\n" +
                        "\tFROM sells,books\n" +
                        "\tWHERE sells.BookId=books.BookId \n" +
                        "\tAND YEAR(sellDate)=" + choice + "\n" +
                        "\t\tAND bookType='新闻'\n" +
                        "\tGROUP BY MONTH(sellDate)) AS new on 月份=bMONTH)\n" +
                        "\t\tleft outer join \n" +
                        "\t\t(SELECT MONTH(sellDate) aMONTH,SUM(sells.sellNum) 小说销量\n" +
                        "\t\tFROM sells,books\n" +
                        "\t\tWHERE sells.bookId=books.bookId\n" +
                        "\t\tAND YEAR(sellDate)=" + choice + "\n" +
                        "\t\t\tAND bookType='小说'\n" +
                        "\t\tGROUP BY MONTH(sellDate)) AS novel on 月份=aMONTH)\n" +
                        "\t\t\t\tleft outer join\n" +
                        "\t\t\t\t\t(SELECT MONTH(sellDate) cMONTH,SUM(sells.SellNum) 教材销量\n" +
                        "\t\t\t\t\tFROM sells,books\n" +
                        "\t\t\t\t\tWHERE sells.bookId=books.bookId\n" +
                        "\t\t\t\t\tAND YEAR(sellDate)=" + choice + "\n" +
                        "\t\t\t\t\t\tAND bookType='教育'\n" +
                        "\t\t\t\t\tGROUP BY MONTH(sellDate)) AS learn on 月份=cMONTH)\n" +
                        "\t\t\t\t\t\tleft outer join sells ON 月份=MONTH(sellDate)\n" +
                        "WHERE YEAR(sellDate)=" + choice + "\n" +
                        "GROUP BY 月份\n" +
                        "ORDER BY 月份 DESC",
                new String[]{"月份", "新闻销量", "小说销量", "教材销量", "总销售额", "总销售量"},
                new String[]{"月份", "新闻销量", "小说销量", "教材销量", "总销售额", "总销售量"});
        JScrollPane monthScrollPane = new JScrollPane(monthTable);
        sellTabbedPane.addTab("按月统计销量", monthScrollPane);

        //按季统计的销售记录
        JTable qTable = CommonUtils.getJTable("SELECT 季,新闻销量,小说销量,教材销量,SUM(sellNum*realPrice) 总销售额,SUM(sellNum) 总销售量\n" +
                        "FROM ((( (select DISTINCT QUARTER(sellDate) 季 from sells) Q left outer join \n" +
                        "\t(SELECT QUARTER(sellDate) bMONTH,SUM(sells.sellNum) 新闻销量\n" +
                        "\tFROM sells,books\n" +
                        "\tWHERE sells.BookId=books.BookId\n" +
                        "\t\tAND YEAR(sellDate)=" + choice + "\n" +
                        "\t\tAND bookType='新闻'\n" +
                        "\tGROUP BY QUARTER(sellDate)) AS new on 季=bMONTH)\n" +
                        "\t\tleft outer join \n" +
                        "\t\t(SELECT QUARTER(sellDate) aMONTH,SUM(sells.sellNum) 小说销量\n" +
                        "\t\tFROM sells,books\n" +
                        "\t\tWHERE sells.bookId=books.bookId\n" +
                        "\t\t\tAND YEAR(sellDate)=" + choice + "\n" +
                        "\t\t\tAND bookType='小说'\n" +
                        "\t\tGROUP BY QUARTER(sellDate)) AS novel on 季=aMONTH)\n" +
                        "\t\t\t\tleft outer join\n" +
                        "\t\t\t\t\t(SELECT QUARTER(sellDate) cMONTH,SUM(sells.sellNum) 教材销量\n" +
                        "\t\t\t\t\tFROM sells,books\n" +
                        "\t\t\t\t\tWHERE sells.bookId=books.bookId\n" +
                        "\t\t\t\t\t\tAND YEAR(sellDate)=" + choice + "\n" +
                        "\t\t\t\t\t\tAND bookType='教育'\n" +
                        "\t\t\t\t\tGROUP BY QUARTER(sellDate)) AS learn on 季=cMONTH)\n" +
                        "\t\t\t\t\t\tleft outer join sells ON 季=QUARTER(sellDate)\n" +
                        "WHERE YEAR(sellDate)=" + choice + "\n" +
                        "GROUP BY 季\n" +
                        "ORDER BY 季 DESC",
                new String[]{"季", "新闻销量", "小说销量", "教材销量", "总销售额", "总销售量"},
                new String[]{"季", "新闻销量", "小说销量", "教材销量", "总销售额", "总销售量"});

        JScrollPane qScrollPane = new JScrollPane(qTable);
        sellTabbedPane.addTab("按季统计销量", qScrollPane);
    }
}
