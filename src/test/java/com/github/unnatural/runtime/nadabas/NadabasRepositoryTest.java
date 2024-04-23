/*
Unnatural support library

Copyright 2024 Chris Humphreys, https://github.com/chrishumphreys/unnatural-runtime

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.github.unnatural.runtime.nadabas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NadabasRepositoryTest {
    public static final String EXISTING_VALUE = "Hello World!";
    public static final int ISN = 799;
    private static final String SAMPLE_JSON = "nadabas/sample.json";
    private static final String NEW_VALUE = "Goodbye World!";
    private NadabasRepository<SampleNadabas> underTest;

    @BeforeEach
    void setUp() {
        underTest = new NadabasRepository<>(new SampleEvaluator(), SampleNadabas.class);
    }

    @Test
    void canInitialiseArrestSummonsRepository() {
        InputStream jsonIs = getJsonData(SAMPLE_JSON);
        underTest.initialise(jsonIs);

        SampleView view = underTest.getByIsn(SampleView.class, 799);
        assertEquals(ISN, view.getIsn());
        assertEquals(EXISTING_VALUE, view.getMyString());
    }

    @Test
    void canStoreNewValue() {
        SampleView view = new SampleView();
        view.setMyString(NEW_VALUE);
        int isn = underTest.store(view);

        SampleView reload = underTest.getByIsn(SampleView.class, isn);

        assertEquals(isn, reload.getIsn());
        assertEquals(NEW_VALUE, reload.getMyString());
    }

    @Test
    void canUpdate() {
        underTest.initialise(getJsonData(SAMPLE_JSON));
        SampleView view = underTest.getByIsn(SampleView.class, ISN);
        assertEquals(EXISTING_VALUE, view.getMyString());

        view.setMyString(NEW_VALUE);
        underTest.update(view);

        SampleView reload = underTest.getByIsn(SampleView.class, ISN);
        assertEquals(NEW_VALUE, reload.getMyString());
    }

    @Test
    void readBy() {
        underTest.initialise(getJsonData(SAMPLE_JSON));

        List<SampleView> results = underTest.readBy(SampleView.class, "MY-STRING = {myString}", EXISTING_VALUE);

        assertEquals(1, results.size());
        assertEquals(ISN, results.get(0).getIsn());
    }

    @Test
    void readByCount() {
        underTest.initialise(getJsonData(SAMPLE_JSON));

        List<SampleView> results = underTest.readByWithCount(SampleView.class, "MY-STRING = {myString}", 1, EXISTING_VALUE);

        assertEquals(1, results.size());
        assertEquals(ISN, results.get(0).getIsn());
    }

    private static InputStream getJsonData(String jsonClasspathResource) {
        InputStream jsonIs = Thread.currentThread().getContextClassLoader().getResourceAsStream(jsonClasspathResource);
        if (jsonIs == null) {
            throw new RuntimeException("Could not find " + jsonClasspathResource);
        }
        return jsonIs;
    }
}