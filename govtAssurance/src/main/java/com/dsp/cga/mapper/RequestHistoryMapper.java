package com.dsp.cga.mapper;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.dto.DocumentMetadataDTO;
import com.dsp.cga.assurance.dto.RequestHistoryDto;
import com.dsp.cga.entity.RequestHistory;

@Component
public class RequestHistoryMapper {
	
	public RequestHistory toRequestHistory (RequestHistory input) {

		RequestHistory history = RequestHistory.builder()
									.actionTakenBy(input.getActionTakenBy())
									.actionTakenByName(input.getActionTakenByName())
//									.actionTakenOn(input.getCreatedAt())
//									.status(input.getStatus())
									.pendingWith(input.getPendingWith())
									.pendingWithName(input.getPendingWithName())
									.commentForDisplay(input.getCommentForDisplay())
									.build();
		return history;
	}
	
	/**
	 * 
	 * @param docMap
	 * @return
	 */
	public DocumentMetadataDTO toDocumentDTO (Map<String,Object> docMap) {

		DocumentMetadataDTO document = DocumentMetadataDTO.builder()
				.documentId(docMap.get("documentName") != null ? docMap.get("documentName").toString() : "")
				.uuid(UUID.fromString(docMap.get("documentUuid").toString())) 
				.build();
				
		return document;
	}

	public static RequestHistoryDto toDTO(RequestHistory entity) {
		if(entity == null) {
			return null;
		}
		return RequestHistoryDto.builder()
				.id(entity.getId())
				.requestId(entity.getRequestId())
				.requestType(entity.getRequestType())
				.actionType(entity.getActionType())
				.referenceFiles(entity.getReferenceFiles())
				.pendingWith(entity.getPendingWith())
				.actionTakenBy(entity.getActionTakenBy())
				.ipAddress(entity.getIpAddress())
				.actionTakenByName(entity.getActionTakenByName())
				.pendingWithName(entity.getPendingWithName())
				.commentForDisplay(entity.getCommentForDisplay())
				.build();
	}
	
	public static RequestHistory toEntity(RequestHistoryDto dto)
	{
		 if (dto == null) {
	            return null;
	        }
	        
	        return RequestHistory.builder()
	        		.requestId(dto.getRequestId())
	        		.requestType(dto.getRequestType())
	        		.actionType(dto.getActionType())
	        		.referenceFiles(dto.getReferenceFiles())
	        		.pendingWith(dto.getPendingWith())
	        		.actionTakenBy(dto.getActionTakenBy())
	        		.ipAddress(dto.getIpAddress())
	        		.actionTakenByName(dto.getActionTakenByName())
	        		.pendingWithName(dto.getPendingWithName())
	        		.commentForDisplay(dto.getCommentForDisplay())
	        		.build();
	}
	
	 public static void updateEntityFromDTO(RequestHistoryDto dto,RequestHistory entity)
	 {
		 if (dto == null || entity == null) {
	            return;
	        }
	 }
}
