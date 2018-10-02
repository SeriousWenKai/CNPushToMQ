# CNPushToMQ
将收到的推送插入消息队列

1.说明：给四台机器使用，两台MQ，两台用于插入MQ的机器，通过Spring的Profile进行区分使用。
2.说明：配置的Profile有如下两个'FromNginx'和'ToMongo'

## 2018-09-12
1. 改JPA为常规Hibernate
2. 完成日志正确存入队列并读取插入Mongo
3. TODO:使用缓存来减少对数据库的访问

## 2018-10-02
1. 设备离线逻辑：
+ 设备正常传输日志-》日志设置一小时expireTime-》每当有新的同设备日志刷新其时间
+ 注：由于已经订阅了MQ，再订阅Redis时出现MessageListenerAdapter重复小问题，解决方案：把Redis订阅放到了EDoctor的后台去，但是保留API调用在