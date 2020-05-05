package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@ToString
public class Suborders {
    List<Suborder> suborders = new ArrayList<>();
}
