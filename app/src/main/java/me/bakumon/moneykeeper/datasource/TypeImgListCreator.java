/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.bakumon.moneykeeper.datasource;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.ui.addtype.TypeImgBean;


/**
 * 产生类型图片数据
 *
 * @author Bakumon https://bakumon.me
 */
public class TypeImgListCreator {

    public static List<TypeImgBean> createTypeImgBeanData(int type) {

        List<TypeImgBean> list = new ArrayList<>();
        TypeImgBean bean;

        if (type == RecordType.TYPE_OUTLAY) {
            bean = new TypeImgBean("type_eat");
            list.add(bean);

            bean = new TypeImgBean("type_calendar");
            list.add(bean);

            bean = new TypeImgBean("type_3c");
            list.add(bean);

            bean = new TypeImgBean("type_clothes");
            list.add(bean);

            bean = new TypeImgBean("type_candy");
            list.add(bean);

            bean = new TypeImgBean("type_cigarette");
            list.add(bean);

            bean = new TypeImgBean("type_humanity");
            list.add(bean);

            bean = new TypeImgBean("type_pill");
            list.add(bean);

            bean = new TypeImgBean("type_fitness");
            list.add(bean);

            bean = new TypeImgBean("type_sim");
            list.add(bean);

            bean = new TypeImgBean("type_study");
            list.add(bean);

            bean = new TypeImgBean("type_pet");
            list.add(bean);

            bean = new TypeImgBean("type_train");
            list.add(bean);
        } else {
            bean = new TypeImgBean("type_salary");
            list.add(bean);

            bean = new TypeImgBean("type_pluralism");
            list.add(bean);
        }
        return list;
    }

}
