package com.scalepoint.automation.utils.data.entity.order;

import com.scalepoint.automation.shared.Locale;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class ShippingAddress {

   String firstName;//Gerald
   String lastName;//Monroe
   String addressLine1 = "";
   String addressLine2 = "";
   String zipCode = "";
   String city = "Copenhagen";
   String state= "";
   String country = Locale.DK.getValue();//DK
   String mobilePhone = "";
   String phone = "";
}
