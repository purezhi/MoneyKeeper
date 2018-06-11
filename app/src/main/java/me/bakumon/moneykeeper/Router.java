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

package me.bakumon.moneykeeper;

/**
 * 路由 Url
 *
 * @author Bakumon https://bakumon.me
 */
public class Router {
    public static final String SCHEME = "mk";

    /**
     * floo 的页面映射 mappings 的 key
     */
    public static class Url {
        public static final String URL_HOME = "home";
        public static final String URL_ADD_RECORD = "add_record";
        public static final String URL_TYPE_MANAGE = "type_Manage";
        public static final String URL_TYPE_SORT = "type_sort";
        public static final String URL_ADD_TYPE = "add_type";
        public static final String URL_STATISTICS = "statistics";
        public static final String URL_TYPE_RECORDS = "type_records";
        public static final String URL_SETTING = "setting";
        public static final String URL_OPEN_SOURCE = "open_source";
        public static final String URL_ABOUT = "about";
    }

    /**
     * floo stack 使用的key，用于标示已经打开的 activity
     */
    public static class IndexKey {
        public static final String INDEX_KEY_HOME = "index_key_home";
    }

    /**
     * floo 传递数据使用的 key
     */
    public static class ExtraKey {
        public static final String KEY_RECORD_BEAN = "key_record_bean";
        public static final String KEY_TYPE = "key_type";
        public static final String KEY_TYPE_BEAN = "key_type_bean";
        public static final String KEY_TYPE_NAME = "key_type_name";
        public static final String KEY_RECORD_TYPE = "key_record_type";
        public static final String KEY_RECORD_TYPE_ID = "key_record_type_id";
        public static final String KEY_YEAR = "key_year";
        public static final String KEY_MONTH = "key_month";
        public static final String KEY_SORT_TYPE = "key_sort_type";
    }
}
