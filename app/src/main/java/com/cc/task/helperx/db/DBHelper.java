package com.cc.task.helperx.db;//package com.cc.helperqq.db;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import com.cc.helperqq.utils.LogUtils;
//
///**
// * Created by fangying on 2017/9/5.
// */
//
//public class DBHelper extends SQLiteOpenHelper {
//
//    private static DBHelper dbHelper;
//    private static String QQ_DATA = "qq_data.db";
//    public static final String CREATE_TB_CONTACT = DBConfig.CREATE_INFO
//            + DBConfig.TB_QQCONTACT + "("
//            + DBConfig.C_ID + " integer ,"
//            + DBConfig.C_QQCONTACT_WXSIGN + " txt, "
//            + DBConfig.C_QQCONTACT_ID + " varchar(50) primary key, "
//            + DBConfig.C_QQCONTACT_NAME + " text,"
//            + DBConfig.C_QQCONTACT_SEX + " varchar(10))";
//
//    public static final String CREATE_TB_GROUP = DBConfig.CREATE_INFO
//            + DBConfig.TB_QQGROUP + "("
//            + DBConfig.C_ID + " integer ,"
//            + DBConfig.C_QQGROUP_ID + "  VARCHAR (50) PRIMARY KEY " +
//            "REFERENCES tb_group_members (c_group_id) NOT NULL,"
//            + DBConfig.C_QQGROUP_NAME + " text NOT NULL ,"
//            + DBConfig.C_QQGROUP_TYPE + " var,"
//            + DBConfig.C_QQGROUP_WXSIGN + " txt,"
//            + DBConfig.C_QQGROUP_INDEX + " var)";
//
//    public static final String CREATE_TB_REPLYMSG = DBConfig.CREATE_INFO
//            + DBConfig.TB_REPLYMSG + "("
//            + DBConfig.C_ID + " integer,"
//            + DBConfig.C_REPLYMSG_NAME + " varchar(50) primary key,"
//            + DBConfig.C_REPLYMSG_ID + " var,"
//            + DBConfig.C_REPLYMSG_WXSIGN + " txt,"
//            + DBConfig.C_REPLYMSG_TIME + " var,"
//            + DBConfig.C_REPLYMSG_TYPE + " var)";
////            + "foreign key (" + DbConfig.C_REPLYMSG_NAME + ") references " + DbConfig.TB_QQGROUP + "(" + DbConfig.C_QQGROUP_NAME + ")" + ")";
//
//    public static final String CREATE_TB_MEMBERSGROUP = DBConfig.CREATE_INFO
//            + DBConfig.TB_GROUP_MEMBERS + "("
//            + DBConfig.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//            + DBConfig.C_MEMBERS_NAME + " CHAR  NOT NULL,"
//            + DBConfig.C_MEMBERS_ID + " CHAR  NOT NULL,"
//            + DBConfig.C_GROUP_ID + " CHAR  REFERENCES "
//            + DBConfig.TB_QQGROUP + " (" + DBConfig.C_QQGROUP_ID + "), "
//            + DBConfig.C_MEMBERS_SEX + " VAR, "
//            + DBConfig.C_MEMBERS_TROOPNICK + " VAR,"
//            + DBConfig.C_MEGROUP_NAME + " VAR,"
//            + DBConfig.C_MEGROUP_WXSIGN +" VAR,"
//            + DBConfig.C_MEMBERS_SENDTIME + " CHAR NOT NULL" + ")";
//
//
//    public DBHelper(Context context, int version) {
//        super(context, QQ_DATA, null, version);
//    }
//
////    public static synchronized DBHelper getDBInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
////        if (dbHelper == null) {
////            dbHelper = new DBHelper(context, name, factory, version);
////        }
////        return dbHelper;
////    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        LogUtils.logInfo(" 创建数据库    sqLiteDatabase  Version = " + sqLiteDatabase.getVersion());
//        sqLiteDatabase.getVersion();
//        LogUtils.logInfo("   " + CREATE_TB_CONTACT + "   " + CREATE_TB_GROUP + "    " + CREATE_TB_REPLYMSG + "    ");
//        sqLiteDatabase.execSQL(CREATE_TB_CONTACT);
//        sqLiteDatabase.execSQL(CREATE_TB_GROUP);
//        sqLiteDatabase.execSQL(CREATE_TB_REPLYMSG);
//        sqLiteDatabase.execSQL(CREATE_TB_MEMBERSGROUP);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        LogUtils.logInfo(" 更新数据库 newVersion = " + newVersion + "    Version=  " + sqLiteDatabase.getVersion() + "   oldVersion = " + oldVersion);
//        switch (oldVersion) {
//            case 1:
//                updataSqlite01(sqLiteDatabase);
//            case 2:
//                updataSqlite02(sqLiteDatabase);
//            case 3:
//            case 4:
//                updataSqlite03(sqLiteDatabase);
//            case 6:
//            case 7:
//            case 8:
//                updataSqlite05(sqLiteDatabase);
//            case 9:
//                updataSqlite06(sqLiteDatabase);
//            case 10:
//                updataSqlite07(sqLiteDatabase);
//            case 11:
//                updataSqlite08(sqLiteDatabase);
//            case 12:
//            case 13:
//                updataSqlite09(sqLiteDatabase);
//                break;
//        }
//    }
//
//    private void updataSqlite01(SQLiteDatabase db) {
//        LogUtils.logInfo("   更新数据库  1111   ");
//        String group_sql1 = "alter table " + DBConfig.TB_QQGROUP + " add column " + DBConfig.C_QQGROUP_TYPE + " var ";
//        String group_sq2 = "alter table " + DBConfig.TB_QQGROUP + " add column " + DBConfig.C_QQGROUP_INDEX + " var ";
//        db.execSQL(group_sq2);
//        db.execSQL(group_sql1);
//        db.execSQL(CREATE_TB_REPLYMSG);
//    }
//
//    private void updataSqlite02(SQLiteDatabase db) {
//        LogUtils.logInfo("   更新数据库  2222   ");
//        String group_sql = "alter table " + DBConfig.TB_REPLYMSG + " add column " + DBConfig.C_REPLYMSG_ID + " var ";
//        db.execSQL(group_sql);
//    }
//
//    private void updataSqlite03(SQLiteDatabase database) {
//        LogUtils.logInfo("   更新数据库  33333   ");
//
//        if (isFieldExist(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_ID + "var")) {
//            String str = "alter table " + DBConfig.TB_REPLYMSG + " rename to " + DBConfig.TB_REPLYMSG + "_reold";
//            String q = "create table " + DBConfig.TB_REPLYMSG + "("
////                    + DbConfig.C_ID + " integer,"
//                    + DBConfig.C_REPLYMSG_NAME + " varchar(50) primary key,"
//                    + DBConfig.C_REPLYMSG_ID + " var,"
//                    + DBConfig.C_REPLYMSG_TIME + " var,"
//                    + DBConfig.C_REPLYMSG_TYPE + " var)";
//
//            String sr = "insert into " + DBConfig.TB_REPLYMSG + "(" + DBConfig.C_REPLYMSG_ID
//                    + "," + DBConfig.C_REPLYMSG_NAME + "," + DBConfig.C_REPLYMSG_TIME + "," + DBConfig.C_REPLYMSG_TYPE + ")"
//                    + " select " + DBConfig.C_REPLYMSG_ID + "var" + "," + DBConfig.C_REPLYMSG_NAME + ","
//                    + DBConfig.C_REPLYMSG_TIME + "," + DBConfig.C_REPLYMSG_TYPE + " from " + DBConfig.TB_REPLYMSG + "_reold";
//
//            String redale = "drop table if exists " + DBConfig.TB_REPLYMSG + "_reold";
//            database.execSQL(str);
//            database.execSQL(q);
//            database.execSQL(sr);
//            database.execSQL(redale);
//        }
//    }
//
//    private void updataSqlite05(SQLiteDatabase database) {
//        LogUtils.logInfo("   更新数据库  5555   ");
//        String tale = DBConfig.TB_REPLYMSG + "_old";
//        if (tabbleIsExist(tale)) {
//            String dale = "drop table if exists " + DBConfig.TB_REPLYMSG + "_old";
//            database.execSQL(dale);
//        }
//        String str = "alter table " + DBConfig.TB_REPLYMSG + " rename to " + DBConfig.TB_REPLYMSG + "_reold";
//        String q = "create table " + DBConfig.TB_REPLYMSG + "("
////                + DbConfig.C_ID + " integer,"
//                + DBConfig.C_REPLYMSG_NAME + " varchar(50) primary key,"
//                + DBConfig.C_REPLYMSG_ID + " var,"
//                + DBConfig.C_REPLYMSG_TIME + " var,"
//                + DBConfig.C_REPLYMSG_TYPE + " var)";
//
//        String sr = "insert into " + DBConfig.TB_REPLYMSG + "(" + DBConfig.C_REPLYMSG_ID + ","
//                + DBConfig.C_REPLYMSG_NAME + "," + DBConfig.C_REPLYMSG_TIME + "," + DBConfig.C_REPLYMSG_TYPE + ")"
//                + " select " + DBConfig.C_REPLYMSG_ID + "," + DBConfig.C_REPLYMSG_NAME + ","
//                + DBConfig.C_REPLYMSG_TIME + "," + DBConfig.C_REPLYMSG_TYPE + " from " + DBConfig.TB_REPLYMSG + "_reold";
//
//
//        String groupstr = "alter table " + DBConfig.TB_QQGROUP + " rename to " + DBConfig.TB_QQGROUP + "_reold";
//        String groupq = "create table " + DBConfig.TB_QQGROUP + "("
////                + DbConfig.C_ID + " integer,"
//                + DBConfig.C_QQGROUP_ID + " varchar(50) primary key,"
//                + DBConfig.C_QQGROUP_NAME + " var,"
//                + DBConfig.C_QQGROUP_TYPE + " var,"
//                + DBConfig.C_QQGROUP_INDEX + " var)";
//
//        String groupsr = "insert into " + DBConfig.TB_QQGROUP + "(" + DBConfig.C_QQGROUP_ID + ","
//                + DBConfig.C_QQGROUP_NAME + "," + DBConfig.C_QQGROUP_INDEX + "," + DBConfig.C_QQGROUP_TYPE + ")"
//                + " select " + DBConfig.C_QQGROUP_ID + "," + DBConfig.C_QQGROUP_NAME + ","
//                + DBConfig.C_QQGROUP_INDEX + "," + DBConfig.C_QQGROUP_TYPE + " from " + DBConfig.TB_QQGROUP + "_reold";
//
//        String accountstr = "alter table " + DBConfig.TB_QQCONTACT + " rename to " + DBConfig.TB_QQCONTACT + "_reold";
//        String accountq = "create table " + DBConfig.TB_QQCONTACT + "("
////                + DbConfig.C_ID + " integer,"
//                + DBConfig.C_QQCONTACT_ID + " varchar(50) primary key,"
//                + DBConfig.C_QQCONTACT_NAME + " var,"
//                + DBConfig.C_QQCONTACT_SEX + " var)";
//
//        String accountsr = "insert into " + DBConfig.TB_QQCONTACT + "(" + DBConfig.C_QQCONTACT_ID + ","
//                + DBConfig.C_QQCONTACT_NAME + "," + DBConfig.C_QQCONTACT_SEX + ")"
//                + " select " + DBConfig.C_QQCONTACT_ID + "," + DBConfig.C_QQCONTACT_NAME + ","
//                + DBConfig.C_QQCONTACT_SEX + " from " + DBConfig.TB_QQCONTACT + "_reold";
//
//
//        database.execSQL(str);
//        database.execSQL(q);
//        database.execSQL(sr);
//
//        database.execSQL(accountstr);
//        database.execSQL(accountq);
//        database.execSQL(accountsr);
//
//        database.execSQL(groupstr);
//        database.execSQL(groupq);
//        database.execSQL(groupsr);
//
//
//        String redale = "drop table if exists " + DBConfig.TB_REPLYMSG + "_reold";
//        String acdale = "drop table if exists " + DBConfig.TB_QQCONTACT + "_reold";
//        String grdale = "drop table if exists " + DBConfig.TB_QQGROUP + "_reold";
//        database.execSQL(redale);
//        database.execSQL(acdale);
//        database.execSQL(grdale);
//    }
//
//    private void updataSqlite06(SQLiteDatabase database) {
//        String foreign = "PRAGMA foreign_keys = 0";
//        String sqli = "  CREATE TABLE sqlitestudio_temp_table AS SELECT * FROM tb_qqgroup";
//        String sqlu = " DROP TABLE tb_qqgroup";
//        String sql = " CREATE TABLE tb_qqgroup ( " +
//                "c_qqgroup_id    VARCHAR (50) PRIMARY KEY " +
//                "REFERENCES tb_group_members (c_group_id) NOT NULL," +
//                "c_qqgroup_name  text NOT NULL ," +
//                "c_qqgroup_type  VAR," +
//                "c_qqgroup_index VAR" +
//                ")";
//        String sqlo = "INSERT INTO tb_qqgroup("
//                + "c_qqgroup_id,"
//                + "c_qqgroup_name,"
//                + "c_qqgroup_type,"
//                + "c_qqgroup_index"
//                + ") "
//                + "SELECT c_qqgroup_id,"
//                + "c_qqgroup_name,"
//                + "c_qqgroup_type,"
//                + "c_qqgroup_index"
//                + " FROM sqlitestudio_temp_table";
//        String sqly = " DROP TABLE sqlitestudio_temp_table";
//        String sqlp = " PRAGMA foreign_keys = 1";
//        database.execSQL(foreign);
//        database.execSQL(sqli);
//        database.execSQL(sqlu);
//        database.execSQL(sql);
//        database.execSQL(sqlo);
//        database.execSQL(sqly);
//        database.execSQL(sqlp);
//        database.execSQL(CREATE_TB_MEMBERSGROUP);
//    }
//
//    private void updataSqlite07(SQLiteDatabase database) {
//        LogUtils.logInfo("   更新数据库  777777   ");
//        String retb = "alter table " + DBConfig.TB_GROUP_MEMBERS + " rename to " + DBConfig.TB_GROUP_MEMBERS + "_reold";
//        String create_tb = DBConfig.CREATE_INFO
//                + DBConfig.TB_GROUP_MEMBERS + "("
//                + DBConfig.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + DBConfig.C_MEMBERS_NAME + " CHAR  NOT NULL,"
//                + DBConfig.C_MEMBERS_ID + " CHAR  NOT NULL,"
//                + DBConfig.C_GROUP_ID + " CHAR  REFERENCES "
//                + DBConfig.TB_QQGROUP + " (" + DBConfig.C_QQGROUP_ID + "), "
//                + DBConfig.C_MEMBERS_SEX + " VAR, "
//                + DBConfig.C_MEMBERS_TROOPNICK + " VAR,"
//                + DBConfig.C_MEMBERS_SENDTIME + " CHAR NOT NULL" + ")";
//
//        String accountsr = "insert into " + DBConfig.TB_GROUP_MEMBERS + "(" + DBConfig.ID + ","
//                + DBConfig.C_MEMBERS_NAME + "," + DBConfig.C_MEMBERS_ID + "," + DBConfig.C_GROUP_ID
//                + "," + DBConfig.C_MEMBERS_SEX + "," + DBConfig.C_MEMBERS_TROOPNICK + "," + DBConfig.C_MEMBERS_SENDTIME + ")"
//                + " select " + DBConfig.ID + "," + DBConfig.C_MEMBERS_NAME + "," + DBConfig.C_MEMBERS_ID + "," + DBConfig.C_GROUP_ID
//                + "," + DBConfig.C_MEMBERS_SEX + "," + DBConfig.C_MEMBERS_TROOPNICK + "," + DBConfig.C_MEMBERS_SENDTIME + " from " + DBConfig.TB_GROUP_MEMBERS + "_reold";
//
//        String grdale = "drop table if exists " + DBConfig.TB_GROUP_MEMBERS + "_reold";
//        database.execSQL(retb);
//        database.execSQL(create_tb);
//        database.execSQL(accountsr);
//        database.execSQL(grdale);
//    }
//
//    private void updataSqlite08(SQLiteDatabase database) {
//        LogUtils.logInfo("   更新数据库  888888   ");
//        String retb = "alter table " + DBConfig.TB_GROUP_MEMBERS + " rename to " + DBConfig.TB_GROUP_MEMBERS + "_reold";
//        String create_tb = DBConfig.CREATE_INFO
//                + DBConfig.TB_GROUP_MEMBERS + "("
//                + DBConfig.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + DBConfig.C_MEMBERS_NAME + " CHAR  NOT NULL,"
//                + DBConfig.C_MEMBERS_ID + " CHAR  NOT NULL,"
//                + DBConfig.C_GROUP_ID + " CHAR  REFERENCES "
//                + DBConfig.TB_QQGROUP + " (" + DBConfig.C_QQGROUP_ID + "), "
//                + DBConfig.C_MEMBERS_SEX + " VAR, "
//                + DBConfig.C_MEGROUP_NAME + " VAR,"
//                + DBConfig.C_MEMBERS_TROOPNICK + " VAR,"
//                + DBConfig.C_MEMBERS_SENDTIME + " CHAR NOT NULL" + ")";
//
//        String accountsr = "insert into " + DBConfig.TB_GROUP_MEMBERS + "(" + DBConfig.ID + ","
//                + DBConfig.C_MEMBERS_NAME + "," + DBConfig.C_MEMBERS_ID + "," + DBConfig.C_GROUP_ID
//                + "," + DBConfig.C_MEMBERS_SEX + "," + DBConfig.C_MEMBERS_TROOPNICK + "," + DBConfig.C_MEMBERS_SENDTIME + ")"
//                + " select " + DBConfig.ID + "," + DBConfig.C_MEMBERS_NAME + "," + DBConfig.C_MEMBERS_ID + "," + DBConfig.C_GROUP_ID
//                + "," + DBConfig.C_MEMBERS_SEX + "," + DBConfig.C_MEMBERS_TROOPNICK + "," + DBConfig.C_MEMBERS_SENDTIME + " from " + DBConfig.TB_GROUP_MEMBERS + "_reold";
//
//        String grdale = "drop table if exists " + DBConfig.TB_GROUP_MEMBERS + "_reold";
//        database.execSQL(retb);
//        database.execSQL(create_tb);
//        database.execSQL(accountsr);
//        database.execSQL(grdale);
//    }
//
//    private void updataSqlite09(SQLiteDatabase database) {
//        LogUtils.logInfo("   更新数据库  99999999   ");
//        String str = "alter table " + DBConfig.TB_REPLYMSG + " rename to " + DBConfig.TB_REPLYMSG + "_reold";
//        String q = "create table " + DBConfig.TB_REPLYMSG + "("
//                + DBConfig.C_ID + " integer,"
//                + DBConfig.C_REPLYMSG_NAME + " varchar(50) primary key,"
//                + DBConfig.C_REPLYMSG_ID + " var,"
//                + DBConfig.C_REPLYMSG_TIME + " var,"
//                + DBConfig.C_REPLYMSG_TYPE + " var)";
//
//        String sr = "insert into " + DBConfig.TB_REPLYMSG + "(" + DBConfig.C_REPLYMSG_ID + ","
//                + DBConfig.C_REPLYMSG_NAME + "," + DBConfig.C_REPLYMSG_TIME + "," + DBConfig.C_REPLYMSG_TYPE + ")"
//                + " select " + DBConfig.C_REPLYMSG_ID + "," + DBConfig.C_REPLYMSG_NAME + ","
//                + DBConfig.C_REPLYMSG_TIME + "," + DBConfig.C_REPLYMSG_TYPE + " from " + DBConfig.TB_REPLYMSG + "_reold";
//
//
//        String groupstr = "alter table " + DBConfig.TB_QQGROUP + " rename to " + DBConfig.TB_QQGROUP + "_reold";
//        String groupq = "create table " + DBConfig.TB_QQGROUP + "("
//                + DBConfig.C_ID + " integer,"
//                + DBConfig.C_QQGROUP_ID + " varchar(50) primary key,"
//                + DBConfig.C_QQGROUP_NAME + " var,"
//                + DBConfig.C_QQGROUP_TYPE + " var,"
//                + DBConfig.C_QQGROUP_INDEX + " var)";
//
//        String groupsr = "insert into " + DBConfig.TB_QQGROUP + "(" + DBConfig.C_QQGROUP_ID + ","
//                + DBConfig.C_QQGROUP_NAME + "," + DBConfig.C_QQGROUP_INDEX + "," + DBConfig.C_QQGROUP_TYPE + ")"
//                + " select " + DBConfig.C_QQGROUP_ID + "," + DBConfig.C_QQGROUP_NAME + ","
//                + DBConfig.C_QQGROUP_INDEX + "," + DBConfig.C_QQGROUP_TYPE + " from " + DBConfig.TB_QQGROUP + "_reold";
//
//        String accountstr = "alter table " + DBConfig.TB_QQCONTACT + " rename to " + DBConfig.TB_QQCONTACT + "_reold";
//        String accountq = "create table " + DBConfig.TB_QQCONTACT + "("
//                + DBConfig.C_ID + " integer,"
//                + DBConfig.C_QQCONTACT_ID + " varchar(50) primary key,"
//                + DBConfig.C_QQCONTACT_NAME + " var,"
//                + DBConfig.C_QQCONTACT_SEX + " var)";
//
//        String accountsr = "insert into " + DBConfig.TB_QQCONTACT + "(" + DBConfig.C_QQCONTACT_ID + ","
//                + DBConfig.C_QQCONTACT_NAME + "," + DBConfig.C_QQCONTACT_SEX + ")"
//                + " select " + DBConfig.C_QQCONTACT_ID + "," + DBConfig.C_QQCONTACT_NAME + ","
//                + DBConfig.C_QQCONTACT_SEX + " from " + DBConfig.TB_QQCONTACT + "_reold";
//
//
//        database.execSQL(str);
//        database.execSQL(q);
//        database.execSQL(sr);
//
//        database.execSQL(accountstr);
//        database.execSQL(accountq);
//        database.execSQL(accountsr);
//
//        database.execSQL(groupstr);
//        database.execSQL(groupq);
//        database.execSQL(groupsr);
//
//
//        String redale = "drop table if exists " + DBConfig.TB_REPLYMSG + "_reold";
//        String acdale = "drop table if exists " + DBConfig.TB_QQCONTACT + "_reold";
//        String grdale = "drop table if exists " + DBConfig.TB_QQGROUP + "_reold";
//        database.execSQL(redale);
//        database.execSQL(acdale);
//        database.execSQL(grdale);
//    }
//
//    /**
//     * 判断某表里某字段是否存在
//     *
//     * @param db
//     * @param tableName
//     * @param fieldName
//     * @return
//     */
//    private boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName) {
//        String queryStr = "select sql from sqlite_master where type = 'table' and name = '%s'";
//        queryStr = String.format(queryStr, tableName);
//        Cursor c = db.rawQuery(queryStr, null);
//        String tableCreateSql = null;
//        try {
//            if (c != null && c.moveToFirst()) {
//                tableCreateSql = c.getString(c.getColumnIndex("sql"));
//            }
//        } finally {
//            if (c != null)
//                c.close();
//        }
//        if (tableCreateSql != null && tableCreateSql.contains(fieldName))
//            return true;
//        return false;
//    }
//
//    /**
//     * 判断某张表是否存在
//     *
//     * @param tableName 表名
//     * @return
//     */
//    public boolean tabbleIsExist(String tableName) {
//        boolean result = false;
//        if (tableName == null) {
//            return false;
//        }
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        try {
//            db = this.getReadableDatabase();
//            String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim() + "' ";
//            cursor = db.rawQuery(sql, null);
//            if (cursor.moveToNext()) {
//                int count = cursor.getInt(0);
//                if (count > 0) {
//                    result = true;
//                }
//            }
//
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//        return result;
//    }
//
//}
