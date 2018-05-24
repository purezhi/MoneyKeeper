package me.bakumon.moneykeeper.database.entity;

import java.util.Date;

/**
 * 某天的支出或收入总和
 *
 * @author Bakumon https://bakumon.me
 */
public class DaySumMoneyBean {
    /**
     * 类型
     * 0：支出
     * 1：收入
     *
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     */
    public int type;

    public Date time;
    /**
     * 支出或收入的总和
     */
    public String daySumMoney;
}
