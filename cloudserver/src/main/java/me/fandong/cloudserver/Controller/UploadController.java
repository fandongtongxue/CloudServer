package me.fandong.cloudserver.Controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import me.fandong.cloudserver.Model.FilePathModel;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.springframework.web.bind.annotation.RequestMethod;
import com.qiniu.storage.UploadManager;
import com.aliyun.oss.model.UploadFileRequest;
import com.aliyun.oss.model.UploadFileResult;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.ClientException;

@RestController
public class UploadController {
    private StringMap stringMap;

    private static Logger logger = Logger.getLogger(UploadController.class);

    @RequestMapping(value="/uploadImageFile",method=RequestMethod.POST)
    public String uploadImageFile(HttpServletRequest request, MultipartHttpServletRequest multiRequest) throws IOException{
        stringMap = new StringMap();

        //把当前时间戳作为字符串
        String key = System.currentTimeMillis() + ".jpg";
        //测试服务器文件路径13
//        File file = new File("/Users/fandong/Git/CloudServer/cloudserver/src/main/resources/static/"+key);
        //测试服务器文件路径15
//        File file = new File("/Users/fandongtongxue/Documents/GitHub/CloudServer/cloudserver/src/main/resources/static/"+key);
        //正式服务器文件路径(要可直接访问,40GB高效云盘地址以供云盘上传使用)
        //美国服务器
        File file = new File("/home/static/"+key);
        //如果文件路径不存在,创建父目录,创建当前文件路径
        if(!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //输入输出流
        FileOutputStream fos = new FileOutputStream(file);
        FileInputStream fis = (FileInputStream) multiRequest.getFile("imageFile").getInputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = fis.read(buffer)) != -1){
            fos.write(buffer, 0, len);
        }
        fos.close();
        fis.close();

        //上传之后的路径放到Data
        FilePathModel model = new FilePathModel();
        model.setFilePath(file.getPath());
        stringMap.put("data",model);
        stringMap.put("status",1);
        stringMap.put("msg","文件上传成功");
        logger.info("result:"+Json.encode(stringMap));
        return Json.encode(stringMap);
    }

    @RequestMapping(value="/uploadVideoFile",method=RequestMethod.POST)
    public String uploadVideoFile(HttpServletRequest request, MultipartHttpServletRequest multiRequest) throws IOException{
        stringMap = new StringMap();

        //把当前时间戳作为字符串
        String key = System.currentTimeMillis() + ".mov";
        //测试服务器文件路径13
//        File file = new File("/Users/fandong/Git/CloudServer/cloudserver/src/main/resources/static/"+key);
        //测试服务器文件路径15
//        File file = new File("/Users/fandongtongxue/Documents/GitHub/CloudServer/cloudserver/src/main/resources/static/"+key);
        //正式服务器文件路径(要可直接访问,40GB高效云盘地址以供云盘上传使用)
        //美国服务器
        File file = new File("/home/static/"+key);
        //如果文件路径不存在,创建父目录,创建当前文件路径
        if(!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //输入输出流
        FileOutputStream fos = new FileOutputStream(file);
        FileInputStream fis = (FileInputStream) multiRequest.getFile("videoFile").getInputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = fis.read(buffer)) != -1){
            fos.write(buffer, 0, len);
        }
        fos.close();
        fis.close();

        //上传之后的路径放到Data
        FilePathModel model = new FilePathModel();
        model.setFilePath(file.getPath());
        stringMap.put("data",model);
        stringMap.put("status",1);
        stringMap.put("msg","文件上传成功");
        logger.info("result:"+Json.encode(stringMap));
        return Json.encode(stringMap);
    }

    //上传文件之后的上传云
    @RequestMapping(value="/uploadQiniuFile",method=RequestMethod.POST)
    public String uploadQiniuFile(HttpServletRequest request) {
        stringMap = new StringMap();
        String AK = request.getParameter("AK");
        String SK = request.getParameter("SK");
        String bucket = request.getParameter("bucket");
        String key = request.getParameter("key");
        String filePath = request.getParameter("filePath");
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
        if (key == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","key为空");
            return Json.encode(stringMap);
        }
        if (filePath == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","filePath为空");
            return Json.encode(stringMap);
        }
        logger.info("url:"+"/uploadQiniuFile");
        logger.info("params:"+"AK:"+AK+" SK:"+SK+" bucket:"+bucket);
        Auth auth = Auth.create(AK, SK);

        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);

        UploadManager uploadManager = new UploadManager(c);
        try {
            uploadManager.put(filePath,key,auth.uploadToken(bucket));
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","文件上传到七牛云成功");
            logger.info("result:"+Json.encode(stringMap));
            File file = new File(filePath);
            file.delete();
            return Json.encode(stringMap);
        } catch (QiniuException e) {
            Response r = e.response;
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",e.error());
            logger.info("result:"+Json.encode(stringMap));
            return Json.encode(stringMap);
        }
    }

    @RequestMapping(value="/uploadAliyunFile",method=RequestMethod.POST)
    public String uploadAliyunFile(HttpServletRequest request) {
        stringMap = new StringMap();

        String accessKeyId = request.getParameter("accessKeyId");
        String accessKeySecret = request.getParameter("accessKeySecret");
        String bucket = request.getParameter("bucket");
        String endPoint = request.getParameter("endPoint");
        String key = request.getParameter("key");
        String filePath = request.getParameter("filePath");

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
        if (filePath == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","filePath为空");
            return Json.encode(stringMap);
        }

        logger.info("url:"+"/getAliyunOSSFileList");
        logger.info("params:"+"accessKeyId:"+accessKeyId+" accessKeySecret:"+accessKeySecret+" endPoint:"+endPoint+" bucket:"+bucket);

        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);

        try {
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucket, key);
            // The local file to upload---it must exist.
            uploadFileRequest.setUploadFile(filePath);
            // Sets the concurrent upload task number to 5.
            uploadFileRequest.setTaskNum(5);
            // Sets the part size to 1MB.
            uploadFileRequest.setPartSize(1024 * 1024 * 1);
            // Enables the checkpoint file. By default it's off.
            uploadFileRequest.setEnableCheckpoint(true);

            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);

            CompleteMultipartUploadResult multipartUploadResult =
                    uploadResult.getMultipartUploadResult();
            System.out.println(multipartUploadResult.getETag());
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","文件上传到阿里云成功");
            logger.info("result:"+Json.encode(stringMap));
            File file = new File(filePath);
            file.delete();
            return Json.encode(stringMap);

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","文件上传到阿里云失败");
            logger.info("result:"+Json.encode(stringMap));
            return Json.encode(stringMap);
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","文件上传到阿里云失败");
            logger.info("result:"+Json.encode(stringMap));
            return Json.encode(stringMap);
        } catch (Throwable e) {
            e.printStackTrace();
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","文件上传到阿里云失败");
            logger.info("result:"+Json.encode(stringMap));
            return Json.encode(stringMap);
        } finally {
            ossClient.shutdown();
        }
    }
}
