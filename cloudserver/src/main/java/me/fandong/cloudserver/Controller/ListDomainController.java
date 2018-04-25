package me.fandong.cloudserver.Controller;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import me.fandong.cloudserver.Model.QiniuCommonListModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ListDomainController {
    private StringMap stringMap;
    /*
    获取DomainList
    @method GET
    @param AK
    @param SK
    @param bucket
    @author 范东同学
     */
    @RequestMapping("getDomainList")
    public String getDomainList(HttpServletRequest request) {
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
        Auth auth = Auth.create(AK, SK);
        //地区
        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);
        //BucketManager
        BucketManager bucketManager = new BucketManager(auth, c);
        QiniuCommonListModel model = new QiniuCommonListModel();
        try {
            model.setList(bucketManager.domainList(bucket));
            stringMap.put("data",model);
            stringMap.put("status",1);
            stringMap.put("msg","获取Bucket列表数据成功");
            return Json.encode(stringMap);
        } catch (QiniuException e) {
            e.printStackTrace();
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",e.error());
            return Json.encode(stringMap);
        }
    }
}
