angular.module("app.admin.restaurant.manage.orders",
    ["common.directives", "app.admin.restaurant.manage.orders.services"])
    .controller("AdminOrdersController", function($scope, $state, $stateParams, $modal, $timeout,
                                                  adminOrdersResource, globalHandleUI) {
//    $scope.restaurantID = $stateParams.restaurantId,
        $scope.init = function() {
            $scope.restaurantID = $stateParams.restaurantId,
                console.log("order restaurant");
            $scope.page = 1,
                $scope.records = 10, $scope.isScrollEnd = !0,
                $scope.daterange = {
                    startDate: moment().hour(0).minute(0).second(0).format("YYYY-MM-DD HH:mm:ss"),
                    endDate: moment().hour(23).minute(59).second(59).format("YYYY-MM-DD HH:mm:ss")
                },
                $scope.renderDateRange = {
                    from: moment($scope.daterange.startDate).format("YYYY-MM-DD"),
                    to: moment($scope.daterange.endDate).format("YYYY-MM-DD")
                }, $scope.daterangeOptions = {
                ranges: {
                    Today: [moment(), moment()],
                    Yesterday: [moment().subtract(1, "days"), moment().subtract(1, "days")],
                    "Last 7 Days": [moment().subtract(6, "days"), moment()],
                    "Last 30 Days": [moment().subtract(29, "days"), moment()],
                    "This Month": [moment().startOf("month"), moment().endOf("month")],
                    "Last Month": [moment().subtract(1, "month").startOf("month"), moment().subtract(1, "month").endOf("month")]
                }
            }, $scope.initLazyLoad = function(page, size) {
                var params = "?page=" + page + "&size=" + size;
                return params
            }, $scope.loadMoreOrders = function() {
                var params = $scope.initLazyLoad($scope.page, $scope.records),
                    data = [];

                $scope.isScrollEnd && adminOrdersResource.get($scope.restaurantID, params).success(function(payload) {
                    data = payload.data.orders, isLoad = !1,
                        $scope.orders = $scope.orders.concat(data)
                })
            }, $scope.countByStatus = function(listOrder) {
                if ($scope.status = {
                        open: 0,
                        late: 0
                    }, listOrder)
                    for (var i = 0; i < listOrder.length; i++) "OPEN" == listOrder[i].orderStatus && $scope.status.open++, "LATE" == listOrder[i].orderStatus && $scope.status.late++
            }, $scope.getTotalPrice = function(item) {
                for (var totalPrice = 0, i = 0; i < item.orderDetails.length; i++)
                    totalPrice += item.orderDetails[i].quantity * item.orderDetails[i].unitPrice;
                return totalPrice
            }, $scope.getAllOrder = function() {

                adminOrdersResource.get($scope.restaurantID, "").success(function(payload) {
                    console.log(payload.data.orders);
                    $scope.orders = payload.data.orders,
                        $scope.countByStatus($scope.orders)
                })

            }, $scope.statusFilter = function(status) {

                console.log(status);
                globalHandleUI.handleHighlightFitler(status),
                    $scope.status = {
                        open: 0,
                        late: 0
                    };

                var params = "";
                status && (params = "?status=" + status),
                    console.log("dine");
                console.log(params);
                adminOrdersResource.get($scope.restaurantID, params).success(function(payload) {
                    console.log(payload);
                    $scope.orders = payload.data.orders,
                        console.log($scope.orders);
                    "" === params && $scope.countByStatus($scope.orders)
                })
            }, $scope.daterangeFilter = function(startDate, endDate) {
                var params = "?from=" + startDate + "&to=" + endDate;
                console.log($scope.restaurantID);
                adminOrdersResource.get($scope.restaurantID, params).success(function(payload) {
                    $scope.orders = payload.data.orders,
                        $scope.countByStatus($scope.orders)
                })
            },

                $scope.daterangeFilter($scope.daterange.startDate, $scope.daterange.endDate)
        }, $scope.init(),
            $scope.handleTableScroll = function() {
                var maxRows = 20,
                    table = document.getElementById("order-table"),
                    wrapper = table.parentNode,
                    rowsInTable = table.rows.length,
                    height = 0;
                if (rowsInTable > maxRows) {
                    for (var i = 0; maxRows > i; i++) height += table.rows[i].clientHeight;
                    wrapper.style.height = height + "px"
                }
            }, $timeout($scope.handleTableScroll, 200), $scope.viewDetail = function(item) {
            var modalInstance = $modal.open({
                templateUrl: "scripts/admin/restaurant/manage/order/views/modal/order.html",
                resolve: {
                    itemToProcess: function() {
                        return item
                    }
                },
                size: "lg",
                controller: function($scope, $modalInstance, adminOrdersResource, itemToProcess) {
                    $scope.init = function() {
                        $scope.itemToProcess = angular.copy(itemToProcess) || {}
                    }, $scope.init(), $scope.getTotal = function() {
                        for (var orders = $scope.itemToProcess.orderDetails, total = 0, i = 0; i < orders.length; i++) total += orders[i].quantity * orders[i].unitPrice;
                        return total
                    }, $scope.sendOrderByMail = function() {
                        alertify.alert("Coming Soon...")
                    }, $scope.acceptOrder = function(item) {
                        $scope.tempItem = angular.copy(item),
                            $scope.tempItem.orderStatus = "ACCEPTED",
                            delete $scope.tempItem.customer, delete $scope.tempItem.orderDetails, $scope.acceptLoading = !0, setTimeout(function() {
                            adminOrdersResource.addOrUpdate($scope.tempItem).success(function() {
                                $modalInstance.close()
                            })
                        }, 300)
                    }, $scope.cancel = function() {
                        $modalInstance.dismiss("cancel")
                    }
                }
            });
            modalInstance.result.then(function(data) {
                var isMatchId = !1;
                if (data) {
                    for (var i = 0; i < $scope.orders.length; i++)
                        if ($scope.orders[i].id == data.id) {
                            isMatchId = !0, $scope.orders[i] = data,
                                alertify.success("Updated Successfully"), $state.go($state.current, {}, {
                                reload: !0
                            });
                            break
                        }
                    isMatchId || (alertify.success("Created Successfully"), $state.go($state.current, {}, {
                        reload: !0
                    }))
                }
            })
        }
    });