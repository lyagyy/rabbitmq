package com.gyy.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_Work_Queues2 {
    public static void main(String[] args) throws IOException, TimeoutException {


        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置参数
        factory.setHost("127.0.0.1");//ip 默认值localhost
        factory.setPort(5672);//端口
        factory.setVirtualHost("/");//虚拟机 默认"/"
        factory.setUsername("guest");//用户名
        factory.setPassword("guest");//密码
        //3.创建连接connection
        Connection connection = factory.newConnection();
        //4.创建channel
        Channel channel = connection.createChannel();
        //5.创建队列Queue
        /**
         queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
         参数：
         1. queue：队列名称
         2. durable:是否持久化，当mq重启之后，还在
         3. exclusive：
         * 是否独占。只能有一个消费者监听这队列
         * 当Connection关闭时，是否删除队列
         4. autoDelete:是否自动删除。当没有Consumer时，自动删除掉
         5. arguments：参数。
         */
        //如果没有一个名字叫hello_world的队列，则会创建该队列，如果有则不会创建
        channel.queueDeclare("work_queues",true,false,false,null);

        //6.接收消息
        Consumer consumer = new DefaultConsumer(channel){
            /*
               回调方法，当收到消息后，会自动执行该方法

               1. consumerTag：标识
               2. envelope：获取一些信息，交换机，路由key...
               3. properties:配置信息
               4. body：数据

            */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                /*System.out.println("consumerTag"+consumerTag);
                System.out.println("envelope"+envelope);
                System.out.println("envelope"+envelope.getRoutingKey());
                System.out.println("properties"+properties);*/
                System.out.println("body"+new String(body));
            }
        };

         /*
        basicConsume(String queue, boolean autoAck, Consumer callback)
        参数：
            1. queue：队列名称
            2. autoAck：是否自动确认
            3. callback：回调对象

         */
        channel.basicConsume("work_queues",true,consumer);


        //7.不要释放资源

    }
}
