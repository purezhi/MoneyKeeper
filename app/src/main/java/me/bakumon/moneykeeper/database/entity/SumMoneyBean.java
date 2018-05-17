package me.bakumon.moneykeeper.database.entity;

/**
 * 支出或收入的总和
 *
 * @author Bakumon https://bakumon.me
 */
public class SumMoneyBean {
    /**
     * 类型
     * 0：支出
     * 1：收入
     *
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     */
    public int type;
    /**
     * 支出或收入的总和
     */
    public String sumMoney;
}
