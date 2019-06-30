package com.jakowaty.piotrbe.router;

import java.util.HashMap;
import java.util.Map;

import com.jakowaty.piotrbe.router.exception.RouteNotFoundException;

public class RouteMatcher {

	final public static String delimiterOpen = "{:";
	final public static String delimiterClose = "::}";

	public boolean match(String requestPathInfo, String routePattern) {

		if (!this.validate(routePattern) || !this.validate(requestPathInfo)) {
			return false;
		}

		requestPathInfo = requestPathInfo.trim();
		routePattern = routePattern.trim();

		if (routePattern.endsWith("/")) {
			routePattern = routePattern.substring(0, (routePattern.length() - 1));
		}

		if (requestPathInfo.endsWith("/")) {
			requestPathInfo = requestPathInfo.substring(0, (requestPathInfo.length() - 1));
		}

		if (requestPathInfo.equals(routePattern)) {
			return true;
		}

		String[] pathInfoSplitted = requestPathInfo.split("/");
		String[] routePatternSplitted = routePattern.split("/");

		if (pathInfoSplitted.length != routePatternSplitted.length) {
			return false;
		}

		for (int i = 0; i < routePatternSplitted.length; i++) {
			if (routePatternSplitted[i].equals(pathInfoSplitted[i])) {
				continue;
			}

			if (routePatternSplitted[i].startsWith(RouteMatcher.delimiterOpen) &&
				routePatternSplitted[i].endsWith(RouteMatcher.delimiterClose)) {
				//@todo param type validation
				continue;
			}

			return false;
		}

		return true;
	}

	public boolean validate(String routePattern) {
		//@TODO: 1. valid route pattern cant have 2 same positional params names
		return true;
	}
	
	public Map<String, String> extractParams(String requestPathInfo, String routePattern)
	throws RouteNotFoundException {
		if (!this.validate(routePattern) || 
			!this.validate(requestPathInfo) ||
			!this.match(requestPathInfo, routePattern)) {
			throw new RouteNotFoundException();
		}
		
		Map<String, String> params = new HashMap<>();
		String[] pathInfoSplitted = requestPathInfo.split("/");
		String[] routePatternSplitted = routePattern.split("/");
		
		if (requestPathInfo.equals(routePattern)) {
			return params;
		}
		
		if (pathInfoSplitted.length == routePatternSplitted.length) {
			for (int i = 0; i < routePatternSplitted.length; i++) {
				
				if (routePatternSplitted[i].startsWith(RouteMatcher.delimiterOpen) &&
					routePatternSplitted[i].endsWith(RouteMatcher.delimiterOpen)) {
					params.put(
					routePatternSplitted[i]
						.substring(0, (routePatternSplitted[i].length() - 1))
						.replaceFirst("\\" + RouteMatcher.delimiterOpen, ""),
						pathInfoSplitted[i]
					);
						
				}
			}
		}
		
		return params;
	}
}
