package Crawler;

/**
 * A class that helps easily do tasks that might fail, and try them again if the do.
 */
public class MultiAttemptProcess {

    private RunnableWithException runnable;
    private Runnable onFailure;
    private int maxAttempts;

    /**
     * A constructor for MultiAttemptProcess.
     * @param runnable the task you want to attempt to do more then one if fails.
     * @param onFailure the task to do if the original functions fail on all attempts.
     * @param maxAttempts the max amount of attempts to try before running onFailure function.
     */
    public MultiAttemptProcess(RunnableWithException runnable, Runnable onFailure, int maxAttempts){
        this.runnable = runnable;
        this.onFailure = onFailure;
        this.maxAttempts = maxAttempts;
    }

    /**
     * Start attempting task.
     */
    public void start(){
        int attemptsCounter = 0;
        while(attemptsCounter < maxAttempts){
            try{
                runnable.run();
                return;
            }
            catch(Exception e){
                ++attemptsCounter;
            }
        }
        this.onFailure.run();
    }
}
