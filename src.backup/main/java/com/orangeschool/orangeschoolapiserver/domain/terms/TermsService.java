package com.orangeschool.orangeschoolapiserver.domain.terms;

import com.orangeschool.orangeschoolapiserver.common.dto.request.IdListDto;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.terms.dto.CreateTermsDto;
import com.orangeschool.orangeschoolapiserver.domain.terms.dto.TermsDto;
import com.orangeschool.orangeschoolapiserver.domain.terms.dto.UpdateTermsDto;
import com.orangeschool.orangeschoolapiserver.domain.terms.entity.Terms;
import com.orangeschool.orangeschoolapiserver.domain.terms.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TermsService {

    private final TermsRepository termsRepository;

    @Transactional
    public void create(CreateTermsDto createTermsDto) throws Exception {

        Terms terms = Terms.builder()
                .title(createTermsDto.getTitle())
                .content(createTermsDto.getContent())
                .build();

        termsRepository.save(terms);
    }

    @Transactional(readOnly = true)
    public Page<TermsDto> get(Pageable pageable) throws Exception {
        return termsRepository.search(pageable);
    }

    @Transactional(readOnly = true)
    public TermsDto getById(Long termsId) throws Exception {

        Optional<Terms> termsOptional = termsRepository.findById(termsId);

        if (termsOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return TermsDto.create(termsOptional.get());
    }

    @Transactional
    public void put(Long termsId, UpdateTermsDto updateTermsDto) throws Exception {

        Optional<Terms> termsOptional = termsRepository.findById(termsId);

        if (termsOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Terms terms = termsOptional.get();
        terms.update(
                updateTermsDto.getTitle(),
                updateTermsDto.getContent());

        termsRepository.save(terms);
    }

    @Transactional
    public void delete(Long termsId) throws Exception {
        termsRepository.deleteById(termsId);
    }

    @Transactional
    public void deleteAll(IdListDto idListDto) throws Exception {
        termsRepository.deleteAllById(idListDto.getIdList());
    }
}
