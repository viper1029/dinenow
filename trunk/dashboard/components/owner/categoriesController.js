angular.module("app.owner.menu.categories", [])
    .controller("CategoriesController", ["$rootScope", "$scope", "$filter", "$modal", "$state", "ngTableConfig", "categoriesResource", function($rootScope, $scope, $filter, $modal, $state, ngTableConfig, categoriesResource) {
        $scope.initList = function() {
            $scope.defaults = categoriesResource.defaults,
                categoriesResource.get().success(function(payload) {
                    $scope.categories = payload.data.categories, $scope.tableParams = ngTableConfig.config($scope.categories)
                })
        }, $scope.initList(), $scope.open = function(item) {
            var modalInstance = $modal.open({
                templateUrl: "scripts/owner/menu/views/modal/categories.html",
                resolve: {
                    itemToProcess: function() {
                        return item
                    }
                },
                controller: function($scope, $modalInstance, categoriesResource, itemToProcess) {
                    $scope.itemToProcess = angular.copy(itemToProcess) || {}, $scope.save = function() {
                        $scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $rootScope.restaurantID || ""),
                            categoriesResource.addOrUpdate($scope.itemToProcess).success(function() {
                                $modalInstance.close($scope.itemToProcess)
                            }).error(function() {
                                alertify.error("Oops! Please try again")
                            })
                    }, $scope.cancel = function() {
                        $modalInstance.dismiss("cancel")
                    }
                }
            });
            modalInstance.result.then(function(data) {
                var isMatchId = !1;
                if (data) {
                    for (var i = 0; i < $scope.categories.length; i++)
                        if ($scope.categories[i].id == data.id) {
                            isMatchId = !0, $scope.categories[i] = data, alertify.success("Updated Successfully"), $state.go($state.current, {}, {
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
                e && categoriesResource["delete"](item).success(function() {
                    $state.go($state.current, {}, {
                        reload: !0
                    }), alertify.success("Deleted successfully")
                }).error(function() {
                    alertify.error("Oops! This category was used on Sub-Menu or Menu. Please check it again before deleting.")
                })
            })
        }
    }]);