/*
 * This program was produced for the U.S. Agency for International Development. It was prepared by the USAID | DELIVER PROJECT, Task Order 4. It is part of a project which utilizes code originally licensed under the terms of the Mozilla Public License (MPL) v2 and therefore is licensed under MPL v2 or later.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the Mozilla Public License as published by the Mozilla Foundation, either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Mozilla Public License for more details.
 *
 * You should have received a copy of the Mozilla Public License along with this program. If not, see http://www.mozilla.org/MPL/
 */
package org.openlmis.report.mapper.lookup;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.report.model.dto.RnRStatusSummaryReport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RnRStatusSummaryReportMapper {


    @Select("select count(rnrid) totalStatus,status from vw_number_rnr_created where requisitiongroupid = #{requisitionGroupId} order by status")
    public List<RnRStatusSummaryReport> getRnRStatusSummaryData(@Param("requisitionGroupId") Long requisitionGroupId);

    @Select("select facilitycode,facilityname,facilitytypename,createddate,status  from vw_rnr_status_details \n" +
            " where requisitiongroupid = #{requisitiongroupid} and programid=#{programId} and periodid=#{periodId}\n" +
            "            group by facilitycode,facilityname,facilitytypename,createddate,status order by facilityname ")
    public List<RnRStatusSummaryReport> getRnRStatusDetails(@Param("requisitionGroupId") Long requisitionGroupId, @Param("programId") Long programId, @Param("periodId") Long periodId);

    @Select("SELECT\n" +
            "programs.name AS programname,\n" +
            "programs.id AS programid,\n" +
            "vw_facility_requisitions.periodid,\n" +
            "processing_schedules.name AS periodname,\n" +
            "vw_facility_requisitions.geographiczoneid AS geographiczoneid,\n" +
            "vw_facility_requisitions.geographiczonename,\n" +
            "facility_types.name AS facilitytypename,\n" +
            "vw_facility_requisitions.facilityid,\n" +
            "vw_facility_requisitions.facilitycode,\n" +
            "vw_facility_requisitions.facilityname,\n" +
            "vw_facility_requisitions.rnrid,\n" +
            "vw_facility_requisitions.status,\n" +
            "vw_facility_requisitions.createddate, " +
            "d.region_name AS region " +
            "FROM\n" +
            "vw_facility_requisitions\n" +
            "INNER JOIN programs ON programs.id = vw_facility_requisitions.programid\n" +
            "INNER JOIN facility_types ON facility_types.id = vw_facility_requisitions.typeid\n" +
            "INNER JOIN processing_periods ON processing_periods.id = vw_facility_requisitions.periodid\n" +
            "INNER JOIN processing_schedules ON processing_schedules.id = processing_periods.scheduleid\n" +
            "INNER JOIN vw_districts d on vw_facility_requisitions.geographiczoneid = d.district_id  "+
            "WHERE vw_facility_requisitions.geographiczoneid in (select geographiczoneid from fn_get_user_geographiczone_children(#{userId}::int,#{zoneId}::int))\n" +
            "AND programid = #{programId}\n" +
            "AND periodid = #{periodId}\n" +
            "AND  status= #{status}\n" +
            "AND status in ('APPROVED','AUTHORIZED','IN_APPROVAL','RELEASED') ")
    List<RnRStatusSummaryReport> getRnRStatusDetail(@Param("userId") Long userId, @Param("periodId") Long periodId, @Param("programId") Long programId, @Param("zoneId") Long zoneId, @Param("status") String status);


    @Select("SELECT\n" +
            "vw_facility_requisitions.status,\n" +
            "count(*) totalStatus\n" +
            "FROM vw_facility_requisitions " +
            "INNER JOIN programs ON programs.id = vw_facility_requisitions.programid\n" +
            "where vw_facility_requisitions.geographiczoneid in (select geographiczoneid from fn_get_user_geographiczone_children(#{userId}::int,#{zoneId}::int))\n" +
            "and vw_facility_requisitions.programid = #{programId} " +
            "and vw_facility_requisitions.periodid = #{periodId} " +
            "and status in ('IN_APPROVAL','AUTHORIZED','APPROVED','RELEASED') and emergency = false " +
            "GROUP BY vw_facility_requisitions.status ")
    public List<RnRStatusSummaryReport> getRnRStatusSummary(@Param("userId") Long userId, @Param("zoneId") Long zoneId, @Param("periodId") Long periodId,
                                                            @Param("programId") Long programId);

    @Select("SELECT\n" +
            "vw_facility_requisitions.status,\n" +
            "count(*) totalEmergencyRnRStatus\n" +
            "FROM vw_facility_requisitions " +
            "INNER JOIN programs ON programs.id = vw_facility_requisitions.programid\n" +
            "where vw_facility_requisitions.geographiczoneid in (select geographiczoneid from fn_get_user_geographiczone_children(#{userId}::int,#{zoneId}::int))\n" +
            "and vw_facility_requisitions.programid = #{programId} " +
            "and vw_facility_requisitions.periodid = #{periodId} " +
            "and status in ('IN_APPROVAL','AUTHORIZED','APPROVED','RELEASED') and emergency = true " +
            "GROUP BY vw_facility_requisitions.status ")
    public List<RnRStatusSummaryReport> getEmergencyRnRStatusSummary(@Param("userId") Long userId, @Param("zoneId") Long zoneId, @Param("periodId") Long periodId,
                                                            @Param("programId") Long programId);


    @Select("select programname, status, count(rnrid) totalStatus from vw_rnr_status" +
            "where  requisitiongroupid = #{requisitiongroupId} and periodid = #{periodId} " +
            "and status in ('APPROVED','AUTHORIZED','IN_APPROVAL','RELEASED') " +
            "group by programname, status " +
            "order by status")
    public List<RnRStatusSummaryReport> getRnRStatusByRequisitionGroupAndPeriodData(@Param("requisitionGroupId") Long requisitionGroupId, @Param("periodId") Long periodId);


    @Select("WITH Q as (select x.status,x.totalRnRStatus,y.expected FROM \n" +
            "(SELECT status,count(*) totalRnRStatus FROM requisition_status_changes\n" +
            "WHERE rnrId IN \n" +
            "(SELECT id from requisitions \n" +
            "JOIN vw_facility_requisitions ON requisitions.ID = vw_facility_requisitions.RNRid \n" +
            " WHERE requisitions.programId=#{programId} AND requisitions.periodId=#{periodId}  and "+
            "requisitions.status NOT IN ('INITIATED', 'SUBMITTED', 'SKIPPED','Not yet started') and requisitions.emergency = false\n" +
            ") and status not in ('INITIATED', 'SUBMITTED', 'SKIPPED','Not yet started')\n" +
            " GROUP BY Status) x,\n" +
            " (SELECT count(*) expected FROM vw_expected_facilities WHERE " +
            "vw_expected_facilities.programId=#{programId} AND  vw_expected_facilities.periodId=#{periodId} "+
            " )y\n" +
            "\n" +
            ") SELECT status,totalRnRStatus,expected,\n" +
            " \n" +
            " ROUND(\n" +
            "  100.0 * (\n" +
            "      SUM(CASE WHEN expected > 0 THEN totalRnRStatus ELSE 0 END) / expected\n" +
            "  ), 0) AS percent_total\n" +
            " FROM q\n" +
            " GROUP BY q.status,q.totalRnRStatus,q.expected\n")
    public List<RnRStatusSummaryReport> getExtraAnalyticsDataForRnRSummary(@Param("periodId") Long periodId,
            @Param("programId") Long programId);
}
