package me.fandong.cloudserver.Controller;

import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import me.fandong.cloudserver.Model.QiniuTokenModel;
import org.springframework.web.bind.annotation.RequestMapping;
import com.qiniu.util.Auth;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TokenController {
    private StringMap stringMap;
    /*
    配置AK/SK
    @method GET
    @param AK
    @param SK
    @param bucket
    @author 范东同学
     */
    @RequestMapping("getUploadToken")
    public String getUploadToken(HttpServletRequest request){
        stringMap = new StringMap();
        String AK = request.getParameter("AK");
        String SK = request.getParameter("SK");
        String bucket = request.getParameter("bucket");
        //传空值处理
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
        if (bucket == null){
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","bucket为空");
            return Json.encode(stringMap);
        }
        //密钥配置
        Auth auth = Auth.create(AK, SK);
        //创建上传对象
        StringMap stringMap = new StringMap();
        QiniuTokenModel model = new QiniuTokenModel();
        model.setToken(auth.uploadToken(bucket));
        stringMap.put("data",model);
        stringMap.put("status",0);
        stringMap.put("msg","获取上传token成功");
        return Json.encode(stringMap);
    }
}
