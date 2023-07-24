package com.github.jldelarbre.javaExperiments.request;

import com.github.jldelarbre.javaExperiments.request.utils.FakeRequest;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.github.jldelarbre.javaExperiments.request.impl.ServiceProviderImpl.aServiceProvider;
import static com.github.jldelarbre.javaExperiments.request.utils.FakeRequest.Builder.aFakeRequest;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class ThreadedRequestExecutorShould {

    private static final int WAIT_TIME = 5;

    private ServiceProvider serviceProvider;
    private RequestExecutor requestExecutor;
    private UndoRedoService undoRedoService;
    private Semaphore semaphore;
    private Long threadCallback = null;
    private String firstRequest = null;

    @Before
    public void setUp() throws Exception {
        serviceProvider = aServiceProvider();
        requestExecutor = serviceProvider.getRequestExecutor();
        undoRedoService = serviceProvider.getUndoRedoService();
    }

    @Test
    public void executeInDedicatedThreadForSeparateThreadRequest() {
        FakeRequest.Builder builder = aFakeRequest().thatNeedSeparateThread(true);
        List<Long> threads = builder.getThreads();
        Request request = builder.build();

        requestExecutor.execute(request);

        assertEquals(1, threads.size());
        Long currentThreadId = Thread.currentThread().getId();
        Long taskThread = threads.get(0);
        String errMsg = "Thread should be different: " + currentThreadId + " / " + taskThread;
        assertFalse(errMsg, currentThreadId.equals(taskThread));
    }

    @Test
    public void executeCallbackInDedicatedThreadForSeparateThreadRequest() throws InterruptedException {
        FakeRequest.Builder builder = aFakeRequest().thatNeedSeparateThread(true);
        List<Long> threads = builder.getThreads();
        Request request = builder.build();

        semaphore = new Semaphore(0);

        RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void call(Request.Result result) {
                threadCallback = Thread.currentThread().getId();
                semaphore.release();
            }
        };

        requestExecutor.execute(request, requestCallback);

        semaphore.tryAcquire(WAIT_TIME, TimeUnit.SECONDS);

        assertEquals(1, threads.size());
        Long taskThread = threads.get(0);
        Long currentThreadId = Thread.currentThread().getId();
        String errMsg = "Thread should be different: " + currentThreadId + " / " + taskThread;
        assertFalse(errMsg, currentThreadId.equals(taskThread));
        errMsg = "Thread should be different: " + currentThreadId + " / " + threadCallback;
        assertFalse(errMsg, currentThreadId.equals(threadCallback));
        assertEquals("Task and callback shall be the same", taskThread, threadCallback);
    }

    @Test
    public void executeInCurrentThreadForNormalRequest() {
        FakeRequest.Builder builder = aFakeRequest();
        List<Long> threads = builder.getThreads();
        Request request = builder.build();

        requestExecutor.execute(request);

        assertEquals(1, threads.size());
        Long currentThreadId = Thread.currentThread().getId();
        Long taskThread = threads.get(0);
        String errMsg = "Thread should be the same: " + currentThreadId + " / " + taskThread;
        assertEquals(errMsg, currentThreadId, taskThread);
    }

    @Test
    public void executeCallbackInCurrentThreadForNormalRequest() {
        FakeRequest.Builder builder = aFakeRequest();
        List<Long> threads = builder.getThreads();
        Request request = builder.build();
        RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void call(Request.Result result) {
                threadCallback = Thread.currentThread().getId();
            }
        };

        requestExecutor.execute(request, requestCallback);

        assertEquals(1, threads.size());
        Long currentThreadId = Thread.currentThread().getId();
        Long taskThread = threads.get(0);
        String errMsg = "Thread should be the same: " + currentThreadId + " / " + taskThread;
        assertEquals(errMsg, currentThreadId, taskThread);
        errMsg = "Thread should be the same: " + currentThreadId + " / " + threadCallback;
        assertEquals(errMsg, currentThreadId, threadCallback);
    }

    @Test
    public void undoRedoInCurrentThreadForNormalRequest() {
        FakeRequest.Builder builder = aFakeRequest();
        List<Long> threads = builder.getThreads();
        Request request = builder.build();

        requestExecutor.execute(request);
        undoRedoService.undo();
        undoRedoService.redo();

        assertEquals(3, threads.size());
        Long currentThreadId = Thread.currentThread().getId();
        Long undoThread = threads.get(1);
        String errMsg = "Thread should be the same: " + currentThreadId + " / " + undoThread;
        assertEquals(errMsg, currentThreadId, undoThread);
        Long redoThread = threads.get(2);
        errMsg = "Thread should be the same: " + currentThreadId + " / " + redoThread;
        assertEquals(errMsg, currentThreadId, redoThread);
    }

    @Test
    public void undoRedoInDedicatedThreadForSeparateThreadRequest() throws InterruptedException {
        semaphore = new Semaphore(0);
        FakeRequest.Builder builder = aFakeRequest().thatNeedSeparateThread(true).withSemaphoreToRelease(semaphore);
        List<Long> threads = builder.getThreads();
        Request request = builder.build();

        requestExecutor.execute(request);
        semaphore.tryAcquire(WAIT_TIME, TimeUnit.SECONDS);
        undoRedoService.undo();
        semaphore.tryAcquire(WAIT_TIME, TimeUnit.SECONDS);
        undoRedoService.redo();
        semaphore.tryAcquire(WAIT_TIME, TimeUnit.SECONDS);

        assertEquals(3, threads.size());
        Long currentThreadId = Thread.currentThread().getId();
        Long undoThread = threads.get(1);
        String errMsg = "Thread should be different: " + currentThreadId + " / " + undoThread;
        assertFalse(errMsg, currentThreadId.equals(undoThread));
        Long redoThread = threads.get(2);
        errMsg = "Thread should be different: " + currentThreadId + " / " + redoThread;
        assertFalse(errMsg, currentThreadId.equals(redoThread));
    }

    @Test
    public void executeInOrderTasks() throws InterruptedException {
        Request request1 = aFakeRequest().thatNeedSeparateThread(true).build();
        Request request2 = aFakeRequest().build();

        semaphore = new Semaphore(0);
        Semaphore semaphoreEndOfRequest1 = new Semaphore(0);
        Object syncObject = new Object();

        RequestCallback requestCallback1 = new RequestCallback() {
            @Override
            public void call(Request.Result result) {
                try {
                    semaphore.tryAcquire(300, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (syncObject) {
                    if (firstRequest == null) {
                        firstRequest = "request1";
                    }
                }
                semaphoreEndOfRequest1.release();
            }
        };

        RequestCallback requestCallback2 = new RequestCallback() {
            @Override
            public void call(Request.Result result) {
                synchronized (syncObject) {
                    if (firstRequest == null) {
                        firstRequest = "request2";
                    }
                }
                // Avoid to wait if request2 is finished and request1 still running
                // Unnecessary if request1 finished first but no drawback
                semaphore.release();
            }
        };

        requestExecutor.execute(request1, requestCallback1);
        requestExecutor.execute(request2, requestCallback2);

        semaphoreEndOfRequest1.acquire();

        assertEquals("Request 1 shall terminate before request 2 is started", "request1", firstRequest);
    }
}
