package me.fandong.cloudserver.Controller;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import me.fandong.cloudserver.Model.AliyunOSSBucketListModel;
import me.fandong.cloudserver.Model.AliyunOSSBucketModel;
import me.fandong.cloudserver.Model.FileModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AliyunOSSBucketController {
    private StringMap stringMap;

    @RequestMapping("/getAliyunOSSBucketList")
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
        System.out.println(Json.encode(stringMap));
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
        System.out.println(Json.encode(stringMap));
        return Json.encode(stringMap);
    }

    @RequestMapping("/createAliyunBucket")
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

        System.out.println(Json.encode(stringMap));
        // accessKey请登录https://ak-console.aliyun.com/#/查看
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        ossClient.createBucket(bucket);
        stringMap.put("data","");
        stringMap.put("status",1);
        stringMap.put("msg","bucket创建成功");
        System.out.println(Json.encode(stringMap));
        return Json.encode(stringMap);
    }

    @RequestMapping("/deleteAliyunBucket")
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

        System.out.println(Json.encode(stringMap));
        // accessKey请登录https://ak-console.aliyun.com/#/查看
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        try {
            ossClient.deleteBucket(bucket);
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","bucket删除成功");
            System.out.println(Json.encode(stringMap));
            return Json.encode(stringMap);
        }catch (OSSException oe){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",oe.getMessage());
            System.out.println(Json.encode(stringMap));
            return Json.encode(stringMap);
        }catch (ClientException ce) {
            stringMap.put("data", "");
            stringMap.put("status", 0);
            stringMap.put("msg", ce.getErrorMessage());
            System.out.println(Json.encode(stringMap));
            return Json.encode(stringMap);
        }
    }
}
