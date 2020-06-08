package cn.gdou;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class TravelPortionApplicationTests {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void  testRedis(){
        this.redisTemplate.opsForValue().set("k1","v1");
        String str=this.redisTemplate.opsForValue().get("k1");
        System.out.println("str="+str);
    }


    /*
    * 过期时间
    * */
    @Test
    public void  testRedis2(){
        this.redisTemplate.opsForValue().set("k2","v2",30, TimeUnit.SECONDS);
    }

    /*
    * redis中hash类型测试
    * */
    @Test
    public void testHash(){
        // <String,Object,Object> 参数1为key，【Object,Object为键值对】
        //redis中有user则获取，没有则创建
        BoundHashOperations<String, Object, Object> user = this.redisTemplate.boundHashOps("user");
        System.out.println(user);
        user.put("username","xu");
        user.put("age","23");

        //获取单个数据
        Object username=user.get("username");
        System.out.println("username："+username);

        //获取所有数据
        Map<Object,Object> userMap=user.entries();


        for (Map.Entry<Object,Object> map:userMap.entrySet()){
            System.out.println(map.getKey()+"："+map.getValue()) ;
        }

    }
}
