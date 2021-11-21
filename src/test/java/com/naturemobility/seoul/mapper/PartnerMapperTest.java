package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.partners.PartnerInfo;
import com.naturemobility.seoul.domain.partners.PartnerStatus;
import com.naturemobility.seoul.domain.users.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class PartnerMapperTest {
    @Autowired
    PartnerMapper partnerMapper;

    @Test
    void saveAndFindByIdx() {
        PartnerInfo partnerInfo = new PartnerInfo("김하나","kim@naver.com","1234");
        log.info(partnerInfo.toString());
        partnerMapper.save(partnerInfo);
        PartnerInfo result=partnerMapper.findByIdx(partnerInfo.getPartnerIdx()).get();
        log.info(result.toString());
        assertThat(partnerInfo.getPartnerIdx()).isEqualTo(result.getPartnerIdx());
    }

    @Test
    void update() {
        PartnerInfo partnerInfo = new PartnerInfo("김하나","kim@naver.com","1234");
        log.info(partnerInfo.toString());
        partnerMapper.save(partnerInfo);
        PartnerInfo result=partnerMapper.findByIdx(partnerInfo.getPartnerIdx()).get();
        result.setPartnerName("김연경");
        PartnerInfo result2=partnerMapper.findByIdx(partnerInfo.getPartnerIdx()).get();
        partnerMapper.update(result);
        log.info(result.toString());
        assertThat(result.getPartnerName()).isEqualTo(result2.getPartnerName());
    }

    @Test
    void delete() {
        PartnerInfo partnerInfo = new PartnerInfo("김하나","kim@naver.com","1234");
        log.info(partnerInfo.toString());
        partnerMapper.save(partnerInfo);
        partnerMapper.delete(partnerInfo);
        PartnerInfo result=partnerMapper.findByIdx(partnerInfo.getPartnerIdx()).orElseGet(()->null);
        assertThat(result).isEqualTo(null);
    }

    @Test
    void findByStatus() {
        PartnerInfo partnerInfo = new PartnerInfo("김하나","kim@naver.com","1234");
        log.info(partnerInfo.toString());
        partnerMapper.save(partnerInfo);
        PartnerInfo result=partnerMapper.findByIdx(partnerInfo.getPartnerIdx()).get();
        result.setPartnerStatus(PartnerStatus.WITHDRAWN.getValue());
        partnerMapper.update(result);
        List<PartnerInfo> partners = partnerMapper.findByStatus(PartnerStatus.WITHDRAWN.getValue());
        for (PartnerInfo partner : partners){
            assertThat(partner.getPartnerStatus()).isEqualTo(PartnerStatus.WITHDRAWN.getValue());
        }
    }

    @Test
    void findByEmail() {
        PartnerInfo partnerInfo = new PartnerInfo("김하나","kim@naver.com","1234");
        log.info(partnerInfo.toString());
        partnerMapper.save(partnerInfo);
        List<PartnerInfo> partners = partnerMapper.findByEmail(partnerInfo.getPartnerEmail());
        for (PartnerInfo partner : partners){
            assertThat(partner.getPartnerEmail()).isEqualTo(partnerInfo.getPartnerEmail());
        }
    }

    @Test
    void findByEmailAndStatus() {
        PartnerInfo partnerInfo = new PartnerInfo("김하나","kim@naver.com","1234");
        log.info(partnerInfo.toString());
        partnerMapper.save(partnerInfo);
        List<PartnerInfo> partners = partnerMapper.findByEmailAndStatus(partnerInfo.getPartnerEmail(),PartnerStatus.ACTIVE.getValue());
        for (PartnerInfo partner : partners){
            assertThat(partner.getPartnerEmail()).isEqualTo(partnerInfo.getPartnerEmail());
            assertThat(partner.getPartnerStatus()).isEqualTo(PartnerStatus.ACTIVE.getValue());
        }
    }
}