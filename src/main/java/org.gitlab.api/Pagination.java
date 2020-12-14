package org.gitlab.api;

/**
 * This is the class to represent pagination in the gitlab api.
 * Gitlab Web API: https://docs.gitlab.com/ee/api/#pagination
 */
public final class Pagination {
    /**
     * By default, each page returns 20 results at a time.
     */
    private static final int DEFAULT_PAGE_SIZE = 20;
    /**
     * Gitlab supports retrieve at most 20 results per page.
     */
    private static final int MAX_PAGE_SIZE = 100;
    /**
     * The default pagination is retrieve the first page with {@link #DEFAULT_PAGE_SIZE}
     */
    private static final Pagination DEFAULT_PAGINATION = new Pagination(1, DEFAULT_PAGE_SIZE);
    /**
     * The page number to be retrieved
     */
    private final int pageNumber;
    /**
     * The results per pages to be retrieved
     */
    private final int pageSize;

    /**
     * Initialize the Pagination with page number and size
     *
     * @param pageNumber the page number to be retrieved
     * @param pageSize   the results per pages to be retrieved
     */
    private Pagination(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * Returns {@code Pagination} with page number 1 and default page size.
     *
     * @return The default {@code Pagination} object
     */
    public static Pagination getDefaultPagination() {
        return DEFAULT_PAGINATION;
    }

    /**
     * Returns {@code Pagination} with specified page number and items per page.
     *
     * @param pageNumber page number (an integer starting from 1)
     * @param pageSize   items per page (can be any integer between 1 and 100, both inclusive)
     * @return A {@code Pagination} object
     * @throws IllegalArgumentException if specified page number or page size out of valid range
     */
    public static Pagination of(int pageNumber, int pageSize) {
        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
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