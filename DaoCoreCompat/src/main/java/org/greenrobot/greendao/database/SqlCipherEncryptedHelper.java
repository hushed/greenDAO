package org.greenrobot.greendao.database;

import android.content.Context;

import net.zetetic.database.sqlcipher.SQLiteDatabase;
import net.zetetic.database.sqlcipher.SQLiteOpenHelper;

class SqlCipherEncryptedHelper extends SQLiteOpenHelper implements DatabaseOpenHelper.EncryptedHelper {

    private final DatabaseOpenHelper delegate;

    public SqlCipherEncryptedHelper(DatabaseOpenHelper delegate, Context context, String name, int version, String password) {
        super(context, name, password, null, version, 0, null, null, false);
        this.delegate = delegate;
        System.loadLibrary("sqlcipher");
    }

    private Database wrap(SQLiteDatabase sqLiteDatabase) {
        return new EncryptedDatabase(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        delegate.onCreate(wrap(db));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        delegate.onUpgrade(wrap(db), oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        delegate.onOpen(wrap(db));
    }

    @Override
    public Database getEncryptedReadableDb(String password) {
        return wrap(getReadableDatabase());
    }

    @Override
    public Database getEncryptedWritableDb(String password) {
        return wrap(getWritableDatabase());
    }
}
