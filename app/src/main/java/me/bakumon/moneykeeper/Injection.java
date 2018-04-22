package me.bakumon.moneykeeper;

import android.content.Context;

import me.bakumon.moneykeeper.database.AppDatabase;
import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.datasource.LocalAppDataSource;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

/**
 * @author Bakumon https://bakumon.me
 */
public class Injection {
    public static AppDataSource provideUserDataSource(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return new LocalAppDataSource(database);
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        AppDataSource dataSource = provideUserDataSource(context);
        return new ViewModelFactory(dataSource);
    }
}
