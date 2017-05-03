package com.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by chenliang3 on 2016/3/3.
 */
public class DaoGeneration {

    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(1, "com.ya.mei.greendao");
        addNews(schema);
        addStats(schema);
        new DaoGenerator().generateAll(schema, "../NBA/app/src/main/java-gen");
    }

    public static void addNews(Schema schema){
        Entity news = schema.addEntity("GreenNews");
        news.addIdProperty();
        news.addStringProperty("newslist");
        news.addStringProperty("type");
    }

    public static void addStats(Schema schema){
        Entity stat = schema.addEntity("GreenStat");
        stat.addIdProperty();
        stat.addStringProperty("statentity");
        stat.addStringProperty("statkind");
    }

}
