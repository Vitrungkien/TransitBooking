
/**
 * TransitBooking - Single Page Application (SPA) Management Logic
 */

$(document).ready(function () {
    console.log("SPA Management Initialized");
    initSpaRouter();
});

function initSpaRouter() {
    // Handle Sidebar Clicks
    $('.spa-link').on('click', function (e) {
        e.preventDefault();
        const page = $(this).data('page');
        navigateTo(page);
    });

    // Initial Load based on path or default
    const path = window.location.pathname;
    if (path.includes('management-order')) navigateTo('orders');
    else if (path.includes('management-notice')) navigateTo('notices');
    else navigateTo('products');
}

function navigateTo(page) {
    // Update Sidebar UI
    $('.spa-link').removeClass('active');
    $(`.spa-link[data-page="${page}"]`).addClass('active');

    // Show Loader
    $('#spa-loader').css('display', 'flex');

    // Load Content
    switch (page) {
        case 'products': loadProductsModule(); break;
        case 'orders': loadOrdersModule(); break;
        case 'notices': loadNoticesModule(); break;
        case 'profile': loadProfileModule(); break;
        default: loadProductsModule();
    }

    // Update Browser URL (History API)
    const url = page === 'products' ? '/management' : `/management-${page}`;
    window.history.pushState({ page }, '', url);
}

// --- MODULE: PRODUCTS ---
function loadProductsModule() {
    ApiService.get(API_ENDPOINTS.SELLER.PRODUCT.MY_STORE_ALL)
        .then(data => {
            renderProductsView(data);
            $('#spa-loader').hide();
        })
        .catch(err => handleModuleError("Sản phẩm", err));
}

function renderProductsView(products) {
    const activeCount = products.filter(p => p.display).length;
    const html = `
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="font-weight-bold mb-0">Quản lý Chuyến xe</h2>
                <p class="text-muted mb-0">Theo dõi và cập nhật các tuyến xe của cửa hàng</p>
            </div>
            <a href="/management/add-product" class="btn btn-primary btn-lg shadow-sm" style="border-radius: 10px; font-weight: 700;">
                <i class="fas fa-plus mr-2"></i> THÊM MỚI
            </a>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon" style="background: rgba(36, 116, 229, 0.1); color: var(--primary);"><i class="fas fa-route"></i></div>
                <div class="stat-info"><h3>${products.length}</h3><p>Tổng số vé</p></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background: rgba(40, 167, 69, 0.1); color: var(--success);"><i class="fas fa-check-circle"></i></div>
                <div class="stat-info"><h3>${activeCount}</h3><p>Đang hiển thị</p></div>
            </div>
        </div>

        <div class="content-card">
            <div class="card-header"><h5 class="card-title">Danh sách chi tiết</h5></div>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr><th>STT</th><th>Thông tin tuyến</th><th>Thời gian</th><th>Giá & Slot</th><th>Trạng thái</th><th>Thao tác</th></tr>
                    </thead>
                    <tbody id="product-list-body"></tbody>
                </table>
            </div>
        </div>
    `;
    $('#main-content-area').html(html);

    const tbody = $('#product-list-body');
    if (products.length === 0) {
        tbody.append('<tr><td colspan="6" class="text-center p-5 text-muted">Chưa có chuyến xe nào.</td></tr>');
        return;
    }

    products.forEach((p, i) => {
        tbody.append(`
            <tr>
                <td>${i + 1}</td>
                <td>
                    <div class="d-flex align-items-center">
                        <img src="${p.productImage || '/img/Picture1.png'}" class="product-img mr-3 shadow-sm">
                        <div class="route-info"><span class="route-main">${p.productName}</span><span class="route-sub">${p.startAddress} &rarr; ${p.endAddress}</span></div>
                    </div>
                </td>
                <td><div class="font-weight-bold">${formatTime(p.startTime)}</div></td>
                <td><div class="text-primary font-weight-bold">${p.price.toLocaleString()}đ</div><div class="small">Còn ${p.remainSeat} chỗ</div></td>
                <td><span class="badge badge-status ${p.display ? 'badge-success' : 'badge-secondary'}">${p.display ? 'HIỂN THỊ' : 'ẨN'}</span></td>
                <td>
                    <div class="action-btns">
                        <button class="btn-action btn-view" title="Xem chi tiết" onclick="showProductDetails(${p.productId})"><i class="fas fa-eye"></i></button>
                        <a href="/management/${p.productId}/update-product" class="btn-action btn-edit" title="Chỉnh sửa"><i class="fas fa-pen"></i></a>
                        <button class="btn-action btn-delete" title="Xóa" onclick="prepareDelete(${p.productId}, '${p.productName}')"><i class="fas fa-trash"></i></button>
                    </div>
                </td>
            </tr>
        `);
    });
}

