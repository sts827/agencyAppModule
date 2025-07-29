package kr.co.wayplus.travel.service.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.front.ProductMapper;
import kr.co.wayplus.travel.model.ProductDetailSchedule;
import kr.co.wayplus.travel.model.ProductInfo;
import kr.co.wayplus.travel.model.ProductPriceOption.DayList;
import kr.co.wayplus.travel.model.ProductPriceOption.FixPriceList;
import kr.co.wayplus.travel.model.ProductTourImages;

@Service
public class PrdocutService {

    private final ProductMapper mapper;

    @Autowired
    public PrdocutService(ProductMapper mapper) {
        this.mapper = mapper;
    }
}
