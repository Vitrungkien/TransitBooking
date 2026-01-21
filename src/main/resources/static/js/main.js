
// State
let products = [];
let pageSize = 10;
let currentPage = 0;

$(document).ready(function () {
    console.log("Main.js loaded with jQuery");

    setupEventHandlers();

    // Determine current page type
    const productList = $('#productList');
    if (productList.length > 0) {
        if (typeof isSearchPage === 'undefined') {
            loadInitialData();
        }
    }
});

function setupEventHandlers() {
    // Toggle Product Detail
    $(document).on('click', '.info-button', function (e) {
        e.preventDefault();
        const container = $(this).closest('.transit-ticket');
        const detailDiv = container.find('.ticket-detail');
        const arrowIcon = $(this).find('i');

        if (detailDiv.is(':visible')) {
            detailDiv.slideUp(300);
            arrowIcon.removeClass('fa-caret-up').addClass('fa-caret-down');
        } else {
            detailDiv.slideDown(300);
            arrowIcon.removeClass('fa-caret-down').addClass('fa-caret-up');
            // Default tab
            detailDiv.find('.tabs-content > div').hide();
            detailDiv.find('.tab-images').css('display', 'flex');
            detailDiv.find('.ticket-tabs button').removeClass('active');
            detailDiv.find('.tab-btn-img').addClass('active');
        }
    });

    // Tab Switching in Detail
    $(document).on('click', '.ticket-tabs button', function (e) {
        e.preventDefault();
        const button = $(this);
        const container = button.closest('.ticket-detail');
        container.find('.tabs-content > div').hide();
        container.find('.ticket-tabs button').removeClass('active');
        button.addClass('active');

        if (button.hasClass('tab-btn-img')) {
            container.find('.tab-images').css('display', 'flex');
        } else if (button.hasClass('tab-btn-util')) {
            container.find('.tab-utils').show();
        } else if (button.hasClass('tab-btn-pos')) {
            container.find('.tab-positions').show();
        } else if (button.hasClass('tab-btn-policy')) {
            container.find('.tab-policy').show();
        } else if (button.hasClass('tab-btn-review')) {
            container.find('.tab-reviews').show();
        }
    });

    // Notice List Toggle (on hover)
    $(document).on('mouseenter', '.badge-notice-wrapper', function () {
        $(this).closest('.transit-ticket').find('.notice-overlay').stop().fadeIn(200);
    }).on('mouseleave', '.transit-ticket', function () {
        $(this).find('.notice-overlay').stop().fadeOut(200);
    });

    // Order Form Modal
    $(document).on('click', '.btn-order-ticket', function () {
        const modalId = $(this).data('target');
        $(modalId).fadeIn(300);
        $('body').css('overflow', 'hidden');
    });

    $(document).on('click', '.btn-dismiss-form', function () {
        $(this).closest('.frmDatVe-modal').fadeOut(300);
        $('body').css('overflow', 'auto');
    });
}

function loadInitialData() {
    // Sidebars
    ApiService.get(API_ENDPOINTS.HOME.NOTICE.ALL).then(renderAllNotices).catch(() => { });
    ApiService.get(API_ENDPOINTS.HOME.STORE.ALL_STORE_NAME).then(renderStoreNames).catch(() => { });

    // Main List
    ApiService.get(API_ENDPOINTS.HOME.PRODUCT.ALL)
        .then(data => {
            products = data;
            displayProducts(0);
            if (products.length > pageSize) displayPagination();
        })
        .catch(err => {
            console.error('Load products failed', err);
            if (typeof ToastService !== 'undefined') ToastService.error("Không thể tải danh sách vé");
        });
}

function renderAllNotices(notices) {
    const container = $('#all-notice');
    if (!container.length) return;
    container.empty();
    notices.forEach(notice => {
        const time = moment(notice.lastUpdate).format('HH:mm DD/MM');
        container.append(`
            <div class="notice-item p-2 mb-2 border-bottom shadow-sm bg-white rounded">
                <div class="d-flex justify-content-between align-items-center">
                    <span class="badge badge-success" style="font-size:0.65rem;">${notice.storeName}</span>
                    <small class="text-muted" style="font-size:0.7rem;">${time}</small>
                </div>
                <h6 class="mt-1 mb-0 text-danger" style="font-size: 0.85rem; font-weight:700;">${notice.title}</h6>
                <p class="mb-0 small text-muted" style="font-size: 0.75rem; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">${notice.content}</p>
            </div>
        `);
    });
}

function renderStoreNames(names) {
    const container = $('#nha-xe');
    if (!container.length) return;
    container.empty();
    names.forEach(name => {
        container.append(`<div class="p-2 mb-1 border-bottom store-name-item" style="cursor:pointer; font-size:0.9rem; font-weight:500; color:#2474E5;">${name}</div>`);
    });
}

