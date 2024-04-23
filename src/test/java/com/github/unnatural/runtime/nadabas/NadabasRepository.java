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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.unnatural.runtime.AdabasRecord;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * An incomplete in-memory implementation for a repository of adabas records
 * Capable of reading JSON data copied from DTS elasticsearch
 * @param <E>
 */
@Slf4j
public class NadabasRepository<E extends NadabasRecord> {
    private final Map<Integer, E> records = new HashMap<>();
    private final ObjectMapper mapper;

    private final NadabasEvaluator<E> evaluator;
    private final Class<E> recordClass;
    @Getter
    private int nextIsn = 1000;

    public NadabasRepository(NadabasEvaluator<E> evaluator, Class<E> recordClass) {
        this.evaluator = evaluator;
        this.recordClass = recordClass;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public void initialise(InputStream jsonIs) {
        try {
            E[] recordsIn = mapper.readValue(jsonIs, (Class<E[]>) recordClass.arrayType());
            Arrays.stream(recordsIn).forEach(r -> records.put(r.getIsn(), r));
        } catch (IOException e) {
            throw new RuntimeException("Unable to initialise Nadabas repository: " + e.getMessage());
        }
    }

    public <T extends AdabasRecord> T getByIsn(Class<T> viewClass, int isn) {
        NadabasRecord srcRecord = records.get(isn);
        if (srcRecord == null){
            return null;
        }
        return adaptToViewClass(srcRecord, viewClass);
    }

    private static <T extends AdabasRecord> T adaptToViewClass(NadabasRecord srcRecord, Class<T> viewClass) {
        T result = instantiate(viewClass);
        copyFieldsByName(srcRecord, result);
        return result;
    }

    private static <T> T instantiate(Class<T> classToInstantiate) {
        try {
            return classToInstantiate.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    private static void copyFieldsByName(Object src, Object dest) {
        Field[] srcFields = src.getClass().getDeclaredFields();
        Arrays.stream(srcFields).forEach((srcField) -> getField(dest.getClass(), srcField.getName()).ifPresent(
                        (destField -> copyValue(srcField, src, destField, dest))));
    }

    private static void copyValue(Field srcField, Object src, Field destField, Object dest) {
        try {
            srcField.setAccessible(true);
            destField.setAccessible(true);
            if (simpleSrcType(srcField)) {
                Object value = srcField.get(src);
                if (destField.getType().equals(srcField.getType())) {
                    destField.set(dest, value);
                } else {
                    if (srcField.getType().equals(int.class)) {
                        setViaCustomSetter(value, destField, dest, "Integer", int.class);
                    } else if (srcField.getType().equals(String.class)) {
                        setViaCustomSetter(value, destField, dest, "String", String.class);
                    }
                }
            } else if (arrayOfSimpleSrcType(srcField)){
                if (destField.getType().equals(srcField.getType())) {
                    copyArrayValues(srcField, src, destField, dest);
                } else {
                    log.warn("Unable to copy {} on class {} to class {} - {}: {}", srcField.getName(),
                            src.getClass().getName(), dest.getClass().getName(), "unsupported array",
                            srcField.getType().getName());
                }
            } else {
                log.warn("Unable to copy {} on class {} to class {} - {}: {}", srcField.getName(),
                        src.getClass().getName(), dest.getClass().getName(), "complex src type",
                        srcField.getType().getName());
            }
        } catch (IllegalAccessException e) {
            log.warn("Unable to copy {} on class {} to class {} - {}", srcField.getName(),
                    src.getClass().getName(), dest.getClass().getName(), e.getMessage());
        }
    }

    private static void copyArrayValues(Field srcField, Object src, Field destField, Object dest) throws IllegalAccessException {
        if (destField.get(dest) == null) {
            destField.set(dest, srcField.get(src));
        } else {
            if (srcField.getType().equals(int[].class)) {
                int[] srcArray = (int[]) srcField.get(src);
                int[] destArray = (int[]) destField.get(dest);
                System.arraycopy(srcArray, 0, destArray, 0, srcArray.length);
            } else {
                Object[] srcArray = (Object[]) srcField.get(src);
                Object[] destArray = (Object[]) destField.get(dest);
                System.arraycopy(srcArray, 0, destArray, 0, srcArray.length);
            }
        }
    }

    private static boolean arrayOfSimpleSrcType(Field srcField) {
        return srcField.getType().equals(int[].class) || srcField.getType().equals(String[].class) || srcField.getType().equals(LocalDate[].class) || srcField.getType().equals(LocalDateTime[].class);

    }

    private static boolean simpleSrcType(Field srcField) {
        return srcField.getType().equals(int.class) || srcField.getType().equals(String.class) || srcField.getType().equals(LocalDate.class) || srcField.getType().equals(LocalDateTime.class);
    }

    private static void setViaCustomSetter(Object value, Field destField, Object dest, String typeName, Class<?> paramClass) {
        try {
            Method setter = destField.getType().getMethod("setFrom" + typeName, paramClass);
            Object complexDestField = destField.get(dest);
            setter.invoke(complexDestField, value);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e ) {
            log.warn("Unable to set {} on class {} - {} {}", destField.getName(),
                    dest.getClass().getName(), "no custom setter for", typeName);
        }
    }

    static Optional<Field> getField(Class<?> clazz, String fieldName) {
        if (classHasField(clazz, fieldName)){
            try {
                return Optional.of(clazz.getDeclaredField(fieldName));
            } catch (NoSuchFieldException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private static boolean classHasField(Class<?> clazz, String fieldName) {
        return Arrays.stream(clazz.getDeclaredFields()).anyMatch(f -> f.getName().equals(fieldName));
    }

    public <T  extends AdabasRecord> List<T> readBy(Class<T> viewClazz, String where, Object... params) {
        return records.values().stream().filter(r -> evaluator.match(r, where, params))
                .map(r -> adaptToViewClass(r, viewClazz))
                .collect(Collectors.toList());
    }

    public <T extends AdabasRecord> List<T> readByWithCount(Class<T> viewClazz, String where, int count, Object... params) {
        List<T> results = readBy(viewClazz, where, params);
        return results.subList(0, Math.min(count, results.size()));
    }

    public <T  extends AdabasRecord> void update(T view) {
        if (!records.containsKey(view.getIsn())){
            throw new RuntimeException("No record found for ISN " + view.getIsn());
        }
        E record = records.get(view.getIsn());
        copyFieldsByName(view, record);
    }

    public <T extends AdabasRecord> int store(T view) {
        view.setIsn(nextIsn++);
        E record = instantiate(recordClass);
        copyFieldsByName(view, record);
        records.put(record.getIsn(), record);
        return view.getIsn();
    }

}
