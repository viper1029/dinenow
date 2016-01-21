angular.module("app.admin.restaurant.manage.restaurant.subscriptions", [])
    .controller("AdminSubscriptionsController", function($scope, $rootScope, $modal, $state, $stateParams, ngTableConfig, adminSubscriptionResource) {
        $scope.getSubscriptions = function() {
            setTimeout(function() {
                var restaurantID = $stateParams.restaurantId;
                adminSubscriptionResource.getSubscriptions(restaurantID).success(function(payload) {
                    $scope.subscriptions = payload.data, $scope.isLoadingSubscriptions = !1
                }).error(function() {})
            }, 200)
        }, $scope.init = function() {
            $scope.isLoadingSubscriptions = !0;
            var checkAssigned = function(assignedPlan, listPlans) {
                for (var i = 0; i < listPlans.length; i++)
                    if (listPlans[i].id === assignedPlan.id) return assignedPlan.id
            };
            $scope.isLoadingList = !0, setTimeout(function() {
                adminSubscriptionResource.getPlan().success(function(payload) {
                    $scope.plans = payload.data;
                    var restaurantID = $stateParams.restaurantId;
                    adminSubscriptionResource.getPlanAssigned(restaurantID).success(function(payload) {
                        $scope.itemAssigned = checkAssigned(payload.data, $scope.plans) || !1, $scope.isLoadingList = !1
                    }).error(function() {
                        $scope.isLoadingList = !1
                    }), $scope.getSubscriptions()
                })
            }, 200)
        }, $scope.init(), $scope.onCancelSubscription = function(item) {
            var restaurantID = $stateParams.restaurantId;
            alertify.confirm("Are you sure you want to CANCEL this subscription ?", function(e) {
                e && adminSubscriptionResource.cancelSubscription(restaurantID, item.id).success(function() {
                    alertify.success("Cancel Successfully."), $state.go($state.current, {}, {
                        reload: !0
                    })
                }).error(function() {
                    alertify.error("Oops! Something went wrong.")
                })
            })
        }, $scope.onAssign = function(item) {
            if ($scope.subscriptions.length > 0) alertify.alert("This restaurant was subscribed a plan! Please cancel it before assign another plan.");
            else {
                var restaurantID = $stateParams.restaurantId,
                    objectModel = {};
                objectModel.planStripe = item.id, item.isLoadingAssign = !0, setTimeout(function() {
                    adminSubscriptionResource.assignPlan(restaurantID, objectModel).success(function(payload) {
                        alertify.success("Assigned Plan Successfully"), item.isLoadingAssign = !1, $scope.itemAssigned = payload.data.id || !1
                    })
                }, 300)
            }
        }
    });