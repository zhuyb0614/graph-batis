# graph-batis
GraphQL&amp;Mybatis的绝妙搭配
* [GraphQL 中文文档](https://graphql.cn/)

## 性能优势
* 通过GraphQL前端可以向后台发送自己想要的数据结构,知道了前端具体要什么数据,我们就可以根据前端需要的结构查询最少的数据,减轻DB压力.
## 开发优势
* 开发时可以将所有(夸张比喻)关联关系的表统统写一个SQL里,graph-batis-core中的*CleanSqlInterceptor*,将根据前端发送的数据结构,将不需要的查询字和表剔出.一次编写,处处使用!
## 效果演示
以下样例都只通过org.zhuyb.graphbatis.mapper.RoomDao#findAll方法定义的如下SQL,自动精简后查询.无需多次编写关联关系

```sql
SELECT
   st.student_name,
   st.student_id,
   su.subject_id,
   su.subject_name,
   t.teacher_id,
   t.teacher_name,
   r.room_id,
   r.room_name
FROM
   t_room AS r
JOIN t_teacher_room AS tr ON tr.room_id = r.room_id
JOIN t_student AS st ON st.room_id = r.room_id
JOIN t_teacher AS t ON t.teacher_id = tr.teacher_id
JOIN t_subject AS su ON su.subject_id = t.subject_id
```

进入graph-batis-example目录执行 mvn spring-boot:run启动服务后,打开下方链接打开graphiql
http://localhost:8080/graphiql?query=%7B%0A%20%20findRooms%20%7B%0A%20%20%20%20roomId%0A%20%20%20%20roomName%0A%20%20%20%20teachers%7B%0A%20%20%20%20%20%20teacherName%0A%20%20%20%20%7D%0A%20%20%7D%0A%7D%0A




![](./img/graphiQLV2.png)

实际执行SQL

```sql
SELECT
   t.teacher_id,
   t.teacher_name,
   r.room_id,
   r.room_name
FROM
   t_room AS r
JOIN t_teacher_room AS tr ON tr.room_id = r.room_id
JOIN t_teacher AS t ON t.teacher_id = tr.teacher_id
```



http://localhost:8080/graphiql?query=%7B%0A%20%20findRooms%20%7B%0A%20%20%20%20roomId%0A%20%20%20%20roomName%0A%20%20%20%20students%7B%0A%20%20%20%20%20%20studentId%0A%20%20%20%20%20%20studentName%0A%20%20%20%20%7D%0A%20%20%7D%0A%7D%0A

![](./img/graphiQL2V2.png)

实际执行SQL

```sql
SELECT
   st.student_name,
   st.student_id,
   r.room_id,
   r.room_name
FROM
   t_room AS r
JOIN t_student AS st ON st.room_id = r.room_id
```



## TODO

- [x]  代码生成器提供
- [x]  去除VO,使其符合GraphQL规范
- [x]  查询缓存
- [x]  向上抽取抽象,使其通用化不局限GraphQL
- [x]  接入JOOQ
- [ ]  提供Client
- [ ]  接入Hibernate
- [ ]  JDBC方式SQL改写
- [ ]  中间表voyager
- [ ]  分页
- [ ]  复杂条件查询
