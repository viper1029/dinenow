angular.module("app.admin.restaurant", ["app.admin.restaurant.service",
        "app.admin.restaurant.directive", "app.admin.restaurant.manage", "theme.google_maps", "searchBoxAutocomplete"])
    .controller("RestaurantController", function ($rootScope, $scope, $modal,
                                                  $state, $filter, ngTableParams, restaurantsResource, $timeout) {
        $scope.init = function (page) {
            setTimeout(function () {
                restaurantsResource.get().success(function (payload) {
                    $scope.restaurants = payload.data.restaurants,
                        console.log($scope.restaurants);
                    $scope.initNgTable()
                })
            }, 200);

            $scope.initNgTable = function () {
                $scope.tableParams = new ngTableParams({
                    page: 1,
                    count: 10,
                    sorting: {
                        name: "asc"
                    }
                }, {
                    total: $scope.restaurants.length,
                    getData: function ($defer, params) {
                        var orderedData = params.sorting() ? $filter("orderBy")($scope.restaurants, params.orderBy()) : $scope.restaurants;
                        $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                    }
                })
            }
        },
            $scope.init(),

            $scope.open = function (item) {
                var modalInstance = $modal.open({
                    templateUrl: "scripts/admin/restaurant/views/modal/restaurant.html",
                    resolve: {
                        itemToProcess: function () {
                            return item
                        }
                    },
                    windowClass: 'app-modal-window ',

                    controller: function ($timeout, $scope, $modalInstance, restaurantsResource, itemToProcess) {

                        $scope.itemToProcess = angular.copy(itemToProcess) || {},

                            $scope.tagOptions = {
                                multiple: !0,
                                simple_tags: !0,
                                tags: []
                            },

                            console.log("itemToProcess" + $scope.itemToProcess);
                        console.log("tagOptions" + $scope.tagOptions);
                        $scope.initMap = function () {
                            $scope.marker;
                            $scope.locationRestaurant = {
                                lat: 0,
                                lng: 0
                            };
                            $scope.addressMapInstances;
                            $scope.addressMapOptions = {
                                lat: -12.043333,
                                lng: -77.028333,
                                panControl: !1,
                                streetViewControl: !1,
                                mapTypeControl: !1,
                                zoom: 10,
                                click: function (e) {
                                    console.log(e);
                                    $scope.locationRestaurant.lat = e.latLng.lat();
                                    $scope.locationRestaurant.lng = e.latLng.lng();
                                    console.log($scope.locationRestaurant);
                                    $scope.marker ? $scope.marker.setPosition($scope.locationRestaurant) :
                                        $scope.marker = $scope.addressMapInstances.addMarker({
                                            lat: e.latLng.lat(),
                                            lng: e.latLng.lng()
                                        });
                                    $timeout(function () {

                                        $scope.addressMapInstances.setCenter(e.latLng.lat(), e.latLng.lng()),
                                            $scope.addressMapInstances.refresh()
                                    }, 200);
                                    $scope.itemToProcess.location = $scope.locationRestaurant
                                }
                            };

                            $scope.$on("GMaps:created", function (event, mapInstance) {
                                console.log(mapInstance);
                                //console.log("hello");
                                if (mapInstance.key)
                                    $scope[mapInstance.key] = mapInstance.map;
                                var location = $scope.itemToProcess.location;
                                console.log(location);
                                $scope.addressMapInstances && location &&
                                ($scope.marker = $scope.addressMapInstances.addMarker({
                                    lat: location.lat,
                                    lng: location.lng
                                }),
                                    // google.maps.event.trigger($scope.addressMapInstances.map, 'resize'),
                                    $scope.addressMapInstances.setCenter(location.lat, location.lng)),
                                    //console.log($scope.addressMapInstances);
                                    $scope.addressMapInstances.refresh()

                                //console.log($scope.addressMapInstances);

                            })


                        };
                        $scope.init = function () {
                            $scope.itemToProcess.id || ($scope.itemToProcess = {
                                acceptTakeOut: !1,
                                active: 2,
                                acceptDelivery: !1,
                                acceptDineIn: !1,
                                networkStatus: "ONLINE"
                            });
                            $scope.initMap()
                        };

                        $scope.init();

                        $scope.submitGeocoding = function (add) {
                            GMaps.geocode({
                                address: add,
                                callback: function (results, status) {
                                    if ("OK" == status) {
                                        var latlng = results[0].geometry.location;
                                        $scope.addressMapInstances.setCenter(latlng.lat(), latlng.lng());
                                        $scope.addressMapInstances.removeMarkers();
                                        $scope.addressMapInstances.addMarker({
                                            lat: latlng.lat(),
                                            lng: latlng.lng()
                                        });
                                        $scope.itemToProcess.location.lng = latlng.lng();
                                        $scope.itemToProcess.location.lat = latlng.lat();
                                        $scope.itemToProcess.address1 = "";
                                            $scope.itemToProcess.address2 = "";
                                            $scope.itemToProcess.province = "";
                                            $scope.itemToProcess.country = "";
                                            $scope.itemToProcess.city = "";
                                            $scope.itemToProcess.postalCode = "";
                                        for(var i =0; results[0].address_components.length; i++) {
                                            var addr = results[0].address_components[i];
                                            if (addr.types[0] == 'street_number') {
                                                $scope.itemToProcess.address1 = addr.long_name;
                                            }
                                            if (addr.types[0] == 'route') {
                                                $scope.itemToProcess.address2 = addr.long_name;
                                            }
                                            if (addr.types[0] == 'locality') {
                                                $scope.itemToProcess.city = addr.long_name;
                                            }
                                            if (addr.types[0] == 'administrative_area_level_1') {
                                                $scope.itemToProcess.province = addr.long_name;
                                            }
                                            if (addr.types[0] == 'country') {
                                                $scope.itemToProcess.country = addr.long_name;
                                            }
                                            if (addr.types[0] == 'postal_code') {
                                                $scope.itemToProcess.postalCode = addr.long_name;
                                            }
                                        }
                                    }
                                }
                            })
                        };

                        $scope.loadMap = !1,
                            $scope.save = function () {
                                console.log($scope.itemToProcess);
                                $scope.itemToProcess.logo = "http://grandislerestaurant.com/wp-content/uploads/2012/12/restaurant-gallery.png";
                                restaurantsResource.addOrUpdate($scope.itemToProcess).success(function (res) {
                                    console.log(res);
                                    $modalInstance.close($scope.itemToProcess)
                                }).error(function () {
                                    alertify.error("Oops! Please try again")
                                })
                            };
                        $scope.cancel = function () {
                            $modalInstance.dismiss("cancel")
                        };
                        $scope.loadMap = !0
                    }
                });
                modalInstance.result.then(function (data) {
                    var isMatchId = !1;
                    if ($state.go($state.current, {}, {
                            reload: !0
                        }), data) {
                        for (var i = 0; i < $scope.restaurants.length; i++)
                            if ($scope.restaurants[i].id == data.id) {
                                isMatchId = !0, $scope.restaurants[i] = data, alertify.success("Updated Successfully");
                                break
                            }
                        isMatchId || (alertify.success("Created Successfully"), $state.go($state.current, {}, {
                            reload: !0
                        }))
                    }
                })

            }, $scope.manage = function (item) {
            console.log("from manage" + item);
            item.manageLoading = !0,
                window.localStorage.setItem("restaurantLocation", JSON.stringify(item.location)),
                setTimeout(function () {

                    item.manageLoading = !1,

                        $state.go("app.admin.restaurant.manage.menu.menus.list", {
                            restaurantId: item.id
                        })
                }, 300)
        }, $scope["delete"] = function (item) {
            alertify.confirm("Are you sure you want to delete?", function (e) {
                e && restaurantsResource["delete"](item).success(function () {
                    alertify.success("Delete successfully"), $state.go($state.current, {}, {
                        reload: !0
                    })
                }).error(function () {
                    alertify.error("Oops! Please try again")
                })
            })
        }
    });
