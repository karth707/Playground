package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Callable is a task that can be executed async and return a result
 * - Similar to Runnable, represents unit of work to be done
 * - Difference is it can return a response or throw a checked exception (both not possible with runnable)
 * - Typically submitted to ExecutorService for execution
 * ----
 * Future is a result of an async computation that is not immediately available
 * - Callables respond with a Future
 * - get() is blocking and isDone() used to check
 * ----
 * Use Executor Service to execute multiple callable tasks
 * - Fetch fake stock prices concurrently and return results
 */
public class CustomCallable {

    public static void main(String[] args) {
        List<String> tickers = List.of("AMZN", "AMPL", "UBER", "TSLA", "MSFT", "GOOG", "NVDA", "SQ");

        List<Future<Double>> pricesFutures = new ArrayList<>();

        try (ExecutorService es = Executors.newFixedThreadPool(2)) {
            for (String ticker : tickers) {
                Future<Double> priceFuture = es.submit(new StockPriceFetcherTask(ticker));
                System.out.println(priceFuture);
                pricesFutures.add(priceFuture);
            }

            // - If this block of code is outside the try-with-resource of the ExecutorService
            //   - It would wait for all the futures to be completed before exiting the try block
            //   - If we have it inside the try, like below, then the main thread moves ahead and can do other work
            //   - Finally we can block on the isDone() & get() when we do need the future values
            // - If we don't use the try-with-resource then we need to call the shutdown()
            //   - Then the code below would continue till we hit the isDone() & get()
            for (int t=0; t<tickers.size(); t++) {
                try {
                    // get() is blocking so we can use a while on the isDone() to check on progress
                    while (!pricesFutures.get(t).isDone()) {
                        System.out.println("Waiting for fetch of " + tickers.get(t) + " to complete...");
                        Util.doWork(2000);
                    }
                    System.out.println("[" + tickers.get(t) + "]: $" + pricesFutures.get(t).get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

class StockPriceFetcherTask implements Callable<Double> {

    private final String ticker;

    public StockPriceFetcherTask(String ticker) {
        this.ticker = ticker;
    }

    @Override
    public Double call() throws Exception {
        String thread = Thread.currentThread().getName();
        System.out.println("[" + thread + "] Checking stock price for: " + ticker + "...");
        Util.doWork(10000);
        Double price = Math.random()*1000;
        System.out.println("[" + thread + "] Retrieved price for " + ticker + ": $" + price);
        return price;
    }
}
