package com.cc.task.helperx.db;//package com.cc.helperqq.db;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.cc.helperqq.Entity.ContactInfo;
//import com.cc.helperqq.Entity.GroupInfo;
//import com.cc.helperqq.Entity.MembersInfo;
//import com.cc.helperqq.Entity.ReplyMsgInfo;
//import com.cc.helperqq.utils.LogUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by fangying on 2017/9/5.
// */
//
//public class DBOperator {
//
//    /**
//     * 添加數據
//     *
//     * @param database
//     * @param info
//     */
//    public void addData(SQLiteDatabase database, ContactInfo info) {
//        ContentValues values = new ContentValues();
//        values.put(DBConfig.C_ID, info.getId());
//        values.put(DBConfig.C_QQCONTACT_ID, info.getContactId());
//        values.put(DBConfig.C_QQCONTACT_NAME, info.getContactName());
//        values.put(DBConfig.C_QQCONTACT_SEX, info.getContactSex());
//        database.insert(DBConfig.TB_QQCONTACT, null, values);
//    }
//
//    public void addReplyMsgData(SQLiteDatabase database, ReplyMsgInfo msgInfo) {
//        ContentValues values = new ContentValues();
//        values.put(DBConfig.C_ID, msgInfo.getId());
//        values.put(DBConfig.C_REPLYMSG_NAME, msgInfo.getMsgName());
//        values.put(DBConfig.C_REPLYMSG_TIME, msgInfo.getMsgTime());
//        values.put(DBConfig.C_REPLYMSG_TYPE, msgInfo.getMsgType());
//        values.put(DBConfig.C_REPLYMSG_ID, msgInfo.getMsgid());
//        database.insert(DBConfig.TB_REPLYMSG, null, values);
//    }
//
//    public void addGroupData(SQLiteDatabase database, GroupInfo info) {
//        ContentValues values = new ContentValues();
//        values.put(DBConfig.C_ID, info.getId());
//        values.put(DBConfig.C_QQGROUP_ID, info.getGroupId());
//        values.put(DBConfig.C_QQGROUP_NAME, info.getGroupName());
//        values.put(DBConfig.C_QQGROUP_TYPE, info.getGroupType());
//        values.put(DBConfig.C_QQGROUP_INDEX, info.getGroupIsTrue());
//        database.insert(DBConfig.TB_QQGROUP, null, values);
//    }
//
//    public void addMembersData(SQLiteDatabase database, MembersInfo membersInfo) {
//        ContentValues values = new ContentValues();
////        values.put(DBConfig.ID, membersInfo.getId());
//        values.put(DBConfig.C_GROUP_ID, membersInfo.getGroupId() + "");
//        values.put(DBConfig.C_MEMBERS_ID, membersInfo.getMemberId() + "");
//        values.put(DBConfig.C_MEMBERS_NAME, membersInfo.getMembersName() + "");
//        values.put(DBConfig.C_MEMBERS_SENDTIME, membersInfo.getMemberSendTime() + "");
//        values.put(DBConfig.C_MEGROUP_NAME, membersInfo.getGroupName() + "");
//        database.insert(DBConfig.TB_GROUP_MEMBERS, null, values);
//    }
//
//    /***
//     * 刪除數據
//     * @param database
//     * @param type
//     */
//    public void DeleteData(SQLiteDatabase database, String type) {
//        database.execSQL("delete from " + "表名" + " where " + "字段" + " = " + type);
//    }
//
//    public boolean isfind(SQLiteDatabase database, String tb, String id, String str) {
//
//        // query(String table, String[] columns, String selection,String[] selectionArgs, String groupBy, String having, String orderBy)
//        Cursor cursor = database.rawQuery("select * from " + tb + " where " + id + " = " + str, null);
//        boolean result = cursor.moveToNext();
//        cursor.close();
//        return result;
//    }
//
//    public ReplyMsgInfo queryReplyMsg(SQLiteDatabase database, String selection, String name) {
//        Cursor cursor = database.query(DBConfig.TB_REPLYMSG, null, selection + "=?", new String[]{name}, null, null, null);
//        ReplyMsgInfo replyMsgInfo = new ReplyMsgInfo();
//        while (cursor.moveToNext()) {
//            int index = 0;
//            replyMsgInfo.setId(cursor.getInt(cursor.getColumnIndex(DBConfig.C_ID)));
//            replyMsgInfo.setMsgid(cursor.getString(cursor.getColumnIndex(DBConfig.C_REPLYMSG_ID)));
//            replyMsgInfo.setMsgName(cursor.getString(cursor.getColumnIndex(DBConfig.C_REPLYMSG_NAME)));
//            replyMsgInfo.setMsgTime(cursor.getString(cursor.getColumnIndex(DBConfig.C_REPLYMSG_TIME)));
//            replyMsgInfo.setMsgType(cursor.getInt(cursor.getColumnIndex(DBConfig.C_REPLYMSG_TYPE)));
//        }
//        cursor.close();
//        return replyMsgInfo;
//    }
//
//    public GroupInfo queryGroup(SQLiteDatabase database, String groupName) {
//        Cursor cursor = database.query(DBConfig.TB_QQGROUP, null, DBConfig.C_QQGROUP_NAME + "=?", new String[]{groupName}, null, null, null);
//        GroupInfo groupInfo = new GroupInfo();
//        while (cursor.moveToNext()) {
//            int index = 0;
//            groupInfo.setId(cursor.getInt(cursor.getColumnIndex(DBConfig.C_ID)));
//            groupInfo.setGroupIsTrue(cursor.getString(cursor.getColumnIndex(DBConfig.C_QQGROUP_INDEX)));
//            groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(DBConfig.C_QQGROUP_ID)));
//            groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(DBConfig.C_QQGROUP_NAME)));
//            groupInfo.setGroupType(cursor.getString(cursor.getColumnIndex(DBConfig.C_QQGROUP_TYPE)));
//        }
//        cursor.close();
//        return groupInfo;
//    }
//
//
//    public List<ContactInfo> queryAllContact(SQLiteDatabase database) {
//        Cursor cursor = database.rawQuery("select * from " + DBConfig.TB_QQCONTACT, null);
//        List<ContactInfo> infoList = new ArrayList<ContactInfo>();
//        while (cursor.moveToNext()) {
//            ContactInfo info = new ContactInfo();
//            int index = 0;
//            info.setContactId(cursor.getString(cursor.getColumnIndex(DBConfig.C_QQCONTACT_ID)));
//            info.setContactName(cursor.getString(cursor.getColumnIndex(DBConfig.C_QQCONTACT_NAME)));
////            info.setContactSex(cursor.getString(index++));
//            infoList.add(info);
//            info = null;
//        }
//        cursor.close();
//        return infoList;
//    }
//
//    public List<GroupInfo> queryAllGroup(SQLiteDatabase database) {
//
//        Cursor cursor = database.rawQuery("select * from " + DBConfig.TB_QQGROUP, null);
//        List<GroupInfo> infoList = new ArrayList<GroupInfo>();
//        while (cursor.moveToNext()) {
//            GroupInfo info = new GroupInfo();
////            int index = 0;
////            info.setId(cursor.getInt(cursor.getColumnIndex(DBConfig.C_ID)));
//            info.setGroupId(cursor.getString(cursor.getColumnIndex(DBConfig.C_QQGROUP_ID)));
//            info.setGroupName(cursor.getString(cursor.getColumnIndex(DBConfig.C_QQGROUP_NAME)));
//            info.setGroupType(cursor.getString(cursor.getColumnIndex(DBConfig.C_QQGROUP_TYPE)));
//            info.setGroupIsTrue(cursor.getString(cursor.getColumnIndex(DBConfig.C_QQGROUP_INDEX)));
//            infoList.add(info);
//            info = null;
//        }
//        cursor.close();
//        return infoList;
//    }
//
//    public List<ReplyMsgInfo> queryAllReplyMsg(SQLiteDatabase database) {
//        Cursor cursor = database.rawQuery("select * from " + DBConfig.TB_REPLYMSG, null);
//        List<ReplyMsgInfo> replyMsgInfoList = new ArrayList<ReplyMsgInfo>();
//        while (cursor.moveToNext()) {
//            ReplyMsgInfo info = new ReplyMsgInfo();
//            info.setId(cursor.getInt(cursor.getColumnIndex(DBConfig.C_ID)));
//            info.setMsgid(cursor.getString(cursor.getColumnIndex(DBConfig.C_REPLYMSG_ID)));
//            info.setMsgName(cursor.getString(cursor.getColumnIndex(DBConfig.C_REPLYMSG_NAME)));
//            info.setMsgTime(cursor.getString(cursor.getColumnIndex(DBConfig.C_REPLYMSG_TIME)));
//            info.setMsgType(cursor.getInt(cursor.getColumnIndex(DBConfig.C_REPLYMSG_TYPE)));
//            replyMsgInfoList.add(info);
//            info = null;
//        }
//        cursor.close();
//        return replyMsgInfoList;
//    }
//
//    public List<MembersInfo> queryAllMembers(SQLiteDatabase database) {
//        Cursor cursor = database.rawQuery("select * from " + DBConfig.TB_GROUP_MEMBERS, null);
//        List<MembersInfo> membersInfos = new ArrayList<MembersInfo>();
//        while (cursor.moveToNext()) {
//            MembersInfo info = new MembersInfo();
//            info.setId(cursor.getInt(cursor.getColumnIndex(DBConfig.ID)));
//            info.setGroupId(cursor.getString(cursor.getColumnIndex(DBConfig.C_GROUP_ID)));
//            info.setMemberSendTime(cursor.getString(cursor.getColumnIndex(DBConfig.C_MEMBERS_SENDTIME)));
//            info.setMembersName(cursor.getString(cursor.getColumnIndex(DBConfig.C_MEMBERS_NAME)));
//            info.setMemberId(cursor.getString(cursor.getColumnIndex(DBConfig.C_MEMBERS_ID)));
//            info.setMembersSex(cursor.getString(cursor.getColumnIndex(DBConfig.C_MEMBERS_SEX)));
//            info.setGroupName(cursor.getString(cursor.getColumnIndex(DBConfig.C_MEGROUP_NAME)));
//            membersInfos.add(info);
//            info = null;
//        }
//        cursor.close();
//        return membersInfos;
//    }
//
//    public void updateGroupData(SQLiteDatabase database, GroupInfo groupInfo) {
//        if (isDataExists(database, DBConfig.TB_QQGROUP, DBConfig.C_QQGROUP_ID, DBConfig.C_QQGROUP_ID, groupInfo.getGroupId())) {
//            ContentValues values = new ContentValues();
//            values.put(DBConfig.C_ID, groupInfo.getId());
//            values.put(DBConfig.C_QQGROUP_ID, groupInfo.getGroupId());
//            values.put(DBConfig.C_QQGROUP_NAME, groupInfo.getGroupName());
//            values.put(DBConfig.C_QQGROUP_TYPE, groupInfo.getGroupType());
//            values.put(DBConfig.C_QQGROUP_INDEX, groupInfo.getGroupIsTrue());
//            database.update(DBConfig.TB_QQGROUP, values, DBConfig.C_QQGROUP_ID + "=" + groupInfo.getGroupId(), null);
//        }
//    }
//
//    public void updateReplyMsgData(SQLiteDatabase database, ReplyMsgInfo msgInfo) {
//        if (isDataExists(database, DBConfig.TB_REPLYMSG, DBConfig.C_REPLYMSG_NAME, DBConfig.C_REPLYMSG_NAME, msgInfo.getMsgName())) {
//            ContentValues values = new ContentValues();
//            values.put(DBConfig.C_ID, msgInfo.getId());
//            values.put(DBConfig.C_REPLYMSG_NAME, msgInfo.getMsgName());
//            values.put(DBConfig.C_REPLYMSG_TIME, msgInfo.getMsgTime());
//            values.put(DBConfig.C_REPLYMSG_TYPE, msgInfo.getMsgType());
//            values.put(DBConfig.C_QQCONTACT_ID, msgInfo.getMsgid());
//            database.update(DBConfig.TB_REPLYMSG, values, DBConfig.C_REPLYMSG_NAME + "=" + msgInfo.getMsgName(), null);
//        }
//    }
//
////    /***
////     * 更新數據
////     * @param database
////     * @param info
////     */
////    public void updateData(SQLiteDatabase database, ContactInfo info) {
////        if (isDataExists(database, info.getId(), DbConfig.TB_QQCONTACT)) {
////            ContentValues values = new ContentValues();
////            values.put(DbConfig.C_QQCONTACT_ID, info.getContactId());
////            values.put(DbConfig.C_QQCONTACT_NAME, info.getContactName());
////            values.put(DbConfig.C_QQCONTACT_SEX, info.getContactSex());
////
////            database.update(DbConfig.TB_QQCONTACT, values, DbConfig.C_ID + " = " + info.getId(), null);
////        }
////    }
////
////    public void updateData(SQLiteDatabase database, GroupInfo info) {
////        if (isDataExists(database, info.getId(), DbConfig.TB_QQGROUP)) {
////            ContentValues values = new ContentValues();
////            values.put(DbConfig.C_QQGROUP_ID, info.getGroupId());
////            values.put(DbConfig.C_QQGROUP_NAME, info.getGroupName());
//////            values.put(DbConfig.C_QQCONTACT_SEX,info.getContactSex());
////
////            database.update(DbConfig.TB_QQGROUP, values, DbConfig.C_ID + " = " + info.getId(), null);
////        }
////    }
//
//
//    public int dataNum(SQLiteDatabase database, String tb, String columns, String whereStr, String str) {
//        Cursor cursor = database.query(tb, new String[]{columns}, whereStr + "=?", new String[]{str}, null, null, null);
//        int count = cursor.getCount();
//        cursor.close();
//        return count;
//    }
//
//
//    public boolean isDataExists(SQLiteDatabase database, String tb, String columns, String whereStr, String str) {
//        // query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
//        Cursor cursor = database.query(tb, new String[]{columns}, whereStr + "=?", new String[]{str}, null, null, null);
//        int count = cursor.getCount();
//        cursor.close();
//        return count > 0;
////        String[] selectionArgs={"星期一","2013"};
//    }
//
//    public boolean isExists(SQLiteDatabase database, String tb, String columns1, String columns2, String selection, String selectionArgs1, String selectionArgs2) {
//        // query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
//        Cursor cursor = database.query(tb, new String[]{columns1, columns2}, selection, new String[]{selectionArgs1, selectionArgs2}, null, null, null);
//        int count = cursor.getCount();
//        cursor.close();
//        return count > 0;
//
//    }
//
//    public void updaMemberData(SQLiteDatabase database, MembersInfo membersInfo) {
//        if (isDataExists(database, DBConfig.TB_GROUP_MEMBERS, DBConfig.C_MEMBERS_ID, DBConfig.C_MEMBERS_ID, membersInfo.getMemberId())) {
//            ContentValues values = new ContentValues();
////            values.put(DBConfig.ID, membersInfo.getId());
//            values.put(DBConfig.C_GROUP_ID, membersInfo.getGroupId() + "");
//            values.put(DBConfig.C_MEMBERS_ID, membersInfo.getMemberId() + "");
//            values.put(DBConfig.C_MEMBERS_NAME, membersInfo.getMembersName() + "");
//            values.put(DBConfig.C_MEMBERS_SENDTIME, membersInfo.getMemberSendTime() + "");
//            values.put(DBConfig.C_MEGROUP_NAME, membersInfo.getGroupName() + "");
//            database.insert(DBConfig.TB_GROUP_MEMBERS, null, values);
//        }
//    }
//}
