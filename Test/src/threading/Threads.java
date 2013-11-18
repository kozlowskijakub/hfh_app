package threading;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 11/11/13
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Threads {
    NotThreadSafe sharedInstance = new NotThreadSafe();

    public void call() {
        new Thread(new MyRunnable(sharedInstance)).start();
        new Thread(new MyRunnable(sharedInstance)).start();
    }
}


class MyRunnable implements Runnable {
    NotThreadSafe instance = null;

    public MyRunnable(NotThreadSafe instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        this.instance = instance;
    }
}

class NotThreadSafe {
    StringBuilder builder = new StringBuilder();

    public void add(String text) {
        this.builder.append(text);
    }
}