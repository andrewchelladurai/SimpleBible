package com.andrewchelladurai.simplebible.data;

import android.annotation.SuppressLint;

import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookRepository
    implements RepositoryOps {

    private static final List<Book>         ITEMS         = new ArrayList<>();
    @SuppressLint("UseSparseArrays")
    private static final Map<Integer, Book> ITEM_MAP      = new HashMap<>();
    private static       BookRepository     THIS_INSTANCE = new BookRepository();

    private BookRepository() {
    }

    public static BookRepository getInstance() {
        return THIS_INSTANCE;
    }

    /**
     * Populates the repository using the given source
     *
     * @param source can be a cursor or List<@{@link Book}>
     *
     * @return true if successfully loaded
     */
    @Override
    public boolean populate(final Object source) {
        return false;
    }

    /**
     * Clears the repository
     */
    @Override
    public void clear() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    /**
     * Retruns true if both the List and Map of items is empty, false otherwise.
     *
     * @return true if List and Map is empty.
     */
    @Override
    public boolean isEmpty() {
        return ITEMS.isEmpty() && ITEM_MAP.isEmpty();
    }

    /**
     * Returns size of the repository
     *
     * @return -1 is List and Map size do not match, else the size of List
     */
    @Override
    public int size() {
        return (ITEM_MAP.size() == ITEMS.size()) ? ITEMS.size() : -1;
    }

    /**
     * Search for a record in the list and return it, null if not found
     *
     * @param key the key used in the repository's Map
     *
     * @return the record found in the repository, null if not found
     */
    @Override
    public Object getRecordUsingKey(final Object key) {
        return null;
    }

    /**
     * Search for a record in the list using the passed value and return it.
     *
     * @param value the identifying value
     *
     * @return the record found in the repository, null if not found
     */
    @Override
    public Object getRecordUsingValue(final Object value) {
        return null;
    }

    /**
     * Return the list of Objects contained in the repository
     *
     * @return List<@               Book>
     */
    @Override
    public List<Book> getList() {
        return ITEMS;
    }
}
