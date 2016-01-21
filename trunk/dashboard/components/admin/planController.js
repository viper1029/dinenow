angular.module("app.admin.plan.list", []).controller("AdminPlanController", function($scope, $modal, $state, $stateParams, ngTableConfig, adminPlanResource) {
    $scope.initList = function() {
        var restaurantID = $stateParams.restaurantId;
        $scope.isLoadingList = !0, setTimeout(function() {
            adminPlanResource.get(restaurantID).success(function(payload) {
                $scope.listPlans = payload.data, $scope.tableParams = ngTableConfig.config($scope.listPlans), $scope.isLoadingList = !1
            })
        }, 200)
    }, $scope.initList(), $scope.create = function() {
        var modalInstance = $modal.open({
            templateUrl: "scripts/admin/plan/views/modal/plan-create.html",
            controller: function($scope, $modalInstance, adminPlanResource) {
                $scope.itemToProcess = {
                    currency: "usd",
                    trialPeriodDays: 0
                }, $scope.enumTypes = {
                    interval: ["day", "week", "month", "year"]
                }, $scope.save = function() {
                    $scope.isLoadingSave = !0;
                    var genID = function() {
                            return Math.random().toString(36).substr(2, 16)
                        },
                        objectModel = angular.copy($scope.itemToProcess);
                    objectModel.id = genID(), objectModel.amount = 100 * objectModel.amount, objectModel.trialPeriodDays || (objectModel.trialPeriodDays = 0), setTimeout(function() {
                        adminPlanResource.post(objectModel).success(function() {
                            $scope.isLoadingSave = !1, $modalInstance.close(objectModel)
                        })
                    }, 300)
                }, $scope.cancel = function() {
                    $modalInstance.dismiss("cancel")
                }
            }
        });
        modalInstance.result.then(function() {
            alertify.success("Created Successfully"), $state.go($state.current, {}, {
                reload: !0
            })
        })
    }, $scope.edit = function(item) {
        var modalInstance = $modal.open({
            templateUrl: "scripts/admin/plan/views/modal/plan-edit.html",
            resolve: {
                itemToProcess: function() {
                    return item
                }
            },
            controller: function($scope, $modalInstance, adminPlanResource, itemToProcess) {
                $scope.itemToProcess = angular.copy(itemToProcess), $scope.itemToProcess.amount = $scope.itemToProcess.amount / 100, $scope.enumTypes = {
                    interval: ["day", "week", "month", "year"]
                }, $scope.save = function() {
                    $scope.isLoadingSave = !0, setTimeout(function() {
                        adminPlanResource.put($scope.itemToProcess).success(function() {
                            $scope.isLoadingSave = !1, $modalInstance.close($scope.itemToProcess)
                        })
                    }, 300)
                }, $scope.cancel = function() {
                    $modalInstance.dismiss("cancel")
                }
            }
        });
        modalInstance.result.then(function() {
            alertify.success("Updated Successfully"), $state.go($state.current, {}, {
                reload: !0
            })
        })
    }, $scope["delete"] = function(item) {
        alertify.confirm("Are you sure you want to delete?", function(e) {
            e && adminPlanResource["delete"](item).success(function() {
                $state.go($state.current, {}, {
                    reload: !0
                }), alertify.success("Delete Successfully")
            }).error(function() {
                alertify.error("Oop! Please try again")
            })
        })
    }
});