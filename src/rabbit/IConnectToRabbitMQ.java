package rabbit;

import android.util.Log;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 10/15/13
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class IConnectToRabbitMQ {

    public String mServer;
    public String mExchange;

    protected Channel mModel = null;
    protected Connection mConnection;

    protected boolean Running;

    protected String MyExchangeType;

    public IConnectToRabbitMQ(String server, String exchange, String exchangeType) {
        mServer = server;
        mExchange = exchange;
        MyExchangeType = exchangeType;
    }

    public void Dispose() throws IOException {
        Running = false;

        try {

            if (mConnection != null) {
                mConnection.close();
            }
            if (mModel != null) {
                mModel.abort();
            }
        } catch (IOException e) {
            Log.e("myIOException", e.toString());
        }
    }

    public boolean connectToRabbitMQ() {
        if (mModel != null && mModel.isOpen())
            return true;

        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(mServer);
            mConnection = connectionFactory.newConnection();
            mModel = mConnection.createChannel();
            mModel.exchangeDeclare(mExchange, MyExchangeType, true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
