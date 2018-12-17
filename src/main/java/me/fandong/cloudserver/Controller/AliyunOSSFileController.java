package me.fandong.cloudserver.Controller;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fandong.cloudserver.Model.FileListModel;
import me.fandong.cloudserver.Model.FileModel;
import me.fandong.cloudserver.service.MyRequestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AliyunOSSFileController {
    private StringMap stringMap;
    private static Logger logger = Logger.getLogger(AliyunOSSFileController.class);

    @Autowired
    MyRequestService myRequestService;

    @ApiOperation(value="获取阿里云OSSBucket的文件列表", notes="根据accessKeyId、accessKeySecret、endPoint、bucket、keyPrefix、marker获取Bucket中文件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessKeyId", value = "accessKeyId", required = true, dataType = "string"),
            @ApiImplicitParam(name = "accessKeySecret", value = "accessKeySecret", required = true, dataType = "string"),
            @ApiImplicitParam(name = "endPoint", value = "endPoint", required = true, dataType = "string"),
            @ApiImplicitParam(name = "bucket", value = "bucket", required = true, dataType = "string"),
            @ApiImplicitParam(name = "keyPrefix", value = "keyPrefix", required = false, dataType = "string"),
            @ApiImplicitParam(name = "marker", value = "marker", required = false, dataType = "string")
    })
    @GetMapping("/getAliyunOSSFileList")
    public String getFileList (String accessKeyId, String accessKeySecret, String endPoint, String bucket, String keyPrefix, String marker){
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
        if (bucket == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","bucket为空");
            return Json.encode(stringMap);
        }
        if (endPoint == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","endPoint为空");
            return Json.encode(stringMap);
        }
        logger.info("url:"+"/getAliyunOSSFileList");
        logger.info("params:"+"accessKeyId:"+accessKeyId+" accessKeySecret:"+accessKeySecret+" endPoint:"+endPoint+" bucket:"+bucket);
        String params = "accessKeyId:"+accessKeyId+"&accessKeySecret:"+accessKeySecret+"&endPoint:"+endPoint+"&bucket:"+bucket;
        // endpoint以杭州为例，其它region请按实际情况填写
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        final int maxKeys = 20;
        ObjectListing objectListing = null;
        do {
            try {
                objectListing = ossClient.listObjects(new ListObjectsRequest(bucket).
                        withPrefix(keyPrefix).withMarker(marker).withMaxKeys(maxKeys));
                List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
                ArrayList array = new ArrayList<FileModel>();
                for (OSSObjectSummary s : sums) {
                    FileModel model = new FileModel();
                    model.setFileName(s.getKey());
                    model.setFileSize(s.getSize()/1024 + "KB");
                    model.setFileType("未知");
                    array.add(model);
                }
                marker = objectListing.getNextMarker();
                FileListModel listModel = new FileListModel();
                listModel.setList(array);
                listModel.setMarker(marker);
                if (marker == null){
                    listModel.setMarker("");
                }
                stringMap.put("data",listModel);
                stringMap.put("status",1);
                stringMap.put("msg","获取文件列表数据成功");
                // 关闭client
                ossClient.shutdown();
                logger.info("result:"+Json.encode(stringMap));
                return Json.encode(stringMap);
            }catch (OSSException oe){
                stringMap.put("data","");
                stringMap.put("status",0);
                stringMap.put("msg",oe.getMessage());
                logger.info("result:"+Json.encode(stringMap));
                myRequestService.createMyRequest("/getAliyunOSSFileList",params,Json.encode(stringMap),"0",oe.getMessage());
                return Json.encode(stringMap);
            }catch (ClientException ce) {
                stringMap.put("data", "");
                stringMap.put("status", 0);
                stringMap.put("msg", ce.getErrorMessage());
                logger.info("result:"+Json.encode(stringMap));
                myRequestService.createMyRequest("/getAliyunOSSFileList",params,Json.encode(stringMap),"0",ce.getErrorMessage());
                return Json.encode(stringMap);
            }

        } while (objectListing.isTruncated());
    }

    @ApiOperation(value="删除阿里云OSSBucket的文件", notes="根据accessKeyId、accessKeySecret、endPoint、bucket、key删除Bucket中的文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessKeyId", value = "accessKeyId", required = true, dataType = "string"),
            @ApiImplicitParam(name = "accessKeySecret", value = "accessKeySecret", required = true, dataType = "string"),
            @ApiImplicitParam(name = "endPoint", value = "endPoint", required = true, dataType = "string"),
            @ApiImplicitParam(name = "bucket", value = "bucket", required = true, dataType = "string"),
            @ApiImplicitParam(name = "key", value = "key", required = true, dataType = "string")
    })
    @PostMapping("/deleteAliyunOSSFile")
    public String deleteFile (String accessKeyId, String accessKeySecret, String endPoint, String bucket, String key){
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
        if (bucket == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","bucket为空");
            return Json.encode(stringMap);
        }
        if (key == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","key为空");
            return Json.encode(stringMap);
        }
        if (endPoint == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","endPoint为空");
            return Json.encode(stringMap);
        }

        logger.info("url:"+"/deleteAliyunOSSFile");
        logger.info("params:"+"accessKeyId:"+accessKeyId+" accessKeySecret:"+accessKeySecret+" endPoint:"+endPoint+" bucket:"+bucket+" key:"+key);
        String params = "accessKeyId:"+accessKeyId+"&accessKeySecret:"+accessKeySecret+"&endPoint:"+endPoint+"&bucket:"+bucket+"&key:"+key;
        // endpoint以杭州为例，其它region请按实际情况填写
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        try {
            ossClient.deleteObject(bucket,key);
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","文件删除成功");
            logger.info("result:"+Json.encode(stringMap));
            return Json.encode(stringMap);
        }catch (OSSException oe){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",oe.getMessage());
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/deleteAliyunOSSFile",params,Json.encode(stringMap),"0",oe.getMessage());
            return Json.encode(stringMap);
        }catch (ClientException ce) {
            stringMap.put("data", "");
            stringMap.put("status", 0);
            stringMap.put("msg", ce.getErrorMessage());
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/deleteAliyunOSSFile",params,Json.encode(stringMap),"0",ce.getErrorMessage());
            return Json.encode(stringMap);
        }

    }
}
