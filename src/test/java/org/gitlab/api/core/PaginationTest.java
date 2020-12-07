package org.gitlab.api.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaginationTest {

    @Test
    void getDefaultPagination() {
        Pagination defaultPagination = Pagination.getDefaultPagination();
        assertEquals(0, defaultPagination.getPageNumber());
        assertEquals(20, defaultPagination.getPageSize());
    }

    @Test
    void getPagination() {
        Pagination pagination = Pagination.getPagination(1, 5);
        assertEquals(1, pagination.getPageNumber());
        assertEquals(5, pagination.getPageSize());
        assertDoesNotThrow(() -> {
            Pagination.getPagination(1, 100);
        });
        assertDoesNotThrow(() -> {
            Pagination.getPagination(100, 1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Pagination.getPagination(-1, 5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Pagination.getPagination(1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Pagination.getPagination(1, 200);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Pagination.getPagination(-1, -1);
        });
    }

    @Test
    void getPageNumber() {
        Pagination defaultPagination = Pagination.getDefaultPagination();
        assertEquals(0, defaultPagination.getPageNumber());
        Pagination pagination = Pagination.getPagination(1, 5);
        assertEquals(1, pagination.getPageNumber());
    }

    @Test
    void getPageSize() {
        Pagination defaultPagination = Pagination.getDefaultPagination();
        assertEquals(20, defaultPagination.getPageSize());
        Pagination pagination = Pagination.getPagination(1, 5);
        assertEquals(5, pagination.getPageSize());
    }
}