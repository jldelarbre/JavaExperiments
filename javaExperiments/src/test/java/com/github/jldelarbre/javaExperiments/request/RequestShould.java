package com.github.jldelarbre.javaExperiments.request;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;

import static com.github.jldelarbre.javaExperiments.request.utils.FakeRequest.Builder.aFakeSuccedingRequest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class RequestShould {

    private Request.Builder requestBuilder;

    public RequestShould(Request.Builder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> requestBuilders() {
        return ImmutableSet.of(new Object[] {
            aFakeSuccedingRequest()
        });
    }

    @Test
    public void haveAShortDescription() {
        Request request = requestBuilder.build();

        String shortDescription = request.getShortDescription();

        assertFalse("Request shall have a short description", shortDescription.isEmpty());
    }

    @Test
    public void haveADescription() {
        Request request = requestBuilder.build();

        String description = request.getDescription();

        assertFalse("Request shall have a description", description.isEmpty());
    }
}
