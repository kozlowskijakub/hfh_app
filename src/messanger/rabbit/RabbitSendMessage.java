package messanger.rabbit;

import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import messanger.RabbitMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RabbitSendMessage extends AsyncTask<List<RabbitMessage>, Void, Void> {
    private static final String TASK_QUEUE_NAME = "fromClientToServer";
    private ConnectionFactory connectionFactory = null;
    private Channel channel = null;
    private Connection connection = null;
//        private static String address = "176.31.86.191";
//    private static int port =  38888;
    private static String address = "192.168.0.19";
    private static int port = 5672;


    //176.31.86.191
    //        private static String address = "371.1283.118.231";
//    private String output = "";
    // when application closed it stop work
    public static boolean canceled = false;

//    public static void main(String[] args) {
//        Send("test message");
//    }
//
//    public static void Send(String message) {
//                 this.
//    }

    @Override
    protected Void doInBackground(List<RabbitMessage>... params) {
        Log.e("beforedebug: ", "something before");
//        Debug.waitForDebugger();
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost(address);
        this.connectionFactory.setPort(port);

        List<RabbitMessage> messages = ((ArrayList) params[0]);
        Log.e("size: ", String.valueOf(messages.size()));
        for (RabbitMessage message : messages) {
            if (!canceled) {
                try {
                    this.connection = this.connectionFactory.newConnection();
                    this.channel = this.connection.createChannel();
                    this.channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

                    this.channel.basicPublish("",
                            TASK_QUEUE_NAME,
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            (message.content + ", time:" + message.time).getBytes());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("myIOException", e.toString());
                } finally {
                    try {
                        this.channel.close();
                        this.connection.close();
                    } catch (IOException e) {
                        Log.e("connectionCloseException", e.toString());
                    }
                }
            }
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

//    @Override
//    protected void onPostExecute(Void context) {
//        super.onPostExecute(context);
//        Log.i("onPostExecute", "onPostExecute performed with output:" + this.output);
//    }

}
