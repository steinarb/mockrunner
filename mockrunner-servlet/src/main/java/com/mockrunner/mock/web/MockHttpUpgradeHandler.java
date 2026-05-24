package com.mockrunner.mock.web;

import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.WebConnection;

public class MockHttpUpgradeHandler implements HttpUpgradeHandler {

	@Override
	public void init(WebConnection wc) {
		// No-op
	}

	@Override
	public void destroy() {
		// No-op
	}

}
