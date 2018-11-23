package de.philippdormann.gymnasiumherzogenaurach;

class Helper {
    String addParameterToUrl(String url, String key, String value) {
        if (url.toLowerCase().contains("?")) {
            url += "&";
        } else {
            url += "?";
        }
        url += key + "=" + value;
        return url;
    }
}
