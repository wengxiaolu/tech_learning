import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁三个原则
 * 1. 允许多个线程同时读共享变量；
 * 2. 只允许一个线程写共享变量；
 * 3. 如果一个写线程正在执行写操作，此时禁止读线程读共享变量。
 *
 * 只有写锁支持条件变量，读锁是不支持条件变量
 * @author wengxiaolu
 * 注意点：只支持降级，不支持锁的升级，即read->write是不支持的
 * 实现完备的缓存
 */
public class ReadWriteLockExample {
    //缓存器
    private Map<String, Object> map = new HashMap<String, Object>();
    private ReadWriteLock rwl = new ReentrantReadWriteLock();
    public static void main(String[] args) {

    }
    public Object get(String id){
        Object value = null;
        //首先开启读锁，从缓存中去取
        rwl.readLock().lock();
        try{
            value = map.get(id);
            //如果缓存中没有释放读锁，上写锁
            if(value == null){
                rwl.readLock().unlock();
                rwl.writeLock().lock();
                try{
                    if(value == null){
                        //此时可以去数据库中查找，这里简单的模拟一下
                        value = "aaa";
                    }
                }finally{
                    rwl.writeLock().unlock(); //释放写锁
                }
                rwl.readLock().lock(); //然后再上读锁
            }
        }finally{
            rwl.readLock().unlock(); //最后释放读锁
        }
        return value;
    }

    //另一种实现
//    final Map<K, V> m =
//            new HashMap<>();
//    final ReadWriteLock rwl =
//            new ReentrantReadWriteLock();
//    final Lock r = rwl.readLock();
//    final Lock w = rwl.writeLock();
//    V get(K key) {
//        V v = null;
//        // 读缓存
//        r.lock();
//        try {
//            v = m.get(key);
//        } finally{
//            r.unlock();
//        }
//        // 缓存中存在，返回
//        if(v != null) {
//            return v;
//        }
//        // 缓存中不存在，查询数据库
//        w.lock();
//        try {
//            // 再次验证
//            // 其他线程可能已经查询过数据库
//            v = m.get(key);
//            if(v == null){
//                // 查询数据库
//                v= 省略代码无数
//                m.put(key, v);
//            }
//        } finally{
//            w.unlock();
//        }
//        return v;
//    }
}
