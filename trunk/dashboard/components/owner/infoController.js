angular.module("app.owner.restaurant.info", [])
    .controller("InfoController", ["$scope", "$rootScope", "$state", "infoResource", "globalHandleUI", function($scope, $rootScope, $state, infoResource, globalHandleUI) {
        globalHandleUI.handleToggleLeftBar(), $scope.init = function() {
            $scope.restaurantInfo = $scope.restaurantInfo || {},
                infoResource.get().success(function(payload) {
                    console.log(payload.data);
                    $scope.restaurantInfo = payload.data.restaurant,
                        $scope.tempRestaurantInfo = angular.copy($scope.restaurantInfo),
                        $rootScope.isAccept = {
                            delivery: $scope.restaurantInfo.acceptDelivery,
                            takeout: $scope.restaurantInfo.acceptTakeOut
                        }
                })
        }, $scope.init(), $scope.save = function(params) {
            $scope.saveLoading = !0, setTimeout(function() {
                infoResource.update(params).success(function() {
                    $rootScope.isAccept = {
                        delivery: params.acceptDelivery,
                        takeout: params.acceptTakeOut
                    }, alertify.success("Updated successfully"), $scope.saveLoading = !1
                }).error(function() {})
            }, 300)
        }, $scope.cancel = function() {
            $scope.restaurantInfo = angular.copy($scope.tempRestaurantInfo)
        }
    }]);