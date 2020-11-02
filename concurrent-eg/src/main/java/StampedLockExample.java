import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock 支持三种模式，分别是：写锁、悲观读锁和乐观读
 *
 * ReadWriteLock 支持多个线程同时读，但是当多个线程同时读的时候，所有的写操作会被阻塞；
 * 而 StampedLock 提供的乐观读，是允许一个线程获取写锁的，也就是说不是所有的写操作都被阻塞
 *
 * 注意点：1.StampedLock 不支持重入 2.StampedLock 的悲观读锁、写锁都不支持条件变量
 *       3.如果线程阻塞在 StampedLock 的 readLock() 或者 writeLock() 上时，此时调用该阻塞线程的 interrupt() 方法，会导致 CPU 飙升
 * @author wengxiaolu
 */
public class StampedLockExample {
    private int x, y;
    final StampedLock sl = new StampedLock();
    // 计算到原点的距离
    double distanceFromOrigin() {
        // 乐观读
        long stamp = sl.tryOptimisticRead();
        // 读入局部变量，
        // 读的过程数据可能被修改
        int curX = x, curY = y;
        // 判断执行读操作期间，
        // 是否存在写操作，如果存在，
        // 则 sl.validate 返回 false
        if (!sl.validate(stamp)){
            // 升级为悲观读锁
            stamp = sl.readLock();
            try {
                curX = x;
                curY = y;
            } finally {
                // 释放悲观读锁
                sl.unlockRead(stamp);
            }
        }
        return Math.sqrt(curX * curX + curY * curY);
    }
}
