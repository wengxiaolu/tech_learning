public class Test2 implements Runnable {
    private BlockingQueueLockExample blockingQueueLockExample;
    public Test2(BlockingQueueLockExample blockingQueueLockExample){
        this.blockingQueueLockExample = blockingQueueLockExample;
    }
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        blockingQueueLockExample.deq();
    }

    public static void main(String[] args) {
        BlockingQueueLockExample blockingQueueLockExample = new BlockingQueueLockExample();
        Test test = new Test(blockingQueueLockExample);
        Test2 test2 = new Test2(blockingQueueLockExample);
        new Thread(test).start();
        new Thread(test2).start();
    }
}
