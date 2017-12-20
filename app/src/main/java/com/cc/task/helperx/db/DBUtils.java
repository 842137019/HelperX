package com.cc.task.helperx.db;

/**
 *
 * Created by Administrator on 2017/5/17.
 */

public class DBUtils {

//    public static <E> void setFieldValueByFieldName(E entry, Class entryClass, String fieldName, Object value) {
//        try {
//            Field idField = entryClass.getDeclaredField(fieldName);
//            idField.setAccessible(true);
//            idField.set(entry, value);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static <E> List<E> dbValueConverJavaValue(Cursor cursor, Class<E> tableClass) {
//        List<E> entrys = new ArrayList<>();
//        try {
//            String[] columnNames = cursor.getColumnNames();
//            if (columnNames.length == 0) {
//                return entrys;
//            }
//            while (cursor.moveToNext()) {
//                E entry = tableClass.newInstance();
//                for(String fieldName : columnNames) {
//                    setFieldValue(cursor, entry, tableClass, fieldName);
//                }
//                entrys.add(entry);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return entrys;
//    }
//
//    public static <E> void setFieldValue(Cursor cursor, E entry, Class<E> tableClass, String fieldName) throws Exception {
//        Field field = tableClass.getDeclaredField(fieldName);
//        int fieldIndex = cursor.getColumnIndex(fieldName);
//        field.setAccessible(true);
//        Class fieldTypeClass = field.getType();
//        if (fieldTypeClass == int.class || fieldTypeClass == Integer.class) {
//            int intValue = cursor.getInt(fieldIndex);
//            field.setInt(entry, intValue);
//        } else if (fieldTypeClass == long.class || fieldTypeClass == Long.class) {
//            long longValue = cursor.getLong(fieldIndex);
//            field.setLong(entry, longValue);
//        } else if (fieldTypeClass == boolean.class || fieldTypeClass == Boolean.class) {
//            int intValue = cursor.getInt(fieldIndex);
//            boolean isTrue = false;
//            if (intValue == 1) {
//                isTrue = true;
//            }
//            field.setBoolean(entry, isTrue);
//        } else if (fieldTypeClass == String.class) {
//            String strValue = cursor.getString(fieldIndex);
//            if(!TextUtils.isEmpty(strValue)){
//                field.set(entry, strValue);
//            }
//        } else if (fieldTypeClass == float.class || fieldTypeClass == Float.class) {
//            float floatValue = cursor.getFloat(fieldIndex);
//            field.setFloat(entry, floatValue);
//        } else if (fieldTypeClass == double.class || fieldTypeClass == Double.class) {
//            double doubleValue = cursor.getDouble(fieldIndex);
//            field.setDouble(entry, doubleValue);
//        }
//    }
//
//    public static Map<String, String> getFieldNameAndTypeByClass(Class tableClass) {
//        Field[] fileds = tableClass.getDeclaredFields();
//        Map<String, String> tbFields = new HashMap<>();
//        for (Field field : fileds) {
//            int fieldModifier = field.getModifiers();
//            if (!Modifier.isFinal(fieldModifier) && !Modifier.isStatic(fieldModifier)) {
//                String tbFieldName = field.getName();
//                Class fieldJavaType = field.getType();
//                String fieldType = javaTypeConverDbType(fieldJavaType);
//                if (tbFieldName.equals(DBManager.TB_ID_NAME)) {
//                    if (fieldJavaType == Long.class || fieldJavaType == long.class) {
//                        tbFields.put(tbFieldName, fieldType);
//                    }
//                } else {
//                    tbFields.put(tbFieldName, fieldType);
//                }
//            }
//        }
//        return tbFields;
//    }
//
//    public static <E> void javaTypeConverDbType(E entry, ContentValues values, Field field) throws Exception {
//        String tbFieldName = field.getName();
//        Class javaType = field.getType();
//        field.setAccessible(true);
//        if (javaType == String.class) {
//            Object tbFieldValue = field.get(entry);
//            if (tbFieldValue != null) {
//                values.put(tbFieldName, tbFieldValue.toString());
//            }else{
//                values.put(tbFieldName, "");
//            }
//        } else if (javaType == int.class || javaType == Integer.class) {
//            values.put(tbFieldName, field.getInt(entry));
//        } else if (javaType == long.class || javaType == Long.class) {
//            values.put(tbFieldName, field.getLong(entry));
//        } else if (javaType == float.class || javaType == Float.class) {
//            values.put(tbFieldName, field.getFloat(entry));
//        } else if (javaType == double.class || javaType == Double.class) {
//            values.put(tbFieldName, field.getDouble(entry));
//        } else if (javaType == boolean.class || javaType == Boolean.class) {
//            int filedValue = 0;
//            if (field.getBoolean(entry)) {
//                filedValue = 1;
//            }
//            values.put(tbFieldName, filedValue);
//        }
//    }
//
//    public static String javaTypeConverDbType(Class javaType) {
//        if (javaType == null) {
//            return null;
//        }
//        String dbType = null;
//        if (javaType == String.class) {
//            dbType = "text";
//        } else if (javaType == int.class || javaType == Integer.class) {
//            dbType = "integer";
//        } else if (javaType == long.class || javaType == Long.class) {
//            dbType = "integer";
//        } else if (javaType == float.class || javaType == Float.class) {
//            dbType = "float";
//        } else if (javaType == double.class || javaType == Double.class) {
//            dbType = "double";
//        } else if (javaType == boolean.class || javaType == Boolean.class) {
//            dbType = "integer";
//        }
//        return dbType;
//    }
//
//    public static <E> ContentValues getFieldNameAndValueByClass(E entry, Class tableClass) {
//        Field[] fileds = tableClass.getDeclaredFields();
//        ContentValues values = new ContentValues();
//        try {
//            for (Field field : fileds) {
//                int fieldModifier = field.getModifiers();
//                if (!Modifier.isFinal(fieldModifier) && !Modifier.isStatic(fieldModifier)) {
//                    javaTypeConverDbType(entry, values, field);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return values;
//    }

}
