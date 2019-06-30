package com.jakowaty.piotrbe.router;

import java.util.Map;

public class Route {
	protected String path;
	protected String method;
	protected String pattern;
	protected Map<String, String> params;
	
	public String getPath() {
		return this.path;
	}
	
	public String getMethod() {
		return this.method;
	}

	public String getPattern() {
		return this.pattern;
	}
	
	public Map<String, String> getParams() {
		return this.params;
	}
	
	public Route(String path, String method, String pattern, Map<String, String> params) {
		this.path = path;
		this.method = method;
		this.pattern = pattern;
		this.params = params;
	}
}
