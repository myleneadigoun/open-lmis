/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.distribution.dto;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openlmis.core.domain.Refrigerator;
import org.openlmis.db.categories.UnitTests;
import org.openlmis.distribution.domain.RefrigeratorProblem;
import org.openlmis.distribution.domain.RefrigeratorReading;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.openlmis.distribution.domain.MonitoringDeviceType.OTHER_MONITORING_DEVICE;

@Category(UnitTests.class)
public class RefrigeratorReadingDTOTest {

  @Test
  public void shouldTransformRefrigeratorReadingToReading() throws Exception {
    Long facilityVisitId = 5L;
    Reading temperature = new Reading("32.4", false);
    Reading functioningCorrectly = new Reading("N", false);
    Reading lowAlarmEvents = new Reading("2", false);
    Reading highAlarmEvents = new Reading("", true);
    Reading problemSinceLastTime = new Reading("Y", false);
    RefrigeratorProblem problems = new RefrigeratorProblem(3L, false, true, false, false, false, false, null);
    Reading hasMonitoringDevice = new Reading("Y", false);
    Reading monitoringDeviceType = new Reading("OTHER_MONITORING_DEVICE", false);
    Reading monitoringDeviceOtherType = new Reading("some_other_device", false);
    Reading temperatureReportingForm = new Reading("Y", false);
    Reading highestTemperatureReported = new Reading("50", false);
    Reading lowestTemperatureReported = new Reading("10", false);
    Reading problemOccurredDate = new Reading("08/05/2016", false);
    Reading problemReportedDate = new Reading("09/05/2016", false);
    Reading equipmentRepaired = new Reading("Y", false);
    Reading equipmentRepairedDate = new Reading("09/05/2016", false);
    Reading totalDaysCceUptime = new Reading("1", false);
    Reading notes = new Reading("Notes");
    Long facilityId = 2L;

    RefrigeratorReading expectedReading = new RefrigeratorReading(new Refrigerator("brand", "model", "serial number", facilityId, true), facilityVisitId, 32.4F,
        "N", 2, null, "Y", problems, "Notes", "Y", OTHER_MONITORING_DEVICE, "some_other_device", "Y", 50, 10, new Date(116, 4, 8), new Date(116, 4, 9), "Y", new Date(116, 4, 9), 1);

    RefrigeratorReadingDTO refrigeratorReadingDTO = new RefrigeratorReadingDTO(new Refrigerator("brand", "model", "serial number", facilityId, true),
      facilityVisitId,
      temperature,
      functioningCorrectly,
      lowAlarmEvents,
      highAlarmEvents,
      problemSinceLastTime,
      problems.transform(),
      notes,
      hasMonitoringDevice,
      monitoringDeviceType,
      monitoringDeviceOtherType,
      temperatureReportingForm,
      highestTemperatureReported,
      lowestTemperatureReported,
      problemOccurredDate,
      problemReportedDate,
      equipmentRepaired,
      equipmentRepairedDate,
      totalDaysCceUptime);

    RefrigeratorReading refrigeratorReading = refrigeratorReadingDTO.transform();

    assertThat(refrigeratorReading.getFacilityVisitId(), is(expectedReading.getFacilityVisitId()));
    assertThat(refrigeratorReading.getTemperature(), is(expectedReading.getTemperature()));
    assertThat(refrigeratorReading.getFunctioningCorrectly(), is(expectedReading.getFunctioningCorrectly()));
    assertThat(refrigeratorReading.getLowAlarmEvents(), is(expectedReading.getLowAlarmEvents()));
    assertThat(refrigeratorReading.getHighAlarmEvents(), is(expectedReading.getHighAlarmEvents()));
    assertThat(refrigeratorReading.getProblemSinceLastTime(), is(expectedReading.getProblemSinceLastTime()));

    assertThat(refrigeratorReading.getProblem(), is(notNullValue()));
    assertThat(refrigeratorReading.getProblem().getReadingId(), is(expectedReading.getProblem().getReadingId()));
    assertThat(refrigeratorReading.getProblem().getOperatorError(), is(expectedReading.getProblem().getOperatorError()));
    assertThat(refrigeratorReading.getProblem().getBurnerProblem(), is(expectedReading.getProblem().getBurnerProblem()));
    assertThat(refrigeratorReading.getProblem().getGasLeakage(), is(expectedReading.getProblem().getGasLeakage()));
    assertThat(refrigeratorReading.getProblem().getEgpFault(), is(expectedReading.getProblem().getEgpFault()));
    assertThat(refrigeratorReading.getProblem().getThermostatSetting(), is(expectedReading.getProblem().getThermostatSetting()));
    assertThat(refrigeratorReading.getProblem().getOther(), is(expectedReading.getProblem().getOther()));
    assertThat(refrigeratorReading.getProblem().getOtherProblemExplanation(), is(expectedReading.getProblem().getOtherProblemExplanation()));

    assertThat(refrigeratorReading.getNotes(), is(expectedReading.getNotes()));
  }

