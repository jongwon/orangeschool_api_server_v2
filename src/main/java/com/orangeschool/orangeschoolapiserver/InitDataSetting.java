package com.orangeschool.orangeschoolapiserver;

import com.orangeschool.orangeschoolapiserver.common.enums.ManagerAuthority;
import com.orangeschool.orangeschoolapiserver.domain.location.entity.Location;
import com.orangeschool.orangeschoolapiserver.domain.location.repository.LocationRepository;
import com.orangeschool.orangeschoolapiserver.domain.manager.entity.Manager;
import com.orangeschool.orangeschoolapiserver.domain.manager.repository.ManagerRepository;
import com.orangeschool.orangeschoolapiserver.domain.terms.entity.Terms;
import com.orangeschool.orangeschoolapiserver.domain.terms.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@RequiredArgsConstructor
public class InitDataSetting {

    private final LocationRepository locationRepository;
    private final ManagerRepository managerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TermsRepository termsRepository;

    @PostConstruct
    public void init() {

        Optional<Location> locationOptional = locationRepository.findByCode(1100000000L);

        if (locationOptional.isEmpty()) {

            Map<Long, String> dataMap = new HashMap<>();

            dataMap.put(1100000000L, "서울특별시");
            dataMap.put(2600000000L, "부산광역시");
            dataMap.put(2800000000L, "인천광역시");
            dataMap.put(2700000000L, "대구광역시");
            dataMap.put(3000000000L, "대전광역시");
            dataMap.put(2900000000L, "광주광역시");
            dataMap.put(3100000000L, "울산광역시");
            dataMap.put(3611000000L, "세종특별자치시");
            dataMap.put(4100000000L, "경기도");
            dataMap.put(4300000000L, "충청북도");
            dataMap.put(4400000000L, "충청남도");
            dataMap.put(4500000000L, "전라북도");
            dataMap.put(4600000000L, "전라남도");
            dataMap.put(4700000000L, "경상북도");
            dataMap.put(4800000000L, "경상남도");
            dataMap.put(5100000000L, "강원특별자치도");
            dataMap.put(5000000000L, "제주특별자치도");

            List<Location> locationList = new ArrayList<>();

            for (Map.Entry<Long, String> entry : dataMap.entrySet()) {
                Long code = entry.getKey();
                String title = entry.getValue();

                Location location = Location.builder()
                        .code(code)
                        .title(title)
                        .build();
                locationList.add(location);
            }

            locationRepository.saveAll(locationList);
        }

        Optional<Manager> managerOptional = managerRepository.findById(1L);

        if (managerOptional.isEmpty()) {

            Manager manager = Manager.builder()
                    .password(passwordEncoder.encode("1q2w3e4r!@#"))
                    .name("최고관리자")
                    .email("admin@naver.com")
                    .managerAuthority(ManagerAuthority.ROOT)
                    .isApproved(true)
                    .isActive(true)
                    .accessMenu("")
                    .build();

            managerRepository.save(manager);
        }

        Optional<Terms> termsOptional = termsRepository.findById(1L);

        if (termsOptional.isEmpty()) {

            List<String> titleList = new ArrayList<>();
            titleList.add("만 14세 이상입니다");
            titleList.add("회원이용약관");
            titleList.add("개인정보수집 및 이용동의");
            titleList.add("개인정보 제 3자 제공 동의");
            titleList.add("이메일 및 SMS 마케팅정보수신 동의");
            titleList.add("서비스 알림 수신 동의");
            titleList.add("광고성 알림 수신 동의");

            List<Terms> termsList = new ArrayList<>();

            for (int i = 0; i < titleList.size(); i++) {
                String title = titleList.get(i);

                Terms terms = Terms.builder()
                        .title(title)
                        .content("")
                        .build();
                termsList.add(terms);
            }

            termsRepository.saveAll(termsList);
        }
    }
}
