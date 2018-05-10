package me.fandong.cloudserver.Controller;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import me.fandong.cloudserver.Model.AliyunOSSBucketListModel;
import me.fandong.cloudserver.Model.AliyunOSSBucketModel;
import me.fandong.cloudserver.Model.FileModel;
import me.fandong.cloudserver.service.MyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;

@RestController
public class AliyunOSSBucketController {

    private static Logger logger = Logger.getLogger(AliyunOSSBucketController.class);

    @Autowired
    MyRequestService myRequestService;

    private StringMap stringMap;
    @GetMapping("/getAliyunOSSBucketList")
    public String getBucketList(HttpServletRequest request) {
        stringMap = new StringMap();

        String accessKeyId = request.getParameter("accessKeyId");
        String accessKeySecret = request.getParameter("accessKeySecret");
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
        logger.debug("test");
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

    @PostMapping("/createAliyunBucket")
    public String createBucket (HttpServletRequest request){
        stringMap = new StringMap();

        String accessKeyId = request.getParameter("accessKeyId");
        String accessKeySecret = request.getParameter("accessKeySecret");
        String endPoint = request.getParameter("endPoint");
        String bucket = request.getParameter("bucket");
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

    @PostMapping("/deleteAliyunBucket")
    public String deleteBucket (HttpServletRequest request){
        stringMap = new StringMap();

        String accessKeyId = request.getParameter("accessKeyId");
        String accessKeySecret = request.getParameter("accessKeySecret");
        String endPoint = request.getParameter("endPoint");
        String bucket = request.getParameter("bucket");
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
