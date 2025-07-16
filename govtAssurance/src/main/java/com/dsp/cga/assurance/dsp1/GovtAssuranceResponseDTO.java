package com.dsp.cga.assurance.dsp1;


import lombok.Data;
import java.util.List;

@Data
public class GovtAssuranceResponseDTO {

    private Metadata _metadata;
    private List<Record> records;

    @Data
    public static class Metadata {
        private int currentPageNumber;
        private int perPageSize;
        private int totalElements;
        private int totalPages;
    }

    @Data
    public static class Record {
        private String assuNo;
        private int loksabha;
        private int session;
        private String date;
        private String ministry;
        private String member;
        private String subject;
        private String department;
        private String category;
        private String status;
        private String impRecordNo;
        private String remarks;
        private String textOfAssurance;
        private String url;
        private String answerHtml;
        private String questionHtml;
        private String qtype;
        private int qno;
    }
}
