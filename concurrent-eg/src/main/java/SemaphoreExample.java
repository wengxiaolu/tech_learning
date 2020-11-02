import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * 信号量模式
 * init()：设置计数器的初始值。
 * down()：计数器的值减 1；如果此时计数器的值小于 0，则当前线程将被阻塞，否则当
 * 前线程可以继续执行。
 * up()：计数器的值加 1；如果此时计数器的值小于或者等于 0，则唤醒等待队列中的一个
 * 线程，并将其从等待队列中移除
 *  三个操作都是原子性操作
 *
 * down() 和 up() 对应的则是 acquire() 和 release()
 * @author wengxiaolu
 *
 * 限流器
 */
public class SemaphoreExample<T,R> {
    final List<T> pool;
    final Semaphore sem;

    public SemaphoreExample(int size,T t) {
        pool = new Vector<T>(){};
        for(int i=0;i<size;i++){
            pool.add(t);
        }
        sem = new Semaphore(size);
    }
    //利用对象池的对象，调用 func
    R exec(Function<T,R> func) throws InterruptedException {
        T t = null;
        sem.acquire();
        try {
            t = pool.remove(0);
            return func.apply(t);
        }finally {
            pool.add(t);
            sem.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SemaphoreExample<Integer, String> pool = new SemaphoreExample<Integer, String>(2, 4);
        pool.exec(t->{
            System.out.println(t);
            return t.toString();
        });
    }
}
