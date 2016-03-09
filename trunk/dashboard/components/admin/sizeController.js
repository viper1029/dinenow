angular.module("app.admin.restaurant.manage.menu.sizes", [])
    .controller("AdminSizesController", ["$scope", "$modal", "$state",
        "$stateParams", "ngTableConfig", "adminSizesResource", "sizesResource",
        function ($scope, $modal, $state, $stateParams, ngTableConfig, adminSizesResource, sizesResource) {

            var init = function () {
                var restaurantID = $stateParams.restaurantId;
                adminSizesResource.getAll(restaurantID).success(function (payload) {
                    $scope.sizes = payload.data.sizes;
                    $scope.tableParams = ngTableConfig.config($scope.sizes)
                })
            };

            var open = function (item) {
                var modalInstance = $modal.open({
                    templateUrl: "scripts/owner/menu/views/modal/sizes.html",
                    resolve: {
                        itemToProcess: function () {
                            return item
                        }
                    },
                    controller: function ($scope, $modalInstance, sizesResource, itemToProcess) {
                        $scope.itemToProcess = angular.copy(itemToProcess) || {};
                        $scope.save = function () {
                            $scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $stateParams.restaurantId);
                            sizesResource.addOrUpdate($scope.itemToProcess).success(function () {
                                $modalInstance.close($scope.itemToProcess)
                            }).error(function () {
                                alertify.error("Oops! Please try again")
                            })
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss("cancel")
                        }
                    }
                });
                modalInstance.result.then(function (data) {
                    var isMatchId = !1;
                    if (data) {
                        for (var i = 0; i < $scope.sizes.length; i++)
                            if ($scope.sizes[i].id == data.id) {
                                isMatchId = !0, $scope.sizes[i] = data, alertify.success("Updated Successfully"), $state.go($state.current, {}, {
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

            var deleteSize = function (item) {
                alertify.confirm("Are you sure you want to delete?", function (e) {
                    e && sizesResource.delete(item).success(function () {
                        $state.go($state.current, {}, {
                            reload: !0
                        });
                        alertify.success("Delete Successfully")
                    }).error(function () {
                        alertify.error("Oop! Please try again")
                    })
                })
            };

            init();
            $scope.open = open;
            $scope.deleteSize = deleteSize;
        }]);