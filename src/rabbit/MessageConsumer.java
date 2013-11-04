package rabbit;

import android.os.Handler;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 10/15/13
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageConsumer extends IConnectToRabbitMQ {


    //The Queue name for this consumer
    private String mQueue;
    private QueueingConsumer MySubscription;

    // kast message to poast back
    private byte[] mLastMessage;

    private Handler mMessageHandler = new Handler();
    private Handler mConsumerHandler = new Handler();

    public MessageConsumer(String server, String exchange, String exchangeType) {
        super(server, exchange, exchangeType);
    }

    // inteface to be implemented by an object that is interested
    public interface OnReceiveMessageHandler {
        public void onReceiveMessage(byte[] message);
    }

    // A reference to the listener, we can only have one at a time
    private OnReceiveMessageHandler mOnReceiveMessageHandler;

    public void setmOnReceiveMessageHandler(OnReceiveMessageHandler handler) {
        mOnReceiveMessageHandler = handler;
    }

    // Create runnable for postiong back to main thread

    final Runnable mReturnMessage = new Runnable() {

        @Override
        public void run() {
            mOnReceiveMessageHandler.onReceiveMessage(mLastMessage);
        }
    };
    final Runnable mConsumeRunner = new Runnable() {
        @Override
        public void run() {
            Consume();
        }
    };

    @Override
    public boolean connectToRabbitMQ() {
        if (super.connectToRabbitMQ()) {
            try {
                mQueue = mModel.queueDeclare().getQueue();
                MySubscription = new QueueingConsumer(mModel);
                mModel.basicConsume(mQueue, false, MySubscription);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            if (MyExchangeType == "fanout")
                AddBinding(""); // fanout has default binding
            Running = true;
            mConsumerHandler.post((Runnable) mConsumerHandler);
            return true;
        }
        return false;
    }

    public void AddBinding(String routingKey) {
        try {
            mModel.queueBind(mQueue, mExchange, routingKey);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void RemoveBinding(String routingKey) {
        try {
            mModel.queueUnbind(mQueue, mExchange, routingKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Consume() {
        Thread thread = new Thread() {

            @Override
            public void run() {
                while (Running) {
                    QueueingConsumer.Delivery delivery;
                }
            }
        };
        thread.start();
    }

    public void Dispose() throws IOException {
        Running = false;

        if (mConnection != null) {
            mConnection.close();
        }
        if (mModel != null) {
            mModel.abort();
        }
    }

}
