/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

function VisitInfoController($scope, distributionService, $routeParams) {
  $scope.distribution = distributionService.distribution;
  $scope.distributionReview = distributionService.distributionReview;
  $scope.selectedFacility = $routeParams.facility;

  $scope.stockoutCausesNotRecorded = {};
  $scope.receivedProductsNotRecorded = {};

  $scope.convertToDateObject = function (dateText) {
    var dateParts = dateText.split('/');

    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
  };

  $scope.startDate = $scope.convertToDateObject($scope.distribution.period.stringStartDate);

  $scope.reasons = {
    badWeather: "ROAD_IMPASSABLE",
    noTransport: "TRANSPORT_UNAVAILABLE",
    facilityClosed: "HEALTH_CENTER_CLOSED",
    unavailableFuelFunds: "FUEL_FUNDS_UNAVAILABLE",
    unavailablePerDiemFunds: "PERDIEM_FUNDS_UNAVAILABLE",
    refrigeratorsNotWorking: "REFRIGERATORS_NOT_WORKING",
    noRefrigerators: "NO_REFRIGERATORS",
    notPartOfProgram: "HEALTH_CENTER_NOT_IN_DLS",
    other: "OTHER"
  };

  $scope.beforeDatepickerShowDay = function (date) {
    if (!$("#visitDate").val().length && $scope.startDate.getTime() == date.getTime()) {
      return [true, "ui-state-active"];
    }

    return [true, ""];
  };

  $scope.setApplicableVisitInfo = function () {
    var visit = $scope.distribution.facilityDistributions[$scope.selectedFacility].facilityVisit;

    if (isUndefined(visit.visited)) {
        return;
    }

    if (visit.visited.value) {
      visit.reasonForNotVisiting = setApplicableField(visit.reasonForNotVisiting);
      visit.otherReasonDescription = setApplicableField(visit.otherReasonDescription);
      return;
    }

    visit.observations = setApplicableField(visit.observations);
    visit.confirmedBy = setApplicableField(visit.confirmedBy);
    visit.verifiedBy = setApplicableField(visit.verifiedBy);
    visit.vehicleId = setApplicableField(visit.vehicleId);
    visit.visitDate = setApplicableField(visit.visitDate);
    visit.stockouts = setApplicableField(visit.stockouts);
    $scope.clearStockoutCauses();
    visit.hasAdditionalProductSources = setApplicableField(visit.hasAdditionalProductSources);
    scope.clearAdditionalProductSources();
    visit.stockCardsUpToDate = setApplicableField(visit.stockCardsUpToDate);
  };

  $scope.clearStockoutCauses = function () {
    var visit = $scope.distribution.facilityDistributions[$scope.selectedFacility].facilityVisit;

    if (!visit.stockoutCauses) {
      visit.stockoutCauses = {};
    }

    $.each(['coldChainEquipmentFailure','incorrectEstimationNeeds','stockoutZonalWarehouse','deliveryNotOnTime','productsTransferedAnotherFacility','other','stockoutCausesOther'], function (i, elem) {
      visit.stockoutCauses[elem] = setApplicableField(visit.stockoutCauses[elem]);
    });
  };

  $scope.clearStockoutCausesOther = function () {
    var visit = $scope.distribution.facilityDistributions[$scope.selectedFacility].facilityVisit;

    if (!visit.stockoutCauses) {
      visit.stockoutCauses = {};

      visit.stockoutCauses.stockoutCausesOther = setApplicableField(visit.stockoutCauses.stockoutCausesOther);
    }
  };

  $scope.clearAdditionalProductSources = function () {
    var visit = $scope.distribution.facilityDistributions[$scope.selectedFacility].facilityVisit;

    if (!visit.additionalProductSources) {
      visit.additionalProductSources = {};
    }

    $.each(['anotherHealthFacility','zonalWarehouse','other','additionalProductSourcesOther'], function (i, elem) {
      visit.additionalProductSources[elem] = setApplicableField(visit.additionalProductSources[elem]);
    });
  };

  $scope.clearAdditionalProductSourcesOther = function () {
    var visit = $scope.distribution.facilityDistributions[$scope.selectedFacility].facilityVisit;

    if (!visit.additionalProductSources) {
      visit.additionalProductSources = {};

      visit.additionalProductSources.additionalProductSourcesOther = setApplicableField(visit.additionalProductSources.additionalProductSourcesOther);
    }
  };

  function setApplicableField(field) {
    if (isUndefined(field)) {
        return {type: 'reading'};
    }

    if (isUndefined(field.original)) {
        return {type: 'reading'};
    }

    return { original: field.original, type: 'reading' };
  }
}
