package multithreading;

public class Level1_BasicThreads {
    
    static class MyTask implements Runnable {
        private String taskName;
        
        public MyTask(String taskName) {
            this.taskName = taskName;
        }
        
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            
            for (int i = 1; i <= 3; i++) {
                System.out.printf("[%s] %s - Step %d%n", threadName, taskName, i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            System.out.printf("[%s] %s completed!%n", threadName, taskName);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("\n=== LEVEL 1: Basic Thread Creation ===\n");
        System.out.println("Observe: Threads run independently with random order\n");
        
        Thread t1 = new Thread(new MyTask("Task-1"), "Worker-1");
        Thread t2 = new Thread(new MyTask("Task-2"), "Worker-2");
        Thread t3 = new Thread(new MyTask("Task-3"), "Worker-3");
        
        t1.start();
        t2.start();
        t3.start();
        
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n All threads completed!");
        System.out.println("Key Takeaway: Output order is random because threads run independently!\n");
    }
}