function showProductDetails(id) {
    $('#spa-loader').css('display', 'flex');
    ApiService.get(API_ENDPOINTS.SELLER.PRODUCT.DETAIL(id))
        .then(p => {
            $('#spa-loader').hide();
            const modalHtml = `
                <div class="modal fade" id="detailModal" tabindex="-1">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content" style="border-radius: 15px; overflow: hidden;">
                            <div class="modal-header bg-light border-0 py-3">
                                <h5 class="modal-title font-weight-bold text-primary"><i class="fas fa-info-circle mr-2"></i>Chi tiết Chuyến xe #${p.productID}</h5>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                            </div>
                            <div class="modal-body p-4">
                                <div class="row mb-4">
                                    <div class="col-md-4"><img src="${p.productImage || '/img/Picture1.png'}" class="img-fluid rounded shadow-sm" onerror="this.src='/img/Picture1.png'"></div>
                                    <div class="col-md-8">
                                        <h4 class="font-weight-bold mb-1">${p.productName}</h4>
                                        <p class="text-muted"><i class="fas fa-bus mr-2"></i>${p.type} • ${p.bienSoXe || 'N/A'}</p>
                                        <div class="row">
                                            <div class="col-6">
                                                <label class="small text-muted mb-0">Giá vé</label>
                                                <p class="font-weight-bold text-danger" style="font-size: 1.25rem;">${p.price.toLocaleString()}đ</p>
                                            </div>
                                            <div class="col-6">
                                                <label class="small text-muted mb-0">Chỗ trống</label>
                                                <p class="font-weight-bold" style="font-size: 1.25rem;">${p.remainSeat} Ghế</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="bg-light p-3 rounded-lg mb-4">
                                    <div class="row">
                                        <div class="col-md-6 border-right">
                                            <label class="small font-weight-bold text-uppercase text-muted">Xuất phát</label>
                                            <p class="mb-0 font-weight-bold text-dark">${p.startAddress}</p>
                                            <p class="small text-primary font-weight-bold"><i class="far fa-clock mr-1"></i>${formatTime(p.startTime)}</p>
                                        </div>
                                        <div class="col-md-6 pl-md-4">
                                            <label class="small font-weight-bold text-uppercase text-muted">Điểm cuối</label>
                                            <p class="mb-0 font-weight-bold text-dark">${p.endAddress}</p>
                                            <p class="small text-success font-weight-bold"><i class="far fa-clock mr-1"></i>${p.endTime ? formatTime(p.endTime) : '---'}</p>
                                        </div>
                                    </div>
                                </div>
                                <h6 class="font-weight-bold mb-3 border-bottom pb-2">Lộ trình chi tiết (${p.stopList ? p.stopList.length : 0} điểm dừng)</h6>
                                <div class="stop-list-scroll" style="max-height: 200px; overflow-y: auto;">
                                    ${p.stopList && p.stopList.length > 0 ? p.stopList.map((s, idx) => `
                                        <div class="d-flex align-items-center mb-2 p-2 border-bottom-0 bg-white rounded shadow-xs" style="border: 1px solid #f0f0f0;">
                                            <div class="mr-3 font-weight-bold text-muted">${idx + 1}</div>
                                            <div class="flex-grow-1">
                                                <div class="font-weight-bold small">${s.stopAddress}</div>
                                                <div class="small text-muted"><i class="far fa-clock mr-1"></i>${formatTime(s.stopTime)}</div>
                                            </div>
                                        </div>
                                    `).join('') : '<p class="text-center text-muted py-3">Không có điểm dừng chi tiết.</p>'}
                                </div>
                            </div>
                            <div class="modal-footer border-0">
                                <button type="button" class="btn btn-light px-4" data-dismiss="modal">Đóng</button>
                                <a href="/management/${p.productID}/update-product" class="btn btn-primary px-4 shadow-sm">Chỉnh sửa</a>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            $('#modal-container').html(modalHtml);
            $('#detailModal').modal('show');
        })
        .catch(err => handleModuleError("Chi tiết sản phẩm", err));
}

// --- MODULE: ORDERS ---
function loadOrdersModule() {
    ApiService.get('/api/v1/seller/my-store/all-order')
        .then(data => {
            renderOrdersView(data);
            $('#spa-loader').hide();
        })
        .catch(err => handleModuleError("Đơn hàng", err));
}

function renderOrdersView(orders) {
    const totalCount = orders.length;
    const pendingCount = orders.filter(o => o.orderStatus === 'Chờ xác nhận' || o.orderStatus === 'WAITING').length;
    const completedCount = orders.filter(o => o.orderStatus === 'Đã hoàn thành' || o.orderStatus === 'COMPLETED').length;

    const html = `
        <h2 class="font-weight-bold mb-4">Quản lý Đơn hàng</h2>
        <div class="stats-grid mb-4">
            <div class="stat-card">
                <div class="stat-icon" style="background: rgba(36, 116, 229, 0.1); color: var(--primary);"><i class="fas fa-shopping-bag"></i></div>
                <div class="stat-info"><h3>${totalCount}</h3><p>Tổng đơn hàng</p></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background: rgba(250, 173, 20, 0.1); color: #faad14;"><i class="fas fa-clock"></i></div>
                <div class="stat-info"><h3>${pendingCount}</h3><p>Chờ xác nhận</p></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background: rgba(40, 167, 69, 0.1); color: var(--success);"><i class="fas fa-check-double"></i></div>
                <div class="stat-info"><h3>${completedCount}</h3><p>Hoàn thành</p></div>
            </div>
        </div>
        <div class="content-card">
            <div class="card-header"><h5 class="card-title">Danh sách giao dịch</h5></div>
            <div class="table-responsive">
                <table class="table">
                    <thead><tr><th>STT</th><th>Mã đơn</th><th>Khách hàng</th><th>Vé</th><th>Tổng tiền</th><th>Trạng thái</th><th>Hành động</th></tr></thead>
                    <tbody id="order-list-body"></tbody>
                </table>
            </div>
        </div>
    `;
    $('#main-content-area').html(html);

    const tbody = $('#order-list-body');
    orders.forEach((o, i) => {
        let statusClass = 'status-pending';
        if (o.orderStatus === 'Đã hoàn thành') statusClass = 'status-completed';
        if (o.orderStatus === 'Đã hủy') statusClass = 'status-cancelled';

        tbody.append(`
            <tr>
                <td>${i + 1}</td>
                <td class="font-weight-bold text-primary">#${o.orderId}</td>
                <td><div>${o.phoneNumber}</div><small class="text-muted">${moment(o.createdAt).format('DD/MM/YYYY')}</small></td>
                <td><div class="font-weight-bold">${o.productName}</div><small>${o.pickUpAddress}</small></td>
                <td><div class="text-danger font-weight-bold">${o.totalPrice.toLocaleString()}đ</div></td>
                <td><span class="status-badge ${statusClass}">${o.orderStatus}</span></td>
                <td><button class="btn btn-outline-primary btn-sm" onclick="alert('Tính năng detail đang được tích hợp SPA...')">Chi tiết</button></td>
            </tr>
        `);
    });
}

// --- MODULE: NOTICES ---
function loadNoticesModule() {
    ApiService.get('/api/v1/seller/my-store/all-notices')
        .then(data => {
            renderNoticesView(data);
            $('#spa-loader').hide();
        })
        .catch(err => handleModuleError("Thông báo", err));
}

function renderNoticesView(notices) {
    const html = `
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="font-weight-bold mb-0">Quản lý Thông báo</h2>
            <button class="btn btn-primary shadow-sm">TẠO THÔNG BÁO</button>
        </div>
        <div class="content-card">
            <div class="table-responsive">
                <table class="table">
                    <thead><tr><th>STT</th><th>Chuyến xe</th><th>Nội dung</th><th>Ngày gửi</th><th>Trạng thái</th></tr></thead>
                    <tbody id="notice-list-body"></tbody>
                </table>
            </div>
        </div>
    `;
    $('#main-content-area').html(html);

    const tbody = $('#notice-list-body');
    notices.forEach((n, i) => {
        tbody.append(`
            <tr>
                <td>${i + 1}</td>
                <td class="font-weight-bold">${n.productName}</td>
                <td><div class="notice-title">${n.title}</div><div class="small">${n.content}</div></td>
                <td>${moment(n.createdAt).format('DD/MM/YYYY')}</td>
                <td><span class="${n.expired ? 'status-expired' : 'status-active'}">${n.expired ? 'Hết hiệu lực' : 'Đang hiện'}</span></td>
            </tr>
        `);
    });
}

// --- MODULE: PROFILE ---
function loadProfileModule() {
    ApiService.get('/api/v1/me')
        .then(data => {
            renderProfileView(data);
            $('#spa-loader').hide();
        })
        .catch(err => handleModuleError("Hồ sơ", err));
}

function renderProfileView(u) {
    const html = `
        <h2 class="font-weight-bold mb-4">Hồ sơ Cửa hàng</h2>
        <div class="row">
            <div class="col-lg-4">
                <div class="content-card text-center pb-4">
                    <div class="profile-header"></div>
                    <img src="/img/user.png" class="profile-avatar shadow">
                    <h4 class="mt-3 font-weight-bold">${u.firstName} ${u.lastName || ''}</h4>
                    <p class="badge badge-primary">${u.role}</p>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="content-card p-4">
                    <h5 class="font-weight-bold mb-4 border-bottom pb-2">Thông tin chi tiết</h5>
                    <div class="row">
                        <div class="col-md-6 mb-3"><label class="small text-muted mb-0">Email</label><p class="font-weight-bold">${u.email}</p></div>
                        <div class="col-md-6 mb-3"><label class="small text-muted mb-0">Số điện thoại</label><p class="font-weight-bold">${u.phoneNumber}</p></div>
                    </div>
                </div>
            </div>
        </div>
    `;
    $('#main-content-area').html(html);
}

// --- HELPERS ---
function handleModuleError(moduleName, err) {
    console.error(`Error loading ${moduleName}:`, err);
    $('#spa-loader').hide();
    $('#main-content-area').html(`
        <div class="text-center py-5">
            <i class="fas fa-exclamation-triangle text-danger mb-3" style="font-size: 3rem;"></i>
            <h4 class="text-danger">Không thể tải module ${moduleName}</h4>
            <p class="text-muted">Đã xảy ra lỗi kết nối với máy chủ. Vui lòng thử lại sau.</p>
            <button class="btn btn-outline-primary mt-3" onclick="location.reload()">Tải lại trang</button>
        </div>
    `);
}

function prepareDelete(id, name) {
    if (confirm(`Bạn có chắc chắn muốn xóa "${name}"?`)) {
        // Simple mock of delete logic for SPA example
        alert('Đã gửi yêu cầu xóa ' + id);
    }
}
