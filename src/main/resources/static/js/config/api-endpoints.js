/**
 * api-endpoints.js
 * Khai báo toàn bộ endpoint của hệ thống
 */

const API_ENDPOINTS = {

    BASE: '/api',

    HOME: {
        PRODUCT: {
            ALL: '/all-product',
            DETAIL: (id) => `/api/v1/seller/my-store/${id}`,
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
            DETAIL: (id) => `/api/v1/seller/my-store/${id}`,
        },
        ORDER: {
            ALL: '/api/v1/seller/my-store/all-orders',
        },
        NOTICE: {
            ALL: '/api/v1/seller/my-store/all-notices',
        }
    },
    SELLER: {
        PRODUCT: {
            MY_STORE_ALL: '/api/v1/seller/my-store/all-product',
            DETAIL: (id) => `/api/v1/seller/my-store/${id}`,
            UPDATE: (id) => `/api/v1/seller/my-store/${id}/update-product`,
            DELETE: (id) => `/api/v1/seller/my-store/${id}/remove`,
            DISPLAY: (id) => `/api/v1/seller/my-store/${id}/display-status`,
        }
    },
    LOG_OUT: '/logout',
};
