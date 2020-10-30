public class Test implements Runnable {
    private BlockingQueueLockExample blockingQueueLockExample;
    public Test(BlockingQueueLockExample blockingQueueLockExample){
        this.blockingQueueLockExample = blockingQueueLockExample;
    }
    public void run() {
        for(int i = 0; i <1000;i++){
            blockingQueueLockExample.enq(i);
        }
    }
}
