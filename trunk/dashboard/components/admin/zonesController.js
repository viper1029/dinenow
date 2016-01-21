angular.module("app.admin.restaurant.manage.restaurant.zones", ["theme.google_maps"])
    .controller("AdminZoneController", function($rootScope, $scope, $modal, $state, $stateParams, CENTER, ngTableConfig, adminDeliveryZoneResource) {
        $scope.locationRestaurant = window.localStorage.getItem("restaurantLocation"),
            $scope.initZoneMap = function() {
                var setMarker = function(map, center) {
                        var marker = map.addMarker({
                            lat: center.lat,
                            lng: center.lng
                        });
                        return marker
                    },
                    drawZone = function(map, zones) {
                        for (var polygon = {}, i = 0; i < zones.length; i++) polygon = map.drawPolygon({
                            paths: zones[i].deliveryZoneCoords,
                            useGeoJSON: !0,
                            clickable: !1
                        });
                        return polygon
                    },
                    restaurantID = $stateParams.restaurantId;
                adminDeliveryZoneResource.get(restaurantID).success(function(payload) {
                    if ($scope.deliveryZone = payload.data.deliveryzones, $scope.tableParams = ngTableConfig.config($scope.deliveryZone), $scope.locationRestaurant) {
                        $scope.mapCenter = "object" != typeof $scope.locationRestaurant ? JSON.parse($scope.locationRestaurant) : $scope.locationRestaurant, $scope.zoneMap = new GMaps({
                            div: "#zone-map",
                            zoom: 12,
                            lat: $scope.mapCenter.lat,
                            lng: $scope.mapCenter.lng
                        }); {
                            setMarker($scope.zoneMap, $scope.mapCenter), drawZone($scope.zoneMap, $scope.deliveryZone)
                        }
                    }
                })
            }, $scope.init = function() {
            $scope.initZoneMap()
        }, $scope.init(), $scope.open = function(item, event) {
            item ? (event.stopPropagation(), item.loading = !0) : $scope.createLoading = !0, setTimeout(function() {
                var modalInstance = $modal.open({
                    templateUrl: "scripts/admin/restaurant/manage/restaurant/views/modal/zone.html",
                    resolve: {
                        itemToProcess: function() {
                            return item
                        },
                        scope: function() {
                            return $scope
                        }
                    },
                    controller: function($scope, $modalInstance, itemToProcess, adminDeliveryZoneResource, scope) {
                        $scope.itemToProcess = angular.copy(itemToProcess) || {}, $scope.drawOnMap = {
                            setMarker: function(map, center) {
                                var marker = map.addMarker({
                                    lat: center.lat,
                                    lng: center.lng
                                });
                                return marker
                            },
                            drawZone: function(map, zones) {
                                for (var polygon = {}, i = 0; i < zones.length; i++) polygon = map.drawPolygon({
                                    paths: zones[i].deliveryZoneCoords,
                                    useGeoJSON: !0,
                                    clickable: !1
                                });
                                return polygon
                            },
                            comparePolygon: function(polygonPath1, polygonPath2) {
                                if (polygonPath1.length === polygonPath2.length) {
                                    for (var isEqual = !0, i = 0; i < polygonPath1.length; i++)
                                        if (polygonPath1[i].lat != polygonPath2[i].lat && polygonPath1[i].lng != polygonPath2[i].lng) {
                                            isEqual = !1;
                                            break
                                        }
                                    return isEqual
                                }
                                return !1
                            },
                            drawZoneEditing: function(mapInstance, zonesAll, zoneEditing, modelPath) {
                                for (var isEqualPolygon = !1, j = 0; j < zonesAll.length; j++)
                                    if (isEqualPolygon = $scope.drawOnMap.comparePolygon(zoneEditing.deliveryZoneCoords, zonesAll[j].deliveryZoneCoords)) {
                                        var lat = modelPath.deliveryZoneCoords[0].lat,
                                            lng = modelPath.deliveryZoneCoords[0].lng;
                                        mapInstance.setCenter(lat, lng);
                                        var polygon = new google.maps.Polygon({
                                            paths: modelPath.deliveryZoneCoords,
                                            editable: !0,
                                            draggable: !0
                                        });
                                        polygon.setMap(mapInstance.map), google.maps.event.addListener(polygon, "mouseup", function() {
                                            $scope.coordinates = polygon.getPath().getArray()
                                        })
                                    } else mapInstance.drawPolygon({
                                        paths: zonesAll[j].deliveryZoneCoords,
                                        useGeoJSON: !0,
                                        clickable: !1
                                    })
                            }
                        }, $scope.initMapCreateMode = function() {
                            $scope.coordinates = [], $scope.$on("GMaps:created", function(event, mapInstance) {
                                if (mapInstance.key && ($scope[mapInstance.key] = mapInstance.map), "polygonInstance" == mapInstance.key) {
                                    var drawingManager = new google.maps.drawing.DrawingManager({
                                        drawingControl: !0,
                                        drawingControlOptions: {
                                            position: google.maps.ControlPosition.TOP_CENTER,
                                            drawingModes: [google.maps.drawing.OverlayType.POLYGON]
                                        }
                                    });
                                    if (drawingManager.setMap(mapInstance.map.map), google.maps.event.addListener(drawingManager, "polygoncomplete", function(polygon) {
                                            $scope.coordinates = polygon.getPath().getArray(), drawingManager.drawingMode = null, drawingManager.drawingControl = !1, drawingManager.setMap(mapInstance.map.map), google.maps.event.addListener(polygon, "click", function() {
                                                polygon.setEditable(!0), polygon.setDraggable(!0)
                                            }), google.maps.event.addListener(polygon, "mouseup", function() {
                                                $scope.coordinates = polygon.getPath().getArray()
                                            })
                                        }), $scope.polygonInstance) {
                                        $scope.drawOnMap.setMarker($scope.polygonInstance, scope.mapCenter), $scope.drawOnMap.drawZone($scope.polygonInstance, scope.deliveryZone)
                                    }
                                }
                            })
                        }, $scope.initMapEditMode = function(item) {
                            $scope.$on("GMaps:created", function(event, mapInstance) {
                                if (mapInstance.key && ($scope[mapInstance.key] = mapInstance.map), $scope.polygonInstance) {
                                    {
                                        $scope.drawOnMap.setMarker($scope.polygonInstance, scope.mapCenter)
                                    }
                                    $scope.drawOnMap.drawZoneEditing($scope.polygonInstance, scope.deliveryZone, item, $scope.itemToProcess)
                                }
                            })
                        }, $scope.init = function() {
                            $scope.typeZone = [{
                                label: "City",
                                value: "CITY"
                            }, {
                                label: "Radius",
                                value: "RADIUS"
                            }, {
                                label: "Draw",
                                value: "CUSTOM"
                            }], $scope.itemToProcess.id ? ($scope.changedMap = !0, $scope.circleInstance, $scope.circleOptions = {
                                lat: scope.mapCenter.lat,
                                lng: scope.mapCenter.lng,
                                zoom: 11,
                                panControl: !1,
                                streetViewControl: !1,
                                mapTypeControl: !1
                            }, $scope.polygonInstance, $scope.polygonOptions = {
                                lat: scope.mapCenter.lat,
                                lng: scope.mapCenter.lng,
                                zoom: 11,
                                panControl: !1,
                                streetViewControl: !1,
                                mapTypeControl: !1
                            }, $scope.initMapEditMode($scope.itemToProcess)) : ($scope.itemToProcess = {
                                center: {
                                    lat: 0,
                                    lng: 0
                                },
                                radius: 1e3
                            }, $scope.circleInstance, $scope.circleOptions = {
                                lat: scope.mapCenter.lat,
                                lng: scope.mapCenter.lng,
                                zoom: 11,
                                panControl: !1,
                                streetViewControl: !1,
                                mapTypeControl: !1,
                                click: function(e) {
                                    $scope.itemToProcess.center.lat = e.latLng.lat(), $scope.itemToProcess.center.lng = e.latLng.lng(), $scope.zoneCircle ? $scope.zoneCircle.setCenter($scope.itemToProcess.center) : ($scope.zoneCircle = $scope.circleInstance.drawCircle({
                                        center: $scope.itemToProcess.center,
                                        radius: $scope.itemToProcess.radius,
                                        strokeColor: "#5D92C2",
                                        strokeOpacity: 1,
                                        strokeWeight: 3,
                                        fillColor: "#5D92C2",
                                        fillOpacity: .4,
                                        editable: !0,
                                        dragend: function(e) {}
                                    }), google.maps.event.addListener($scope.zoneCircle, "radius_changed", function() {
                                        var newRadius = $scope.zoneCircle.getRadius();
                                        newRadius = Math.round(newRadius), $scope.itemToProcess.radius = newRadius, setTimeout(function() {
                                            $scope.$apply()
                                        }, 100)
                                    }))
                                }
                            }, $scope.$watch("itemToProcess.radius", function(newVal) {
                                newVal && $scope.zoneCircle && $scope.zoneCircle.setRadius(newVal)
                            }), $scope.polygonInstance, $scope.paths = [], $scope.polygonOptions = {
                                lat: scope.mapCenter.lat,
                                lng: scope.mapCenter.lng,
                                panControl: !1,
                                streetViewControl: !1,
                                mapTypeControl: !1,
                                zoom: 11,
                                click: function(e) {
                                    $scope.paths.push(e.latLng)
                                }
                            }, $scope.initMapCreateMode())
                        }, $scope.init(), $scope.loadMap = !1, $scope.save = function() {
                            $scope.saveLoading = !0, $scope.getCoordinates(), $scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $stateParams.restaurantId), setTimeout(function() {
                                adminDeliveryZoneResource.addOrUpdate($scope.itemToProcess).success(function() {
                                    $scope.saveLoading = !1, $modalInstance.close($scope.itemToProcess)
                                }).error(function() {
                                    alertify.error("Oops! Please try again")
                                })
                            }, 500)
                        }, $scope.cancel = function() {
                            $modalInstance.dismiss("cancel")
                        }, $scope.loadMap = !0, $scope.getCoordinates = function() {
                            $scope.itemToProcess.deliveryZoneCoords = [];
                            for (var i = ($scope.coordinates.length, 0); i < $scope.coordinates.length; i++) {
                                var tempObj = {
                                    lat: 0,
                                    lng: 0
                                };
                                tempObj.lat = $scope.coordinates[i].lat(), tempObj.lng = $scope.coordinates[i].lng(), $scope.itemToProcess.deliveryZoneCoords.push(tempObj)
                            }
                            $scope.itemToProcess.deliveryZoneCoords.push($scope.itemToProcess.deliveryZoneCoords[0])
                        }, $scope.changeMapType = function() {
                            $scope.changedMap = !0
                        }
                    }
                });
                modalInstance.opened.then(function() {
                    item ? item.loading = !1 : $scope.createLoading = !1
                }), modalInstance.result.then(function(data) {
                    var isMatchId = !1;
                    if (data) {
                        for (var i = 0; i < $scope.deliveryZone.length; i++)
                            if ($scope.deliveryZone[i].id == data.id) {
                                isMatchId = !0, $scope.deliveryZone[i] = data, alertify.success("Updated Successfully"), $state.go($state.current, {}, {
                                    reload: !0
                                });
                                break
                            }
                        isMatchId || (alertify.success("Created Successfully"), $state.go($state.current, {}, {
                            reload: !0
                        }))
                    }
                })
            }, 300)
        }, $scope.showOnMap = function(item) {
            var lat = (item.deliveryZoneCoords, item.deliveryZoneCoords[0].lat),
                lng = item.deliveryZoneCoords[0].lng;
            $scope.zoneMap.setCenter(lat, lng)
        }, $scope.deleteZone = function(item, event) {
            event.stopPropagation(), alertify.confirm("Are you sure you want to delete?", function(e) {
                e && adminDeliveryZoneResource["delete"](item).success(function() {
                    for (var i = 0; i < $scope.deliveryZone.length; i++)
                        if ($scope.deliveryZone[i].id == item.id) {
                            $scope.deliveryZone.splice(i, 1);
                            break
                        }
                    alertify.success("Deleted successfully")
                }).error(function() {
                    alertify.error("Oops! Please try again")
                })
            })
        }
    });