  @Test
  public void shouldValidateRefrigeratorProblemsIfFunctioningCorrectlySinceLastVisitIsFalse() throws Exception {
    Long facilityVisitId = 5L;
    Reading temperature = new Reading("32.4", false);
    Reading functioningCorrectly = new Reading("N", false);
    Reading lowAlarmEvents = new Reading("2", false);
    Reading highAlarmEvents = new Reading("", true);
    Reading problemSinceLastTime = new Reading("Y", false);
    RefrigeratorProblem problems = mock(RefrigeratorProblem.class);
    RefrigeratorProblemDTO problem = mock(RefrigeratorProblemDTO.class);
    Reading hasMonitoringDevice = new Reading("Y", false);
    Reading monitoringDeviceType = new Reading("OTHER_MONITORING_DEVICE", false);
    Reading monitoringDeviceOtherType = new Reading("some_other_device", false);
    Reading temperatureReportingForm = new Reading("Y", false);
    Reading highestTemperatureReported = new Reading("50", false);
    Reading lowestTemperatureReported = new Reading("10", false);
    Reading problemOccurredDate = new Reading("08/05/2016", false);
    Reading problemReportedDate = new Reading("09/05/2016", false);
    Reading equipmentRepaired = new Reading("Y", false);
    Reading equipmentRepairedDate = new Reading("09/05/2016", false);
    Reading totalDaysCceUptime = new Reading("1", false);
    String notes = "Notes";
    Long facilityId = 2L;

    doReturn(problem).when(problems).transform();

    RefrigeratorReading expectedReading = new RefrigeratorReading(new Refrigerator("brand", "model", "serial number", facilityId, true), facilityVisitId, 32.4F,
        "N", 2, null, "Y", problems, notes, "Y", OTHER_MONITORING_DEVICE, "some_other_device", "Y", 50, 10, new Date(116, 4, 8), new Date(116, 4, 9), "Y", new Date(116, 4, 9), 1);

    RefrigeratorReadingDTO refrigeratorReadingDTO = new RefrigeratorReadingDTO(new Refrigerator("brand", "model", "serial number", facilityId, true),
      facilityVisitId,
      temperature,
      functioningCorrectly,
      lowAlarmEvents,
      highAlarmEvents,
      problemSinceLastTime,
      problem,
      new Reading(notes),
      hasMonitoringDevice,
      monitoringDeviceType,
      monitoringDeviceOtherType,
      temperatureReportingForm,
      highestTemperatureReported,
      lowestTemperatureReported,
      problemOccurredDate,
      problemReportedDate,
      equipmentRepaired,
      equipmentRepairedDate,
      totalDaysCceUptime);

    RefrigeratorReading refrigeratorReading = refrigeratorReadingDTO.transform();

    assertThat(refrigeratorReading, is(expectedReading));
    verify(problems).validate();
  }

  @Test
  public void shouldSetEmptyProblemsIfProblemSinceLastVisitIsFalse() throws Exception {
    Long facilityVisitId = 5L;
    Reading temperature = new Reading("32.4", false);
    Reading functioningCorrectly = new Reading("Y", false);
    Reading lowAlarmEvents = new Reading("2", false);
    Reading highAlarmEvents = new Reading("", true);
    Reading problemSinceLastTime = new Reading("N", false);
    RefrigeratorProblem problems = new RefrigeratorProblem();
    Reading hasMonitoringDevice = new Reading("Y", false);
    Reading monitoringDeviceType = new Reading("OTHER_MONITORING_DEVICE", false);
    Reading monitoringDeviceOtherType = new Reading("some_other_device", false);
    Reading temperatureReportingForm = new Reading("Y", false);
    Reading highestTemperatureReported = new Reading("50", false);
    Reading lowestTemperatureReported = new Reading("10", false);
    Reading problemOccurredDate = new Reading("08/05/2016", false);
    Reading problemReportedDate = new Reading("09/05/2016", false);
    Reading equipmentRepaired = new Reading("Y", false);
    Reading equipmentRepairedDate = new Reading("09/05/2016", false);
    Reading totalDaysCceUptime = new Reading("1", false);
    RefrigeratorProblemDTO problem = problems.transform();
    String notes = "Notes";
    Long facilityId = 2L;

    RefrigeratorReadingDTO refrigeratorReadingDTO = new RefrigeratorReadingDTO(new Refrigerator("brand", "model", "serial number", facilityId, true),
      facilityVisitId,
      temperature,
      functioningCorrectly,
      lowAlarmEvents,
      highAlarmEvents,
      problemSinceLastTime,
      problem,
      new Reading(notes),
      hasMonitoringDevice,
      monitoringDeviceType,
      monitoringDeviceOtherType,
      temperatureReportingForm,
      highestTemperatureReported,
      lowestTemperatureReported,
      problemOccurredDate,
      problemReportedDate,
      equipmentRepaired,
      equipmentRepairedDate,
      totalDaysCceUptime);

    RefrigeratorReading refrigeratorReading = refrigeratorReadingDTO.transform();

    RefrigeratorReading expectedReading = new RefrigeratorReading(new Refrigerator("brand", "model", "serial number", facilityId, true), facilityVisitId, 32.4F,
        "Y", 2, null, "N", null, notes, "Y", OTHER_MONITORING_DEVICE, "some_other_device", "Y", 50, 10, new Date(116, 4, 8), new Date(116, 4, 9), "Y", new Date(116, 4, 9), 1);

    assertThat(refrigeratorReading, is(expectedReading));
    assertThat(refrigeratorReading.getProblem(), is(equalTo(problems)));

    assertThat(problems.getOperatorError(), is(nullValue()));
    assertThat(problems.getBurnerProblem(), is(nullValue()));
    assertThat(problems.getGasLeakage(), is(nullValue()));
    assertThat(problems.getEgpFault(), is(nullValue()));
    assertThat(problems.getThermostatSetting(), is(nullValue()));
    assertThat(problems.getOther(), is(nullValue()));
    assertThat(problems.getOtherProblemExplanation(), is(nullValue()));
  }
}
