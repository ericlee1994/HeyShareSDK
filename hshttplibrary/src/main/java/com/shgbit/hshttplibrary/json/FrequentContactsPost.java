package com.shgbit.hshttplibrary.json;

public class FrequentContactsPost {
	private String sessionId;
    private Favorite[] favorites;
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Favorite[] getFavorites() {
		return favorites;
	}

	public void setFavorites(Favorite[] favorites) {
		this.favorites = favorites;
	}
}
