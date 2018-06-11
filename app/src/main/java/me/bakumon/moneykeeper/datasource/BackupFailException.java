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

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;

/**
 * 备份失败异常
 *
 * @author Bakumon https://bakumon.me
 */
public class BackupFailException extends Exception {
    public BackupFailException() {
        super(App.getINSTANCE().getString(R.string.text_tip_backup_fail));
    }
}
