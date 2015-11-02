package org.openlmis.vaccine.domain.VaccineOrderRequisition;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.*;
import org.openlmis.vaccine.dto.OrderRequisitionDTO;
import org.openlmis.vaccine.dto.OrderRequisitionStockCardDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.openlmis.vaccine.utils.ListUtil.emptyIfNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VaccineOrderRequisition extends BaseModel {
    public static final SimpleDateFormat form = new SimpleDateFormat("YYYY-MM-dd");
    private Long periodId;
    private Long programId;
    private VaccineOrderStatus status;
    private ProcessingPeriod period;
    private Facility facility;
    private Program program;
    private Long supervisoryNodeId;
    private Long facilityId;
    private String orderDate;
    private boolean emergency;
    private List<VaccineOrderRequisitionLineItem> lineItems;
    private List<VaccineOrderRequisitionStatusChange> statusChanges;
    private List<OrderRequisitionDTO> orderRequisitionDTOs;
    private List<OrderRequisitionStockCardDTO> stockCards;
    private List<VaccineOrderRequisitionColumns> columnsList;

    public void viewOrderRequisitionLineItems(List<OrderRequisitionStockCardDTO> stockCards, List<ProgramProduct> programProducts) {
        lineItems = new ArrayList<>();

       for(ProgramProduct programProduct :emptyIfNull(programProducts)) {

           for (OrderRequisitionStockCardDTO stockCard : stockCards) {

               VaccineOrderRequisitionLineItem lineItem = new VaccineOrderRequisitionLineItem();

               lineItem.setOrderId(id);
               lineItem.setProductId(stockCard.getProduct().getId());
               lineItem.setProductName(stockCard.getProduct().getPrimaryName());
               lineItem.setMaxmonthsofstock(stockCard.getMaxmonthsofstock());
               lineItem.setOverriddenisa(-1 /*stockCard.getOverriddenisa()*/);
               lineItem.setEop(stockCard.getEop());
               lineItem.setStockOnHand(stockCard.getTotalQuantityOnHand());
               lineItem.setMinMonthsOfStock(stockCard.getMinmonthsofstock());
               lineItem.setOrderedDate(form.format(new Date()));
               if (stockCard.getProduct().getPrimaryName().equals(programProduct.getProduct().getPrimaryName())){
                   lineItem.setProductCategory(programProduct.getProductCategory().getName());
               }

               lineItems.add(lineItem);
           }
       }
    }


    public void initiateOrderRequisitionLineItem(List<ProgramProduct>programProducts){
        orderRequisitionDTOs = new ArrayList<>();
        for(ProgramProduct pp: programProducts){
            OrderRequisitionDTO lineItem = new OrderRequisitionDTO();
            lineItem.setProductCategory(pp.getProductCategory().getName());
            orderRequisitionDTOs.add(lineItem);
        }
    }

    public void initColumns(List<VaccineOrderRequisitionColumns> columns){
        columnsList = new ArrayList<>();
        for(VaccineOrderRequisitionColumns columns1: columns){
            VaccineOrderRequisitionColumns lineItem = new VaccineOrderRequisitionColumns();
            lineItem.setName(lineItem.getName());
            lineItem.setLabel(lineItem.getLabel());
            lineItem.setDisplayOrder(columns1.getDisplayOrder());
            columnsList.add(lineItem);
        }

    }

}
