package com.epam.rd.java.basic.practice8;

import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;
import org.junit.Assert;
import org.junit.Test;

public class EntityTest {

    @Test
    public void usersWithSameLoginShouldBeEqual() {
        User u1 = new User();
        User u2 = new User();
        u1.setLogin("John");
        u1.setId(1);
        u2.setLogin("John");
        u2.setId(2);
        Assert.assertEquals(u1, u2);
    }

    @Test
    public void teamsWithSameNameShouldBeEqual() {
        Team t1 = new Team();
        Team t2 = new Team();
        t1.setName("Kyiv");
        t1.setId(1);
        t2.setName("Kyiv");
        t2.setId(2);
        Assert.assertEquals(t1, t2);
    }

    @Test
    public void sameEntitiesShouldBeEqual() {
        Team t1 = new Team();
        t1.setName("Kyiv");
        Team t2 = t1;
        User u1 = new User();
        u1.setLogin("John");
        User u2 = u1;
        Assert.assertEquals(t1, t2);
        Assert.assertEquals(u1, u2);
        Assert.assertEquals(t1.hashCode(), t2.hashCode());
        Assert.assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    public void entitiesOfDifferentClassesShouldNotBeEqual() {
        Team t1 = new Team();
        t1.setName("Kyiv");
        User u1 = new User();
        u1.setLogin("John");
        Assert.assertNotEquals(t1, u1);
        Assert.assertNotEquals(u1, t1);
    }


}
