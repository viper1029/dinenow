angular.module("app.owner.restaurant.subscriptions", [])
    .controller("SubscriptionsController", function($scope, $cookieStore, $modal, $state, subscriptionsResource) {
        $scope.isSubmited = !1, $scope.isLoadingData = !0, $scope.hideAlert = !1, $scope.statusSubmit = "", $scope.cards = [], $scope.getCard = function() {
            setTimeout(function() {
                var customerStripe = $cookieStore.get("customerStripe");
                customerStripe && subscriptionsResource.getCard(customerStripe).success(function(payload) {
                    $scope.cards = payload.data, $scope.cards.length > 0 && ($scope.cardInfo = $scope.cards[0])
                }).error(function() {
                    $scope.isLoadingData = !1
                })
            }, 100)
        }, $scope.getSubscription = function() {
            setTimeout(function() {
                subscriptionsResource.getSubscription().success(function(payload) {
                    console.log(payload.data);
                    if ($scope.isLoadingData = !1, payload.data.length > 0) {
                        var lastItem = payload.data.length - 1;
                        $scope.subscription = payload.data[lastItem]
                    } else $scope.subscription = !1
                }).error(function() {
                    $scope.subscription = !1, $scope.isLoadingData = !1
                })
            }, 100)
        }, $scope.getAssignedPlan = function() {
            setTimeout(function() {
                subscriptionsResource.getPlanAssigned().success(function(payload) {
                    console.log(payload.data);
                    $scope.planAssigned = payload.data, $scope.planAssigned.amount = $scope.planAssigned.amount / 100, $scope.planAssigned && ($scope.getSubscription(), $scope.getCard())
                }).error(function() {
                    $scope.planAssigned = !1, $scope.isLoadingData = !1
                })
            }, 100)
        }, $scope.init = function() {
            $scope.getAssignedPlan()
        }, $scope.init(), $scope.stripeCallback = function(code, result) {
            if (result.error) scope.statusSubmit = "error", $scope.isLoadingSubmit = !1;
            else {
                var cardToken = {};
                cardToken.tokenStripe = result.id, setTimeout(function() {
                    $scope.isLoadingSubmit = !0, subscriptionsResource.registerPlan(cardToken).success(function() {
                        $scope.isLoadingSubmit = !1, scope.statusSubmit = "success", $scope.isSubmited = !0
                    }).error(function() {
                        scope.statusSubmit = "error", $scope.isLoadingSubmit = !1
                    })
                }, 300)
            }
        }, $scope.onAddCard = function() {
            var modalInstance = $modal.open({
                templateUrl: "scripts/owner/restaurant/views/modal/subscriptions.html",
                size: "sm",
                resolve: {
                    scope: function() {
                        return $scope
                    }
                },
                controller: function(scope, $scope, $cookieStore, $modalInstance, subscriptionsResource) {
                    $scope.planAssigned = angular.copy(scope.planAssigned), $scope.stripeCallback = function(code, result) {
                        if (result.error) $modalInstance.close();
                        else {
                            var cardToken = {};
                            cardToken.tokenStripe = result.id, setTimeout(function() {
                                $scope.isLoadingSubmit = !0, subscriptionsResource.registerPlan(cardToken).success(function(payload) {
                                    var data = {};
                                    data.subscription = payload.data, data.cardInfo = result.card, alertify.success("Payment Successfully !"), $modalInstance.close(data)
                                }).error(function() {
                                    $modalInstance.close(), alertify.error("Oops! Something when wront. Please try again !")
                                })
                            }, 300)
                        }
                    }, $scope.cancel = function() {
                        $modalInstance.close()
                    }
                }
            });
            modalInstance.result.then(function(data) {
                data && ($scope.cardInfo = data.cardInfo, $scope.subscription = data.subscription)
            })
        }, $scope.onDeleteCard = function(card) {
            var modalInstance = $modal.open({
                templateUrl: "scripts/common/views/confirm-delete.html",
                resolve: {
                    card: function() {
                        return card
                    }
                },
                controller: function(card, $scope, $cookieStore, $modalInstance, subscriptionsResource) {
                    $scope["delete"] = function() {
                        setTimeout(function() {
                            $scope.isLoadingDelete = !0;
                            var userID = $cookieStore.get("authUserID");
                            subscriptionsResource.deleteCard(userID, card.id).success(function() {
                                alertify.success("Deleted Successfully"), $scope.isLoadingDelete = !1, $modalInstance.close(!0)
                            }).error(function() {
                                alertify.error("Oops! Something went wrong...")
                            })
                        }, 300)
                    }, $scope.cancel = function() {
                        $modalInstance.close()
                    }
                }
            });
            modalInstance.result.then(function(data) {
                data && ($scope.cardInfo = void 0)
            })
        }, $scope.onCloseAlert = function() {
            $scope.statusSubmit = ""
        }
    });