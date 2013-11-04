package rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 11/4/13
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue2";

    public static void main(String... args) {
        String[] tab = {"some.. ", "value ..", "from ..", "string ..", "table ..!"};
        args = tab;

        for (int i = 0; i < 3000; i++) {
            ConnectionFactory connectionFactory = null;
            try {
                connectionFactory = new ConnectionFactory();
                connectionFactory.setHost("37.128.118.216");
//                connectionFactory.setHost("http://37.128.118.216");
//                connectionFactory.setHost("localhost");

            } catch (Exception e) {
//                Log.e("connectionException", e.toString());
                e.printStackTrace();
            }

            try {
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
//                String message = getMessage(args);
                String message = "default message send from remote host";
                channel.basicPublish("",
                        TASK_QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes());

                System.out.println(" [x] sent '" + message + "'");
                channel.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
}
