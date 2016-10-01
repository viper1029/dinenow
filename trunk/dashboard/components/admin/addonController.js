angular.module("app.admin.restaurant.manage.menu.addon", [])
    .controller("AdminAddonController", ["$scope", "$modal", "$state", "$stateParams",
        "$timeout", "ngTableConfig", "adminAddonResource", "adminSizesResource",
        function ($scope, $modal, $state, $stateParams, $timeout, ngTableConfig, adminAddonResource) {

            var initAddons = function () {
                var restaurantID = $stateParams.restaurantId;
                adminAddonResource.getAll(restaurantID).success(function (payload) {
                    $scope.addons = payload.data.addons;
                    $scope.tableParams = ngTableConfig.config($scope.addons)
                })
            };

            var open = function (item) {
                var modalInstance = $modal.open({
                    templateUrl: "scripts/admin/restaurant/manage/menu/views/modal/addon.html",
                    resolve: {
                        itemToProcess: function () {
                            return item
                        }
                    },
                    controller: function ($scope, $modalInstance, adminAddonResource, adminSizesResource, itemToProcess) {
                        var init = function () {
                            $scope.onShow = {
                                size: !1
                            };

                            $scope.itemToProcess = angular.copy(itemToProcess) || {};
                            var restaurantID = $stateParams.restaurantId;

                            var loadAddonDetails = function () {
                                    adminAddonResource.getByID($scope.itemToProcess).success(function (payload) {
                                        $scope.itemToProcess = payload.data.addon;
                                    });
                            }
                        };

                        init();
                        $scope.save = function () {
                            if(($scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $stateParams.restaurantId || ""))) {
                                adminAddonResource.addOrUpdate($scope.itemToProcess).success(function () {
                                        $modalInstance.close($scope.itemToProcess)
                                    }).error(function () {
                                        alertify.error("Oops! Please try again")
                                    })
                            }
                            else {
                             alertify.error("Please add size and price")
                            }
                        };

                        $scope.cancel = function () {
                            $modalInstance.dismiss("cancel")
                        };
                    }
                });
                modalInstance.result.then(function (data) {
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
            };

            var deleteAddon = function (item) {
                alertify.confirm("Are you sure you want to delete?", function (e) {
                    e && adminAddonResource["delete"](item).success(function () {
                        $state.go($state.current, {}, {
                            reload: !0
                        });
                        alertify.success("Delete Successfully")
                    }).error(function () {
                        alertify.error("Oop! Please try again")
                    })
                })
            };

            initAddons();
            $scope.open = open;
            $scope.deleteAddon = deleteAddon;
        }]);