angular.module("app.admin.restaurant.manage.restaurant.info", [])
    .controller("AdminInfoController", function($scope, $rootScope, $state, $stateParams, adminInfoResource, adminTaxesResource, globalHandleUI) {
        globalHandleUI.handleToggleLeftBar(), $scope.init = function() {
            $scope.restaurantInfo = $scope.restaurantInfo || {};
            var restaurantID = $stateParams.restaurantId;
            adminInfoResource.get(restaurantID).success(function(payload) {
                $scope.restaurantInfo = payload.data.restaurant,
                    $scope.tempRestaurantInfo = angular.copy($scope.restaurantInfo),
                    $rootScope.isAccept = {
                        delivery: $scope.restaurantInfo.acceptDelivery,
                        takeout: $scope.restaurantInfo.acceptTakeOut
                    }, adminTaxesResource.get(restaurantID).success(function(payload) {
                    $scope.listTaxes = payload.data.taxes
                    console.log($scope.listTaxes);
                })
            })
        }, $scope.init(), $scope.save = function(params) {
            $scope.saveLoading = !0, params.tax = params.tax.id,
                setTimeout(function() {
                    adminInfoResource.update(params).success(function(payload) {
                        $rootScope.isAccept = {
                            delivery: params.acceptDelivery,
                            takeout: params.acceptTakeOut
                        }, params.tax = payload.data.tax, alertify.success("Updated successfully"), $scope.saveLoading = !1
                    }).error(function() {})
                }, 300)
        }, $scope.cancel = function() {
            $scope.restaurantInfo = angular.copy($scope.tempRestaurantInfo)
        }
    });