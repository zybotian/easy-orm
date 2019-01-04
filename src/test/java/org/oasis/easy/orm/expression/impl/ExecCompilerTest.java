package org.oasis.easy.orm.expression.impl;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author tianbo
 * @date 2018-12-24
 */
public class ExecCompilerTest {

    @Test
    public void testDelete() throws Exception {
        String sql1 = "DELETE FROM user WHERE account_time=:1";
        ExecCompiler doCompiler1 = new ExecCompiler(sql1);
        BunchUnit units1 = (BunchUnit) doCompiler1.doCompile();
        Assert.assertEquals("DELETE FROM user WHERE account_time=", units1.getUnits().get(0).toString());
        Assert.assertEquals(":1", units1.getUnits().get(1).toString());

        String sql2 = "DELETE FROM user WHERE account_time=:1 AND status=1";
        ExecCompiler doCompiler2 = new ExecCompiler(sql2);
        BunchUnit units2 = (BunchUnit) doCompiler2.doCompile();
        Assert.assertEquals("DELETE FROM user WHERE account_time=", units2.getUnits().get(0).toString());
        Assert.assertEquals(":1", units2.getUnits().get(1).toString());
        Assert.assertEquals(" AND status=1", units2.getUnits().get(2).toString());

        String sql3 = "DELETE FROM user WHERE 1=1";
        ExecCompiler doCompiler3 = new ExecCompiler(sql3);
        TextUnit units3 = (TextUnit) doCompiler3.doCompile();
        Assert.assertEquals("DELETE FROM user WHERE 1=1", units3.toString());

        String sql4 = "DELETE FROM user";
        ExecCompiler doCompiler4 = new ExecCompiler(sql4);
        TextUnit units4 = (TextUnit) doCompiler4.doCompile();
        Assert.assertEquals("DELETE FROM user", units4.toString());

        String sql5 = "DELETE * FROM user";
        ExecCompiler doCompiler5 = new ExecCompiler(sql5);
        TextUnit units5 = (TextUnit) doCompiler5.doCompile();
        Assert.assertEquals("DELETE * FROM user", units5.toString());
    }

    @Test
    public void testUpdate() throws Exception {
        String sql1 = "UPDATE Person SET Address = 1, City = 2 WHERE LastName = 3";
        ExecCompiler doCompiler1 = new ExecCompiler(sql1);
        TextUnit units1 = (TextUnit) doCompiler1.doCompile();
        Assert.assertEquals("UPDATE Person SET Address = 1, City = 2 WHERE LastName = 3", units1.toString());

        String sql2 = "UPDATE Person SET Address = :1, City = :2 WHERE LastName = :3";
        ExecCompiler doCompiler2 = new ExecCompiler(sql2);
        BunchUnit units2 = (BunchUnit) doCompiler2.doCompile();
        Assert.assertEquals("UPDATE Person SET Address = ", units2.getUnits().get(0).toString());
        Assert.assertEquals(":1", units2.getUnits().get(1).toString());
        Assert.assertEquals(", City = ", units2.getUnits().get(2).toString());
        Assert.assertEquals(":2", units2.getUnits().get(3).toString());
        Assert.assertEquals(" WHERE LastName = ", units2.getUnits().get(4).toString());
        Assert.assertEquals(":3", units2.getUnits().get(5).toString());

        String sql3 = "UPDATE Person SET Address = :1.address, City = :1.city WHERE LastName = :1.lastName";
        ExecCompiler doCompiler3 = new ExecCompiler(sql3);
        BunchUnit units3 = (BunchUnit) doCompiler3.doCompile();
        Assert.assertEquals("UPDATE Person SET Address = ", units3.getUnits().get(0).toString());
        Assert.assertEquals(":1.address", units3.getUnits().get(1).toString());
        Assert.assertEquals(", City = ", units3.getUnits().get(2).toString());
        Assert.assertEquals(":1.city", units3.getUnits().get(3).toString());
        Assert.assertEquals(" WHERE LastName = ", units3.getUnits().get(4).toString());
        Assert.assertEquals(":1.lastName", units3.getUnits().get(5).toString());

        String sql4 = "UPDATE Person SET Address = :address, City = :city WHERE LastName = :lastName";
        ExecCompiler doCompiler4 = new ExecCompiler(sql4);
        BunchUnit units4 = (BunchUnit) doCompiler4.doCompile();
        Assert.assertEquals("UPDATE Person SET Address = ", units4.getUnits().get(0).toString());
        Assert.assertEquals(":address", units4.getUnits().get(1).toString());
        Assert.assertEquals(", City = ", units4.getUnits().get(2).toString());
        Assert.assertEquals(":city", units4.getUnits().get(3).toString());
        Assert.assertEquals(" WHERE LastName = ", units4.getUnits().get(4).toString());
        Assert.assertEquals(":lastName", units4.getUnits().get(5).toString());
    }