function displayProducts(page) {
    currentPage = page;
    const start = page * pageSize;
    const end = Math.min((page + 1) * pageSize, products.length);
    const container = $('#productList');
    container.empty();

    if (products.length === 0) {
        container.append('<div class="alert alert-info shadow-sm">Không tìm thấy chuyến xe nào phù hợp.</div>');
        return;
    }

    for (let i = start; i < end; i++) {
        container.append(createProductHTML(products[i]));
    }
}

function createProductHTML(p) {
    const travelTime = calculateTimeDifference(p.startTime, p.endTime);
    const startTimeStr = formatTime(p.startTime);
    const endTimeStr = formatTime(p.endTime);
    const priceFormatted = p.price ? p.price.toLocaleString('vi-VN') + 'đ' : 'Liên hệ';
    const storeName = p.store ? p.store.storeName : 'Nhà xe';
    const productId = p.productId || p.productID;

    // Use a unique class "transit-ticket" to avoid clashes with old home.css rules
    const html = `
        <div class="transit-ticket card shadow-sm mb-4 border-0 overflow-hidden" style="border-radius:15px; background:white;">
            <div class="card-body p-0">
                <div class="row no-gutters">
                    <!-- Left Section: Image and Badge -->
                    <div class="col-md-3 position-relative bg-light d-flex align-items-center justify-content-center p-3" style="min-height:160px;">
                        <div class="badge-notice-wrapper position-absolute px-2 py-1 text-white rounded shadow-sm" style="top:10px; left:10px; background:#007AFF; cursor:pointer; font-size:0.65rem; z-index:10; font-weight:bold;">
                            THÔNG BÁO <i class="fas fa-bell ml-1"></i>
                        </div>
                        <img src="${p.productImage || '/img/Picture1.png'}" class="img-fluid rounded shadow-sm w-100" style="height: 120px; object-fit:cover;" onerror="this.src='/img/Picture1.png'" />
                    </div>

                    <!-- Middle Section: Main Info -->
                    <div class="col-md-6 p-3 border-right border-left">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <h5 class="text-primary font-weight-bold mb-0" style="font-size:1.1rem;">${storeName}</h5>
                            <div class="text-warning d-flex">
                                <i class="fas fa-star" style="font-size:0.8rem;"></i>
                                <i class="fas fa-star" style="font-size:0.8rem;"></i>
                                <i class="fas fa-star" style="font-size:0.8rem;"></i>
                                <i class="fas fa-star" style="font-size:0.8rem;"></i>
                                <i class="fas fa-star" style="font-size:0.8rem;"></i>
                            </div>
                        </div>
                        <h6 class="font-weight-bold mb-1 text-dark">Tuyến: ${p.productName}</h6>
                        <p class="text-muted small mb-3">${p.type} ${p.bienSoXe ? `• ${p.bienSoXe}` : ''}</p>
                        
                        <div class="row text-center no-gutters align-items-center">
                            <div class="col-4">
                                <div class="h5 mb-0 font-weight-bold">${startTimeStr}</div>
                                <div class="small text-muted text-truncate px-1">${p.startAddress}</div>
                            </div>
                            <div class="col-4 px-2">
                                <div class="d-flex flex-column align-items-center">
                                    <small class="text-primary font-weight-bold" style="font-size:0.7rem;">${travelTime}</small>
                                    <div class="w-100 position-relative my-1" style="height:2px; background:#dee2e6;">
                                        <div class="position-absolute" style="right:0; top:-4px; width:10px; height:10px; border-radius:50%; background:#2474E5; border:2px solid #fff;"></div>
                                        <div class="position-absolute" style="left:0; top:-4px; width:10px; height:10px; border-radius:50%; background:#fff; border:2px solid #2474E5;"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="h5 mb-0 font-weight-bold">${endTimeStr}</div>
                                <div class="small text-muted text-truncate px-1">${p.endAddress}</div>
                            </div>
                        </div>
                    </div>

                    <!-- Right Section: Price and Actions -->
                    <div class="col-md-3 p-3 d-flex flex-column justify-content-between text-right bg-white">
                        <div>
                            <div class="h4 text-primary font-weight-bold mb-0">${priceFormatted}</div>
                            <span class="badge badge-light text-success font-weight-bold py-1 px-2 mt-1">Giao dịch an toàn</span>
                        </div>
                        <div class="mt-2">
                            <p class="text-danger small font-weight-bold mb-1">Cần ${p.remainSeat} chỗ trống</p>
                            <p class="mb-2 small font-weight-bold text-muted"><i class="fas fa-phone-alt"></i> ${p.phoneNumber}</p>
                            <button class="btn btn-warning btn-block font-weight-bold btn-order-ticket shadow-sm py-2" data-target="#frmDatVe${productId}" style="border-radius:10px;">ĐẶT VÉ</button>
                            <a href="javascript:void(0)" class="info-button d-block mt-2 small text-primary text-center font-weight-bold" style="text-decoration:none;">
                                Chi tiết <i class="fas fa-caret-down"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Detail Tabs (Hidden by default) -->
            <div class="ticket-detail p-3 bg-light border-top" style="display:none;">
                <ul class="nav nav-pills nav-justified mb-3 ticket-tabs" style="gap:10px;">
                    <li class="nav-item"><button class="nav-link active tab-btn-img py-1 px-2 small">Hình ảnh</button></li>
                    <li class="nav-item"><button class="nav-link tab-btn-util py-1 px-2 small">Tiện ích</button></li>
                    <li class="nav-item"><button class="nav-link tab-btn-pos py-1 px-2 small">Điểm đón/trả</button></li>
                    <li class="nav-item"><button class="nav-link tab-btn-policy py-1 px-2 small">Chính sách</button></li>
                    <li class="nav-item"><button class="nav-link tab-btn-review py-1 px-2 small">Thông tin</button></li>
                </ul>
                <div class="tabs-content bg-white p-3 rounded shadow-sm" style="min-height:150px;">
                    <div class="tab-images row no-gutters" style="display:flex; margin:0 -5px;">
                         <div class="col-3 px-1"><img src="https://static.vexere.com/production/images/1691571338555.jpeg" class="img-fluid rounded shadow-sm" /></div>
                         <div class="col-3 px-1"><img src="https://static.vexere.com/production/images/1691571338859.jpeg" class="img-fluid rounded shadow-sm" /></div>
                         <div class="col-3 px-1"><img src="https://static.vexere.com/production/images/1691571339090.jpeg" class="img-fluid rounded shadow-sm" /></div>
                         <div class="col-3 px-1"><img src="https://static.vexere.com/production/images/1692701522841.jpeg" class="img-fluid rounded shadow-sm" /></div>
                    </div>
                    <div class="tab-positions" style="display:none">
                         <div class="alert alert-warning p-2 small mb-3"><i class="fas fa-info-circle"></i> Lịch này có thể thay đổi tùy tình hình thực tế</div>
                         <div id="stops${productId}" class="stops-list pl-3"></div>
                    </div>
                    <div class="tab-utils" style="display:none">${p.tienIch || 'Không có thông tin tiện ích'}</div>
                    <div class="tab-policy" style="display:none">${p.policy || 'Áp dụng chính sách mặc định của nhà xe'}</div>
                    <div class="tab-reviews" style="display:none">${p.description || p.productName}</div>
                </div>
            </div>

            <!-- Internal Notice List Overlay -->
            <div id="noticeList${productId}" class="notice-overlay position-absolute bg-white shadow-lg p-3 rounded" style="display:none; top:50px; left:20px; z-index:100; width:300px; border:2px solid #2474E5;">
                <h6 class="text-center font-weight-bold border-bottom pb-2 mb-2 text-primary">Thông báo từ nhà xe</h6>
                <div class="notices-scroll" style="max-height: 250px; overflow-y:auto;"></div>
            </div>
        </div>

        <!-- Order Form Modal (Outer) -->
        <div id="frmDatVe${productId}" class="frmDatVe-modal" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; z-index:2000; background:rgba(0,0,0,0.7); overflow-y:auto;">
            <div class="container py-5 d-flex justify-content-center">
                <form class="bg-white p-4 rounded shadow-lg w-100" style="max-width:550px;" action="/api/v1/user/${productId}/order" method="post">
                    <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                        <h4 class="mb-0 text-primary font-weight-bold">Đặt Vé Trực Tuyến</h4>
                        <button type="button" class="btn-dismiss-form border-0 bg-transparent h3 m-0" style="line-height:1;">&times;</button>
                    </div>
                    
                    <div class="row mb-4 bg-light p-3 rounded mx-0">
                        <div class="col-12"><p class="mb-1 text-dark"><strong>Tuyến:</strong> ${p.productName}</p></div>
                        <div class="col-6"><p class="mb-0 small text-muted"><strong>Nhà xe:</strong> ${storeName}</p></div>
                        <div class="col-6 text-right"><p class="mb-0 text-primary font-weight-bold">Giá: ${priceFormatted}</p></div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3"><label class="small font-weight-bold text-muted">ĐIỂM ĐÓN</label><input type="text" class="form-control" name="pickUpAddress" required placeholder="Địa chỉ đón" /></div>
                        <div class="col-md-6 mb-3"><label class="small font-weight-bold text-muted">ĐIỂM TRẢ</label><input type="text" class="form-control" name="destinationAddress" required placeholder="Địa chỉ trả" /></div>
                        <div class="col-12 mb-3"><label class="small font-weight-bold text-muted">THỜI GIAN ĐÓN</label><input type="datetime-local" class="form-control" name="pickTime" required /></div>
                        <div class="col-md-6 mb-3"><label class="small font-weight-bold text-muted">SỐ ĐIỆN THOẠI</label><input type="number" class="form-control" name="phoneNumber" required /></div>
                        <div class="col-md-6 mb-3"><label class="small font-weight-bold text-muted">SỐ LƯỢNG VÉ</label><input type="number" class="form-control" name="quantity" value="1" min="1" required /></div>
                        <div class="col-12 mb-4"><label class="small font-weight-bold text-muted">GHI CHÚ</label><textarea class="form-control" name="message" rows="2" placeholder="Yêu cầu thêm..."></textarea></div>
                    </div>
                    
                    <div class="alert alert-info py-2 small border-0 shadow-sm mb-4"><i class="fas fa-info-circle"></i> Thanh toán sau khi lên xe. Tài xế sẽ gọi xác nhận.</div>

                    <div class="row">
                        <div class="col-6 px-1"><button type="submit" class="btn btn-primary btn-block btn-lg font-weight-bold shadow-sm">XÁC NHẬN</button></div>
                        <div class="col-6 px-1"><button type="button" class="btn btn-outline-secondary btn-block btn-lg btn-dismiss-form">ĐÓNG</button></div>
                    </div>
                </form>
            </div>
        </div>
    `;

    const productDiv = $(html);

    // Render stops in detail
    const stopsList = productDiv.find(`#stops${productId}`);
    if (p.stopList && p.stopList.length > 0) {
        const list = $('<div class="border-left pl-3 py-1 position-relative" style="border-width:2px !important; border-color:#2474E5 !important;"></div>');
        p.stopList.forEach((s, idx) => {
            list.append(`
                <div class="mb-2 position-relative">
                    <span class="position-absolute" style="left:-21px; top:6px; width:12px; height:12px; background:${s.rightNow ? '#dc3545' : '#2474E5'}; border:2px solid #fff; border-radius:50%; box-shadow:0 0 0 1px #2474E5;"></span>
                    <strong class="${s.rightNow ? 'text-danger' : 'text-dark'}">${formatTime(s.stopTime)}</strong> - ${s.stopAddress}
                    ${s.rightNow ? '<span class="badge badge-danger ml-2">Vị trí hiện tại</span>' : ''}
                </div>
            `);
        });
        stopsList.append(list);
    }

    // Render notices in ticket list
    const noticesScroll = productDiv.find(`#noticeList${productId} .notices-scroll`);
    if (p.noticeList && p.noticeList.length > 0) {
        p.noticeList.slice(-5).forEach(n => {
            const time = moment(n.lastUpdate).format('HH:mm DD/MM');
            noticesScroll.append(`
                <div class="p-2 border-bottom small bg-light rounded mb-2 mx-1 shadow-sm">
                    <div class="d-flex justify-content-between mb-1">
                        <strong class="text-primary" style="font-size:0.7rem;">${n.storeName}</strong>
                        <small class="text-muted" style="font-size:0.65rem;">${time}</small>
                    </div>
                    <p class="mb-0 font-weight-bold text-danger" style="font-size:0.75rem;">${n.title}</p>
                    <p class="mb-0 text-muted" style="font-size:0.7rem;">${n.content}</p>
                </div>
            `);
        });
    } else {
        noticesScroll.html('<p class="text-center text-muted small mt-2">Chưa có thông báo gần đây.</p>');
    }

    return productDiv;
}

