package me.bakumon.moneykeeper;

import me.bakumon.moneykeeper.database.AppDatabase;
import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.datasource.LocalAppDataSource;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

/**
 * @author Bakumon https://bakumon.me
 */
public class Injection {
    public static AppDataSource provideUserDataSource() {
        AppDatabase database = AppDatabase.getInstance();
        return new LocalAppDataSource(database);
    }

    public static ViewModelFactory provideViewModelFactory() {
        AppDataSource dataSource = provideUserDataSource();
        return new ViewModelFactory(dataSource);
    }
}
