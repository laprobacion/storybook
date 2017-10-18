package com.read.storybook.service;

import org.json.JSONObject;

public class ServiceMock {
    JSONObject resp;
    String url;

    public ServiceMock(String url){
        this.url = url;
        try {
            resp = new JSONObject(initialize());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public JSONObject getResp(){
        return this.resp;
    }
    private String initialize(){
        String res = null;
        if(this.url == null){
            return "{\"message\": \"success\", \"isActive\" : \"1\", \"isAdmin\" : \"1\"}";
        }
        if(this.url.indexOf("http://jabahan.com/storybook/user/search.php") > -1){
            res = "{\"message\": \"success\", \"isActive\" : \"1\", \"isAdmin\" : \"1\"}";
        }
        if(this.url.indexOf("http://jabahan.com/storybook/user/login.php?") > -1){
            res = "{\"message\": \"success\", \"isActive\" : \"1\", \"isAdmin\" : \"1\"}";
        }
        if(this.url.indexOf("http://jabahan.com/storybook/level/read.php") > -1){
            res = "{\"records\" : [ {\"id\":\"123123\",\"name\":\"teslevel\",\"isActive\":\"1\" }]}";
        }
        if(this.url.indexOf("http://jabahan.com/storybook/story/search.php?") > -1){
            res = "{\"records\" : [ {\"id\":\"123123\",\"name\":\"teslevel\",\"isActive\":\"1\" }]}";
        }
        if(this.url.indexOf("http://jabahan.com/storybook/story/searchimages.php") > -1){
            res = "{\"records\" : [ {\"image\":\"1321\" },{\"image\":\"3212\" }]}";
        }
        if(this.url.indexOf("http://jabahan.com/storybook/question/search.php") > -1){
            res = "{\"records\" : [ {\"QUESTION_ID\":\"412423\",\"DESCRIPTION\":\"desc1\",\"ISACTIVE\":\"1\", \"choices\" :{\"choices\" :[" +
                        "{\"CHOICE_ID\":\"1\",\"DESCRIPTION\":\"c1\",\"isActive\":\"1\",\"isAnswer\":\"1\"}," +
                        "{\"CHOICE_ID\":\"2\",\"DESCRIPTION\":\"c2\",\"isActive\":\"1\",\"isAnswer\":\"0\"}," +
                        "{\"CHOICE_ID\":\"3\",\"DESCRIPTION\":\"c3\",\"isActive\":\"1\",\"isAnswer\":\"0\"}]}}," +
                    "{\"QUESTION_ID\":\"43421\",\"DESCRIPTION\":\"wqewerewtrvfdgbdfgbdhbdhd dfh dfh dfh dghd rtyw45y34ysrhsdh dg h\",\"ISACTIVE\":\"1\", \"choices\" : {\"choices\" :[" +
                        "{\"CHOICE_ID\":\"1\",\"DESCRIPTION\":\"c1\",\"isActive\":\"1\",\"isAnswer\":\"1\"}," +
                        "{\"CHOICE_ID\":\"2\",\"DESCRIPTION\":\"asdfasdfasdfasdfasdfasdfasdf86asdfasdfa567sdfasdfasdfasdf asdfasdfasdfa  dsfadsf asdf fasf asdfa \",\"isActive\":\"1\",\"isAnswer\":\"0\"}," +
                        "{\"CHOICE_ID\":\"3\",\"DESCRIPTION\":\"asdfasdfasdfasdfas867dfasdfasdfasdfasdfasdfa456sdfasdfasdf asdfasdfasdfa  dsfadsf asdf fasf asdfa \",\"isActive\":\"1\",\"isAnswer\":\"0\"}," +
                        "{\"CHOICE_ID\":\"3\",\"DESCRIPTION\":\"asdfasdfasdfasdfasd789fasdfasdfasdfasdfasdfasdfas345dfasdf asdfasdfasdfa  dsfadsf asdf fasf asdfa \",\"isActive\":\"1\",\"isAnswer\":\"0\"}," +
                        "{\"CHOICE_ID\":\"3\",\"DESCRIPTION\":\"asdfasdfasdfasdfasdf890asdfasdfasdfasdfasdfasdfasdfas23df asdfasdfasdfa  dsfadsf asdf fasf asdfa \",\"isActive\":\"1\",\"isAnswer\":\"0\"}]}}" +
                    "]}";
        }

        return res;
    }
}
