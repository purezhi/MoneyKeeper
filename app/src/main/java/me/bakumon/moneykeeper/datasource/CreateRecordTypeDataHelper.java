package me.bakumon.moneykeeper.datasource;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.database.entity.RecordType;


/**
 * 产生初始化的记录类型数据
 *
 * @author Bakumon https://bakumon.me
 */
public class CreateRecordTypeDataHelper {

    public static RecordType[] createRecordTypeData() {

        List<RecordType> list = new ArrayList<>();

        Resources res = App.getINSTANCE().getResources();

        RecordType type;

        // 支出
        type = new RecordType(res.getString(R.string.text_type_eat), R.mipmap.type_eat, 0, 1);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_calendar), R.mipmap.type_calendar, 0, 2);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_3c), R.mipmap.type_3c, 0, 3);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_clothes), R.mipmap.type_clothes, 0, 4);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_candy), R.mipmap.type_candy, 0, 5);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_cigarette), R.mipmap.type_cigarette, 0, 6);
        list.add(type);


        type = new RecordType(res.getString(R.string.text_type_humanity), R.mipmap.type_humanity, 0, 7);
        list.add(type);


        type = new RecordType(res.getString(R.string.text_type_pill), R.mipmap.type_pill, 0, 8);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_fitness), R.mipmap.type_fitness, 0, 9);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_sim), R.mipmap.type_sim, 0, 10);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_study), R.mipmap.type_study, 0, 11);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_pet), R.mipmap.type_pet, 0, 12);
        list.add(type);


        // 收入
        type = new RecordType(res.getString(R.string.text_type_salary), R.mipmap.type_salary, 1, 1);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_pluralism), R.mipmap.type_pluralism, 1, 2);
        list.add(type);

        return list.toArray(new RecordType[list.size()]);
    }

}
