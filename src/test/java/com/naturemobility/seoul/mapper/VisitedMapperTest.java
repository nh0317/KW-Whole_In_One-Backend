package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.paging.PageInfo;
import com.naturemobility.seoul.domain.visited.GetVisitedByUserIdx;
import com.naturemobility.seoul.domain.visited.VisitedInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VisitedMapperTest {

    @Autowired
    VisitedMapper visitedMapper;
    @Test
    void findAllByUserIdx() {
        VisitedInfo user = new VisitedInfo(1L);
        Integer total = visitedMapper.cntTotalVisited(1L);
        if(total>0) {
            user.setTotalPage(total);

            List<GetVisitedByUserIdx> visited;
            visited = visitedMapper.findAllByUserIdx(user);
            for (GetVisitedByUserIdx v : visited) {
                System.out.println(v.toString());
            }
        }
    }
}