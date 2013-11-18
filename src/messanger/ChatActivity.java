package messanger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.jk.hfh_app.R;
import messanger.database.ChatDatabaseHandler;
import messanger.rabbit.RabbitSendMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 11/4/13
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChatActivity extends Activity {

    private LinearLayout ll_message_container;
    private EditText messageView;
    private List<RabbitMessage> inMessages;
    private List<RabbitMessage> outMessages;
    private ChatDatabaseHandler database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("basics", "bascic info");
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.chat);
        this.ll_message_container = (LinearLayout) findViewById(R.id.ll_message_container);
        this.messageView = (EditText) findViewById(R.id.text_send);
        this.database = ChatDatabaseHandler.getInstance(this);


        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));

//        String dbName = this.database.getDatabaseName();
//        File f = this.getDatabasePath(dbName);

//        this.database.createTables();
        this.inMessages = database.getInMsgs();
        this.outMessages = database.getOutMsgs();

        displayMessages();

    }

    public void sendMessage(View view) {
        RabbitMessage newMessage = new RabbitMessage();
        newMessage.time = System.currentTimeMillis();

        newMessage.content = messageView.getText().toString() + System.nanoTime();
        View relativeLayoutView = null;
        TextView tv_context = null;
        TextView tv_time = null;

        try {
            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            relativeLayoutView = inflater.inflate(R.layout.msg_out, null);
        } catch (Exception e) {
            Log.e("ExceptionLayoutInflater", e.toString());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            tv_context = (TextView) relativeLayoutView.findViewById(R.id.tv_content);
            tv_time = (TextView) relativeLayoutView.findViewById(R.id.tv_time);
            // tworze now
            this.ll_message_container.addView(relativeLayoutView);
            this.database.createOutMsg(newMessage);
        } catch (Exception e) {
            Log.e("Exceptiontv_time", e.toString());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        tv_context.setText(newMessage.content);
        tv_time.setText(formatTime(newMessage.time));


        new RabbitSendMessage().execute(this.outMessages);
//        RabbitSendMessage rsb = new RabbitSendMessage();


    }

    private void displayMessages() {
        LayoutInflater inflater;
        View relativeLayoutView;
        TextView tv_context;
        TextView tv_time;
        List<RabbitMessage> outMsgs = this.database.getOutMsgs();

        for (RabbitMessage msg : outMsgs) {
            inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            relativeLayoutView = inflater.inflate(R.layout.msg_out, null);

            tv_context = (TextView) relativeLayoutView.findViewById(R.id.tv_content);
            tv_time = (TextView) relativeLayoutView.findViewById(R.id.tv_time);
            tv_context.setText(msg.content);
            tv_time.setText(formatTime(msg.time));

            ll_message_container.addView(relativeLayoutView);
        }
    }

    private String formatTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("+03"));
        return simpleDateFormat.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RabbitSendMessage.canceled = true;
    }

}
