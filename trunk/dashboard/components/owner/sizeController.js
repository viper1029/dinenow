angular.module("app.owner.menu.sizes", [])
    .controller("SizesController", ["$rootScope", "$scope", "$modal", "$state", "ngTableConfig", "sizesResource", function($rootScope, $scope, $modal, $state, ngTableConfig, sizesResource) {
        $scope.init = function() {
            sizesResource.get().success(function(payload) {
                $scope.sizes = payload.data.sizes, $scope.tableParams = ngTableConfig.config($scope.sizes)
            })
        }, $scope.init(), $scope.open = function(item) {
            var modalInstance = $modal.open({
                templateUrl: "scripts/owner/menu/views/modal/sizes.html",
                resolve: {
                    itemToProcess: function() {
                        return item
                    }
                },
                controller: function($scope, $modalInstance, sizesResource, itemToProcess) {
                    $scope.itemToProcess = angular.copy(itemToProcess) || {}, $scope.save = function() {
                        $scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $rootScope.restaurantID), sizesResource.addOrUpdate($scope.itemToProcess).success(function() {
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
        }, $scope["delete"] = function(item) {
            alertify.confirm("Are you sure you want to delete?", function(e) {
                e && sizesResource["delete"](item).success(function() {
                    $state.go($state.current, {}, {
                        reload: !0
                    }), alertify.success("Delete Successfully")
                }).error(function() {
                    alertify.error("Oop! Please try again")
                })
            })
        }
    }]);