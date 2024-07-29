package com.jacob.json.converter.tongyi;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.List;

public class JSONArrayToJSONObject {
    public static void main(String[] args) {
        String jsonString = """
                [
                  {
                    "id": 1,
                    "name": "Leanne Graham",
                    "username": "Bret",
                    "email": "Sincere@april.biz",
                    "address": {
                      "street": "Kulas Light",
                      "suite": "Apt. 556",
                      "city": "Gwenborough",
                      "zipcode": "92998-3874",
                      "geo": {
                        "lat": "-37.3159",
                        "lng": "81.1496"
                      }
                    },
                    "phone": "1-770-736-8031 x56442",
                    "website": "hildegard.org",
                    "company": {
                      "name": "Romaguera-Crona",
                      "catchPhrase": "Multi-layered client-server neural-net",
                      "bs": "harness real-time e-markets"
                    }
                  },
                  {
                    "id": 2,
                    "name": "Ervin Howell",
                    "username": "Antonette",
                    "email": "Shanna@melissa.tv",
                    "address": {
                      "street": "Victor Plains",
                      "suite": "Suite 879",
                      "city": "Wisokyburgh",
                      "zipcode": "90566-7771",
                      "geo": {
                        "lat": "-43.9509",
                        "lng": "-34.4618"
                      }
                    },
                    "phone": "010-692-6593 x09125",
                    "website": "anastasia.net",
                    "company": {
                      "name": "Deckow-Crist",
                      "catchPhrase": "Proactive didactic contingency",
                      "bs": "synergize scalable supply-chains"
                    }
                  },
                  {
                    "id": 3,
                    "name": "Clementine Bauch",
                    "username": "Samantha",
                    "email": "Nathan@yesenia.net",
                    "address": {
                      "street": "Douglas Extension",
                      "suite": "Suite 847",
                      "city": "McKenziehaven",
                      "zipcode": "59590-4157",
                      "geo": {
                        "lat": "-68.6102",
                        "lng": "-47.0653"
                      }
                    },
                    "phone": "1-463-123-4447",
                    "website": "ramiro.info",
                    "company": {
                      "name": "Romaguera-Jacobson",
                      "catchPhrase": "Face to face bifurcated interface",
                      "bs": "e-enable strategic applications"
                    }
                  },
                  {
                    "id": 4,
                    "name": "Patricia Lebsack",
                    "username": "Karianne",
                    "email": "Julianne.OConner@kory.org",
                    "address": {
                      "street": "Hoeger Mall",
                      "suite": "Apt. 692",
                      "city": "South Elvis",
                      "zipcode": "53919-4257",
                      "geo": {
                        "lat": "29.4572",
                        "lng": "-164.2990"
                      }
                    },
                    "phone": "493-170-9623 x156",
                    "website": "kale.biz",
                    "company": {
                      "name": "Robel-Corkery",
                      "catchPhrase": "Multi-tiered zero tolerance productivity",
                      "bs": "transition cutting-edge web services"
                    }
                  }
                ]""";

        // 将JSON字符串转换为JSONArray
        JSONArray jsonArray = JSON.parseArray(jsonString);

        // 遍历JSONArray，将每个元素转换为JSONObject
        List<JSONObject> jsonObjectList = jsonArray.toJavaList(JSONObject.class);

        // 使用jsonObjectList中的数据
        for (JSONObject obj : jsonObjectList) {
            System.out.println("Name: " + obj.getString("name"));
            System.out.println("Email: " + obj.getString("email"));
            // 可以继续访问其他字段
        }
    }
}
