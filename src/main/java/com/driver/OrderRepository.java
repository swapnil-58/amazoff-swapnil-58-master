package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String,Order> orderMap = new HashMap<>();
    HashMap<String,DeliveryPartner>  deliveryPartnerMap = new HashMap<>();
    HashMap<String,ArrayList<String>> orderPartnerPairMap = new HashMap<>();
    HashMap<String, Boolean> assignMap = new HashMap<>();
    public void addOrder(Order order) {    // 1. add order to hashmap...
        String id = order.getId();
        if(!orderMap.containsKey(id))
        orderMap.put(id, order);
    }

    public void addPartner(String id) {   // 2. add delivery partner to hashmap...
        DeliveryPartner dp = new DeliveryPartner(id);
        if(!deliveryPartnerMap.containsKey(id))
        deliveryPartnerMap.put(id, dp);
    }

    public void addOrderPartnerPair(String oId, String pId) {     // 3.adding partner pair...
        if(orderMap.containsKey(oId) && deliveryPartnerMap.containsKey(pId)) {
            if(!orderPartnerPairMap.containsKey(pId) || !orderPartnerPairMap.get(pId).contains(oId)) {
                orderPartnerPairMap.get(pId).add(oId);
                int orders = deliveryPartnerMap.get(pId).getNumberOfOrders();
                deliveryPartnerMap.get(pId).setNumberOfOrders(orders+1);
                assignMap.put(oId, true);
            }
        }
    }

    public Order getOrderById(String id) {    // 4. get order by id...
        if(orderMap.containsKey(id))
            return orderMap.get(id);
        else
            return null;
    }

    public DeliveryPartner getPartnerById(String id) {   // 5. get delivery partner by id...
        if(deliveryPartnerMap.containsKey(id)) {
            return deliveryPartnerMap.get(id);
        }
        else return null;
    }

    public Integer getOrderCountByPartnerId(String id) {  // 6. get count of number of orders...
        if(deliveryPartnerMap.containsKey(id)) {
            return deliveryPartnerMap.get(id).getNumberOfOrders();
        }
        return 0;
    }

    public List<String> getOrdersByPartnerId(String id) {   // 7. orders  by partner...
        if(orderPartnerPairMap.containsKey(id)) {
            return orderPartnerPairMap.get(id);
        }
        return null;
    }

    public List<String> getAllOrders() {        // 8. get all orders...
        List<String> ans = new ArrayList<>();

        for(String i: orderMap.keySet()) {
            ans.add(i);
        }
        if(!orderMap.isEmpty())
        return ans;
        else
            return null;
    }

    public Integer getCountOfUnassignedOrders() {   // 9. count of unassigned integers...

        int count = 0;
        if(!orderMap.isEmpty()) {
            for (String i : assignMap.keySet()) {
                if (assignMap.get(i) == false)
                    count++;
            }
        }
        return count;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String id) {  // 10. count of orders that left undelivered...

        String hr = time.substring(0,2);
        String min = time.substring(3);
        int hh = Integer.parseInt(hr);
        int mm = Integer.parseInt(min);
        int tempTime =  hh*60 + mm;
        int count = 0;

       if(orderPartnerPairMap.containsKey(id)) {
           ArrayList<String> temp = orderPartnerPairMap.get(id);
           int n = temp.size();

           for(String i: temp) {
               if(orderMap.get(i).getDeliveryTime() >tempTime)
                   count++;
           }
       }
       return count;
    }

    public String getLastDeliveryTimeByPartnerId(String id) {    // 11.last delivery
        int max = 0;
        if(orderPartnerPairMap.containsKey(id)) {
            ArrayList<String> orderList = orderPartnerPairMap.get(id);


           for(String idx: orderList) {
               max = Math.max(max, orderMap.get(idx).getDeliveryTime());
           }
        }
       int hr = max/60;
        int min = max%60;
        String HH = String.valueOf(hr);
        String mm = String.valueOf(min);
        if(HH.length() == 1) {
            HH = "0"+HH;
        }
        if(mm.length() == 1) {
            mm = "0"+mm;
        }
        String time = HH+":"+mm;
        return time;
    }

    public void deletePartnerById(String id) {
        if(deliveryPartnerMap.containsKey(id)) {
            deliveryPartnerMap.remove(id);
            ArrayList<String> temp = orderPartnerPairMap.get(id);

            for (String i : temp) {
                assignMap.put(i, false);
            }
        }

        if(orderPartnerPairMap.containsKey(id))
        orderPartnerPairMap.remove(id);
    }

    public void deleteOrderById(String id) {
        if(orderMap.containsKey(id)) orderMap.remove(id);

        for(String i: orderPartnerPairMap.keySet()) {

            ArrayList<String> temp = orderPartnerPairMap.get(i);
            if(temp.contains(id))
                temp.remove(id);

        }

        if(assignMap.containsKey(id)) assignMap.remove(id);
    }

}
