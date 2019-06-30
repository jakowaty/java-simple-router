package com.jakowaty.piotrbe.router;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.IIOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jakowaty.piotrbe.router.exception.RouteNotFoundException;
import com.jakowaty.piotrbe.router.exception.UnsupportedRequestMethodException;
import com.jakowaty.piotrbe.router.interfaces.LambdaControllerInterface;

public class SimpleRouter {
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_ANY = "ANY";
	public static final String METHOD_DELETE = "DELETE";
	public static final String METHOD_PUT = "PUT";
	public static final String METHOD_PATCH = "PATCH";
	public static final String METHOD_HEAD = "HEAD";
	
	protected RouteMatcher matcher;
	protected Map
	<
		String, 
		Map<String, LambdaControllerInterface>
	> callables;
	
	public SimpleRouter() {
		//initialize map 0 level
		this.callables = new HashMap<String, Map<String, LambdaControllerInterface>>();
		this.matcher = new RouteMatcher();
		
		//prepare slots for methods
		this.callables.put(
			SimpleRouter.METHOD_GET,
			new HashMap<String, LambdaControllerInterface>()
		);
		
		this.callables.put(
			SimpleRouter.METHOD_POST,
			new HashMap<String, LambdaControllerInterface>()
		);
		
		this.callables.put(
			SimpleRouter.METHOD_ANY,
			new HashMap<String, LambdaControllerInterface>()
		);
		
		this.callables.put(
			SimpleRouter.METHOD_HEAD,
			new HashMap<String, LambdaControllerInterface>()
		);
		
		this.callables.put(
			SimpleRouter.METHOD_PATCH,
			new HashMap<String, LambdaControllerInterface>()
		);
		
		this.callables.put(
			SimpleRouter.METHOD_PUT,
			new HashMap<String, LambdaControllerInterface>()
		);
		
		this.callables.put(
			SimpleRouter.METHOD_DELETE,
			new HashMap<String, LambdaControllerInterface>()
		);
	}
	
	/**
	 * 
	 * @param pattern
	 * @param callable
	 */
	public void any(String pattern, LambdaControllerInterface callable) {
		this.register(SimpleRouter.METHOD_ANY, pattern, callable);
	}
	
	/**
	 * 
	 * @param pattern
	 * @param callable
	 */
	public void delete(String pattern, LambdaControllerInterface callable) {
		this.register(SimpleRouter.METHOD_DELETE, pattern, callable);
	}
	
	/**
	 * 
	 * @param pattern
	 * @param callable
	 */
	public void get(String pattern, LambdaControllerInterface callable) {
		this.register(SimpleRouter.METHOD_GET, pattern, callable);
	}
	
	/**
	 * 
	 * @param pattern
	 * @param callable
	 */
	public void head(String pattern, LambdaControllerInterface callable) {
		this.register(SimpleRouter.METHOD_HEAD, pattern, callable);
	}
	
	/**
	 * 
	 * @param pattern
	 * @param callable
	 */
	public void patch(String pattern, LambdaControllerInterface callable) {
		this.register(SimpleRouter.METHOD_PATCH, pattern, callable);
	}
	
	/**
	 * 
	 * @param pattern
	 * @param callable
	 */
	public void post(String pattern, LambdaControllerInterface callable) {
		this.register(SimpleRouter.METHOD_POST, pattern, callable);
	}
	
	/**
	 * 
	 * @param pattern
	 * @param callable
	 */
	public void put(String pattern, LambdaControllerInterface callable) {
		this.register(SimpleRouter.METHOD_PUT, pattern, callable);
	}


	public String find(String method, String pathInfo, String query)
	throws UnsupportedRequestMethodException, RouteNotFoundException {
		String matchingKey = null;
		// check if we know request method
		if (!this.callables.containsKey(method)) {
			throw new UnsupportedRequestMethodException();
		}
		
		// check method routes
		for (String pattern1 : this.callables.get(method).keySet()) {
			if (this.matcher.match(pathInfo, pattern1)) {
				matchingKey = pattern1;
				break;
			}
		}
		
		// if not found in method maybe will be found in methods run for ANY method
		if (matchingKey == null) {
			for (String pattern2 : this.callables.get(SimpleRouter.METHOD_ANY).keySet()) {
				if (this.matcher.match(pathInfo, pattern2)) {
					method = SimpleRouter.METHOD_ANY;
					matchingKey = pattern2;
					break;
				}
			}			
		}
		
		//if still no matching key
		if (matchingKey == null) {
			throw new RouteNotFoundException();
		}
		
		return matchingKey;
	}
	
	public void run(HttpServletRequest request, HttpServletResponse response)
	throws RouteNotFoundException, UnsupportedRequestMethodException, IOException
	{
		String matchingKey = this.find(
			request.getMethod(),
			request.getPathInfo(),
			request.getQueryString()
		);
		
			
		LambdaControllerInterface action = 
				this.callables.get(request.getMethod()).get(matchingKey);

		action.run(
			request,
			response,
			this,
			new Route(
				request.getPathInfo(),
				request.getMethod(),
				matchingKey,
				this.matcher.extractParams(request.getPathInfo(), matchingKey)
			)
		);
	}
	
	protected boolean register(
		String method,
		String pattern,
		LambdaControllerInterface callable
	) {
		if (
				!this.callables.containsKey(method) ||
				!this.matcher.validate(pattern)
			) {
			return false;
		}
		
		this.callables.get(method).put(pattern, callable);
		return true;
	}
}
