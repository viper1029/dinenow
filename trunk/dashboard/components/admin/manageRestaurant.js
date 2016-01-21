angular.module("app.admin.restaurant.manage", ["app.admin.restaurant.manage.menu", "app.admin.restaurant.manage.restaurant", "app.admin.restaurant.manage.orders"])
    .controller("RestaurantManageController", function($scope, $state, restaurantsResource, globalHandleUI) {
        globalHandleUI.handleAccordionMenu(), globalHandleUI.handleToggleLeftBar(), $scope.$state = $state, $scope.init = function() {
            var itemId = $state.params.restaurantId;
            restaurantsResource.getById(itemId).success(function(payload) {
                $scope.restaurant = payload.data
            }).error(function() {
                alertify.error("Oops! Please try again")
            })
        }, $scope.init()
        console.log("test");
    });