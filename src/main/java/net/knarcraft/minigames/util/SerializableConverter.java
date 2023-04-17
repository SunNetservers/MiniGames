package net.knarcraft.minigames.util;

import net.knarcraft.minigames.container.SerializableContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A converter for converting between normal and serializable classes
 */
public final class SerializableConverter {

    private SerializableConverter() {

    }

    /**
     * Converts the given collection into a collection of serializable objects
     *
     * @param values           <p>The values to make serializable</p>
     * @param targetCollection <p>The collection to store the converted objects to</p>
     * @param target           <p>An instance of the target serializable container</p>
     * @param <T>              <p>The type of the value to make serializable</p>
     */
    public static <T> void makeSerializable(@NotNull Collection<T> values,
                                            @NotNull Collection<SerializableContainer<T>> targetCollection,
                                            @NotNull SerializableContainer<T> target) {
        for (T item : values) {
            targetCollection.add(target.getSerializable(item));
        }
    }

    /**
     * Converts the given collection of serializable containers into a collection of normal objects
     *
     * @param values           <p>The values to convert to normal</p>
     * @param targetCollection <p>The collection to store the converted objects to</p>
     * @param <T>              <p>The type of the value to convert to normal</p>
     */
    public static <T> void getRawValue(@NotNull Collection<SerializableContainer<T>> values,
                                       @NotNull Collection<T> targetCollection) {
        for (SerializableContainer<T> item : values) {
            targetCollection.add(item.getRawValue());
        }
    }

    /**
     * Converts the given collection into a collection of serializable objects
     *
     * @param values    <p>The values to make serializable</p>
     * @param targetMap <p>The map to store the converted objects to</p>
     * @param target    <p>An instance of the target serializable container</p>
     * @param <T>       <p>The type of the value to make serializable</p>
     */
    public static <S, T> void makeSerializable(@NotNull Map<S, Set<T>> values,
                                               @NotNull Map<S, Set<SerializableContainer<T>>> targetMap,
                                               @NotNull SerializableContainer<T> target) {
        for (Map.Entry<S, Set<T>> item : values.entrySet()) {
            Set<SerializableContainer<T>> conversionCollection = new HashSet<>();
            makeSerializable(item.getValue(), conversionCollection, target);
            targetMap.put(item.getKey(), conversionCollection);
        }
    }

    /**
     * Converts the given collection of serializable containers into a collection of normal objects
     *
     * @param values    <p>The values to convert to normal</p>
     * @param targetMap <p>The map to store the converted objects to</p>
     * @param <S>       <p>The type of the map's key</p>
     * @param <T>       <p>The type of the value to convert to normal</p>
     */
    public static <S, T> void getRawValue(@NotNull Map<S, Set<SerializableContainer<T>>> values,
                                          @NotNull Map<S, Set<T>> targetMap) {
        for (Map.Entry<S, Set<SerializableContainer<T>>> item : values.entrySet()) {
            Set<T> conversionCollection = new HashSet<>();
            getRawValue(item.getValue(), conversionCollection);
            targetMap.put(item.getKey(), conversionCollection);
        }
    }

}
