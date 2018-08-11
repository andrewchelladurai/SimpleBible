package com.andrewchelladurai.simplebible.data;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 11-Aug-2018 @ 7:31 PM.
 */
interface RepositoryOps {

    /**
     * Populates the repository using the given source
     *
     * @param list can be a cursor or List<@{@link com.andrewchelladurai.simplebible.data.entities.Book}>
     *
     * @return true if successfully loaded
     */
    boolean populate(@NonNull List<?> list);

    /**
     * Clears the repository
     */
    void clear();

    /**
     * Retruns true if both the List and Map of items is empty, false otherwise.
     *
     * @return true if List and Map is empty.
     */
    boolean isEmpty();

    /**
     * Returns size of the repository
     *
     * @return -1 is List and Map size do not match, else the size of List
     */
    int size();

    /**
     * Search for a record in the list and return it, null if not found
     *
     * @param key the key used in the repository's Map
     *
     * @return the record found in the repository, null if not found
     */
    @Nullable
    Object getRecordUsingKey(@NonNull Object key);

    /**
     * Search for a record in the list using the passed value and return it.
     *
     * @param value the identifying value
     *
     * @return the record found in the repository, null if not found
     */
    @Nullable
    Object getRecordUsingValue(@NonNull Object value);

    /**
     * Return the list of Objects contained in the repository
     *
     * @return list of records
     */
    List<?> getList();

    /**
     * Validate the repository against passed params
     *
     * @param objects keys or values to validate the repository
     *
     * @return true if validated
     */
    boolean validate(Object... objects);
}
