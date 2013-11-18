package messanger.rabbit;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 11/5/13
 * Time: 10:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class RabbitReceiveMessage extends AsyncTask<String, Context, Void> {

    // should be changed to fromServerToClient messages
    private static final String TASK_QUEUE_NAME = "fromClientToServer";
    private static ConnectionFactory factory;
    private static Connection connection;
    private static Channel channel;
    private static QueueingConsumer consumer;

    private static String address = "37.128.118.216";

    @Override
    protected Void doInBackground(String... params) {

        try {
            factory = new ConnectionFactory();
            factory.setHost(address);
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            channel.basicQos(1);
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(TASK_QUEUE_NAME, false, consumer);

            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());

                // delivery message
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }

        } catch (IOException e) {
            Log.e("IOExceptiondoInBackground", e.toString());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            Log.e("InterruptedExceptiondoInBackground", e.toString());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

//    @Override
//    protected void onProgressUpdate(Context... values) {
//        super.onProgressUpdate(values);    //To change body of overridden methods use File | Settings | File Templates.
//
//    }
}
