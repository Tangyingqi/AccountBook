package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String[] args)throws Exception{
        //两个参数分别代表：数据库版本号，自动代码生成所在的包名
        Schema schema = new Schema(1,"com.tyq.greendao");

        addData(schema);

        //使用DaoGenerator的generateAll方法自动生成代码，会放入第二个参数的路径中
        new DaoGenerator().generateAll(schema,"/Android Sample/AccountBook/app/src/main/java-gen");

    }

    private static void addData(Schema schema){
        //一个实体类就代表一张表，此处表名为Person
        Entity person = schema.addEntity("Person");

        //生成表的字段
        person.addIdProperty();
        person.addStringProperty("name");
        person.addStringProperty("money");
    }
}
