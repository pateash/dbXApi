package com.example.dbx.model;

public class UpdateCommentRequest {
	private String comment;
	
	public UpdateCommentRequest() {
		
	}

	public UpdateCommentRequest(String comment) {
		super();
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "UpdateCommentRequest [comment=" + comment + "]";
	}
}
