angular.module("app.admin.restaurant.manage.menu.items", ["flow"])
  .controller("AdminItemsController",
    function ($scope, $filter, $modal, $state,
              $stateParams, ngTableConfig, adminItemsResource, adminSizesResource, adminAddonResource,
              adminModifiersResource) {

      var initItems = function () {
        var restaurantID = $stateParams.restaurantId;
        adminItemsResource.get(restaurantID).success(function (payload) {
          $scope.items = payload.data.items;
          $scope.tableParams = ngTableConfig.config($scope.items)
        });

        var loadSizes = function () {
          adminSizesResource.getAll(restaurantID).success(function (payload) {
            $scope.listSizes = payload.data.sizes
          })
        };

        var loadAddons = function () {
          adminAddonResource.get(restaurantID).success(function (payload) {
            $scope.listAddons = payload.data.addons
          })
        };

        var loadModifiers = function () {
          adminModifiersResource.get(restaurantID).success(function (payload) {
            $scope.modifiers = payload.data.modifiers
          })
        };
        loadSizes();
        loadModifiers();
      };

      var open = function (item) {
        var modalInstance = $modal.open({
          templateUrl: "scripts/admin/restaurant/manage/menu/views/modal/items.html",
          resolve: {
            itemToProcess: function () {
              return item
            },
            listSizes: function () {
              return $scope.listSizes
            },
            modifiers: function () {
              return $scope.modifiers
            }
          },
          windowClass: 'app-modal-window-med ',
          controller: function ($scope, $modalInstance, adminItemsResource, itemToProcess, listSizes, modifiers) {
            var init = function () {
              $scope.itemToProcess = angular.copy(itemToProcess) || {};
              $scope.listSizes = angular.copy(listSizes) || {};
              $scope.modifiers = angular.copy(modifiers) || {};
              $scope.tagOptions = {
                multiple: !0,
                simple_tags: !0,
                tags: ["BBQ", "Asia", "Vegetarian", "Western"]
              };

              $scope.onShow = {
                size: !1,
                modifier: !1
              }

              var loadItem = function () {
                adminItemsResource.getByID($scope.itemToProcess).success(function (payload) {
                  $scope.itemToProcess = payload.data.item;
                  $scope.itemToProcess.itemSize.push({
                    size: "",
                    price: 0
                  });
                  $scope.itemToProcess.modifiers.push({

                  });
                  loadItemDetails();
                });
              };

              var loadItemDetails = function () {
                if ($scope.itemToProcess.id == null) {
                  $scope.itemToProcess = {
                    availabilityStatus: "AVAILABLE",
                    linkImage: "empty",
                    price: 0,
                    itemSize: [{
                      size: "",
                      price: 0
                    }],
                    modifiers: [{

                    }]
                  }
                }
              }
              loadItem();
            };

            init();

            $scope.getLinkImage = function (message) {
              var response = JSON.parse(message);
              $scope.itemToProcess.linkImage = response.data.link
            };

            $scope.addItemSize = function (itemSizeId, price) {
              $scope.itemToProcess.itemSize.push({
                size: "",
                price: 0
              })
            };

            $scope.addModifier = function (modifierId) {
              $scope.itemToProcess.modifiers.push({

              })
            };

            $scope.save = function () {
              $scope.itemToSave = angular.copy($scope.itemToProcess)
              $scope.itemToSave.id || ($scope.itemToSave.restaurantId = $stateParams.restaurantId || "");

              var splice = $scope.itemToSave.itemSize.length > 1 &&
                ($scope.itemToSave.itemSize[$scope.itemToSave.itemSize.length - 1].size === undefined ||
                $scope.itemToSave.itemSize[$scope.itemToSave.itemSize.length - 1].size.id === null ||
                $scope.itemToSave.itemSize[$scope.itemToSave.itemSize.length - 1].size.id === undefined);

              if (splice) {
                $scope.itemToSave.itemSize.splice(-1, 1);
              }

              var spliceModifier = $scope.itemToSave.modifiers.length >= 1 &&
                ($scope.itemToSave.modifiers[$scope.itemToSave.modifiers.length - 1] === undefined ||
                $scope.itemToSave.modifiers[$scope.itemToSave.modifiers.length - 1].id === null ||
                $scope.itemToSave.modifiers[$scope.itemToSave.modifiers.length - 1].id === undefined);


              if (spliceModifier) {
                $scope.itemToSave.modifiers.splice(-1, 1);
              }

              adminItemsResource.addOrUpdate($scope.itemToSave).success(function () {
                $modalInstance.close($scope.itemToProcess)
              }).error(function () {
                alertify.error("Oops! Please try again")
              })
            }, $scope.cancel = function () {
              $modalInstance.dismiss("cancel")
            }
          }
        });
        modalInstance.result.then(function (data) {
          var isMatchId = !1;
          if (data) {
            for (var i = 0; i < $scope.items.length; i++)
              if ($scope.items[i].id == data.id) {
                isMatchId = !0, $scope.items[i] = data, alertify.success("Updated Successfully"), $state.go($state.current, {}, {
                  reload: !0
                });
                break
              }
            isMatchId || (alertify.success("Created Successfully"), $state.go($state.current, {}, {
              reload: !0
            }))
          }
        })
      };

      $scope.pause = function (item) {
        item.pauseLoading = !0, item.availabilityStatus = "AVAILABLE" == item.availabilityStatus ? "PAUSED" : "AVAILABLE", setTimeout(function () {
          adminItemsResource.addOrUpdate(item).success(function () {
            item.pauseLoading = !1
          }).error(function () {
          })
        }, 800)
      };

      var deleteItem = function (item) {
        alertify.confirm("Are you sure you want to delete?", function (e) {
          e && adminItemsResource["delete"](item).success(function () {
            $state.go($state.current, {}, {
              reload: !0
            });
            alertify.success("Deleted successfully")
          }).error(function () {
            alertify.error("Oops! This item was used on Sub-Menu or Menu. Please check it again before deleting.")
          })
        })
      };

      initItems();
      $scope.open = open;
      //$scope.deleteAddon = deleteAddon;
    });