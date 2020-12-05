package org.gitlab.api.core;

public final class Pagination {
    // By default, get requests return 20 results at a time.
    private static final int DEFAULT_PAGE_SIZE = 20;
    // Get requests returns 100 items at maximum.
    private static final int MAX_PAGE_SIZE = 100;
    private static final Pagination DEFAULT_PAGINATION = new Pagination(1, DEFAULT_PAGE_SIZE);

    private final int pageNumber;
    private final int pageSize;

    private Pagination(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * Returns {@code Pagination} with page number 0 and default page size.
     *
     * @return The default {@code Pagination} object
     */
    public static Pagination getDefault() {
        return DEFAULT_PAGINATION;
    }

    /**
     * Returns {@code Pagination} with specified page number and items per page.
     *
     * @param pageNumber page number (an integer starting from 0)
     * @param pageSize   items per page (can be any integer between 1 and 100, both inclusive)
     * @return A {@code Pagination} object
     * @throws IllegalArgumentException if specified page number or page size out of valid range
     */
    public static Pagination get(int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("cannot have negative page number");
        } else if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("cannot have page size less than 1 or greater than " + MAX_PAGE_SIZE);
        }
        return new Pagination(pageNumber, pageSize);
    }

    /**
     * Returns page number of this {@code Pagination}.
     *
     * @return The page number
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Returns page size of this {@code Pagination}.
     *
     * @return The page size
     */
    public int getPageSize() {
        return pageSize;
    }
}