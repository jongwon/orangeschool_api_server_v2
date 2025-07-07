package com.orangeschool.orangeschoolapiserver.common;

import com.orangeschool.orangeschoolapiserver.common.dto.request.ExcelRequestDto;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import com.orangeschool.orangeschoolapiserver.common.utils.ExcelManagement;
import com.orangeschool.orangeschoolapiserver.domain.academy.entity.Academy;
import com.orangeschool.orangeschoolapiserver.domain.academy.repository.AcademyRepository;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "엑셀", description = "excel")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ExcelController {

    private final CommonMemberRepository commonMemberRepository;
    private final AcademyRepository academyRepository;
    private final ExcelManagement excelManagement;

    @Operation(summary = "회원 엑셀 다운로드")
    @PostMapping("/admin/excel/commonMember")
    public void getCommonMemberExcel(@RequestBody ExcelRequestDto excelRequestDto, HttpServletRequest request, HttpServletResponse response) {
        OutputStream fileOut = null;
        SXSSFWorkbook workbook = null;
        try {
            List<CommonMember> commonMemberList = new ArrayList<>();
            if(excelRequestDto.getIsAll()) {
                if(excelRequestDto.getMemberType() == MemberType.PARENT) {
                    commonMemberList =  commonMemberRepository.findByMemberType(MemberType.PARENT);
                }else {
                    commonMemberList =  commonMemberRepository.findByMemberType(MemberType.CHILD);
                }
            }else {
                commonMemberList =  commonMemberRepository.findByIdIn(excelRequestDto.getIdList());
            }
            workbook = excelManagement.getCommonMemberExcel(commonMemberList);
            response.setContentType("Application/download;charset=utf-8");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Disposition", "attachment; filename=" + excelManagement.fileNameEncoding("excel.xlsx", request) + ";");
            fileOut = response.getOutputStream();
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOut != null)
                    fileOut.close();
                if (workbook != null)
                    workbook.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Operation(summary = "학원 엑셀 다운로드")
    @PostMapping("/admin/excel/academy")
    public void getAcademyExcel(@RequestBody ExcelRequestDto excelRequestDto, HttpServletRequest request, HttpServletResponse response) {
        OutputStream fileOut = null;
        SXSSFWorkbook workbook = null;
        try {
            List<Academy> academyList = new ArrayList<>();
            if(excelRequestDto.getIsAll()) {
                academyList =  academyRepository.findAll();
            }else {
                academyList =  academyRepository.findByIdIn(excelRequestDto.getIdList());
            }
            workbook = excelManagement.getAcademyExcel(academyList);
            response.setContentType("Application/download;charset=utf-8");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Disposition", "attachment; filename=" + excelManagement.fileNameEncoding("excel.xlsx", request) + ";");
            fileOut = response.getOutputStream();
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOut != null)
                    fileOut.close();
                if (workbook != null)
                    workbook.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
