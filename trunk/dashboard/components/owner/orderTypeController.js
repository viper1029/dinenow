angular.module("app.owner.restaurant.ordertype", ["ui.calendar"])
    .controller("OrderTypeController", function($scope, $rootScope, $compile, uiCalendarConfig, handleCalendarHours, timeResource, infoResource) {
        $scope.events = [],
            $scope.options = {
                dineIn: {
                    title: "Dine-In",
                    color: "#007802",
                    textColor: "white"
                },
                delivery: {
                    title: "Delivery",
                    color: "#177CAA",
                    textColor: "white"
                },
                takeOut: {
                    title: "TakeOut",
                    color: "#952626",
                    textColor: "white"
                }
            },
            $scope.isLoadingEvents = !0,
            $scope.init = function() {
                $scope.datetimes = handleCalendarHours.formatDatetime,
                    $scope.isAccept = {
                        delivery: !1,
                        takeout: !1
                    };
                var convertHoursToEvents = function(hours) {
                    for (var events = [], i = 0; i < hours.dineInHours.length; i++)
                        events.push({
                            id: hours.dineInHours[i].id,
                            title: $scope.options.dineIn.title,
                            start: moment.utc(hours.dineInHours[i].fromTime).format(),
                            end: moment.utc(hours.dineInHours[i].toTime).format(),
                            color: $scope.options.dineIn.color,
                            textColor: $scope.options.dineIn.textColor,
                            dayInWeek: hours.dineInHours[i].weekDayType
                        });
                    for (var i = 0; i < hours.acceptDeliveryHours.length; i++) events.push({
                        id: hours.acceptDeliveryHours[i].id,
                        title: "Delivery",
                        start: moment.utc(hours.acceptDeliveryHours[i].fromTime).format(),
                        end: moment.utc(hours.acceptDeliveryHours[i].toTime).format(),
                        color: "#177CAA",
                        textColor: "white",
                        dayInWeek: hours.acceptDeliveryHours[i].weekDayType
                    });
                    for (var i = 0; i < hours.acceptTakeOutHours.length; i++) events.push({
                        id: hours.acceptTakeOutHours[i].id,
                        title: "TakeOut",
                        start: moment.utc(hours.acceptTakeOutHours[i].fromTime).format(),
                        end: moment.utc(hours.acceptTakeOutHours[i].toTime).format(),
                        color: "#952626",
                        textColor: "white",
                        dayInWeek: hours.acceptTakeOutHours[i].weekDayType
                    });
                    return events
                };
                setTimeout(function() {
                    infoResource.get().success(function(payload) {
                        console.log("++++++++++");
                        console.log(payload.data.restaurant);
                        $scope.hours = {
                            dineInHours: payload.data.restaurant.dineInHours,
                            acceptDeliveryHours: payload.data.restaurant.acceptDeliveryHours,
                            acceptTakeOutHours: payload.data.restaurant.acceptTakeOutHours
                        }, $scope.isAccept.delivery = payload.data.restaurant.acceptDelivery,
                            $scope.isAccept.takeout = payload.data.restaurant.acceptTakeOut,
                            /* $scope.fetchEvents = convertHoursToEvents($scope.hours);
                             for (var i = 0; i < $scope.fetchEvents.length; i++)
                             $scope.events.push($scope.fetchEvents[i]);*/
                            $scope.isLoadingEvents = !1
                    })
                }, 300)
            }, $scope.init(),
            $scope.initCalendar = function() {
                $scope.getEvents = function(start, end, timezone, callback) {
                    return $scope.events ? callback($scope.events) : void 0
                }, $scope.uiConfig = {
                    calendar: {
                        header: {
                            left: "today prev,next",
                            center: "",
                            right: ""
                        },
                        events: $scope.getEvents,
                        defaultView: "agendaWeek",
                        editable: !1,
                        eventLimit: !0,
                        firstDay: 1,
                        allDaySlot: !1,
                        eventClick: function(calEvent) {
                            alertify.confirm("Delete the event " + calEvent.title, function(e) {
                                if (e)
                                    for (var i = 0; i < $scope.events.length; i++) $scope.events[i].id === calEvent.id && $scope.events.splice(i, 1)
                            })
                        }
                    }
                }, $scope.eventSources = [$scope.events],
                    $scope.addEvents = function() {
                        return {
                            dinein: function(params) {
                                var date = handleCalendarHours.getDateByIndex(params.day.value),
                                    startTime = handleCalendarHours.getDateByISO8601(date, params.startTime),
                                    endTime = handleCalendarHours.getDateByISO8601(date, params.endTime),
                                    objEvent = {
                                        id: handleCalendarHours.getUniqueID(),
                                        title: "Dine-In",
                                        start: startTime,
                                        end: endTime,
                                        color: "#007802",
                                        textColor: "white",
                                        dayInWeek: params.day.label
                                    };
                                $scope.events.push(objEvent), $scope.dinein = {}
                            },
                            delivery: function(params) {
                                var date = handleCalendarHours.getDateByIndex(params.day.value),
                                    startTime = handleCalendarHours.getDateByISO8601(date, params.startTime),
                                    endTime = handleCalendarHours.getDateByISO8601(date, params.endTime),
                                    objEvent = {
                                        id: handleCalendarHours.getUniqueID(),
                                        title: "Delivery",
                                        start: startTime,
                                        end: endTime,
                                        color: "#177CAA",
                                        textColor: "white",
                                        dayInWeek: params.day.label
                                    };
                                $scope.events.push(objEvent), $scope.delivery = {}
                            },
                            takeout: function(params) {
                                var date = handleCalendarHours.getDateByIndex(params.day.value),
                                    startTime = handleCalendarHours.getDateByISO8601(date, params.startTime),
                                    endTime = handleCalendarHours.getDateByISO8601(date, params.endTime),
                                    objEvent = {
                                        id: handleCalendarHours.getUniqueID(),
                                        title: "TakeOut",
                                        start: startTime,
                                        end: endTime,
                                        color: "#952626",
                                        textColor: "white",
                                        dayInWeek: params.day.label
                                    };
                                $scope.events.push(objEvent), $scope.takeout = {}
                            }
                        }
                    }, $scope.onClearEvents = function() {
                    alertify.confirm("Are you sure want to clear all events on calendar ?", function(e) {
                        if (e)
                            for (var i = $scope.events.length - 1; i >= 0; i--) $scope.events.splice(i, 1)
                    })
                }
            },
            $scope.initCalendar(),
            $scope.initEndTime = function() {
                return {
                    dinein: function(startTime) {
                        $scope.datetimes.dineinEndTimes = handleCalendarHours.getEndTimes(startTime)
                    },
                    delivery: function(startTime) {
                        $scope.datetimes.deliveryEndTimes = handleCalendarHours.getEndTimes(startTime)
                    },
                    takeout: function(startTime) {
                        $scope.datetimes.takeoutEndTimes = handleCalendarHours.getEndTimes(startTime)
                    }
                }
            }, $scope.onSave = function() {
            $scope.isLoadingSave = !0;
            var objectModel = {};
            objectModel = handleCalendarHours.synObjectOnAPI($scope.events), objectModel.acceptDelivery = $scope.isAccept.delivery, objectModel.acceptTakeOut = $scope.isAccept.takeout, timeResource.putTime(objectModel).success(function() {
                alertify.success("Saved Successfully !"), $scope.isLoadingSave = !1
            })
        }
    });