package com.scalepoint.automation.stubs;

import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcher;

public class CustomRnvMatcher extends RequestMatcher {
    @Override
    public String getName() {
        return "newPrice is 122";
    }

    @Override
    public MatchResult match(Request request) {
        return MatchResult.of(request.getBodyAsString().contains("newPrice=\"122.00\""));
    }
}
