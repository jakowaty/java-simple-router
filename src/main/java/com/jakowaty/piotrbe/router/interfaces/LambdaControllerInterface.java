package com.jakowaty.piotrbe.router.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jakowaty.piotrbe.router.Route;
import com.jakowaty.piotrbe.router.SimpleRouter;

@FunctionalInterface
public interface LambdaControllerInterface {
	public boolean run(
		HttpServletRequest req,
		HttpServletResponse res,
		SimpleRouter router,
		Route route
	);
}
