package com.malautru.greed.Tools.Internet;

import java.util.concurrent.ExecutionException;

import okhttp3.Request;

public class TypeAheadAPI {
    private static TypeAheadAPI typeAheadAPI;

    private TypeAheadAPI() {
        typeAheadAPI = this;
    }

    // Singleton handler for text suggestion API
    public static TypeAheadAPI getInstance() {
        if (typeAheadAPI != null)
            return typeAheadAPI;

        return new TypeAheadAPI();
    }

    // Return String[] from JSON response
    public String[] getTypeAheadSuggestion(String query) throws ExecutionException, InterruptedException {
        return new HTTPRequestHandler().execute(new Request.Builder().url("https://cdn.gnoosic.com/typeahead?query=" + query)
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36")
                .header("Accept", "*/*")
                .build()).get().split("\n");
    }
}
