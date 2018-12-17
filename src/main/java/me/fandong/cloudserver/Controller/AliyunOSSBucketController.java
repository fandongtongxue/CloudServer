package me.fandong.cloudserver.Controller;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import io.swagger.annotations.Api;
import me.fandong.cloudserver.Model.AliyunOSSBucketListModel;
import me.fandong.cloudserver.Model.AliyunOSSBucketModel;
import me.fandong.cloudserver.Model.FileModel;
import me.fandong.cloudserver.service.MyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
public class AliyunOSSBucketController {

    private static Logger logger = Logger.getLogger(AliyunOSSBucketController.class);

    @Autowired
    MyRequestService myRequestService;

    private StringMap stringMap;

    @ApiOperation(value="获取阿里云OSSBucket列表", notes="根据accessKeyId和accessKeySecret查询Bucket列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessKeyId", value = "accessKeyId", required = true, dataType = "string"),
            @ApiImplicitParam(name = "accessKeySecret", value = "accessKeySecret", required = true, dataType = "string")
    })
    @GetMapping("/getAliyunOSSBucketList")
    public String getBucketList(String accessKeyId, String accessKeySecret) {
        stringMap = new StringMap();

        //传空值处理
        if (accessKeyId == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","accessKeyId为空");
            return Json.encode(stringMap);
        }
        if (accessKeySecret == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","accessKeySecret为空");
            return Json.encode(stringMap);
        }
        logger.info("url:"+"/getAliyunOSSBucketList");
        logger.info("params:"+"accessKeyId:"+accessKeyId+" accessKeySecret:"+accessKeySecret);
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // accessKey请登录https://ak-console.aliyun.com/#/查看
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 列举bucket
        List<Bucket> buckets = ossClient.listBuckets();

        AliyunOSSBucketListModel model = new AliyunOSSBucketListModel();
        // 关闭client
        ossClient.shutdown();

        ArrayList array = new ArrayList<FileModel>();
        for (Bucket bucket:buckets) {
            AliyunOSSBucketModel bucketModel = new AliyunOSSBucketModel();
            bucketModel.setName(bucket.getName());
            bucketModel.setEndPoint(bucket.getExtranetEndpoint());
            array.add(bucketModel);
        }
        model.setList(array);
        stringMap.put("data",model);
        stringMap.put("status",1);
        stringMap.put("msg","获取Bucket列表数据成功");
        logger.info("result:"+Json.encode(stringMap));
        return Json.encode(stringMap);
    }

    @ApiOperation(value="创建阿里云OSSBucket", notes="根据accessKeyId、accessKeySecret、endPoint、bucket创建Bucket")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessKeyId", value = "accessKeyId", required = true, dataType = "string"),
            @ApiImplicitParam(name = "accessKeySecret", value = "accessKeySecret", required = true, dataType = "string"),
            @ApiImplicitParam(name = "endPoint", value = "endPoint", required = true, dataType = "string"),
            @ApiImplicitParam(name = "bucket", value = "bucket", required = true, dataType = "string")
    })
    @PostMapping("/createAliyunBucket")
    public String createBucket (String accessKeyId, String accessKeySecret, String endPoint, String bucket){
        stringMap = new StringMap();

        //传空值处理
        if (accessKeyId == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","accessKeyId为空");
            return Json.encode(stringMap);
        }
        if (accessKeySecret == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","accessKeySecret为空");
            return Json.encode(stringMap);
        }
        if (endPoint == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","endPoint为空");
            return Json.encode(stringMap);
        }
        if (bucket == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","bucket为空");
            return Json.encode(stringMap);
        }
        logger.info("url:"+"/createAliyunBucket");
        logger.info("params:"+"accessKeyId:"+accessKeyId+" accessKeySecret:"+accessKeySecret+" endPoint:"+endPoint+" bucket:"+bucket);
        // accessKey请登录https://ak-console.aliyun.com/#/查看
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        ossClient.createBucket(bucket);
        stringMap.put("data","");
        stringMap.put("status",1);
        stringMap.put("msg","bucket创建成功");
        logger.info("result:"+Json.encode(stringMap));
        return Json.encode(stringMap);
    }

    @ApiOperation(value="删除阿里云OSSBucket", notes="根据accessKeyId、accessKeySecret、endPoint、bucket删除Bucket")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessKeyId", value = "accessKeyId", required = true, dataType = "string"),
            @ApiImplicitParam(name = "accessKeySecret", value = "accessKeySecret", required = true, dataType = "string"),
            @ApiImplicitParam(name = "endPoint", value = "endPoint", required = true, dataType = "string"),
            @ApiImplicitParam(name = "bucket", value = "bucket", required = true, dataType = "string")
    })
    @PostMapping("/deleteAliyunBucket")
    public String deleteBucket (String accessKeyId, String accessKeySecret, String endPoint, String bucket){
        stringMap = new StringMap();

        //传空值处理
        if (accessKeyId == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","accessKeyId为空");
            return Json.encode(stringMap);
        }
        if (accessKeySecret == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","accessKeySecret为空");
            return Json.encode(stringMap);
        }
        if (endPoint == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","endPoint为空");
            return Json.encode(stringMap);
        }
        if (bucket == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","bucket为空");
            return Json.encode(stringMap);
        }

        logger.info("url:"+"/deleteAliyunBucket");
        logger.info("params:"+"accessKeyId:"+accessKeyId+" accessKeySecret:"+accessKeySecret+" endPoint:"+endPoint+" bucket:"+bucket);

        // accessKey请登录https://ak-console.aliyun.com/#/查看
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        String params = "accessKeyId:"+accessKeyId+"&accessKeySecret:"+accessKeySecret+"&endPoint:"+endPoint+"&bucket:"+bucket;
        try {
            ossClient.deleteBucket(bucket);
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","bucket删除成功");
            logger.info("result:"+Json.encode(stringMap));
            return Json.encode(stringMap);
        }catch (OSSException oe){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",oe.getMessage());
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/deleteAliyunBucket",params,Json.encode(stringMap),"0",oe.getMessage());
            return Json.encode(stringMap);
        }catch (ClientException ce) {
            stringMap.put("data", "");
            stringMap.put("status", 0);
            stringMap.put("msg", ce.getErrorMessage());
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/deleteAliyunBucket",params,Json.encode(stringMap),"0",ce.getErrorMessage());
            return Json.encode(stringMap);
        }
    }
}
