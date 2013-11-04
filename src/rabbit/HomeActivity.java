package rabbit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.jk.hfh_app.R;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 11/4/13
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class HomeActivity extends Activity {
    private MessageConsumer mConsumer;
    private TextView mOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.main);

        // The output TextView we'll use to display messages
        mOutput = (TextView) findViewById(R.id.output);

        //consumer connection
//        192.168.2.103 local
        mConsumer = new MessageConsumer("37.128.118.216", "logs", "fanout");


        // brooker connection
        mConsumer.connectToRabbitMQ();

        //register for messages
        mConsumer.setmOnReceiveMessageHandler(new MessageConsumer.OnReceiveMessageHandler() {
            @Override
            public void onReceiveMessage(byte[] message) {
                String text = "";
                try {
                    text = new String(message, "UTF8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("UnsupportedEncodingException", e.toString());
                }
                mOutput.append("\n" + text);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        mConsumer.connectToRabbitMQ();
    }

    @Override
    protected void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
        try {
            mConsumer.Dispose();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
