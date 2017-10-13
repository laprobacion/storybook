package com.read.storybook.service;


import org.json.JSONObject;

import java.io.Serializable;

public interface ServiceResponse {
    void postExecute(JSONObject response);
}
