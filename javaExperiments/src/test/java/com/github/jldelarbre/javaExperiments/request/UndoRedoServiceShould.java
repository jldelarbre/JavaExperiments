package com.github.jldelarbre.javaExperiments.request;

import com.github.jldelarbre.javaExperiments.request.utils.FakeRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.github.jldelarbre.javaExperiments.request.impl.ServiceProviderImpl.aServiceProvider;
import static com.github.jldelarbre.javaExperiments.request.utils.FakeRequest.Builder.aFakeRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UndoRedoServiceShould {

    private ServiceProvider serviceProvider;
    private RequestExecutor requestExecutor;
    private UndoRedoService undoRedoService;

    @Before
    public void setUp() throws Exception {
        serviceProvider = aServiceProvider();
        requestExecutor = serviceProvider.getRequestExecutor();
        undoRedoService = serviceProvider.getUndoRedoService();
    }

    @Test
    public void startWithEmptyStack() {
        assertTrue("Undo list should be empty", undoRedoService.getUndoActionShortDescriptions().isEmpty());
        assertTrue("Redo list should be empty", undoRedoService.getRedoActionShortDescriptions().isEmpty());

        // Shall do nothing but must not generate errors
        undoRedoService.redo();
        undoRedoService.undo();
    }

    @Test
    public void beAbleToUndoRedo() {
        final String shortDescription1 = "shortDescription1";
        FakeRequest.Builder builder = aFakeRequest().withShortDescription(shortDescription1);
        List<String> actions = builder.getActions();
        Request request1 = builder.build();

        requestExecutor.execute(request1);

        assertTrue(undoRedoService.getRedoActionShortDescriptions().isEmpty());
        assertEquals(1, undoRedoService.getUndoActionShortDescriptions().size());
        assertEquals(shortDescription1, undoRedoService.getUndoActionShortDescriptions().get(0));
        assertEquals("execute", actions.get(0));

        undoRedoService.undo();

        assertTrue(undoRedoService.getUndoActionShortDescriptions().isEmpty());
        assertEquals(1, undoRedoService.getRedoActionShortDescriptions().size());
        assertEquals(shortDescription1, undoRedoService.getRedoActionShortDescriptions().get(0));
        assertEquals("undo", actions.get(1));

        undoRedoService.redo();

        assertTrue(undoRedoService.getRedoActionShortDescriptions().isEmpty());
        assertEquals(1, undoRedoService.getUndoActionShortDescriptions().size());
        assertEquals(shortDescription1, undoRedoService.getUndoActionShortDescriptions().get(0));
        assertEquals("redo", actions.get(2));
    }



    @Test
    public void beAbleToUndoRedoManyTime() {
        final String shortDescription1 = "shortDescription1";
        final String shortDescription2 = "shortDescription2";
        Request request1 = aFakeRequest().withShortDescription(shortDescription1).build();
        Request request2 = aFakeRequest().withShortDescription(shortDescription2).build();

        requestExecutor.execute(request1);
        requestExecutor.execute(request2);

        assertEquals(0, undoRedoService.getRedoActionShortDescriptions().size());
        assertEquals(2, undoRedoService.getUndoActionShortDescriptions().size());
        assertEquals(shortDescription2, undoRedoService.getUndoActionShortDescriptions().get(0));
        assertEquals(shortDescription1, undoRedoService.getUndoActionShortDescriptions().get(1));

        undoRedoService.undo();

        assertEquals(1, undoRedoService.getUndoActionShortDescriptions().size());
        assertEquals(1, undoRedoService.getRedoActionShortDescriptions().size());
        assertEquals(shortDescription1, undoRedoService.getUndoActionShortDescriptions().get(0));
        assertEquals(shortDescription2, undoRedoService.getRedoActionShortDescriptions().get(0));

        undoRedoService.redo();

        assertEquals(0, undoRedoService.getRedoActionShortDescriptions().size());
        assertEquals(2, undoRedoService.getUndoActionShortDescriptions().size());
        assertEquals(shortDescription2, undoRedoService.getUndoActionShortDescriptions().get(0));
        assertEquals(shortDescription1, undoRedoService.getUndoActionShortDescriptions().get(1));

        undoRedoService.undo();
        undoRedoService.undo();

        assertEquals(2, undoRedoService.getRedoActionShortDescriptions().size());
        assertEquals(0, undoRedoService.getUndoActionShortDescriptions().size());
        assertEquals(shortDescription1, undoRedoService.getRedoActionShortDescriptions().get(0));
        assertEquals(shortDescription2, undoRedoService.getRedoActionShortDescriptions().get(1));

        undoRedoService.redo();

        assertEquals(1, undoRedoService.getRedoActionShortDescriptions().size());
        assertEquals(1, undoRedoService.getUndoActionShortDescriptions().size());
        assertEquals(shortDescription1, undoRedoService.getUndoActionShortDescriptions().get(0));
        assertEquals(shortDescription2, undoRedoService.getRedoActionShortDescriptions().get(0));

        undoRedoService.redo();

        assertEquals(0, undoRedoService.getRedoActionShortDescriptions().size());
        assertEquals(2, undoRedoService.getUndoActionShortDescriptions().size());
        assertEquals(shortDescription2, undoRedoService.getUndoActionShortDescriptions().get(0));
        assertEquals(shortDescription1, undoRedoService.getUndoActionShortDescriptions().get(1));
    }

    @Test
    public void shallClearRedoStackOnNewRequest() {
        Request request = aFakeRequest().build();
        Request request2 = aFakeRequest().build();

        requestExecutor.execute(request);

        undoRedoService.undo();
        assertEquals(1, undoRedoService.getRedoActionShortDescriptions().size());

        requestExecutor.execute(request2);

        assertEquals(0, undoRedoService.getRedoActionShortDescriptions().size());
        assertEquals(1, undoRedoService.getUndoActionShortDescriptions().size());
    }

    @Test
    public void shallNotStackAnUndoableRequest() {
        Request undoableRequest = aFakeRequest().thatShallBeAddedToUndoRedoStack(false).build();

        requestExecutor.execute(undoableRequest);

        assertTrue(undoRedoService.getUndoActionShortDescriptions().isEmpty());
        assertTrue(undoRedoService.getRedoActionShortDescriptions().isEmpty());
    }

    @Test
    public void shallNotStackAnUndoableRequestWithExistingRequest() {
        Request request = aFakeRequest().build();
        Request undoableRequest = aFakeRequest().thatShallBeAddedToUndoRedoStack(false).build();

        requestExecutor.execute(request);
        requestExecutor.execute(undoableRequest);

        assertEquals(0, undoRedoService.getRedoActionShortDescriptions().size());
        assertEquals(1, undoRedoService.getUndoActionShortDescriptions().size());
    }

    @Test
    public void shallResetUndoRedoStackWithUndoableRequest() {
        Request request = aFakeRequest().build();
        Request undoableRequest = aFakeRequest().thatImplyUndoRedoStackReset(true).build();

        requestExecutor.execute(request);

        assertEquals(1, undoRedoService.getUndoActionShortDescriptions().size());

        requestExecutor.execute(undoableRequest);

        assertEquals(0, undoRedoService.getRedoActionShortDescriptions().size());
        assertEquals(0, undoRedoService.getUndoActionShortDescriptions().size());
    }
}
