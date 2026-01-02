/**
 * rest-api-service.js
 * Dùng chung cho toàn bộ project Spring Thymeleaf
 */

const ApiService = (() => {

    const DEFAULT_HEADERS = {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    };

    /**
     * Build query string từ object
     */
    function buildQuery(params = {}) {
        const query = new URLSearchParams(params).toString();
        return query ? `?${query}` : '';
    }

    /**
     * Xử lý response chung
     */
    async function handleResponse(response) {
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error ${response.status}`);
        }
        return response.status === 204 ? null : response.json();
    }

    /**
     * GET
     */
    async function get(url, params = {}) {
        const query = buildQuery(params);
        const response = await fetch(url + query, {
            method: 'GET',
            headers: DEFAULT_HEADERS,
            credentials: 'same-origin'
        });
        return handleResponse(response);
    }

    /**
     * POST
     */
    async function post(url, body = {}) {
        const response = await fetch(url, {
            method: 'POST',
            headers: DEFAULT_HEADERS,
            credentials: 'same-origin',
            body: JSON.stringify(body)
        });
        return handleResponse(response);
    }

    /**
     * PUT
     */
    async function put(url, body = {}) {
        const response = await fetch(url, {
            method: 'PUT',
            headers: DEFAULT_HEADERS,
            credentials: 'same-origin',
            body: JSON.stringify(body)
        });
        return handleResponse(response);
    }

    /**
     * DELETE
     */
    async function remove(url) {
        const response = await fetch(url, {
            method: 'DELETE',
            headers: DEFAULT_HEADERS,
            credentials: 'same-origin'
        });
        return handleResponse(response);
    }

    /**
     * Upload file (multipart/form-data)
     */
    async function upload(url, formData) {
        const response = await fetch(url, {
            method: 'POST',
            credentials: 'same-origin',
            body: formData
        });
        return handleResponse(response);
    }

    return {
        get,
        post,
        put,
        delete: remove,
        upload
    };
})();
