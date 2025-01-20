package concurrency;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Generic concurrent file reader
 * - Read from multiple text files concurrently
 * - Using thread pool and executor framework
 * - Process and print each line
 * - Thread name prefix for each data entry
 * ----
 * Executor Framework
 *   - Without Executor service, the whole lifecycle management of the thread is manual
 *   - Like thread creation, runnable func params, join() to wait, etc.
 *   - No separation of concern if all this is manual
 * - Executor Interface
 *   - Base interface which takes the runnable in its execute() method
 * - ExecutorService Interface
 *   - Extends the Executor interface and provides higher level apis
 *   - Also supports Callable instead of Runnable that can return responses as well
 *   - task submission, termination, etc.
 * - ThreadPoolExecutor class
 *   - Most common impl for the ExecutorService
 *   - Manages a pool of workers for task execution, size, eviction policy, etc.
 * - Executor class
 *   - Util class with factor methods for different types of ExecutorService
 */
public class CustomFileReader {



    public void readFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String thread = Thread.currentThread().getName();
            String line;
            while ((line = br.readLine()) != null) {
                Util.doWork(1000);
                System.out.println("[" + thread + "] " + line);
            }
        } catch (IOException e) {
            throw new RuntimeException("failed reading file", e);
        }
    }


    public static void main(String[] args) {

        CustomFileReader cfr = new CustomFileReader();

        List<String> filePaths = Util.generateFilePaths("/tmp/concurrency/example/files/file_", 10);
        System.out.println("files: " + filePaths);

        // 10 files, thread pool size 3 ~1min14sec
        // 10 files, thread pool size 10 ~20 sec
        // surround with try with resource instead of manual call to executorService.shutdown()
        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
             filePaths.forEach(file -> {
                 executorService.execute(() -> cfr.readFile(file));
             });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
