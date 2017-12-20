package com.cc.task.helperx.utils;

/**
 * Created by yzq on 2017/5/15.
 */

public class RandomName {

    private static String[] base = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9"};

    public static String getRandomName(int range){
        StringBuilder sb = new StringBuilder();
        String factorStr;
        for(int i = 0; i < range; i ++){
            int factor = Utils.getRandom().nextInt(base.length);
            if(i % 3 == 0){
                factorStr = base[factor].toUpperCase();
            }else{
                factorStr = base[factor];
            }
            sb.append(factorStr);
        }
        return sb.toString();
    }
}
