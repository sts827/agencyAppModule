package kr.co.wayplus.travel.service.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.front.ContentsMapper;

@Service
public class ContentsService {

    private final ContentsMapper mapper;

    @Autowired
    public ContentsService(ContentsMapper mapper) {
        this.mapper = mapper;
    }
}
