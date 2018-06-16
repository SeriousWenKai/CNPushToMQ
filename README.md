# CNPushToMQ
将收到的推送插入消息队列

1.说明：给四台机器使用，两台MQ，两台用于插入MQ的机器，通过Spring的Profile进行区分使用。
2.说明：配置的Profile有如下两个'FromNginx'和'ToMongo'