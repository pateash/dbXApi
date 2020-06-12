package com.example.dbx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionBeanUpdate {
	private String comment;
	private ExceptionStatus status;

	@Override
	public String toString() {
		return "UpdateCommentRequest [comment=" + comment + ", status=" + status + "]";
	}
}
