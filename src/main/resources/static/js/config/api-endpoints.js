/**
 * api-endpoints.js
 * Khai báo toàn bộ endpoint của hệ thống
 */

const API_ENDPOINTS = {

    BASE: '/api',

    HOME: {
        PRODUCT: {
            ALL: '/all-product',
            DETAIL: (id) => `'/api/v1/seller/my-store/'${id}`,
        },

        ORDER: {
            CREATE: '/api/orders',
            DETAIL: (id) => `/api/orders/${id}`
        },

        NOTICE: {
            ALL: '/all-notice',
            BY_PRODUCT: (productId) => `/api/notices/product/${productId}`,
        },

        STORE: {
            ALL_STORE_NAME: '/all-store-name'
        }
    },
    MY: {
        PRODUCT: {
            ALL: '/api/v1/seller/my-store/all-product',
            DETAIL: (id) => `'/api/v1/seller/my-store/'${id}`,
        },

        ORDER: {
            CREATE: '/api/orders',
            DETAIL: (id) => `/api/orders/${id}`
        },

        NOTICE: {
            ALL: '/api/v1/seller/my-store/all-notices',
            BY_PRODUCT: (productId) => `/api/notices/product/${productId}`,
        },

        STORE: {
            ALL_STORE_NAME: '/all-store-name'
        }
    },
    LOG_OUT: '/logout',
};
