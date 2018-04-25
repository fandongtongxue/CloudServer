package me.fandong.cloudserver.Controller;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DeleteFileController {
    private StringMap stringMap;

    @RequestMapping("deleteFile")
    public String deleteFile(HttpServletRequest request){
        stringMap = new StringMap();
        String AK = request.getParameter("AK");
        String SK = request.getParameter("SK");
        String bucket = request.getParameter("bucket");
        String key = request.getParameter("fileName");
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
        if (key == null){
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","key为空");
            return Json.encode(stringMap);
        }
        Auth auth = Auth.create(AK, SK);
        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);
        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);
        try {
            bucketManager.delete(bucket,key);
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","删除文件成功");
            return Json.encode(stringMap);
        } catch (QiniuException e) {
            Response r = e.response;
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",e.error());
            return Json.encode(stringMap);
        }
    }
}
