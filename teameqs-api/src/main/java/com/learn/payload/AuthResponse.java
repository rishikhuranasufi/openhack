package com.learn.payload;

public class AuthResponse {
    private String accessToken;
    private String username;
    private String name;
    private String email;
    private String role;
    private String id;

    public AuthResponse(String accessToken, String username, String name, String email, String role, String id) {
		this.accessToken = accessToken;
		this.username = username;
		this.name = name;
		this.email = email;
		this.role = role;
		this.id = id;
	}
    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
}