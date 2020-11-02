import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 管程 多条件变量 实现阻塞队列
 * @author wengxiaolu
 */
public class ReentrantLockExample {
    int maxSize =  10;
    List<Integer> queue = new ArrayList<Integer>();
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    public void enq(int i){
        lock.lock();
        try{
                while(queue.size()>=maxSize){
                    notFull.await();
                }
                //入队操作
                System.out.println("i = "+i+" 入队了..");
                queue.add(i);
                notEmpty.signalAll();
        } catch (InterruptedException e) {
            System.out.println("i = "+i+" 入队失败了..");
            System.out.println("线程中断中...不具备入队能力！");
        } finally{
            lock.unlock();
        }

    }

    public void deq(){
        lock.lock();
        try{
            while(queue.size()>0&&queue.size()<maxSize){
                this.notEmpty.await();
            }
            //出队操作
            System.out.println("i = "+queue.get(0)+" 出队了..");
            try{
                queue.remove(0);
            }catch(Exception e){

            }
            notFull.signalAll();
        } catch (InterruptedException e) {
            System.out.println("线程中断中...不具备出队能力！");
        } finally{
            lock.unlock();
        }
    }
}


