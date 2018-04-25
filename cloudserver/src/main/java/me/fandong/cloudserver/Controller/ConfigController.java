package me.fandong.cloudserver.Controller;

import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import me.fandong.cloudserver.Model.QiniuCommonListModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ConfigController {
    private StringMap stringMap;
    /*
    配置AK/SK
    @method GET
    @param AK
    @param SK
    @author 范东同学
     */
    @RequestMapping("/config")
    public String config(HttpServletRequest request){
        stringMap = new StringMap();

        String AK = request.getParameter("AK");
        String SK = request.getParameter("SK");
        if (AK == null){
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","AK为空");
            return Json.encode(stringMap);
        }
        if (SK == null){
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","SK为空");
            return Json.encode(stringMap);
        }
        stringMap.put("data","");
        stringMap.put("status",0);
        stringMap.put("msg","配置成功");
        return Json.encode(stringMap);
    }
}
