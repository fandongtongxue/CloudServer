package me.fandong.cloudserver.Controller;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fandong.cloudserver.Model.QiniuCommonListModel;
import me.fandong.cloudserver.service.MyRequestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class QiniuBucketController {
    private StringMap stringMap;
    private static Logger logger = Logger.getLogger(QiniuBucketController.class);
    @Autowired
    MyRequestService myRequestService;

    @ApiOperation(value="获取七牛云存储Bucket列表", notes="根据AK和SK查询Bucket列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AK", value = "AK", required = true, dataType = "string"),
            @ApiImplicitParam(name = "SK", value = "SK", required = true, dataType = "string")
    })
    @GetMapping("/getQiniuBucketList")
    public String getBucketList(String AK, String SK) {
        stringMap = new StringMap();
        //传空值处理
        if (AK == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","AK为空");
            return Json.encode(stringMap);
        }
        if (SK == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","SK为空");
            return Json.encode(stringMap);
        }
        logger.info("url:"+"/getQiniuBucketList");
        logger.info("params:"+"AK:"+AK+" SK:"+SK);
        String params ="AK:"+AK+"&SK:"+SK;

        Auth auth = Auth.create(AK, SK);
        //地区
        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);
        //BucketManager
        BucketManager bucketManager = new BucketManager(auth, c);
        QiniuCommonListModel model = new QiniuCommonListModel();
        try {
            model.setList(bucketManager.buckets());
            stringMap.put("data",model);
            stringMap.put("status",1);
            stringMap.put("msg","获取Bucket列表数据成功");
            logger.info("result:"+Json.encode(stringMap));
            return Json.encode(stringMap);
        } catch (QiniuException e) {
            e.printStackTrace();
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",e.error());
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/getQiniuBucketList",params,Json.encode(stringMap),"0",e.error());
            return Json.encode(stringMap);
        }
    }

    @ApiOperation(value="获取七牛云存储Bucket的Domain列表", notes="根据AK、SK、Bucket查询Bucket的Domain列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AK", value = "AK", required = true, dataType = "string"),
            @ApiImplicitParam(name = "SK", value = "SK", required = true, dataType = "string"),
            @ApiImplicitParam(name = "bucket", value = "bucket", required = true, dataType = "string")
    })
    @GetMapping("/getQiniuDomainList")
    public String getDomainList(String AK, String SK, String bucket) {
        stringMap = new StringMap();
        //传空值处理
        if (AK == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","AK为空");
            return Json.encode(stringMap);
        }
        if (SK == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","SK为空");
            return Json.encode(stringMap);
        }
        if (bucket == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","bucket为空");
            return Json.encode(stringMap);
        }
        logger.info("url:"+"/getQiniuDomainList");
        logger.info("params:"+"AK:"+AK+" SK:"+SK+" bucket:"+bucket);
        String params ="AK:"+AK+"&SK:"+SK+"&bucket:"+bucket;
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
            logger.info("result:"+Json.encode(stringMap));
            return Json.encode(stringMap);
        } catch (QiniuException e) {
            e.printStackTrace();
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",e.error());
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/getQiniuDomainList",params,Json.encode(stringMap),"0",e.error());
            return Json.encode(stringMap);
        }
    }

    @ApiOperation(value="创建七牛云存储Bucket", notes="根据AK、SK、Bucket，Region来创建Bucket")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AK", value = "AK", required = true, dataType = "string"),
            @ApiImplicitParam(name = "SK", value = "SK", required = true, dataType = "string"),
            @ApiImplicitParam(name = "bucket", value = "bucket", required = true, dataType = "string"),
            @ApiImplicitParam(name = "region", value = "region", required = true, dataType = "string")
    })
    @PostMapping("/createQiniuBucket")
    public String createBucket (String AK, String SK, String bucket, String region){
        stringMap = new StringMap();
        //传空值处理
        if (AK == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","AK为空");
            return Json.encode(stringMap);
        }
        if (SK == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","SK为空");
            return Json.encode(stringMap);
        }
        if (bucket == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","bucket为空");
            return Json.encode(stringMap);
        }
        if (region == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","region为空");
            return Json.encode(stringMap);
        }
        logger.info("url:"+"/createQiniuBucket");
        logger.info("params:"+"AK:"+AK+" SK:"+SK+" bucket"+bucket+" region:"+region);
        String params ="AK:"+AK+"&SK:"+SK+"&bucket:"+bucket+"&region:"+region;
        Auth auth = Auth.create(AK, SK);
        //地区
        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);
        //BucketManager
        BucketManager bucketManager = new BucketManager(auth, c);
        try {
            bucketManager.createBucket(bucket,region);
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","bucket创建成功");
            logger.info("result:"+Json.encode(stringMap));
            return Json.encode(stringMap);
        } catch (Exception e) {
            e.printStackTrace();
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",e.toString());
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/createQiniuBucket",params,Json.encode(stringMap),"0",e.toString());
            return Json.encode(stringMap);
        }
    }
}
