package com.read.storybook.service;


import com.loopj.android.http.RequestParams;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.Encryptor;
import com.read.storybook.util.Util;

public class UserStoryService {

    public static void save(String storyId, String userId, int score, int item, Service service){
        RequestParams params = new RequestParams();
        params.put("storyId", storyId);
        params.put("userId", userId);
        params.put("score", score);
        params.put("item", item);
        service.post("http://jabahan.com/storybook/userstory/save.php", params);
        service.execute();
    }

    public static void search(Service service){
        service.post("http://jabahan.com/storybook/userstory/search.php", new RequestParams());
        service.execute();
    }

    public static void searchByName(Service service){
        RequestParams params = new RequestParams();
        params.put("userId", AppCache.getInstance().getUser().getId());
        service.post("http://jabahan.com/storybook/userstory/searchbyname.php", params);
        service.execute();
    }
}
