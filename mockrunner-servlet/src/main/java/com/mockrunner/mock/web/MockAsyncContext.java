package com.mockrunner.mock.web;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MockAsyncContext implements AsyncContext {

	private ServletRequest request;
	private ServletResponse response;
	private boolean hasOriginalRequestAndResponse;
	private long timeout;

	@Override
	public ServletRequest getRequest() {
		return request;
	}

	public MockAsyncContext setRequest(ServletRequest request) {
		this.request = request;
		return this;
	}

	@Override
	public ServletResponse getResponse() {
		return response;
	}

	public MockAsyncContext setResponse(ServletResponse response) {
		this.response = response;
		return this;
	}

	@Override
	public boolean hasOriginalRequestAndResponse() {
		return hasOriginalRequestAndResponse;
	}

	public MockAsyncContext setHasOriginalRequestAndResponse(boolean hasOriginalRequestAndResponse) {
		this.hasOriginalRequestAndResponse = hasOriginalRequestAndResponse;
		return this;
	}

	@Override
	public void dispatch() {
		// No-op
	}

	@Override
	public void dispatch(String path) {
		// No-op
	}

	@Override
	public void dispatch(ServletContext context, String path) {
		// No-op
	}

	@Override
	public void complete() {
		// No-op
	}

	@Override
	public void start(Runnable run) {
		// No-op
	}

	@Override
	public void addListener(AsyncListener listener) {
		// No-op
	}

	@Override
	public void addListener(AsyncListener listener, ServletRequest servletRequest, ServletResponse servletResponse) {
		// No-op
	}

	@Override
	public <T extends AsyncListener> T createListener(Class<T> clazz) throws ServletException {
		return (T) new MockAsyncListener();
	}

	@Override
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public MockAsyncContext setTimeoutFluent(long timeout) {
		this.timeout = timeout;
		return this;
	}

	@Override
	public long getTimeout() {
		return timeout;
	}

}
