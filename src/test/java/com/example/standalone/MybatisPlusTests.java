package com.example.standalone;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.example.standalone.mapper.UserMapper;
import com.example.standalone.model.entity.User;
import com.example.standalone.service.UserService;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusTests {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Test
    public void test(){
        User user = new User();
        user.setName("hello9").setPhone("1231231").setAboutme("2222");
        user.setCreateTime(LocalDateTime.now());
        user.setType("1");
        user.setEnable(true);
        user.setEmail("spring_boot11@163.com").setAvatar("avatar").setAgencyId(1);
        user.setPasswd("75fb23b165249cedeb60544c4095ec99");
        user.setRemark("beizhu");
        int rows = userMapper.insert(user);
        System.out.println(rows);
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    public void selectById(){
        User user = userMapper.selectById(15);
        System.out.println(user);
    }
    @Test
    public void selectByIds(){
        List<User> users = userMapper.selectBatchIds(Arrays.asList(15, 14));
        users.forEach(System.out::println);
    }
    @Test
    public void selectByMap(){
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("enable", true);
        map.put("agency_id", 1); //key是数据库中的列名，不是实体里的属性名
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrapper(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
//        1. like '%刘%' and create_time < now
        queryWrapper.like("name", "刘").lt("create_time", LocalDateTime.now()); //也是列名
//        2. create_time < str_to_date(xxx) and create_time > str_to_date(now) and name is not null
//        queryWrapper.between("create_time", LocalDateTime.MIN, LocalDateTime.now()).isNotNull("name");
//        3. name like '刘%' or age >= 25 order by age desc, create_time asc
//        queryWrapper.likeRight("name", "刘").or().ge("age", 25).orderByDesc("age").orderByAsc("create_time");
//        4. date_format(create_time, '%Y-%m-%d') and manager_id in (select id from user where name like '王%')
//        queryWrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", "2019-01-01")
//                .inSql("manager_id", "select id from user where name like '王%'");
//        5. name like '%刘' and (age < 30 or enabled is not null)
//        queryWrapper.likeLeft("name", "刘").and(wq-> wq.lt("age", 30).or().isNotNull("enabled"));
//        6. name like '王%' or (age > 20 and age < 30 and email is not null)
//        queryWrapper.likeRight("name", "王").or(wq -> wq.between("age", 20, 30).isNotNull("email"));
//        7. (age < 40 or email is not null) and name like '%王'
//        queryWrapper.nested(qw->qw.lt("age", 40).or().isNotNull("email")).likeLeft("name", "王");
//        8. age in (20,30,31,32)
//        queryWrapper.in("age", Arrays.asList(20,30,31,32));
//        9. limt 1 只能写在最后，有sql注入风险
//        queryWrapper.last("limit 1");
//        10. 只要其中的几列 这里是列名不是属性名
//        queryWrapper.select("id", "name");
//        11. 不要其中几列， 这里也是列名
//        queryWrapper.select(User.class, info -> !info.getColumn().equals("create_time") && !info.getColumn().equals("manager_id"));
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByCondition(){
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        String name = "";
        //所有条件构造方法的第一个参数都是condition，true表示最终会拼到sql里，false不拼
        queryWrapper.like(!"".equals(name), "name", "刘");
        userMapper.selectList(queryWrapper);
    }

    @Test
    public void selectByWrapperEntity(){
        //通过实体来查
        User conditionUser = new User();
        conditionUser.setName("hello7").setAgencyId(1);
        QueryWrapper<User> queryWrapper = Wrappers.<User>query(conditionUser);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
        //不想等值的话，在实体对应字段上加@TableField(condition=SqlCondition.LIKE)
    }

    @Test
    public void selectByAllEq(){
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("name", "刘强东");
        map.put("age", "30");
        map.put("email", null); //等价于email is null
//        queryWrapper.allEq(map);
//        queryWrapper.allEq(map, false); //map中值为null的会忽略掉
        queryWrapper.allEq((k,v)->!k.equals("name"), map, false); //加入前过滤一波map, 第三个参数是map中值为null的会忽略掉
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectMaps(){
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.select("id","name").like("name", "刘");
        List<Map<String, Object>> resultMap = userMapper.selectMaps(queryWrapper);
        resultMap.forEach(System.out::println);

        //select avg(age) avg_age, min(age) min_age, max(age) max_age from user
        //group by manager_id having sum(age) < 500
        QueryWrapper<User> queryWrapper2 = Wrappers.<User>query();
        queryWrapper2.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age")
                .groupBy("manager_id").having("sum(age)<{0}", 500);
        List<Map<String, Object>> resultMap2 = userMapper.selectMaps(queryWrapper2);
        resultMap2.forEach(System.out::println);
    }

    @Test
    public void selectObjs(){
        //只返回一列的时候可以使用
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        List<Object> objects = userMapper.selectObjs(queryWrapper);
        objects.forEach(System.out::println);
    }

    @Test
    public void selectCount(){
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like("name", "刘");
        Integer count = userMapper.selectCount(queryWrapper);
        System.out.println(count);
    }

    @Test
    public void selectOne(){
        //查询的结果必须只有一条或者null
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like("name", "刘");
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

    @Test
    public void selectLambda(){
//        只有列名的取法有变化，使用lambda可以防止误写
//        LambdaQueryWrapper<Object> lambda = new QueryWrapper<>().lambda();
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        lambdaQueryWrapper.like(User::getName, "刘"); //where name like '%刘%'
        lambdaQueryWrapper.gt(User::getEnable, 0);
        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        users.forEach(System.out::println);

        //3.0.7新增
        LambdaQueryChainWrapper<User> lambdaQueryChainWrapper = new LambdaQueryChainWrapper<User>(userMapper);
        List<User> users2 = lambdaQueryChainWrapper.like(User::getName, "刘").ge(User::getAgencyId, 0).list();
        users2.forEach(System.out::println);
    }

    @Test
    public void selectMy(){ //自定义sql接收wrapper
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like("name", "刘");
        List<User> users = userMapper.selectAll(queryWrapper);
        users.forEach(System.out::println);

        List<User> users2 = userMapper.selectAll2(queryWrapper);
        users2.forEach(System.out::println);
    }

    @Test
    public void selectPage(){
        //两种常规的单表分页查询
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like("name", "hello");
        Page<User> page = new Page<>(1, 3, false); //当前页，每页多少条，是否查总记录数(如果不查得不到当前是第几页和总记录数
//        IPage<User> userIPage = userMapper.selectPage(page, queryWrapper);
        IPage<Map<String, Object>> userIPage = userMapper.selectMapsPage(page, queryWrapper);
        System.out.println(userIPage.getPages());
        System.out.println(userIPage.getTotal());
//        List<User> records = userIPage.getRecords();
        List<Map<String, Object>> records = userIPage.getRecords();
        records.forEach(System.out::println);
    }

    @Test
    public void selectMyPage(){
        //多表分页的写法
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like("name", "hello");
        Page<User> page = new Page<>(1, 3);
        IPage<User> userIPage = userMapper.selectUserPage(page, queryWrapper);
        System.out.println(userIPage.getPages());
        System.out.println(userIPage.getTotal());
        userIPage.getRecords().forEach(System.out::println);
    }

    @Test
    public void updateById(){
        User user = new User();
        user.setId(15L);
        user.setPhone("158111111");
        int rows = userMapper.updateById(user);
        System.out.println(rows);
    }

    @Test
    public void updateByWrapper(){
        UpdateWrapper<User> updateWrapper = Wrappers.<User>update();
        //UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", '刘');
        User user = new User();
        user.setPhone("12312321");
        user.setEmail(null); //默认实体中不为null的值会被拼到set中
        int rows = userMapper.update(user, updateWrapper);
        System.out.println(rows);

        //只更新几个字段
        UpdateWrapper<User> updateWrapper2 = Wrappers.<User>update();
        //UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper2.eq("name", '刘').set("phone","12345554");
        int rows2 = userMapper.update(null, updateWrapper);
        System.out.println(rows2);

        //使用lambdaWrapper
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = Wrappers.<User>lambdaUpdate();
        lambdaUpdateWrapper.like(User::getName, "刘").set(User::getAboutme, "12312312axsdas");
        userMapper.update(null, lambdaUpdateWrapper);

        LambdaUpdateChainWrapper<User> lambdaUpdateChainWrapper = new LambdaUpdateChainWrapper<User>(userMapper);
        boolean update = lambdaUpdateChainWrapper.like(User::getName, "刘").set(User::getAboutme, "12312312axsdas").update();
        //成功为true, 失败false
        System.out.println(update);
    }

    @Test
    public void deleteById(){
        int rows = userMapper.deleteById(1140505546428837890L);
        System.out.println(rows);
    }

    @Test
    public void deleteByMap(){
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("enable", false);
        map.put("agency_id", 0); //key是数据库中的列名，不是实体里的属性名
        int rows = userMapper.deleteByMap(map);
        System.out.println(rows);
    }

    @Test
    public void deleteByBatchIds(){
        int rows = userMapper.deleteBatchIds(Arrays.asList(1L, 2L, 3L, 4L));
        System.out.println(rows);
    }

    @Test
    public void deleteByWrapper(){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getName, "hello7");
        int rows = userMapper.delete(lambdaQueryWrapper);
        System.out.println(rows);
    }

    @Test
    public void testActiveRecord(){
        //AR模式，当实体类extends Model<T>之后，就开启了AR模式
        //插入
        User user = new User();
        user.setPhone("12312321");
        user.setName("AR模式").setEmail("12312788@qq.com");
        user.setEnable(true).setAgencyId(1).setType("1");
        user.setCreateTime(LocalDateTime.now());
        boolean result = user.insert();
        System.out.println(result);
        System.out.println(user.getId());
        //查询
        User user2 = new User();
        user2.setId(15L);
        User selectUser = user2.selectById();
        System.out.println(selectUser == user2); //false
        //修改
        User user3 = new User();
        user3.setId(user.getId());
        user3.setAboutme("AR修改");
//        boolean updated = user3.updateById();
        boolean updated = user3.insertOrUpdate(); //会先查id存不存在，存在是修改，不存在是插入
        System.out.println(updated);
        //删除
        User user4 = new User();
        user4.setId(user.getId());
        boolean deleted = user4.deleteById();
        System.out.println(deleted);
    }

    @Test
    public void testService(){
        User one = userService.getOne(Wrappers.<User>query().like("name", "hello"), false); //传false 超过1条不报错
//        userService.query().ge(User::getId, 15L).one();
        User one1 = userService.lambdaQuery().ge(User::getId, 15L).one();
        userService.lambdaUpdate().ge(User::getId, 15L).set(User::getEnable, false).update();
    }
}
