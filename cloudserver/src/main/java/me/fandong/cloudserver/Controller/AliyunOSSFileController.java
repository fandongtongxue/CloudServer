package me.fandong.cloudserver.Controller;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import me.fandong.cloudserver.Model.FileListModel;
import me.fandong.cloudserver.Model.FileModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AliyunOSSFileController {
    private StringMap stringMap;

    @RequestMapping("/getAliyunOSSFileList")
    public String getFileList (HttpServletRequest request){
        stringMap = new StringMap();

        String accessKeyId = request.getParameter("accessKeyId");
        String accessKeySecret = request.getParameter("accessKeySecret");
        String bucket = request.getParameter("bucket");
        String endPoint = request.getParameter("endPoint");
        String keyPrefix = request.getParameter("keyPrefix");
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
        System.out.println(Json.encode(stringMap));

        // endpoint以杭州为例，其它region请按实际情况填写
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        final int maxKeys = 1000;
        String nextMarker = "";
        ObjectListing objectListing = null;
        do {
            try {
                objectListing = ossClient.listObjects(new ListObjectsRequest(bucket).
                        withPrefix(keyPrefix).withMarker(nextMarker).withMaxKeys(maxKeys));
                List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
                ArrayList array = new ArrayList<FileModel>();
                for (OSSObjectSummary s : sums) {
                    FileModel model = new FileModel();
                    model.setFileName(s.getKey());
                    model.setFileSize(s.getSize()/1024 + "KB");
                    model.setFileType("未知");
                    array.add(model);
                }
                nextMarker = objectListing.getNextMarker();
                FileListModel listModel = new FileListModel();
                listModel.setList(array);
                listModel.setMarker(nextMarker);
                stringMap.put("data",listModel);
                stringMap.put("status",1);
                stringMap.put("msg","获取文件列表数据成功");
                // 关闭client
                ossClient.shutdown();
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

        } while (objectListing.isTruncated());
    }

    @RequestMapping("/deleteAliyunOSSFile")
    public String deleteFile (HttpServletRequest request){
        stringMap = new StringMap();

        String accessKeyId = request.getParameter("accessKeyId");
        String accessKeySecret = request.getParameter("accessKeySecret");
        String bucket = request.getParameter("bucket");
        String endPoint = request.getParameter("endPoint");
        String key = request.getParameter("key");

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
        System.out.println(Json.encode(stringMap));

        // endpoint以杭州为例，其它region请按实际情况填写
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        try {
            ossClient.deleteObject(bucket,key);
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","文件删除成功");
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
