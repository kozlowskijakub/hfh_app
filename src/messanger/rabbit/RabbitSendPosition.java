package messanger.rabbit;

import android.os.AsyncTask;
import android.util.Log;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import messanger.RabbitMessage;
import org.json.JSONException;
import org.json.JSONObject;
import tracer.logicObjects.POI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 11/14/13
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class RabbitSendPosition extends AsyncTask<POI, Void, Void> {
    private static final String TASK_QUEUE_NAME = "currentClientLocation";
    private ConnectionFactory connectionFactory = null;
    private Channel channel = null;
    private Connection connection = null;

    private static String address = "176.31.86.191";
    private static int port = 38888;
//    private static String address = "192.168.0.19";
//    private static int port = 5672;

    //    private static String address = "192.168.0.19";
    public static boolean canceled = false;

//    public static void main(String[] args) {
//        Send("test message");
//    }
//
//    public static void Send(String message) {
//                 this.
//    }
//public static ArrayList<POI> poiList = new ArrayList<POI>();

    @Override
    protected Void doInBackground(POI... params) {
        Log.e("beforedebug: ", "something before");
//        Debug.waitForDebugger();
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost(address);
        this.connectionFactory.setPort(port);

        POI poi = ((POI) params[0]);

        // create JSON message
        String jsonMessage = poi.getLatitude() + "," + poi.getLongitude() + "," + poi.getTime();
//        JSONObject jsonObj = new JSONObject();
//        try {
//            jsonObj.put("latitude",poi.getLatitude());
//            jsonObj.put("longitude",poi.getLongitude());
//            jsonObj.put("timestamp",poi.getTime());
//        } catch (JSONException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

        if (!canceled) {
            try {
                this.connection = this.connectionFactory.newConnection();
                this.channel = this.connection.createChannel();
                this.channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

                this.channel.basicPublish("",
                        TASK_QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        (jsonMessage).getBytes());

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

//        for (POI message : poi) {
//            if (!canceled) {
//                try {
//                    this.connection = this.connectionFactory.newConnection();
//                    this.channel = this.connection.createChannel();
//                    this.channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
//
//                    this.channel.basicPublish("",
//                            TASK_QUEUE_NAME,
//                            MessageProperties.PERSISTENT_TEXT_PLAIN,
//                            (message.content + ", time:" + message.time).getBytes());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("myIOException", e.toString());
//                } finally {
//                    try {
//                        this.channel.close();
//                        this.connection.close();
//                    } catch (IOException e) {
//                        Log.e("connectionCloseException", e.toString());
//                    }
//                }
//            }
//        }

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