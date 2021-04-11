package Crawler;

/**
 * Like Runnable interface but run() can throw exceptions.
 */
public interface RunnableWithException {

    /**
     * function you want to run.
     * @throws Exception any exception the function wants to throw.
     */
    void run() throws Exception;
}