function displayPagination() {
    const totalPages = Math.ceil(products.length / pageSize);
    const container = $('#pagination');
    if (!container.length) return;
    container.empty();

    const group = $('<div class="btn-group shadow-sm"></div>');

    const prev = $(`<button class="btn btn-outline-primary ${currentPage === 0 ? 'disabled' : ''}"><i class="fas fa-chevron-left"></i></button>`)
        .on('click', () => currentPage > 0 && changePage(currentPage - 1));

    for (let i = 0; i < totalPages; i++) {
        const btn = $(`<button class="btn ${i === currentPage ? 'btn-primary active' : 'btn-outline-primary'}">${i + 1}</button>`)
            .on('click', () => changePage(i));
        group.append(btn);
    }

    const next = $(`<button class="btn btn-outline-primary ${currentPage === totalPages - 1 ? 'disabled' : ''}"><i class="fas fa-chevron-right"></i></button>`)
        .on('click', () => currentPage < totalPages - 1 && changePage(currentPage + 1));

    container.append(group);
}

function changePage(page) {
    if (page === currentPage) return;
    displayProducts(page);
    displayPagination();
    window.scrollTo({ top: $('#productList').offset().top - 100, behavior: 'smooth' });
}

function initWithProducts(data) {
    products = data;
    displayProducts(0);
    if (products.length > pageSize) displayPagination();
}
