package com.ulicae.cinelog.data;

import android.content.Context;

import com.ulicae.cinelog.data.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;

public class ProdOpenHelper extends DaoMaster.OpenHelper {

    public ProdOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onOpen(Database db) {
        DaoMaster.createAllTables(db, true);
    }
}
