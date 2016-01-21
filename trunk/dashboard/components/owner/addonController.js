
angular.module("app.owner.menu.addon", [])
    .controller("AddonController", ["$rootScope", "$scope", "$modal", "$state", "$timeout", "ngTableConfig", "addonResource", "sizesResource", function($rootScope, $scope, $modal, $state, $timeout, ngTableConfig, addonResource) {
        $scope.initList = function() {
            addonResource.get().success(function(payload) {
                $scope.addons = payload.data.addons, $scope.tableParams = ngTableConfig.config($scope.addons)
            })
        }, $scope.initList(), $scope.open = function(item) {
            var modalInstance = $modal.open({
                templateUrl: "scripts/owner/menu/views/modal/addon.html",
                resolve: {
                    itemToProcess: function() {
                        return item
                    }
                },
                controller: function($scope, $modalInstance, addonResource, sizesResource, itemToProcess) {
                    $scope.init = function() {
                        $scope.onShow = {
                            size: !1
                        }, $scope.itemToProcess = angular.copy(itemToProcess) || {}, sizesResource.get().success(function(payload) {
                            $scope.listSizes = payload.data.sizes
                        }), $scope.itemToProcess.id ? setTimeout(function() {
                            addonResource.getByID($scope.itemToProcess).success(function(payload) {
                                $scope.itemToProcess = payload.data.addons, $scope.itemToProcess.sizePrices.push({
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
                    }, $scope.init(), $scope.addSizePrice = function() {
                        $scope.itemToProcess.sizePrices.push({
                            size: "",
                            price: 0
                        })
                    }, $scope.deleteSizePrice = function(index) {
                        $scope.itemToProcess.sizePrices.splice(index, 1)
                    }, $scope.save = function() {
                        $scope.itemToProcess.sizePrices.length > 1 ? ($scope.itemToProcess.sizePrices.splice(-1, 1), $scope.itemToProcess.sizePrices.length > 0 && ($scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $rootScope.restaurantID || ""),
                            addonResource.addOrUpdate($scope.itemToProcess).success(function() {
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
                alertify.success(data.id ? "Updated Successfully" : "Created Successfully"), $state.go($state.current, {}, {
                    reload: !0
                })
            })
        }, $scope["delete"] = function(item) {
            alertify.confirm("Are you sure you want to delete?", function(e) {
                e && addonResource["delete"](item).success(function() {
                    $state.go($state.current, {}, {
                        reload: !0
                    }), alertify.success("Delete Successfully")
                }).error(function() {
                    alertify.error("Oop! Please try again")
                })
            })
        }
    }]);