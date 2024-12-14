package com.trpo.lab;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.trpo.lab.AdjustModel.Adjustable;
import com.trpo.lab.AdjustModel.InertionModel;
import com.trpo.lab.PID.PID;

public class Main {

    static Adjustable model = new InertionModel(1f);
    static PID pid = new PID(model, 100, 10, 0, 1.2f, 0.01f, 0.3f);

    public static void main(String[] args) throws Exception
    {
        new Thread(pid).start();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.queueDeclare("PID", false, false, false, null);

        channel.basicConsume("PID", (consumerTag, message) -> {
            Float Target = Float.parseFloat(new String(message.getBody()));
            pid.setTarget(Target);
        }, consumerTag -> {});
    }
}