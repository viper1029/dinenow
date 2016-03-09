angular.module("app.admin.restaurant.manage.menu.categories", [])
    .controller("AdminCategoriesController", ["$scope", "$filter", "$modal", "$state", "$stateParams",
        "ngTableConfig", "adminCategoriesResource",
        function ($scope, $filter, $modal, $state, $stateParams, ngTableConfig, adminCategoriesResource) {

            var init = function () {
                var restaurantID = $stateParams.restaurantId;
                adminCategoriesResource.getAll(restaurantID).success(function (payload) {
                    $scope.categories = payload.data.categories;
                    $scope.tableParams = ngTableConfig.config($scope.categories)
                })
            };

            var open = function (item) {
                var modalInstance = $modal.open({
                    templateUrl: "scripts/admin/restaurant/manage/menu/views/modal/categories.html",
                    resolve: {
                        itemToProcess: function () {
                            return item
                        }
                    },
                    controller: function ($scope, $modalInstance, adminCategoriesResource, itemToProcess) {
                        $scope.itemToProcess = angular.copy(itemToProcess) || {};
                        $scope.save = function () {
                            $scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $stateParams.restaurantId);
                            adminCategoriesResource.addOrUpdate($scope.itemToProcess).success(function () {
                                $modalInstance.close($scope.itemToProcess)
                            }).error(function () {
                                alertify.error("Oops! Please try again")
                            })
                        };

                        $scope.cancel = function () {
                            $modalInstance.dismiss("cancel")
                        };
                    }
                });
                modalInstance.result.then(function (data) {
                    data && (alertify.success("Updated Successfully."), $state.go($state.current, {}, {
                        reload: !0
                    }))
                })
            };

            var deleteCategory = function (item) {
                alertify.confirm("Are you sure you want to delete?", function (e) {
                    e && adminCategoriesResource.delete(item).success(function () {
                        $state.go($state.current, {}, {
                            reload: !0
                        });
                        alertify.success("Deleted successfully")
                    }).error(function () {
                        alertify.error("Oops! This category is used in a Menu. Please remove it before deleting.")
                    })
                })
            };

            init();
            $scope.open = open;
            $scope.delete = deleteCategory;
        }]);