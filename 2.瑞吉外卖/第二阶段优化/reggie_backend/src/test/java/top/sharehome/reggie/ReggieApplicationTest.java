package top.sharehome.reggie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ReggieApplicationTest {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class TestUser {
        private String name;
        private Integer age;
        private String address;
        public final static String NAME = "name";
        public final static String AGE = "age";
        public final static String ADDRESS = "address";
    }

    /**
     * Redis操作String类型数据，redis中String一般用于存储单个数据；
     */
    @Test
    public void doString() {
        // 首先创建String类型的操作类
        ValueOperations<Object, Object> opsForValue = redisTemplate.opsForValue();

        // 普通增加（修改）
        opsForValue.set("key1", "value1");
        // 定时增加（修改），超时时间为10秒
        opsForValue.set("key2", "value2", 10, TimeUnit.SECONDS);
        // 条件增加（修改），如果key存在就增加（修改）失败
        Boolean ifAbsent = opsForValue.setIfAbsent("key3", "value3");
        System.out.println("ifAbsent = " + ifAbsent);
        // 条件增加（修改），如果key不存在就增加（修改）失败
        Boolean ifPresent = opsForValue.setIfPresent("key4", "value4");
        System.out.println("ifPresent = " + ifPresent);

        // 批量增加
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        for (int i = 0; i < 5; i++) {
            String key = "key" + (i + 5);
            String value = "value" + (i + 5);
            map.put(key, value);
        }
        opsForValue.multiSet(map);

        // 查询
        ArrayList<Object> queryList = new ArrayList<Object>();
        for (int i = 0; i < 9; i++) {
            Object o = opsForValue.get("key" + (i + 1));
            queryList.add(o);
        }
        System.out.println("list = " + queryList.toString());

        // 删除
        ArrayList<Object> deleteList = new ArrayList<Object>();
        for (int i = 0; i < 9; i++) {
            deleteList.add("key" + (i + 1));
        }
        redisTemplate.delete(deleteList);
    }


    /**
     * Redis操作Hash类型数据，redis中Hash一般用于存储对象数据
     */
    @Test
    public void doHash() {
        // 首先创建Hash类型的操作类
        HashOperations<Object, Object, Object> opsForHash = redisTemplate.opsForHash();
        // 增加（修改）对象（name，age，address单个字段）
        TestUser testUser1 = new TestUser("Antony1", 21, "HRBCU1");
        opsForHash.put("person1", TestUser.NAME, testUser1.getName());
        opsForHash.put("person1", TestUser.AGE, testUser1.getAge());
        opsForHash.put("person1", TestUser.ADDRESS, testUser1.getAddress());
        // 增加（修改）对象（Map对象）
        TestUser testUser2 = new TestUser("Antony2", 22, "HRBCU2");
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put(TestUser.NAME, testUser2.getName());
        map.put(TestUser.AGE, testUser2.getAge());
        map.put(TestUser.ADDRESS, testUser2.getAddress());
        opsForHash.putAll("person2", map);

        // 查询对象（单个字段查询后组成一个对象）
        String name1 = (String) opsForHash.get("person1", TestUser.NAME);
        Integer age1 = (Integer) opsForHash.get("person1", TestUser.AGE);
        String address1 = (String) opsForHash.get("person1", TestUser.ADDRESS);
        TestUser queryPerson1 = new TestUser(name1, age1, address1);
        System.out.println("queryUser1 = " + queryPerson1);

        // 查询对象（entries查询）
        Map<Object, Object> mapEntry = opsForHash.entries("person1");
        TestUser queryPerson2 = new TestUser((String) mapEntry.get(TestUser.NAME), (Integer) mapEntry.get(TestUser.AGE), (String) mapEntry.get(TestUser.ADDRESS));
        System.out.println("queryPerson2 = " + queryPerson2);

        // 查询对象（keys,values查询）
        Set<Object> keys = opsForHash.keys("person1");
        List<Object> keyList = new ArrayList<>(keys);
        List<Object> valueList = opsForHash.values("person1");
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        for (int i = 0; i < keys.size(); i++) {
            hashMap.put(keyList.get(i), valueList.get(i));
        }
        TestUser queryPerson3 = new TestUser((String) hashMap.get(TestUser.NAME), (Integer) hashMap.get(TestUser.AGE), (String) hashMap.get(TestUser.ADDRESS));
        System.out.println("queryPerson3 = " + queryPerson3);

        // 删除对象
        opsForHash.delete("person1", TestUser.NAME, TestUser.AGE, TestUser.ADDRESS);
        opsForHash.delete("person2", TestUser.NAME, TestUser.AGE, TestUser.ADDRESS);
    }

    /**
     * Redis操作List有序可重复集合类型数据，redis中List一般用于存储多个数据，该数据存储时保持相同顺序且可重复
     * List可以看成一个队列，所以redis中的List数据类型也可以用来做消息队列使用
     */
    @Test
    public void doList() {
        // 首先创建List类型的操作类
        ListOperations<Object, Object> opsForList = redisTemplate.opsForList();

        // 向左（头部）添加一个数据（向右即为rightPush）
        opsForList.leftPush("list", 1);
        // 向左（头部）添加多个数据（向右即为rightPushAll）
        opsForList.leftPushAll("list", 5, 2, 4, 3);

        // 查询数据（需要查询范围）
        List<Object> list1 = opsForList.range("list", 0, -1);
        System.out.println("list = " + list1);

        // 向右（尾部）弹出一个数据（向左即为leftPop）
        // 利用循环弹出全部数据，弹出完毕后该List即被删除
        for (int i = 0; i < list1.size(); i++) {
            Object exit = opsForList.rightPop("list");
            System.out.println("第" + (i + 1) + "次弹出结果为：" + exit);
        }
    }

    /**
     * Redis操作Set无序不重复集合类型数据，redis中Set一般用于存储多个数据，该数据存储时不会保持相同顺序且不可重复
     */
    @Test
    public void doSet() {
        // 首先创建Set类型的操作类
        SetOperations<Object, Object> opsForSet = redisTemplate.opsForSet();

        // 添加（多个）数据
        opsForSet.add("set", 1, 1, 3, 3, 2, 2);

        // 查询数据
        Set<Object> members1 = opsForSet.members("set");
        System.out.println("members = " + members1);

        // 删除数据
        opsForSet.remove("set", 1, 2, 3);
    }

    /**
     * Redis操作ZSet权重排序不重复集合类型数据，redis中Set一般用于存储多个数据，该数据存储时会按照给定权重排序且可不重复
     */
    @Test
    public void doZSet() {
        // 首先创建ZSet类型的操作类
        ZSetOperations<Object, Object> opsForZSet = redisTemplate.opsForZSet();

        // 添加数据和对应权重，当重复的数据时，权重会被覆盖，redis会按照权重由小到大排序
        opsForZSet.add("zset", 1, 10.0);
        opsForZSet.add("zset", 2, 11.0);
        opsForZSet.add("zset", 3, 12.0);
        opsForZSet.add("zset", 1, 13.0);
        opsForZSet.add("zset", 100, 14.0);

        // 查询数据
        Set<Object> zset1 = opsForZSet.range("zset", 0, -1);
        System.out.println("zset1 = " + zset1);

        // 改变权重
        opsForZSet.incrementScore("zset", 2, 20.0);
        Set<Object> zset2 = opsForZSet.range("zset", 0, -1);
        System.out.println("zset2 = " + zset2);

        // 删除数据
        opsForZSet.remove("zset", 100, 1, 2, 3);
    }

    /**
     * Redis中的一些通用操作
     */
    @Test
    public void testGeneral() {
        // 获取redis中所有的key
        Set<Object> keys = redisTemplate.keys("*");
        System.out.println("keys = " + keys);

        // 判断某个key是否存在
        Boolean hasKey = redisTemplate.hasKey("backup1");
        System.out.println("hasKey = " + hasKey);

        // 删除指定key
        Boolean delete = redisTemplate.delete("backup1");
        System.out.println("delete = " + delete);

        // 获取指定key对应的value的数据类型
        DataType studentType = redisTemplate.type("backup2");
        System.out.println("studentType = " + studentType);
    }
}