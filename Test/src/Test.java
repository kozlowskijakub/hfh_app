import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 11/9/13
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
//        HashMap<String, String> loans = new HashMap<String, String>();
//        loans.put("home loan", "citibank");
//        loans.put("personal loan", "Wells Frago");

        TreeSet<MyWay> ways = new TreeSet<MyWay>();

        MyWay mw = new MyWay();
        mw.id = 31;
        mw.nodes.add(32);
        mw.nodes.add(33);
        mw.nodes.add(34);
        MyWay mw2 = new MyWay();
        mw2.id = 41;
        mw2.nodes.add(42);
        mw2.nodes.add(43);
        mw2.nodes.add(44);
        MyWay mw3 = new MyWay();
        mw3.id = 52;
        mw3.nodes.add(42);
        mw3.nodes.add(43);
        mw3.nodes.add(44);
        MyWay mw4 = new MyWay();
        mw4.id = 62;
        mw4.nodes.add(62);
        mw4.nodes.add(63);
        mw4.nodes.add(64);


        ways.add(mw4);
        ways.add(mw);
        ways.add(mw3);
        ways.add(mw2);


//        for (MyWay w : ways) {
//            System.out.println(w.toString());
//        }

        HashMap<String, Integer> hm = new HashMap();
        HashSet<String> hs = new HashSet();


        for (Integer i = 0; i < 10; i++) {
            hm.put(Character.toString((char) i.intValue()), i);
            hs.add(Character.toString((char) i.intValue()));
        }
        hs = (HashSet<String>) hm.keySet();
        for (String i : hs) {
                  System.out.println(i);
        }


    }
}

class MyWay implements Comparable {
    int id;
    ArrayList<Integer> nodes = new ArrayList<Integer>();

    @Override
    public String toString() {
        String val = "";

        for (Integer i : this.nodes) {
            val += " val:" + i.toString();
        }
        return "id:" + this.id + val;
    }

    @Override
    public int compareTo(Object o) {
        MyWay way = (MyWay) o;
        return ((Integer) this.id).compareTo(way.id);
    }
}