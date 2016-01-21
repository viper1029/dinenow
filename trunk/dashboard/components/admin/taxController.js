angular.module("app.admin.restaurant.manage.restaurant.taxes", [])
    .controller("AdminTaxesController", function($scope, $rootScope, $modal, $state, $stateParams, ngTableConfig, adminTaxesResource) {
        $scope.initList = function() {
            var restaurantID = $stateParams.restaurantId;
            console.log(restaurantID);
            setTimeout(function() {
                adminTaxesResource.get(restaurantID).success(function(payload) {
                    console.log(payload);
                    $scope.taxes = payload.data.taxes,
                        $scope.tableParams = ngTableConfig.config($scope.taxes)
                })
            }, 300)
        }, $scope.initList(),
            $scope.open = function(item) {
                var modalInstance = $modal.open({
                    templateUrl: "scripts/admin/restaurant/manage/restaurant/views/modal/taxes.html",
                    resolve: {
                        itemToProcess: function() {
                            return item
                        }
                    },
                    controller: function($scope, $modalInstance, adminTaxesResource, itemToProcess) {
                        $scope.init = function() {
                            $scope.itemToProcess = angular.copy(itemToProcess) || {}, $scope.itemToProcess.id && setTimeout(function() {
                                adminTaxesResource.getByID($scope.itemToProcess).success(function(payload) {
                                    $scope.itemToProcess = payload.data.tax
                                })
                            }, 100)
                        }, $scope.init(), $scope.save = function() {
                            $scope.isLoadingSave = !0, setTimeout(function() {
                                $scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $stateParams.restaurantId || ""), adminTaxesResource.addOrUpdate($scope.itemToProcess).success(function() {
                                    $scope.isLoadingSave = !1, $modalInstance.close($scope.itemToProcess)
                                })
                            }, 300)
                        }, $scope.cancel = function() {
                            $modalInstance.dismiss("cancel")
                        }
                    }
                });
                modalInstance.result.then(function(data) {
                    alertify.success(data.id ? "Updated Successfully" : "Created Successfully"), $state.go($state.current, {}, {
                        reload: !0
                    })
                })
            }, $scope["delete"] = function(item) {
            alertify.confirm("Are you sure you want to delete?", function(e) {
                e && adminTaxesResource["delete"](item).success(function() {
                    $state.go($state.current, {}, {
                        reload: !0
                    }), alertify.success("Delete Successfully")
                }).error(function() {
                    alertify.error("Oop! Please try again")
                })
            })
        }
    });