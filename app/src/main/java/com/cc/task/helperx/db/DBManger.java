package com.cc.task.helperx.db;//package com.cc.helperqq.db;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.text.TextUtils;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * Created by fangying on 2017/10/28.
// */
//
//public class DBManger {
//
////    private AtomicInteger atomicInteger = new AtomicInteger();
////    private static DBManger dbManger;
////    private static DBHelper dbHelper;
////    private SQLiteDatabase database;
////
////    private DBManger(){
////
////    }
////
////    public static synchronized void initializeInstance(DBHelper dbHelper){
////        if (dbManger == null ) {
////            dbManger = new DBManger();
////        }
////    }
////
////    public static synchronized DBManger getInstance() {
////        if (dbManger ==null) {
////            throw new IllegalStateException(DBManger.class.getSimpleName()
////            + " is not initialized, call initializeInstance(..) method first.");
////        }
////        return dbManger;
////    }
////
////    /**
////     * 打开数据库对象
////     * @return
////     */
////    public boolean  openDatabase() {
////        boolean isTrue = false;
////        if (database == null){
////            database = SQLiteDatabase.openOrCreateDatabase("",null);
////        }
////
////        if (database != null){
////            isTrue = true;
////        }
////        return isTrue;
////    }
////    public void closeDatabase(){
////        if(database != null){
////            database.close();
////        }
////    }
////
////    public boolean openDefaultDatabase() {
////        boolean isTrue = false;
////        if (database == null) {
////            database = SQLiteDatabase.openOrCreateDatabase(dbPath,null);
////        }
////        if(database != null){
////            isTrue = true;
////        }
////        return isTrue;
////    }
////
////    public boolean openDatabase(String path){
////        if (TextUtils.isEmpty(path)) {
////            throw new RuntimeException("database path is null.");
////        }
////        boolean isTrue = false;
////        if (database == null) {
////            database = SQLiteDatabase.openOrCreateDatabase(path,null);
////        }
////        if(database != null){
////            isTrue = true;
////        }
////        return isTrue;
////    }
////
////    public <E> void createTable(Class<E> tableClass) throws Exception {
////        if (database == null) {
////            throw new RuntimeException("数据库打开或创建失败.");
////        }
////
////        if (tableClass == null) {
////            throw new RuntimeException("请传入实体类对象.");
////        }
////
////        String tbName = tableClass.getSimpleName();
////        Map<String, String> tbFields = DBUtils.getFieldNameAndTypeByClass(tableClass);
////
////        if (tbFields.isEmpty()) {
////            throw new RuntimeException("实体对象没有可创建的字段.");
////        }
////
////        if (tbFields.get(TB_ID_NAME) == null) {
////            throw new RuntimeException("实体必须创建字段名为" + TB_ID_NAME + "的属性，而且类型必须为Long.");
////        }
////
////        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
////        sb.append(" " + tbName + "(");
////        Set<String> fieldNames = tbFields.keySet();
////        for (String fieldName : fieldNames) {
////            String fieldType = tbFields.get(fieldName);
////            if (fieldName.equals(TB_ID_NAME)) {
////                sb.append(fieldName + " " + fieldType + " PRIMARY KEY autoincrement,");
////            } else {
////                sb.append(fieldName + " " + fieldType + ",");
////            }
////        }
////        String sql = sb.substring(0, sb.lastIndexOf(","));
////        sql += ")";
////        database.execSQL(sql);
////    }
////
////    public <E> E save(E entry) throws Exception{
////        if (database == null) {
////            throw new RuntimeException("数据库打开或创建失败.");
////        }
////
////        if (entry == null) {
////            throw new RuntimeException("保存的实体不能为空.");
////        }
////
////        long rowId = -1;
////        try{
////            Class tableClass = entry.getClass();
////            String tbName = tableClass.getSimpleName();
////            ContentValues values =  DBUtils.getFieldNameAndValueByClass(entry, tableClass);
////            values.remove(TB_ID_NAME);
////            database.beginTransaction();
////            rowId = database.insert(tbName, null, values);
////            database.setTransactionSuccessful();
////            if (rowId != -1) {
////                DBUtils.setFieldValueByFieldName(entry, tableClass, TB_ID_NAME, rowId);
////                return entry;
////            }
////        }catch (Exception e){
////            e.printStackTrace();
////        }finally {
////            database.endTransaction();
////        }
////        return null;
////    }
////
////    public <E> boolean update(E entry)throws Exception{
////        if (database == null) {
////            throw new RuntimeException("数据库打开或创建失败.");
////        }
////
////        if (entry == null) {
////            throw new RuntimeException("修改的实体不能为空.");
////        }
////
////        boolean isTrue = false;
////        try {
////            Class tableClass = entry.getClass();
////            String tbName = tableClass.getSimpleName();
////            ContentValues values =  DBUtils.getFieldNameAndValueByClass(entry, tableClass);
////            database.beginTransaction();
////            if (database.update(tbName, values, TB_ID_NAME + "= ?", new String[]{values.getAsLong(TB_ID_NAME) + ""}) > 0) {
////                isTrue = true;
////            }
////            database.setTransactionSuccessful();
////        }catch (Exception e){
////            e.printStackTrace();
////        }finally {
////            database.endTransaction();
////        }
////        return isTrue;
////    }
////
////    public <E> boolean delete(Class<E> entry,long id) throws Exception{
////        if (database == null) {
////            throw new RuntimeException("数据库打开或创建失败.");
////        }
////
////        if (entry == null) {
////            throw new RuntimeException("修改的实体类对象不能为空.");
////        }
////
////        boolean isTrue = false;
////        try{
////            String tbName = entry.getSimpleName();
////            database.beginTransaction();
////            if (database.delete(tbName, TB_ID_NAME + "= ?", new String[]{id + ""}) > 0) {
////                isTrue = true;
////            }
////            database.setTransactionSuccessful();
////        }catch (Exception e){
////            e.printStackTrace();
////        }finally {
////            database.endTransaction();
////        }
////        return isTrue;
////    }
////
////    public <E> boolean delete(Class<E> entry) throws Exception{
////        if (database == null) {
////            throw new RuntimeException("数据库打开或创建失败.");
////        }
////
////        if (entry == null) {
////            throw new RuntimeException("修改的实体类对象不能为空.");
////        }
////
////        boolean isTrue = false;
////        try{
////            String tbName = entry.getSimpleName();
////            database.beginTransaction();
////            if (database.delete(tbName, null, null) > 0) {
////                isTrue = true;
////            }
////            database.setTransactionSuccessful();
////        }catch (Exception e){
////            e.printStackTrace();
////        }finally {
////            database.endTransaction();
////        }
////        return isTrue;
////    }
////
////    public <E> boolean deleteByCondtion(Class<E> entry,Map<String,String> condtion) throws Exception{
////        if (database == null) {
////            throw new RuntimeException("数据库打开或创建失败.");
////        }
////
////        if (entry == null) {
////            throw new RuntimeException("修改的实体类对象不能为空.");
////        }
////
////        boolean isTrue = false;
////        try{
////            String tbName = entry.getSimpleName();
////            database.beginTransaction();
////            if (database.delete(tbName, parseDeleteParmas(condtion), setConditionValue(condtion)) > 0) {
////                isTrue = true;
////            }
////            database.setTransactionSuccessful();
////        }catch (Exception e){
////            e.printStackTrace();
////        }finally {
////            database.endTransaction();
////        }
////        return isTrue;
////    }
////
////    public <E> E findEntryById(Class<E> tableClass, long id)throws Exception {
////        if (database == null) {
////            throw new RuntimeException("数据库打开或创建失败.");
////        }
////
////        if (tableClass == null) {
////            throw new RuntimeException("查询的实体类对象不能为空.");
////        }
////
////        String tbName = tableClass.getSimpleName();
////        StringBuilder sb = new StringBuilder("SELECT * FROM " + tbName);
////        sb.append(" WHERE " + TB_ID_NAME + "=?");
////        Cursor cursor = database.rawQuery(sb.toString(), new String[]{id + ""});
////        List<E> entrys =  DBUtils.dbValueConverJavaValue(cursor, tableClass);
////        E result = null;
////        if (!entrys.isEmpty()) {
////            result = entrys.get(0);
////        }
////        return result;
////    }
////
////    public <E> List<E> findEntryList(Class<E> tableClass) throws Exception {
////        if (database == null) {
////            throw new RuntimeException("数据库打开或创建失败.");
////        }
////
////        if (tableClass == null) {
////            throw new RuntimeException("查询的实体类对象不能为空.");
////        }
////
////        String tbName = tableClass.getSimpleName();
////        StringBuilder sb = new StringBuilder("SELECT * FROM " + tbName);
////        Cursor cursor = database.rawQuery(sb.toString(), new String[]{});
////        return  DBUtils.dbValueConverJavaValue(cursor, tableClass);
////    }
////
////    public <E> List<E> findFirstEntryByCondtion(Class<E> tableClass,Map<String,String> params) {
////        if (database == null) {
////            throw new RuntimeException("数据库打开或创建失败.");
////        }
////        String tbName = tableClass.getSimpleName();
////        StringBuilder sb = new StringBuilder("SELECT * FROM " + tbName);
////        sb.append(" WHERE 1=1 ");
////        sb.append(parseParmas(params));
////        Cursor cursor = database.rawQuery(sb.toString(),setConditionValue(params));
////        List<E> entrys =  DBUtils.dbValueConverJavaValue(cursor,tableClass);
////        return entrys;
////    }
////
////    private String[] setConditionValue(Map<String,String> params){
////        if(params == null || params.size() == 0){
////            return null;
////        }
////
////        String[] result = new String[params.size()];
////        int index = 0;
////        for(String key : params.keySet()){
////            String value = params.get(key).toString();
////            result[index++] = value;
////        }
////        return result;
////    }
////
////    private String parseDeleteParmas(Map<String,String> params){
////        StringBuilder sb = new StringBuilder();
////        Set<String> keys = params.keySet();
////        int totalCount = keys.size();
////        int index = 0;
////        for(String key : keys){
////            index += 1;
////            if(index < totalCount){
////                sb.append(key +"=?"+" and ");
////            }else{
////                sb.append(key +"=?");
////            }
////        }
////        return sb.toString();
////    }
////
////    private String parseParmas(Map<String,String> params){
////        StringBuilder sb = new StringBuilder();
////        for(String key : params.keySet()){
////            sb.append( " and " +key +"=?");
////        }
////        return sb.toString();
////    }
//}
