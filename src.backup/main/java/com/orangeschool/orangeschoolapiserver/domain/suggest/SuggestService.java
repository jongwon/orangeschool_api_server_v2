package com.orangeschool.orangeschoolapiserver.domain.suggest;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.suggest.dto.CreateSuggestDto;
import com.orangeschool.orangeschoolapiserver.domain.suggest.dto.SuggestDto;
import com.orangeschool.orangeschoolapiserver.domain.suggest.entity.Suggest;
import com.orangeschool.orangeschoolapiserver.domain.suggest.repository.SuggestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SuggestService {

    private final SuggestRepository suggestRepository;
    private final CommonMemberRepository commonMemberRepository;

    @Transactional(readOnly = true)
    public Page<SuggestDto> get(Pageable pageable, KeywordSearchDto keywordSearchDto) throws Exception {

        return suggestRepository.search(pageable, keywordSearchDto);
    }

    @Transactional(readOnly = true)
    public SuggestDto getById(Long suggestId) throws Exception {

        Optional<Suggest> suggestOptional = suggestRepository.findById(suggestId);
        if (!suggestOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }
        return SuggestDto.create(suggestOptional.get());
    }

    @Transactional
    public void delete(Long suggestId) throws Exception {
        Optional<Suggest> suggestOptional = suggestRepository.findById(suggestId);
        if (!suggestOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }
        suggestRepository.delete(suggestOptional.get());
    }


    @Transactional
    public void create(Long suggesterId, CreateSuggestDto createSuggestDto) throws Exception {
        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(suggesterId);
        if (!commonMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }
        CommonMember commonMember = commonMemberOptional.get();

        Suggest suggest = Suggest.builder()
                .commonMember(commonMember)
                .title(createSuggestDto.getTitle())
                .content(createSuggestDto.getContent())
                .build();

        suggestRepository.save(suggest);
    }


}
