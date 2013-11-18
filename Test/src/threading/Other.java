package threading;



/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 11/11/13
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class Other {

    public void onClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

}
