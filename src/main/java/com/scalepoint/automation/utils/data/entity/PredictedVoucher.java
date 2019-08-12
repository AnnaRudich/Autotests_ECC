package com.scalepoint.automation.utils.data.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Builder
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PredictedVoucher implements Serializable {

    private String voucherName;
}
