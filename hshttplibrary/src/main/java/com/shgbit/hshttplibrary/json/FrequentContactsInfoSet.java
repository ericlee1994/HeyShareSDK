package com.shgbit.hshttplibrary.json;


public class FrequentContactsInfoSet {
     private String result;
     private String failedMessage;
     private Favorite[] favorites;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getFailedMessage() {
		return failedMessage;
	}
	public void setFailedMessage(String failedMessage) {
		this.failedMessage = failedMessage;
	}
	public Favorite[] getFavorites() {
		return favorites;
	}
	public void setFavorites(Favorite[] favorites) {
		this.favorites = favorites;
	}
}