    @Test
    public void testSelect() throws Exception {
        String sql1 = "SELECT count(id) FROM user";
        ExecCompiler doCompiler1 = new ExecCompiler(sql1);
        TextUnit units1 = (TextUnit) doCompiler1.doCompile();
        Assert.assertEquals(sql1, units1.toString());

        String sql2 = "SELECT count(id) FROM user WHERE application_id=:1.appId";
        ExecCompiler doCompiler2 = new ExecCompiler(sql2);
        BunchUnit units2 = (BunchUnit) doCompiler2.doCompile();
        String[] unitStr2 = {"SELECT count(id) FROM user WHERE application_id=", ":1.appId"};
        for (int i = 0; i < unitStr2.length; i++) {
            Assert.assertEquals(unitStr2[i], units2.getUnits().get(i).toString());
        }

        String sql3 = "SELECT count(*) FROM user WHERE application_id=:1.appId";
        ExecCompiler doCompiler3 = new ExecCompiler(sql3);
        BunchUnit units3 = (BunchUnit) doCompiler3.doCompile();
        String[] unitStr3 = {"SELECT count(*) FROM user WHERE application_id=", ":1.appId"};
        for (int i = 0; i < unitStr3.length; i++) {
            Assert.assertEquals(unitStr3[i], units3.getUnits().get(i).toString());
        }

        String sql4 = "SELECT count(1) FROM user WHERE application_id=:1.appId";
        ExecCompiler doCompiler4 = new ExecCompiler(sql4);
        BunchUnit units4 = (BunchUnit) doCompiler4.doCompile();
        String[] unitStr4 = {"SELECT count(1) FROM user WHERE application_id=", ":1.appId"};
        for (int i = 0; i < unitStr4.length; i++) {
            Assert.assertEquals(unitStr4[i], units4.getUnits().get(i).toString());
        }

        String sql5 = "SELECT count(1) FROM user WHERE application_id=:1";
        ExecCompiler doCompiler5 = new ExecCompiler(sql5);
        BunchUnit units5 = (BunchUnit) doCompiler5.doCompile();
        String[] unitStr5 = {"SELECT count(1) FROM user WHERE application_id=", ":1"};
        for (int i = 0; i < unitStr5.length; i++) {
            Assert.assertEquals(unitStr5[i], units5.getUnits().get(i).toString());
        }

        String sql6 = "SELECT count(distinct(id)) FROM user WHERE application_id IN (:1.appId)";
        ExecCompiler doCompiler6 = new ExecCompiler(sql6);
        BunchUnit units6 = (BunchUnit) doCompiler6.doCompile();
        String[] unitStr6 = {"SELECT count(distinct(id)) FROM user WHERE application_id IN (", ":1.appId", ")"};

        for (int i = 0; i < unitStr6.length; i++) {
            Assert.assertEquals(unitStr6[i], units6.getUnits().get(i).toString());
        }

        String sql7 = "SELECT user_id FROM user WHERE create_time>=:time1 and create_time<:time2 ORDER BY create_time DESC LIMIT :offset,:limit";
        ExecCompiler doCompiler7 = new ExecCompiler(sql7);
        BunchUnit units7 = (BunchUnit) doCompiler7.doCompile();
        String[] unitStr7 = {"SELECT user_id FROM user WHERE create_time>=", ":time1", " and create_time<", ":time2",
                " ORDER BY create_time DESC LIMIT ", ":offset", ",", ":limit"};
        for (int i = 0; i < unitStr7.length; i++) {
            Assert.assertEquals(unitStr7[i], units7.getUnits().get(i).toString());
        }

        String sql8 = "SELECT id,name FROM ##(:table) WHERE id>0"
                + "#if(:startTime > 0) { AND create_time>=:startTime }"
                + "#if(:statusCode != 0) { AND status=:statusCode } #else { AND status > 0 }"
                + "#if(:groupBy!=null) { GROUP BY ##(:groupBy) }"
                + "#if(:orderBy!=null) { ORDER BY ##(:orderBy) } #else { ORDER BY create_time DESC }"
                + "#if(:limit>0) { LIMIT :limit } #else { LIMIT 20 }";
        ExecCompiler doCompiler8 = new ExecCompiler(sql8);
        BunchUnit units8 = (BunchUnit) doCompiler8.doCompile();
        Assert.assertEquals("SELECT id,name FROM ", units8.getUnits().get(0).toString());

        JoinUnit joinExprUnit81 = (JoinUnit) units8.getUnits().get(1);
        Assert.assertEquals(":table", joinExprUnit81.toString());
        Assert.assertEquals(" WHERE id>0", units8.getUnits().get(2).toString());

        ChoiceUnit choiceUnit83 = (ChoiceUnit) units8.getUnits().get(3);
        Assert.assertEquals(":startTime > 0", choiceUnit83.getExpr());
        BunchUnit unitIfTrue83 = (BunchUnit) choiceUnit83.getIfUnit();
        Assert.assertEquals(" AND create_time>=", unitIfTrue83.getUnits().get(0).toString());
        Assert.assertEquals(":startTime", unitIfTrue83.getUnits().get(1).toString());

        ChoiceUnit choiceUnit84 = (ChoiceUnit) units8.getUnits().get(4);
        Assert.assertEquals(":statusCode != 0", choiceUnit84.getExpr());
        BunchUnit unitIfTrue84 = (BunchUnit) choiceUnit84.getIfUnit();
        Assert.assertEquals(" AND status=", unitIfTrue84.getUnits().get(0).toString());
        Assert.assertEquals(":statusCode", unitIfTrue84.getUnits().get(1).toString());
        Assert.assertEquals(" ", unitIfTrue84.getUnits().get(2).toString());
        TextUnit unitIfFalse84 = (TextUnit) choiceUnit84.getElseUnit();
        Assert.assertEquals(" AND status > 0 ", unitIfFalse84.toString());

        ChoiceUnit choiceUnit85 = (ChoiceUnit) units8.getUnits().get(5);
        Assert.assertEquals(":groupBy!=null", choiceUnit85.getExpr());

        ChoiceUnit choiceUnit86 = (ChoiceUnit) units8.getUnits().get(6);
        Assert.assertEquals(":orderBy!=null", choiceUnit86.getExpr());

        ChoiceUnit choiceUnit87 = (ChoiceUnit) units8.getUnits().get(7);
        Assert.assertEquals(":limit>0", choiceUnit87.getExpr());
    }

