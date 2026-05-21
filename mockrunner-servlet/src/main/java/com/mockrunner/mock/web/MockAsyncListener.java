package com.mockrunner.mock.web;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

public class MockAsyncListener implements AsyncListener {

	@Override
	public void onComplete(AsyncEvent event) throws IOException {
		// No-op
	}

	@Override
	public void onTimeout(AsyncEvent event) throws IOException {
		// No-op
	}

	@Override
	public void onError(AsyncEvent event) throws IOException {
		// No-op

	}

	@Override
	public void onStartAsync(AsyncEvent event) throws IOException {
		// No-op
	}

}
