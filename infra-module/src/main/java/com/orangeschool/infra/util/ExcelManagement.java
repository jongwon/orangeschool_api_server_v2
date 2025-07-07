package com.orangeschool.infra.util;

// 도메인 엔티티에 대한 직접 의존성 제거
// import com.orangeschool.orangeschoolapiserver.domain.academy.entity.Academy;
// import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ExcelManagement {

    // TODO: CommonMember 엔티티 대신 DTO를 사용하도록 리팩토링 필요
    /*
    public SXSSFWorkbook getCommonMemberExcel(List<CommonMember> commonMemberList) {
        String[] cellId = new String[]{"번호", "이름", "성별", "생년월일", "휴대폰 번호", "이메일", "주소", "상세주소"};

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(true);
        // 시트 생성
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet("회원");
        // 시트 열 너비 설정
        sheet.setColumnWidth(0, 5000); // 단위 : 1글자(아마도 영어?)너비/256
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 5000);
        sheet.setColumnWidth(7, 5000);

        // 해당 1행의 첫번째 열 셀 생성 - 헤더
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < cellId.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(cellId[i]);
        }
        // 2번째 줄부터 데이터 추가
        int rowIdx = 1;
        for (int i = 0; i < commonMemberList.size(); i++) {
            CommonMember commonMember = commonMemberList.get(i);
            Row bodyRow = sheet.createRow(rowIdx++);
            bodyRow.createCell(0).setCellValue(i + 1);
            bodyRow.createCell(1).setCellValue(commonMember.getName());
            bodyRow.createCell(2).setCellValue(commonMember.getGenderTitle());
            bodyRow.createCell(3).setCellValue(commonMember.getBirth());
            bodyRow.createCell(4).setCellValue(commonMember.getPhoneNumber());
            bodyRow.createCell(5).setCellValue(commonMember.getEmail());
            bodyRow.createCell(6).setCellValue(commonMember.getAddress());
            bodyRow.createCell(7).setCellValue(commonMember.getAddressDetail());
        }
        return workbook;
    }

    */
    
    // TODO: Academy 엔티티 대신 DTO를 사용하도록 리팩토링 필요
    /*
    public SXSSFWorkbook getAcademyExcel(List<Academy> academyList) {
        String[] cellId = new String[]{"번호", "학원명", "학원 등록 유저", "주소", "상세주소"};

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(true);
        // 시트 생성
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet("회원");
        // 시트 열 너비 설정
        sheet.setColumnWidth(0, 5000); // 단위 : 1글자(아마도 영어?)너비/256
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);

        // 해당 1행의 첫번째 열 셀 생성 - 헤더
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < cellId.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(cellId[i]);
        }
        // 2번째 줄부터 데이터 추가
        int rowIdx = 1;
        for (int i = 0; i < academyList.size(); i++) {
            Academy academy = academyList.get(i);
            Row bodyRow = sheet.createRow(rowIdx++);
            bodyRow.createCell(0).setCellValue(i + 1);
            bodyRow.createCell(1).setCellValue(academy.getAcademyName());
            bodyRow.createCell(2).setCellValue(academy.getPostUserEmail());
            bodyRow.createCell(3).setCellValue(academy.getAddress());
            bodyRow.createCell(4).setCellValue(academy.getAddressDetail());
        }
        return workbook;
    }
    */

    public String fileNameEncoding(String fileName, HttpServletRequest request) throws UnsupportedEncodingException {
        String browser = request.getHeader("User-Agent");
        if (browser.contains("MSIE") || browser.contains("Trident")) { // Trident = IE11
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        } else if (browser.contains("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < fileName.length(); i++) {
                char c = fileName.charAt(i);
                if (c > '~')
                    sb.append(URLEncoder.encode("" + c, StandardCharsets.UTF_8));
                else
                    sb.append(c);
            }
            fileName = sb.toString();
        } else { //browser.contains("Firefox") || browser.contains("Opera") || browser.contains("Safari") || Default
            fileName = "\"" + new String(fileName.getBytes(StandardCharsets.UTF_8), "8859_1") + "\"";
        }
        return fileName;
    }
}