    @Test
    public void testCreateTable() throws Exception {
        String createUserTable = "create table user "
                + "(id bigint not null AUTO_INCREMENT primary key"
                + ",name varchar(200) not null "
                + ",group_id bigint not null"
                + ",birthday datetime not null"
                + ",age int not null"
                + ",money decimal(20,2) not null"
                + ",status int not null"
                + ",editable int not null"
                + ",create_time bigint not null"
                + ",update_time bigint not null"
                + ");";
        ExecCompiler doCompiler = new ExecCompiler(createUserTable);
        TextUnit textUnit = (TextUnit) doCompiler.doCompile();
        Assert.assertEquals(createUserTable, textUnit.toString());

        createUserTable = "CREATE TABLE IF NOT EXISTS `service_code` (" +
                "    `id`                    BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT" +
                "    COMMENT '主键'," +
                "    `code`                  VARCHAR(64)      NOT NULL DEFAULT 0" +
                "    COMMENT '内容'," +
                "    `status`                INT UNSIGNED     NOT NULL DEFAULT 0" +
                "    COMMENT '状态0=未使用,1=已使用'," +
                "    `user_id`               BIGINT UNSIGNED  NOT NULL DEFAULT 0" +
                "    COMMENT '用户id'," +
                "    `event_id`              BIGINT UNSIGNED  NOT NULL DEFAULT 0" +
                "    COMMENT '事件id'," +
                "    `create_time`           BIGINT UNSIGNED  NOT NULL DEFAULT 0" +
                "    COMMENT '创建时间'," +
                "    `update_time`           BIGINT UNSIGNED  NOT NULL DEFAULT 0" +
                "    COMMENT '修改时间'," +
                "    PRIMARY KEY (`id`)," +
                "    UNIQUE KEY `uk_sc_co` (`code`)" +
                ")" +
                "  ENGINE = InnoDB" +
                "  AUTO_INCREMENT = 1" +
                "  DEFAULT CHARSET = utf8;";
        ExecCompiler doCompiler1 = new ExecCompiler(createUserTable);
        TextUnit textUnit1 = (TextUnit) doCompiler1.doCompile();
        Assert.assertEquals(createUserTable, textUnit1.toString());
    }

    @Test
    public void testInsert() throws Exception {

        String insert = "insert into user values(:1.id,:1.name)";
        ExecCompiler doCompiler = new ExecCompiler(insert);
        BunchUnit units = (BunchUnit) doCompiler.doCompile();
        String[] unintStr1 = {"insert into user values(", ":1.id", ",", ":1.name", ")"};
        for (int i = 0; i < units.getUnits().size(); i++) {
            Assert.assertEquals(unintStr1[i], units.getUnits().get(i).toString());
        }

        String insert2 = "insert into user values(:entity.id,:entity.name)";
        ExecCompiler doCompiler2 = new ExecCompiler(insert2);
        units = (BunchUnit) doCompiler2.doCompile();
        String[] unintStr2 = {"insert into user values(", ":entity.id", ",", ":entity.name", ")"};
        for (int i = 0; i < units.getUnits().size(); i++) {
            Assert.assertEquals(unintStr2[i], units.getUnits().get(i).toString());
        }

        String insert3 = "insert into `user`(`id`,`name`) values(:entity.id,:entity.name)";
        ExecCompiler doCompiler3 = new ExecCompiler(insert3);
        units = (BunchUnit) doCompiler3.doCompile();
        String[] unintStr3 = {"insert into `user`(`id`,`name`) values(", ":entity.id", ",", ":entity.name", ")"};
        for (int i = 0; i < units.getUnits().size(); i++) {
            Assert.assertEquals(unintStr3[i], units.getUnits().get(i).toString());
        }
    }
}
