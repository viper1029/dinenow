angular.module("app.admin.restaurant.manage.menu.addon", [])
    .controller("AdminAddonController", ["$scope", "$modal", "$state", "$stateParams",
        "$timeout", "ngTableConfig", "adminAddonResource", "adminSizesResource", function($scope, $modal, $state, $stateParams, $timeout, ngTableConfig, adminAddonResource) {
            $scope.initList = function() {
                var restaurantID = $stateParams.restaurantId;
                adminAddonResource.get(restaurantID).success(function(payload) {
                    $scope.addons = payload.data.addons, $scope.tableParams = ngTableConfig.config($scope.addons)
                })
            }, $scope.initList(),
                $scope.open = function(item) {
                    console.log(item);
                    var modalInstance = $modal.open({
                        templateUrl: "scripts/admin/restaurant/manage/menu/views/modal/addon.html",
                        resolve: {
                            itemToProcess: function() {
                                return item
                            }
                        },
                        controller: function($scope, $modalInstance, adminAddonResource, adminSizesResource, itemToProcess) {
                            $scope.init = function() {
                                $scope.onShow = {
                                    size: !1
                                }, $scope.itemToProcess = angular.copy(itemToProcess) || {};
                                console.log($scope.itemToProcess);
                                var restaurantID = $stateParams.restaurantId;
                                setTimeout(function(){
                                    adminSizesResource.get(restaurantID).success(function(payload) {
                                        $scope.listSizes = payload.data.sizes
                                        console.log($scope.listSizes);
                                    })
                                },200),

                                    $scope.itemToProcess.id ? setTimeout(function() {
                                        adminAddonResource.getByID($scope.itemToProcess).success(function(payload) {
                                            console.log(payload);
                                            $scope.itemToProcess = payload.data.addons,
                                                console.log($scope.itemToProcess);
                                            $scope.itemToProcess.sizePrices.push({
                                                size: "",
                                                price: 0
                                            })
                                        })
                                    }, 100) : $scope.itemToProcess = {
                                        availabilityStatus: "AVAILABLE",
                                        sizePrices: [{
                                            size: "",
                                            price: 0
                                        }]
                                    }
                            },
                                $scope.init(),
                                $scope.addSizePrice = function() {
                                    $scope.itemToProcess.sizePrices.push({
                                        size: "",
                                        price: 0
                                    })
                                }, $scope.deleteSizePrice = function(index) {
                                $scope.itemToProcess.sizePrices.splice(index, 1)
                            }, $scope.save = function() {
                                $scope.itemToProcess.sizePrices.length > 1 ? ($scope.itemToProcess.sizePrices.splice(-1, 1), $scope.itemToProcess.sizePrices.length > 0 && ($scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $stateParams.restaurantId || ""), adminAddonResource.addOrUpdate($scope.itemToProcess).success(function() {
                                    $modalInstance.close($scope.itemToProcess)
                                }).error(function() {
                                    alertify.error("Oops! Please try again")
                                }))) : alertify.error("Please add size and price")
                            }, $scope.cancel = function() {
                                $modalInstance.dismiss("cancel")
                            }
                        }
                    });
                    modalInstance.result.then(function(data) {
                        var isMatchId = !1;
                        if (data) {
                            for (var i = 0; i < $scope.addons.length; i++)
                                if ($scope.addons[i].id == data.id) {
                                    isMatchId = !0, $scope.addons[i] = data, alertify.success("Updated Successfully"), $state.go($state.current, {}, {
                                        reload: !0
                                    });
                                    break
                                }
                            isMatchId || (alertify.success("Created Successfully"), $state.go($state.current, {}, {
                                reload: !0
                            }))
                        }
                    })
                }, $scope["delete"] = function(item) {
                alertify.confirm("Are you sure you want to delete?", function(e) {
                    e && adminAddonResource["delete"](item).success(function() {
                        $state.go($state.current, {}, {
                            reload: !0
                        }), alertify.success("Delete Successfully")
                    }).error(function() {
                        alertify.error("Oop! Please try again")
                    })
                })
            }
        }]);