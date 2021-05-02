package com.gyy.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_HelloWorld {
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
        channel.queueDeclare("hello_world",true,false,false,null);

        //6.接收消息
        Consumer consumer = new DefaultConsumer(channel){
            /*
               回调方法，当收到消息后，会自动执行该方法

             * consumerTag 消息者标签，在channel.basicConsume时候可以指定
             * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * properties 属性信息
             * body 消息

            */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               /* System.out.println("consumerTag"+" "+consumerTag);
                System.out.println("envelope"+" "+envelope);
                System.out.println("envelope"+" "+envelope.getRoutingKey());
                System.out.println("properties"+" "+properties);
                System.out.println("body"+" "+new String(body));*/
                //路由key
                System.out.println("路由key为：" + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为：" + envelope.getExchange());
                //消息id
                System.out.println("消息id为：" + envelope.getDeliveryTag());
                //收到的消息
                System.out.println("接收到的消息为：" + new String(body, "utf-8"));
            }
        };

         /*
        basicConsume(String queue, boolean autoAck, Consumer callback)
        参数：
            1. queue：队列名称
            2. autoAck：是否自动确认
            3. callback：回调对象

         */
        channel.basicConsume("hello_world",true,consumer);


        //7.不要释放资源,应该监听一些消息
        //channel.close();
        //connection.close();
    }
}
